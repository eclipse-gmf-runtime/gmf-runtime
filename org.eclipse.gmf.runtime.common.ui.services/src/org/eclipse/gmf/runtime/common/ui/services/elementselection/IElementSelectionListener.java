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
 * Interface describing a listener for the element selection service.
 * <p>
 * The element selection service returns IMatchingObjectEvent to the
 * IElementSelectionListener. If there are ten matching objects, then eleven
 * IMatchingObjectEvent will be sent to the listener. The first ten will have
 * type MatchingObjectEventType.MATCH and will have a matching object. The
 * eleventh event will have type MatchingObjectEventType.END_OF_MATCHES.
 * 
 * @author Anthony Hunter
 */
public interface IElementSelectionListener {

    /**
     * A matching object event has been sent.
     * 
     * @param serviceEvent
     *            the matching object event.
     */
    public void matchingObjectEvent(IMatchingObjectEvent matchingObjectEvent);
}
