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

package de.fhg.igd.slf4jplus.ui.errorlog;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import de.fhg.igd.slf4jplus.ALoggerUtil;
import de.fhg.igd.slf4jplus.logback.observer.EventGroup;
import de.fhg.igd.slf4jplus.logback.observer.GroupAwareLogObserver;

/**
 * Log observer that forwards log messages to the error log
 * 
 * @author Simon Templer
 */
public class ErrorLogObserver extends GroupAwareLogObserver {

	/**
	 * @see GroupAwareLogObserver#acceptRawEvent(ch.qos.logback.classic.spi.LoggingEvent)
	 */
	@Override
	protected boolean acceptRawEvent(LoggingEvent event) {
		Level level = event.getLevel();
		return level == Level.INFO || level == Level.WARN || level == Level.ERROR;
	}

	/**
	 * @see GroupAwareLogObserver#processEvent(LoggingEvent)
	 */
	@Override
	protected void processEvent(LoggingEvent event) {
		IStatus status = createStatus(event, false);
		if (status != null) {
			Activator.getDefault().getLog().log(status);
		}
	}

	/**
	 * @see GroupAwareLogObserver#processEvents(EventGroup)
	 */
	@Override
	protected void processEvents(EventGroup events) {
		IStatus status = createStatus(events);
		if (status != null) {
			Activator.getDefault().getLog().log(status);
		}
	}

	/**
	 * Create an {@link IStatus} from the given event group
	 * 
	 * @param events the event group
	 * 
	 * @return the {@link IStatus} or <code>null</code>
	 */
	private IStatus createStatus(EventGroup events) {
		if (events.hasChildren()) {
			MultiStatus status = (MultiStatus) createStatus(events.getEvent(), true);
			for (EventGroup child : events.getChildren()) {
				IStatus childStatus = createStatus(child);
				if (childStatus != null) {
					status.add(childStatus);
				}
			}
			return status;
		}
		else {
			return createStatus(events.getEvent(), false);
		}
	}

	/**
	 * Create an {@link IStatus} from the given event
	 * 
	 * @param event the logging event
	 * @param multi if a {@link MultiStatus} shall be created
	 * 
	 * @return the {@link IStatus} or <code>null</code>
	 */
	private IStatus createStatus(LoggingEvent event, boolean multi) {
		// pluginId
		String pluginId = ALoggerUtil.getBundleName(event.getMarker());
		if (pluginId == null) {
			// default to logger name
			String name = event.getLoggerName();
			if (name != null) {
				pluginId = "[" + name + "]";
			}
			
			if (pluginId == null) {
				pluginId = "#unknown";
			}
		}
		
		// throwable
		Throwable throwable;
		if (event.getThrowableProxy() != null && event.getThrowableProxy() instanceof ThrowableProxy) {
			throwable = ((ThrowableProxy)event.getThrowableProxy()).getThrowable();
		}
		else {
			throwable = null;
		}
		
		// create status object
		IStatus status;
		if (multi) {
			status = new MultiStatus(
					pluginId,
					IStatus.OK,
					event.getFormattedMessage(), 
					throwable);
		}
		else {
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
			
			status = new Status(
					severity, 
					pluginId, 
					event.getFormattedMessage(), 
					throwable);
		}
		
		return status;
	}

}
