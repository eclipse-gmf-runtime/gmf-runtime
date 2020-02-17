/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.internal.l10n.CommonUIServicesMessages;
import org.eclipse.osgi.util.NLS;

/**
 * Abstract implementation of an element selection provider.
 * 
 * @author Anthony Hunter
 */
public abstract class AbstractElementSelectionProvider
    extends AbstractProvider
    implements IElementSelectionProvider {

    /**
     * The element selection input.
     */
    private IElementSelectionInput elementSelectionInput;

    /**
     * The element selection listener.
     */
    private IElementSelectionListener elementSelectionListener;


    /**
     * {@inheritDoc}
     */
    public boolean provides(IOperation operation) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public ElementSelectionServiceJob getMatchingObjects(
            IElementSelectionInput input, IElementSelectionListener listener) {
        elementSelectionInput = input;
        ElementSelectionServiceJob job = createSelectionJob();
        elementSelectionListener = listener;
        return job;
    }
    
    /**
     * Creates the selection service job that runs the provider's search.
     * This method should configure the new job with the appropriate
     * priority, scheduling rules, etc. but should not schedule it.
     * 
     * @return a new selection provider job
     */
    protected ElementSelectionServiceJob createSelectionJob() {
        ElementSelectionServiceJob job = new ElementSelectionServiceJob(getJobName(), this);
        job.setPriority(Job.SHORT);
        return job;
    }

    /**
     * {@inheritDoc}
     */
    public Object resolve(IMatchingObject object) {
        /** 
         * Extenders are expected to override.
         */
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void run(IProgressMonitor monitor) {
        /** 
         * Extenders are expected to override.
         */
    }

    /**
     * Get the name for the ElementSelectionServiceJob. Clients can override.
     * 
     * @return the name for the job.
     */
    protected String getJobName() {
        String providerName = getClass().getName().substring(
            getClass().getName().lastIndexOf('.') + 1);
        String filter = getElementSelectionInput().getInput();
        return NLS.bind(
            CommonUIServicesMessages.ElementSelectionService_JobName,
            new String[] {providerName, filter});
    }

    /**
     * Fire an end of matches event since there are no more matches.
     */
    protected void fireEndOfMatchesEvent() {
        IMatchingObject matchingObject = new AbstractMatchingObject(null, null,
            null, this);
        MatchingObjectEvent matchingObjectEvent = new MatchingObjectEvent(
            MatchingObjectEventType.END_OF_MATCHES, matchingObject);
        fireMatchingObjectEvent(matchingObjectEvent);
    }

    /**
     * Fire a matching object event.
     * 
     * @param matchingObject
     *            the matching object.
     */
    protected void fireMatchingObjectEvent(IMatchingObject matchingObject) {
        MatchingObjectEvent matchingObjectEvent = new MatchingObjectEvent(
            MatchingObjectEventType.MATCH, matchingObject);
        fireMatchingObjectEvent(matchingObjectEvent);
    }

    /**
     * Send the matching object event to the listener.
     * 
     * @param matchingObjectEvent
     *            the matching object event.
     */
    protected void fireMatchingObjectEvent(
            IMatchingObjectEvent matchingObjectEvent) {
        getElementSelectionListener().matchingObjectEvent(matchingObjectEvent);
    }

    /**
     * Retrieve the element selection input.
     * 
     * @return the element selection input.
     */
    protected IElementSelectionInput getElementSelectionInput() {
        return elementSelectionInput;
    }

    /**
     * Retrieve the element selection listener.
     * 
     * @return the element selection listener.
     */
    protected IElementSelectionListener getElementSelectionListener() {
        return elementSelectionListener;
    }
}
