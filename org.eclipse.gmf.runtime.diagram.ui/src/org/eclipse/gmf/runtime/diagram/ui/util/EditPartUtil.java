/******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.util;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.InternalTransaction;
import org.eclipse.emf.transaction.impl.InternalTransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.emf.core.util.PackageUtil;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * provides different utility functions for the EditPart
 * @author mmostafa
 */

public class EditPartUtil {
	
	/** 
	 * gets the <code>Editpart</code>'s semantic element Class Id, this could be used to
	 * check the semantic element type
	 * @param editpart the owner of the semantic element
	 * @return the semantic element class Id
	 */
	public static String getSemanticEClassName(IGraphicalEditPart editPart) {
		if (editPart.getModel() instanceof View){
			View view = (View)editPart.getModel();
			EObject element = view.getElement();
			return element == null ? null : PackageUtil.getID(EMFCoreUtil.getProxyClass(element));
		}
		return null;
	}
    
    /**
     * remove all the canonical edit policies on the passed edit part, if the considerChildren 
     * flag is ON, it will remove the canonical editpolicies on every edit part in the 
     * passed edit part hirarchy
     * @param editPart the edit part to remove the edit policy from
     * @param considerChildren determine the the canonical edit policies will be removed from 
     *        children as well or not
     */
    public static void removeCanonicalEditPolicies(EditPart editPart, boolean considerChildren) {
        EditPolicy ep = editPart.getEditPolicy(EditPolicyRoles.CANONICAL_ROLE);
        if (ep!=null){
            editPart.removeEditPolicy(ep);
        }
        if (considerChildren){
            Iterator childrenIterator = editPart.getChildren().iterator();
            removeCanonicalEditPolicies(childrenIterator);
            if (editPart instanceof AbstractGraphicalEditPart){
                AbstractGraphicalEditPart gEP = (AbstractGraphicalEditPart)editPart;
                Iterator sourceConnectionsIterator = gEP.getSourceConnections().iterator();
                removeCanonicalEditPolicies(sourceConnectionsIterator);
                Iterator targetConnectionsIterator = gEP.getTargetConnections().iterator();
                removeCanonicalEditPolicies(targetConnectionsIterator);
            }
        }
    }

    private static void removeCanonicalEditPolicies(Iterator childrenIterator) {
        while (childrenIterator.hasNext()) {
            EditPart child = (EditPart)childrenIterator.next();
            removeCanonicalEditPolicies(child,true);
        }
    }
	
	/**
	 * Used as general utility usually in the context of handling notifications to ensure that a runnable will
	 * be run in the appropriate thread context so that SWT or other reservered resources can be accessed.  Should be only
	 * be used when the client is sure that in the stack of execution SWT resources will be accessed directly.  Otherwise,
	 * this could incur a performance penalty when handling notifications.  Also, it may spawn the runnable asynchronously
	 * to the main thread which may break assumptions on ordering when the events are handled.
	 * 
	 * @param editPart the <code>EditPart</code> that is receiving notification or is being accessed potentially
	 * on a worker thread that needs to ensure safe access to SWT or other resources reserved for the main thread.
	 * @param runThreadSafe the <code>Runnable</code> that is to be executed in a thread safe manner.
	 */
	public static void synchronizeRunnableToMainThread(IGraphicalEditPart editPart, Runnable runThreadSafe) {
		InternalTransactionalEditingDomain editingDomain = (InternalTransactionalEditingDomain)editPart.getEditingDomain();
        
		if (Display.getCurrent() == null && editingDomain != null && editingDomain.getActiveTransaction() != null) {
			if (editingDomain != null) {
	        	PlatformUI.getWorkbench().getDisplay().syncExec(editingDomain.createPrivilegedRunnable(runThreadSafe));
	            return;
	        }
	        else {
	        	PlatformUI.getWorkbench().getDisplay().asyncExec(runThreadSafe);
			}
		}
		else {
			runThreadSafe.run();
		}
	}
	
    /**
     * Checks if the current active transaction is a Write transaction or not
     * unprotected transaction are not considered write transaction
     * 
     * @param editPart the <code>IGraphicalEditPart</code> that is used as a context to find the currently
     * running transaction if any.
     * @param includeUnprotected <code>boolean</code> value that if <code>true</code> will consider unprotected
     * transactions when determining if a write transaction is in progress.
     * @param otherThread <code>boolean</code> value that if <code>true</code>, will verify whether there is
     * an active transaction only when on a different thread then the caller.  This is useful to determine if a 
     * deadlock scenario will occur.
     * @return <code>true</code> if the current active transaction is a write transaction 
     */
    public static boolean isWriteTransactionInProgress(IGraphicalEditPart editPart, boolean includeUnprotected, boolean otherThread) {
        TransactionalEditingDomain theEditingDomain = editPart.getEditingDomain();
        if (theEditingDomain instanceof InternalTransactionalEditingDomain){
            InternalTransactionalEditingDomain internalEditingDomain = 
                (InternalTransactionalEditingDomain)theEditingDomain;
            InternalTransaction transaction = internalEditingDomain.getActiveTransaction();
            if (transaction!=null && !transaction.isReadOnly()) {
            	if (!includeUnprotected) {
	                Object unprotectedMode = transaction.getOptions().get(Transaction.OPTION_UNPROTECTED); 
	                if (Boolean.TRUE.equals(unprotectedMode)) {
	                	return false;
	                }
            	}
                
            	if (otherThread && Thread.currentThread() != transaction.getOwner())
            		return true;
            	else if (!otherThread)
            		return true;
            }
        }
        return false;
    }
}
