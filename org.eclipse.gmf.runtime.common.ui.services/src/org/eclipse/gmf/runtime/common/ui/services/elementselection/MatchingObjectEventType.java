/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.common.ui.services.elementselection;

/**
 * Interface describing the type of the matching object event for the element
 * selection service.
 * 
 * @author Anthony Hunter
 */
public class MatchingObjectEventType {

    /**
     * A matching object event that contains a matching object.
     */
    public static final MatchingObjectEventType MATCH = new MatchingObjectEventType();

    /**
     * A matching object event signaling that there are no more matching
     * objects. This event does not contain a matching object and there are no
     * further events to be received.
     */
    public static final MatchingObjectEventType END_OF_MATCHES = new MatchingObjectEventType();
}
