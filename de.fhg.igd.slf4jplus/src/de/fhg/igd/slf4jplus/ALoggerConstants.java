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

package de.fhg.igd.slf4jplus;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Enhanced logger constants
 * 
 * @author Simon Templer
 */
public interface ALoggerConstants {
	
	/**
	 * Marker name for user messages
	 */
	public static final String USER_MARKER_NAME = "USER";
	
	/**
	 * Marker for user messages
	 */
	public static final Marker USER_MESSAGE = MarkerFactory.getMarker(USER_MARKER_NAME);
	
	/**
	 * Marker name for transaction begin
	 */
	public static final String TRANSACTION_BEGIN_NAME = "TRANSACTION_BEGIN";
	
	/**
	 * Marker for transaction begin
	 */
	public static final Marker TRANSACTION_BEGIN = MarkerFactory.getMarker(TRANSACTION_BEGIN_NAME);
	
	/**
	 * Marker name for transaction end
	 */
	public static final String TRANSACTION_END_NAME = "TRANSACTION_END";
	
	/**
	 * Marker for transaction end
	 */
	public static final Marker TRANSACTION_END = MarkerFactory.getMarker(TRANSACTION_END_NAME);
	
	/**
	 * Name of the MDC property containing the open transactions
	 */
	public static final String MDC_TRANSACTIONS = "TRANSACTIONS";
	
	/**
	 * Separator of transactions identifiers in the MDC transactions property
	 */
	public static final String MDC_TRANSACTIONS_SEPARATOR = ";";
	
	/**
	 * Marker name for messages that define a new group
	 */
	public static final String GROUP_MARKER_NAME = "GROUP";
	
	/**
	 * Marker name for the marker referencing the marker with the bundle name
	 */
	public static final String BUNDLE_MARKER_NAME = "BUNDLE";
	
	/**
	 * Prefix for bundle name marker names
	 */
	public static final String BUNDLE_NAME_PREFIX = "BUNDLE_";
	
	/**
	 * Marker name of a marker that has no meaning by itself but references
	 * other markers
	 */
	public static final String WRAPPER_MARKER_NAME = "WRAPPER";

}
