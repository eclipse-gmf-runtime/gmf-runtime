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
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.common.ui.util.ICustomData;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;

/**
 * A request to paste the clipboard data on to the view
 * 
 * @author vramaswa
 * @canBeSeenBy %level1
 */
public class PasteViewRequest extends Request {
    /**
     * The clipboard data used for the paste
     */
    private final ICustomData[] data;

    /**
     * Constructor for PasteViewRequest.
     * @param data The clipboard data
     */
    public PasteViewRequest(ICustomData[] data) {
        super(RequestConstants.REQ_PASTE);

        Assert.isNotNull(data);
        this.data = data;
    }

    /**
     * Returns the data.
     * @return ICustomData[]
     */
    public ICustomData[] getData() {
        return data;
    }
}
