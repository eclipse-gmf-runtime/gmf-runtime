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

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.emf.ui.services.action.AbstractModelActionFilterProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

/**
 * An action filter provider for the Diagram UI Actions plugin.
 * 
 * @author cmahoney
 */
public class DiagramActionFilterProvider
	extends AbstractModelActionFilterProvider {

	/**
	 * This string from XML is used to identify this provider
	 */
	private static final String CAN_DUPLICATE = "canDuplicate"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.ui.services.action.AbstractModelActionFilterProvider#doTestAttribute(java.lang.Object,
	 *      java.lang.String, java.lang.String)
	 */
	protected boolean doTestAttribute(Object target, String name, String value) {
		if (CAN_DUPLICATE.equals(name)) {
			return DuplicateActionDelegate.canDuplicate(getStructuredSelection(), PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getPartService().getActivePart());
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.ui.services.action.AbstractModelActionFilterProvider#doProvides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	protected boolean doProvides(IOperation operation) {
		return true;
	}
    
    /**
     * Finds my editing domain by adating the current selection to
     * <code>EObject</code>.
     */
    protected TransactionalEditingDomain getEditingDomain(Object target) {

        TransactionalEditingDomain result = null;
        IStructuredSelection selection = getStructuredSelection();

        if (selection != null && !selection.isEmpty()) {

            for (Iterator i = selection.iterator(); i.hasNext()
                && result == null;) {
                Object next = i.next();

                if (next instanceof IAdaptable) {
                    EObject element = (EObject) ((IAdaptable) next)
                        .getAdapter(EObject.class);

                    if (element != null) {
                        result = TransactionUtil.getEditingDomain(element);
                    }
                }
            }
        }

        return result;
    }

}
