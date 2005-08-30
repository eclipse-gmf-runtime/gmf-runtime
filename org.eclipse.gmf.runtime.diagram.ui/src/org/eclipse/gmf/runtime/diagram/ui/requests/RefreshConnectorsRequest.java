/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.requests;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.Request;

/**
 * A request to refresh the connectors.  
 * This request could have different options indicating which or how
 * the connectors should be refreshed.
 * Or we could have different types depending on what type of 
 * relationships to refresh.
 * 
 * @author chmahone
 */
public class RefreshConnectorsRequest extends Request {

	/** A list of the view objects in <code>IAdaptable</code> form. */
	List shapes;

	/** true to turn on filtering, false to turn off filtering.
	 * By default, filtering is turned on*/
	boolean filter = true;

	/**
	 * Constructor for RefreshConnectorsRequest.
	 * 
	 * @param shapes list of the view objects in <code>IAdaptable</code>
	 * form
	 */
	public RefreshConnectorsRequest(List shapes) {
		super(RequestConstants.REQ_REFRESH_CONNECTORS);

		this.shapes = new ArrayList(shapes);
	}

	/**
	 * Constructor for RefreshConnectorsRequest.
	 * You can explicitly set the filtering with this constructor.
	 * 
	 * @param shapes list of the view objects in <code>IAdaptable</code>
	 * form
	 * @param filter true to turn on filtering, false to turn it off
	 */
	public RefreshConnectorsRequest(List shapes, boolean filter) {
		this(shapes);
		this.filter = filter;
	}

	/**
	 * Method getShapes.
	 * @return List the list of <code>IAdaptable</code> shapes
	 */
	public List getShapes() {
		return shapes;
	}

	/**
	 * Returns if filtering is turned on or off.
	 * 
	 * @return true if filtering is turned on, false if it is turned off
	 */
	public boolean isFilter() {
		return filter;
	}

}
