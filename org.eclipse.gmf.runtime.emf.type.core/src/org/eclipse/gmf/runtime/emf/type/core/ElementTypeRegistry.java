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

package org.eclipse.gmf.runtime.emf.type.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.dynamichelpers.ExtensionTracker;
import org.eclipse.core.runtime.dynamichelpers.IExtensionChangeHandler;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePlugin;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePluginStatusCodes;
import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.ElementTypeDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.ElementTypeFactoryDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.ElementTypeXmlConfig;
import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.MetamodelDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.MetamodelTypeDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.SpecializationTypeDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.internal.impl.DefaultMetamodelType;
import org.eclipse.gmf.runtime.emf.type.core.internal.impl.SpecializationTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.EMFTypeCoreMessages;

/**
 * The registry of application element types, contributed by the
 * <code>org.eclipse.gmf.runtime.emf.type.core.elementTypes</code> extension
 * point, or registered directly through {@link #register(IMetamodelType)} or
 * {@link #register(ISpecializationType)}.
 * 
 * @author ldamus
 */
public class ElementTypeRegistry {

	/**
	 * The name of the element type extension point.
	 */
	private static final String ELEMENT_TYPES_EXT_P_NAME = "elementTypes"; //$NON-NLS-1$

	/**
	 * Empty element type array for convenience.
	 */
	private static final IElementType[] EMPTY_ELEMENT_TYPE_ARRAY = new IElementType[] {};

	/**
	 * My specialization type registr. Keeps track of the specializations and
	 * advice bindings and provides methods for finding matching edit helper
	 * advice.
	 */
	private final SpecializationTypeRegistry specializationTypeRegistry;

	/**
	 * Metamodel type descriptors stored by nsURI. Each key is a namespace URI
	 * and each value is a map, whose key is an EClass name and whose value is a
	 * collection of MetamodelTypeDescriptors.
	 */
	private final Map metamodelTypeDescriptorsByNsURI;

	/**
	 * All metamodel type descriptors stored by ID. Each value is an instance of
	 * <code>MetamodelTypeDescriptor</code>.
	 */
	private final Map metamodelTypeDescriptorsById;

	/**
	 * ElementTypeFactories stored by kind. Each factory declares a string that
	 * describes the kind of element that it is responsible for creating. Each
	 * value is a single <code>ElementTypeFactory</code>.
	 */
	private final Map elementTypeFactoryMap;

	/**
	 * Listeners for changes to this registry.
	 */
	private final List elementTypeRegistryListeners;

	/**
	 * Singleton instance.
	 */
	private static ElementTypeRegistry INSTANCE;

	private ExtensionTracker extensionTracker;
	
	private IExtensionChangeHandler extensionListener;

	/**
	 * Constructs a new element type registry.
	 */
	private ElementTypeRegistry(IConfigurationElement[] configs) {
		super();

		specializationTypeRegistry = new SpecializationTypeRegistry();
		metamodelTypeDescriptorsByNsURI = new HashMap();
		metamodelTypeDescriptorsById = new HashMap();
		elementTypeFactoryMap = new HashMap();
		elementTypeRegistryListeners = new ArrayList();

		registerNullElementType();

		if (EMFTypePlugin.isDynamicAware()) {
			startExtensionTracking();
		}
		
		load(configs);
	}

	private void startExtensionTracking() {
		extensionListener = new IExtensionChangeHandler() {
			
			public void addExtension(IExtensionTracker tracker,
					IExtension extension) {
				load(extension.getConfigurationElements());
			}

			public void removeExtension(IExtension extension, Object[] objects) {
				// Extension removal not supported
			}
		};
		
		IExtensionPoint point = Platform.getExtensionRegistry()
				.getExtensionPoint(EMFTypePlugin.getPluginId(),
						ELEMENT_TYPES_EXT_P_NAME);
		
		extensionTracker = new ExtensionTracker();
		extensionTracker.registerHandler(extensionListener, ExtensionTracker
				.createExtensionPointFilter(point));
	}

	/**
	 * Gets the singleton element type registry.
	 * 
	 * @return the singleton element type registry
	 */
	public static ElementTypeRegistry getInstance() {

		if (INSTANCE == null) {

			IConfigurationElement[] configs = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(EMFTypePlugin.getPluginId(),
					ELEMENT_TYPES_EXT_P_NAME);

			INSTANCE = new ElementTypeRegistry(configs);
		}
		return INSTANCE;
	}

	/**
	 * Gets the edit helper advice for <code>type</code> in order of most
	 * general advice to most specific advice. This order is used so that the
	 * more specific advice can act on or modify the more general advice.
	 * <P>
	 * The client context is inferred from the <code>type</code>.
	 * 
	 * @param type
	 *            the element type for which to obtain editing advice
	 * @return the array of edit helper advice descriptors
	 */
	public IEditHelperAdvice[] getEditHelperAdvice(IElementType type) {
		
		IClientContext clientContext = ClientContextManager.getInstance()
				.getBinding(type);

		return getEditHelperAdvice(type, clientContext);
	}

