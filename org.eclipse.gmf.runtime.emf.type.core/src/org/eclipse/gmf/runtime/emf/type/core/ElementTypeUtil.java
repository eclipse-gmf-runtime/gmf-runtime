/*
 * Copyright (c) 2015 Christian W. Damus and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Christian W. Damus - initial API and implementation 
 */

package org.eclipse.gmf.runtime.emf.type.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.IEditHelperAdviceDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.internal.impl.DefaultClientContext;

/**
 * Utility methods to simplify working with element types and the registry.
 * 
 * @since 1.9
 */
public class ElementTypeUtil {
	/** Convenient constant for no flags. */
	public static final int NONE = 0x00;

	/**
	 * Flag indicating to include/process advice bindings of the element type
	 * being manipulated.
	 */
	public static final int ADVICE_BINDINGS = 0x01;

	/**
	 * Flag indicating to include/process specialization types of the element
	 * type being manipulated.
	 */
	public static final int SPECIALIZATIONS = 0x02;

	/**
	 * Flag indicating to include/process client context bindings of the element
	 * type being manipulated.
	 */
	public static final int CLIENT_CONTEXTS = 0x04;

	/**
	 * Mask of all dependency-update flags.
	 */
	public static final int ALL_DEPENDENTS = ADVICE_BINDINGS | SPECIALIZATIONS | CLIENT_CONTEXTS;

	/**
	 * Not instantiable by clients.
	 */
	private ElementTypeUtil() {
		super();
	}

	/**
	 * Removes an element type from the registry with options for handling
	 * dependencies.
	 * 
	 * @param elementType
	 *            the element type to remove
	 * @param flags
	 *            a combination of bits indicating how to deal with
	 *            dependencies. Valid flags are:
	 *            <ul>
	 *            <li>{@link #ADVICE_BINDINGS} - also remove
	 *            dynamically-registered advice bindings targeting the type</li>
	 *            <li>{@link #SPECIALIZATIONS} - also remove all
	 *            dynamically-registered element types specializing the type</li>
	 *            </ul>
	 * 
	 * @return {@code true} if the element type was removed, {@code false}
	 *         otherwise (such as, for example, if any specializations are
	 *         statically registered types, which admittedly would be an odd
	 *         circumstance)
	 */
	public static boolean deregister(IElementType elementType, int flags) {
		if ((flags & SPECIALIZATIONS) != 0) {
			// Remove these first to make it feasible to remove the elementType
			ISpecializationType[] specializations = ElementTypeRegistry.getInstance().getSpecializationsOf(
					elementType.getId());
			if (specializations.length > 0) {
				// We have all the specializations, so don't need that flag
				int recursiveFlags = flags & ~SPECIALIZATIONS;
				for (ISpecializationType next : sortBySpecialization(Arrays.asList(specializations))) {
					deregister(next, recursiveFlags);
				}
			}
		}

		if ((flags & ADVICE_BINDINGS) != 0) {
			removeAdviceBindings(elementType);
		}

		if ((flags & CLIENT_CONTEXTS) != 0) {
			unbindIDFromAllContexts(elementType.getId());
		}

		return ElementTypeRegistry.getInstance().deregister(elementType);
	}

	/**
	 * Removes all dynamically contributed advice bindings targeting an element
	 * type.
	 * 
	 * @param elementType
	 *            the element type
	 */
	private static void removeAdviceBindings(IElementType elementType) {
		final String typeID = elementType.getId();
		final Set<IAdviceBindingDescriptor> advicesToRemove = new HashSet<IAdviceBindingDescriptor>();
		final ElementTypeRegistry reg = ElementTypeRegistry.getInstance();

		// Copy the advices to a set to avoid concurrent modifications of the
		// registry's maps
		Iterator<IEditHelperAdviceDescriptor> advices = reg.getSpecializationTypeRegistry().getAdviceBindings(typeID);
		while (advices.hasNext()) {
			IEditHelperAdviceDescriptor next = advices.next();
			if (typeID.equals(next.getTypeId())) {
				advicesToRemove.add(next);
			}
		}

		// Use the main registry because it does the listener notifications
		for (IAdviceBindingDescriptor next : advicesToRemove) {
			reg.deregisterAdvice(next);
		}
	}

