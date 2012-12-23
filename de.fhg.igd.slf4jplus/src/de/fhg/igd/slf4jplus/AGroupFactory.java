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

import java.util.HashMap;
import java.util.Map;

import de.fhg.igd.slf4jplus.internal.AGroupImpl;

/**
 * Factory for log groups
 * 
 * @author Simon Templer
 */
public abstract class AGroupFactory {
	
	private static final Map<String, AGroup> groups = new HashMap<String, AGroup>();
	
	/**
	 * Get the group with the given name
	 * 
	 * @param name the group name which is also its message
	 * 
	 * @return the group
	 */
	public static AGroup getGroup(String name) {
		AGroup group;
		synchronized (groups) {
			group = groups.get(name);
			if (group == null) {
				group = new AGroupImpl(name);
				groups.put(name, group);
			}
		}
		return group;
	}

}
