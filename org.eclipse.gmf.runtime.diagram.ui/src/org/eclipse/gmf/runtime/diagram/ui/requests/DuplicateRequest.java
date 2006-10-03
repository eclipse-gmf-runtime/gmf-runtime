/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.requests;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.requests.GroupRequest;

/**
 * The request used to duplicate a list of editparts. A list that will hold the
 * new duplicated views after the command is executed can be retrieved via
 * <code>getDuplicatedViews()</code>.
 * 
 * @author cmahoney
 */
public final class DuplicateRequest
	extends GroupRequest {

	/**
	 * This will be populated with the views that are duplicated after the
	 * command executes.
	 */
	private List duplicatedViews = new ArrayList();
    
    /**
     * The offset from the location of the original views where the new views
     * will be placed.
     */
    private Point offset;


	/**
	 * Creates a new <code>DuplicateElementsRequest</code>.
	 */
	public DuplicateRequest() {
		super(RequestConstants.REQ_DUPLICATE);
	}

	/**
	 * Gets the list that will hold the new duplicated views after the command
	 * is executed.
	 * 
	 * @return Returns the duplicatedViews.
	 */
	public final List getDuplicatedViews() {
		return duplicatedViews;
	}

    /**
     * Gets the offset from the location of the original views where the new
     * views will be placed.
     * 
     * @return the offset
     */
    public final Point getOffset() {
        return offset;
    }

    /**
     * Sets the offset from the location of the original views where the new
     * views will be placed.
     * 
     * @param offset
     *            The offset to set.
     */
    public final void setOffset(Point offset) {
        this.offset = offset;
    }

}