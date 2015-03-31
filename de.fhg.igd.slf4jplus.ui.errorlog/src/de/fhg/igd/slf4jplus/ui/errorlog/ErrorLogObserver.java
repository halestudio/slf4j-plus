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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.core.runtime.IStatus;
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

	private static final int MULTI_STATUS_AGGREGATE_THRESHOLD = 200;
	private static final int MULTI_STATUS_AGGREGATE_GROUP_MAX = 100;

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
			CustomMultiStatus status = (CustomMultiStatus) createStatus(events.getEvent(), true);
			List<IStatus> children = new ArrayList<IStatus>(MULTI_STATUS_AGGREGATE_THRESHOLD);
			List<IStatus> infos = null;
			List<IStatus> warnings = null;
			List<IStatus> errors = null;
			AtomicInteger infoCount = null;
			AtomicInteger warnCount = null;
			AtomicInteger errorCount = null;
			boolean aggregate = false;
			for (EventGroup child : events.getChildren()) {
				IStatus childStatus = createStatus(child);
				if (childStatus != null) {
					if (!aggregate) {
						if (children.size() < MULTI_STATUS_AGGREGATE_THRESHOLD) {
							// normal mode - just add children for multi-status
							children.add(childStatus);
						}
						else {
							// switch to aggregate mode
							//TODO threshold configurable?
							
							aggregate = true;
							
							// init aggregation lists
							infos = new ArrayList<IStatus>(MULTI_STATUS_AGGREGATE_GROUP_MAX);
							warnings = new ArrayList<IStatus>(MULTI_STATUS_AGGREGATE_GROUP_MAX);
							errors = new ArrayList<IStatus>(MULTI_STATUS_AGGREGATE_GROUP_MAX);
							infoCount = new AtomicInteger();
							warnCount = new AtomicInteger();
							errorCount = new AtomicInteger();
							
							// store already created children
							for (IStatus c : children) {
								bucketInsert(c, infos, warnings, errors, infoCount, warnCount, errorCount);
							}
							// store new child
							bucketInsert(childStatus, infos, warnings, errors, infoCount, warnCount, errorCount);
							
							children.clear();
						}
					}
					else {
						// aggregate already active
						
						// store new child in respective bucket
						bucketInsert(childStatus, infos, warnings, errors, infoCount, warnCount, errorCount);
					}
				}
			}
			
			if (aggregate) {
				// create group children
				
				// error
				if (errors != null && !errors.isEmpty()) {
					@SuppressWarnings("null")
					CustomMultiStatus errorStatus = new CustomMultiStatus(
							status.getPlugin(), status.getCode(),
							MessageFormat.format((errors.size() < errorCount.get()) ? ("{0} errors (listing {1})") : ("{0} errors"),
									errorCount.get(), errors.size()),
							null);
					errorStatus.setChildren(errors.toArray(new IStatus[errors.size()]));
					children.add(errorStatus);
				}
				
				// warn
				if (warnings != null && !warnings.isEmpty()) {
					@SuppressWarnings("null")
					CustomMultiStatus warnStatus = new CustomMultiStatus(
							status.getPlugin(), status.getCode(),
							MessageFormat.format((warnings.size() < warnCount.get()) ? ("{0} warnings (listing {1})") : ("{0} warnings"),
									warnCount.get(), warnings.size()),
							null);
					warnStatus.setChildren(warnings.toArray(new IStatus[warnings.size()]));
					children.add(warnStatus);
				}
				
				// info
				if (infos != null && !infos.isEmpty()) {
					@SuppressWarnings("null")
					CustomMultiStatus infoStatus = new CustomMultiStatus(
							status.getPlugin(), status.getCode(),
							MessageFormat.format((infos.size() < infoCount.get()) ? ("{0} information messages (listing {1})") : ("{0} information messages"),
									infoCount.get(), infos.size()),
							null);
					infoStatus.setChildren(infos.toArray(new IStatus[infos.size()]));
					children.add(infoStatus);
				}
			}
			
			status.setChildren(children.toArray(new IStatus[children.size()]));
			return status;
		}
		else {
			return createStatus(events.getEvent(), false);
		}
	}

	private void bucketInsert(IStatus child, List<IStatus> infos,
			List<IStatus> warnings, List<IStatus> errors, AtomicInteger infoCount,
			AtomicInteger warnCount, AtomicInteger errorCount) {
		int sev = child.getSeverity();
		if (sev < IStatus.WARNING) {
			if (infos.size() < MULTI_STATUS_AGGREGATE_GROUP_MAX) {
				infos.add(child);
			}
			infoCount.incrementAndGet();
		}
		else if (sev < IStatus.ERROR) {
			if (warnings.size() < MULTI_STATUS_AGGREGATE_GROUP_MAX) {
				warnings.add(child);
			}
			warnCount.incrementAndGet();
		}
		else {
			if (errors.size() < MULTI_STATUS_AGGREGATE_GROUP_MAX) {
				errors.add(child);
			}
			errorCount.incrementAndGet();
		}
	}

	/**
	 * Create an {@link IStatus} from the given event
	 * 
	 * @param event the logging event
	 * @param multi if a {@link CustomMultiStatus} shall be created
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
			status = new CustomMultiStatus(
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
