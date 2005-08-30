/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.internal.services.semantic;

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * @author melaasar
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.core.*
 *
 * @deprecated Use the Element Type API
 *             {@link org.eclipse.gmf.runtime.emf.type.core.IElementType#getEditCommand(IEditCommandRequest)}
 *             to get semantic commands.
 */
public class GetCommandOperation implements IOperation {

	/**
	 * the semantic request
	 */
	private final SemanticRequest _semanticRequest;


	/**
	 * Method GetCommandOperation.
	 * @param semanticRequest
	 * @param modelOperationContext
	 */
	public GetCommandOperation(SemanticRequest semanticRequest) {
			_semanticRequest = semanticRequest;
	}
	/**
	 * Returns the semanticRequest.
	 * @return SemanticRequest
	 */
	public SemanticRequest getSemanticRequest() {
		return _semanticRequest;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(IProvider)
	 */
	public Object execute(IProvider provider) {
		return ((ISemanticProvider) provider).getCommand(
			getSemanticRequest());
	}

}
