/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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