/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.tools;

import java.util.List;

import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateUnspecifiedTypeConnectionRequest;

/**
 * This specialized connection tool adds support for a multi-type connection tool.
 * That is, the tool is given a list of connection types and when the user
 * completes the gesture, a popup appears asking the user to pick one of the
 * connection types.
 * 
 * @author cmahoney
 */
public class UnspecifiedTypeConnectionTool
	extends ConnectionCreationTool {

	/**
	 * The possible connection types to appear in the popup (of type
	 * <code>IElementType</code>).
	 */
	private List connectionTypes;

	/**
	 * Creates a new instance with a list of possible connection types.
	 * 
	 * @param connectionTypes
	 *            The possible connection types to appear in the popup (of
	 *            type <code>IElementType</code>).
	 */
	public UnspecifiedTypeConnectionTool(List connectionTypes) {
		super();
		this.connectionTypes = connectionTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.tools.TargetingTool#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		return new CreateUnspecifiedTypeConnectionRequest(connectionTypes,
			false, getPreferencesHint());
	}

}