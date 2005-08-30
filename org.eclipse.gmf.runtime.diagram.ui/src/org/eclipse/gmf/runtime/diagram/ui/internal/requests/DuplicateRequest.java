/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.requests;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.requests.GroupRequest;

import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;

/**
 * The request used to duplicate a list of editparts. A list that will hold the
 * new duplicated views after the command is executed can be retrieved via
 * <code>getDuplicatedViews()</code>.
 * 
 * @author cmahoney
 * @canBeSeenBy %level1
 */
public class DuplicateRequest
	extends GroupRequest {

	/**
	 * This will be populated with the views that are duplicated after the
	 * command executes.
	 */
	List duplicatedViews = new ArrayList();

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
	public List getDuplicatedViews() {
		return duplicatedViews;
	}

}