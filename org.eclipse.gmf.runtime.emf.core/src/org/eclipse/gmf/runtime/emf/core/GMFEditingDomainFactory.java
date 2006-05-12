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

package org.eclipse.gmf.runtime.emf.core;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.WorkspaceEditingDomainFactory;
import org.eclipse.gmf.runtime.emf.core.internal.resources.PathmapManager;
import org.eclipse.gmf.runtime.emf.core.util.CrossReferenceAdapter;


/**
 * Factory for {@link TransactionalEditingDomain}s that are properly configured
 * to support a GMF application.  This factory should be preferred over the
 * {@link org.eclipse.emf.workspace.WorkspaceEditingDomainFactory} because it
 * attaches adapters and sets up other other properties of the resource set
 * and/or editing domain on the client's behalf.
 *
 * @author Christian W. Damus (cdamus)
 */
public class GMFEditingDomainFactory
	extends WorkspaceEditingDomainFactory {

    /**
     * The single shared instance of the GMF editing domain factory.
     */
    private static GMFEditingDomainFactory instance  = new GMFEditingDomainFactory();

    /**
     * Gets the single shared instance of the GMF editing domain factory.
     * 
     * @return the editing domain factory
     */
    public static WorkspaceEditingDomainFactory getInstance() {
        return instance;
    }
    
	public TransactionalEditingDomain createEditingDomain() {
		TransactionalEditingDomain result = super.createEditingDomain();
		configure(result);
		return result;
	}

	public TransactionalEditingDomain createEditingDomain(IOperationHistory history) {
		TransactionalEditingDomain result = super.createEditingDomain(history);
		configure(result);
		return result;
	}

	public TransactionalEditingDomain createEditingDomain(ResourceSet rset, IOperationHistory history) {
		TransactionalEditingDomain result = super.createEditingDomain(rset, history);
		configure(result);
		return result;
	}

	public TransactionalEditingDomain createEditingDomain(ResourceSet rset) {
		TransactionalEditingDomain result = super.createEditingDomain(rset);
		configure(result);
		return result;
	}

	/**
	 * Configures the specified editing domain for correct functioning in the
	 * GMF environment.
	 * 
	 * @param domain the new editing domain
	 */
	protected void configure(TransactionalEditingDomain domain) {
		ResourceSet rset = domain.getResourceSet();
		
		// ensure that the cross-referencing adapter is installed
		if (CrossReferenceAdapter.getExistingCrossReferenceAdapter(rset) == null) {
			rset.eAdapters().add(new CrossReferenceAdapter());
		}
		
		// ensure that the path map manager is installed
		if (PathmapManager.getExistingPathmapManager(rset) == null) {
			rset.eAdapters().add(new PathmapManager());
		}
	}
}
