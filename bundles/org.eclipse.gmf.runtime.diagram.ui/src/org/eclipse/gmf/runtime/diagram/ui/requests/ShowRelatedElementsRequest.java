/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
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
import org.eclipse.gmf.runtime.common.ui.dialogs.ExpansionType;

/**
 * @author schafe 
 *
 * Request for showing related elements.
 */
public class ShowRelatedElementsRequest extends Request {

	private List selectedShapes;
	private List relatedShapes;
	private List relationshipsToShow;
	private boolean isExpandIndefinite;
	private int expandLevel;

	/**
	 * The expansion type, which could be incoming, outgoing, both, or all connected
	 */
	protected ExpansionType expansionType;
	
	/**
	 * The Stopping List 
	 */
	protected List stoppingList;
	
	/**
	 * List of Models.  Do not use IModels because it does not have a
	 * contains method.
	 */
	protected List models;

	/**
	 * Use the IRelationshipFilter when true, don't use it when false
	 */
	protected boolean useFilter;

	/**
	 * Constructor for ShowRelatedElementsRequest.
	 * @param shapes List of IShapeView objects that were selected
	 * @param relationshipTypesToShow  List of relationship type hints
	 * to show.  
	 * @param isExpandIndefinite true to expand indefinitely, false not to.
	 * @param expandLevel specify an int level here for the number of
	 * levels to expand if you set isExpandIndefinite to false.
	 * @param expansionType the ExpansionType for deciding which
	 * direction to expand in.
	 * @param useFilter use the IRelationshipFilter when true, 
	 * don't use it when false
	 * @param models List of IModel objects, if null, the default will
	 * be used.
	 * @param stoppingList List of the stopping conditions
	 * for related elements if this is true
	 */
	public ShowRelatedElementsRequest(
		List shapes,
		List relationshipTypesToShow,
		boolean isExpandIndefinite,
		int expandLevel,
		ExpansionType expansionType,
		boolean useFilter, 
		List models,
		List stoppingList) {

		super(RequestConstants.REQ_SHOW_RELATED_ELEMENTS);
		this.selectedShapes = new ArrayList(shapes);
		this.relatedShapes = new ArrayList();
		this.relationshipsToShow = relationshipTypesToShow;
		this.isExpandIndefinite = isExpandIndefinite;
		this.expandLevel = expandLevel;
		this.expansionType = expansionType;
		this.useFilter = useFilter;
		this.models = models;
		this.stoppingList = stoppingList;
	}

	/**
	 * Constructor for ShowRelatedElementsRequest.
	 * Filter is set to the default of true and the default list of models
	 * will be used.
	 * 
	 * @param shapes List of IShapeView objects that were selected
	 * @param relationshipTypesToShow  List of relationship type hints
	 * to show.  Use CoreUMLTypeInfo.
	 * @param isExpandIndefinite true to expand indefinitely, false not to.
	 * @param expandLevel specify an int level here for the number of
	 * levels to expand if you set isExpandIndefinite to false.
	 * @param expansionType should include incoming or outgoing  relationships when searching
	 * for related elements if this is true
	 */
	public ShowRelatedElementsRequest(
		List shapes,
		List relationshipTypesToShow,
		boolean isExpandIndefinite,
		int expandLevel,
		ExpansionType expansionType) {

		this(
			shapes,
			relationshipTypesToShow,
			isExpandIndefinite,
			expandLevel,
			expansionType,
			true,
			null,
			new ArrayList());
	}

	/**
	 * Method getShapes.
	 * @return List the list of <code>IAdaptable</code> shapes
	 */
	public List getSelectedShapes() {
		return this.selectedShapes;
	}

	/**
	 * Method getRelatedShapes.
	 * @return List the list of <code>IAdaptable</code> shapes
	 */
	public List getRelatedShapes() {
		return this.relatedShapes;
	}

	/**
	 * Method setRelatedShapes.
	 * @param relatedShapes List the list of <code>IAdaptable</code> shapes
	 */
	public void setRelatedShapes(List relatedShapes) {
		this.relatedShapes = relatedShapes;
	}

	/**
	 * Method getRelationshipsToShow.
	 * @return List
	 */
	public List getRelationshipsToShow() {
		return this.relationshipsToShow;
	}

	/**
	 * Method isExpandIndefinite.
	 * @return boolean
	 */
	public boolean isExpandIndefinite() {
		return this.isExpandIndefinite;
	}

	/**
	 * Method getExpandLevel.
	 * @return int
	 */
	public int getExpandLevel() {
		return this.expandLevel;
	}

	/**
	 * Method getUseIncoming.
	 * This will be deprecated when all SRE implementors implement the all
	 * connected option.  Use getExpansionType() instead. 
	 * 
	 * @return boolean
	 */
	public boolean getUseIncoming() {
		return expansionType.equals(ExpansionType.INCOMING) ||
			expansionType.equals(ExpansionType.BOTH);
	}

	/**
	 * Method getUseOutgoing.
	 * This will be deprecated when all SRE implementors implement the all
	 * connected option.  Use getExpansionType() instead.
	 * 
	 * @return boolean
	 */
	public boolean getUseOutgoing() {
		return expansionType.equals(ExpansionType.OUTGOING) ||
			expansionType.equals(ExpansionType.BOTH);
	}
	
	/**
	 * Return the expansion type that was passed into the constructor.
	 * 
	 * @return ExpansionType
	 */
	public ExpansionType getExpansionType() {
		return expansionType;
	}	

	/**
	 * Returns if the filter should be used or not
	 * Use the IRelationshipFilter when true, don't use it when false
	 * @return true if the IRelationshipFilter should be used, false if it
	 * shouldn't
	 */
	public boolean getUseFilter() {
		return this.useFilter;
	}

	/**
     * Return a List of IModel objects
	 * 
	 * @return List of IModel objects
	 */
	public List getModels() {
		return models;
	}

	/**
	 * Method getStoppingList
	 * @return stoppingList
	 */	
	public List getStoppingList() {
		return stoppingList;
	}
}