	/**
	 * Gets the edit helper advice for <code>type</code> to which the
	 * <code>clientContext</code> has been bound, in order of most general
	 * advice to most specific advice. This order is used so that the more
	 * specific advice can act on or modify the more general advice.
	 * 
	 * @param type
	 *            the element type for which to obtain editing advice
	 * @param clientContext
	 *            the client context
	 * @return the array of edit helper advice descriptors
	 */
	public IEditHelperAdvice[] getEditHelperAdvice(IElementType type, IClientContext clientContext) {
		
		IClientContext context = (clientContext == null) ? ClientContextManager
				.getDefaultClientContext() : clientContext;

		EClass eClass = type.getEClass();
		MetamodelTypeDescriptor metamodelType = (eClass != null) ? getMetamodelTypeDescriptor(
				eClass, context)
				: null;
		List result = specializationTypeRegistry.getEditHelperAdvice(type,
				metamodelType, context);

		return (IEditHelperAdvice[]) result.toArray(new IEditHelperAdvice[] {});
	}
	
	/**
	 * Gets the edit helper advice for <code>eObject</code> in order of most
	 * general advice to most specific advice. This order is used so that the
	 * more specific advice can act on or modify the more general advice.
	 * 
	 * @param eObject
	 *            the model element for which to obtain editing advice
	 * @param clientContext
	 *            the client context
	 * @return the array of edit helper advice
	 */
	public IEditHelperAdvice[] getEditHelperAdvice(EObject eObject,
			IClientContext clientContext) {

		Collection result;
		MetamodelTypeDescriptor desc = getMetamodelTypeDescriptor(eObject, clientContext);
		if (desc == null) {
			result = specializationTypeRegistry.getEditHelperAdvice(
					eObject,
					DefaultMetamodelType.getDescriptorInstance(), clientContext);
		} else {
			result = specializationTypeRegistry.getEditHelperAdvice(eObject, desc, clientContext);
		}

		return (IEditHelperAdvice[]) result.toArray(new IEditHelperAdvice[] {});
	}
	
	/**
	 * Gets the edit helper advice for <code>eObject</code> in order of most
	 * general advice to most specific advice. This order is used so that the
	 * more specific advice can act on or modify the more general advice.
	 * <P>
	 * The client context will be inferred from the <code>eObject</code>.
	 * 
	 * @param eObject
	 *            the model element for which to obtain editing advice
	 * @return the array of edit helper advice
	 */
	public IEditHelperAdvice[] getEditHelperAdvice(EObject eObject) {
	
		IClientContext clientContext = ClientContextManager.getInstance()
				.getClientContextFor(eObject);
		
		return getEditHelperAdvice(eObject, clientContext);
	}

	/**
	 * Gets the edit helper advice registered for <code>o</code>, which can
	 * be either an EObject or an IElementType or an
	 * <code>IEditHelperContext</code>.
	 * 
	 * @param o
	 *            the element or type
	 * @return the edit helper advice, or <code>null</code> if none.
	 */
	public IEditHelperAdvice[] getEditHelperAdvice(Object o) {
		
		if (o instanceof EObject) {
			return getEditHelperAdvice((EObject) o);

		} else if (o instanceof IElementType) {
			return getEditHelperAdvice((IElementType) o);

		} else if (o instanceof IEditHelperContext) {
			IEditHelperContext editHelperContext = (IEditHelperContext) o;
			IClientContext clientContext = editHelperContext.getClientContext();
			IElementType elementType = editHelperContext.getElementType();
			EObject eObject = editHelperContext.getEObject();

			if (clientContext != null) {
				if (elementType != null) {
					return getEditHelperAdvice(elementType, clientContext);

				} else if (eObject != null) {
					return getEditHelperAdvice(eObject, clientContext);
				}
			} else {
				if (elementType != null) {
					return getEditHelperAdvice(elementType);

				} else if (eObject != null) {
					return getEditHelperAdvice(eObject);
				}
			}
		}
		return null;
	}
	
