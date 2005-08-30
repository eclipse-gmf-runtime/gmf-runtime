/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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

