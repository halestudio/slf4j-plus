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

import org.slf4j.Logger;
import org.slf4j.Marker;


/**
 * Enhanced logger interface with support for user messages and message grouping
 * 
 * @author Simon Templer
 */
public interface ALogger extends ALoggerConstants, Logger {
	
	/**
	 * Create a log message marker
	 * 
	 * @param userMessage if the message is a user message
	 * @param parentGroup the parent group, may be <code>null</code>
	 * 
	 * @return the marker, which may be <code>null</code>
	 */
	public Marker createMarker(boolean userMessage, AGroup parentGroup);
	
	/**
	 * Begin a log transaction.
	 * Every transaction has to be terminated by calling
	 * {@link ATransaction#end()}
	 * 
	 * @param msg the transaction name/message
	 *  
	 * @return the log transaction object 
	 */
	public ATransaction begin(String msg);
	
	// convenience methods
	
	/**
	 * Log a user message a the INFO level
	 * 
	 * @param msg the message
	 */
	public void userInfo(String msg);

	/**
	 * Log a user message a the INFO level
	 * 
	 * @param msg the message
	 * @param t the throwable to log
	 */
	public void userInfo(String msg, Throwable t);
	
	/**
	 * Log a user message a the WARN level
	 * 
	 * @param msg the message
	 */
	public void userWarn(String msg);

	/**
	 * Log a user message a the WARN level
	 * 
	 * @param msg the message
	 * @param t the throwable to log
	 */
	public void userWarn(String msg, Throwable t);
	
	/**
	 * Log a user message a the ERROR level
	 * 
	 * @param msg the message
	 */
	public void userError(String msg);

	/**
	 * Log a user message a the ERROR level
	 * 
	 * @param msg the message
	 * @param t the throwable to log
	 */
	public void userError(String msg, Throwable t);
	
	/**
	 * Log a message a the TRACE level
	 * 
	 * @param parent the message group, may be <code>null</code>
	 * @param msg the message
	 */
	public void trace(AGroup parent, String msg);

	/**
	 * Log a message a the TRACE level
	 * 
	 * @param parent the message group, may be <code>null</code>
	 * @param msg the message
	 * @param t the throwable to log
	 */
	public void trace(AGroup parent, String msg, Throwable t);
	
	/**
	 * Log a message a the DEBUG level
	 * 
	 * @param parent the message group, may be <code>null</code>
	 * @param msg the message
	 */
	public void debug(AGroup parent, String msg);

	/**
	 * Log a message a the DEBUG level
	 * 
	 * @param parent the message group, may be <code>null</code>
	 * @param msg the message
	 * @param t the throwable to log
	 */
	public void debug(AGroup parent, String msg, Throwable t);
	
	/**
	 * Log a message a the INFO level
	 * 
	 * @param parent the message group, may be <code>null</code>
	 * @param msg the message
	 */
	public void info(AGroup parent, String msg);

	/**
	 * Log a message a the INFO level
	 * 
	 * @param parent the message group, may be <code>null</code>
	 * @param msg the message
	 * @param t the throwable to log
	 */
	public void info(AGroup parent, String msg, Throwable t);
	
	/**
	 * Log a message a the WARN level
	 * 
	 * @param parent the message group, may be <code>null</code>
	 * @param msg the message
	 */
	public void warn(AGroup parent, String msg);

	/**
	 * Log a message a the WARN level
	 * 
	 * @param parent the message group, may be <code>null</code>
	 * @param msg the message
	 * @param t the throwable to log
	 */
	public void warn(AGroup parent, String msg, Throwable t);
	
	/**
	 * Log a message a the ERROR level
	 * 
	 * @param parent the message group, may be <code>null</code>
	 * @param msg the message
	 */
	public void error(AGroup parent, String msg);

	/**
	 * Log a message a the WARN level
	 * 
	 * @param parent the message group, may be <code>null</code>
	 * @param msg the message
	 * @param t the throwable to log
	 */
	public void error(AGroup parent, String msg, Throwable t);
	
	/**
	 * Log a user message a the INFO level
	 * 
	 * @param parent the message group, may be <code>null</code>
	 * @param msg the message
	 */
	public void userInfo(AGroup parent, String msg);

	/**
	 * Log a user message a the INFO level
	 * 
	 * @param parent the message group, may be <code>null</code>
	 * @param msg the message
	 * @param t the throwable to log
	 */
	public void userInfo(AGroup parent, String msg, Throwable t);
	
	/**
	 * Log a user message a the WARN level
	 * 
	 * @param parent the message group, may be <code>null</code>
	 * @param msg the message
	 */
	public void userWarn(AGroup parent, String msg);

	/**
	 * Log a user message a the WARN level
	 * 
	 * @param parent the message group, may be <code>null</code>
	 * @param msg the message
	 * @param t the throwable to log
	 */
	public void userWarn(AGroup parent, String msg, Throwable t);
	
	/**
	 * Log a user message a the ERROR level
	 * 
	 * @param parent the message group, may be <code>null</code>
	 * @param msg the message
	 */
	public void userError(AGroup parent, String msg);

	/**
	 * Log a user message a the ERROR level
	 * 
	 * @param parent the message group, may be <code>null</code>
	 * @param msg the message
	 * @param t the throwable to log
	 */
	public void userError(AGroup parent, String msg, Throwable t);

}
