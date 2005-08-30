/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.internal.services.semantic;

import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * @author melaasar
 *
 * Typical semantic request types
 * 
 * @deprecated Use {@link org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest}s
 *             to get commands from the Element Type API
 *             {@link org.eclipse.gmf.runtime.emf.type.core.IElementType#getEditCommand(IEditCommandRequest)}.
 */
public interface SemanticRequestTypes {

	public static final String 
		SEMREQ_CREATE_COMPONENT_ELEMENT = "create_component_element",//$NON-NLS-1$
		SEMREQ_CREATE_RELATIONSHIP_ELEMENT = "create_relationship_element",//$NON-NLS-1$
		SEMREQ_DESTROY_ELEMENT = "destroy_element",//$NON-NLS-1$
		SEMREQ_MOVE_ELEMENT = "move_element",//$NON-NLS-1$
		SEMREQ_REORIENT_RELATIONSHIP_SOURCE = "reorient_relationship_source",//$NON-NLS-1$
		SEMREQ_REORIENT_RELATIONSHIP_TARGET = "reorient_relationship_target",//$NON-NLS-1$
		SEMREQ_DUPLICATE_ELEMENTS = "duplicate_elements";//$NON-NLS-1$

}
