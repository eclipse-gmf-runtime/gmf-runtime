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

package org.eclipse.gmf.runtime.emf.type.core.internal.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.emf.type.core.IClientContext;
import org.eclipse.gmf.runtime.emf.type.core.IContainerDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.IElementMatcher;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.IElementTypeDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.IMetamodelType;
import org.eclipse.gmf.runtime.emf.type.core.ISpecializationType;
import org.eclipse.gmf.runtime.emf.type.core.ISpecializationTypeDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePlugin;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePluginStatusCodes;
import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.AdviceBindingDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.AdviceBindingInheritance;
import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.ElementTypeDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.IEditHelperAdviceDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.MetamodelDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.MetamodelTypeDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.SpecializationTypeDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.EMFTypeCoreMessages;

/**
 * Registry of specialization types populated by the
 * <code>ElementTypeRegistry</code>. Keeps track of the specializations and
 * advice bindings and provides methods for finding matching edit helper advice.
 * <P>
 * This class is for internal use only.
 * 
 * @author ldamus
 */
public class SpecializationTypeRegistry {
	
	/**
	 * Set containing the ALL advice binding inheritance.
	 */
	private final static Set ALL = Collections
			.singleton(AdviceBindingInheritance.ALL);

	/**
	 * Set containing the ALL and NONE advice binding inheritance.
	 */
	private final static Set ALL_NONE = new HashSet(
			Arrays
					.asList(new AdviceBindingInheritance[] {
							AdviceBindingInheritance.NONE,
							AdviceBindingInheritance.ALL }));

	/**
	 * Specialization type descriptors stored by ID. Each value is a
	 * <code>SpecializationTypeDescriptor</code>.
	 */
	private final Map specializationTypeDescriptors;

	/**
	 * Specialization type descriptors stored by the ID of the type that has
	 * been specialized. Each value is a Set of type descriptors for the types
	 * that specialize the key type ID.
	 */
	private final Map specializationsForTypeId;

	/**
	 * Edit helper advice stored by target element type ID. Each value is a set
	 * of IEditHelperAdviceDescriptors.
	 */
	private final Map adviceBindings;

	/**
	 * Constructs a new specialization type registry.
	 */
	public SpecializationTypeRegistry() {
		super();

		specializationTypeDescriptors = new HashMap();
		specializationsForTypeId = new HashMap();
		adviceBindings = new HashMap();
	}
	
	/**
	 * Registers <code>specializationType</code> if it has a unique ID in the
	 * registry.
	 * 
	 * @param specializationType
	 *            the element type
	 * @return <code>true</code> if the element type was registered,
	 *         <code>false</code> otherwise.
	 */
	public boolean registerSpecializationType(ISpecializationType specializationType) {
		
		if (specializationType == null
			|| specializationTypeDescriptors.containsKey(specializationType
				.getId())) {

			return false;
		}
		
		SpecializationTypeDescriptor descriptor = new SpecializationTypeDescriptor(
			specializationType);
		
		return registerSpecializationType(descriptor);
	}

	/**
	 * Registers the specialization element type described by
	 * <code>configElement</code>.
	 * 
	 * @param configElement
	 *            the configutation element
	 * @param metamodelDescriptor
	 *            the descriptor for the metamodel containing the EClass for the
	 *            new element type
	 * @return the new specialization type descriptor if it has been registered,
	 *         <code>null</code> otherwise.
	 * 
	 * @throws CoreException
	 *             on any problem accessing a configuration element
	 */
	public SpecializationTypeDescriptor registerSpecializationType(
			IConfigurationElement configElement,
			MetamodelDescriptor metamodelDescriptor)
		throws CoreException {

		SpecializationTypeDescriptor descriptor = new SpecializationTypeDescriptor(
			configElement, metamodelDescriptor);

		boolean wasRegistered = registerSpecializationType(descriptor);

		return wasRegistered ? descriptor
			: null;
	}
	
