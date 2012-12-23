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

package de.fhg.igd.slf4jplus.logback.appender;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import de.fhg.igd.slf4jplus.ALoggerUtil;

/**
 * Filter the rejects messages that are transaction markers
 * 
 * @author Simon Templer
 */
public class FlattenFilter extends Filter<LoggingEvent> {

	/**
	 * @see Filter#decide(Object)
	 */
	@Override
	public FilterReply decide(LoggingEvent event) {
		if (ALoggerUtil.beginsTransaction(event.getMarker())
				|| ALoggerUtil.endsTransaction(event.getMarker())) {
			return FilterReply.DENY;
		}
		else {
			return FilterReply.NEUTRAL;
		}
	}

}
