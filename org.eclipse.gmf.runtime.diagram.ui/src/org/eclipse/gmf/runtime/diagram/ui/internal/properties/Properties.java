/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.properties;

import org.eclipse.gmf.runtime.emf.core.util.PackageUtil;
import org.eclipse.gmf.runtime.notation.NotationPackage;

/*
 * @canBeSeenBy %partners
 */
/**
 * constants for the Diagtam ui
 * @author sshae
 *
 */
public interface Properties {
	/**
	 * the persisted edges Structural featrue Id
	 */
	public static String ID_PERSISTED_EDGES = PackageUtil.getID(NotationPackage.eINSTANCE.getDiagram_PersistedEdges());
	/**
	 * the transient edges Structural featrue Id
	 */
	public static String ID_TRANSIENT_EDGES = PackageUtil.getID(NotationPackage.eINSTANCE.getDiagram_TransientEdges());

	/**
	 * the  visible property ID
	 */
	public static String ID_ISVISIBLE = PackageUtil.getID(NotationPackage.eINSTANCE.getView_Visible());
	
	/**
	 * the  persisted children property ID
	 */
	public static String ID_PERSISTED_CHILDREN = PackageUtil.getID(NotationPackage.eINSTANCE.getView_PersistedChildren());
	
	/**
	 * the  transient children property ID
	 */
	public static String ID_TRANSIENT_CHILDREN = PackageUtil.getID(NotationPackage.eINSTANCE.getView_TransientChildren());
	
	/**
	 * the  source connections  property ID
	 */
	public static String ID_SOURCECONNECTIONS = PackageUtil.getID(NotationPackage.eINSTANCE.getView_SourceEdges());
	
	/**
	 * the  target  connections  property ID
	 */
	public static String ID_TARGETCONNECTIONS = PackageUtil.getID(NotationPackage.eINSTANCE.getView_TargetEdges());

	/**
	 * the  bend point property ID
	 */
	public static String ID_BENDPOINT = PackageUtil.getID(NotationPackage.eINSTANCE.getRelativeBendpoints_Points());

	/**
	 * the  semantic hint  property ID
	 */
	public static String ID_SEMANTICREF = PackageUtil.getID(NotationPackage.eINSTANCE.getView_Element());

