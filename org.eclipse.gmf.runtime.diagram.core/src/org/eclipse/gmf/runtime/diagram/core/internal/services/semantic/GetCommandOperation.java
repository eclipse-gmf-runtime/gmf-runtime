/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.internal.services.semantic;

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;

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