	/**
	 * Registers <code>descriptor</code> if it has a unique ID in the
	 * registry.
	 * 
	 * @param descriptor
	 * @return <code>true</code> if the element was registered,
	 *         <code>false</code> otherwise.
	 */
	private boolean registerSpecializationType(
			SpecializationTypeDescriptor descriptor) {

		if (checkForDuplicate(descriptor)) {
			return false;
		}

		// Put the type in the main specialization map.
		specializationTypeDescriptors.put(descriptor.getId(), descriptor);

		// Register its edit helper advice
		IEditHelperAdviceDescriptor editHelperAdvice = descriptor
			.getEditHelperAdviceDescriptor();

		if (editHelperAdvice != null) {
			register(editHelperAdvice);
		}

		// Put the type in the map of specialized types for a given ID
		String[] specializedTypes = descriptor.getSpecializationTypeIds();
		for (int i = 0; i < specializedTypes.length; i++) {
			Set specializations = (Set) specializationsForTypeId
				.get(specializedTypes[i]);

			if (specializations == null) {
				specializations = new HashSet();
				specializationsForTypeId.put(specializedTypes[i],
					specializations);
			}
			specializations.add(descriptor);
		}
		return true;
	}

	/**
	 * Loads the edit helper advice binding described by
	 * <code>configElement</code>.
	 * 
	 * @param configElement
	 *            the configutation element
	 * @throws CoreException
	 *             on any problem accessing a configuration element
	 */
	public void registerAdviceBinding(IConfigurationElement configElement,
			MetamodelDescriptor metamodelDescriptor)
		throws CoreException {

		IEditHelperAdviceDescriptor descriptor = new AdviceBindingDescriptor(
			configElement, metamodelDescriptor);
		register(descriptor);
	}

	/**
	 * Registers the descriptor with the advice bindings for the target ID.
	 * 
	 * @param descriptor
	 *            the edit helper advice descriptor
	 */
	private void register(IEditHelperAdviceDescriptor descriptor) {

		String targetId = descriptor.getTypeId();
		Set bindings = (Set) adviceBindings.get(targetId);

		if (bindings == null) {
			bindings = new HashSet();
			adviceBindings.put(targetId, bindings);
		}
		bindings.add(descriptor);
	}

	/**
	 * Removes the specialization type <code>specializationTypeDescriptor</code>.
	 * 
	 * @param specializationTypeDescriptor
	 *            the specialization type to remove
	 */
	public void removeSpecializationType(
			SpecializationTypeDescriptor specializationTypeDescriptor) {

		specializationTypeDescriptors.remove(specializationTypeDescriptor.getId());
		
		String[] specializedTypes = specializationTypeDescriptor
			.getSpecializationTypeIds();

		for (int j = 0; j < specializedTypes.length; j++) {
			Set specializations = (Set) specializationsForTypeId
				.get(specializedTypes[j]);

			specializations.remove(specializationTypeDescriptor);
		}
	}

	/**
	 * Gets a list containing the immediate specializations of <code>type</code>.
	 * 
	 * @param type
	 *            the element type for which to find specializations
	 * @param clientContext
	 *            the client context
	 * @return the list of all <code>SpecializationTypeDescriptor</code> s of
	 *         <code>type</code>
	 */
	private List getImmediateSpecializationTypeDescriptors(
			ElementTypeDescriptor type, IClientContext clientContext) {
		return getSpecializationTypeDescriptors(type, false, clientContext);
	}
	
	/**
	 * Gets a list containing all specializations of <code>type</code>, in
	 * breadth-first order.
	 * 
	 * @param type
	 *            the element type
	 * @param clientContext
	 *            the client context
	 * @return the array of all specializations of <code>type</code>
	 */
	public ISpecializationType[] getAllSpecializationTypes(
			IElementTypeDescriptor type, IClientContext clientContext) {

		List descriptors = getSpecializationTypeDescriptors(type, true, clientContext);
		ISpecializationType[] result = new ISpecializationType[descriptors.size()];
		int index = 0;
		
		for (Iterator i = descriptors.iterator(); i.hasNext(); index++) {
			SpecializationTypeDescriptor next = (SpecializationTypeDescriptor) i.next();
			result[index] = (ISpecializationType) next.getElementType();
		}
		return result;
	}

