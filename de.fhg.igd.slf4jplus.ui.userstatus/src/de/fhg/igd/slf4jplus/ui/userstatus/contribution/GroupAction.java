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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;

import de.fhg.igd.slf4jplus.ui.userstatus.MessageGroup;

/**
 * Action for message groups
 * 
 * @author Simon Templer
 */
public class GroupAction extends Action implements IMenuCreator {
	
	private Menu menu;
	
	private final List<IContributionItem> items = new ArrayList<IContributionItem>();

	/**
	 * Constructor
	 * 
	 * @param group the message group
	 */
	public GroupAction(MessageGroup group) {
		super(group.getName() + "@" + group.getMessages().size(),  //$NON-NLS-1$
				IAction.AS_DROP_DOWN_MENU);
		
		setMenuCreator(this);
		
		Level maxLevel = null;
		for (LoggingEvent event : group.getMessages()) {
			IAction action = new MessageAction(event);
			IContributionItem item = new ActionContributionItem(action);
			
			items.add(item);
			
			if (maxLevel == null) {
				maxLevel = event.getLevel();
			}
			else {
				maxLevel = getMaxLevel(maxLevel, event.getLevel());
			}
		}
		
		setImageDescriptor(StatusContribution.getImageDescriptor(maxLevel));
	}
	
	/**
	 * Compare two levels and return the greater
	 * 
	 * @param level1 the first level
	 * @param level2 the second level
	 * 
	 * @return the highest level
	 */
	private Level getMaxLevel(Level level1, Level level2) {
		int l1 = level1.toInt();
		int l2 = level2.toInt();
		
		return Level.toLevel(Math.max(l1, l2));
	}

	/**
	 * @see IMenuCreator#dispose()
	 */
	@Override
	public void dispose() {
		if (menu != null) {
			menu.dispose();
		}
	}

	/**
	 * @see IMenuCreator#getMenu(Control)
	 */
	@Override
	public Menu getMenu(Control parent) {
		dispose();
		
		menu = new Menu(parent);
		fillMenu(menu);
		
		return menu;
	}

	/**
	 * Fill the menu
	 * 
	 * @param menu the menu to fill
	 */
	protected void fillMenu(Menu menu) {
		for (int i = 0; i < items.size(); i++) {
			items.get(i).fill(menu, i);
		}
	}

	/**
	 * @see IMenuCreator#getMenu(org.eclipse.swt.widgets.Menu)
	 */
	@Override
	public Menu getMenu(Menu parent) {
		dispose();
		
		menu = new Menu(parent);
		fillMenu(menu);
		
		return menu;
	}
	
}
