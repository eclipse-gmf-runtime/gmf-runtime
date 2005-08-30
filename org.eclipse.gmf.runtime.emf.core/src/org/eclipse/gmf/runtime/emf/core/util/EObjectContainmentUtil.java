/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.emf.core.edit.MObjectType;

/**
 * Utilities for analysis of the containment and relationships type-conformance
 * relationships between {@link EObject}s.
 * 
 * @author Linda Damus
 */
public class EObjectContainmentUtil {

	/**
	 * Protected constructor. This class should never be instantiated.
	 */
	protected EObjectContainmentUtil() {
		// Protected constructor.
	}

	/**
	 * Determines if both <code>anElement</code> and <code>anElementKind</code>
	 * are the same type. 
	 * 
	 * @param anElement Element whose kind is being tested (not null)
	 * @param anElementKind Element kind against which the test is performed
	 * @return true if <code>anElement</code> is the same type as 
	 *          <code>anElementKind</code>, false otherwise.
	 */
	public static boolean isSameKind(EObject anElement, EClass anElementKind) {
		assert anElement != null;
		
		return isKindRelatedToKinds(anElement.eClass(), new EClass[] {anElementKind },
			new RelationKind[] {RelationKind.SAME_TYPE });
	}

	/**
	 * Determines if <code>anElement</code> is either a strict subtype 
	 * (descendant) of <code>anElementKind</code> or both <code>anElement</code>
	 * and <code>anElementKind</code> are the same type. 
	 * 
	 * @param anElement the element whose kind is being tested (not null)
	 * @param anElementKind the element kind against which the test is
	 *                       performed
	 * 
	 * @return true if <code>anElement</code> conforms to 
	 *          <code>anElementKind</code>, false otherwise.
	 */
	public static boolean isAnySubtypeOfKind(EObject anElement,
		EClass anElementKind) {
		assert anElement != null;
		
		return isAnySubtypeOfKinds(anElement, new EClass[] {anElementKind });
	}

	/**
	 * Determines if <code>anElement</code> is either a strict subtype 
	 * (descendant) of or the same type as any of the kinds in 
	 * <code>elementKinds</code>. 
	 * 
	 * @param anElement the element whose kind is being tested (not null)
	 * @param elementKinds the element kinds against which the test is
	 *                       performed
	 * 
	 * @return true if <code>anElement</code> conforms to any of the kinds in
	 *          <code>elementKinds</code>, false otherwise.
	 */
	public static boolean isAnySubtypeOfKinds(EObject anElement,
		EClass[] elementKinds) {
		assert anElement != null;
		
		return isKindRelatedToKinds(anElement.eClass(), elementKinds, new RelationKind[] { RelationKind.STRICT_SUBTYPE,
			RelationKind.SAME_TYPE });
	}

	/**
	 * Determines if <code>class1</code> is either a strict subtype 
	 * (descendant) of <code>class2</code> or both <code>class1</code>
	 * and <code>class2</code> are the same type. 
	 * 
	 * @param class1 the kind being tested
	 * @param class2 the kind against which the test is performed
	 * 
	 * @return true if <code>class1</code> conforms to
	 *          <code>class2</code>, false otherwise.
	 */
	public static boolean isKindAnySubtypeOfKind(EClass class1,	EClass class2) {
		
		return isKindAnySubtypeOfKinds(class1, new EClass[] {class2});
	}

	/**
	 * Determines if <code>eClass</code> is either a strict subtype 
	 * (descendant) of or the same type as any of the specified kinds.
	 * 
	 * @param eClass the kind being tested
	 * @param kinds the kinds against which the test is performed
	 * 
	 * @return true if <code>eClass</code> conforms to any of the kinds in
	 *          <code>elementKinds</code>, false otherwise.
	 */
	public static boolean isKindAnySubtypeOfKinds(EClass eClass, EClass[] kinds) {

		return isKindRelatedToKinds(eClass, kinds, new RelationKind[] { RelationKind.STRICT_SUBTYPE,
			RelationKind.SAME_TYPE });
	}

	/**
	 * Gets the nearest container conforming to <code>anElementKind</code> in the 
	 * containment hierarchy starting the search up the containment hierarchy
	 * with <code>anElement</code>.
	 * 
	 * @param anElement the starting element
	 * @param anElementKind the container kind
	 * @return the container of kind <code>anElementKind</code>, or null if there
	 *          is no such container in the containment hierarchy of 
	 *          <code>anElement</code>
	 */
	public static EObject findContainerOfAnySubtype(EObject anElement,
		EClass anElementKind) {

		return findContainerOfAnySubtypes(anElement,
			new EClass[] {anElementKind });
	}

