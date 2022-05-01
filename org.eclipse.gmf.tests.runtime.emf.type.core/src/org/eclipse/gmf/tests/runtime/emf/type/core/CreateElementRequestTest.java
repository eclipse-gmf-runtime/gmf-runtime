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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.SpecializationType;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.GetEditContextRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Department;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Employee;
import org.eclipse.gmf.tests.runtime.emf.type.core.internal.EmployeeType;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class CreateElementRequestTest
    extends AbstractEMFTypeTest {

    private CreateElementRequest fixture;

    private Department department;
    private Department department2;

    private Employee employee;

    public CreateElementRequestTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(CreateElementRequestTest.class);
    }

    protected void doModelSetup(Resource resource) {

        department = (Department) getEmployeeFactory().create(
            getEmployeePackage().getDepartment());
        department.setName("Department"); //$NON-NLS-1$
        resource.getContents().add(department);
        
        department2 = (Department) getEmployeeFactory().create(
            getEmployeePackage().getDepartment());
        department2.setName("Department2"); //$NON-NLS-1$
        resource.getContents().add(department2);

        employee = (Employee) getEmployeeFactory().create(
            getEmployeePackage().getEmployee());
        resource.getContents().add(employee);
    }

    protected CreateElementRequest getFixture() {
        return fixture;
    }

    protected void setFixture(CreateElementRequest fixture) {
        this.fixture = fixture;
    }

    public void test_getEditHelperContext_eObject() {
        // Verifies that the container of a create element request is used
        // by default as the edit helper context, when no overrides are
        // implemented.

        setFixture(new CreateElementRequest(getEditingDomain(), department,
            EmployeeType.EMPLOYEE));
        Object editHelperContext = getFixture().getEditHelperContext();
        assertEquals(department, editHelperContext);
    }

    public void test_getEditHelperContext_elementType() {
        // Verifies that the edit helper context can be customized by clients

        setFixture(new CreateElementRequest(getEditingDomain(), department,
            EmployeeType.TOP_SECRET));
        Object editHelperContext = getFixture().getEditHelperContext();
        IElementType elementType = ElementTypeRegistry.getInstance()
            .getElementType(editHelperContext);

        // Edit helper for security cleared employees returns the secret
        // department element type as is edit helper context.
        assertEquals(EmployeeType.SECRET_DEPARTMENT, elementType);

        // Get the edit command and execute it
        ICommand command = elementType.getEditCommand(getFixture());
        assertNotNull(command);
        assertTrue(command.canExecute());

        try {
            command.execute(new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e.getLocalizedMessage());
        }
        CommandResult result = command.getCommandResult();
        assertEquals(IStatus.OK, result.getStatus().getCode());

        Employee topSecretEmployee = null;

        topSecretEmployee = (Employee) result.getReturnValue();

        // Verify that the edit helper override set the correct security
        // clearance value
        assertNotNull(topSecretEmployee);
        assertTrue(topSecretEmployee.isSecurityClearance());
    }

    public void test_invalidateEditHelperContext() {
        // Verifies that the edit helper context request is invalidated when the
        // container is changed

        CreateElementRequest request = new CreateElementRequest(
            getEditingDomain(), department, EmployeeType.EMPLOYEE);
        setFixture(request);
        Object departmentContext = getFixture().getEditHelperContext();
        assertEquals(department, departmentContext);

        request.setContainer(employee);
        Object employeeContext = getFixture().getEditHelperContext();
        assertNotSame(departmentContext, employeeContext);
    }
    
    /**
     * Verifies that getting the editing domain from a request in which neither
     * the editing domain nor the container have been specified returns 'null'.
     */
    public void test_getEditingDomain_noContainer_131766() {
        CreateElementRequest request = new CreateElementRequest(
            EmployeeType.DEPARTMENT);
        TransactionalEditingDomain domain = request.getEditingDomain();
        assertNull(domain);
    }
    
    /**
     * Verifies that setting the container on a CreateElementRequest
     * does not result in the creation of a new GetElementContextRequest.
     */
    public void test_noNewRequestWhenSetContainer_132253() {

        final GetEditContextRequest[] contextRequestArray = new GetEditContextRequest[] {null};
        ElementTypeRegistry.getInstance().register(
            new SpecializationType("132253", null, "132253", //$NON-NLS-1$ //$NON-NLS-2$
                new IElementType[] {EmployeeType.MANAGER}, null, null,
                new AbstractEditHelperAdvice() {

                    protected ICommand getBeforeEditContextCommand(
                            GetEditContextRequest request) {
                        contextRequestArray[0] = request;
                        return null;
                    };
                }));

        CreateElementRequest request = new CreateElementRequest(department,
            EmployeeType.MANAGER);

        request.getEditHelperContext();
        GetEditContextRequest contextRequest1 = contextRequestArray[0];
        contextRequestArray[0] = null;

        request.setContainer(department2);

        request.getEditHelperContext();
        GetEditContextRequest contextRequest2 = contextRequestArray[0];
        contextRequestArray[0] = null;

        assertSame(contextRequest1, contextRequest2);
    }
    
    /**
     * Verifies that setting the container or containment feature during the
     * request to get the edit context does not clear the edit context request,
     * causing the request to be made again the next time we look for the edit
     * helper context.
     */
    public void test_singleGetEditContextRequest_129582() {

        final GetEditContextRequest[] contextRequestArray = new GetEditContextRequest[] {null};
        ElementTypeRegistry.getInstance().register(
            new SpecializationType("132253", null, "132253", //$NON-NLS-1$ //$NON-NLS-2$
                new IElementType[] {EmployeeType.MANAGER}, null, null,
                new AbstractEditHelperAdvice() {

                    protected ICommand getBeforeEditContextCommand(
                            GetEditContextRequest request) {
                        contextRequestArray[0] = request;
                        CreateElementRequest createRequest = (CreateElementRequest) request
                            .getEditCommandRequest();
                        createRequest
                            .setContainmentFeature(getEmployeePackage()
                                .getDepartment_Manager());
                        return null;
                    };
                }));

        CreateElementRequest request = new CreateElementRequest(department,
            EmployeeType.MANAGER);

        request.getEditHelperContext();
        GetEditContextRequest contextRequest1 = contextRequestArray[0];
        contextRequestArray[0] = null;

        request.setContainer(department2);

        request.getEditHelperContext();
        GetEditContextRequest contextRequest2 = contextRequestArray[0];
        contextRequestArray[0] = null;

        assertSame(contextRequest1, contextRequest2);
    }
}
