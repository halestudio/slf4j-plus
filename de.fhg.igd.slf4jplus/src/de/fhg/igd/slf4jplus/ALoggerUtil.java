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

import java.util.Iterator;

import org.slf4j.Marker;

/**
 * {@link ALogger} utility methods
 * 
 * @author Simon Templer
 */
public abstract class ALoggerUtil implements ALoggerConstants {
	
	/**
	 * Determines if the message with the given marker is a user message
	 *  
	 * @param marker the marker
	 * 
	 * @return if the message is a user message
	 */
	public static boolean isUserMessage(Marker marker) {
		if (marker == null) {
			return false;
		}
		
		return marker.contains(USER_MARKER_NAME);
	}
	
	/**
	 * Determines if the message with the given marker is grouped
	 *  
	 * @param marker the marker
	 * 
	 * @return if the message is grouped
	 */
	public static boolean isGrouped(Marker marker) {
		if (marker == null) {
			return false;
		}
		
		return marker.contains(GROUP_MARKER_NAME);
	}
	
	/**
	 * Get the group name for the given marker
	 *  
	 * @param marker the marker
	 * 
	 * @return the group name or <code>null</code> if no such information
	 *  could be found
	 */
	public static String getGroupName(Marker marker) {
		if (marker == null) {
			return null;
		}
		
		if (marker.contains(GROUP_MARKER_NAME)) {
			Marker groupMarker = findMarker(marker, GROUP_MARKER_NAME);
			if (groupMarker != null && groupMarker.hasReferences()) {
				// groupMarker is present, its only reference should be the marker with the group name
				Object nameMarker = groupMarker.iterator().next();
				if (nameMarker instanceof Marker) {
					String name = ((Marker) nameMarker).getName();
					if (name != null && !name.isEmpty()) {
						// valid group name
						return name;
					}
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Determines if the message with the given marker begins a transaction
	 *  
	 * @param marker the marker
	 * 
	 * @return if the message begins a transaction
	 */
	public static boolean beginsTransaction(Marker marker) {
		if (marker == null) {
			return false;
		}
		
		return marker.contains(TRANSACTION_BEGIN_NAME);
	}
	
	/**
	 * Determines if the message with the given marker ends a transaction
	 *  
	 * @param marker the marker
	 * 
	 * @return if the message ends a transaction
	 */
	public static boolean endsTransaction(Marker marker) {
		if (marker == null) {
			return false;
		}
		
		return marker.contains(TRANSACTION_END_NAME);
	}
	
	/**
	 * Get the bundle name for the given marker
	 *  
	 * @param marker the marker
	 * 
	 * @return the bundle name or <code>null</code> if no such information
	 *  could be found
	 */
	public static String getBundleName(Marker marker) {
		if (marker == null) {
			return null;
		}
		
		if (marker.contains(BUNDLE_MARKER_NAME)) {
			Marker bundleMarker = findMarker(marker, BUNDLE_MARKER_NAME);
			if (bundleMarker != null && bundleMarker.hasReferences()) {
				// bundleMarker is present, its only reference should be the marker with the bundle name
				Object nameMarker = bundleMarker.iterator().next();
				if (nameMarker instanceof Marker) {
					String name = ((Marker) nameMarker).getName();
					if (name != null && name.startsWith(BUNDLE_NAME_PREFIX)) {
						// valid bundle name
						return name.substring(BUNDLE_NAME_PREFIX.length());
					}
				}
			}
		}
		
		return null;
	}

	/**
	 * Find the marker with the given name in the given marker or its references
	 * 
	 * @param marker the marker to search
	 * @param name the name of the marker to find
	 * 
	 * @return the found marker or <code>null</code>
	 */
	public static Marker findMarker(Marker marker, String name) {
		if (marker == null) {
			return null;
		}
		
		if (marker.getName().equals(name)) {
			return marker;
		}
		else if (marker.hasReferences()) {
			Iterator<?> refs = marker.iterator();
			while (refs.hasNext()) {
				Object ref = refs.next();
				if (ref instanceof Marker) {
					Marker result = findMarker((Marker) ref, name);
					if (result != null) {
						return result;
					}
				}
			}
			
			return null;
		}
		else {
			return null;
		}
	}

}
