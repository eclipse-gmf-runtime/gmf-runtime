/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
