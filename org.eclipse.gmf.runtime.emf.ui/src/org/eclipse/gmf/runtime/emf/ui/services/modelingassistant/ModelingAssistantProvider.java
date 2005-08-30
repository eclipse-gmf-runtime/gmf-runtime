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

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;

/**
 * Abstract class for the Modeling Assistant Provider so that subclasses don't
 * have to override methods they do not provide for. Provides some default
 * behavior.
 * 
 * @author cmahoney
 */
public abstract class ModelingAssistantProvider
	extends AbstractProvider
	implements IModelingAssistantProvider {

	/**
	 * <p>
	 * For the <code>Get*TypesFor*</code> operations, this will return true if
	 * the corresponding <code>get*TypesFor*</code> call does not return an
	 * empty list.
	 * </p>
	 * 
	 * <p>
	 * For the <code>SelectElementFor*</code> operations, this will return
	 * true if the corresponding <code>getTypesFor*</code> call does not
	 * return an empty list.
	 * </p>
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	public boolean provides(IOperation operation) {
		if (operation instanceof GetRelTypesOnSourceOperation) {

			return !getRelTypesOnSource(
				((GetRelTypesOnSourceOperation) operation).getSource())
				.isEmpty();

		} else if (operation instanceof GetRelTypesOnTargetOperation) {

			return !getRelTypesOnTarget(
				((GetRelTypesOnTargetOperation) operation).getTarget())
				.isEmpty();

		} else if (operation instanceof GetRelTypesOnSourceAndTargetOperation) {

			return !getRelTypesOnSourceAndTarget(
				((GetRelTypesOnSourceAndTargetOperation) operation).getSource(),
				((GetRelTypesOnSourceAndTargetOperation) operation).getTarget())
				.isEmpty();

		} else if (operation instanceof GetTypesForSourceOperation) {

			return !getTypesForSource(
				((GetTypesForSourceOperation) operation).getTarget(),
				((GetTypesForSourceOperation) operation).getRelationshipType())
				.isEmpty();

		} else if (operation instanceof GetTypesForTargetOperation) {

			return !getTypesForTarget(
				((GetTypesForTargetOperation) operation).getSource(),
				((GetTypesForTargetOperation) operation).getRelationshipType())
				.isEmpty();

		} else if (operation instanceof GetRelTypesForSREOnSourceOperation) {

			return !getRelTypesForSREOnSource(
				((GetRelTypesForSREOnSourceOperation) operation).getSource())
				.isEmpty();

		} else if (operation instanceof GetRelTypesForSREOnTargetOperation) {

			return !getRelTypesForSREOnTarget(
				((GetRelTypesForSREOnTargetOperation) operation).getTarget())
				.isEmpty();

		} else if (operation instanceof SelectExistingElementForSourceOperation) {

			return !getTypesForSource(
				((SelectExistingElementForSourceOperation) operation)
					.getTarget(),
				((SelectExistingElementForSourceOperation) operation)
					.getRelationshipType()).isEmpty();

		} else if (operation instanceof SelectExistingElementForTargetOperation) {

			return !getTypesForTarget(
				((SelectExistingElementForTargetOperation) operation)
					.getSource(),
				((SelectExistingElementForTargetOperation) operation)
					.getRelationshipType()).isEmpty();

		} else if (operation instanceof GetTypesForAttributeOperation) {

			return !getTypesForAttribute(
				((GetTypesForAttributeOperation) operation).getAttribute())
				.isEmpty();

		} else if (operation instanceof GetTypesForActionBarOperation) {

			return !getTypesForActionBar(
				((GetTypesForActionBarOperation) operation).getHost())
				.isEmpty();

		}
		return false;
	}

	/**
	 * Returns an empty list.
	 * 
	 * @see com.ibm.xtools.msl.ui.internal.services.modelingassistant.IModelingAssistantProvider#getTypesForAttribute(IAdaptable)
	 */
	public List getTypesForAttribute(IAdaptable attribute) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * Returns the same list of relationship types as
	 * <code>getRelTypesOnSource</code>.
	 * 
	 * @see org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.IModelingAssistantProvider#getRelTypesForSREOnSource(org.eclipse.core.runtime.IAdaptable)
	 */
	public List getRelTypesForSREOnSource(IAdaptable source) {
		return getRelTypesOnSource(source);
	}

	/**
	 * Returns the same list of relationship types as
	 * <code>getRelTypesOnTarget</code>.
	 * 
	 * @see com.ibm.xtools.msl.ui.internal.services.modelingassistant.IModelingAssistantProvider#getRelTypesForSREOnTarget(IAdaptable)
	 */
	public List getRelTypesForSREOnTarget(IAdaptable target) {
		return getRelTypesOnTarget(target);
	}

	/**
	 * Returns an empty list.
	 * 
	 * @see com.ibm.xtools.msl.ui.internal.services.modelingassistant.IModelingAssistantProvider#getRelTypesOnSource(IAdaptable)
	 */
	public List getRelTypesOnSource(IAdaptable source) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * Returns an empty list.
	 * 
	 * @see com.ibm.xtools.msl.ui.internal.services.modelingassistant.IModelingAssistantProvider#getRelTypesOnTarget(IAdaptable)
	 */
	public List getRelTypesOnTarget(IAdaptable target) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * Returns an empty list.
	 * 
	 * @see com.ibm.xtools.msl.ui.internal.services.modelingassistant.IModelingAssistantProvider#getRelTypesOnSourceAndTarget(IAdaptable,
	 *      IAdaptable)
	 */
	public List getRelTypesOnSourceAndTarget(IAdaptable source,
			IAdaptable target) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * Returns an empty list.
	 * 
	 * @see com.ibm.xtools.msl.ui.internal.services.modelingassistant.IModelingAssistantProvider#getTypesForSource(IAdaptable,
	 *      IElementType)
	 */
	public List getTypesForSource(IAdaptable target,
			IElementType relationshipType) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * Returns an empty list.
	 * 
	 * @see com.ibm.xtools.msl.ui.internal.services.modelingassistant.IModelingAssistantProvider#getTypesForTarget(IAdaptable,
	 *      IElementType)
	 */
	public List getTypesForTarget(IAdaptable source,
			IElementType relationshipType) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @see com.ibm.xtools.msl.ui.internal.services.modelingassistant.IModelingAssistantProvider#selectExistingElementForSource(IAdaptable,
	 *      IElementType)
	 */
	public EObject selectExistingElementForSource(IAdaptable target,
			IElementType relationshipType) {

		return null;
	}

	/**
	 * Returns null.
	 * 
	 * @see com.ibm.xtools.msl.ui.internal.services.modelingassistant.IModelingAssistantProvider#selectExistingElementForTarget(IAdaptable,
	 *      IElementType)
	 */
	public EObject selectExistingElementForTarget(IAdaptable source,
			IElementType relationshipType) {

		return null;
	}

	/**
	 * Returns an empty list.
	 * 
	 * @see org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.IModelingAssistantProvider#getTypesForActionBar(IAdaptable)
	 */
	public List getTypesForActionBar(IAdaptable host) {
		return Collections.EMPTY_LIST;
	}
}

