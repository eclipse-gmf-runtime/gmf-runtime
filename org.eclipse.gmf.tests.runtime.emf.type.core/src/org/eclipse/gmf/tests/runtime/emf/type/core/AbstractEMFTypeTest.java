/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.emf.type.core;

import java.util.Arrays;
import java.util.LinkedHashSet;

import junit.framework.TestCase;

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
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelperAdvice;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeeFactory;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage;

public class AbstractEMFTypeTest
    extends TestCase {

    private TransactionalEditingDomain editingDomain;

    private Resource resource;

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
        resource = editingDomain
            .getResourceSet()
            .createResource(
                URI
                    .createURI("null://org.eclipse.gmf.tests.runtime.emf.type.core")); //$NON-NLS-1$

        RecordingCommand command = new RecordingCommand(editingDomain) {

            protected void doExecute() {
                doModelSetup();
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
        resource.unload();
        editingDomain.dispose();
    }

    protected void doModelSetup() {
        // Do nothing.
    }

    protected TransactionalEditingDomain getEditingDomain() {
        return editingDomain;
    }

    protected EmployeePackage getEmployeePackage() {
        return employeePkg;
    }

    protected Resource getResource() {
        return resource;
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

	protected IEditHelperAdvice[] getWildcardAdvice() {
		// get wildcard advices by finding advices on the default element type
		//     (which can only have wildcard advice)
		IElementType dflt = ElementTypeRegistry.getInstance().getType(
				"org.eclipse.gmf.runtime.emf.type.core.default"); //$NON-NLS-1$
		assertNotNull(dflt);
		return ElementTypeRegistry.getInstance().getEditHelperAdvice(dflt);
	}

	protected IEditHelperAdvice[] getNonWildcardAdvice(IElementType type) {
	    	LinkedHashSet result = new LinkedHashSet();
	    	
	    	result.addAll(Arrays.asList(ElementTypeRegistry.getInstance().getEditHelperAdvice(type)));
	    	result.removeAll(Arrays.asList(getWildcardAdvice()));
	    	
	    	return (IEditHelperAdvice[]) result.toArray(new IEditHelperAdvice[result.size()]);
	   }

	protected IEditHelperAdvice[] getNonWildcardAdvice(EObject element) {
	    	LinkedHashSet result = new LinkedHashSet();
	    	
	    	result.addAll(Arrays.asList(ElementTypeRegistry.getInstance().getEditHelperAdvice(element)));
	    	result.removeAll(Arrays.asList(getWildcardAdvice()));
	    	
	    	return (IEditHelperAdvice[]) result.toArray(new IEditHelperAdvice[result.size()]);
	   }
}
