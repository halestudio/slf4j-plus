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

import java.util.List;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;

import de.fhg.igd.slf4jplus.ui.userstatus.MessageGroup;
import de.fhg.igd.slf4jplus.ui.userstatus.Messages;
import de.fhg.igd.slf4jplus.ui.userstatus.StatusControl;
import de.fhg.igd.slf4jplus.ui.userstatus.StatusObserver;

/**
 * Status observer contribution item
 * 
 * @author Simon Templer
 */
public class StatusContribution extends ContributionItem {

	private final StatusObserver observer;
	
	private final StatusControl control;
	
	private final IContributionItem clearItem;
	
	/**
	 * Create a status contribution item
	 * 
	 * @param observer the status observer 
	 * @param control the status control
	 */
	public StatusContribution(StatusObserver observer, StatusControl control) {
		super();
		
		this.observer = observer;
		this.control = control;
		
		IAction clearAction = new ClearAction(observer);
		clearItem = new ActionContributionItem(clearAction);
	}

	/**
	 * @see ContributionItem#fill(Menu, int)
	 */
	@Override
	public void fill(Menu menu, int index) {
		List<MessageGroup> groups = observer.getMessages();
		
		if (!groups.isEmpty()) {
			for (MessageGroup group : groups) {
				List<LoggingEvent> messages = group.getMessages();
				if (messages.size() == 1) {
					// single message
					IAction action = new MessageAction(messages.get(0));
					IContributionItem item = new ActionContributionItem(action);
					item.fill(menu, index++);
				}
				else if (messages.size() > 1) {
					// multiple messages
					IAction action = new GroupAction(group);
					IContributionItem item = new ActionContributionItem(action);
					item.fill(menu, index++);
				}
			}
			
			new Separator().fill(menu, index++);
			
			index = addOptions(menu, index);
			
			clearItem.fill(menu, index++);
		}
		else {
			index = addOptions(menu, index);
		}
	}

	/**
	 * Add options to the menu
	 * 
	 * @param menu the menu
	 * @param index the index to insert the menu items at
	 * 
	 * @return the index after the inserted items
	 */
	private int addOptions(Menu menu, int index) {
		IMenuManager errorHandling = new MenuManager(Messages.StatusContribution_0);
		
		IAction dialogAction = new OnErrorBehaviourAction(control, true);
		IAction blinkAction = new OnErrorBehaviourAction(control, false);
		
		errorHandling.add(dialogAction);
		errorHandling.add(blinkAction);
		
		errorHandling.fill(menu, index++);
		
		return index;
	}

	/**
	 * @see ContributionItem#isDynamic()
	 */
	@Override
	public boolean isDynamic() {
		return true;
	}

	/**
	 * Get an image descriptor for the given level
	 * 
	 * @param level the level
	 * 
	 * @return the image descriptor or <code>null</code> if no appropriate image
	 *   was found
	 */
	public static ImageDescriptor getImageDescriptor(Level level) {
		String key;
		
		if (level == Level.INFO) {
			key = ISharedImages.IMG_OBJS_INFO_TSK;
		}
		else if (level == Level.WARN) {
			key = ISharedImages.IMG_OBJS_WARN_TSK;
		}
		else if (level == Level.ERROR) {
			key = ISharedImages.IMG_OBJS_ERROR_TSK;
		}
		else {
			key = null;
		}
		
		if (key != null) {
			return PlatformUI.getWorkbench().getSharedImages().
					getImageDescriptor(key);
		}
		else {
			return null;
		}
	}

}
