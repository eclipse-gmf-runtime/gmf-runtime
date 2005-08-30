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

import org.eclipse.gmf.runtime.diagram.ui.requests.CreateUnspecifiedTypeRequest;

/**
 * This specialized creation tool adds support for a multi-type creation tool.
 * That is, the tool is given a list of element types and when the user completes
 * the gesture, a popup appears asking the user to pick one of the element types
 * to be created.
 * 
 * @author cmahoney
 */
public class UnspecifiedTypeCreationTool
	extends CreationTool {

	/**
	 * List of element types of which one will be created (of type
	 * <code>IElementType</code>).
	 */
	private List elementTypes;

	/**
	 * Creates a new instance with a list of possible element types.
	 * 
	 * @param elementTypes
	 *            List of element types of which one will be created (of type
	 *            <code>IElementType</code>).
	 */
	public UnspecifiedTypeCreationTool(List elementTypes) {
		super();
		this.elementTypes = elementTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.tools.TargetingTool#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		return new CreateUnspecifiedTypeRequest(elementTypes, getPreferencesHint());
	}
}