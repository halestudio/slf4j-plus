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

import java.util.List;

import ch.qos.logback.classic.spi.LoggingEvent;

/**
 * Represents a group of messages
 * 
 * @author Simon Templer
 */
public class MessageGroup {
	
	private final String name;
	
	private final List<LoggingEvent> messages;

	/**
	 * Create a message group
	 * 
	 * @param name the group name
	 * @param messages the messages
	 */
	public MessageGroup(String name, List<LoggingEvent> messages) {
		super();
		this.name = name;
		this.messages = messages;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the messages
	 */
	public List<LoggingEvent> getMessages() {
		return messages;
	}

}
