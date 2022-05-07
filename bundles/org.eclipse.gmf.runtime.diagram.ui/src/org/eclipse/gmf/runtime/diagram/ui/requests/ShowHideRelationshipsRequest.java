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

package org.eclipse.gmf.runtime.diagram.ui.requests;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.Request;

/**
 * @author schafe
 *
 * Request for showing and hiding relationships.
 */
public class ShowHideRelationshipsRequest extends Request{
	
	List selectedShapes;	
	List relationshipsToShow;
	List relationshipsToHide;

	/**
	 * Constructor for ShowHideRelationshipsRequest.
	 * @param shapes
	 * @param relationshipTypesToShow
	 * @param relationshipTypesToHide
	 */
	public ShowHideRelationshipsRequest(List shapes, List relationshipTypesToShow, List relationshipTypesToHide) {
		super(RequestConstants.REQ_SHOWHIDE_RELATIONSHIPS);

		this.selectedShapes = new ArrayList(shapes);
		this.relationshipsToShow = relationshipTypesToShow;
		this.relationshipsToHide = relationshipTypesToHide;
	}

	/**
	 * Method getShapes.
	 * @return List the list of <code>IAdaptable</code> shapes
	 */
	public List getSelectedShapes() {
		return this.selectedShapes;
	}
	
	
	/**
	 * Method getRelationshipsToShow.
	 * @return List
	 */
	public List getRelationshipsToShow() {
		return this.relationshipsToShow;
	}
	
	
	/**
	 * Method getRelationshipsToHide.
	 * @return List
	 */
	public List getRelationshipsToHide() {
		return this.relationshipsToHide;
	}

}
