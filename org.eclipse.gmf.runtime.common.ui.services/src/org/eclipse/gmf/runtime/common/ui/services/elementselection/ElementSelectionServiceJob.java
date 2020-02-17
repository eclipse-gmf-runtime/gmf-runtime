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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

/**
 * A job for an element selection provider. Each element selection provider is
 * run asynchronously in a separate job, sending matching objects to the
 * listener.
 * <p>
 * This is required since a ElementSelectionProvider may be a long running
 * process. Control is returned to the caller to either display a progress
 * monitor or update the UI as matching objects are received.
 * 
 * @author Anthony Hunter
 */
public class ElementSelectionServiceJob
    extends Job {

    /**
     * The element selection provider.
     */
    private IElementSelectionProvider provider;

    /**
     * Constructor for a ElementSelectionServiceJob.
     * 
     * @param name
     *            the name of the job.
     * @param provider
     *            The element selection provider.
     */
    public ElementSelectionServiceJob(String name,
            IElementSelectionProvider provider) {
        super(name);
        this.provider = provider;
    }

    /**
     * {@inheritDoc}
     */
    protected IStatus run(IProgressMonitor monitor) {
        provider.run(monitor);
        if (monitor.isCanceled()) {
            return Status.CANCEL_STATUS;
        }
        return Status.OK_STATUS;
    }

}
