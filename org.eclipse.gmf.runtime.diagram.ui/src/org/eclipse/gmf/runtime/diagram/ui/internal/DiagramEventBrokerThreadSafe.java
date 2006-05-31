/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.diagram.ui.internal;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.diagram.core.listener.DiagramEventBroker;
import org.eclipse.gmf.runtime.diagram.ui.internal.parts.NotificationForEditPartsListener;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * This is an extension of the DiagramEventBroker that has special handling for notifications that
 * occurs from a worker thread / non-UI thread.  If the notification occurs on the main thread
 * then execution is delegated to the super class immediately.  If execution is not on the main thread
 * then there are 2 scenarios that have to be considered.  
 * 
 * The first scenario is for a long operation
 * that has been exeucted with a progress meter, where the progress meter is displaying UI on the main
 * thread and there is a background thread that is being executed that the progress meter is monitoring.
 * For this scenario, the UI updates on the diagram viewer have been disabled so as to avoid concurrency
 * issues and it is desirable that the notifications continue to be handled on the worker thread so that
 * the progress meter doesn't lock up.
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
 */
class DiagramEventBrokerThreadSafe extends DiagramEventBroker {

	WeakReference editingDomainRef;
	
    public DiagramEventBrokerThreadSafe(TransactionalEditingDomain editingDomain) {
        super();
        editingDomainRef = new WeakReference(editingDomain);
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.core.listener.DiagramEventBroker#resourceSetChanged(org.eclipse.emf.transaction.ResourceSetChangeEvent)
     */
    public void resourceSetChanged(ResourceSetChangeEvent event) {
        if (Display.getCurrent() == null) {
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
        }
        
        super.resourceSetChanged(event);
    }
    
    private boolean shouldSynchronizeWithMainThread(ResourceSetChangeEvent event) {
    	
    	for (Iterator i = event.getNotifications().iterator(); i.hasNext();) {
        	final Notification notification = (Notification) i.next();
        	Set nlSet = getInterestedNotificationListeners(notification, false);
        	Iterator iter = nlSet.iterator();
        	while (iter.hasNext()) {
        		Object listener = iter.next();
            	if (listener instanceof NotificationForEditPartsListener) {
            		EditPartViewer viewer = ((NotificationForEditPartsListener)listener).getViewer();
            		if (viewer instanceof DiagramGraphicalViewer) {
                        if (!((DiagramGraphicalViewer)viewer).areUpdatesDisabled()) {
                        	return true;
                        }
                    }
            	}
        	}
         }
        
        return false;
    }
    
    private void internal_resourceSetChanged(ResourceSetChangeEvent event) {
        super.resourceSetChanged(event);
    }
}