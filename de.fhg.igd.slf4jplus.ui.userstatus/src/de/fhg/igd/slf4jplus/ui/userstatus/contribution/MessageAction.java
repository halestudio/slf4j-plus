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

import java.text.DateFormat;
import java.util.Date;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Display;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import de.fhg.igd.slf4jplus.ALoggerUtil;
import de.fhg.igd.slf4jplus.ui.userstatus.Messages;
import de.fhg.igd.slf4jplus.ui.userstatus.StackTraceErrorDialog;
import de.fhg.igd.slf4jplus.ui.userstatus.preferences.UserStatusPreferences;

/**
 * Action for a single message
 * 
 * @author Simon Templer
 */
public class MessageAction extends Action {

	private final LoggingEvent event;
	
	private static final DateFormat format = DateFormat.getTimeInstance(DateFormat.MEDIUM); 
	
	/**
	 * Constructor
	 * 
	 * @param event the message event
	 */
	public MessageAction(LoggingEvent event) {
		super(getText(event), IAction.AS_PUSH_BUTTON);
		
		setImageDescriptor(StatusContribution.getImageDescriptor(event.getLevel()));
		
		this.event = event;
	}

	/**
	 * Get the text for the menu item
	 * 
	 * @param event the logging event
	 * 
	 * @return the menu item text
	 */
	private static String getText(LoggingEvent event) {
		String time = format.format(new Date(event.getTimeStamp()));
		return event.getFormattedMessage() + "@" + time; //$NON-NLS-1$
	}

	/**
	 * @see Action#run()
	 */
	@Override
	public void run() {
		showErrorDialog(event);
	}
	
	/**
	 * Show an error dialog for the given event
	 * 
	 * @param event the logging event
	 */
	public static void showErrorDialog(LoggingEvent event) {
		IStatus status = createStatus(event);
		
		/*ErrorDialog.openError(Display.getCurrent().getActiveShell(), 
				null, null, status);*/
		
		StackTraceErrorDialog dialog = new StackTraceErrorDialog(
				Display.getCurrent().getActiveShell(), 
				getTitle(status), 
				null, 
				status, 
				IStatus.OK | IStatus.INFO | IStatus.WARNING | IStatus.ERROR);
		dialog.setShowErrorLogLink(UserStatusPreferences.getShowLogLink());
		
		dialog.open();
	}

	private static String getTitle(IStatus status) {
		switch (status.getSeverity()) {
			case IStatus.INFO:
				return Messages.MessageAction_1;
			case IStatus.WARNING:
				return Messages.MessageAction_2;
			case IStatus.ERROR:
				return Messages.MessageAction_3;
		}
		
		return null;
	}

	/**
	 * Create an {@link IStatus} from the given event
	 * 
	 * @param event the logging event
	 * 
	 * @return the {@link IStatus} or <code>null</code>
	 */
	private static IStatus createStatus(LoggingEvent event) {
		// pluginId
		String pluginId = ALoggerUtil.getBundleName(event.getMarker());
		if (pluginId == null) {
			pluginId = Messages.MessageAction_0;
		}
		
		// throwable
		Throwable throwable;
		if (event.getThrowableProxy() != null && event.getThrowableProxy() instanceof ThrowableProxy) {
			throwable = ((ThrowableProxy)event.getThrowableProxy()).getThrowable();
		}
		else {
			throwable = null;
		}
		
		// severity
		Level level = event.getLevel();
		
		if (!level.isGreaterOrEqual(Level.INFO)) {
			return null;
		}
		
		int severity;
		if (level == Level.INFO) {
			severity = IStatus.INFO;
		}
		else if (level == Level.WARN) {
			severity = IStatus.WARNING;
		}
		else {
			// default to error
			severity = IStatus.ERROR;
		}
		
		IStatus status = new Status(
				severity, 
				pluginId, 
				event.getFormattedMessage(), 
				throwable);
		
		return status;
	}

}
