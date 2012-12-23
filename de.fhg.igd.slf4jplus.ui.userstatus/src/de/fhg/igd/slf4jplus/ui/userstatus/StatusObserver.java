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

package de.fhg.igd.slf4jplus.ui.userstatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import de.fhg.igd.slf4jplus.ALoggerUtil;
import de.fhg.igd.slf4jplus.logback.observer.FlatLogObserver;

/**
 * Log observer that tracks user messages
 * 
 * @author Simon Templer
 */
public class StatusObserver extends FlatLogObserver {
	
	private final Set<StatusListener> listeners = new HashSet<StatusListener>();
	
	private final HashMap<String, List<LoggingEvent>> userEvents = new LinkedHashMap<String, List<LoggingEvent>>();
	
	private Status currentStatus = Status.OK;
	
	private int newMessages = 0;
	
	private int messages = 0;

	/**
	 * @see FlatLogObserver#processEvent(LoggingEvent)
	 */
	@Override
	protected void processEvent(LoggingEvent event) {
		if (ALoggerUtil.isUserMessage(event.getMarker())) {
			// event is a user message
			String group = ALoggerUtil.getGroupName(event.getMarker());
			if (group == null) {
				group = event.getFormattedMessage();
			}
			
			add(group, event);
		}
	}
	
	/**
	 * Set the current status
	 * 
	 * @param status the status
	 */
	private void setStatus(Status status) {
		this.currentStatus = status;
	}

	/**
	 * Get the current status
	 * 
	 * @return the current status
	 */
	public Status getStatus() {
		return currentStatus;
	}

	/**
	 * @return the if there are new messages
	 */
	public int getNewMessageCount() {
		return newMessages;
	}
	
	/**
	 * Get the message count
	 * 
	 * @return the message count
	 */
	public int getMessageCount() {
		return messages;
	}

	/**
	 * Called when the status has changed
	 * 
	 * @param event the logging event associated with the status change, may be
	 *   <code>null</code> 
	 */
	protected void notifyListeners(LoggingEvent event) {
		synchronized (listeners) {
			for (StatusListener listener : listeners) {
				listener.onStatusChanged(getStatus(), getNewMessageCount(),
						getMessageCount(), event);
			}
		}
	}

	/**
	 * Add an event
	 * 
	 * @param group the group name
	 * @param event the event to add 
	 */
	private synchronized void add(String group, LoggingEvent event) {
		List<LoggingEvent> events = userEvents.get(group);
		if (events == null) {
			events = new ArrayList<LoggingEvent>();
			userEvents.put(group, events);
		}
		
		events.add(event);
		
		updateStatus(event);
		newMessages++;
		messages++;
		
		notifyListeners(event);
	}
	
	/**
	 * Mark all messages read
	 */
	public synchronized void markRead() {
		newMessages = 0;
		
		notifyListeners(null);
	}
	
	/**
	 * Clear the message log
	 */
	public synchronized void clear() {
		userEvents.clear();
		
		messages = 0;
		newMessages = 0;
		setStatus(Status.OK);
		
		notifyListeners(null);
	}

	/**
	 * Update the status
	 * 
	 * @param event the event that has just been added
	 */
	private void updateStatus(LoggingEvent event) {
		Level level = event.getLevel();
		Status eventStatus;
		
		if (level == Level.ERROR) {
			eventStatus = Status.ERROR;
		}
		else if (level == Level.WARN) {
			eventStatus = Status.WARN;
		}
		else {
			// default to info
			eventStatus = Status.INFO;
		}
		
		setStatus(getHighestStatus(eventStatus, getStatus()));
	}

	/**
	 * Get the highest status of the given status objects
	 * 
	 * @param status1 the first status
	 * @param status2 the second status
	 * 
	 * @return the highest status of both
	 */
	private Status getHighestStatus(Status status1, Status status2) {
		int cp = status1.compareTo(status2);
		if (cp < 0) {
			return status2;
		}
		else {
			return status1;
		}
	}

	/**
	 * Add a listener
	 * 
	 * @param listener the listener to add
	 */
	public void addListener(StatusListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
	
	/**
	 * Remove a listener
	 * 
	 * @param listener the listener to remove
	 */
	public void removeListener(StatusListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}
	
	/**
	 * Get the current messages
	 * 
	 * @return the current messages
	 */
	public synchronized List<MessageGroup> getMessages() {
		List<MessageGroup> groups = new ArrayList<MessageGroup>(userEvents.size());
		
		for (Entry<String, List<LoggingEvent>> entry : userEvents.entrySet()) {
			MessageGroup group = new MessageGroup(
					entry.getKey(), 
					new ArrayList<LoggingEvent>(entry.getValue()));
			groups.add(group);
		}
		
		return groups;
	}

}
