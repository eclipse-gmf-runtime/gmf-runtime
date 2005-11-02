/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.providers;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.GetRelTypesOnSourceAndTargetOperation;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.GetRelTypesOnSourceOperation;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.GetRelTypesOnTargetOperation;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.GetTypesForSourceOperation;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.GetTypesForTargetOperation;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.ModelingAssistantProvider;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;

/**
 * Provides modeling assistant functionality for geoshapes (i.e. connector
 * handles, connector creation to a new geoshape).
 * 
 * @author cmahoney
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.geoshapes.*
 */
public class GeoshapeModelingAssistantProvider
	extends ModelingAssistantProvider {

	/** List containing the connector types. */
	private static List connectionTypes = null;

	/** List containing the geoshape types. */
	private static List shapeTypes = null;

	/**
	 * Gets the list of connector types initialized lazily.
	 * 
	 * @return the list of connector types
	 */
	private static List getConnectionTypes() {
		if (connectionTypes == null) {
			connectionTypes = Collections.singletonList(GeoshapeType.LINE);
		}
		return connectionTypes;
	}

	/**
	 * Gets the list of shape types initialized lazily.
	 * 
	 * @return the list of shape types
	 */
	private static List getShapeTypes() {
		if (shapeTypes == null) {
			shapeTypes = GeoshapeType.getShapeTypes();
		}
		return shapeTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	public boolean provides(IOperation operation) {
		if (operation instanceof GetRelTypesOnSourceOperation
			|| operation instanceof GetRelTypesOnTargetOperation
			|| operation instanceof GetRelTypesOnSourceAndTargetOperation
			|| operation instanceof GetTypesForTargetOperation
			|| operation instanceof GetTypesForSourceOperation) {
			return super.provides(operation);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.IModelingAssistantProvider#getRelTypesOnSource(org.eclipse.core.runtime.IAdaptable)
	 */
	public List getRelTypesOnSource(IAdaptable source) {
		return getConnectionTypes();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.IModelingAssistantProvider#getRelTypesOnTarget(org.eclipse.core.runtime.IAdaptable)
	 */
	public List getRelTypesOnTarget(IAdaptable target) {
		return getConnectionTypes();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.IModelingAssistantProvider#getRelTypesOnSourceAndTarget(org.eclipse.core.runtime.IAdaptable,
	 *      org.eclipse.core.runtime.IAdaptable)
	 */
	public List getRelTypesOnSourceAndTarget(IAdaptable source,
			IAdaptable target) {
		return getConnectionTypes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.IModelingAssistantProvider#getTypesForSource(org.eclipse.core.runtime.IAdaptable,
	 *      org.eclipse.gmf.runtime.emf.core.internal.util.IElementType)
	 */
	public List getTypesForSource(IAdaptable target,
			IElementType relationshipType) {

		if (relationshipType.equals(GeoshapeType.LINE)) {
			return getShapeTypes();
		}

		return Collections.EMPTY_LIST;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.IModelingAssistantProvider#getTypesForTarget(org.eclipse.core.runtime.IAdaptable,
	 *      org.eclipse.gmf.runtime.emf.core.internal.util.IElementType)
	 */
	public List getTypesForTarget(IAdaptable source,
			IElementType relationshipType) {

		if (relationshipType.equals(GeoshapeType.LINE)) {
			return getShapeTypes();
		}

		return Collections.EMPTY_LIST;
	}
}