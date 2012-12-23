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

package de.fhg.igd.slf4jplus.ui.userstatus.preferences;

/**
 * Preference constants
 * 
 * @author Simon Templer
 */
public interface UserStatusPreferenceConstants {
	
	/**
	 * The preference name for the setting if a dialog is shown for a new error
	 * instead of the status item just blinking
	 */
	public static final String ON_ERROR_SHOW_DIALOG = "onErrorShowDialog"; //$NON-NLS-1$
	
	/**
	 * Alert level property name for configuration file
	 */
	public static final String PROPERTY_ALERTLEVEL = "alertlevel"; //$NON-NLS-1$
	
	/**
	 * Name of the configuration file 
	 */
	public static final String USERSTATUS_PROPERTIES_FILE = "userstatus.properties"; //$NON-NLS-1$

}
