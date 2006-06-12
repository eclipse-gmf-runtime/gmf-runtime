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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.command.ICompositeCommand;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyDependentsRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.AbstractEMFTypeTest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Client;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.Customer;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeeFactory;
import org.eclipse.gmf.tests.runtime.emf.type.core.internal.ClientDependentsAdvice;
import org.eclipse.gmf.tests.runtime.emf.type.core.internal.DestroyCustomerAdvice;

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
    	assertNoDuplicates(cmd);
    	
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
    	assertNoDuplicates(cmd);
    	
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

    /**
	 * Tests that we completely destroy cross-resource-contained objects, also
	 * (removing them from their resource).
	 */
    public void test_destroy_crossResourceContained_136738() {
    	// use the Ecore metamodel because EXTLibrary does not have any
    	//    cross-resource containment support
    	
    	// create a couple of resources
    	final Resource res1 = getEditingDomain().getResourceSet().createResource(
    			URI.createURI("null://res1.ecore")); //$NON-NLS-1$
    	final Resource res2 = getEditingDomain().getResourceSet().createResource(
    			URI.createURI("null://res2.ecore")); //$NON-NLS-1$
    	
    	// the Ecore model
    	final EPackage pkg1 = EcoreFactory.eINSTANCE.createEPackage();
    	pkg1.setName("package1"); //$NON-NLS-1$
    	final EClass class1 = EcoreFactory.eINSTANCE.createEClass();
    	class1.setName("Class1"); //$NON-NLS-1$
    	final EClass class2 = EcoreFactory.eINSTANCE.createEClass();
    	class2.setName("Class2"); //$NON-NLS-1$
    	
    	// establish cross-resource containment
        RecordingCommand command = new RecordingCommand(getEditingDomain()) {
            protected void doExecute() {
                res1.getContents().add(pkg1);
                pkg1.getEClassifiers().add(class1);
                pkg1.getEClassifiers().add(class2);
                class2.getESuperTypes().add(class1);  // set a reference feature
                
                res2.getContents().add(class2);
            }};
        getEditingDomain().getCommandStack().execute(command);
    	
        // check that we have cross-resource containment
        assertSame(pkg1, class2.eContainer());
        assertSame(res2, class2.eResource());
        
    	DestroyElementRequest req = new DestroyElementRequest(class2, false);
    	IElementType type = ElementTypeRegistry.getInstance().getElementType(
    			req.getEditHelperContext());
    	
    	assertNotNull(type);
    	
    	ICommand cmd = type.getEditCommand(req);
    	
    	assertNotNull(cmd);
    	assertNoDuplicates(cmd);
    	
    	execute(cmd);
    	
    	assertDestroyed(class2);
    	assertNull(class2.eResource());
    	
    	undo(cmd);
    	
    	assertSame(pkg1, class2.eContainer());
    	assertSame(res2, class2.eResource());
    	assertTrue(class2.getESuperTypes().contains(class1));  // check the reference
    	
    	redo(cmd);
    	
    	assertDestroyed(class2);
    	assertNull(class2.eResource());
   }
    
    /**
	 * Tests that the element to destroy in a DestroyRequest is the same in the
	 * before advice as it is in the after advice.
	 */
    public void test_preserveElementToDestroy_142561() {

		DestroyElementRequest req = new DestroyElementRequest(parentCompany,
				false);
		IElementType type = ElementTypeRegistry.getInstance().getElementType(
				req.getEditHelperContext());

		req.setParameter(DestroyCustomerAdvice.BEFORE, new ArrayList());
		req.setParameter(DestroyCustomerAdvice.AFTER, new ArrayList());

		assertNotNull(type);

		ICommand cmd = type.getEditCommand(req);

		assertNotNull(cmd);
		assertNoDuplicates(cmd);

		execute(cmd);

		assertDestroyed(parentCompany);

		// verify that the after advice sees the same elements to destroy in the reverse order
		List before = (List) req.getParameter(DestroyCustomerAdvice.BEFORE);
		List after = (List) req.getParameter(DestroyCustomerAdvice.AFTER);

		Collections.reverse(after);
		assertEquals(before, after);
	}
    
    /**
	 * Verifies the advice on the DestroyDependentsRequest (e.g.,
	 * ClientDependentsAdvice) can access the initial element that was requested
	 * to be destroyed.
	 */
    public void test_initialElementToBeDestroyed_146559() {
    	DestroyElementRequest req = new DestroyElementRequest(parentCompany,
				false);
		IElementType type = ElementTypeRegistry.getInstance().getElementType(
				req.getEditHelperContext());
		
		assertNotNull(type);

		ICommand cmd = type.getEditCommand(req);

		assertNotNull(cmd);
		assertNoDuplicates(cmd);

		execute(cmd);

		assertDestroyed(parentCompany);

		// verify that the dependents advice sees the same initial element to destroy
		DestroyDependentsRequest dependentsRequest = (DestroyDependentsRequest) req
				.getParameter(DestroyElementRequest.DESTROY_DEPENDENTS_REQUEST_PARAMETER);
		Object initial = dependentsRequest
				.getParameter(ClientDependentsAdvice.INITIAL);
		
		assertEquals(parentCompany, initial);
    }
    
    //
    // Test framework methods
    //
    
    protected void doModelSetup(Resource resource) {
        parentCompany = getEmployeeFactory().createCustomer();
        parentCompany.setName("GlobalMega"); //$NON-NLS-1$
        resource.getContents().add(parentCompany);
        
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
    
    /**
	 * Tests that there are no duplicate commands to destroy the same element in
	 * <code>cmd</code>. Used to verify Bugzilla 145763.
	 * 
	 * @param cmd
	 *            the command to test
	 */
	private void assertNoDuplicates(ICommand cmd) {
		assertNoDuplicatesImpl(cmd, new ArrayList());
	}

	private void assertNoDuplicatesImpl(ICommand cmd, List toDestroy) {

		if (cmd instanceof DestroyElementCommand) {
			try {
				Method getElementToDestroy = DestroyElementCommand.class
						.getDeclaredMethod("getElementToDestroy", null); //$NON-NLS-1$
				getElementToDestroy.setAccessible(true);
				EObject element = (EObject) getElementToDestroy.invoke(
						(DestroyElementCommand) cmd, null);

				if (toDestroy.contains(element)) {
					fail("Duplicate destroy command for: " + element); //$NON-NLS-1$
				}
				toDestroy.add(element);
			} catch (NoSuchMethodException nsme) {
				fail("Unexpected exception: " + nsme); //$NON-NLS-1$
			} catch (IllegalAccessException iae) {
				fail("Unexpected exception: " + iae); //$NON-NLS-1$
			} catch (InvocationTargetException ite) {
				fail("Unexpected exception: " + ite); //$NON-NLS-1$
			}

		} else if (cmd instanceof ICompositeCommand) {
			for (Iterator i = ((ICompositeCommand) cmd).iterator(); i.hasNext();) {
				ICommand next = (ICommand) i.next();
				assertNoDuplicatesImpl(next, toDestroy);
			}
		}
	}
}
