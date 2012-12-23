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

package de.fhg.igd.slf4jplus.logback.observer.internal;

import java.util.ArrayList;
import java.util.List;

import ch.qos.logback.classic.spi.LoggingEvent;

import de.fhg.igd.slf4jplus.logback.observer.EventGroup;

/**
 * Event group
 * 
 * @author Simon Templer
 */
public class EventGroupImpl implements EventGroup {
	
	private String groupId;
	
	private LoggingEvent event;
	
	private List<EventGroup> children;
	
	private final EventGroup parent;

	/**
	 * Create an event group
	 * 
	 * @param parent the parent group
	 */
	public EventGroupImpl(EventGroup parent) {
		super();
		
		this.parent = parent;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * @see EventGroup#getParent()
	 */
	@Override
	public EventGroup getParent() {
		return parent;
	}

	/**
	 * @see EventGroup#getGroupId()
	 */
	@Override
	public String getGroupId() {
		return groupId;
	}

	/**
	 * @param event the event to set
	 */
	public void setEvent(LoggingEvent event) {
		this.event = event;
	}

	/**
	 * @see EventGroup#getChildren()
	 */
	@Override
	public Iterable<EventGroup> getChildren() {
		return children;
	}

	/**
	 * @see EventGroup#getEvent()
	 */
	@Override
	public LoggingEvent getEvent() {
		return event;
	}

	/**
	 * @see EventGroup#hasChildren()
	 */
	@Override
	public boolean hasChildren() {
		return children != null && !children.isEmpty();
	}

	/**
	 * Add a logging event
	 * 
	 * @param event the event to add
	 */
	public void add(LoggingEvent event) {
		if (children == null) {
			children = new ArrayList<EventGroup>();
		}
		
		EventGroupImpl group = new EventGroupImpl(this);
		group.setEvent(event);
		children.add(group);
	}

	/**
	 * Add an event group
	 * 
	 * @param group the event group
	 */
	public void add(EventGroup group) {
		if (children == null) {
			children = new ArrayList<EventGroup>();
		}
		
		children.add(group);
	}

	/**
	 * Remove the given child
	 * 
	 * @param child the child to remove
	 */
	public void removeChild(EventGroup child) {
		children.remove(child);
	}

	/**
	 * @see EventGroup#isEmptyGroup()
	 */
	@Override
	public boolean isEmptyGroup() {
		if (getGroupId() != null) {
			// is event group
			if (!hasChildren()) {
				return true;
			}
			else {
				for (EventGroup child : getChildren()) {
					if (child.isEmptyGroup())  {
						return true;
					}
				}
				
				return false;
			}
		}
		else {
			return false;
		}
	}

}