	/**
	 * Gets the nearest container conforming to any kind specified in
	 * <code>elementKinds</code> in the containment hierarchy starting the
	 * search up the containment hierarchy with <code>anElement</code>.
	 * 
	 * @param anElement the starting element (not null)
	 * @param elementKinds the valid container kinds
	 * @return the container of kind <code>anElement</code>, or null if there
	 *          is no such container in the containment hierarchy of 
	 *          <code>anElement</code>
	 */
	public static EObject findContainerOfAnySubtypes(EObject anElement,
		EClass[] elementKinds) {
		assert anElement != null;
		
		return findContainerOfKinds(anElement, elementKinds,
			new RelationKind[] { RelationKind.STRICT_SUBTYPE, RelationKind.SAME_TYPE });
	}

	/**
	 * Gets the nearest container of kind <code>anElementKind</code> in the 
	 * containment hierarchy starting the search up the containment hierarchy
	 * with <code>anElement</code>.  The matching container is of the exact type
	 * <code>anElementKind</code>.
	 * 
	 * @param anElement the starting element (not null)
	 * @param anElementKind the container kind
	 * @return the container of kind <code>anElementKind</code>, or null if there
	 *          is no such container in the containment hierarchy of 
	 *          <code>anElement</code>
	 */
	public static EObject findContainerOfSameType(EObject anElement,
		EClass anElementKind) {
		assert anElement != null;
		
		return findContainerOfSameTypes(anElement,
			new EClass[] {anElementKind });
	}

	/**
	 * Gets the nearest container of a kind specified in <code>elementKinds</code>
	 * in the containment hierarchy starting the search up the containment hierarchy
	 * with <code>anElement</code>.  The matching container is of the exact type
	 * of any of the <code>elementKinds</code>.
	 * 
	 * @param anElement the starting element (not null)
	 * @param elementKinds the container kinds
	 * @return the container of kind <code>anElementKind</code>, or null if there
	 *          is no such container in the containment hierarchy of 
	 *          <code>anElement</code>
	 */
	public static EObject findContainerOfSameTypes(EObject anElement,
		EClass[] elementKinds) {
		assert anElement != null;
		
		return findContainerOfKinds(anElement, elementKinds,
			new RelationKind[] {RelationKind.SAME_TYPE });
	}

	/**
	 * Gets the relation between two EClasses. The result could be one of the
	 * following: <code>SAME_TYPE</code>: eClass is the same as otherEClass.
	 * <code>STRICT_BASETYPE</code>: eClass is a super type of otherEClass.
	 * <code>STRICT_SUBTYPE</code>: eClass is a subtype of otherEClass.
	 * <code>UNRELATED_TYPE</code>: eClass is unrelated to otherEClass
	 * 
	 * @param class1
	 *            The first class.
	 * @param class2
	 *            The second class
	 * @return The relationship kind.
	 */
	static RelationKind getRelationTo(EClass class1, EClass class2) {

		if (class1 == class2)
			return RelationKind.SAME_TYPE;

		else if ((class1 == null) || (class2 == null))
			return RelationKind.UNRELATED_TYPE;

		else if (class1.isSuperTypeOf(class2))
			return RelationKind.STRICT_BASETYPE;

		else if (class2.isSuperTypeOf(class1))
			return RelationKind.STRICT_SUBTYPE;

		else
			return RelationKind.UNRELATED_TYPE;
	}

	/**
	 * Gets the nearest container of any kind specified in
	 * <code>elementKinds</code> in the containment hierarchy starting the
	 * search up the containment hierarchy with <code>anElement</code>. 
	 * The relationship between <code>anElementKind</code> and the container
	 * must be described by one of the types in <code>relationKinds</code>.
	 * 
	 * @param anElement the starting element (not null)
	 * @param elementKinds the valid container kinds
	 * @param relationKinds the types of element kind relationships
	 * @return the container of kind <code>anElement</code>, or null if there
	 *          is no such container in the containment hierarchy of 
	 *          <code>anElement</code>
	 */
	static EObject findContainerOfKinds(EObject anElement,
		EClass[] elementKinds, RelationKind[] relationKinds) {
		assert anElement != null;
		
		EObject parent = anElement;
		while (parent != null) {

			if (EObjectContainmentUtil.isKindRelatedToKinds(parent.eClass(), elementKinds,
				relationKinds)) {
				return parent;
			}
			parent = parent.eContainer();
		}

		return null;
	}

