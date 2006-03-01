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

package org.eclipse.gmf.runtime.diagram.core;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.WorkspaceEditingDomainFactory;
import org.eclipse.gmf.runtime.diagram.core.internal.listener.NotationSemProc;
import org.eclipse.gmf.runtime.emf.core.GMFEditingDomainFactory;


/**
 * Factory for {@link TransactionalEditingDomain}s that are properly configured
 * to support a GMF diagram application. This factory should be preferred over
 * the {@link GMFEditingDomainFactory} because it attaches a listener required
 * to update the notation model after changes to the semantic model.
 * 
 * @author cmahoney
 */
public class DiagramEditingDomainFactory
    extends GMFEditingDomainFactory {

    /**
     * The single shared instance of the GMF diagram editing domain factory.
     */
    private static DiagramEditingDomainFactory instance = new DiagramEditingDomainFactory();

    /**
     * Gets the single shared instance of the GMF diagram editing domain factory.
     * 
     * @return the editing domain factory
     */
    public static WorkspaceEditingDomainFactory getInstance() {
        return instance;
    }  
    
    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.emf.core.GMFEditingDomainFactory#configure(org.eclipse.emf.transaction.TransactionalEditingDomain)
     */
    protected void configure(TransactionalEditingDomain domain) {
        super.configure(domain);
        domain.addResourceSetListener(new NotationSemProc());
    }

}
