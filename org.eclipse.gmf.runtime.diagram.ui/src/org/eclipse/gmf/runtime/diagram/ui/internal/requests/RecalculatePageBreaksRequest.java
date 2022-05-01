/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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

