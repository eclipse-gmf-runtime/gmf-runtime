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

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.ITerminalOwnerEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.LogicFlowCompartmentEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.LogicShapeCompartmentEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.util.LogicSemanticType;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.ModelingAssistantProvider;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;

/**
 * Provides modeling assistant services for logic diagrams.
 * 
 * @author qili, cmahoney
 */
public class LogicModelingAssistantProvider
	extends ModelingAssistantProvider {

	/** List containing the one relationship type -- transition */
	private static final List wireType = Collections
		.singletonList(LogicSemanticType.WIRE);

	/** List containing valid types for the target */
	private static final List targetTypes = new ArrayList(1);
	static {
		targetTypes.add(LogicSemanticType.TERMINAL);
	}
	
	/** List containing valid logic shape types for the action bars */
	private static final List logicShapeTypes = new ArrayList(5);
	static {
		logicShapeTypes.add(LogicSemanticType.FLOWCONTAINER);
		logicShapeTypes.add(LogicSemanticType.CIRCUIT);
		logicShapeTypes.add(LogicSemanticType.LED);
		logicShapeTypes.add(LogicSemanticType.ORGATE);
		logicShapeTypes.add(LogicSemanticType.ANDGATE);
		logicShapeTypes.add(LogicSemanticType.XORGATE);
	}

	/**
	 * @see com.ibm.xtools.bml.ui.internal.services.modelingassistant.IModelingAssistantProvider#getRelTypesOnSource(IAdaptable)
	 */
	public List getRelTypesOnSource(IAdaptable source) {
		if (source.getAdapter(ITerminalOwnerEditPart.class) != null) {
			return wireType;
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @see com.ibm.xtools.bml.ui.internal.services.modelingassistant.IModelingAssistantProvider#getRelTypesOnSourceAndTarget(IAdaptable,
	 *      IAdaptable)
	 */
	public List getRelTypesOnSourceAndTarget(IAdaptable source,
			IAdaptable target) {
		if (source.getAdapter(ITerminalOwnerEditPart.class) != null
			&& target.getAdapter(ITerminalOwnerEditPart.class) != null) {
			return wireType;
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @see com.ibm.xtools.bml.ui.internal.services.modelingassistant.IModelingAssistantProvider#getRelTypesOnTarget(IAdaptable)
	 */
	public List getRelTypesOnTarget(IAdaptable target) {
		if (target.getAdapter(ITerminalOwnerEditPart.class) != null) {
			return wireType;
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @see com.ibm.xtools.bml.ui.internal.services.modelingassistant.IModelingAssistantProvider#getTypesForTarget(IAdaptable,
	 *      IElementType)
	 */
	public List getTypesForTarget(IAdaptable source,
			IElementType relationshipType) {
		if (source.getAdapter(ITerminalOwnerEditPart.class) != null
			&& wireType.contains(relationshipType)) {
			return targetTypes;
		}
		return Collections.EMPTY_LIST;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.IModelingAssistantProvider#getTypesForActionBar(org.eclipse.core.runtime.IAdaptable)
	 */
	public List getTypesForActionBar(IAdaptable host) {
		Object ep = host.getAdapter(IGraphicalEditPart.class);
		if (ep instanceof LogicShapeCompartmentEditPart
			|| ep instanceof LogicFlowCompartmentEditPart) {
			return logicShapeTypes;
		}
		return Collections.EMPTY_LIST;
	}
}