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

/**
 * Request to Recalculate Page Breaks.
 * 
 * @author jcorchis
 * @canBeSeenBy %level1
 */
public class RecalculatePageBreaksRequest extends Request {

    /**
     * Constructor for RecalculatePageBreaks.
     */
    public RecalculatePageBreaksRequest() {
        super(RequestConstants.REQ_RECALCULATE_PAGEBREAKS);
    }

    /**
     * Constructor for RecalculatePageBreaks.
     * @param type
     */
    public RecalculatePageBreaksRequest(Object type) {
        super(RequestConstants.REQ_RECALCULATE_PAGEBREAKS);
    }
    
    public Object getType() {
    	return RequestConstants.REQ_RECALCULATE_PAGEBREAKS;
    }    
}