	/**
	 * Determines if <code>anElement</code>'s kind is related to
	 * <code>elementKind</code> in a way described by at least one 
	 * relationship in <code>relationKinds</code>. 
	 * <P>
	 * For example, from the UML 7.0 modeling language, if 
	 * <code>anElement</code> is a <b>UMLDatatype</b> object and
	 * <code>elementKind</code> is a <b>UMLDatatype</b> and 
	 * <code>relationKinds</code> contains <b>TypeRelationKind.SAME_TYPE</b>,
	 * this method will return true
	 * 
	 * @param eClass the kind being tested
	 * @param elementKinds the kinds against which the test is performed
	 * @param relationKinds the type of kind relationships
	 * 
	 * @return true if <code>eClass</code> is related to
	 *          <code>elementKinds</code> in the way described by
	 *          <code>relationKind</code>, false otherwise.
	 */
	static boolean isKindRelatedToKinds(EClass eClass,
		EClass[] elementKinds, RelationKind[] relationKinds) {

		for (int i = 0; i < elementKinds.length; i++) {

			RelationKind relation = getRelationTo(eClass, elementKinds[i]);

			for (int j = 0; j < relationKinds.length; j++) {
				if (relation == relationKinds[j]) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Returns the root element of the element passed in.
	 * 
	 * @param element an element that we will find the root element for
	 * @return the element that is the root element of the element that
	 * was passed in (not null)
	 * 
	 * @deprecated Use {@link org.eclipse.emf.ecore.util.EcoreUtil#getRootContainer(org.eclipse.emf.ecore.EObject)},
	 *      instead.
	 */
	public static EObject getRootElement(EObject element) {
		assert element != null;
		
		EObject containerElement = element.eContainer();
		if (containerElement != null) {
			return getRootElement(containerElement);
		}
		return element;
	}

	/**
	 * Returns the path of the element passed in as a string separated by
	 * <code>"::"</code>.
	 * 
	 * @param element the element that we will make the string path for (not null)
	 * @return the path for the element that was passed in
	 */
	public static String makePath(EObject element) {
		assert element != null;
		
		EObject containerElement = element.eContainer();
		if (containerElement != null) {
			return makePath(containerElement) + StringStatics.COLON
				+ StringStatics.COLON
				+ EObjectUtil.getName(element);
		}
		return EObjectUtil.getName(element);
	}

	/**
	 * Determines whether or not <code>parent</code> contains, or is
	 * equal to <code>descendent</code>.
	 * 
	 * @param parent the parent element to test (not null)
	 * @param descendent the child element to test (not null)
	 * @return <code>true</code> if <code>parent</code> contains
	 * 		   <code>descendent</code>, <code>false</code> otherwise.
	 * 
	 * @deprecated Use {@link org.eclipse.emf.ecore.util.EcoreUtil#isAncestor(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject)},
	 *      instead.
	 */
	public static boolean isDescendentOf(EObject parent, EObject descendent) {
		assert parent != null;
		assert descendent != null;
		
		if (parent == null || descendent == null) {
			return false;
		}

		EObject container = descendent;
		while (container != null) {
			if (parent.equals(container)) {
				return true;
			}
			container = container.eContainer();
		}

		return false;
	}

	/**
	 * Gets the object, if any, of the given kind that contains all the 
	 * elements in <code>objects</code>.
	 * 
	 * @param objects the list of EObjects (not null)
	 * @param desiredContainerClass the type of common container to find
	 * 
	 * @return the common container, or <code>null</code> if none is found (when
	 *    the <code>objects</code> are in different roots).
	 */
	public static EObject getLeastCommonContainer(List objects,
		EClass desiredContainerClass) {
		assert objects != null;
		
		EObject commonContainer = null;

		List prevContainers = new ArrayList();

		for (Iterator i = objects.iterator(); i.hasNext();) {

			EObject element = (EObject) i.next();
			List containers = new ArrayList();

			boolean found = false;

			EObject container = element;

			while (container != null) {

				EObject eContainer = container;

				if (desiredContainerClass != null) {

					EClass containerClass = eContainer.eClass();

					RelationKind relation = getRelationTo(
						containerClass, desiredContainerClass);

					if ((relation == RelationKind.SAME_TYPE)
						|| (relation == RelationKind.STRICT_SUBTYPE)) {

						containers.add(container);

						if (!found) {
							if ((prevContainers.isEmpty())
								|| (commonContainer == null)) {
								commonContainer = container;
								found = true;
							} else if ((prevContainers.contains(container))
								&& (contains(container, commonContainer))) {
								commonContainer = container;
								found = true;
							}
						}
					}
				}

				container = container.eContainer();
			}

			if (!found)
				return null;

			prevContainers = containers;
		}

		return commonContainer;
	}

	/**
	 * Queries whether an <code>element</code> is in the containment sub-tree
	 * of a <code>container</code>.  That is, either the <code>container</code>
	 * <i>is</i> an <code>element</code> or (recursively) contains it.
	 * 
	 * @param container The potential container.
	 * @param element The element.
	 * @return True if container contains element else false.
	 */
	private static boolean contains(EObject container, EObject element) {

		if (container == element)
			return true;
		else if ((container == null) || (element == null))
			return false;
		else
			return contains(container, element.eContainer());
	}

	/**
	 * Remove elements from <code>l</code> that are contained within other
	 * elements that are also in <code>l</code>.
	 * 
	 * @param l the list to be modified (not null)
	 * 
	 * @see #getUniqueElementsAncestry(Set)
	 */
	public static void removeContainedElements(List l) {
		assert l != null;
		
		List elementsToRemove = new ArrayList();

		Iterator i = l.iterator();

		while (i.hasNext()) {

			EObject element = (EObject) i.next();
			EObject container = element.eContainer();

			while (container != null) {

				if ((l.contains(container)) && (!elementsToRemove.contains(l))) {
					elementsToRemove.add(element);
					break;
				} else
					container = container.eContainer();
			}
		}

		Iterator j = elementsToRemove.iterator();

		while (j.hasNext()) {

			EObject element = (EObject) j.next();
			l.remove(element);
		}
	}

	/**
	 * Finds the first feature in <code>container</code> that can contain an element
	 * of kind <code>childType</code>.
	 * 
	 * @param container the container element (not null)
	 * @param childType the type of child
	 * @return the feature of <code>container</code> that can contain an element
	 * 		   of kind <code>childType</code>.
	 */
	public static EReference findFeature(EObject container, EClass childType) {
		assert container != null;
		
		EClass containerType = container.eClass();
		Iterator i = containerType.getEAllReferences().iterator();

		while (i.hasNext()) {

			EReference reference = (EReference) i.next();

			if (EObjectUtil.canContain(container, reference, childType, false)) {
				return reference;
			}
		}

		return null;
	}
	
	/**
	 * Returns a set of elements such that there is no containment between
	 * any elements in the set
	 * 
	 * @param elementSet set of elements (not null)
	 * @return set of elements 
	 * 
	 * @see #removeContainedElements(List)
	 */
	public static Set getUniqueElementsAncestry(Set elementSet) {
		assert elementSet != null;
		
		Iterator it = elementSet.iterator();
		EObject container = null;
		while (it.hasNext()) {
			container = ((EObject) it.next()).eContainer();
			while (container != null) {
				if (elementSet.contains(container)) {
					it.remove();
					break;
				}
				container = container.eContainer();
			}
		}
		return elementSet;
	}
	
	/**
	 * Answers whether or not the {@link MObjectType} associated with
	 * <code>o</code> (by adapting it to {@link EObject} if necessary)
	 * is the same as the specified <code>type</code>.
	 * 
	 * @param o
	 *            the object to test, can be <code>null</code>
	 * @param type
	 *            the object type
	 * @return <code>true</code> if the object has the same type,
	 *         <code>false</code> otherwise
	 */
	public static boolean hasMObjectType(Object o, MObjectType type) {
		
		EObject eObject = null;
		
		if (o instanceof EObject) {
			eObject = (EObject) o;
			
		} else if (o instanceof IAdaptable) {
			eObject = (EObject) ((IAdaptable) o).getAdapter(EObject.class);
		}
		
		if (eObject != null) {
			return type == EObjectUtil.getType(eObject);
		}
		
		return false;
	}
	
}