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
import de.fhg.igd.slf4jplus.ALoggerUtil;
import de.fhg.igd.slf4jplus.logback.LogObserver;

/**
 * Log observer that ignores any group or transaction informations
 * 
 * @author Simon Templer
 */
public abstract class FlatLogObserver implements LogObserver {

	/**
	 * @see LogObserver#onEvent(LoggingEvent)
	 */
	@Override
	public void onEvent(LoggingEvent event) {
		if (ALoggerUtil.beginsTransaction(event.getMarker())
				|| ALoggerUtil.endsTransaction(event.getMarker())) {
			// ignore transaction markers
			return;
		}
		
		processEvent(event);
	}

	/**
	 * Process a single log event
	 * 
	 * @param event the log event
	 */
	protected abstract void processEvent(LoggingEvent event);

}
