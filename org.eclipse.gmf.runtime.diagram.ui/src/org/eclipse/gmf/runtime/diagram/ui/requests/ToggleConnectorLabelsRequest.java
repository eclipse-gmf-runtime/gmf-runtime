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
 *  * @deprecated Renamed to {@link org.eclipse.gmf.runtime.diagram.ui.requests.ToggleConnectionLabelsRequest}
 */
public class ToggleConnectorLabelsRequest extends Request {
	
	/** show or hide flag */
	private boolean _showConnectorLabel;
	
	/**
	 * Constructor
	 * @param showConnectorLabel to show/hide the labels
	 */
	public ToggleConnectorLabelsRequest(boolean showConnectorLabel) {
		super(RequestConstants.REQ_TOGGLE_CONNECTION_LABELS);
		_showConnectorLabel = showConnectorLabel;
	}
	
	/**
	 * gets the show/hide flag.
	 * @return <code>true</code> or <code>flase</code>
	 */
	public final boolean showConnectorLabel() {
		return _showConnectorLabel;
	}	
}
