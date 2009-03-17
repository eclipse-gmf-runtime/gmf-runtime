/******************************************************************************
 * Copyright (c) 2006, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.diagram.ui;

import java.lang.ref.WeakReference;

import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.core.listener.DiagramEventBroker;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * This is an extension of the DiagramEventBroker that has special handling for notifications that
 * occurs from a worker thread / non-UI thread.  If the notification occurs on the main thread
 * then execution is delegated to the super class immediately.  If execution is not on the main thread
 * then there are 2 scenarios that have to be considered.  
 * 
 * The first scenario is for a long operation
 * that has been executed with a progress meter, where the progress meter is displaying UI on the main
 * thread and there is a background thread that is being executed that the progress meter is monitoring.
 * For this scenario, the UI updates on the diagram viewer have been disabled so as to avoid concurrency
 * issues.  When notifications are handled, they are synchronized to the main thread to avoid
 * SWTExceptions when UI tries to access SWT resources when updating figures or other UI.
 * 
 * The second scenario is for when a job has been executed on a worker thread, but has not been executed
 * through the OperationHistory.  Consequently, there is no hook for turning off display updates.  This
 * means that if the notifications were to continue on the worker thread, then the display could update
 * at the same time on the main thread thereby causing concurrent modification errors and other errors.
 * In this case, we need to resynchronize the notifications with the main thread.  In order to do this
 * it is necessary to run the notifications in an synchronous runnable that will run immediately
 * on the main thread.
 * 
 * @author sshaw
 * @since 1.2
 */
public class DiagramEventBrokerThreadSafe extends DiagramEventBroker {

	WeakReference editingDomainRef;
	
    public DiagramEventBrokerThreadSafe(TransactionalEditingDomain editingDomain) {
        super();
        editingDomainRef = new WeakReference(editingDomain);
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.core.listener.DiagramEventBroker#resourceSetChanged(org.eclipse.emf.transaction.ResourceSetChangeEvent)
     */
    public void resourceSetChanged(ResourceSetChangeEvent event) {
        if (shouldSynchronizeWithMainThread(event)) {
           	// force synchronization with the main thread
            final ResourceSetChangeEvent eventToHandle = event;
            TransactionalEditingDomain editingDomain = (TransactionalEditingDomain)editingDomainRef.get();
            if (editingDomain != null) {
	            PlatformUI.getWorkbench().getDisplay().syncExec(editingDomain.createPrivilegedRunnable(new Runnable() { 
	                public void run() {
	                    internal_resourceSetChanged(eventToHandle);
	                }
	            }));
	                
	            return;
            }
        }
        
        super.resourceSetChanged(event);
    }
    
    private boolean shouldSynchronizeWithMainThread(ResourceSetChangeEvent event) {
    	if (Display.getCurrent() == null)
    		return true;
        
        return false;
    }
    
    private void internal_resourceSetChanged(ResourceSetChangeEvent event) {
        super.resourceSetChanged(event);
    }
}