/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.          	       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.tools;

import java.util.List;

import org.eclipse.gef.Request;

import org.eclipse.gmf.runtime.diagram.ui.requests.CreateUnspecifiedTypeConnectionRequest;

/**
 * This specialized connector tool adds support for a multi-type connector tool.
 * That is, the tool is given a list of relationship types and when the user
 * completes the gesture, a popup appears asking the user to pick one of the
 * relationship types.
 * 
 * @author cmahoney
 */
public class UnspecifiedTypeConnectorTool
	extends ConnectorCreationTool {

	/**
	 * The possible relationship types to appear in the popup (of type
	 * <code>IElementType</code>).
	 */
	private List relationshipTypes;

	/**
	 * Creates a new instance with a list of possible relationship types.
	 * 
	 * @param relationshipTypes
	 *            The possible relationship types to appear in the popup (of
	 *            type <code>IElementType</code>).
	 */
	public UnspecifiedTypeConnectorTool(List relationshipTypes) {
		super();
		this.relationshipTypes = relationshipTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.tools.TargetingTool#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		return new CreateUnspecifiedTypeConnectionRequest(relationshipTypes,
			false, getPreferencesHint());
	}

}