	/**
	 * Gets the array of types that can be contained in the structural
	 * <code>feature</code> of <code>eContainer</code>. The result will not
	 * include types that represent abstract EClasses.
	 * 
	 * @param eContainer
	 *            the container
	 * @param reference
	 *            the feature
	 * @param clientContext
	 *            the client context
	 * @return the array of types
	 */
	public IElementType[] getContainedTypes(EObject eContainer,
			EReference reference, IClientContext clientContext) {
		
		Set result = new HashSet();

		EClass containerEClass = eContainer.eClass();

		if (reference.isContainment()
			&& (containerEClass.getEAllReferences().contains(reference))) {

			// Get the reference type (eclass) and all of its subtypes
			EClass eType = (EClass) reference.getEType();
			Set types = getSubtypes(containerEClass.getEPackage(), eType);
			types.add(eType);

			// Get the metamodel types for the eclasses
			List metamodelTypeDescriptors = getMetamodelTypeDescriptors(types, clientContext);
			
			for (Iterator i = metamodelTypeDescriptors.iterator(); i.hasNext();) {

				MetamodelTypeDescriptor nextMetamodelTypeDescriptor = (MetamodelTypeDescriptor) i
					.next();
				IMetamodelType nextMetamodelType = (IMetamodelType) nextMetamodelTypeDescriptor
					.getElementType();

				if (nextMetamodelType != null
					&& !nextMetamodelType.getEClass().isAbstract()) {

					// Add the metamodel type
					result.add(nextMetamodelType);

					// Add the specialization types that match the given
					// container and reference
					Collection specializationDescriptors = specializationTypeRegistry
						.getMatchingSpecializations(
							nextMetamodelTypeDescriptor, eContainer, reference, clientContext);

					for (Iterator j = specializationDescriptors.iterator(); j
						.hasNext();) {
						ElementTypeDescriptor nextDescriptor = (ElementTypeDescriptor) j
							.next();
						IElementType nextElementType = nextDescriptor
							.getElementType();

						if (nextElementType != null) {
							result.add(nextElementType);
						}
					}
				}
			}
		}
		return (IElementType[]) result.toArray(EMPTY_ELEMENT_TYPE_ARRAY);
	}

	/**
	 * Gets the array of types that can be contained in the structural
	 * <code>feature</code> of <code>eContainer</code>.  The result will 
	 * not include types that represent abstract EClasses.
	 * <P>
	 * The client context will be inferred from the <code>eContainer</code>.
	 * 
	 * @param eContainer
	 *            the container
	 * @param reference
	 *            the feature
	 * @return the array of types
	 */
	public IElementType[] getContainedTypes(EObject eContainer,
			EReference reference) {

		IClientContext clientContext = ClientContextManager.getInstance()
				.getClientContextFor(eContainer);
		
		return getContainedTypes(eContainer, reference, clientContext);
	}

	/**
	 * Gets the subtypes of <code>eType</code> in the <code>pkg</code>.
	 * 
	 * @param pkg
	 *            the package in which to find matching classifiers
	 * @param eType
	 *            the eClass for which to find subtypes
	 * @return the matching subtypes
	 */
	private Set getSubtypes(EPackage pkg, final EClass eType) {

		Set result = new HashSet();
		List classifiers = pkg.getEClassifiers();

		for (Iterator i = classifiers.iterator(); i.hasNext();) {
			EClassifier nextClassifier = (EClassifier) i.next();

			if (nextClassifier instanceof EClass
				&& ((EClass) nextClassifier).getEAllSuperTypes()
					.contains(eType)) {
				result.add(nextClassifier);
			}
		}
		return result;
	}

	/**
	 * Gets the metamodel type for <code>eClass</code> in the client
	 * <code>context</code>. If there is none registered against the
	 * <code>eClass</code>, returns the metamodel type for the nearest
	 * supertype of <code>eClass/code> that has a metamodel type.
	 * 
	 * @param eClass
	 *            the metaclass
	 * @param context the client context
	 * @return the metamodel type for this <code>eClass</code> in the client <code>context</code>, or <code>null</code> if none can be found.
	 */
	private IMetamodelType getMetamodelType(EClass eClass, IClientContext context) {
		
		MetamodelTypeDescriptor descriptor = getMetamodelTypeDescriptor(eClass, context);

		if (descriptor != null) {
			return (IMetamodelType) descriptor.getElementType();
		}
		return null;
	}

	/**
	 * Gets the metamodel type registered for <code>eObject</code>'s EClass.
	 * If there is none registered against the <code>eClass</code>, returns
	 * the metamodel type for the nearest supertype of
	 * <code>eClass/code> that has a metamodel type.
	 * 
	 * @param eObject
	 *            the model element
	 * @param clientContext the clientContext
	 * @return the metamodel type for this <code>eObject</code>
	 */
	private IMetamodelType getMetamodelType(EObject eObject, IClientContext clientContext) {
		
		return getMetamodelType(eObject.eClass(), clientContext);
	}

