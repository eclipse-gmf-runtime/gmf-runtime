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
package org.eclipse.gmf.tests.runtime.emf.type.core.commands;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.AbstractEMFTypeTest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Client;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeeFactory;

/**
 * Tests the extensible {@link DestroyElementCommand}.
 * 
 * @author Christian W. Damus (cdamus)
 */
public class DestroyElementCommandTest
    extends AbstractEMFTypeTest {

	static final String ANNOTATION_SOURCE = "org.eclipse.gmf.tests.runtime.emf.type.core"; //$NON-NLS-1$
	
    private Customer parentCompany;
    private Customer customer;
    private Client billieJo;
    private Client jimBob;
    
    private EAnnotation billieJoAnnotation;
    private EAnnotation jimBobAnnotation;

    public DestroyElementCommandTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(DestroyElementCommandTest.class, "DestroyElementCommand Tests"); //$NON-NLS-1$
    }

    /**
     * Tests just the functionality of the destroy command, itself.
     */
    public void test_destroyCommand_basic() {
    	DestroyElementCommand cmd = new DestroyElementCommand(
    			new DestroyElementRequest(billieJo, false));
    	
    	execute(cmd);
    	
    	assertDestroyed(billieJo);
    	
    	// annotation was not destroyed
    	assertSame(billieJoAnnotation.eContainer(), customer);
    	
    	// but reference to Billie Jo was cleared
    	assertEquals(0, billieJoAnnotation.getReferences().size());
    	
    	undo(cmd);
    	
    	assertSame(customer, billieJo.eContainer());
    	assertSame(billieJoAnnotation.eContainer(), customer);
    	assertTrue(billieJoAnnotation.getReferences().contains(billieJo));
    	
    	redo(cmd);
    	
    	assertDestroyed(billieJo);
    	assertSame(billieJoAnnotation.eContainer(), customer);
    	assertEquals(0, billieJoAnnotation.getReferences().size());
   }

    /**
     * Tests that we cannot destroy detached elements.
     */
    public void test_destroyCommand_detached() {
    	Client detachedClient = getEmployeeFactory().createClient();
    	
    	DestroyElementRequest req = new DestroyElementRequest(detachedClient, false);
    	
    	assertNull(req.getEditHelperContext());
    	
    	DestroyElementCommand cmd = new DestroyElementCommand(req);
    	
    	assertFalse(cmd.canExecute());
   }
    
    /**
     * Tests that dependents are correctly destroyed, as well.
     */
    public void test_destroyCommand_withDependents() {
    	DestroyElementRequest req = new DestroyElementRequest(billieJo, false);
    	IElementType type = ElementTypeRegistry.getInstance().getElementType(
    			req.getEditHelperContext());
    	
    	assertNotNull(type);
    	
    	ICommand cmd = type.getEditCommand(req);
    	
    	assertNotNull(cmd);
    	
    	execute(cmd);
    	
    	assertDestroyed(billieJo);
    	assertDestroyed(billieJoAnnotation);
    	
    	undo(cmd);
    	
    	assertSame(customer, billieJo.eContainer());
    	assertSame(billieJoAnnotation.eContainer(), customer);
    	assertTrue(billieJoAnnotation.getReferences().contains(billieJo));
    	
    	redo(cmd);
    	
    	assertDestroyed(billieJo);
    	assertDestroyed(billieJoAnnotation);
   }
    
    /**
     * Tests that all contents (recursively) are correctly destroyed, as well.
     * Incidentally tests the destruction of resource roots.
     */
    public void test_destroyCommand_withContainment() {
    	// destroy the parent company (resource root)
    	DestroyElementRequest req = new DestroyElementRequest(parentCompany, false);
    	IElementType type = ElementTypeRegistry.getInstance().getElementType(
    			req.getEditHelperContext());
    	
    	assertNotNull(type);
    	
    	ICommand cmd = type.getEditCommand(req);
    	
    	assertNotNull(cmd);
    	
    	execute(cmd);
    	
    	assertDestroyed(parentCompany);
    	assertDestroyed(customer);
    	assertDestroyed(billieJo);
    	assertDestroyed(billieJoAnnotation);
    	assertDestroyed(jimBob);
    	assertDestroyed(jimBobAnnotation);
    	
    	undo(cmd);
    	
    	assertSame(getResource(), parentCompany.eResource());
    	assertSame(parentCompany, customer.eContainer());
    	assertSame(customer, billieJo.eContainer());
    	assertSame(billieJoAnnotation.eContainer(), customer);
    	assertTrue(billieJoAnnotation.getReferences().contains(billieJo));
    	assertSame(customer, jimBob.eContainer());
    	assertSame(jimBobAnnotation.eContainer(), customer);
    	assertTrue(jimBobAnnotation.getReferences().contains(jimBob));
    	
    	redo(cmd);
    	
    	assertDestroyed(parentCompany);
    	assertDestroyed(customer);
    	assertDestroyed(billieJo);
    	assertDestroyed(billieJoAnnotation);
    	assertDestroyed(jimBob);
    	assertDestroyed(jimBobAnnotation);
    }
    
    //
    // Test framework methods
    //
    
    protected void doModelSetup() {
        parentCompany = getEmployeeFactory().createCustomer();
        parentCompany.setName("GlobalMega"); //$NON-NLS-1$
        getResource().getContents().add(parentCompany);
        
        customer = getEmployeeFactory().createCustomer();
        customer.setName("Acme"); //$NON-NLS-1$
        parentCompany.getSubsidiaries().add(customer);
        
        billieJo = createClient("Billie Jo", "Swanson", customer); //$NON-NLS-1$ //$NON-NLS-2$
        billieJoAnnotation = createAnnotation(billieJo);
        jimBob = createClient("Jim Bob", "Jones", customer); //$NON-NLS-1$ //$NON-NLS-2$
        jimBobAnnotation = createAnnotation(jimBob);
    }
    
    protected Client createClient(String firstName, String lastName, Customer customer) {
    	Client result = EmployeeFactory.eINSTANCE.createClient();
        result.setFirstName(firstName);
        result.setLastName(lastName);
        customer.getRepresentatives().add(result);
        
        return result;
    }
    
    protected EAnnotation createAnnotation(Client client) {
    	EAnnotation result = EcoreFactory.eINSTANCE.createEAnnotation();
    	
    	result.setSource(ANNOTATION_SOURCE);
    	result.getReferences().add(client);
    	client.getRepresents().getEAnnotations().add(result);
    	
    	return result;
    }
    
    protected void assertDestroyed(EObject eObject) {
    	assertTrue((eObject.eContainer() == null)
    			|| (eObject.eContainer() instanceof ChangeDescription));
    	
    	// no references exist to any other object.  Note that there should not
    	//    be a container reference of type ChangeDescription  :-)
    	for (Iterator iter = eObject.eClass().getEAllReferences().iterator(); iter.hasNext();) {
    		EReference next = (EReference) iter.next();
    		
    		assertFalse(eObject.eIsSet(next));
    	}
    	
    	// in case it was a root object
    	assertFalse(getResource().getContents().contains(eObject));
    }
}
