/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.common.ui.services.internal.elementselection;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.ElementSelectionService;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.ElementSelectionServiceJob;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.IElementSelectionInput;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.IElementSelectionListener;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.IMatchingObjectEvent;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.MatchingObjectEventType;
import org.eclipse.gmf.runtime.common.ui.services.internal.CommonUIServicesPlugin;
import org.eclipse.gmf.runtime.common.ui.services.internal.CommonUIServicesStatusCodes;
import org.eclipse.gmf.runtime.common.ui.services.internal.l10n.CommonUIServicesMessages;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.osgi.util.NLS;
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

    private IProgressMonitor progressMonitor;

    private IElementSelectionInput elementSelectionInput;

    class ElementSelectionListener
        implements IElementSelectionListener {

        public void matchingObjectEvent(IMatchingObjectEvent matchingObjectEvent) {
            if (matchingObjectEvent.getEventType() == MatchingObjectEventType.END_OF_MATCHES) {
                running = MatchingObjectEventType.END_OF_MATCHES;
            } else {
                progressMonitor.worked(1);
                progressMonitor.subTask(matchingObjectEvent.getMatchingObject()
                    .getDisplayName());
                synchronized (results) {
                    results.add(matchingObjectEvent.getMatchingObject());
                }
            }
        }
    };

    IRunnableWithProgress runnable = new IRunnableWithProgress() {

        public void run(IProgressMonitor monitor)
            throws InvocationTargetException, InterruptedException {
            progressMonitor = monitor;
            ElementSelectionServiceJob job = ElementSelectionService.getInstance().getMatchingObjects(
                elementSelectionInput, new ElementSelectionListener());
            monitor.beginTask(getJobName(), 1000);
            while (true) {
                synchronized (running) {
                    if (running == MatchingObjectEventType.END_OF_MATCHES) {
                        break;
                    }
                }
                monitor.worked(1);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    break;
                }
                if (monitor.isCanceled()) {
                    job.cancel();
                    break;
                }
            }
            monitor.done();
        }
    };

    public List getMatchingObjects(IElementSelectionInput input) {
        this.elementSelectionInput = input;
        try {
            new ProgressMonitorDialog(PlatformUI.getWorkbench().getDisplay()
                .getActiveShell()).run(true, true, runnable);
        } catch (InvocationTargetException e) {
            Log.error(CommonUIServicesPlugin.getDefault(),
                CommonUIServicesStatusCodes.SERVICE_FAILURE,
                "executeWithProgressMonitor", e); //$NON-NLS-1$
        } catch (InterruptedException e) {
            /**
             * Just return when interrupted.
             */
        }
        return results;
    }

    /**
     * Get the name for the ElementSelectionServiceJob. Clients can override.
     * 
     * @return the name for the job.
     */
    protected String getJobName() {
        String providerName = getClass().getName().substring(
            getClass().getName().lastIndexOf('.') + 1);
        String filter = elementSelectionInput.getInput();
        return NLS.bind(
            CommonUIServicesMessages.ElementSelectionService_JobName,
            new String[] {providerName, filter});
    }

}
