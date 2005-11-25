/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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

		} else if (operation instanceof GetTypesOperation) {

			return !getTypes(((GetTypesOperation) operation).getHint(),
				((GetTypesOperation) operation).getData()).isEmpty();

		} else if (operation instanceof GetTypesForPopupBarOperation) {

			return !getTypesForPopupBar(
				((GetTypesForPopupBarOperation) operation).getHost())
				.isEmpty();

		}
		return false;
	}

	/**
	 * Returns an empty list.
	 */
	public List getTypes(String hint, IAdaptable data) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * Returns the same list of relationship types as
	 * <code>getRelTypesOnSource</code>.
	 */
	public List getRelTypesForSREOnSource(IAdaptable source) {
		return getRelTypesOnSource(source);
	}

	/**
	 * Returns the same list of relationship types as
	 * <code>getRelTypesOnTarget</code>.
	 */
	public List getRelTypesForSREOnTarget(IAdaptable target) {
		return getRelTypesOnTarget(target);
	}

	/**
	 * Returns an empty list.
	 */
	public List getRelTypesOnSource(IAdaptable source) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * Returns an empty list.
	 */
	public List getRelTypesOnTarget(IAdaptable target) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * Returns an empty list.
	 */
	public List getRelTypesOnSourceAndTarget(IAdaptable source,
			IAdaptable target) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * Returns an empty list.
	 */
	public List getTypesForSource(IAdaptable target,
			IElementType relationshipType) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * Returns an empty list.
	 */
	public List getTypesForTarget(IAdaptable source,
			IElementType relationshipType) {
		return Collections.EMPTY_LIST;
	}

	public EObject selectExistingElementForSource(IAdaptable target,
			IElementType relationshipType) {

		return null;
	}

	/**
	 * Returns null.
	 */
	public EObject selectExistingElementForTarget(IAdaptable source,
			IElementType relationshipType) {

		return null;
	}

	/**
	 * Returns an empty list.
	 */
	public List getTypesForPopupBar(IAdaptable host) {
		return Collections.EMPTY_LIST;
	}

}