	/**
	 * the  font name   property ID
	 */
	public static String ID_FONTNAME = PackageUtil.getID(NotationPackage.eINSTANCE.getFontStyle_FontName());
	/**
	 * the  font size   property ID
	 */
	public static String ID_FONTSIZE = PackageUtil.getID(NotationPackage.eINSTANCE.getFontStyle_FontHeight());
	/**
	 * the  font bold   property ID
	 */
	public static String ID_FONTBOLD = PackageUtil.getID(NotationPackage.eINSTANCE.getFontStyle_Bold());
	/**
	 * the  font Italic  property ID
	 */
	public static String ID_FONTITALIC = PackageUtil.getID(NotationPackage.eINSTANCE.getFontStyle_Italic());
	/**
	 * the  font under line  property ID
	 */
	public static String ID_FONTUNDERLINE = PackageUtil.getID(NotationPackage.eINSTANCE.getFontStyle_Underline());
	/**
	 * the  font strike through  property ID
	 */
	public static String ID_FONTSTRIKETHROUGH = PackageUtil.getID(NotationPackage.eINSTANCE.getFontStyle_StrikeThrough());
	/**
	 * the  font color   property ID
	 */
	public static String ID_FONTCOLOR = PackageUtil.getID(NotationPackage.eINSTANCE.getFontStyle_FontColor());
	/**
	 * the  line color property ID
	 */
	public static String ID_LINECOLOR = PackageUtil.getID(NotationPackage.eINSTANCE.getLineStyle_LineColor());
	/**
	 * the  fill color property ID
	 */
	public static String ID_FILLCOLOR = PackageUtil.getID(NotationPackage.eINSTANCE.getFillStyle_FillColor());
	/**
	 * the  show compartment title  property ID
	 */
	public static String ID_SHOWCOMPARTMENTTITLE = PackageUtil.getID(NotationPackage.eINSTANCE.getTitleStyle_ShowTitle());
	/**
	 * the  collapsed property ID
	 */
	public static String ID_COLLAPSED = PackageUtil.getID(NotationPackage.eINSTANCE.getDrawerStyle_Collapsed());
	/**
	 * the  routing  property ID
	 */
	public static String ID_ROUTING = PackageUtil.getID(NotationPackage.eINSTANCE.getRoutingStyle_Routing());
	/**
	 * the  smoothness   property ID
	 */
	public static String ID_SMOOTHNESS = PackageUtil.getID(NotationPackage.eINSTANCE.getRoutingStyle_Smoothness());
	/**
	 * the  avoid obstructions  property ID
	 */
	public static String ID_AVOIDOBSTRUCTIONS = PackageUtil.getID(NotationPackage.eINSTANCE.getRoutingStyle_AvoidObstructions());
	/**
	 * the  closest distance   property ID
	 */
	public static String ID_CLOSESTDISTANCE = PackageUtil.getID(NotationPackage.eINSTANCE.getRoutingStyle_ClosestDistance());
	/**
	 * the  jump links status property ID
	 */
	public static String ID_JUMPLINKS_STATUS = PackageUtil.getID(NotationPackage.eINSTANCE.getRoutingStyle_JumpLinkStatus());
	/**
	 * the  jump links type property ID
	 */
	public static String ID_JUMPLINKS_TYPE = PackageUtil.getID(NotationPackage.eINSTANCE.getRoutingStyle_JumpLinkType());
	/**
	 * the  jump links reverse property ID
	 */
	public static String ID_JUMPLINKS_REVERSE = PackageUtil.getID(NotationPackage.eINSTANCE.getRoutingStyle_JumpLinksReverse());
	/**
	 * the  description   property ID
	 */
	public static String ID_DESCRIPTION = PackageUtil.getID(NotationPackage.eINSTANCE.getDescriptionStyle_Description());
	/**
	 * the is canonical   property ID
	 */
	public static String ID_ISCANONICAL = PackageUtil.getID(NotationPackage.eINSTANCE.getCanonicalStyle_Canonical());
	/**
	 * the  sorting  property ID
	 */
	public static String ID_SORTING = PackageUtil.getID(NotationPackage.eINSTANCE.getSortingStyle_Sorting());
	/**
	 * the  sorting keys   property ID
	 */
	public static String ID_SORTING_KEYS = PackageUtil.getID(NotationPackage.eINSTANCE.getSortingStyle_SortingKeys());
	/**
	 * the  sorted objects property ID
	 */
	public static String ID_SORTED_OBJECTS = PackageUtil.getID(NotationPackage.eINSTANCE.getSortingStyle_SortedObjects());
	/**
	 * the  filtering  property ID
	 */
	public static String ID_FILTERING = PackageUtil.getID(NotationPackage.eINSTANCE.getFilteringStyle_Filtering());
	/**
	 * the  filtering keys   property ID
	 */
	public static String ID_FILTERING_KEYS = PackageUtil.getID(NotationPackage.eINSTANCE.getFilteringStyle_FilteringKeys());
	/**
	 * the  filtered objects   property ID
	 */
	public static String ID_FILTERED_OBJECTS = PackageUtil.getID(NotationPackage.eINSTANCE.getFilteringStyle_FilteredObjects());

	/**
	 * the  page x   property ID
	 */
	public static String ID_PAGEX = PackageUtil.getID(NotationPackage.eINSTANCE.getPageStyle_PageX());
	/**
	 * the  page y  property ID
	 */
	public static String ID_PAGEY = PackageUtil.getID(NotationPackage.eINSTANCE.getPageStyle_PageY());
	/**
	 * the  page width   property ID
	 */
	public static String ID_PAGE_WIDTH = PackageUtil.getID(NotationPackage.eINSTANCE.getPageStyle_PageWidth());
	/**
	 * the  page height   property ID
	 */
	public static String ID_PAGE_HEIGHT = PackageUtil.getID(NotationPackage.eINSTANCE.getPageStyle_PageHeight());
	
	/**
	 * the  extent x   property ID
	 */
	public static String ID_EXTENTX = PackageUtil.getID(NotationPackage.eINSTANCE.getSize_Width());
	/**
	 * the  extent y   property ID
	 */
	public static String ID_EXTENTY = PackageUtil.getID(NotationPackage.eINSTANCE.getSize_Height());
	/**
	 * the  position x   property ID
	 */
	public static String ID_POSITIONX = PackageUtil.getID(NotationPackage.eINSTANCE.getLocation_X());
	/**
	 * the  position y   property ID
	 */
	public static String ID_POSITIONY = PackageUtil.getID(NotationPackage.eINSTANCE.getLocation_Y());
	/**
	 * the  ratio   property ID
	 */
	public static String ID_RATIO = PackageUtil.getID(NotationPackage.eINSTANCE.getRatio_Value());

	/**
	 * the diagram link annotation source
	 */
	public static String DIAGRAMLINK_ANNOTATION = "DiagramLink"; //$NON-NLS-1$
}
