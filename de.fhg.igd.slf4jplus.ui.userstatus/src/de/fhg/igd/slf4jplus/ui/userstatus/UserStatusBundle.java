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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.fhg.igd.slf4jplus.logback.StatusService;

/**
 * Bundle activator
 * 
 * @author Simon Templer
 */
public class UserStatusBundle extends AbstractUIPlugin {
	
	/**
	 * The plug-in ID
	 */
	public static final String PLUGIN_ID = "de.fhg.igd.slf4jplus.ui.userstatus"; //$NON-NLS-1$

	/**
	 * The shared instance
	 */
	private static UserStatusBundle _plugin;
	
	private ServiceTracker<StatusService, StatusService> serviceTracker;

	private de.fhg.igd.slf4jplus.ui.userstatus.StatusServiceTracker statusTracker;
	
	/**
	 * The constructor
	 */
	public UserStatusBundle() {
		//nothing to do here
	}

	/**
	 * @see AbstractUIPlugin#start(BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		_plugin = this;
		
		statusTracker = new StatusServiceTracker();
		serviceTracker = new ServiceTracker<StatusService, StatusService>(context,
				StatusService.class, statusTracker);
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
	public static UserStatusBundle getDefault() {
		return _plugin;
	}
	
	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	/**
	 * Get the status observer
	 * 
	 * @return the status observer
	 */
	public static StatusObserver getStatusObserver() {
		return _plugin.statusTracker.getObserver();
	}
	
}
