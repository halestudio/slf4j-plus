// Fraunhofer Institute for Computer Graphics Research (IGD)
// Department Graphical Information Systems (GIS)
//
// Copyright (c) 2015 Fraunhofer IGD
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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * MultiStatus that allows setting all its children at once.
 * 
 * @author Simon Templer
 */
public class CustomMultiStatus extends Status {

	private IStatus[] children;
	
	/**
	 * Create a multi-status.
	 * 
	 * @param pluginId the unique identifier of the relevant plug-in
	 * @param code the plug-in-specific status code, or <code>OK</code>
	 * @param message a human-readable message, localized to the
	 *    current locale
	 * @param exception a low-level exception, or <code>null</code> if not
	 *    applicable 
	 */
	public CustomMultiStatus(String pluginId, int code,
			String message, Throwable exception) {
		super(OK, pluginId, code, message, exception);
	}

	/**
	 * Set the given status array as the status' children
	 * 
	 * @param children the children to set
	 */
	public void setChildren(IStatus[] children) {
		this.children = children;
		for (IStatus child : children) {
			if (child.getSeverity() > getSeverity()) {
				setSeverity(child.getSeverity());
			}
		}
	}

	@Override
	public IStatus[] getChildren() {
		return children;
	}

	@Override
	public boolean isMultiStatus() {
		return true;
	}

}