	/**
	 * Removes a set of (possibly related by specialization) element types from
	 * the registry. Any specialization relationships amongst the types to be
	 * removed are accounted for as necessary to ensure that the types are
	 * removed while maintaining a consistent registry.
	 * 
	 * @param elementTypes
	 *            the element types to remove
	 * @param flags
	 *            a combination of bits indicating how to deal with
	 *            dependencies. Valid flags are:
	 *            <ul>
	 *            <li>{@link #ADVICE_BINDINGS} - also remove
	 *            dynamically-registered advice bindings targeting the type</li>
	 *            <li>{@link #SPECIALIZATIONS} - also remove all
	 *            dynamically-registered element types specializing the type</li>
	 *            </ul>
	 * 
	 * @return the subset (hopefully empty) of the {@code elementTypes} that
	 *         could not be removed for some reason. Individual problems in
	 *         removing element types are logged
	 */
	public static <T extends IElementType> Set<T> deregisterElementTypes(Iterable<T> elementTypes, int flags) {
		final Set<T> result = new HashSet<T>();

		for (T next : sortBySpecialization(elementTypes)) {
			if (!deregister(next, flags)) {
				// Double-check because maybe it was already removed via the
				// SPECIALIZATIONS flag
				if (ElementTypeRegistry.getInstance().getType(next.getId()) != null) {
					result.add(next);
				}
			}
		}

		return result;
	}

	/**
	 * Sorts a list of element types in order from bottom to top of the
	 * specialization hierarchy. The relative ordering of unrelated types is
	 * undefined, but a specialization type is always positioned before all of
	 * its specialized types. Not that the relationship between EMF
	 * {@link EClass}es on which the types are based is not considered in the
	 * sort.
	 * 
	 * @param elementTypes
	 *            a list of element types to sort, in place
	 * @return the {@code elementTypes} list again for convenience of call
	 *         chaining
	 */
	public static <T extends IElementType> List<T> sortBySpecialization(Iterable<T> elementTypes) {
		return toposort(elementTypes, bySpecialization());
	}

	private static Comparator<IElementType> bySpecialization() {
		return new Comparator<IElementType>() {
			
			public int compare(IElementType o1, IElementType o2) {
				int result = 0;

				if (!o1.equals(o2)) {
					if (o1 instanceof ISpecializationType) {
						ISpecializationType s1 = (ISpecializationType) o1;
						if (s1.isSpecializationOf(o2)) {
							result = -1;
						} else if ((o2 instanceof ISpecializationType)
								&& ((ISpecializationType) o2).isSpecializationOf(o1)) {
							result = +1;
						}
					} else if (o2 instanceof ISpecializationType) {
						ISpecializationType s2 = (ISpecializationType) o2;
						if (s2.isSpecializationOf(o1)) {
							result = +1;
						}
						// Else clearly the types are unrelated because if o1
						// were a specialization type, we wouldn't be here
					}
				}

				return result;
			}
		};
	}

	/**
	 * Brute-force topological sort, required because element types are only
	 * partially ordered: there is no ordering amongst metamodel types nor
	 * amongst unrelated specialization types. The only ordering relation is the
	 * bottom-up vertex ordering in the specialization graph.
	 * 
	 * @param items
	 *            the items to be sorted
	 * @param partOrder
	 *            a partial ordering relation on the items
	 * @return the topologically sorted {@code items} as a new mutable list
	 */
	private static <T> List<T> toposort(Iterable<T> items, Comparator<? super T> partOrder) {
		List<T> unsorted = new LinkedList<T>();
		for (T next : items) {
			unsorted.add(next);
		}

		List<T> result = new ArrayList<T>(unsorted.size());

		while (!unsorted.isEmpty()) {
			T min = unsorted.remove(0);

			for (ListIterator<T> iter = unsorted.listIterator(); iter.hasNext();) {
				T next = iter.next();
				if (partOrder.compare(next, min) < 0) {
					// Found a new minimum. Put the old one back for next pass
					iter.set(min);
					min = next;
				}
			}

			// Whatever's the minimum now is the next in our partial ordering
			result.add(min);
		}

		return result;
	}

	/**
	 * Unbinds an element-type or advice ID from all currently registered client
	 * contexts.
	 * 
	 * @param typeID
	 *            the element-type/advice ID to unbind
	 */
	public static void unbindIDFromAllContexts(String typeID) {
		((ClientContext) DefaultClientContext.getInstance()).unbindId(typeID);

		for (Object next : ClientContextManager.getInstance().getClientContexts()) {
			if (next instanceof ClientContext) {
				((ClientContext) next).unbindId(typeID);
			} else if (next instanceof MultiClientContext) {
				((MultiClientContext) next).unbindId(typeID);
			}
		}
	}

	/**
	 * Unbinds an element-type or advice ID regular expression {@code pattern}
	 * from all currently registered client contexts.
	 * 
	 * @param typeID
	 *            the element-type/advice ID to unbind
	 */
	public static void unbindPatternFromAllContexts(Pattern pattern) {
		((ClientContext) DefaultClientContext.getInstance()).unbindPattern(pattern);

		for (Object next : ClientContextManager.getInstance().getClientContexts()) {
			if (next instanceof ClientContext) {
				((ClientContext) next).unbindPattern(pattern);
			} else if (next instanceof MultiClientContext) {
				((MultiClientContext) next).unbindPattern(pattern);
			}
		}
	}
}