	/**
	 * Gets a list containing all specializations of <code>type</code>, in
	 * breadth-first order.
	 * 
	 * @param type
	 *            the element type
	 * @param clientContext
	 *            the client context
	 * @return the list of all specializations of <code>type</code>
	 */
	private List getAllSpecializationTypeDescriptors(
			IElementTypeDescriptor type, IClientContext clientContext) {

		return getSpecializationTypeDescriptors(type, true, clientContext);
	}

	/**
	 * Gets a list containing the descriptors for specializations of
	 * <code>type</code>, in breadth-first order. There are no duplicates in
	 * the list.
	 * 
	 * @param type
	 *            the element type
	 * @param deep
	 *            <code>true</code> if all specializations are to be found,
	 *            recursively, <code>false</code> if only the immediate
	 *            (direct) specializations are to be found.
	 * @return the collection of <code>SpecializationTypeDescriptors</code> of
	 *         <code>type</code>
	 */
	private List getSpecializationTypeDescriptors(IElementTypeDescriptor type,
			boolean deep, IClientContext clientContext) {

		LinkedHashSet result = new LinkedHashSet();
		
		if (type != null) {
			// Get the immediate specializations
			Set specializationDescriptors = (Set) specializationsForTypeId.get(type
				.getId());
	
			if (specializationDescriptors != null) {
				Set specializations = new HashSet();
	
				for (Iterator i = specializationDescriptors.iterator(); i.hasNext();) {
					SpecializationTypeDescriptor nextDescriptor = (SpecializationTypeDescriptor) i
						.next();
	
					if (clientContext.includes(nextDescriptor)) {
						if (nextDescriptor != null) {
							specializations.add(nextDescriptor);
		
							if (deep) {
								// Recursively search for specializations
								result.addAll(getSpecializationTypeDescriptors(
									nextDescriptor, deep, clientContext));
							}
						}
					}
				}
				// Add the immediate specializations last, so that a breadth-first
				// order is maintained
				result.addAll(specializations);
			}
		}
		return new ArrayList(result);
	}

	/**
	 * Gets the array of specialization types that match <code>eObject</code>,
	 * in order of decreasing specialization.
	 * <P>
	 * Returns an empty list if the <code>clientContext</code> is not bound to
	 * the <code>metamodelTypeDescriptor</code>.
	 * 
	 * @param eObject
	 *            the model element for which to find specializations
	 * @param metamodelTypeDescriptor
	 *            the metamodel type descriptor for <code>eObject</code>
	 * @return the list of <code>SpecializationTypeDescriptor</code> s that
	 *         match <code>eObject</code>
	 */
	public List getSpecializationDescriptorsMatching(EObject eObject,
			MetamodelTypeDescriptor metamodelTypeDescriptor, IClientContext clientContext) {
		
		List result = new ArrayList();

		// Get all of the specializations of the metamodel type that matches the
		// eObject
		List specializations = getAllSpecializationTypeDescriptors(
				metamodelTypeDescriptor, clientContext);

		// Get the edit helper specialization descriptors that have matching
		// advice
		for (Iterator i = specializations.iterator(); i.hasNext();) {
			SpecializationTypeDescriptor nextDescriptor = (SpecializationTypeDescriptor) i
				.next();
			IEditHelperAdviceDescriptor adviceDescriptor = nextDescriptor
				.getEditHelperAdviceDescriptor();

			if (adviceDescriptor != null) {
				// See if the advice from this descriptor matches the model
				// element
				if (adviceMatches(eObject, adviceDescriptor)) {
					result.add(nextDescriptor);
				}
			} else {
				// No restrictions
				result.add(nextDescriptor);
			}
		}
		return result;
	}

