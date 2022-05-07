/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.common.ui.services.elementselection;

/**
 * Interface describing a matching object event for the element selection
 * service.
 * <p>
 * The element selection service returns IMatchingObjectEvent to the
 * IElementSelectionListener. If there are ten matching objects, then eleven
 * IMatchingObjectEvent will be sent to the listener. The first ten will have
 * type MatchingObjectEventType.MATCH and will have a matching object. The
 * eleventh event will have type MatchingObjectEventType.END_OF_MATCHES.
 * <p>
 * 
 * @author Anthony Hunter
 */
public interface IMatchingObjectEvent {

    /**
     * Retrieve the matching object event type.
     * 
     * @return the matching object event type.
     */
    public MatchingObjectEventType getEventType();

    /**
     * Retrieve the matching object.
     * 
     * @return the matching object.
     */
    public IMatchingObject getMatchingObject();
}
