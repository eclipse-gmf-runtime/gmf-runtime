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

package org.eclipse.gmf.runtime.diagram.ui.providers.internal;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.ui.editparts.NoteEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.PresentationNotationType;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.GetRelTypesOnSourceAndTargetOperation;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.GetRelTypesOnSourceOperation;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.GetRelTypesOnTargetOperation;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.GetTypesForSourceOperation;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.GetTypesForTargetOperation;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.ModelingAssistantProvider;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;

/**
 * Provides modeling assistant functionality for diagram shapes (i.e. connector
 * handles, connector creation to a new shape).
 * 
 * @author cmahoney
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.providers.*
 */
public class DiagramModelingAssistantProvider
	extends ModelingAssistantProvider {

	/** List containing the connector types. */
	private static List connectorTypes = null;

	/** List containing the shape types. */
	private static List shapeTypes = null;

	/**
	 * Gets the list of connector types initialized lazily.
	 * 
	 * @return the list of connector types
	 */
	private static List getConnectorTypes() {
		if (connectorTypes == null) {
			connectorTypes = Collections
				.singletonList(PresentationNotationType.NOTE_ATTACHMENT);
		}
		return connectorTypes;
	}

	/**
	 * Gets the list of shape types initialized lazily.
	 * 
	 * @return the list of shape types
	 */
	private static List getShapeTypes() {
		if (shapeTypes == null) {
			shapeTypes = Collections
				.singletonList(PresentationNotationType.NOTE);
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
		return getConnectorTypes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.IModelingAssistantProvider#getRelTypesOnTarget(org.eclipse.core.runtime.IAdaptable)
	 */
	public List getRelTypesOnTarget(IAdaptable target) {
		return getConnectorTypes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.IModelingAssistantProvider#getRelTypesOnSourceAndTarget(org.eclipse.core.runtime.IAdaptable,
	 *      org.eclipse.core.runtime.IAdaptable)
	 */
	public List getRelTypesOnSourceAndTarget(IAdaptable source,
			IAdaptable target) {
		
		return source.getAdapter(NoteEditPart.class) != null || target.getAdapter(NoteEditPart.class) != null
			? getConnectorTypes() : Collections.EMPTY_LIST;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.IModelingAssistantProvider#getTypesForSource(org.eclipse.core.runtime.IAdaptable,
	 *      org.eclipse.gmf.runtime.emf.core.internal.util.IElementType)
	 */
	public List getTypesForSource(IAdaptable target,
			IElementType relationshipType) {

		// Attaching a note to another note is unnecessary.
		if (target.getAdapter(NoteEditPart.class) != null
			|| !relationshipType
				.equals(PresentationNotationType.NOTE_ATTACHMENT)) {
			return Collections.EMPTY_LIST;
		}

		return getShapeTypes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.IModelingAssistantProvider#getTypesForTarget(org.eclipse.core.runtime.IAdaptable,
	 *      org.eclipse.gmf.runtime.emf.core.internal.util.IElementType)
	 */
	public List getTypesForTarget(IAdaptable source,
			IElementType relationshipType) {

		// Attaching a note to another note is unnecessary.
		if (source.getAdapter(NoteEditPart.class) != null
			|| !relationshipType
				.equals(PresentationNotationType.NOTE_ATTACHMENT)) {
			return Collections.EMPTY_LIST;
		}

		return getShapeTypes();
	}

}