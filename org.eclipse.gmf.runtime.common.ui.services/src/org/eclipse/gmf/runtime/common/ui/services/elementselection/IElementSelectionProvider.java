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
import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * Interface describing a element selection provider for the element selection
 * service.
 * 
 * @author Anthony Hunter
 */
public interface IElementSelectionProvider
    extends IProvider {

    /**
     * Retrieve a list of matching objects from the provider.
     * <p>
     * The provider is given an IElementSelectionInput and
     * IElementSelectionListener and creates a ElementSelectionServiceJob.
     * <p>
     * The provider returns IMatchingObjectEvent to the
     * IElementSelectionListener. If there are ten matching objects, then eleven
     * IMatchingObjectEvent will be sent to the listener. The first ten will
     * have type MatchingObjectEventType.MATCH and will have a matching object.
     * The eleventh event will have type MatchingObjectEventType.END_OF_MATCHES.
     * 
     * @param input
     *            the element selection input.
     * @param listener
     *            the provider will send matching object events to this
     *            listener.
     * @return the job that is running this provider.
     */
    public ElementSelectionServiceJob getMatchingObjects(
            IElementSelectionInput input, IElementSelectionListener listener);

    /**
     * Retrieve the list of matching objects from the provider.
     * <p>
     * The ElementSelectionServiceJob will invoke run. Clients must not call
     * this method.
     * 
     * @param monitor
     *            a progress monitor in which the provider is running.
     */
    public void run(IProgressMonitor monitor);

    /**
     * Resolve the matching object to a modeling object.
     * 
     * @param object
     *            the matching object.
     * @return a modeling object.
     */
    public Object resolve(IMatchingObject object);
}
