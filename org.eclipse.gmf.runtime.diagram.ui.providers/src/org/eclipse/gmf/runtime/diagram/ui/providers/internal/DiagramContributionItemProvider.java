/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.providers.internal;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gmf.runtime.common.ui.action.actions.global.GlobalActionManager;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalActionId;
import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.AbstractContributionItemProvider;
import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.ActionRegistry;
import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;
import org.eclipse.gmf.runtime.diagram.core.util.ViewType;
import org.eclipse.gmf.runtime.diagram.ui.actions.AddNoteAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.AlignAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.ShowPropertiesViewAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.AddMenuManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.AlignMenuManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.AllCompartmentsAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.ArrangeAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.ArrangeMenuManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.AutoSizeAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.ColorPropertyContributionItem;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.CompartmentMenuManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.ConnectionLabelMenuManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.CopyAppearancePropertiesAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.CreateShapeViewAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.CreateViewAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.DeleteFromDiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.EditMenuManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.FileMenuManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.FiltersMenuManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.FontDialogAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.FontNameContributionItem;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.FontSizeContributionItem;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.FontStyleAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.HideConnectionLabelsAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.MakeSameSizeMenuManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.OpenWithMenuManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.RecalculatePageBreaksAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.RouterAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.RouterMenuManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.SelectAllAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.SelectMenuManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.ShowCompartmentTitleAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.ShowConnectionLabelsAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.ShowInMenuManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.SizeBothAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.SizeHeightAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.SizeWidthAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.SnapBackAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.SnapToGridAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.SortFilterAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.ViewGridAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.ViewMenuManager;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.ViewPageBreaksAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.ViewRulersAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.ZOrderAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.ZOrderMenuManager;
import org.eclipse.gmf.runtime.diagram.ui.internal.actions.DeleteFromModelAction;
import org.eclipse.gmf.runtime.diagram.ui.internal.actions.OpenAction;
import org.eclipse.gmf.runtime.diagram.ui.internal.actions.ZoomContributionItem;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.providers.internal.l10n.DiagramProvidersResourceManager;
import org.eclipse.gmf.runtime.emf.core.edit.MObjectType;
import org.eclipse.gmf.runtime.emf.core.util.EObjectContainmentUtil;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.actions.ActionFactory;

/**
 * @author melaasar
 * @canBeSeenBy %level1
 * 
 * The core diagram contribution item provider.
 * It provides contributions generic to all diagram editors
 * 
 */
