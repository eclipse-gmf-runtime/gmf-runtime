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
package org.eclipse.gmf.runtime.common.ui.services.elementselection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.common.ui.services.internal.CommonUIServicesPlugin;
import org.eclipse.gmf.runtime.common.ui.services.internal.elementselection.ElementSelectionList;
import org.eclipse.gmf.runtime.common.ui.services.internal.elementselection.MatchingObjectsOperation;
import org.eclipse.gmf.runtime.common.ui.services.internal.l10n.CommonUIServicesMessages;
import org.eclipse.gmf.runtime.common.ui.services.util.ActivityFilterProviderDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.PlatformUI;

/**
 * The element selection service.
 * 
 * @author Anthony Hunter
 */
public class ElementSelectionService
    extends Service
    implements IElementSelectionProvider, IElementSelectionListener {

    /**
     * A provider descriptor that will ignore providers that are contributed by
     * a plug-in that is matched to a disabled capability.
     */
    private static class ProviderDescriptor
        extends Service.ProviderDescriptor {

        private ActivityFilterProviderDescriptor activityFilter;

        public ProviderDescriptor(IConfigurationElement element) {
            super(element);
            activityFilter = new ActivityFilterProviderDescriptor(element);
        }

        public boolean provides(IOperation operation) {
            return activityFilter.provides(operation)
                && super.provides(operation);
        }
    }

    private IElementSelectionInput elementSelectionInput;

    private IElementSelectionListener elementSelectionListener;

    private HashMap jobs = new HashMap();

    /**
     * The singleton instance of the type selection service.
     */
    private final static ElementSelectionService instance = new ElementSelectionService();

    static {
        instance.configureProviders(CommonUIServicesPlugin.getPluginId(),
            "elementSelectionProviders"); //$NON-NLS-1$
    }

    /**
     * Constructs a new type selection service.
     */
    protected ElementSelectionService() {
        super(true);
    }

    /**
     * Retrieves the singleton instance of the type selection service.
     * 
     * @return The type selection service singleton.
     */
    public static ElementSelectionService getInstance() {
        return instance;
    }

    /**
     * For backward compatibility, use the element selection service and return
     * the results in a list.
     * 
     * @param input
     *            the element selection input.
     * @return list of matching objects.
     */
    public List getMatchingObjects(IElementSelectionInput input) {
        return new ElementSelectionList().getMatchingObjects(input);
    }

    /**
     * {@inheritDoc}
     */
    public ElementSelectionServiceJob getMatchingObjects(
            IElementSelectionInput input, IElementSelectionListener listener) {
        elementSelectionInput = input;
        elementSelectionListener = listener;
        ElementSelectionServiceJob job = new ElementSelectionServiceJob(
            getJobName(), this);
        job.setPriority(Job.SHORT);
        job.schedule();
        return job;
    }

    /**
     * {@inheritDoc}
     */
    public void run(IProgressMonitor monitor) {
        List results = new ArrayList();
        IOperation operation = new MatchingObjectsOperation(
            elementSelectionInput);

        /**
         * Get the list of element selection providers based on the input.
         */
        for (int i = 0; i < ExecutionStrategy.PRIORITIES.length; ++i) {
            List providers = ExecutionStrategy.FORWARD.getUncachedProviders(
                this, ExecutionStrategy.PRIORITIES[i], operation);
            results.addAll(providers);
        }

        /**
         * Create the jobs for each provider.
         */
        for (Iterator i = results.iterator(); i.hasNext();) {
            IElementSelectionProvider provider = (IElementSelectionProvider) i
                .next();

            addJob(provider);
        }

        /**
         * Start the provider jobs.
         */
        HashMap jobsClone; 
        synchronized (jobs) {
            jobsClone  = (HashMap)jobs.clone();
        }
        for (Iterator i = jobsClone.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            ElementSelectionServiceJob job = (ElementSelectionServiceJob) entry
                .getValue();
            job.schedule();
        }

        /**
         * Now loop, waiting for the provider jobs to complete.
         */
        monitor.beginTask(getJobName(), 1000);
        while (true) {
            synchronized (jobs) {
                if (jobs.size() == 0) {
                    break;
                }
            }
            monitor.worked(1);
            /**
             * if the progress monitor is canceled, then cancel the running jobs.
             */
            if (monitor.isCanceled()) {
                cancelAllJobs();
                break;
            }
        }
        monitor.done();
    }

    /**
     * Resolve the matching object to a modeling object. The service always
     * returns null since the client should be asking the correct provider to
     * resolve the object.
     * 
     * @return null.
     */
    public Object resolve(IMatchingObject object) {
        return null;
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

    /**
     * Add an element selection provider to the list of jobs running the providers.
     * 
     * @param provider an element selection provider.
     */
    private void addJob(IElementSelectionProvider provider) {
        ElementSelectionServiceJob job = provider.getMatchingObjects(
            elementSelectionInput, this);
        synchronized (jobs) {
            jobs.put(provider, job);
        }
    }

    /**
     * Remove an element selection provider from the list.
     * 
     * @param provider an element selection provider.
     */
    private void removeJob(IElementSelectionProvider provider) {
        boolean end_of_matches = false;
        synchronized (jobs) {
            jobs.remove(provider);
            if (jobs.size() == 0) {
                end_of_matches = true;
            }
        }
        /**
         * All the jobs have finished, send end of matches event.
         */
        if (end_of_matches) {
            fireEndOfMatchesEvent();
        }
    }

    /**
     * Send the matching object event to the listener.
     * 
     * @param matchingObjectEvent
     *            the matching object event.
     */
    protected void fireMatchingObjectEvent(
            final IMatchingObjectEvent matchingObjectEvent) {
        PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

            public void run() {
                elementSelectionListener
                    .matchingObjectEvent(matchingObjectEvent);
            }
        });
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
     * {@inheritDoc}
     */
    public void matchingObjectEvent(IMatchingObjectEvent matchingObjectEvent) {
        if (matchingObjectEvent.getEventType() == MatchingObjectEventType.END_OF_MATCHES) {
            removeJob(matchingObjectEvent.getMatchingObject().getProvider());
        } else {
            fireMatchingObjectEvent(matchingObjectEvent);
        }
    }

    /**
     * Cancel the jobs running for the element selection service.
     */
    protected void cancelAllJobs() {
        HashMap jobsClone;
        synchronized (jobs) {
            jobsClone = (HashMap) jobs.clone();
        }
        for (Iterator i = jobsClone.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            IElementSelectionProvider provider = (IElementSelectionProvider) entry.getKey(); 
            ElementSelectionServiceJob job = (ElementSelectionServiceJob) entry
                .getValue();
            job.cancel();
            removeJob(provider);
        }
    }
    
    protected Service.ProviderDescriptor newProviderDescriptor(
            IConfigurationElement element) {
            return new ProviderDescriptor(element);
    }
}
