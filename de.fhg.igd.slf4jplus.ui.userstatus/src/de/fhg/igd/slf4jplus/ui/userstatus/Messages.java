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

import org.eclipse.osgi.util.NLS;

/**
 * Messages
 * 
 * @author Simon Templer
 */
@SuppressWarnings("all")
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "de.fhg.igd.slf4jplus.ui.userstatus.messages"; //$NON-NLS-1$
	public static String ClearAction_0;
	public static String MessageAction_0;
	public static String MessageAction_1;
	public static String MessageAction_2;
	public static String MessageAction_3;
	public static String OnErrorBehaviourAction_0;
	public static String OnErrorBehaviourAction_1;
	public static String StatusContribution_0;
	public static String StatusControl_2;
	public static String StatusControl_3;
	public static String StatusControl_4;
	public static String StatusControl_5;
	public static String StatusControl_6;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
