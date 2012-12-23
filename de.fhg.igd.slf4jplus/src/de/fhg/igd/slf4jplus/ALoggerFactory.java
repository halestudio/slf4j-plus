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

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.igd.slf4jplus.internal.ALoggerImpl;


/**
 * Wraps {@link Logger} in {@link ALogger} instances
 * 
 * @author Simon Templer
 */
public abstract class ALoggerFactory {

	/**
	 * Get an {@link ALogger} instance by name.
	 * 
	 * @param name the logger name
	 * @param bundleName the symbolic name of the OSGi bundle originating the
	 *   messages, may be <code>null</code> if unknown
	 * @return the enhanced logger
	 * 
	 * @see LoggerFactory#getLogger(String)
	 */
	public static ALogger getLogger(String name, String bundleName) {
		return new ALoggerImpl(LoggerFactory.getLogger(name), bundleName);
	}

	/**
	 * Get a new {@link ALogger} instance by class.
	 * 
	 * @param clazz the class
	 * @param bundleName the symbolic name of the OSGi bundle originating the
	 *   messages, may be <code>null</code> if unknown
	 * @return the enhanced logger
	 * 
	 * @see LoggerFactory#getLogger(Class)
	 */
	public static ALogger getLogger(Class<?> clazz, String bundleName) {
		if (bundleName == null) {
			bundleName = determineBundleName(clazz);
		}
		
		return new ALoggerImpl(LoggerFactory.getLogger(clazz), bundleName);
	}
	
	/**
	 * Get a new {@link ALogger} instance by class. Calls from the given class
	 * will be removed from the stack trace when determining location information. 
	 * 
	 * @param clazz the class
	 * @param bundleName the symbolic name of the OSGi bundle originating the
	 *   messages, may be <code>null</code> if unknown
	 * @return the enhanced logger
	 * 
	 * @see LoggerFactory#getLogger(Class)
	 */
	public static ALogger getMaskingLogger(Class<?> clazz, String bundleName) {
		if (bundleName == null) {
			bundleName = determineBundleName(clazz);
		}
		
		return new ALoggerImpl(LoggerFactory.getLogger(clazz), bundleName, clazz.getName());
	}
	
	private static String determineBundleName(Class<?> clazz) {
		// try to automatically determine the bundle name
		ClassLoader classLoader = clazz.getClassLoader();
		
		/*
		 * Doing this with reflections to allow org.osgi.framework as optional dependency:
		 * if (classLoader instanceof BundleReference) {
		 *   bundleName = ((BundleReference) classLoader).getBundle().getSymbolicName();
		 * }
		 */
		try {
			Class<?> bundleReferenceClass = Class.forName("org.osgi.framework.BundleReference");
			Class<?> bundleClass = Class.forName("org.osgi.framework.Bundle");
			if (bundleReferenceClass.isInstance(classLoader)) {
				Method getBundleMethod = bundleReferenceClass.getMethod("getBundle");
				Object bundle = getBundleMethod.invoke(classLoader);
				if (bundleClass.isInstance(bundle)) {
					Method getSymbolicNameMethod = bundleClass.getMethod("getSymbolicName");
					Object name = getSymbolicNameMethod.invoke(bundle);
					if (name instanceof String) {
						return (String) name;
					}
				}
			}
		} catch (Throwable e) {
			// ignore
		}
		
		return null;
	}

	/**
	 * Get a new {@link ALogger} instance by class. The bundle name will be
	 * determined automatically if the class loader of the given class is an
	 * instance of {@link org.osgi.framework.BundleReference}
	 * 
	 * @param clazz the class
	 * @return the enhanced logger
	 * 
	 * @see LoggerFactory#getLogger(Class)
	 */
	public static ALogger getLogger(Class<?> clazz) {
		return getLogger(clazz, null);
	}

}
