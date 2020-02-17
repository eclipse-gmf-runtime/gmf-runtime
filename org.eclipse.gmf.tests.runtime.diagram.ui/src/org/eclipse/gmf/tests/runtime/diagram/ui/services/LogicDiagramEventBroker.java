/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.services;

import java.lang.ref.WeakReference;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.core.listener.DiagramEventBroker;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationListener;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationPreCommitListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;


public class LogicDiagramEventBroker
    extends DiagramEventBroker {
    
    static private boolean isCreated = false;
    static private boolean isMethodInvoked = false;
    
    public LogicDiagramEventBroker(){
        isCreated = true;
    }
    
    static public boolean isCreated() {
        return isCreated;
    }
    
    static public boolean isMethodInvoked() {
        return isMethodInvoked;
    }
    
    WeakReference editingDomainRef;
    
    public LogicDiagramEventBroker(TransactionalEditingDomain editingDomain) {
        super();
        isCreated = true;
        editingDomainRef = new WeakReference(editingDomain);
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.core.listener.DiagramEventBroker#resourceSetChanged(org.eclipse.emf.transaction.ResourceSetChangeEvent)
     */
    public void resourceSetChanged(ResourceSetChangeEvent event) {
        isMethodInvoked = true;
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

    public void addNotificationListener(EObject target, EStructuralFeature key, NotificationListener listener) {
        isMethodInvoked = true;
        super.addNotificationListener(target, key, listener);
    }

    public void addNotificationListener(EObject target, EStructuralFeature key, NotificationPreCommitListener listener) {
        isMethodInvoked = true;
        super.addNotificationListener(target, key, listener);
    }

    public void addNotificationListener(EObject target, NotificationListener listener) {
        isMethodInvoked = true;
        super.addNotificationListener(target, listener);
    }

    public void addNotificationListener(EObject target, NotificationPreCommitListener listener) {
        isMethodInvoked = true;
        super.addNotificationListener(target, listener);
    }

    protected void fireNotification(Notification event) {
        isMethodInvoked = true;
        super.fireNotification(event);
    }

    protected Set getInterestedNotificationListeners(Notification event, NotifierToKeyToListenersSetMap listeners) {
        isMethodInvoked = true;
        return super.getInterestedNotificationListeners(event, listeners);
    }

    protected NotifierToKeyToListenersSetMap getPostCommitListenersMap() {
        isMethodInvoked = true;
        return super.getPostCommitListenersMap();
    }

    protected NotifierToKeyToListenersSetMap getPreCommitListenersMap() {
        isMethodInvoked = true;
        return super.getPreCommitListenersMap();
    }

    protected void handleNotificationOnDeletedElement(ResourceSetChangeEvent event) {
        isMethodInvoked = true;
        super.handleNotificationOnDeletedElement(event);
    }

    public boolean isAggregatePrecommitListener() {
        isMethodInvoked = true;
        return super.isAggregatePrecommitListener();
    }

    protected boolean isDeleted(Set deletedObjects, EObject notifier) {
        isMethodInvoked = true;
        return super.isDeleted(deletedObjects, notifier);
    }

    public void removeNotificationListener(EObject target, NotificationListener listener) {
        isMethodInvoked = true;
        super.removeNotificationListener(target, listener);
    }

    public void removeNotificationListener(EObject target, NotificationPreCommitListener listener) {
        isMethodInvoked = true;
        super.removeNotificationListener(target, listener);
    }

    public void removeNotificationListener(EObject target, Object key, NotificationListener listener) {
        isMethodInvoked = true;
        super.removeNotificationListener(target, key, listener);
    }

    public void removeNotificationListener(EObject target, Object key, NotificationPreCommitListener listener) {
        isMethodInvoked = true;
        super.removeNotificationListener(target, key, listener);
    }

   
    protected boolean shouldIgnoreNotification(Notification notification) {
        isMethodInvoked = true;
        return super.shouldIgnoreNotification(notification);
    }

    public Command transactionAboutToCommit(ResourceSetChangeEvent event) {
        isMethodInvoked = true;
        return super.transactionAboutToCommit(event);
    }
    
    

}
