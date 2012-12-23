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

package de.fhg.igd.slf4jplus.ui.userstatus.contribution;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

import de.fhg.igd.slf4jplus.ui.userstatus.Messages;
import de.fhg.igd.slf4jplus.ui.userstatus.StatusObserver;

/**
 * Action that clears the messages of a status observer
 * 
 * @author Simon Templer
 */
public class ClearAction extends Action {
	
	private final StatusObserver observer;

	/**
	 * Constructor
	 * 
	 * @param observer the status observer
	 */
	public ClearAction(StatusObserver observer) {
		super(Messages.ClearAction_0, IAction.AS_PUSH_BUTTON);
		
		this.observer = observer;
	}

	/**
	 * @see Action#run()
	 */
	@Override
	public void run() {
		observer.clear();
	}

}
