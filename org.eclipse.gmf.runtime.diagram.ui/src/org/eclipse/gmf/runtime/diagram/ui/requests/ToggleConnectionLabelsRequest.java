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

package org.eclipse.gmf.runtime.diagram.ui.requests;

import org.eclipse.gef.Request;

/**
 * Request to set the visiblity of the labels.
 * 
 * @author jcorchis
 */
public class ToggleConnectionLabelsRequest
	extends Request {

	/** show or hide flag */
	private boolean showConnectionLabel;

	/**
	 * Constructor
	 * 
	 * @param showConnectionLabel
	 *            to show/hide the labels
	 */
	public ToggleConnectionLabelsRequest(boolean showConnectionLabel) {
		super(RequestConstants.REQ_TOGGLE_CONNECTION_LABELS);
		this.showConnectionLabel = showConnectionLabel;
	}

	/**
	 * gets the show/hide flag.
	 * 
	 * @return <code>true</code> or <code>flase</code>
	 */
	public final boolean showConnectionLabel() {
		return showConnectionLabel;
	}
}
