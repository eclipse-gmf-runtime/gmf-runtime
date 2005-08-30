/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.ui.services.modelingassistant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;

/**
 * This service is used to assist the user with modeling gestures.
 * 
 * @author cmahoney
 */
public class ModelingAssistantService
	extends Service
	implements IModelingAssistantProvider {

	/**
	 * A descriptor for <code>IModelingAssistantProvider</code> defined
	 * by a configuration element.
	 */
	protected static class ProviderDescriptor
		extends Service.ProviderDescriptor {

		/** the provider configuration parsed from XML */
		private ModelingAssistantProviderConfiguration providerConfiguration;

		/**
		 * Constructs a <code>ISemanticProvider</code> descriptor for
		 * the specified configuration element.
		 * 
		 * @param element The configuration element describing the provider.
		 */
		public ProviderDescriptor(IConfigurationElement element) {
			super(element);

			this.providerConfiguration = ModelingAssistantProviderConfiguration
				.parse(element);
			assert providerConfiguration != null: "providerConfiguration is null"; //$NON-NLS-1$
		}

		/**
		 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
		 */
		public boolean provides(IOperation operation) {
			if (getPolicy() != null)
				return getPolicy().provides(operation);

			return isSupportedInExtension(operation) ? getProvider().provides(
				operation) : false;
		}

		/**
		 * Checks if the operation is supported by the XML extension
		 * @param operation
		 * @return true if the operation is supported; false otherwise
		 */
		private boolean isSupportedInExtension(IOperation operation) {
			if (operation instanceof IModelingAssistantOperation) {
				String operationId = ((IModelingAssistantOperation) operation)
					.getId();
				IAdaptable context = ((IModelingAssistantOperation) operation)
					.getContext();

				return providerConfiguration.supports(operationId, context);
			}
			return false;
		}

	}

	/** The singleton instance of the modeling assistant service. */
	private final static ModelingAssistantService service = new ModelingAssistantService();

	/**
	 * @see org.eclipse.gmf.runtime.common.core.service.Service#newProviderDescriptor(org.eclipse.core.runtime.IConfigurationElement)
	 */
	protected Service.ProviderDescriptor newProviderDescriptor(
		IConfigurationElement element) {
		return new ProviderDescriptor(element);
	}

	/**
	 * Retrieves the singleton instance of the modeling assistant service.
	 * 
	 * @return The modeling assistant service singleton.
	 */
	public static ModelingAssistantService getInstance() {
		return service;
	}

	/**
	 * Accepts a list that may contain other lists and returns a list
	 * containing all the objects.  Also removes duplicates.
	 * @param list
	 * @return the collapsed list
	 */
	private static List collapseList(List list) {
		List collapsedList = new ArrayList();

		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Object object = iter.next();
			if (object instanceof List) {
				for (Iterator iterator = ((List) object).iterator(); iterator
					.hasNext();) {
					Object subObject = iterator.next();
					if (!collapsedList.contains(subObject)) {
						collapsedList.add(subObject);
					}
				}
			} else {
				collapsedList.add(object);
			}
		}
		return collapsedList;
	}

	/**
	 * Executes the <code>GetTypesForAttributeOperation</code> using the 
	 * <code>FORWARD</code> execution strategy.
	 * 
	 * @see com.ibm.xtools.msl.ui.internal.services.modelingassistant.IModelingAssistantProvider#getTypesForAttribute(IAdaptable)
	 */
	public List getTypesForAttribute(IAdaptable attribute) {
		List results = execute(ExecutionStrategy.FORWARD,
			new GetTypesForAttributeOperation(attribute));
		return collapseList(results);
	}

	/**
	 * Executes the <code>GetRelTypesOnSourceOperation</code> using the 
	 * <code>FORWARD</code> execution strategy.
	 * 
	 * @see com.ibm.xtools.msl.ui.internal.services.modelingassistant.IModelingAssistantProvider#getRelTypesOnSource(IAdaptable)
	 */
	public List getRelTypesOnSource(IAdaptable source) {
		List results = execute(ExecutionStrategy.FORWARD,
			new GetRelTypesOnSourceOperation(source));
		return collapseList(results);
	}

	/**
	 * Executes the <code>GetRelTypesOnTargetOperation</code> using the 
	 * <code>FORWARD</code> execution strategy.
	 * 
	 * @see org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.IModelingAssistantProvider#getRelTypesOnTarget(IAdaptable)
	 */
	public List getRelTypesOnTarget(IAdaptable target) {
		List results = execute(ExecutionStrategy.FORWARD,
			new GetRelTypesOnTargetOperation(target));
		return collapseList(results);
	}

	/**
	 * Executes the <code>GetRelTypesOnSourceAndTargetOperation</code> using the 
	 * <code>FORWARD</code> execution strategy.
	 * 
	 * @see com.ibm.xtools.msl.ui.internal.services.modelingassistant.IModelingAssistantProvider#getRelTypesOnSourceAndTarget(IAdaptable, IAdaptable)
	 */
	public List getRelTypesOnSourceAndTarget(IAdaptable source, IAdaptable target) {
		List results = execute(ExecutionStrategy.FORWARD,
			new GetRelTypesOnSourceAndTargetOperation(source, target));
		return collapseList(results);
	}

	/**
	 * Executes the <code>GetRelTypesForSREOnSourceOperation</code> using the 
	 * <code>FORWARD</code> execution strategy.
	 * 
	 * @see com.ibm.xtools.msl.ui.internal.services.modelingassistant.IModelingAssistantProvider#getRelTypesForSREOnSource(org.eclipse.core.runtime.IAdaptable)
	 */
	public List getRelTypesForSREOnSource(IAdaptable source) {
		List results = execute(ExecutionStrategy.FORWARD,
			new GetRelTypesForSREOnSourceOperation(source));
		return collapseList(results);
	}

	/**
	 * Executes the <code>GetRelTypesForSREOnTargetOperation</code> using the 
	 * <code>FORWARD</code> execution strategy.
	 * 
	 * @see com.ibm.xtools.msl.ui.internal.services.modelingassistant.IModelingAssistantProvider#getRelTypesForSREOnTarget(IAdaptable)
	 */
	public List getRelTypesForSREOnTarget(IAdaptable target) {
		List results = execute(ExecutionStrategy.FORWARD,
			new GetRelTypesForSREOnTargetOperation(target));
		return collapseList(results);
	}

	/**
	 * Executes the <code>GetTypesForSourceOperation</code> using the 
	 * <code>FORWARD</code> execution strategy.
	 * 
	 * @see com.ibm.xtools.msl.ui.internal.services.modelingassistant.IModelingAssistantProvider#getTypesForSource(IAdaptable, IElementType)
	 */
	public List getTypesForSource(IAdaptable target,
		IElementType relationshipType) {
		List results = execute(ExecutionStrategy.FORWARD,
			new GetTypesForSourceOperation(target, relationshipType));
		return collapseList(results);
	}

	/**
	 * Executes the <code>GetTypesForTargetOperation</code> using the 
	 * <code>FORWARD</code> execution strategy.
	 * 
	 * @see com.ibm.xtools.msl.ui.internal.services.modelingassistant.IModelingAssistantProvider#getTypesForTarget(IAdaptable, IElementType)
	 */
	public List getTypesForTarget(IAdaptable source,
		IElementType relationshipType) {
		List results = execute(ExecutionStrategy.FORWARD,
			new GetTypesForTargetOperation(source, relationshipType));
		return collapseList(results);
	}

	/**
	 * Executes the <code>SelectExistingElementForSourceOperation</code> using the 
	 * <code>FIRST</code> execution strategy.
	 * 
	 * @see com.ibm.xtools.msl.ui.internal.services.modelingassistant.IModelingAssistantProvider#selectExistingElementForSource(IAdaptable, IElementType)
	 */
	public EObject selectExistingElementForSource(IAdaptable target,
		IElementType relationshipType) {
		List results = execute(ExecutionStrategy.FIRST,
			new SelectExistingElementForSourceOperation(target,
				relationshipType));
		return (EObject) results.get(0);
	}

	/**
	 * Executes the <code>SelectExistingElementForTargetOperation</code> using the 
	 * <code>FIRST</code> execution strategy.
	 * 
	 * @see com.ibm.xtools.msl.ui.internal.services.modelingassistant.IModelingAssistantProvider#selectExistingElementForTarget(IAdaptable, IElementType)
	 */
	public EObject selectExistingElementForTarget(IAdaptable source,
		IElementType relationshipType) {
		List results = execute(ExecutionStrategy.FIRST,
			new SelectExistingElementForTargetOperation(source,
				relationshipType));
		return (EObject) results.get(0);
	}

	/**
	 * Executes the <code>GetTypesForActionBarOperation</code> using the 
	 * <code>FORWARD</code> execution strategy.
	 * 
	 * @see org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.IModelingAssistantProvider#getTypesForActionBar(IAdaptable)
	 */
	public List getTypesForActionBar(IAdaptable host) {
		List results = execute(ExecutionStrategy.FORWARD,
			new GetTypesForActionBarOperation(host));
		return collapseList(results);
	}

}
