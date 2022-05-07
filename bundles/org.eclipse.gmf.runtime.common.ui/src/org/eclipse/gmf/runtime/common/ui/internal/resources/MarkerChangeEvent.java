/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.internal.resources;

import java.util.Map;

import org.eclipse.core.resources.IMarker;


/**
 * Utility class that describes a marker change event.
 * 
 * @author Michael Yee
 */
public class MarkerChangeEvent {

	/**
	 * the event type.
	 */
	private final MarkerChangeEventType eventType;

	/**
	 * the marker
	 */
	private final IMarker marker;

	/**
	 * the marker's old attributes, for a deleted marker only
	 */
	private final Map attributes;

	/**
	 * Constructor for MarkerChangeEvent
	 * 
	 * @param anEventType
	 *            an event from the MarkerChangeEventType enumeration
	 * @param aMarker
	 *            IMarker for the marker change event
	 * @param attributesMap
	 *            marker's old attributes, for a deleted marker only. May be
	 *            null
	 */
	public MarkerChangeEvent(MarkerChangeEventType anEventType,
			IMarker aMarker, Map attributesMap) {
		this.eventType = anEventType;
		this.marker = aMarker;
		this.attributes = attributesMap;
	}

	/**
	 * Utility constructor for MarkerChangeEvent
	 * 
	 * @param anEventType
	 *            an event from the MarkerChangeEventType enumeration
	 * @param aMarker
	 *            IMarker for the marker change event
	 */
	public MarkerChangeEvent(MarkerChangeEventType anEventType, IMarker aMarker) {
		this(anEventType, aMarker, null);
	}

	/**
	 * Gets the event type
	 * 
	 * @return the event type
	 */
	public MarkerChangeEventType getEventType() {
		return eventType;
	}

	/**
	 * Gets the marker
	 * 
	 * @return the marker
	 */
	public IMarker getMarker() {
		return marker;
	}

	/**
	 * Gets the marker's old attributes, for a deleted marker only
	 * 
	 * @return the marker's old attributes, for a deleted marker only, otherwise
	 *         <code>null</code>
	 */
	public Map getAttributes() {
		return attributes;
	}

}