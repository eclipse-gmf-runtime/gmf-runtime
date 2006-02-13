/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.Request;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.diagram.ui.requests.EditCommandRequestWrapper;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;

/**
 * Abstract action to be subclassed for various delete from actions.
 * This Action is used to send a request that will destroy a semantic 
 * element.
 * 
 * @author schafe
 */
public abstract class AbstractDeleteFromAction
	extends DiagramAction {
	
    /**
     * The editing domain used by this action to make changes to the model.
     */
    private TransactionalEditingDomain editingDomain;
	
	/**
	 * Creates an <code>AbstractDeleteFromAction</code> with a default label.
	 *
	 * @param part The part this action will be associated with.
	 */
	public AbstractDeleteFromAction(IWorkbenchPart part) {
		super(part);
		
		
	}
 
	/**
	 * Constructor
	 * @param workbenchPage The workbench page associated with this action
	 */
	public AbstractDeleteFromAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
		
	}

	/**
	 *  Return the semantic request to destroy the element
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		return 	new EditCommandRequestWrapper(new DestroyElementRequest(getEditingDomain(), false));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#updateTargetRequest()
	 */
	protected void updateTargetRequest() {
		
		super.updateTargetRequest();

		DestroyElementRequest deleteReq = getDestroyElementRequest();

		// Reset the element to null
		deleteReq.setElementToDestroy((EObject)null);
	}

	/**
	 * Helper method for getting the <code>DestroyElementRequest</code>
	 * @return the DestroyElementRequest
	 */
	protected DestroyElementRequest getDestroyElementRequest() {

		EditCommandRequestWrapper theWrapper = (EditCommandRequestWrapper) getTargetRequest();

		DestroyElementRequest theRequest = (DestroyElementRequest) theWrapper
			.getEditCommandRequest();

		return theRequest;
	}
    
     /**
     * Gets my editing domain from my workbench part.
     * 
     * @return my editing domain
     */
    protected TransactionalEditingDomain getEditingDomain() {
        
        if (editingDomain == null) {
            // try adapting the workbench part
            IWorkbenchPart part = getWorkbenchPart();

            if (part != null) {
                IEditingDomainProvider edProvider = (IEditingDomainProvider) part
                    .getAdapter(IEditingDomainProvider.class);

                if (edProvider != null) {
                    editingDomain = (TransactionalEditingDomain) edProvider
                        .getEditingDomain();
                }
            }
        }
        return editingDomain;
    }

}
