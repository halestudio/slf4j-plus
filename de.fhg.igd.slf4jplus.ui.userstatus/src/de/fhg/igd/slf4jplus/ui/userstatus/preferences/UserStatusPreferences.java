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

import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import de.fhg.igd.slf4jplus.ui.userstatus.Status;
import de.fhg.igd.slf4jplus.ui.userstatus.UserStatusBundle;

/**
 * User status preferences defaults and utility methods
 * 
 * @author Simon Templer
 */
public class UserStatusPreferences extends AbstractPreferenceInitializer
	implements UserStatusPreferenceConstants {
	
	private static Properties properties = null;
	
	/**
	 * @see AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore ps = UserStatusBundle.getDefault().getPreferenceStore();
		
		ps.setDefault(ON_ERROR_SHOW_DIALOG, true);
	}

	/**
	 * Get if on an error message a dialog shall pop up instead of the status
	 * item blinking
	 * 
	 * @return if a dialog shall be shown for an error message
	 */
	public static boolean getOnErrorShowDialog() {
		IPreferenceStore ps = UserStatusBundle.getDefault().getPreferenceStore();
		
		return ps.getBoolean(ON_ERROR_SHOW_DIALOG);
	}
	
	/**
	 * Set if on an error message a dialog shall pop up instead of the status
	 * item blinking
	 * 
	 * @param value if a dialog shall be shown for an error message
	 */
	public static void setOnErrorShowDialog(boolean value) {
		IPreferenceStore ps = UserStatusBundle.getDefault().getPreferenceStore();
		
		ps.setValue(ON_ERROR_SHOW_DIALOG, value);
	}
	
	/**
	 * Get the minimum alert level
	 * 
	 * @return the minimum alert level
	 */
	public static Status getAlertLevel() {
		Status alertLevel = null;
		Properties props = getProperties();
		if (props.containsKey(PROPERTY_ALERTLEVEL)) {
			alertLevel = Status.valueOf(props.getProperty(PROPERTY_ALERTLEVEL));
		}
		
		// default
		if (alertLevel == null) {
			alertLevel = Status.ERROR;
		}
		
		return alertLevel;
	}
		
	/**
	 * Get the user status configuration properties
	 * 
	 * @return the configuration properties
	 */
	private static Properties getProperties() {
		if (properties == null) {
			properties = new Properties();
			try {
				Enumeration<URL> entries = UserStatusBundle.getDefault().getBundle().findEntries("", USERSTATUS_PROPERTIES_FILE, false); //$NON-NLS-1$
				if (entries != null && entries.hasMoreElements()) {
					URL configURL = entries.nextElement();
					properties.load(configURL.openStream());
				}
			} catch (Throwable e) {
				// ignore
			}
		}
		
		return properties;
	}

	/**
	 * Get if a link to the error log shall be shown in message dialogs
	 * 
	 * @return if the error log link shall be shown
	 */
	public static boolean getShowLogLink() {
		return Boolean.parseBoolean(getProperties().getProperty("loglink", "false")); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
}
