/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.common.ui.services.internal.elementselection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gmf.runtime.common.ui.services.elementselection.ElementSelectionService;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.ElementSelectionServiceJob;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.IElementSelectionInput;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.IElementSelectionListener;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.IMatchingObjectEvent;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.MatchingObjectEventType;
import org.eclipse.ui.PlatformUI;

/**
 * For backward compatibility, use the element selection service and return the
 * results in a list.
 * 
 * @author Anthony Hunter
 */
public class ElementSelectionList {

    private List results = new ArrayList();

    private MatchingObjectEventType running = MatchingObjectEventType.MATCH;

    private IElementSelectionInput elementSelectionInput;

    class ElementSelectionListener
        implements IElementSelectionListener {

        public void matchingObjectEvent(IMatchingObjectEvent matchingObjectEvent) {
            if (matchingObjectEvent.getEventType() == MatchingObjectEventType.END_OF_MATCHES) {
                synchronized (running) {
                    running = MatchingObjectEventType.END_OF_MATCHES;
                }
            } else {
                synchronized (results) {
                    results.add(matchingObjectEvent.getMatchingObject());
                }
            }
        }
    };

    /**
     * Run the element selection service and return the list of matching
     * objects.
     * 
     * @param input
     *            input for the element selection service.
     * @return the list of matching objects.
     */
    public List getMatchingObjects(IElementSelectionInput input) {
        this.elementSelectionInput = input;
        ElementSelectionServiceJob job = ElementSelectionService.getInstance()
            .getMatchingObjects(elementSelectionInput,
                new ElementSelectionListener());
        job.getName();
        while (true) {
            synchronized (running) {
                if (running == MatchingObjectEventType.END_OF_MATCHES) {
                    break;
                }
            }
            if (PlatformUI.getWorkbench().getDisplay().getThread().equals(
                Thread.currentThread())) {
                while (PlatformUI.getWorkbench().getDisplay().readAndDispatch()) {
                    // nothing, just dispatch events so the UI is not hung.
                }
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                break;
            }
        }
        return results;
    }
}
