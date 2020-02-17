/******************************************************************************
 * Copyright 2005, 2008 IBM Corporation and others.
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
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

    protected class JobData {
        public IElementSelectionInput elementSelectionInput;

        public IElementSelectionListener elementSelectionListener;

        public HashMap<IElementSelectionProvider, ElementSelectionServiceJob> jobs = 
            new HashMap<IElementSelectionProvider, ElementSelectionServiceJob>();
    }
    
    private Map<ElementSelectionServiceJob, JobData> jobs2Data = 
        new HashMap<ElementSelectionServiceJob, JobData>();
    
    public JobData getJobData() {
        Job currentJob = jobManager.currentJob();
        assert currentJob != null;
        
        if(currentJob == null) {
            return null;
        }
        
        JobData data = null;
        synchronized(jobs2Data) {
            data = jobs2Data.get(currentJob);
        }
        
        return data;
    }

    /**
     * The singleton instance of the type selection service.
     */
    private final static ElementSelectionService instance = new ElementSelectionService();

    static {
        instance.configureProviders();
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
        ElementSelectionServiceJob job = createSelectionJob();
        JobData data = new JobData();
        data.elementSelectionInput = input;
        data.elementSelectionListener = listener;
        job.setName(getJobName(data));
        synchronized(jobs2Data) {
            jobs2Data.put(job, data);
        }
        job.schedule();
        return job;
    }
    
    
    protected String getJobName() {
        return StringStatics.BLANK;
    }
    
    /**
     * Creates the selection service job that manages the individual provider
     * search jobs.  This method should configure the new job with the appropriate
     * priority, scheduling rules, etc. but should not schedule it.
     * 
     * @return a new selection service job
     */
    protected ElementSelectionServiceJob createSelectionJob() {
        ElementSelectionServiceJob job = new ElementSelectionServiceJob(getJobName(), this);
        job.setPriority(Job.SHORT);
        return job;
    }
    
    public static final IJobManager jobManager = Job.getJobManager();

    /**
     * {@inheritDoc}
     */
    public void run(IProgressMonitor monitor) {
        JobData data = getJobData();
        if(data == null)
            return;
        
        List<IElementSelectionProvider> results = new ArrayList<IElementSelectionProvider>();
        IOperation operation = new MatchingObjectsOperation(
            data.elementSelectionInput);

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
        for (Iterator<IElementSelectionProvider> i = results.iterator(); i.hasNext();) {
            IElementSelectionProvider provider = i.next();

            addJob(data, provider);
        }

        /**
         * Start the provider jobs.
         */
        HashMap jobsClone; 
        synchronized (data) {
            jobsClone  = (HashMap)data.jobs.clone();
        }
        for (Iterator i = jobsClone.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            IElementSelectionProvider provider = (IElementSelectionProvider) entry
            	.getKey();
            ElementSelectionServiceJob job = (ElementSelectionServiceJob) entry
                .getValue();
            
            schedule(provider, job);
        }

        /**
         * Now loop, waiting for the provider jobs to complete.
         */
        monitor.beginTask(getJobName(data), 1000);
        while (true) {
            synchronized (data) {
                if (data.jobs.size() == 0) {
                    break;
                }
            }
            monitor.worked(1);
            /**
             * if the progress monitor is canceled, then cancel the running jobs.
             */
            if (monitor.isCanceled()) {
                synchronized(data) {
                    // nullify the element selection listener.
                    data.elementSelectionListener = null;
                    cancelAllJobs();
                    break;
                }
            }
        }
        monitor.done();
        jobs2Data.clear();
    }
    
    /**
     * Schedules the specified selection provider job.
     * 
     * @param provider a selection provider
     * @param job the <code>provider</code>'s job
     */
    protected void schedule(IElementSelectionProvider provider, ElementSelectionServiceJob job) {
    	job.schedule();
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
    protected String getJobName(JobData data) {
        if((getJobName() != null && getJobName().equals(StringStatics.BLANK)) && data != null) {
            String providerName = getClass().getName().substring(
                getClass().getName().lastIndexOf('.') + 1);
            String filter = data.elementSelectionInput.getInput();
            return NLS.bind(
                CommonUIServicesMessages.ElementSelectionService_JobName,
                new String[] {providerName, filter});
        }
        return getJobName();
    }

    /**
     * Add an element selection provider to the list of jobs running the providers.
     * 
     * @param provider an element selection provider.
     */
    private void addJob(JobData data, IElementSelectionProvider provider) {
        ElementSelectionServiceJob job = provider.getMatchingObjects(
            data.elementSelectionInput, this);
        synchronized (data) {
            data.jobs.put(provider, job);
        }
        
        synchronized(jobs2Data) {
            jobs2Data.put(job, data);
        }
    }

    /**
     * Remove an element selection provider from the list.
     * 
     * @param provider an element selection provider.
     */
    private void removeJob(JobData data, IElementSelectionProvider provider) {
        boolean end_of_matches = false;
        Object job = null;
        synchronized (data) {
            job = data.jobs.remove(provider);
            if (data.jobs.size() == 0) {
                end_of_matches = true;
            }
        }
        
        /**
         * All the jobs have finished, send end of matches event.
         */
        if (end_of_matches) {
            fireEndOfMatchesEvent();
        }

        synchronized(jobs2Data) {
            jobs2Data.remove(job);
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
        final Job currentJob = jobManager.currentJob();
        if(currentJob == null)
            return;
        
        JobData data = null;
        synchronized(jobs2Data) {
            data = jobs2Data.get(currentJob);
        }
        
        if(data == null)
            return;
        
        final JobData finalData = data;
        PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

            public void run() {
                if (finalData.elementSelectionListener != null) {
                    finalData.elementSelectionListener
                        .matchingObjectEvent(matchingObjectEvent);
                }
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
        JobData data = getJobData();
        if(data == null)
            return;
        if (matchingObjectEvent.getEventType() == MatchingObjectEventType.END_OF_MATCHES) {
            removeJob(data, matchingObjectEvent.getMatchingObject().getProvider());
        } else {
            fireMatchingObjectEvent(matchingObjectEvent);
        }
    }
    
    /**
     * Cancel the jobs running for the element selection service.
     */
    protected void cancelAllJobs() {
        JobData data = getJobData();
        HashMap jobsClone;
        synchronized (data) {
            jobsClone = (HashMap) data.jobs.clone();
        }
        for (Iterator i = jobsClone.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            IElementSelectionProvider provider = (IElementSelectionProvider) entry.getKey(); 
            ElementSelectionServiceJob job = (ElementSelectionServiceJob) entry
                .getValue();
            job.cancel();
            removeJob(data, provider);
        }
    }
    
    protected Service.ProviderDescriptor newProviderDescriptor(
            IConfigurationElement element) {
            return new ProviderDescriptor(element);
    }
    
    /**
     * Configures my providers from the <tt>elementSelectionProviders</tt>
     * extension point.
     */
    protected void configureProviders() {
    	configureProviders(
    		CommonUIServicesPlugin.getPluginId(),
        	"elementSelectionProviders"); //$NON-NLS-1$
    }
    
    public void cancelJob(ElementSelectionServiceJob job) {
        JobData data = null;
        synchronized(jobs2Data) {
            data = jobs2Data.get(job);
        }
        
        if (data != null) {
            synchronized(data) {
                data.elementSelectionListener = null;
            }
        }
        
        job.cancel();
    }
}