	/**
	 * Gets the list of specializations of <code>type</code> whose matching
	 * criteria match the given <code>eContainer</code> and
	 * <code>reference</code>.
	 * <P>
	 * Returns an empty list if the <code>clientContext</code> is not bound to
	 * the <code>type</code>.
	 * 
	 * @param type
	 *            the element type
	 * @param eContainer
	 *            the container
	 * @param reference
	 *            the reference feature.
	 * @param clientContext
	 *            the client context
	 * @return the list of matching specializations
	 */
	public List getMatchingSpecializations(ElementTypeDescriptor type,
			EObject eContainer, EReference reference, IClientContext clientContext) {
		
		List result = new ArrayList();

		// Get the specializations of the element type that match the given
		// feature of the container
		List matchingSpecializations = getSpecializationDescriptorsMatching(
			type, eContainer, reference, clientContext);

		result.addAll(matchingSpecializations);

		// Repeat for only the matching specializations
		for (Iterator i = matchingSpecializations.iterator(); i.hasNext();) {
			result.addAll(getMatchingSpecializations((ElementTypeDescriptor) i
				.next(), eContainer, reference, clientContext));
		}
		return result;
	}

	/**
	 * Queries whether the specified edit helper advice descriptor matches an
	 * <code>eObject</code>.
	 * 
	 * @param eObject
	 *            the model element for which to find matching advice
	 * @param editHelperAdviceDescriptor
	 *            an advice descriptor that may opr may not match <code>eObject</code>
	 * @return <code>true</code> if the advice matches; <code>false</code>, otherwise
	 */
	private boolean adviceMatches(EObject eObject,
			IEditHelperAdviceDescriptor editHelperAdviceDescriptor) {

		EReference containmentFeature = eObject.eContainmentFeature();

		IContainerDescriptor container = editHelperAdviceDescriptor
			.getContainerDescriptor();

		if (container != null) {
			IElementMatcher containerMatcher = container.getMatcher();

			if (containerMatcher != null
				&& !containerMatcher.matches(eObject.eContainer())) {
				return false;
			}
		}

		EReference[] features = null;
		if (container != null) {
			features = container.getContainmentFeatures();
		}

		if (features == null || features.length < 1) {
			// All features that can contain this type's EClass are valid
			IElementMatcher matcher = editHelperAdviceDescriptor.getMatcher();

			return (matcher == null) || matcher.matches(eObject);

		} else {

			for (int j = 0; j < features.length; j++) {

				if (features[j] == containmentFeature) {
					IElementMatcher matcher = editHelperAdviceDescriptor
						.getMatcher();

					return (matcher == null) || matcher.matches(eObject);
				}
			}
		}

		return false;
	}

	/**
	 * Gets the list descriptors of the specializations that match the
	 * <code>eContainer</code> and <code>feature</code>.
	 * 
	 * @param eContainer
	 *            the container to match
	 * @param feature
	 *            the containment feature to match
	 * @param clientContext
	 *            the client context
	 * @return the list of <code>SpecializationTypeDescriptor</code> s that
	 *         match
	 */
	private List getSpecializationDescriptorsMatching(
			ElementTypeDescriptor typeDescriptor, EObject eContainer,
			EReference feature, IClientContext clientContext) {

		List result = new ArrayList();
		Collection specializationDescriptors = getImmediateSpecializationTypeDescriptors(
				typeDescriptor, clientContext);

		for (Iterator i = specializationDescriptors.iterator(); i.hasNext();) {
			SpecializationTypeDescriptor nextDescriptor = (SpecializationTypeDescriptor) i
				.next();
			
			if (!clientContext.includes(nextDescriptor)) {
				// descriptor isn't bound to the client context
				continue;
			}
			
			IContainerDescriptor container = nextDescriptor
				.getContainerDescriptor();

			// First, match the container
			IElementMatcher containerMatcher = container != null ? container
				.getMatcher()
				: null;

			if (containerMatcher != null
				&& !containerMatcher.matches(eContainer)) {
				continue;
			}

			// Second, match the containing feature(s)
			EReference[] features = container != null ? container
				.getContainmentFeatures()
				: null;

			if (features == null || features.length < 1) {
				// All features that can contain this type's EClass are valid
				EClass specializedType = nextDescriptor.getElementType()
					.getEClass();

				if (canContain(eContainer.eClass(), feature, specializedType)) {
					result.add(nextDescriptor);
					continue;
				}
			} else {
				for (int j = 0; j < features.length; j++) {

					if (features[j] == feature) {
						result.add(nextDescriptor);
						break;
					}
				}
			}
		}

		return result;
	}