public class DiagramContributionItemProvider
	extends AbstractContributionItemProvider {

	protected IContributionItem createCustomContributionItem(
		String customId,
		IWorkbenchPartDescriptor partDescriptor) {

		IWorkbenchPage workbenchPage = partDescriptor.getPartPage();

		if (customId.equals(ActionIds.CUSTOM_FONT_NAME))
			return new FontNameContributionItem(workbenchPage);
		if (customId.equals(ActionIds.CUSTOM_FONT_SIZE))
			return new FontSizeContributionItem(workbenchPage);
		if (customId.equals(ActionIds.CUSTOM_FONT_COLOR))
			return ColorPropertyContributionItem
				.createFontColorContributionItem(
				workbenchPage);
		if (customId.equals(ActionIds.CUSTOM_LINE_COLOR))
			return ColorPropertyContributionItem
				.createLineColorContributionItem(
				workbenchPage);
		if (customId.equals(ActionIds.CUSTOM_FILL_COLOR))
			return ColorPropertyContributionItem
				.createFillColorContributionItem(
				workbenchPage);
		if (customId.equals(ActionIds.CUSTOM_ZOOM))
			return new ZoomContributionItem(workbenchPage);

		return super.createCustomContributionItem(customId, partDescriptor);
	}

	protected IMenuManager createMenuManager(
		String menuId,
		IWorkbenchPartDescriptor partDescriptor) {

		if (menuId.equals(ActionIds.MENU_DIAGRAM))
			return new MenuManager(DiagramProvidersResourceManager.getI18NString("DiagramMainMenu.DiagramMainMenuText"), ActionIds.MENU_DIAGRAM); //$NON-NLS-1$
		if (menuId.equals(ActionIds.MENU_DIAGRAM_ADD))
			return new AddMenuManager(menuId); 
		if (menuId.equals(ActionIds.MENU_NAVIGATE))
			// Should use a custom ActionMenuManager, but some actions are
			// contributed using the org.eclipse.ui.popupMenus causing problems.
			return new MenuManager(DiagramProvidersResourceManager.getI18NString("NavigateMenuManager.Navigate.ActionLabelText"), ActionIds.MENU_NAVIGATE); //$NON-NLS-1$
		if (menuId.equals(ActionIds.MENU_FILE))
			return new FileMenuManager();
		if (menuId.equals(ActionIds.MENU_EDIT))
			return new EditMenuManager();
		if (menuId.equals(ActionIds.MENU_FILTERS))
			return new FiltersMenuManager();
		if (menuId.equals(ActionIds.MENU_VIEW))
			return new ViewMenuManager();
		if (menuId.equals(ActionIds.MENU_FORMAT))
			// Should use a custom ActionMenuManager, but the color menu
			// is causing "out of index" problems.  Will investigate.			
			return new MenuManager(DiagramProvidersResourceManager.getI18NString("FormatMenuManager.Format.ActionLabelText"), ActionIds.MENU_FORMAT); //$NON-NLS-1$
		if (menuId.equals(ActionIds.MENU_ARRANGE))
			return new ArrangeMenuManager();
		if (menuId.equals(ActionIds.MENU_SELECT))
			return new SelectMenuManager();
		if (menuId.equals(ActionIds.MENU_ALIGN))
			return new AlignMenuManager();
		if (menuId.equals(ActionIds.MENU_COMPARTMENT))
			return new CompartmentMenuManager();
		if (menuId.equals(ActionIds.MENU_CONNECTION_LABEL))
			return new ConnectionLabelMenuManager();
		if (menuId.equals(ActionIds.MENU_ROUTER))
			return new RouterMenuManager();
		if (menuId.equals(ActionIds.MENU_SHOW_IN))
			return new ShowInMenuManager();
		if (menuId.equals(ActionIds.MENU_OPEN_WITH))
			return new OpenWithMenuManager();
		if (menuId.equals(ActionIds.MENU_ZORDER))
			return new ZOrderMenuManager();
		if (menuId.equals(ActionIds.MENU_MAKE_SAME_SIZE))
			return new MakeSameSizeMenuManager();

		return super.createMenuManager(menuId, partDescriptor);
	}

	protected IAction createAction(
		String actionId,
		IWorkbenchPartDescriptor partDescriptor) {

		IWorkbenchPage workbenchPage = partDescriptor.getPartPage();

		if (actionId.equals(ActionIds.ACTION_FONT_BOLD))
			return FontStyleAction.createBoldFontStyleAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_FONT_ITALIC))
			return FontStyleAction.createItalicFontStyleAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_ARRANGE_ALL))
			return ArrangeAction.createArrangeAllAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_ARRANGE_SELECTION))
			return ArrangeAction.createArrangeSelectionAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_TOOLBAR_ARRANGE_ALL))
			return ArrangeAction.createToolbarArrangeAllAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_TOOLBAR_ARRANGE_SELECTION))
			return ArrangeAction.createToolbarArrangeSelectionAction(workbenchPage);
		if (actionId.equals(ActionFactory.SELECT_ALL.getId()))
			return SelectAllAction.createSelectAllAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_SELECT_ALL_SHAPES))
			return SelectAllAction.createSelectAllShapesAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_SELECT_ALL_CONNECTIONS))
			return SelectAllAction.createSelectAllConnectionsAction(
				workbenchPage);		
		if (actionId.equals(ActionIds.ACTION_TOOLBAR_SELECT_ALL))
			return SelectAllAction.createToolbarSelectAllAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_TOOLBAR_SELECT_ALL_SHAPES))
			return SelectAllAction.createToolbarSelectAllShapesAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_TOOLBAR_SELECT_ALL_CONNECTIONS))
			return SelectAllAction.createToolbarSelectAllConnectionsAction(
				workbenchPage);		
		if (actionId.equals(GEFActionConstants.ALIGN_LEFT))
			return new AlignAction(workbenchPage, actionId, PositionConstants.LEFT);
		if (actionId.equals(GEFActionConstants.ALIGN_CENTER))
			return new AlignAction(workbenchPage, actionId, PositionConstants.CENTER);
		if (actionId.equals(GEFActionConstants.ALIGN_RIGHT))
			return new AlignAction(workbenchPage, actionId, PositionConstants.RIGHT);
		if (actionId.equals(GEFActionConstants.ALIGN_TOP))
			return new AlignAction(workbenchPage, actionId, PositionConstants.TOP);
		if (actionId.equals(GEFActionConstants.ALIGN_MIDDLE))
			return new AlignAction(workbenchPage, actionId, PositionConstants.MIDDLE);
		if (actionId.equals(GEFActionConstants.ALIGN_BOTTOM))
			return new AlignAction(workbenchPage, actionId, PositionConstants.BOTTOM);		
		if (actionId.equals(ActionIds.ACTION_ALIGN_LEFT))
			return new AlignAction(workbenchPage, actionId, PositionConstants.LEFT, false);
		if (actionId.equals(ActionIds.ACTION_ALIGN_CENTER))
			return new AlignAction(workbenchPage, actionId, PositionConstants.CENTER, false);
		if (actionId.equals(ActionIds.ACTION_ALIGN_RIGHT))
			return new AlignAction(workbenchPage, actionId, PositionConstants.RIGHT, false);
		if (actionId.equals(ActionIds.ACTION_ALIGN_TOP))
			return new AlignAction(workbenchPage, actionId, PositionConstants.TOP, false);
		if (actionId.equals(ActionIds.ACTION_ALIGN_MIDDLE))
			return new AlignAction(workbenchPage, actionId, PositionConstants.MIDDLE, false);
		if (actionId.equals(ActionIds.ACTION_ALIGN_BOTTOM))
			return new AlignAction(workbenchPage, actionId, PositionConstants.BOTTOM, false);		
		
		if (actionId.equals(ActionIds.ACTION_AUTOSIZE))
			return new AutoSizeAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_COMPARTMENT_ALL))
			return AllCompartmentsAction.createShowAllCompartmentsAction(
				workbenchPage);
		if (actionId.equals(ActionIds.ACTION_COMPARTMENT_NONE))
			return AllCompartmentsAction.createHideAllCompartmentsAction(
				workbenchPage);
		if (actionId.equals(ActionIds.ACTION_ROUTER_RECTILINEAR))
			return RouterAction.createRectilinearRouterAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_ROUTER_OBLIQUE))
			return RouterAction.createObliqueRouterAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_ROUTER_TREE))
			return RouterAction.createTreeRouterAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_COPY_APPEARANCE_PROPERTIES))
			return new CopyAppearancePropertiesAction(workbenchPage);
		if (actionId.equals(ActionFactory.UNDO.getId()))
			return GlobalActionManager.getInstance().createActionHandler(
				workbenchPage,
				GlobalActionId.UNDO);
		if (actionId.equals(ActionFactory.REDO.getId()))
			return GlobalActionManager.getInstance().createActionHandler(
				workbenchPage,
				GlobalActionId.REDO);
		if (actionId.equals(ActionFactory.DELETE.getId()))
			return GlobalActionManager.getInstance().createActionHandler(
				workbenchPage,
				GlobalActionId.DELETE);
		if (actionId.equals(ActionFactory.PROPERTIES.getId()))
			return GlobalActionManager.getInstance().createActionHandler(
				workbenchPage,
				GlobalActionId.PROPERTIES);
		if (actionId.equals(ActionFactory.CUT.getId()))
			return GlobalActionManager.getInstance().createActionHandler(
				workbenchPage,
				GlobalActionId.CUT);
		if (actionId.equals(ActionFactory.COPY.getId()))
			return GlobalActionManager.getInstance().createActionHandler(
				workbenchPage,
				GlobalActionId.COPY);
		if (actionId.equals(ActionFactory.PASTE.getId()))
			return GlobalActionManager.getInstance().createActionHandler(
				workbenchPage,
				GlobalActionId.PASTE);
		if (actionId.equals("open")) //$NON-NLS-1$
			return GlobalActionManager.getInstance().createActionHandler(
				workbenchPage,
				GlobalActionId.OPEN);
		if (actionId.equals(ActionFactory.FIND.getId()))
			return GlobalActionManager.getInstance().createActionHandler(
				workbenchPage,
				GlobalActionId.FIND);
		if (actionId.equals(ActionFactory.PRINT.getId()))
			return GlobalActionManager.getInstance().createActionHandler(
				workbenchPage,
				GlobalActionId.PRINT);
		if (actionId.equals(ActionFactory.SAVE.getId()))
			return GlobalActionManager.getInstance().createActionHandler(
				workbenchPage,
				GlobalActionId.SAVE);
		if (actionId.equals(ActionIds.ACTION_FONT_DIALOG))
			return new FontDialogAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_DELETE_FROM_MODEL)){
			Object theObj = getSelectedObject(partDescriptor);
			if (!EObjectContainmentUtil.hasMObjectType(theObj, MObjectType.MODELING)){
				return null;
			}
			return new DeleteFromModelAction(workbenchPage);
		}	
		if (actionId.equals(ActionIds.ACTION_DELETE_FROM_DIAGRAM)){	
			return new DeleteFromDiagramAction(workbenchPage);
		}	
		if (actionId.equals(ActionIds.ACTION_RECALC_PAGEBREAKS))
			return new RecalculatePageBreaksAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_SORT_FILTER))
			return new SortFilterAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_SNAP_BACK))
			return new SnapBackAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_SHOW_PROPERTIES_VIEW))
			return new ShowPropertiesViewAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_ADD_NOTE))
			return new CreateShapeViewAction(workbenchPage, ActionIds.ACTION_ADD_NOTE, ViewType.NOTE, DiagramProvidersResourceManager.getI18NString("Note.menuItem"), //$NON-NLS-1$
				DiagramProvidersResourceManager.getInstance().getImageDescriptor(
					DiagramProvidersResourceManager.DESC_NOTE));
		if (actionId.equals(ActionIds.ACTION_ADD_TEXT))
			return new CreateViewAction(workbenchPage, ActionIds.ACTION_ADD_TEXT, ViewType.TEXT, DiagramProvidersResourceManager.getI18NString("Text.menuItem"), //$NON-NLS-1$
				DiagramProvidersResourceManager.getInstance().getImageDescriptor(
					DiagramProvidersResourceManager.DESC_TEXT));
		if (actionId.equals(ActionIds.ACTION_ADD_NOTELINK))
			return new AddNoteAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_SHOW_CONNECTION_LABELS))
			return new ShowConnectionLabelsAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_HIDE_CONNECTION_LABELS))
			return new HideConnectionLabelsAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_SHOW_COMPARTMENT_TITLE))
			return new ShowCompartmentTitleAction(workbenchPage);

		// Handle Z-Order Actions
		if (actionId.equals(ActionIds.ACTION_BRING_TO_FRONT))
			return ZOrderAction.createBringToFrontAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_SEND_TO_BACK))
			return ZOrderAction.createSendToBackAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_BRING_FORWARD))
			return ZOrderAction.createBringForwardAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_SEND_BACKWARD))
			return ZOrderAction.createSendBackwardAction(workbenchPage);
	
		// Handle View Actions
		if (actionId.equals(ActionIds.ACTION_VIEW_GRID))
			return new ViewGridAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_SNAP_TO_GRID))
			return new SnapToGridAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_VIEW_PAGEBREAKS))
			return new ViewPageBreaksAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_VIEW_RULERS))
			return new ViewRulersAction(workbenchPage);
		
		// Handle Make Same Size Actions
		if (actionId.equals(ActionIds.ACTION_MAKE_SAME_SIZE_BOTH))
			return new SizeBothAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_MAKE_SAME_SIZE_HEIGHT))
			return new SizeHeightAction(workbenchPage);
		if (actionId.equals(ActionIds.ACTION_MAKE_SAME_SIZE_WIDTH))
			return new SizeWidthAction(workbenchPage);
		if (actionId.equals(ActionIds.OPEN))
			return new OpenAction(workbenchPage);

		return super.createAction(actionId, partDescriptor);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.AbstractContributionItemProvider#getActionFromRegistry(java.lang.String, org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor, org.eclipse.gmf.runtime.common.ui.util.ActionRegistry)
	 */
	protected IAction getActionFromRegistry(String actionId, IWorkbenchPartDescriptor partDescriptor, ActionRegistry registry){
		
		//TODO:
		//Added in order to have the delete from model not show up for visualizer objects.
		//Once contribution item service allows for static method calls in xml, this can
		//be removed.
		if (actionId.equals(ActionIds.ACTION_DELETE_FROM_MODEL)){
			Object theObj = getSelectedObject(partDescriptor);
			if (!EObjectContainmentUtil.hasMObjectType(theObj, MObjectType.MODELING)){
				return null;
			}
		}		
		return super.getActionFromRegistry(actionId, partDescriptor, registry);
	}
	
}
