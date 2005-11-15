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

package org.eclipse.gmf.runtime.diagram.ui.internal.requests;

import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.notation.View;

/**
 * A class that encapsulates data for appearance properties change request.
 * 
 * @author sshaw / nbalaba
 */
public class ApplyAppearancePropertiesRequest extends Request {

	/**
	 * reference to the <code>View</code> to copy the appearance styles from.
	 */
	private View viewToCopyFrom = null;
	
	/**
	 * Create ApplyAppearancePropertiesRequest given the properties map.
	 */
	public ApplyAppearancePropertiesRequest() {
		super(RequestConstants.REQ_APPLY_APPEARANCE_PROPERTIES);
	}

	/**
	 * @return <code>View</code> that is used as a baseline for copying the appearance styles of.
	 */
	public View getViewToCopyFrom() {
		return viewToCopyFrom;
	}

	/**
	 * @param viewToCopyFrom the <code>View</code> that is used as a baseline for copying the appearance styles of.
	 */
	public void setViewToCopyFrom(View viewToCopyFrom) {
		this.viewToCopyFrom = viewToCopyFrom;
	}
}
