// Fraunhofer Institute for Computer Graphics Research (IGD)
// Department Graphical Information Systems (GIS)
//
// Copyright (c) 2010 Fraunhofer IGD
//
// This file is part of slf4j-plus.
//
// slf4j-plus is free software: you can redistribute
// it and/or modify it under the terms of the GNU Lesser General Public License
// as published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// slf4j-plus is distributed in the hope that it will
// be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with slf4j-plus.
// If not, see <http://www.gnu.org/licenses/>.

package de.fhg.igd.slf4jplus.logback.observer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import ch.qos.logback.classic.spi.LoggingEvent;
import de.fhg.igd.slf4jplus.ALoggerConstants;
import de.fhg.igd.slf4jplus.ALoggerUtil;
import de.fhg.igd.slf4jplus.logback.LogObserver;
import de.fhg.igd.slf4jplus.logback.observer.internal.EventGroupImpl;

/**
 * Log observer that provides basic transaction and group handling
 * functionality
 * 
 * @author Simon Templer
 */
public abstract class GroupAwareLogObserver implements LogObserver, ALoggerConstants {
	
	private final Map<String, EventGroupImpl> openTransactions = new HashMap<String, EventGroupImpl>();

	/**
	 * @see LogObserver#onEvent(LoggingEvent)
	 */
	@Override
	public void onEvent(LoggingEvent event) {
		if (!acceptRawEvent(event)) {
			return;
		}
		
		// transactions
		String transactions;
		if (event.getMDCPropertyMap() != null) {
			transactions = event.getMDCPropertyMap().get(MDC_TRANSACTIONS);
		}
		else {
			transactions = null;
		}
		
		String[] transPath;
		if (transactions != null && !transactions.isEmpty()) {
			transPath = transactions.split(MDC_TRANSACTIONS_SEPARATOR);
		}
		else {
			transPath = null;
		}
		
		// get group information
		String groupName = null;
		if (transPath != null && transPath.length > 0
				&& ALoggerUtil.isGrouped(event.getMarker())) {
			// groups only enabled inside transactions
			groupName = ALoggerUtil.getGroupName(event.getMarker());
			if (groupName != null) {
				StringBuffer id = new StringBuffer();
				String[] groupPath = new String[transPath.length + 1];
				for (int i = 0; i < transPath.length; i++) {
					// append trans path to id
					if (i != 0) {
						id.append(MDC_TRANSACTIONS_SEPARATOR);
					}
					id.append(transPath[i]);
					
					// copy to new path
					groupPath[i] = transPath[i];
				}
				
				id.append(MDC_TRANSACTIONS_SEPARATOR);
				id.append(groupName);
				
				groupPath[groupPath.length - 1] = id.toString();
				
				transPath = groupPath;
			}
		}
		
		// get group object
		EventGroupImpl group;
		if (transPath != null && transPath.length > 0) {
			group = getGroup(transPath);
			if (groupName != null && group.getEvent() == null) {
				// set group event if not already set
				LoggingEvent groupEvent = new LoggingEvent();
				groupEvent.setMessage(groupName);
				groupEvent.setMarker(event.getMarker());
				group.setEvent(groupEvent);
			}
		}
		else {
			group = null;
		}
		
		// handle different message types
		if (ALoggerUtil.beginsTransaction(event.getMarker())) {
			if (group != null) {
				group.setEvent(event);
			}
			// else: ignore event
		}
		else if (ALoggerUtil.endsTransaction(event.getMarker())) {
			if (group != null) {
				// close group and process it
				closeGroups(group);
				
				if (group.getParent() == null) {
					if (!group.hasChildren()) {
						/*
						 * don't process empty groups because their event level
						 * should always be defined by their contents XXX this ok?
						 */
					}
					else {
						processEvents(group);
					}
				}
			}
			// else: ignore event
		}
		else {
			// normal message
			if (acceptMessageEvent(event)) {
				if (group != null) {
					// add event to group
					group.add(event);
				}
				else {
					// process single event
					processEvent(event);
				}
			}
		}
	}
	
	/**
	 * Specifies if an event that represents a normal message shall be further 
	 * processed. If the message is part of a group/transaction it would be
	 * added to the corresponding group, otherwise it would be handled by
	 * {@link #processEvent(LoggingEvent)} 
	 * The default behavior is to accept all events. Override this method for
	 * a different behavior.
	 * 
	 * @param event the logging event
	 * 
	 * @return if the event shall be further processed
	 */
	protected boolean acceptMessageEvent(LoggingEvent event) {
		return true;
	}

	/**
	 * Specifies if an event that has not yet been checked for transaction
	 * or group markers shall be processed.
	 * The default behavior is to accept all events. Override this method for
	 * a different behavior.
	 * 
	 * @param event the logging event
	 * 
	 * @return if the event shall be further processed
	 */
	protected boolean acceptRawEvent(LoggingEvent event) {
		return true;
	}

	/**
	 * Close the given group and it children
	 * 
	 * @param group the group to close
	 */
	private void closeGroups(EventGroup group) {
		synchronized (openTransactions) {
			Queue<EventGroup> groups = new LinkedList<EventGroup>();
			groups.add(group);
			
			while (!groups.isEmpty()) {
				EventGroup g = groups.poll();
				
				if (g.getGroupId() != null) {
					openTransactions.remove(g.getGroupId());
					
					if (g.getParent() != null && g.isEmptyGroup()) {
						// remove empty groups from their parents
						((EventGroupImpl) g.getParent()).removeChild(g);
					}
				}
				
				if (g.hasChildren()) {
					for (EventGroup child : g.getChildren()) {
						groups.add(child);
					}
				}
			}
		}
	}

	/**
	 * Get the event group for a transaction path
	 * 
	 * @param transPath the group/transaction identifiers
	 * 
	 * @return the event group
	 */
	private EventGroupImpl getGroup(String[] transPath) {
		synchronized (openTransactions) {
			int foundAt = -1;
			EventGroupImpl group = null;
			for (int i = transPath.length - 1; i >= 0 && foundAt < 0; i--) {
				String id = transPath[i];
				group = openTransactions.get(id);
				if (group != null) {
					foundAt = i;
				}
			}
			
			// foundAt specifies the index of the last group that was found
			if (foundAt == transPath.length - 1) {
				// group already exists
				return group;
			}
			else {
				// groups have to be created
				for (int i = foundAt + 1; i < transPath.length; i++) {
					// create group, previous is parent
					EventGroupImpl parent = group;
					group = new EventGroupImpl(parent);
					if (parent != null) {
						parent.add(group);
					}
					String id = transPath[i];
					group.setGroupId(id);
					openTransactions.put(id, group);
				}
				
				// return the last group created
				return group;
			}
		}
	}

	/**
	 * Process a single logging event
	 * 
	 * @param event the logging event
	 */
	protected abstract void processEvent(LoggingEvent event);
	
	/**
	 * Process a group of (completed) events
	 * 
	 * @param events the event group
	 */
	protected abstract void processEvents(EventGroup events);

}
