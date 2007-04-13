/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.services.eventbroker;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.diagram.core.providers.AbstractDiagramEventBrokerProvider;

/**
 * Operation used in creating a Diagram evnet broker instance
 * 
 * @author MMostafa
 */
public class CreateDiagramEventBrokerOperation
    implements IOperation {

    // the transaction editing domain to use
    private TransactionalEditingDomain editingDomain;

    /**
     * constructor
     * 
     * @param editingDomain
     *            the editing domain to use during this operation
     */
    public CreateDiagramEventBrokerOperation(
            TransactionalEditingDomain editingDomain) {
        this.editingDomain = editingDomain;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse.gmf.runtime.common.core.service.IProvider)
     */
    public Object execute(IProvider provider) {
        if (provider instanceof AbstractDiagramEventBrokerProvider)
            return ((AbstractDiagramEventBrokerProvider) provider)
                .createDiagramEventBroker(editingDomain);
        else
            return null;
    }

    /**
     * @return editing domain
     */
    public TransactionalEditingDomain getEditingDomain() {
        return editingDomain;
    }

}