	/**
	 * Answers whether or not <code>container</code> can contain
	 * <code>contained</code> in its <code>reference</code> feature.
	 * 
	 * @param container
	 *            the container
	 * @param reference
	 *            the reference feature
	 * @param contained
	 *            the element to be contained
	 * @return <code>true</code> if <code>container</code> can contain
	 *         <code>contained</code> in its <code>reference</code> feature,
	 *         <code>false</code> otherwise.
	 */
	private boolean canContain(EClass container, EReference reference,
			EClass contained) {

		if ((reference.isContainment())
			&& (container.getEAllReferences().contains(reference))) {

			EClass eType = (EClass) reference.getEType();

			if ((eType.equals(contained)) || (eType.isSuperTypeOf(contained))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the edit helper advice bound to the <code>elementTypes</code> that
	 * match <code>eObject</code>. The advice will be that which is bound to
	 * the <code>clientContext</code>, and is ordered from most general
	 * advice (inherited from metamodel supertypes) to most specific advice
	 * (bound to specializations).
	 * <P>
	 * Returns an empty list if the <code>clientContext</code> is not bound to
	 * the <code>metamodelTypeDescriptor</code>.
	 * 
	 * @param eObject
	 *            the model element to match
	 * @param metamodelTypeDescriptor
	 *            the metamodel type descriptor for the model element
	 * @param clientContext
	 *            the client context
	 * @return the collection of matching <code>IEditHelperAdvice</code>.
	 */
	public List getEditHelperAdvice(EObject eObject,
			MetamodelTypeDescriptor metamodelTypeDescriptor, IClientContext clientContext) {

		LinkedHashSet result = new LinkedHashSet();

		// Look at advice bound to the metamodel supertypes
		IElementType[] metamodelSupertypes = metamodelTypeDescriptor
			.getElementType().getAllSuperTypes();
		for (int i = 0; i < metamodelSupertypes.length; i++) {
			IElementType nextSupertype = metamodelSupertypes[i];
			result.addAll(getMatchingAdvice(nextSupertype.getId(), eObject,
					ALL, clientContext));
		}

		// Add the advice bound to the metamodel type
		result.addAll(getMatchingAdvice(metamodelTypeDescriptor.getId(),
			eObject, ALL_NONE, clientContext));

		// Get the specializations that match
		List specializationDescriptors = getSpecializationDescriptorsMatching(
			eObject, metamodelTypeDescriptor, clientContext);
		Collections.reverse(specializationDescriptors);
		for (Iterator i = specializationDescriptors.iterator(); i.hasNext();) {
			SpecializationTypeDescriptor nextSpecialization = (SpecializationTypeDescriptor) i
				.next();

			// Look for advice bound to the matching specialization type
			result.addAll(getMatchingAdvice(nextSpecialization.getId(),
				eObject, ALL_NONE, clientContext));
		}

		return new ArrayList(result);
	}

	/**
	 * Gets the edit helper advice bound to the <code>elementType</code> and
	 * the inheritable advice bounds to its supertypes. The advice will be that
	 * which is bound to the <code>clientContext</code>, and is ordered from
	 * most general advice (inherited from metamodel supertypes) to most
	 * specific advice (bound to specializations).
	 * <P>
	 * Returns an empty list if the <code>clientContext</code> is not bound to
	 * the <code>elementType</code>.
	 * 
	 * @param elementType
	 *            the element type whose bound edit helper advice will be
	 *            considered
	 * @param metamodelTypeDescriptor
	 *            the metamodel type descriptor for the eClass
	 * @param clientContext
	 *            the client context
	 * @return the collection of matching <code>IEditHelperAdvice</code>.
	 */
	public List getEditHelperAdvice(IElementType elementType,
			MetamodelTypeDescriptor metamodelTypeDescriptor, IClientContext clientContext) {
		
		LinkedHashSet result = new LinkedHashSet();
		
		String metamodelTypeId = (metamodelTypeDescriptor != null) ? metamodelTypeDescriptor.getId() : null;

		// Look at advice bound to the supertypes
		IElementType[] metamodelSupertypes = elementType.getAllSuperTypes();
		for (int i = 0; i < metamodelSupertypes.length; i++) {
			IElementType nextSupertype = metamodelSupertypes[i];

			if (nextSupertype instanceof ISpecializationType || nextSupertype.getId().equals(metamodelTypeId)) {
				result
						.addAll(getMatchingAdvice(nextSupertype.getId(),
								ALL_NONE, clientContext));

			} else if (nextSupertype instanceof IMetamodelType) {
				result.addAll(getMatchingAdvice(nextSupertype.getId(), ALL, clientContext));
			}
		}

		// Add the advice bound to the element type itself
		result.addAll(getMatchingAdvice(elementType.getId(), ALL_NONE, clientContext));

		return new ArrayList(result);
	}

	/**
	 * Gets the edit helper advice bound to the element type with ID
	 * <code>elementTypeId</code> that matches <code>eObject</code>.
	 * 
	 * @param elementTypeId
	 *            the element type id
	 * @param eObject
	 *            the model element to match
	 * @param considerApplicationToSubtypes
	 *            <code>true</code> if only those advice bindings that apply
	 *            to subtypes should be returned, <code>false</code>
	 *            otherwise.
	 * @param clientContext
	 *            the client context
	 * @return the list of matching <code>IEditHelperAdvice</code>
	 */
	private List getMatchingAdvice(String elementTypeId, EObject eObject,
			Set adviceInheritanceToConsider, IClientContext clientContext) {

		List result = new ArrayList();

		for (Iterator j = getAdviceBindings(elementTypeId); j.hasNext();) {
			IEditHelperAdviceDescriptor nextAdviceDescriptor = (IEditHelperAdviceDescriptor) j
				.next();

			if (clientContext.includes(nextAdviceDescriptor)) {
				// Filter out any of the bound advice that doesn't match
				if (!adviceMatches(eObject, nextAdviceDescriptor)) {
					continue;
				}
				
				if (adviceInheritanceToConsider.contains(nextAdviceDescriptor
						.getInheritance())) {
					
					IEditHelperAdvice nextAdvice = nextAdviceDescriptor
						.getEditHelperAdvice();
	
					if (nextAdvice != null) {
						result.add(nextAdvice);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Gets the edit helper advice bound to the element type with ID
	 * <code>elementTypeId</code> that matches <code>eObject</code>.
	 * 
	 * @param elementTypeId
	 *            the element type id
	 * @param considerApplicationToSubtypes
	 *            <code>true</code> if only those advice bindings that apply
	 *            to subtypes should be returned, <code>false</code>
	 *            otherwise.
	 * @param clientContext
	 *            the client context
	 * @return the list of matching <code>IEditHelperAdvice</code>
	 */
	private List getMatchingAdvice(String elementTypeId,
			Set adviceInheritanceToConsider, IClientContext clientContext) {

		List result = new ArrayList();

		for (Iterator j = getAdviceBindings(elementTypeId); j.hasNext();) {
			IEditHelperAdviceDescriptor nextAdviceDescriptor = (IEditHelperAdviceDescriptor) j
				.next();
			
			if (clientContext.includes(nextAdviceDescriptor)) {

				if (adviceInheritanceToConsider.contains(nextAdviceDescriptor
						.getInheritance())) {
					
					IEditHelperAdvice nextAdvice = nextAdviceDescriptor
						.getEditHelperAdvice();
	
					if (nextAdvice != null) {
						result.add(nextAdvice);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Obtains an iterator over the advices bound exactly to the specified element
	 * type ID and also to patterns matching the element type ID. Advice bindings
	 * are returned for all client contexts.  The caller should filter the list 
	 * by context.
	 * <p>
	 * <b>Note</b> for now, in the interest of simplicity and performance, the
	 * only pattern supported is <code>"*"</code> to match all element types.
	 * </p>
	 * 
	 * @param elementTypeId the element type ID for which to get advice
	 * 
	 * @return an immutable iterator of the advice bindings (cannot
	 *     {@linkplain Iterator#remove() remove} from it)
	 */
	private Iterator getAdviceBindings(String elementTypeId) {
		class MultiIterator implements Iterator {
			private Iterator current;
			private Collection[] collections;
			private int index = 0;
			
			MultiIterator(Collection[] collections) {
				this.collections = collections;
				current = nextIterator();
			}
			
			public boolean hasNext() {
				while (current != null) {
					if (current.hasNext()) {
						return true;
					}
					
					current = nextIterator();
				}
				
				return false;
			}

			public Object next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				
				return current.next();
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
			
			private Iterator nextIterator() {
				Iterator result = null;
				
				while ((result == null) && (index < collections.length)) {
					if (collections[index] != null) {
						result = collections[index].iterator();
						collections[index] = null; // free memory
					}
					
					index++;
				}
				
				return result;
			}
		}
		
		return new MultiIterator(new Collection[] {
				(Collection) adviceBindings.get(elementTypeId),
				(Collection) adviceBindings.get("*") //$NON-NLS-1$
		});
	}

	/**
	 * Gets the specialization type for a given ID.
	 * 
	 * @param id
	 *            the type ID
	 * @return the specialization type, or <code>null</code> if there is none
	 *         registered with this ID.
	 */
	public SpecializationTypeDescriptor getSpecializationTypeDescriptor(
			String id) {
		return (SpecializationTypeDescriptor) specializationTypeDescriptors
			.get(id);
	}

	/**
	 * Gets the collection of all registered specialization type descriptors.
	 * 
	 * @return the registered specialization type descriptors. Each element in
	 *         the collection is a <code>SpecializationTypeDescriptor</code>.
	 */
	public Collection getSpecializationTypeDescriptors() {
		return specializationTypeDescriptors.values();
	}
	
	/**
	 * Gets the collection of all registered specialization type descriptors
	 * bound to the <code>clientContext</code>.
	 * 
	 * @return the registered specialization type descriptors. Each element in
	 *         the collection is a <code>SpecializationTypeDescriptor</code>.
	 */
	public Collection getSpecializationTypeDescriptors(
			IClientContext clientContext) {

		Collection result = new ArrayList();

		for (Iterator i = getSpecializationTypeDescriptors().iterator(); i
				.hasNext();) {
			
			ISpecializationTypeDescriptor next = (ISpecializationTypeDescriptor) i
					.next();

			if (clientContext.includes(next)) {
				result.add(next);
			}
		}
		return result;
	}
	
	/**
	 * Checks to see if an element type with the same ID as
	 * <code>typeDescriptor</code> has already been registered.
	 * 
	 * @param typeDescriptor
	 * @return <code>true</code> if the element type is duplicated,
	 *         <code>false</code> otherwise.
	 */
	private boolean checkForDuplicate(ElementTypeDescriptor typeDescriptor) {
		if (specializationTypeDescriptors.containsKey(typeDescriptor.getId())) {
			Log
					.error(
							EMFTypePlugin.getPlugin(),
							EMFTypePluginStatusCodes.TYPE_NOT_INITED,
							EMFTypeCoreMessages
									.bind(
											EMFTypeCoreMessages.type_not_init_WARN_,
											typeDescriptor.getId(),
											EMFTypeCoreMessages.type_reason_duplicate_id_WARN_));
			return true;
		}
		return false;
	}
}