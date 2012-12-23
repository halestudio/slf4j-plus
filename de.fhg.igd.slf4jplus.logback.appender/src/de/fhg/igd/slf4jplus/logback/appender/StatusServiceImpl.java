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

package de.fhg.igd.slf4jplus.logback.appender;

import java.util.HashSet;
import java.util.Set;

import ch.qos.logback.classic.spi.LoggingEvent;
import de.fhg.igd.slf4jplus.logback.LogObserver;
import de.fhg.igd.slf4jplus.logback.StatusService;

/**
 * Log service implementation
 * 
 * @author Simon Templer
 */
public class StatusServiceImpl implements StatusService {
	
	private final Set<LogObserver> observers = new HashSet<LogObserver>();

	/**
	 * @see StatusService#addObserver(LogObserver)
	 */
	@Override
	public void addObserver(LogObserver observer) {
		synchronized (observers) {
			observers.add(observer);
		}
	}

	/**
	 * @see StatusService#removeObserver(LogObserver)
	 */
	@Override
	public void removeObserver(LogObserver observer) {
		synchronized (observers) {
			observers.remove(observer);
		}
	}
	
	/**
	 * Publish the given logging event
	 * 
	 * @param event the event to publish
	 */
	public void publishEvent(LoggingEvent event) {
		synchronized (observers) {
			for (LogObserver observer : observers) {
				observer.onEvent(event);
			}
		}
	}

}
