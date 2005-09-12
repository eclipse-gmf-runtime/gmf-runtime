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

package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.providers;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.CreateConnectorViewAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.CreateShapeViewAction;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.l10n.GeoshapesResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.providers.internal.PresentationContributionItemProvider;

/**
 * @author jschofie
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.geoshapes.*
 *
 * Creates the actions that can be performed on the various Geometric Shapes
 */
public class GeoshapeContributionItemProvider extends PresentationContributionItemProvider {

	/**
	 * The constructor
	 */
	public GeoshapeContributionItemProvider() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.rational.xtools.common.ui.services.contributionitem.AbstractContributionItemProvider#createAction(java.lang.String, com.rational.xtools.common.ui.util.IWorkbenchPartDescriptor)
	 */
	protected IAction createAction( String actionId, IWorkbenchPartDescriptor partDescriptor ) {

		IWorkbenchPage workbenchPage = partDescriptor.getPartPage();

		if( actionId.equals(ActionIds.ACTION_ADD_LINE) ) {
			return new CreateConnectorViewAction(workbenchPage, 
					ActionIds.ACTION_ADD_LINE, 
					GeoshapeConstants.TOOL_LINE,
					GeoshapesResourceManager.getInstance().getString("geoshape.LineTool.Label"), //$NON-NLS-1$
					GeoshapesResourceManager.getInstance().getImageDescriptor("IconLine.gif")); //$NON-NLS-1$);			
			
		}

		if( actionId.equals(ActionIds.ACTION_ADD_OVAL) ) {
			return new CreateShapeViewAction(workbenchPage, 
					ActionIds.ACTION_ADD_OVAL, 
					GeoshapeConstants.TOOL_OVAL,
					GeoshapesResourceManager.getInstance().getString("geoshape.OvalTool.Label"), //$NON-NLS-1$
					GeoshapesResourceManager.getInstance().getImageDescriptor("IconEllipse.gif")); //$NON-NLS-1$);			
		}

		if( actionId.equals(ActionIds.ACTION_ADD_TRIANGLE) ) {
			return new CreateShapeViewAction(workbenchPage, 
					ActionIds.ACTION_ADD_TRIANGLE, 
					GeoshapeConstants.TOOL_TRIANGLE,
					GeoshapesResourceManager.getInstance().getString("geoshape.TriangleTool.Label"), //$NON-NLS-1$
					GeoshapesResourceManager.getInstance().getImageDescriptor("IconTriangle.gif")); //$NON-NLS-1$);			
		}

		if( actionId.equals(ActionIds.ACTION_ADD_RECTANGLE) ) {
			return new CreateShapeViewAction(workbenchPage, 
					ActionIds.ACTION_ADD_RECTANGLE, 
					GeoshapeConstants.TOOL_RECTANGLE,
					GeoshapesResourceManager.getInstance().getString("geoshape.RectangleTool.Label"), //$NON-NLS-1$
					GeoshapesResourceManager.getInstance().getImageDescriptor("IconRectangle.gif")); //$NON-NLS-1$);			
		}
		
		if( actionId.equals(ActionIds.ACTION_ADD_SHADOWRECTANGLE) ) {
			return new CreateShapeViewAction(workbenchPage, 
					ActionIds.ACTION_ADD_SHADOWRECTANGLE, 
					GeoshapeConstants.TOOL_SHADOWRECTANGLE,
					GeoshapesResourceManager.getInstance().getString("geoshape.ShadowRectangleTool.Label"), //$NON-NLS-1$
					GeoshapesResourceManager.getInstance().getImageDescriptor("IconShadowRectangle.gif")); //$NON-NLS-1$);			
		}

		if( actionId.equals(ActionIds.ACTION_ADD_3DRECTANGLE) ) {
			return new CreateShapeViewAction(workbenchPage, 
					ActionIds.ACTION_ADD_3DRECTANGLE, 
					GeoshapeConstants.TOOL_3DRECTANGLE,
					GeoshapesResourceManager.getInstance().getString("geoshape.3DRectangleTool.Label"), //$NON-NLS-1$
					GeoshapesResourceManager.getInstance().getImageDescriptor("Icon3DRectangle.gif")); //$NON-NLS-1$);			
		}

		if( actionId.equals(ActionIds.ACTION_ADD_ROUNDRECTANGLE) ) {
			return new CreateShapeViewAction(workbenchPage, 
					ActionIds.ACTION_ADD_ROUNDRECTANGLE, 
					GeoshapeConstants.TOOL_ROUNDRECTANGLE,
					GeoshapesResourceManager.getInstance().getString("geoshape.RoundRectangleTool.Label"), //$NON-NLS-1$
					GeoshapesResourceManager.getInstance().getImageDescriptor("IconRoundRectangle.gif")); //$NON-NLS-1$);			
		}

		if( actionId.equals(ActionIds.ACTION_ADD_HEXAGON) ) {
			return new CreateShapeViewAction(workbenchPage, 
					ActionIds.ACTION_ADD_HEXAGON, 
					GeoshapeConstants.TOOL_HEXAGON,
					GeoshapesResourceManager.getInstance().getString("geoshape.HexagonTool.Label"), //$NON-NLS-1$
					GeoshapesResourceManager.getInstance().getImageDescriptor("IconHexagon.gif")); //$NON-NLS-1$);			
		}

		if( actionId.equals(ActionIds.ACTION_ADD_OCTAGON) ) {
			return new CreateShapeViewAction(workbenchPage, 
					ActionIds.ACTION_ADD_OCTAGON, 
					GeoshapeConstants.TOOL_OCTAGON,
					GeoshapesResourceManager.getInstance().getString("geoshape.OctagonTool.Label"), //$NON-NLS-1$
					GeoshapesResourceManager.getInstance().getImageDescriptor("IconOctagon.gif")); //$NON-NLS-1$);			
		}

		if( actionId.equals(ActionIds.ACTION_ADD_PENTAGON) ) {
			return new CreateShapeViewAction(workbenchPage, 
					ActionIds.ACTION_ADD_PENTAGON, 
					GeoshapeConstants.TOOL_PENTAGON,
					GeoshapesResourceManager.getInstance().getString("geoshape.PentagonTool.Label"), //$NON-NLS-1$
					GeoshapesResourceManager.getInstance().getImageDescriptor("IconPentagon.gif")); //$NON-NLS-1$);			
		}

		if( actionId.equals(ActionIds.ACTION_ADD_DIAMOND) ) {
		return new CreateShapeViewAction(workbenchPage, 
					ActionIds.ACTION_ADD_DIAMOND,
					GeoshapeConstants.TOOL_DIAMOND,
					GeoshapesResourceManager.getInstance().getString("geoshape.DiamondTool.Label"), //$NON-NLS-1$
					GeoshapesResourceManager.getInstance().getImageDescriptor("IconDiamond.gif")); //$NON-NLS-1$);			
		}

		if( actionId.equals(ActionIds.ACTION_ADD_CYLINDER) ) {
			return new CreateShapeViewAction(workbenchPage, 
					ActionIds.ACTION_ADD_CYLINDER, 
					GeoshapeConstants.TOOL_CYLINDER,
					GeoshapesResourceManager.getInstance().getString("geoshape.CylinderTool.Label"), //$NON-NLS-1$
					GeoshapesResourceManager.getInstance().getImageDescriptor("IconCylinder.gif")); //$NON-NLS-1$);			
		}

		return super.createAction(actionId, partDescriptor);
	}
	
}
