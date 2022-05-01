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

package org.eclipse.gmf.tests.runtime.emf.type.core;

import java.util.Arrays;
import java.util.LinkedHashSet;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.TransactionalCommandStack;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.core.GMFEditingDomainFactory;
import org.eclipse.gmf.runtime.emf.type.core.ClientContextManager;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IClientContext;
import org.eclipse.gmf.runtime.emf.type.core.IEditHelperContext;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelperAdvice;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeeFactory;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage;

import junit.framework.TestCase;

public class AbstractEMFTypeTest
    extends TestCase {

    private TransactionalEditingDomain editingDomain;

    private Resource defaultResource;
    
    private Resource resourceWithContext;

    private EmployeePackage employeePkg;

    private EmployeeFactory employeeFactory;

    protected AbstractEMFTypeTest(String name) {
        super(name);
    }

    protected void setUp()
        throws Exception {
        super.setUp();

        employeePkg = EmployeePackage.eINSTANCE;
        employeeFactory = (EmployeeFactory) employeePkg.getEFactoryInstance();

        editingDomain = GMFEditingDomainFactory.getInstance().createEditingDomain();
        TransactionalEditingDomain.Registry.INSTANCE
				.add(
						"org.eclipse.gmf.tests.runtime.emf.type.core.EditingDomain", editingDomain); //$NON-NLS-1$
        
        defaultResource = editingDomain
            .getResourceSet()
            .createResource(
                URI
                    .createURI("null://org.eclipse.gmf.tests.runtime.emf.type.core")); //$NON-NLS-1$
        
        resourceWithContext = editingDomain
        .getResourceSet()
        .createResource(
            URI
                .createURI("null://org.eclipse.gmf.tests.runtime.emf.type.core.context")); //$NON-NLS-1$
    
        RecordingCommand command = new RecordingCommand(editingDomain) {

            protected void doExecute() {
                doModelSetup(defaultResource);
                doModelSetupWithContext(resourceWithContext);
            };
        };

        try {
            ((TransactionalCommandStack) editingDomain.getCommandStack()).execute(command,
                null);

        } catch (RollbackException e) {
            fail("setUp() failed:" + e.getLocalizedMessage()); //$NON-NLS-1$
        }
    }

    protected void tearDown()
        throws Exception {
        super.tearDown();
        
        employeeFactory = null;
        employeePkg = null;
        defaultResource.unload();
        resourceWithContext.unload();
        editingDomain.dispose();
    }

    protected void doModelSetup(Resource resource) {
        // Do nothing.
    }
    
    protected void doModelSetupWithContext(Resource resource) {
        // Do nothing.
    }

    protected TransactionalEditingDomain getEditingDomain() {
        return editingDomain;
    }

    protected EmployeePackage getEmployeePackage() {
        return employeePkg;
    }

    protected Resource getResource() {
        return defaultResource;
    }
    
    protected Resource getResourceWithContext() {
        return resourceWithContext;
    }

    protected EmployeeFactory getEmployeeFactory() {
        return employeeFactory;
    }
    
    protected IStatus execute(ICommand command) {
    	assertTrue(command.canExecute());
    	
    	try {
    		IStatus result = command.execute(new NullProgressMonitor(), null);
    		assertTrue(result.isOK());
    		return result;
    	} catch (Exception e) {
    		fail("Command execution failed: " + e.getLocalizedMessage()); //$NON-NLS-1$
    		return Status.CANCEL_STATUS;  // won't get past fail() call
    	}
    }
    
    protected IStatus undo(ICommand command) {
    	assertTrue(command.canUndo());
    	
    	try {
    		IStatus result = command.undo(new NullProgressMonitor(), null);
    		assertTrue(result.isOK());
    		return result;
    	} catch (Exception e) {
    		fail("Command undo failed: " + e.getLocalizedMessage()); //$NON-NLS-1$
    		return Status.CANCEL_STATUS;  // won't get past fail() call
    	}
    }
    
    protected IStatus redo(ICommand command) {
    	assertTrue(command.canRedo());
    	
    	try {
    		IStatus result = command.redo(new NullProgressMonitor(), null);
    		assertTrue(result.isOK());
    		return result;
    	} catch (Exception e) {
    		fail("Command redo failed: " + e.getLocalizedMessage()); //$NON-NLS-1$
    		return Status.CANCEL_STATUS;  // won't get past fail() call
    	}
    }
    
	protected IEditHelperAdvice[] getWildcardAdvice(IClientContext clientContext) {
		// get wildcard advices by finding advices on the default element type
		//     (which can only have wildcard advice)
		IElementType dflt = ElementTypeRegistry.getInstance().getType(
				"org.eclipse.gmf.runtime.emf.type.core.default"); //$NON-NLS-1$
		assertNotNull(dflt);
		return ElementTypeRegistry.getInstance().getEditHelperAdvice(dflt, clientContext);
	}
	
	protected IEditHelperAdvice[] getNonWildcardAdvice(IElementType type) {
		IClientContext context = ClientContextManager.getInstance().getBinding(
				type);
		return getNonWildcardAdvice(type, context);
	}
	
	protected IEditHelperAdvice[] getNonWildcardAdvice(IElementType type, IClientContext context) {
		
		LinkedHashSet result = new LinkedHashSet();
		
		result.addAll(Arrays.asList(ElementTypeRegistry.getInstance()
				.getEditHelperAdvice(type, context)));
		result.removeAll(Arrays.asList(getWildcardAdvice(context)));

		return (IEditHelperAdvice[]) result
				.toArray(new IEditHelperAdvice[result.size()]);
	}
	
	protected IEditHelperAdvice[] getNonWildcardAdvice(IEditHelperContext context) {
		
		LinkedHashSet result = new LinkedHashSet();
		
		result.addAll(Arrays.asList(ElementTypeRegistry.getInstance()
				.getEditHelperAdvice(context)));
		result.removeAll(Arrays.asList(getWildcardAdvice(context.getClientContext())));

		return (IEditHelperAdvice[]) result
				.toArray(new IEditHelperAdvice[result.size()]);
	}

	protected IEditHelperAdvice[] getNonWildcardAdvice(EObject element) {
		IClientContext context = ClientContextManager.getInstance()
				.getClientContextFor(element);
		return getNonWildcardAdvice(element, context);
	}
	
	protected IEditHelperAdvice[] getNonWildcardAdvice(EObject element, IClientContext context) {
		
		LinkedHashSet result = new LinkedHashSet();
		
		result.addAll(Arrays.asList(ElementTypeRegistry.getInstance()
				.getEditHelperAdvice(element, context)));
		result.removeAll(Arrays.asList(getWildcardAdvice(context)));

		return (IEditHelperAdvice[]) result
				.toArray(new IEditHelperAdvice[result.size()]);
	}
}