	/**
	 * Convenience method to get an element type for <code>o</code>.
	 * <P>
	 * If <code>o</code> is an <code>IElementType</code>, returns
	 * <code>o</code>.
	 * <P>
	 * If <code>o</code> is an <code>EObject</code>, returns the metamodel
	 * type registered for <code>o</code>'s eClass in the client context that
	 * is bound to <code>o</code>.
	 * <P>
	 * If <code>o</code> is an <code>IEditHelperContext</code>, returns the
	 * element type in <code>o</code> if specified. Else, returns the
	 * metamodel type registered for the eClass of the EObject specified in
	 * <code>o</code> in the client context specified in <code>o</code>. If
	 * no client context is specified, then the client context bound to the
	 * EObject is used.
	 * <P>
	 * Use {@link #getElementType(EClass, IClientContext)} to get metamodel
	 * types registered for a specific <code>EClass</code>.
	 * 
	 * @param o
	 *            the object for which to find an element type.
	 * @return <code>o</code> itself if it is an element type, otherwise
	 *         returns the registered metamodel type
	 */
	public IElementType getElementType(Object o) {

		if (o instanceof EObject) {
			return getElementType((EObject) o);

		} else if (o instanceof IElementType) {
			return (IElementType) o;
			
		} else if (o instanceof IEditHelperContext) {
			IEditHelperContext editHelperContext = (IEditHelperContext) o;
			IElementType elementType = editHelperContext.getElementType();
			
			if (elementType != null) {
				return elementType;
			}
			
			IClientContext clientContext = editHelperContext.getClientContext();
			EObject eObject = editHelperContext.getEObject();

			if (eObject != null) {
				if (clientContext != null) {
					return getElementType(eObject, clientContext);
				} else {
					return getElementType(eObject);
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the registered element type for <code>eClass</code> that 
	 * has no client contexts explicitly bound to it.
	 * 
	 * @param eClass
	 *            the <code>EClass</code> whose element type is to be found.
	 * @return the metamodel type registered for <code>eClass</code>
	 */
	public IElementType getElementType(EClass eClass) {

		return getElementType(eClass, null);
	}
	
	/**
	 * Gets the registered element type for <code>eClass</code>.
	 * 
	 * @param eClass
	 *            the <code>EClass</code> whose element type is to be found.
	 * @param clientContext
	 *            the client context
	 * @return the metamodel type registered for <code>eClass</code>
	 */
	public IElementType getElementType(EClass eClass, IClientContext clientContext) {

		IElementType result = getMetamodelType(eClass, clientContext);
		if (result == null) {
			// at least provide the default type for default editing support
			result = DefaultMetamodelType.getInstance();
		}
		
		return result;
	}
	
	/**
	 * Gets the registered element type for <code>eObject</code>.
	 * <P>
	 * The client context will be inferred from the <code>eObject</code>.
	 * 
	 * @param eObject
	 *            the <code>EObject</code> whose element type is to be found.
	 * @return the metamodel type registered for <code>eObject</code>'s
	 *         <code>EClass</code>
	 */
	public IElementType getElementType(EObject eObject) {
		
		IClientContext clientContext = ClientContextManager.getInstance()
				.getClientContextFor(eObject);
		
		return getElementType(eObject, clientContext);
	}
	
	/**
	 * Gets the registered element type for <code>eObject</code> in the
	 * <code>clientContext</code>.
	 * 
	 * @param eObject
	 *            the <code>EObject</code> whose element type is to be found.
	 * @param clientContext
	 *            the client context
	 * @return the metamodel type registered for <code>eObject</code>'s
	 *         <code>EClass</code>
	 */
	public IElementType getElementType(EObject eObject,
			IClientContext clientContext) {

		IElementType result = getMetamodelType(eObject, clientContext);
		if (result == null) {
			// at least provide the default type for default editing support
			result = DefaultMetamodelType.getInstance();
		}

		return result;
	}

	/**
	 * Gets the metamodel type descriptor for <code>eObject</code>'s EClass
	 * in the client <code>context</code>. If there is none registered
	 * against the <code>eClass</code> for that <code>context</code>,
	 * returns the metamodel type for the nearest supertype of
	 * <code>eClass</code> that has a metamodel type in that
	 * <code>context</code>.
	 * 
	 * @param eObject
	 *            the model element
	 * @param context
	 *            the client context
	 * @return the metamodel type descriptor
	 */
	private MetamodelTypeDescriptor getMetamodelTypeDescriptor(EObject eObject,
			IClientContext context) {
		return getMetamodelTypeDescriptor(eObject.eClass(), context);
	}

	/**
	 * Gets the metamodel type descriptor for <code>eClass</code> in the
	 * client <code>context</code>. If there is none registered against the
	 * <code>eClass</code> for the client <code>context</code>, returns the
	 * metamodel type for the nearest supertype of <code>eClass</code> that
	 * has a metamodel type in the client <code>context</code>.
	 * 
	 * @param eClass
	 *            the model element eclass
	 * @param context
	 *            the client context
	 * @return the metamodel type descriptor
	 */
	private MetamodelTypeDescriptor getMetamodelTypeDescriptor(EClass eClass,
			IClientContext context) {

		IClientContext clientContext = context;

		if (clientContext == null) {
			// use the default context
			clientContext = ClientContextManager.getDefaultClientContext();
		}
		
		Map metamodelTypeDescriptorsByEClass = (Map) metamodelTypeDescriptorsByNsURI
				.get(eClass.getEPackage().getNsURI());
		Collection descriptors = metamodelTypeDescriptorsByEClass != null ? (Collection) metamodelTypeDescriptorsByEClass
				.get(eClass.getName())
				: null;

		if (descriptors != null) {
			for (Iterator i = descriptors.iterator(); i.hasNext();) {
				MetamodelTypeDescriptor descriptor = (MetamodelTypeDescriptor) i
						.next();
	
				if (clientContext.includes(descriptor)) {
					return descriptor;
				}
			}
			
		} else {
			// Find the metamodel type for the nearest supertype.
			List supertypes = eClass.getEAllSuperTypes();
			for (int i = supertypes.size() - 1; i >= 0; i--) {
				EClass nextEClass = (EClass) supertypes.get(i);
	
				// nsURI could be different in supertypes of the eclass
				metamodelTypeDescriptorsByEClass = (Map) metamodelTypeDescriptorsByNsURI.get(nextEClass.getEPackage().getNsURI());
				descriptors = metamodelTypeDescriptorsByEClass != null ? (Collection) metamodelTypeDescriptorsByEClass
						.get(nextEClass.getName())
						: null;
	
				if (descriptors != null) {
					for (Iterator j = descriptors.iterator(); j.hasNext();) {
						MetamodelTypeDescriptor descriptor = (MetamodelTypeDescriptor) j
								.next();
		
						if (clientContext.includes(descriptor)) {
							return descriptor;
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Gets all of the element types (metamodel type and specialization types)
	 * that match <code>eObject</code> in breadth-first order (specializations
	 * before metamodel types).
	 * 
	 * @param eObject
	 *            the model element to match
	 * @param clientContext
	 *            the client context
	 * @return all of the element types that match the model element
	 */
	public IElementType[] getAllTypesMatching(EObject eObject, IClientContext clientContext) {
		
		List result = new ArrayList();
		IMetamodelType metamodelType = getMetamodelType(eObject, clientContext);

		if (metamodelType != null) {

			// Get the matching specializations
			Collection specializations;
			MetamodelTypeDescriptor desc = getMetamodelTypeDescriptor(eObject, clientContext);
			if (desc == null) {
				specializations = Collections.EMPTY_LIST;
			} else {
				specializations = specializationTypeRegistry
					.getSpecializationDescriptorsMatching(eObject, desc, clientContext);
			}

			for (Iterator i = specializations.iterator(); i.hasNext();) {
				SpecializationTypeDescriptor next = (SpecializationTypeDescriptor) i
					.next();
				IElementType elementType = next.getElementType();
				if (elementType != null) {
					result.add(elementType);
				}
			}
			// Add the metamodel type
			result.add(metamodelType);

			// Add the metamodel supertypes in reverse order
			IElementType[] superTypes = metamodelType.getAllSuperTypes();

			for (int i = superTypes.length - 1; i >= 0; i--) {
				result.add(superTypes[i]);
			}
		}
		
		if (result.isEmpty()) {
			// at least provide the default type for default editing behaviour
			result.add(DefaultMetamodelType.getInstance());
		}
		
		return (IElementType[]) result.toArray(EMPTY_ELEMENT_TYPE_ARRAY);
	}

	/**
	 * Gets all of the element types (metamodel type and specialization types)
	 * that match <code>eObject</code> in breadth-first order (specializations
	 * before metamodel types).
	 * <P>
	 * The client context will be inferred from the <code>eObject</code>.
	 * 
	 * @param eObject
	 *            the model element to match
	 * @return all of the element types that match the model element
	 */
	public IElementType[] getAllTypesMatching(EObject eObject) {

		IClientContext clientContext = ClientContextManager.getInstance()
				.getClientContextFor(eObject);
		
		return getAllTypesMatching(eObject, clientContext);
	}
	
	/**
	 * Gets an array containing all specializations of the element type for
	 * <code>id</code>, in breadth-first order.
	 * 
	 * @param id
	 *            the element type ID
	 * @return the list of all specializations of this element type
	 */
	public ISpecializationType[] getSpecializationsOf(String id) {
		
		IElementTypeDescriptor descriptor = getTypeDescriptor(id);
		
		if (descriptor == null) {
			return new ISpecializationType[] {};
		}
		
		IClientContext clientContext = ClientContextManager.getInstance()
				.getBinding(descriptor);

		return specializationTypeRegistry
				.getAllSpecializationTypes(descriptor, clientContext);
	}

	/**
	 * Gets the element type for <code>id</code>. May return
	 * <code>null</code> if this element is not registered.
	 * 
	 * @param id
	 *            the type ID
	 * @return the registered type with this ID, or <code>null</code> if there
	 *         is none.
	 */
	public IElementType getType(String id) {

		ElementTypeDescriptor typeDescriptor = getTypeDescriptor(id);

		if (typeDescriptor != null) {
			return typeDescriptor.getElementType();
		} else if (DefaultMetamodelType.ID.equals(id)) {
			return DefaultMetamodelType.getInstance();
		}
		return null;
	}

	/**
	 * Gets the element type factory registered to create element types of kind
	 * <code>kindName</code>.
	 * 
	 * @param kindName
	 *            the element type kind name
	 * @return the element type factory for this kind, or
	 *         <code>null/code> if there is none
	 */
	public IElementTypeFactory getElementTypeFactory(String kindName) {
		ElementTypeFactoryDescriptor descriptor = (ElementTypeFactoryDescriptor) elementTypeFactoryMap
			.get(kindName);
		if (descriptor != null) {
			return descriptor.getElementTypeFactory();
		}
		return null;
	}

	/**
	 * Registers <code>metamodelType</code> with this registry, if its ID is
	 * unique in the registry. The type's EClass does not have to be unique in
	 * the registry. Metamodel types in the registry are distinguished by the
	 * client context that is bound to the type.
	 * <P>
	 * Notifies clients if the element type was added to the registry.
	 * 
	 * @param metamodelType
	 *            the element type to register
	 * @return <code>true</code> if the type was registered,
	 *         <code>false</code> otherwise
	 */
	public boolean register(IMetamodelType metamodelType) {

		if (metamodelType == null
			|| getType(metamodelType.getId()) != null) {

			return false;
		}

		MetamodelTypeDescriptor descriptor = new MetamodelTypeDescriptor(
			metamodelType);

		boolean result = register(descriptor);

		if (result) {
			fireElementTypeAddedEvent(new ElementTypeAddedEvent(
				metamodelType.getId()));
		}

		return result;
	}

	/**
	 * Registers <code>specializationType</code> with this registry, if its ID
	 * is unique in the registry.
	 * <P>
	 * Notifies clients if the element type was added to the registry.
	 * 
	 * @param specializationType
	 *            the element type to register
	 * @return <code>true</code> if the type was registered,
	 *         <code>false</code> otherwise
	 */
	public boolean register(ISpecializationType specializationType) {

		if (specializationType == null
			|| getType(specializationType.getId()) != null) {

			return false;
		}

		boolean result = specializationTypeRegistry
			.registerSpecializationType(specializationType);

		if (result) {
			fireElementTypeAddedEvent(new ElementTypeAddedEvent(
				specializationType.getId()));
		}

		return result;
	}

	/**
	 * Removes specialization types from the registry that specialize more than
	 * one metamodel type, or do not specialize any metamodel type.
	 * <P>
	 * Logs an error when an invalid specialization is found.
	 */
	private void removeInvalidSpecializations() {

		List specializationsToReject = new ArrayList();

		Collection specializationTypes = specializationTypeRegistry
			.getSpecializationTypeDescriptors();

		for (Iterator i = specializationTypes.iterator(); i.hasNext();) {
			SpecializationTypeDescriptor nextSpecialization = (SpecializationTypeDescriptor) i
				.next();
			MetamodelTypeDescriptor metamodelTypeDescriptor = getMetamodelTypeDescriptor(nextSpecialization);

			if (metamodelTypeDescriptor == null) {
				specializationsToReject.add(nextSpecialization);
			}
		}

		for (Iterator i = specializationsToReject.iterator(); i.hasNext();) {
			specializationTypeRegistry
				.removeSpecializationType((SpecializationTypeDescriptor) i
					.next());
		}
	}

	/**
	 * Loads the metamodel element <code>configElement</code>.
	 * 
	 * @param configElement
	 *            the configuration element
	 * @throws CoreException
	 *             on any problem accessing a configuration element
	 */
	private void loadMetamodel(IConfigurationElement configElement)
		throws CoreException {

		MetamodelDescriptor descriptor = new MetamodelDescriptor(configElement);

		IConfigurationElement[] typeConfigs = configElement.getChildren();

		for (int i = 0; i < typeConfigs.length; i++) {
			IConfigurationElement next = typeConfigs[i];

			String name = next.getName();
			if (name.equals(ElementTypeXmlConfig.E_METAMODEL_TYPE)) {
				registerMetamodelType(next, descriptor);

			} else if (name.equals(ElementTypeXmlConfig.E_SPECIALIZATION_TYPE)) {
				registerSpecializationType(next, descriptor);

			} else if (name.equals(ElementTypeXmlConfig.E_ADVICE_BINDING)) {
				specializationTypeRegistry.registerAdviceBinding(next,
					descriptor);
			}
		}
	}

	/**
	 * Registers the metamodel element type described by
	 * <code>configElement</code>.
	 * 
	 * @param configElement
	 *            the configutation element
	 * @param metamodelDescriptor
	 *            the descriptor for the metamodel containing the EClass for the
	 *            new element type
	 * 
	 * @throws CoreException
	 *             on any problem accessing a configuration element
	 */
	private void registerMetamodelType(IConfigurationElement configElement,
			MetamodelDescriptor metamodelDescriptor)
		throws CoreException {

		MetamodelTypeDescriptor descriptor = new MetamodelTypeDescriptor(
			configElement, metamodelDescriptor);
		register(descriptor);

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
	 * 
	 * @throws CoreException
	 *             on any problem accessing a configuration element
	 */
	private SpecializationTypeDescriptor registerSpecializationType(
			IConfigurationElement configElement,
			MetamodelDescriptor metamodelDescriptor)
		throws CoreException {

		return specializationTypeRegistry.registerSpecializationType(
			configElement, metamodelDescriptor);
	}

	/**
	 * Adds the metamodel <code>type</code> to this registry. Logs an error if
	 * a metamodel type has already been registered for the same ID.
	 * 
	 * @param typeDescriptor
	 *            the descriptor of the type to be added.
	 * @return <code>true</code> if the type was added, <code>false</code>
	 *         otherwise.
	 */
	private boolean register(MetamodelTypeDescriptor typeDescriptor) {

		if (checkForDuplicate(typeDescriptor)) {
			return false;
		}
		
		String nsURI = typeDescriptor.getNsURI();
		String eClassName = typeDescriptor.getEClassName();

		Map metamodelTypeDescriptorsByEClass = (Map) metamodelTypeDescriptorsByNsURI
				.get(nsURI);

		if (metamodelTypeDescriptorsByEClass == null) {
			metamodelTypeDescriptorsByEClass = new HashMap();
			metamodelTypeDescriptorsByNsURI.put(nsURI, metamodelTypeDescriptorsByEClass);
		}
		
		Collection descriptors = (Collection) metamodelTypeDescriptorsByEClass.get(eClassName);
		
		if (descriptors == null) {
			descriptors = new ArrayList();
			metamodelTypeDescriptorsByEClass.put(eClassName, descriptors);
		}

		descriptors.add(typeDescriptor);

		metamodelTypeDescriptorsById
				.put(typeDescriptor.getId(), typeDescriptor);

		return true;
	}

	/**
	 * Loads the element type factory from <code>configElement</code>.
	 * 
	 * @param configElement
	 *            the configuration element
	 * @throws CoreException
	 *             on any problem accessing a configuration element
	 */
	private void loadElementTypeFactory(IConfigurationElement configElement)
		throws CoreException {

		ElementTypeFactoryDescriptor descriptor = new ElementTypeFactoryDescriptor(
			configElement);
		elementTypeFactoryMap.put(descriptor.getKindName(), descriptor);

	}

	/**
	 * Checks to see if an element type with the same ID as
	 * <code>typeDescriptor</code> has already been registered.
	 * 
	 * @param typeDescriptor
	 * @return <code>true</code> if there is no duplicate, <code>false</code>
	 *         otherwise.
	 */
	private boolean checkForDuplicate(ElementTypeDescriptor typeDescriptor) {

		if (metamodelTypeDescriptorsById.containsKey(typeDescriptor.getId())) {
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

	/**
	 * Gets the metamodel type descriptors that match the EClasses in
	 * <code>eClasses</code> for the client <code>context</code>.
	 * 
	 * @param eClasses
	 *            a Set of <code>EClass</code> instances
	 * @param context
	 *            the client context
	 * @return a List of <code>modelType</code>s
	 */
	private List getMetamodelTypeDescriptors(Set eClasses,
			IClientContext context) {
		
		List result = new ArrayList();

		for (Iterator i = eClasses.iterator(); i.hasNext();) {
			EClass nextType = (EClass) i.next();

			MetamodelTypeDescriptor metamodelTypeDescriptor = getMetamodelTypeDescriptor(
					nextType, context);

			if (metamodelTypeDescriptor != null) {
				result.add(metamodelTypeDescriptor);
			}
		}
		return result;
	}

	/**
	 * Gets the type descriptor for the element type with <code>id</code>
	 * 
	 * @param id
	 *            the type ID
	 * @return the type descriptor, or <code>null</code> if none is registered
	 *         with that ID
	 */
	private ElementTypeDescriptor getTypeDescriptor(String id) {

		ElementTypeDescriptor typeDescriptor = (ElementTypeDescriptor) metamodelTypeDescriptorsById
			.get(id);

		if (typeDescriptor == null) {
			// Try the specialization types
			typeDescriptor = specializationTypeRegistry
				.getSpecializationTypeDescriptor(id);
		}
		return typeDescriptor;
	}

	/**
	 * Gets the descriptor for the metamodel type that the
	 * <code>specializationTypeDescriptor</code> specializes. There should
	 * only be one such metamodel type.
	 * 
	 * @param specializationTypeDescriptor
	 * @return descriptor for the metamodel type that the
	 *         <code>specializationTypeDescriptor</code> specializes, or
	 *         <code>null</code> if the doesn't specialize any metamodel type,
	 *         or it specializes more than one metamodel type.
	 */
	private MetamodelTypeDescriptor getMetamodelTypeDescriptor(
			SpecializationTypeDescriptor specializationTypeDescriptor) {

		MetamodelTypeDescriptor metamodelTypeDescriptor = null;

		String[] specializedTypeIds = specializationTypeDescriptor
			.getSpecializationTypeIds();

		for (int j = 0; j < specializedTypeIds.length; j++) {

			ElementTypeDescriptor nextSpecializedType = getTypeDescriptor(specializedTypeIds[j]);

			if (nextSpecializedType == null) {
				Log
						.error(
								EMFTypePlugin.getPlugin(),
								EMFTypePluginStatusCodes.SPECIALIZATION_TYPE_SPECIALIZES_INVALID_ID,
								EMFTypeCoreMessages
										.bind(
												EMFTypeCoreMessages.specializationType_specializes_invalid_id_ERROR_,
												specializationTypeDescriptor
														.getId(),
												specializedTypeIds[j]));
				return null;
			}
			
			MetamodelTypeDescriptor specializedMetamodelType = null;

			if (nextSpecializedType instanceof MetamodelTypeDescriptor) {
				specializedMetamodelType = (MetamodelTypeDescriptor) nextSpecializedType;

			} else if (nextSpecializedType instanceof SpecializationTypeDescriptor) {
				SpecializationTypeDescriptor nextSpecializationType = (SpecializationTypeDescriptor) nextSpecializedType;
				specializedMetamodelType = getMetamodelTypeDescriptor(nextSpecializationType);
			}

			if (specializedMetamodelType == null) {
				// I am specializing a type that doesn't have a metamodel
				// type. I will be removed from the element type registry.
				metamodelTypeDescriptor = null;
				break;

			} else if (metamodelTypeDescriptor == null) {
				// This is the first metamodel type.
				metamodelTypeDescriptor = specializedMetamodelType;

			} else if (metamodelTypeDescriptor != specializedMetamodelType) {
				// Found that I specialize two different metamodel types
				metamodelTypeDescriptor = null;
				break;
			}
		}
		
		if (metamodelTypeDescriptor == null) {
			Log
					.error(
							EMFTypePlugin.getPlugin(),
							EMFTypePluginStatusCodes.SPECIALIZATION_TYPE_SPECIALIZES_MULTIPLE_METAMODEL_TYPES,
							EMFTypeCoreMessages
									.bind(
											EMFTypeCoreMessages.specializationType_specializes_multiple_metamodel_types_ERROR_,
											specializationTypeDescriptor
													.getId()));
		}
		return metamodelTypeDescriptor;
	}

	/**
	 * Loads the element type definitions from the <code>elementTypes</code>
	 * extension point.
	 * 
	 * @param configs
	 *            the configuration elements
	 */
	private synchronized void load(IConfigurationElement[] configs) {

		for (int i = 0; i < configs.length; i++) {

			IConfigurationElement next = configs[i];
			try {

				String name = next.getName();
				if (name.equals(ElementTypeXmlConfig.E_METAMODEL)) {
					loadMetamodel(next);

				} else if (name
					.equals(ElementTypeXmlConfig.E_ELEMENT_TYPE_FACTORY)) {
					loadElementTypeFactory(next);
					
				}  else if (name.equals(ElementTypeXmlConfig.E_SPECIALIZATION_TYPE)) {
					registerSpecializationType(next, null);
				} 

			} catch (CoreException ce) {

				String sourcePluginId = next.getDeclaringExtension()
					.getContributor().getName();
				Log.error(EMFTypePlugin.getPlugin(), ce.getStatus().getCode(),
						EMFTypeCoreMessages.bind(
								EMFTypeCoreMessages.xml_parsing_ERROR_,
								sourcePluginId, ce.getStatus().getMessage()),
						null);

			} catch (Exception e) {

				String sourcePluginId = next.getDeclaringExtension()
					.getContributor().getName();

				Log.error(EMFTypePlugin.getPlugin(),
						EMFTypePluginStatusCodes.ERROR_PARSING_XML,
						EMFTypeCoreMessages.bind(
								EMFTypeCoreMessages.xml_parsing_ERROR_,
								sourcePluginId, e.getMessage()), e);
			}
		}

		// Remove the specializations that aren't valid.
		removeInvalidSpecializations();
	}

	/**
	 * Adds <code>l</code> as a listener for changes to this registry. Does
	 * nothing if <code>l</code> is already registered as a listener.
	 * 
	 * @param l
	 *            the new listener
	 */
	public void addElementTypeRegistryListener(IElementTypeRegistryListener l) {

		if (!elementTypeRegistryListeners.contains(l)) {
			elementTypeRegistryListeners.add(l);
		}
	}

	/**
	 * Removes <code>l</code> as a listener for changes to this registry. Does
	 * nothing if <code>l</code> is not registered as a listener.
	 * 
	 * @param l
	 *            the listener to remove
	 */
	public void removeElementTypeRegistryListener(IElementTypeRegistryListener l) {

		if (elementTypeRegistryListeners.contains(l)) {
			elementTypeRegistryListeners.remove(l);
		}
	}

	/**
	 * Notifies registered listeners that the element type registry has changed.
	 * 
	 * @param e
	 *            the change event
	 */
	private void fireElementTypeAddedEvent(ElementTypeAddedEvent e) {

		for (Iterator i = elementTypeRegistryListeners.iterator(); i.hasNext();) {
			IElementTypeRegistryListener nextListener = (IElementTypeRegistryListener) i
				.next();
			nextListener.elementTypeAdded(e);
		}
	}
	
	private void registerNullElementType() {
		register(NullElementType.getInstance());
	}
}