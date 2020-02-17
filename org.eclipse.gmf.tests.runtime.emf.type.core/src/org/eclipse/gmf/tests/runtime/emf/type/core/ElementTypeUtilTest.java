/*
 * Copyright (c) 2015 Christian W. Damus and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Christian W. Damus - initial API and implementation 
 */

package org.eclipse.gmf.tests.runtime.emf.type.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.AbstractAdviceBindingDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.AdviceBindingAddedEvent;
import org.eclipse.gmf.runtime.emf.type.core.AdviceBindingInheritance;
import org.eclipse.gmf.runtime.emf.type.core.AdviceBindingRemovedEvent;
import org.eclipse.gmf.runtime.emf.type.core.ClientContextManager;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeAddedEvent;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRemovedEvent;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeUtil;
import org.eclipse.gmf.runtime.emf.type.core.IAdviceBindingDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.IClientContext;
import org.eclipse.gmf.runtime.emf.type.core.IContainerDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.IElementMatcher;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.IElementTypeRegistryListener2;
import org.eclipse.gmf.runtime.emf.type.core.IMetamodelType;
import org.eclipse.gmf.runtime.emf.type.core.ISpecializationType;
import org.eclipse.gmf.runtime.emf.type.core.MetamodelType;
import org.eclipse.gmf.runtime.emf.type.core.SpecializationType;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.tests.runtime.emf.type.core.employee.EmployeePackage;

/**
 * JUnit tests for the {@link ElementTypeUtil} class.
 */
public class ElementTypeUtilTest extends TestCase {

	private TestListener listener;

