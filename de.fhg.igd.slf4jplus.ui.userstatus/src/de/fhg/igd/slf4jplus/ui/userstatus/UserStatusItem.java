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

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

/**
 * User Status item
 * 
 * @author Simon Templer
 */
public class UserStatusItem extends WorkbenchWindowControlContribution {

	/**
	 * Default constructor
	 */
	public UserStatusItem() {
		super();
	}

	/**
	 * @see WorkbenchWindowControlContribution#WorkbenchWindowControlContribution(String)
	 */
	public UserStatusItem(String id) {
		super(id);
	}

	/**
	 * @see ControlContribution#createControl(Composite)
	 */
	@Override
	protected Control createControl(Composite parent) {
		Composite page = new Composite(parent, SWT.NONE);
		
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 3;
		gridLayout.marginWidth = 0;
		gridLayout.marginLeft = 0;
		gridLayout.marginRight = 7;
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		page.setLayout(gridLayout);
		
		StatusControl status = new StatusControl(page, 
				UserStatusBundle.getStatusObserver());
		GridData gd = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		gd.heightHint = 16;
		gd.widthHint = 16;
		status.setLayoutData(gd);
		
		return page;
	}

}
