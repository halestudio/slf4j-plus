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

package de.fhg.igd.slf4jplus.ui.errorlog;

import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import de.fhg.igd.slf4jplus.logback.LogObserver;
import de.fhg.igd.slf4jplus.logback.StatusService;

/**
 * Status service tracker
 * 
 * @author Simon Templer
 */
public class StatusServiceTracker implements ServiceTrackerCustomizer<StatusService, StatusService> {
	
	private final LogObserver observer = new ErrorLogObserver();

	/**
	 * @see ServiceTrackerCustomizer#addingService(ServiceReference)
	 */
	@Override
	public StatusService addingService(ServiceReference<StatusService> reference) {
		StatusService ss = reference.getBundle().getBundleContext().getService(reference);
		ss.addObserver(observer);
		return ss;
	}

	/**
	 * @see ServiceTrackerCustomizer#modifiedService(ServiceReference, Object)
	 */
	@Override
	public void modifiedService(ServiceReference<StatusService> reference, StatusService service) {
		// do nothing
	}

	/**
	 * @see ServiceTrackerCustomizer#removedService(ServiceReference, Object)
	 */
	@Override
	public void removedService(ServiceReference<StatusService> reference, StatusService service) {
		service.removeObserver(observer);
	}

}
