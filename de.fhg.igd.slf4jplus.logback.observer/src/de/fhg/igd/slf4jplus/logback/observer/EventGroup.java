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

import ch.qos.logback.classic.spi.LoggingEvent;

/**
 * Logging event that may contain child events
 * 
 * @author Simon Templer
 */
public interface EventGroup {
	
	/**
	 * Get the group identifier
	 * 
	 * @return the group identifier, may be <code>null</code> 
	 */
	public String getGroupId();
	
	/**
	 * Get the parent group if there is any
	 * 
	 * @return the parent group or <code>null</code>
	 */
	public EventGroup getParent();
	
	/**
	 * Get the event representing the event group
	 * 
	 * @return the logging event
	 */
	public LoggingEvent getEvent();
	
	/**
	 * Determines if the event group has any children
	 *  
	 * @return if there are any children
	 */
	public boolean hasChildren();
	
	/**
	 * Get the child events
	 * 
	 * @return the child events
	 */
	public Iterable<EventGroup> getChildren();

	/**
	 * Determines if this is an event group that contains no concrete events
	 * 
	 * @return if this is an event group contains no concrete events
	 */
	public boolean isEmptyGroup();

}
