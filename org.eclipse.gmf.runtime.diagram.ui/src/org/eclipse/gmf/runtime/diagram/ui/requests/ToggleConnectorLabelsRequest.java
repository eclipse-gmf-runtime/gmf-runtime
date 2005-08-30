/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.requests;

import org.eclipse.gef.Request;

/**
 * Request to set the visiblity of the labels.
 * 
 * @author jcorchis
 */
public class ToggleConnectorLabelsRequest extends Request {
	
	/** show or hide flag */
	private boolean _showConnectorLabel;
	
	/**
	 * Constructor
	 * @param showConnectorLabel to show/hide the labels
	 */
	public ToggleConnectorLabelsRequest(boolean showConnectorLabel) {
		super(RequestConstants.REQ_TOGGLE_CONNECTOR_LABELS);
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
