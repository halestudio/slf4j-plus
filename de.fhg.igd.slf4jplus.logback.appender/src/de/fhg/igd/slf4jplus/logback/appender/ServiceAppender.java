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

import java.util.Hashtable;

import org.osgi.framework.ServiceRegistration;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import de.fhg.igd.slf4jplus.logback.Activator;
import de.fhg.igd.slf4jplus.logback.StatusService;

/**
 * Appender that published logging events to a service
 * 
 * @author Simon Templer
 */
public class ServiceAppender extends AppenderBase<LoggingEvent> {
	
	private final StatusServiceImpl status = new StatusServiceImpl();
	
	private ServiceRegistration<StatusService> serviceReg;

	/**
	 * @see AppenderBase#start()
	 */
	@Override
	public void start() {
		super.start();
		
		// publish OSGi server
		serviceReg = Activator.getContext().registerService(StatusService.class, status,
				new Hashtable<String, Object>());
	}

	/**
	 * @see AppenderBase#stop()
	 */
	@Override
	public void stop() {
		// unpublish OSGi service
		serviceReg.unregister();
		
		super.stop();
	}

	/**
	 * @see AppenderBase#append(Object)
	 */
	@Override
	protected void append(LoggingEvent event) {
		status.publishEvent(event);
	}

}
