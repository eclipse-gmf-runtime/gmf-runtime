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

import junit.framework.TestCase;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.TransactionalCommandStack;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage;

public class AbstractEMFTypeTest
    extends TestCase {

    private TransactionalEditingDomain editingDomain;

    private Resource resource;

    private EmployeePackage employeePkg;

    private EFactory employeeFactory;

    protected AbstractEMFTypeTest(String name) {
        super(name);
    }

    protected void setUp()
        throws Exception {
        super.setUp();

        employeePkg = EmployeePackage.eINSTANCE;
        employeeFactory = employeePkg.getEFactoryInstance();

        editingDomain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();
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

    protected EFactory getEmployeeFactory() {
        return employeeFactory;
    }
}