	public ElementTypeUtilTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(ElementTypeUtilTest.class, "ElementTypeUtil Tests"); //$NON-NLS-1$
	}

	protected ElementTypeRegistry getFixture() {
		return ElementTypeRegistry.getInstance();
	}

	/**
	 * Tests the sorting of element types in bottom-up specialization order.
	 */
	public void test_sort() {
		final IElementType employee = getFixture().getType("org.eclipse.gmf.tests.runtime.emf.type.core.employee"); //$NON-NLS-1$
		final IElementType manager = getFixture().getType("org.eclipse.gmf.tests.runtime.emf.type.core.manager"); //$NON-NLS-1$
		final IElementType executive = getFixture().getType("org.eclipse.gmf.tests.runtime.emf.type.core.executive"); //$NON-NLS-1$
		final IElementType department = getFixture().getType("org.eclipse.gmf.tests.runtime.emf.type.core.department"); //$NON-NLS-1$
		final IElementType secretDept = getFixture().getType(
				"org.eclipse.gmf.tests.runtime.emf.type.core.secretDepartment"); //$NON-NLS-1$

		// create a list in exactly the wrong order
		List<IElementType> types = new ArrayList<IElementType>(5);
		types.add(employee);
		types.add(department);
		types.add(manager);
		types.add(secretDept);
		types.add(executive);

		types = ElementTypeUtil.sortBySpecialization(types);

		assertPrecedes(executive, employee, types);
		assertPrecedes(manager, employee, types);
		assertPrecedes(executive, manager, types);
		assertPrecedes(secretDept, department, types);
	}

	/**
	 * Tests that a dynamically-added element type can be removed from the
	 * registry without options.
	 */
	public void test_deregister_noFlags() {
		final IMetamodelType type = type("dynamic.metamodel.typeToBeRemoved", EmployeePackage.eINSTANCE.getLocation()); //$NON-NLS-1$

		// Register the type now
		getFixture().register(type);

		listener.clear();

		assertTrue(ElementTypeUtil.deregister(type, ElementTypeUtil.NONE));

		listener.assertRemoved(type);
		listener.assertAdvicesRemoved(0);
	}

	/**
	 * Tests that a dynamically-added element type can be removed from the
	 * registry with its advices.
	 */
	public void test_deregister_withAdvices() {
		String id = "dynamic.metamodel.typeToBeRemoved"; //$NON-NLS-1$
		final IMetamodelType type = type(id, EmployeePackage.eINSTANCE.getLocation());

		// Register the type now
		getFixture().register(type);

		Set<IAdviceBindingDescriptor> advices = new HashSet<IAdviceBindingDescriptor>();
		advices.add(new MyAdviceBindingDescriptor("test.advice.1", id)); //$NON-NLS-1$
		advices.add(new MyAdviceBindingDescriptor("test.advice.2", id)); //$NON-NLS-1$

		// And the advices
		for (IAdviceBindingDescriptor next : advices) {
			getFixture().registerAdvice(next);
		}

		listener.clear();

		assertTrue(ElementTypeUtil.deregister(type, ElementTypeUtil.ADVICE_BINDINGS));

		listener.assertRemoved(type);
		for (IAdviceBindingDescriptor next : advices) {
			listener.assertRemoved(next);
		}
	}

	/**
	 * Tests that a dynamically-added element type can be removed from the
	 * registry with its specializations.
	 */
	public void test_deregister_withSubtypes() {
		final IMetamodelType type = type("dynamic.metamodel.typeToBeRemoved", EmployeePackage.eINSTANCE.getLocation()); //$NON-NLS-1$
		final ISpecializationType subtype1 = type("dynamic.specialization.typeToBeRemoved1", type); //$NON-NLS-1$
		final ISpecializationType subtype2 = type("dynamic.specialization.typeToBeRemoved2", type); //$NON-NLS-1$

		// Register the types now
		getFixture().register(type);
		getFixture().register(subtype1);
		getFixture().register(subtype2);

		listener.clear();

		assertTrue(ElementTypeUtil.deregister(type, ElementTypeUtil.SPECIALIZATIONS));

		listener.assertRemoved(type);
		listener.assertRemoved(subtype1);
		listener.assertRemoved(subtype2);
	}

	/**
	 * Tests that a dynamically-added element type can be removed from the
	 * registry with its specializations and its (and their) advice.
	 */
	public void test_deregister_withSubtypesAndAdvice() {
		String id = "dynamic.metamodel.typeToBeRemoved"; //$NON-NLS-1$
		final IMetamodelType type = type(id, EmployeePackage.eINSTANCE.getLocation());

		String subID1 = "dynamic.specialization.typeToBeRemoved1"; //$NON-NLS-1$
		final ISpecializationType subtype1 = type(subID1, type);

		String subID2 = "dynamic.specialization.typeToBeRemoved2"; //$NON-NLS-1$
		final ISpecializationType subtype2 = type(subID2, type);

		// Advise one of the specialization types
		Set<IAdviceBindingDescriptor> advices = new HashSet<IAdviceBindingDescriptor>();
		advices.add(new MyAdviceBindingDescriptor("test.advice.1", subID2)); //$NON-NLS-1$
		advices.add(new MyAdviceBindingDescriptor("test.advice.2", subID2)); //$NON-NLS-1$

		// Register the types now
		getFixture().register(type);
		getFixture().register(subtype1);
		getFixture().register(subtype2);

		// And the advices
		for (IAdviceBindingDescriptor next : advices) {
			getFixture().registerAdvice(next);
		}

		listener.clear();

		assertTrue(ElementTypeUtil.deregister(type, ElementTypeUtil.SPECIALIZATIONS | ElementTypeUtil.ADVICE_BINDINGS));

		listener.assertRemoved(type);
		listener.assertRemoved(subtype1);
		listener.assertRemoved(subtype2);
		for (IAdviceBindingDescriptor next : advices) {
			listener.assertRemoved(next);
		}
	}

	/**
	 * Tests that multiple related dynamically-added element types can be
	 * removed from the registry.
	 */
	public void test_deregister_multiple() {
		final IMetamodelType type = type("dynamic.metamodel.typeToBeRemoved", EmployeePackage.eINSTANCE.getLocation()); //$NON-NLS-1$
		final ISpecializationType subtype1 = type("dynamic.specialization.typeToBeRemoved1", type); //$NON-NLS-1$
		final ISpecializationType subtype2 = type("dynamic.specialization.typeToBeRemoved2", subtype1); //$NON-NLS-1$

		// Order the list top-down to test that they are processed bottom-up
		final List<IElementType> allTypes = Arrays.asList(type, subtype1, subtype2);

		// Register the types now
		getFixture().register(type);
		getFixture().register(subtype1);
		getFixture().register(subtype2);

		listener.clear();

		Set<IElementType> rejects = ElementTypeUtil.deregisterElementTypes(allTypes, ElementTypeUtil.NONE);

		listener.assertRemoved(type);
		listener.assertRemoved(subtype1);
		listener.assertRemoved(subtype2);

		assertTrue(rejects.isEmpty());
	}

	/**
	 * Tests that multiple related dynamically-added element types can be
	 * removed from the registry with their specializations.
	 */
	public void test_deregister_multiple_withSubtypes() {
		final IMetamodelType type = type("dynamic.metamodel.typeToBeRemoved", EmployeePackage.eINSTANCE.getLocation()); //$NON-NLS-1$
		final ISpecializationType subtype1 = type("dynamic.specialization.typeToBeRemoved1", type); //$NON-NLS-1$
		final ISpecializationType subtype2 = type("dynamic.specialization.typeToBeRemoved2", subtype1); //$NON-NLS-1$
		final ISpecializationType subtype3 = type("dynamic.specialization.typeToBeRemoved3", type); //$NON-NLS-1$
		final ISpecializationType subtype4 = type("dynamic.specialization.typeToBeRemoved4", subtype3); //$NON-NLS-1$

		// One of these is redundant
		final List<IElementType> someTypes = Arrays.asList(type, subtype3);

		// Register the types now
		getFixture().register(type);
		getFixture().register(subtype1);
		getFixture().register(subtype2);
		getFixture().register(subtype3);
		getFixture().register(subtype4);

		listener.clear();

		Set<IElementType> rejects = ElementTypeUtil.deregisterElementTypes(someTypes, ElementTypeUtil.SPECIALIZATIONS);

		listener.assertRemoved(type);
		listener.assertRemoved(subtype1);
		listener.assertRemoved(subtype2);
		listener.assertRemoved(subtype3);
		listener.assertRemoved(subtype4);

		assertTrue(rejects.isEmpty());
	}

	/**
	 * Tests that multiple related dynamically-added element types can be
	 * removed from the registry with their specializations and all advice.
	 */
	public void test_deregister_multiple_withSubtypesAndAdvice() {
		String id = "dynamic.metamodel.typeToBeRemoved"; //$NON-NLS-1$
		final IMetamodelType type = type(id, EmployeePackage.eINSTANCE.getLocation());

		String subID1 = "dynamic.specialization.typeToBeRemoved1"; //$NON-NLS-1$
		final ISpecializationType subtype1 = type(subID1, type);

		String subID2 = "dynamic.specialization.typeToBeRemoved2"; //$NON-NLS-1$
		final ISpecializationType subtype2 = type(subID2, subtype1);

		String subID3 = "dynamic.specialization.typeToBeRemoved3"; //$NON-NLS-1$
		final ISpecializationType subtype3 = type(subID3, type);

		String subID4 = "dynamic.specialization.typeToBeRemoved4"; //$NON-NLS-1$
		final ISpecializationType subtype4 = type(subID4, subtype3);

		// One of these is redundant
		final List<IElementType> someTypes = Arrays.asList(type, subtype3);

		// Advise two of the specialization types
		Set<IAdviceBindingDescriptor> advices = new HashSet<IAdviceBindingDescriptor>();
		advices.add(new MyAdviceBindingDescriptor("test.advice.1", subID2)); //$NON-NLS-1$
		advices.add(new MyAdviceBindingDescriptor("test.advice.2", subID2)); //$NON-NLS-1$
		advices.add(new MyAdviceBindingDescriptor("test.advice.3", subID4)); //$NON-NLS-1$
		advices.add(new MyAdviceBindingDescriptor("test.advice.4", subID4)); //$NON-NLS-1$

		// Register the types now
		getFixture().register(type);
		getFixture().register(subtype1);
		getFixture().register(subtype2);
		getFixture().register(subtype3);
		getFixture().register(subtype4);

		// And the advices
		for (IAdviceBindingDescriptor next : advices) {
			getFixture().registerAdvice(next);
		}

		listener.clear();

		Set<IElementType> rejects = ElementTypeUtil.deregisterElementTypes(someTypes, ElementTypeUtil.SPECIALIZATIONS
				| ElementTypeUtil.ADVICE_BINDINGS);

		listener.assertRemoved(type);
		listener.assertRemoved(subtype1);
		listener.assertRemoved(subtype2);
		listener.assertRemoved(subtype3);
		listener.assertRemoved(subtype4);

		assertTrue(rejects.isEmpty());

		for (IAdviceBindingDescriptor next : advices) {
			listener.assertRemoved(next);
		}
	}

	/**
	 * Tests that multiple related dynamically-added element types can be
	 * removed from the registry with their specializations and client-context
	 * bindings.
	 */
	public void test_deregister_multiple_withSubtypesAndClientContexts() {
		final IMetamodelType type = type("dynamic.metamodel.typeToBeRemoved", EmployeePackage.eINSTANCE.getLocation()); //$NON-NLS-1$
		final ISpecializationType subtype1 = type("dynamic.specialization.typeToBeRemoved1", type); //$NON-NLS-1$
		final ISpecializationType subtype2 = type("dynamic.specialization.typeToBeRemoved2", subtype1); //$NON-NLS-1$
		final ISpecializationType subtype3 = type("dynamic.specialization.typeToBeRemoved3", type); //$NON-NLS-1$
		final ISpecializationType subtype4 = type("dynamic.specialization.typeToBeRemoved4", subtype3); //$NON-NLS-1$

		IClientContext context = ClientContextManager.getInstance().getClientContext("org.eclipse.gmf.tests.runtime.emf.type.core.ClientContext1"); //$NON-NLS-1$
		
		// One of these is redundant
		final List<IElementType> someTypes = Arrays.asList(type, subtype3);

		// Register the types now
		getFixture().register(type);
		getFixture().register(subtype1);
		getFixture().register(subtype2);
		getFixture().register(subtype3);
		getFixture().register(subtype4);
		
		// And bind them to the context
		context.bindId(type.getId());
		context.bindId(subtype1.getId());
		context.bindId(subtype2.getId());
		context.bindId(subtype3.getId());
		context.bindId(subtype4.getId());

		assertTrue(context.includes(type));
		assertTrue(context.includes(subtype1));
		assertTrue(context.includes(subtype2));
		assertTrue(context.includes(subtype3));
		assertTrue(context.includes(subtype4));
		
		ElementTypeUtil.deregisterElementTypes(someTypes, ElementTypeUtil.ALL_DEPENDENTS);

		assertFalse(context.includes(type));
		assertFalse(context.includes(subtype1));
		assertFalse(context.includes(subtype2));
		assertFalse(context.includes(subtype3));
		assertFalse(context.includes(subtype4));
	}

	//
	// Test framework
	//

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		listener = new TestListener();
		getFixture().addElementTypeRegistryListener(listener);
	}

	@Override
	protected void tearDown() throws Exception {
		getFixture().removeElementTypeRegistryListener(listener);
		listener = null;

		super.tearDown();
	}

	IMetamodelType type(String id, EClass ecoreType) {
		return new MetamodelType(id, null, id, ecoreType, null);
	}

	ISpecializationType type(String id, IElementType... supertypes) {
		return new SpecializationType(id, null, id, supertypes, null, null, new MySpecializationAdvice());
	}

	<E> void assertPrecedes(E o1, E o2, List<? extends E> list) {
		int i1 = list.indexOf(o1);
		int i2 = list.indexOf(o2);

		assertTrue(i1 >= 0);
		assertTrue(i2 >= 0);
		assertTrue(i1 < i2);
	}

	private static class MySpecializationAdvice extends AbstractEditHelperAdvice {
		public MySpecializationAdvice() {
			super();
		}

		protected ICommand getBeforeCreateCommand(CreateElementRequest request) {
			return super.getBeforeCreateCommand(request);
		}
	}

	private static class MyAdvice extends AbstractEditHelperAdvice {
		public MyAdvice() {
			super();
		}

		protected ICommand getBeforeCreateCommand(CreateElementRequest request) {
			return super.getBeforeCreateCommand(request);
		}
	}

	private static class MyAdviceBindingDescriptor extends AbstractAdviceBindingDescriptor {
		public MyAdviceBindingDescriptor(String id, String typeID) {
			super(id, typeID, AdviceBindingInheritance.ALL, new MyAdvice());
		}

		public IElementMatcher getMatcher() {
			return null;
		}

		public IContainerDescriptor getContainerDescriptor() {
			return null;
		}
	}

	@SuppressWarnings("unused")
	private class TestListener implements IElementTypeRegistryListener2 {

		List<IElementType> addedTypes = new ArrayList<IElementType>();
		List<IElementType> removedTypes = new ArrayList<IElementType>();
		List<IAdviceBindingDescriptor> addedAdvices = new ArrayList<IAdviceBindingDescriptor>();
		List<IAdviceBindingDescriptor> removedAdvices = new ArrayList<IAdviceBindingDescriptor>();

		public void elementTypeAdded(ElementTypeAddedEvent event) {
			addedTypes.add(getFixture().getType(event.getElementTypeId()));
		}

		public void elementTypeRemoved(ElementTypeRemovedEvent event) {
			removedTypes.add(event.getElementType());
		}

		public void adviceBindingAdded(AdviceBindingAddedEvent event) {
			addedAdvices.add(event.getAdviceBinding());
		}

		public void adviceBindingRemoved(AdviceBindingRemovedEvent event) {
			removedAdvices.add(event.getAdviceBinding());
		}

		void clear() {
			addedTypes.clear();
			removedTypes.clear();
			addedAdvices.clear();
			removedAdvices.clear();
		}

		void assertAdded(IElementType type) {
			assertTrue(addedTypes.contains(type));
		}

		void assertRemoved(IElementType type) {
			assertTrue(removedTypes.contains(type));
		}

		void assertAdded(IAdviceBindingDescriptor advice) {
			assertTrue(addedAdvices.contains(advice));
		}

		void assertRemoved(IAdviceBindingDescriptor advice) {
			assertTrue(removedAdvices.contains(advice));
		}

		void assertTypesAdded(int count) {
			assertEquals(count, addedTypes.size());
		}

		void assertTypesRemoved(int count) {
			assertEquals(count, removedTypes.size());
		}

		void assertAdvicesAdded(int count) {
			assertEquals(count, addedAdvices.size());
		}

		void assertAdvicesRemoved(int count) {
			assertEquals(count, removedAdvices.size());
		}
	}
}
