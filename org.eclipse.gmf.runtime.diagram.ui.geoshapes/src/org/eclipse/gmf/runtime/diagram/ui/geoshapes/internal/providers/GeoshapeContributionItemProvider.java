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

import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.CreateConnectionViewAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.CreateShapeViewAction;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.l10n.DiagramUIGeoshapesMessages;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.l10n.DiagramUIGeoshapesPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.providers.internal.DiagramContributionItemProvider;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchPage;

/**
 * @author jschofie
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.geoshapes.*
 * 
 * Creates the actions that can be performed on the various Geometric Shapes
 */
public class GeoshapeContributionItemProvider
	extends DiagramContributionItemProvider {

	/**
	 * The constructor
	 */
	public GeoshapeContributionItemProvider() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.AbstractContributionItemProvider#createAction(java.lang.String,
	 *      org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor)
	 */
	protected IAction createAction(String actionId,
			IWorkbenchPartDescriptor partDescriptor) {

		IWorkbenchPage workbenchPage = partDescriptor.getPartPage();

		if (actionId.equals(ActionIds.ACTION_ADD_LINE)) {
			return new CreateConnectionViewAction(workbenchPage,
				ActionIds.ACTION_ADD_LINE, GeoshapeConstants.TOOL_LINE,
				DiagramUIGeoshapesMessages.geoshape_LineTool_Label,
				DiagramUIGeoshapesPluginImages.DESC_LINE);

		}

		if (actionId.equals(ActionIds.ACTION_ADD_OVAL)) {
			return new CreateShapeViewAction(workbenchPage,
				ActionIds.ACTION_ADD_OVAL, GeoshapeConstants.TOOL_OVAL,
				DiagramUIGeoshapesMessages.geoshape_OvalTool_Label,
				DiagramUIGeoshapesPluginImages.DESC_OVAL);
		}

		if (actionId.equals(ActionIds.ACTION_ADD_TRIANGLE)) {
			return new CreateShapeViewAction(workbenchPage,
				ActionIds.ACTION_ADD_TRIANGLE, GeoshapeConstants.TOOL_TRIANGLE,
				DiagramUIGeoshapesMessages.geoshape_TriangleTool_Label,
				DiagramUIGeoshapesPluginImages.DESC_TRIANGLE);
		}

		if (actionId.equals(ActionIds.ACTION_ADD_RECTANGLE)) {
			return new CreateShapeViewAction(workbenchPage,
				ActionIds.ACTION_ADD_RECTANGLE,
				GeoshapeConstants.TOOL_RECTANGLE,
				DiagramUIGeoshapesMessages.geoshape_RectangleTool_Label,
				DiagramUIGeoshapesPluginImages.DESC_RECTANGLE);
		}

		if (actionId.equals(ActionIds.ACTION_ADD_SHADOWRECTANGLE)) {
			return new CreateShapeViewAction(workbenchPage,
				ActionIds.ACTION_ADD_SHADOWRECTANGLE,
				GeoshapeConstants.TOOL_SHADOWRECTANGLE,
				DiagramUIGeoshapesMessages.geoshape_ShadowRectangleTool_Label,
				DiagramUIGeoshapesPluginImages.DESC_SHADOWRECTANGLE);
		}

		if (actionId.equals(ActionIds.ACTION_ADD_3DRECTANGLE)) {
			return new CreateShapeViewAction(workbenchPage,
				ActionIds.ACTION_ADD_3DRECTANGLE,
				GeoshapeConstants.TOOL_3DRECTANGLE,
				DiagramUIGeoshapesMessages.geoshape_3DRectangleTool_Label,
				DiagramUIGeoshapesPluginImages.DESC_3DRECTANGLE);
		}

		if (actionId.equals(ActionIds.ACTION_ADD_ROUNDRECTANGLE)) {
			return new CreateShapeViewAction(workbenchPage,
				ActionIds.ACTION_ADD_ROUNDRECTANGLE,
				GeoshapeConstants.TOOL_ROUNDRECTANGLE,
				DiagramUIGeoshapesMessages.geoshape_RoundRectangleTool_Label,
				DiagramUIGeoshapesPluginImages.DESC_ROUNDRECTANGLE);
		}

		if (actionId.equals(ActionIds.ACTION_ADD_HEXAGON)) {
			return new CreateShapeViewAction(workbenchPage,
				ActionIds.ACTION_ADD_HEXAGON, GeoshapeConstants.TOOL_HEXAGON,
				DiagramUIGeoshapesMessages.geoshape_HexagonTool_Label,
				DiagramUIGeoshapesPluginImages.DESC_HEXAGON);
		}

		if (actionId.equals(ActionIds.ACTION_ADD_OCTAGON)) {
			return new CreateShapeViewAction(workbenchPage,
				ActionIds.ACTION_ADD_OCTAGON, GeoshapeConstants.TOOL_OCTAGON,
				DiagramUIGeoshapesMessages.geoshape_OctagonTool_Label,
				DiagramUIGeoshapesPluginImages.DESC_OCTAGON);
		}

		if (actionId.equals(ActionIds.ACTION_ADD_PENTAGON)) {
			return new CreateShapeViewAction(workbenchPage,
				ActionIds.ACTION_ADD_PENTAGON, GeoshapeConstants.TOOL_PENTAGON,
				DiagramUIGeoshapesMessages.geoshape_PentagonTool_Label,
				DiagramUIGeoshapesPluginImages.DESC_PENTAGON);
		}

		if (actionId.equals(ActionIds.ACTION_ADD_DIAMOND)) {
			return new CreateShapeViewAction(workbenchPage,
				ActionIds.ACTION_ADD_DIAMOND, GeoshapeConstants.TOOL_DIAMOND,
				DiagramUIGeoshapesMessages.geoshape_DiamondTool_Label,
				DiagramUIGeoshapesPluginImages.DESC_DIAMOND);
		}

		if (actionId.equals(ActionIds.ACTION_ADD_CYLINDER)) {
			return new CreateShapeViewAction(workbenchPage,
				ActionIds.ACTION_ADD_CYLINDER, GeoshapeConstants.TOOL_CYLINDER,
				DiagramUIGeoshapesMessages.geoshape_CylinderTool_Label,
				DiagramUIGeoshapesPluginImages.DESC_CYLINDER);
		}

		return super.createAction(actionId, partDescriptor);
	}

}
