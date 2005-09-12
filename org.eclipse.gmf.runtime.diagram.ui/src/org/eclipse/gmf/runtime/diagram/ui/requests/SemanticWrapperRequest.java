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

package org.eclipse.gmf.runtime.diagram.ui.requests;

import org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.SemanticRequest;
import org.eclipse.gmf.runtime.diagram.core.internal.util.SemanticRequestTranslator;

/**
 * A GEF request that wraps a semantic request
 * 
 * @author melaasar
 * 
 * @deprecated Use
 *             {@link org.eclipse.gmf.runtime.diagram.ui.requests.EditCommandRequestWrapper}
 *             instead.
 */
public class SemanticWrapperRequest extends EditCommandRequestWrapper {

	/** the real semantic service request */
	private final SemanticRequest semanticRequest;

	/**
	 * Creates a SemanticWrapperRequest with the given request type
	 *
	 * @param type The request type
	 * @param semanticRequest The semantic service request
	 */
	public SemanticWrapperRequest(Object type, SemanticRequest semanticRequest) {
		super(type, SemanticRequestTranslator.translate(semanticRequest));
		
		this.semanticRequest = semanticRequest;
	}

	/**
	 * Creates a SemanticWrapperRequest with the default type
	 * @param semanticRequest The semantic service request
	 */
	public SemanticWrapperRequest(SemanticRequest semanticRequest) {
		this(RequestConstants.REQ_SEMANTIC_WRAPPER, semanticRequest);
	}

	/**
	 * Returns the semanticRequest.
	 * @return SemanticRequest
	 */
	public SemanticRequest getSemanticRequest() {
		return semanticRequest;
	}

}
