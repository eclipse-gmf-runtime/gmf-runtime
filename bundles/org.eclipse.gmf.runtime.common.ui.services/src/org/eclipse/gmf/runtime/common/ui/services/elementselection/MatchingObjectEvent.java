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
 * A matching object event for the element selection service.
 * <p>
 * The element selection service returns IMatchingObjectEvent to the
 * IElementSelectionListener. If there are ten matching objects, then eleven
 * IMatchingObjectEvent will be sent to the listener. The first ten will have
 * type MatchingObjectEventType.MATCH and will have a matching object. The
 * eleventh event will have type MatchingObjectEventType.END_OF_MATCHES.
 * 
 * @author Anthony Hunter
 */
public class MatchingObjectEvent
    implements IMatchingObjectEvent {

    /**
     * the matching object event type.
     */
    private MatchingObjectEventType eventType;

    /**
     * the matching object.
     */
    private IMatchingObject matchingObject;

    /**
     * Constructor for a MatchingObjectEvent.
     * 
     * @param eventType
     *            the matching object event type.
     * @param matchingObject
     *            the matching object.
     */
    public MatchingObjectEvent(MatchingObjectEventType eventType,
            IMatchingObject matchingObject) {
        super();
        this.eventType = eventType;
        this.matchingObject = matchingObject;
    }

    /**
     * {@inheritDoc}
     */
    public MatchingObjectEventType getEventType() {
        return eventType;
    }

    /**
     * {@inheritDoc}
     */
    public IMatchingObject getMatchingObject() {
        return matchingObject;
    }

}
