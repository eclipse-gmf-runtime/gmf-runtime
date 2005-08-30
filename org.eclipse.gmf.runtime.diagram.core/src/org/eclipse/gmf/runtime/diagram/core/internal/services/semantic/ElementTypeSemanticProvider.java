/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.internal.services.semantic;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.internal.util.SemanticRequestTranslator;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.IMetamodelType;
import org.eclipse.gmf.runtime.emf.type.core.ISpecializationType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * Lowest-priority provider that will return edit commands for semantic
 * requests. Gets the commands from the edit helpers and edit helper advice
 * registered in the {@link org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry}
 * 
 * @author ldamus
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.core.*
 */
public class ElementTypeSemanticProvider
	extends AbstractSemanticProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.AbstractSemanticProvider#getCommand(org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.SemanticRequest)
	 */
	public ICommand getCommand(SemanticRequest semanticRequest) {

		ICommand result = null;

		IEditCommandRequest editCommandRequest = SemanticRequestTranslator
			.translate(semanticRequest);
		
		// Check the type of the element type to be created if
		// this is a creation request.
		if (editCommandRequest instanceof CreateElementRequest) {
			CreateElementRequest createRequest = (CreateElementRequest) editCommandRequest;
			IElementType elementType = createRequest.getElementType();

			if (!(elementType instanceof ISpecializationType)
				&& !(elementType instanceof IMetamodelType)) {
				
				// Reject any element type that does not implement 
				// one of the "new" interfaces.  This means this element
				// type hasn't been migrated to the new API, so there's no
				// edit helper to do the work.
				return result;
			}

		}

		// Parameter used to indicate to the edit helpers that they should not
		// re-delegate to the semantic service.
		editCommandRequest.setParameter("USE_EDIT_HELPERS", "true"); //$NON-NLS-1$ //$NON-NLS-2$

		IElementType containerType = null;

		Object editHelperContext = editCommandRequest.getEditHelperContext();

		containerType = ElementTypeRegistry.getInstance().getElementType(
			editHelperContext);

		if (containerType != null
			&& (containerType instanceof ISpecializationType
			|| containerType instanceof IMetamodelType)) {
			
			// Reject any container type that does not implement 
			// one of the "new" interfaces.  This means this container
			// type hasn't been migrated to the new API, so there's no
			// edit helper to do the work.
			
			result = containerType.getEditCommand(editCommandRequest);
		}
		
		if (result == null || !result.isExecutable()) {
			editCommandRequest.setParameter("USE_EDIT_HELPERS", null); //$NON-NLS-1$
		}
		
		return result;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.AbstractSemanticProvider#supportsRequest(org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.SemanticRequest)
	 */
	protected boolean supportsRequest(SemanticRequest semanticRequest) {
		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.AbstractSemanticProvider#understandsRequest(org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.SemanticRequest)
	 */
	protected boolean understandsRequest(SemanticRequest semanticRequest) {
		ICommand command = getCommand(semanticRequest);
		return command != null;
	}
	
}