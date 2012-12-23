// Fraunhofer Institute for Computer Graphics Research (IGD)
// Department Graphical Information Systems (GIS)
//
// Copyright (c) 2010 Fraunhofer IGD
//
// This file is part of slf4j-plus.
//
// slf4j-plus is free software: you can redistribute
// it and/or modify it under the terms of the GNU LesserGeneral Public License
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

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.fhg.igd.slf4jplus.logback.StatusService;

/**
 * Bundle activator
 * 
 * @author Simon Templer
 */
public class Activator extends AbstractUIPlugin {
	
	/**
	 * The plug-in ID
	 */
	public static final String PLUGIN_ID = "de.fhg.igd.slf4jplus.ui.errorlog"; //$NON-NLS-1$

	/**
	 * The shared instance
	 */
	private static Activator _plugin;
	
	private ServiceTracker<StatusService, StatusService> serviceTracker;
	
	/**
	 * The constructor
	 */
	public Activator() {
		//nothing to do here
	}

	/**
	 * @see AbstractUIPlugin#start(BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		_plugin = this;
		
		serviceTracker = new ServiceTracker<StatusService, StatusService>(context,
				StatusService.class, new StatusServiceTracker());
		serviceTracker.open();
	}

	/**
	 * @see AbstractUIPlugin#stop(BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		serviceTracker.close();
		
		_plugin = null;
		super.stop(context);
	}

	/**
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return _plugin;
	}
	
}
