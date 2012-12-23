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

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;

import de.fhg.igd.slf4jplus.ui.userstatus.contribution.MessageAction;
import de.fhg.igd.slf4jplus.ui.userstatus.contribution.StatusContribution;
import de.fhg.igd.slf4jplus.ui.userstatus.preferences.UserStatusPreferences;

/**
 * Status control
 * 
 * @author Simon Templer
 */
public class StatusControl extends Canvas implements StatusListener {
	
	/**
	 * The timer interval for the animation in milliseconds
	 */
	private static final int TIMER_INTERVAL = 1500;
	
	private final Image errorImage;
	private final Image warnImage;
	private final Image infoImage;
	private final Image okImage;
	private final Image newImage;
	
	/**
	 * If there are new messages
	 */
	private boolean newMessages = false;
	
	/**
	 * The current status
	 */
	private Status status = Status.OK;
	
	private boolean showNew = false;
	
	private Timer animateTimer = null;
	
	private final StatusObserver observer;
	
	private Menu menu;
	
	private final Display display;
	
	private boolean onErrorShowDialog;
	
	/**
	 * Constructor
	 * 
	 * @param parent the parent composite
	 * @param observer the status observer
	 */
	public StatusControl(Composite parent, StatusObserver observer) {
		super(parent, SWT.DOUBLE_BUFFERED); // | SWT.BORDER);
		
		onErrorShowDialog = UserStatusPreferences.getOnErrorShowDialog();
		
		errorImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
		warnImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_WARN_TSK);
		infoImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_INFO_TSK);
		okImage = UserStatusBundle.getImageDescriptor("icons/ok.gif").createImage(); //$NON-NLS-1$
		newImage = UserStatusBundle.getImageDescriptor("icons/new.gif").createImage(); //$NON-NLS-1$
		
		addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				synchronized (StatusControl.this) {
					Image drawImage;
					
					if (newMessages && showNew
							&& status.equals(Status.ERROR)) {
						drawImage = newImage;
					}
					else {
						switch (status) {
						case OK:
							drawImage = okImage;
							break;
						case WARN:
							drawImage = warnImage;
							break;
						case ERROR:
							drawImage = errorImage;
							break;
						case INFO: // fall through - INFO as default
						default:
							drawImage = infoImage;
							break;
						}
					}
					
					e.gc.drawImage(drawImage, 0, 0);
				}
				
			}
			
		});
		
		display = Display.getCurrent();
		
		// initialize
		this.observer = observer;
		setStatus(observer.getStatus(), observer.getNewMessageCount(),
				observer.getMessageCount());
		observer.addListener(this);
		
		// create menu
		MenuManager manager = new MenuManager();
		manager.setRemoveAllWhenShown(true);
		final IContributionItem contribution = new StatusContribution(observer,
				this);
		manager.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager manager) {
				manager.add(contribution);
			}
			
		});
		menu = manager.createContextMenu(this);
		
		// mouse listener
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				showStatusMenu(e);
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				showStatusMenu(e);
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				showStatusMenu(e);
			}
		});
	}
	
	/**
	 * Change the status on new messages
	 *  
	 * @param status the status
	 * @param newMessages the number of new messages
	 * @param messages the number of messages
	 */
	public void setStatus(Status status, int newMessages, int messages) {
		this.status = status;
		this.newMessages = newMessages > 0;
		
		if (!isDisposed()) {
			if (this.newMessages) {
				if (newMessages == 1) {
					setToolTipText(Messages.StatusControl_2);
				}
				else {
					setToolTipText(newMessages + Messages.StatusControl_3);
				}
			}
			else {
				if (messages == 1) {
					setToolTipText(Messages.StatusControl_4);
				}
				else if (messages > 1) {
					setToolTipText(messages + Messages.StatusControl_5);
				}
				else {
					setToolTipText(Messages.StatusControl_6);
				}
			}
			
			redraw();
		}
		
		doUpdate();
	}

	/**
	 * Update after status change
	 */
	private void doUpdate() {
		if (this.newMessages && !onErrorShowDialog && isAlertLevel(status)) {
			startTimer();
		}
		else {
			stopTimer();
		}
	}
	
	/**
	 * Determines if the given status is at least on alert level
	 * 
	 * @param status the status
	 * @return if the status is at least at alert level
	 */
	private static boolean isAlertLevel(Status status) {
		Status alert = UserStatusPreferences.getAlertLevel();
		return alert.compareTo(status) <= 0;
	}

	/**
	 * @return the onErrorShowDialog
	 */
	public boolean isOnErrorShowDialog() {
		return onErrorShowDialog;
	}

	/**
	 * @param onErrorShowDialog the onErrorShowDialog to set
	 */
	public void setOnErrorShowDialog(boolean onErrorShowDialog) {
		if (this.onErrorShowDialog != onErrorShowDialog) {
			this.onErrorShowDialog = onErrorShowDialog;
			
			UserStatusPreferences.setOnErrorShowDialog(onErrorShowDialog);
			
			doUpdate();
		}
	}

	/**
	 * Start the animation timer
	 */
	private void startTimer() {
		synchronized (this) {
			if (animateTimer == null) {
				animateTimer = new Timer(true);
				
				TimerTask toggleTask = new TimerTask() {
					
					@Override
					public void run() {
						synchronized (StatusControl.this) {
							showNew = !showNew;
						}
						if (!display.isDisposed()) {
							display.syncExec(new Runnable() {
								
								@Override
								public void run() {
									if (!isDisposed()) {
										redraw();
									}
								}
							});
						}
					}
				};
				
				animateTimer.scheduleAtFixedRate(toggleTask, TIMER_INTERVAL, TIMER_INTERVAL);
			}
		}
	}

	/**
	 * Stop the animation timer
	 */
	private void stopTimer() {
		synchronized (this) {
			if (animateTimer != null) {
				animateTimer.cancel();
			}
			
			animateTimer = null;
			showNew = false;
		}
	}

	/**
	 * @see Widget#dispose()
	 */
	@Override
	public void dispose() {
		observer.removeListener(this);
		
		super.dispose();
		
		// dispose images that are not shared
		okImage.dispose();
		newImage.dispose();
	}

	/**
	 * @see StatusListener#onStatusChanged(Status, int, int, LoggingEvent)
	 */
	@Override
	public void onStatusChanged(final Status status, final int newMessages, 
			final int messages, final LoggingEvent event) {
		if (!isDisposed()) {
			Runnable runnable = new Runnable() {
				
				@Override
				public void run() {
					setStatus(status, newMessages, messages);
					
					if (!isDisposed() && onErrorShowDialog && event != null && 
							isAlertLevel(levelToStatus(event.getLevel()))) {
						MessageAction.showErrorDialog(event);
					}
				}
				
			};
			
			if (Display.getCurrent() == null) {
				Display display = getDisplay();
				
				display.asyncExec(runnable);
			}
			else {
				runnable.run();
			}
		}
	}
	
	/**
	 * Convert logback level to {@link Status}
	 * @param level the level
	 * @return the corresponding status
	 */
	protected static Status levelToStatus(Level level) {
		if (level == Level.ERROR) {
			return Status.ERROR;
		}
		else if (level == Level.WARN) {
			return Status.WARN;
		}
		else if (level == Level.INFO) {
			return Status.INFO;
		}
		
		return Status.OK;
	}

	/**
	 * Show the status menu
	 * 
	 * @param e the mouse event
	 */
	private void showStatusMenu(MouseEvent e) {
		observer.markRead();
			
		menu.setLocation(this.toDisplay(e.x, e.y));
		menu.setVisible(true);
	}

}
