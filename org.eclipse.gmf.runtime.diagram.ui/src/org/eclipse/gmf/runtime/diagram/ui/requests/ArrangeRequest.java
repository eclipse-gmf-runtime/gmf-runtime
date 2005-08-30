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

import org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutType;

/**
 * A request to arrange a set of elements.
 * 
 * @author chmahone
 */
public class ArrangeRequest extends Request {

	/** List of <code>EditPart</code> objects */
	protected List editParts = null;

	/** List of <code>IView</code> objects */
	protected List viewAdapters = null;
	
	private String layoutType;

	/**
	 * Constructor for ArrangeRequest.
	 * @param type the request type
	 */
	public ArrangeRequest(String type) {
		super(type);	
		this.layoutType = LayoutType.DEFAULT;	
	}
	
	/**
	 * Constructor for ArrangeRequest.
	 * @param type
	 * @param requestLayoutType
	 */
	public ArrangeRequest(String type, String requestLayoutType) {
		super(type);
		if (requestLayoutType != null){
			this.layoutType = requestLayoutType;
		}	
		else{
			this.layoutType = LayoutType.DEFAULT;
		}	
	}

	/**
	 * Sets the editparts to arrange.
	 * @param ep List of <code>EditPart</code> objects
	 */
	public void setPartsToArrange(List ep) {
		editParts = new ArrayList(ep);
	}

	/**
	 * Gets the editparts to arrange.
	 * @return List List of <code>EditPart</code> objects;
	 * null if this was never set
	 */
	public List getPartsToArrange() {
		return editParts;
	}

	/**
	 * Sets the view adapters for the views to arrange.
	 * @param va List of <code>IView</code> objects
	 */
	public void setViewAdaptersToArrange(List va) {
		viewAdapters = va;
	}

	/**
	 * Gets the view adapters for the views to arrange.
	 * @return List List of <code>IView</code> objects;
	 * null if this was never set
	 */
	public List getViewAdaptersToArrange() {
		return viewAdapters;
	}
	
	/**
	 * Gets the LayoutType.
	 * @return LayoutType
	 */
	public String getLayoutType(){
		return this.layoutType;
	}
}
