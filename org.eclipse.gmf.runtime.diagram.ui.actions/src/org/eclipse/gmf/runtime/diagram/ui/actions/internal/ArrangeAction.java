/******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.tools.ToolUtilities;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.editparts.CompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GroupEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IEditableEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.requests.ArrangeRequest;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchPage;

/**
 * The Arrange Action: arranges a container editpart or a set of selected editparts
 * 
 * @author melaasar
 * @canBeSeenBy %level1
 */
public class ArrangeAction extends DiagramAction {

    private boolean selectionOnly;

    /**
     * @param workbenchPage
     */
    protected ArrangeAction(
        IWorkbenchPage workbenchPage,
        boolean selectionOnly) {
        super(workbenchPage);
        this.selectionOnly = selectionOnly;
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#createTargetRequest()
     */
    protected Request createTargetRequest() {
        return new ArrangeRequest(getId());
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#updateTargetRequest()
     */
    protected void updateTargetRequest() {
        ArrangeRequest request = (ArrangeRequest) getTargetRequest();
        request.setPartsToArrange(getOperationSet());
    }

    private boolean isArrangeAll() {
        return !selectionOnly;
    }

    protected Command getCommand() {
        List operationSet = getOperationSet();
        if (operationSet.isEmpty()) {
            return null;
        }
        CompoundCommand arrangeCC = new CompoundCommand(getLabel());
        if (isArrangeAll()) {
            for (Iterator iter = operationSet.iterator(); iter.hasNext();) {
                EditPart element = (EditPart) iter.next();
                Command cmd = element.getCommand(getTargetRequest());
                if (cmd != null)
                    arrangeCC.add(cmd);
            }
        } else {
            EditPart targetEP = getTargetEditPartForArrangeSelection(operationSet);
            if (targetEP != null) {
                Command cmd = targetEP.getCommand(getTargetRequest());
                if (cmd != null)
                    arrangeCC.add(cmd);
            }
        }
        return arrangeCC;
    }
    
    private EditPart getTargetEditPartForArrangeSelection(List editparts) {

        if (editparts.size() == 1) {
            
            // If there is only one editpart selected, then the Arrange
            // Selected request gets sent to this editpart's target editpart to
            // allow clients to do as they wish.
            return ((EditPart) editparts.get(0))
                .getTargetEditPart(getTargetRequest());
            
        } else {
            
            // If there is more than one editpart selected, then the Arrange
            // Selected request gets sent to the common parent.
            EditPart parentEP = getSelectionParent(editparts);
            if (parentEP == null)
                return null;

            for (int i = 1; i < editparts.size(); i++) {
                EditPart part = (EditPart) editparts.get(i);
                if (part instanceof ConnectionEditPart) {
                    continue;
                }
                // if there is no common parent, then Arrange Selected isn't
                // supported.
                if (part.getParent() != parentEP)
                    return null;
            }
            return parentEP;
        }
    }

    /* 
     * The operation set is the shapes, connections or both on the diagrm edit part
     * (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#createOperationSet()
     */
    protected List createOperationSet() {
        List selection = getSelectedObjects();

        if (isArrangeAll()) {

            if (!selection.isEmpty()) {
                return createOperationSetForArrangeAll(selection);
            }
            if (getDiagramEditPart() != null) {
                return getDiagramEditPart().getChildren();
            }
            return Collections.EMPTY_LIST;

        } else {

            // this is the case of Arrange Selection
            if (selection.isEmpty()
                || !(selection.get(0) instanceof IGraphicalEditPart))
                return Collections.EMPTY_LIST;

            selection = ToolUtilities.getSelectionWithoutDependants(selection);
            return selection;
        }
    }

    /**
     * getSelectionParent Utility to return the logical parent of the selection
     * list
     * 
     * @param editparts
     *            List to parse for a common parent.
     * @return EditPart that is the parent or null if a common parent doesn't
     *         exist.
     */
    private EditPart getSelectionParent(List editparts) {
        ListIterator li = editparts.listIterator();
        while (li.hasNext()) {
            Object obj = li.next();
            if (!(obj instanceof ConnectionEditPart) && obj instanceof EditPart) {
                return ((EditPart)obj).getParent();
            }
        }
        
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
     */
    protected boolean isSelectionListener() {
        return true;
    }

    /**
     * Creates the Arrange All action
     * @param workbenchPage
     */
    public static ArrangeAction createArrangeAllAction(IWorkbenchPage workbenchPage) {
        ArrangeAction action = new ArrangeAction(workbenchPage,false);
        action.setId(ActionIds.ACTION_ARRANGE_ALL);
        action.setText(DiagramUIActionsMessages.ArrangeAction_ArrangeAll_ActionLabelText);
        action.setToolTipText(DiagramUIActionsMessages.ArrangeAction_ArrangeAll_ActionToolTipText);
        
        action
            .setImageDescriptor(DiagramUIActionsPluginImages.DESC_ARRANGE_ALL);
        action
            .setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_ARRANGE_ALL_DISABLED);
        action
            .setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_ARRANGE_ALL);
        return action;
    }
    
    /**
     * Creates the Arrange All action for the toolbar menu
     * @param workbenchPage
     */
    public static ArrangeAction createToolbarArrangeAllAction(IWorkbenchPage workbenchPage) {
        ArrangeAction action = new ArrangeAction(workbenchPage, false);
        action.setId(ActionIds.ACTION_TOOLBAR_ARRANGE_ALL);
        action.setText(DiagramUIActionsMessages.ArrangeAction_toolbar_ArrangeAll_ActionLabelText);
        action.setToolTipText(DiagramUIActionsMessages.ArrangeAction_toolbar_ArrangeAll_ActionToolTipText);
        
        action
            .setImageDescriptor(DiagramUIActionsPluginImages.DESC_ARRANGE_ALL);
        action
            .setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_ARRANGE_ALL_DISABLED);
        action
            .setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_ARRANGE_ALL);
        return action;
    }

    /**
     * Creates the Arrange Selection Only action
     * @param workbenchPage
     */
    public static ArrangeAction createArrangeSelectionAction(IWorkbenchPage workbenchPage) {
        ArrangeAction action = new ArrangeAction(workbenchPage, true);
        action.setId(ActionIds.ACTION_ARRANGE_SELECTION);
        action.setText(DiagramUIActionsMessages.ArrangeAction_ArrangeSelection_ActionLabelText);
        action.setToolTipText(DiagramUIActionsMessages.ArrangeAction_ArrangeSelection_ActionToolTipText);
        
        action
            .setImageDescriptor(DiagramUIActionsPluginImages.DESC_ARRANGE_SELECTED);
        action
            .setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_ARRANGE_SELECTED_DISABLED);
        action
            .setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_ARRANGE_SELECTED);
        return action;
    }
    
    /**
     * Creates the Arrange Selection Only action for the toolbar menu
     * @param workbenchPage
     */
    public static ArrangeAction createToolbarArrangeSelectionAction(IWorkbenchPage workbenchPage) {
        ArrangeAction action = new ArrangeAction(workbenchPage, true);
        action.setId(ActionIds.ACTION_TOOLBAR_ARRANGE_SELECTION);
        action.setText(DiagramUIActionsMessages.ArrangeAction_toolbar_ArrangeSelection_ActionLabelText);
        action.setToolTipText(DiagramUIActionsMessages.ArrangeAction_toolbar_ArrangeSelection_ActionToolTipText);
        
        action
            .setImageDescriptor(DiagramUIActionsPluginImages.DESC_ARRANGE_SELECTED);
        action
            .setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_ARRANGE_SELECTED_DISABLED);
        action
            .setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_ARRANGE_SELECTED);
        return action;
    }
    
    protected void doRun(IProgressMonitor progressMonitor) {
        IPreferenceStore preferenceStore = (IPreferenceStore) getDiagramEditPart().getDiagramPreferencesHint().getPreferenceStore();
        boolean animatedLayout = preferenceStore.getBoolean(
            IPreferenceConstants.PREF_ENABLE_ANIMATED_LAYOUT);
        
        if (animatedLayout)
            Animation.markBegin();
            
        super.doRun(progressMonitor);
        
        if (animatedLayout) {
            int durationInc = 800;
            int factor = 10;
            int size = 0;
            
            List operationSet = getOperationSet();
            if (isArrangeAll()){
                for (Iterator iter = operationSet.iterator(); iter.hasNext();) {
                    IGraphicalEditPart element = (IGraphicalEditPart) iter.next();
                    size += element.getFigure().getChildren().size();
                }
            }
            else if (operationSet != null && !operationSet.isEmpty()) {
                IGraphicalEditPart container = (IGraphicalEditPart)getSelectionParent(operationSet);
                size += container.getFigure().getChildren().size();
            }
            
            int totalDuration = Math.min(durationInc * factor / 2, Math.max(durationInc, (size / 
                    factor) * durationInc));
            
            Animation.run(totalDuration);
        }
    }
    
    /**
     * @param selection
     * @return
     */
    private List createOperationSetForArrangeAll(List selection) {
        Set parentsSet = new HashSet();
        for (Iterator iter = selection.iterator(); iter.hasNext();) {
            Object element = iter.next();
            if (element instanceof ShapeCompartmentEditPart || element instanceof DiagramEditPart){
                parentsSet.add(element);
            } else if (element instanceof EditPart){
                EditPart gEditPart = 
                    (EditPart)element;
                EditPart parentEditPart = gEditPart.getParent();
                if (parentEditPart instanceof ShapeCompartmentEditPart ||
                    parentEditPart instanceof DiagramEditPart){
                    if (!parentsSet.contains(parentEditPart))
                        parentsSet.add(parentEditPart);
                }
            }
        }
        if (parentsSet.isEmpty())
            return Collections.EMPTY_LIST;
        List elements = new ArrayList();
        elements.addAll(parentsSet);            
        return elements;
    }
    
    public String getLabel() {
        return isArrangeAll() ? DiagramUIActionsMessages.ArrangeAction_toolbar_ArrangeAll_ActionLabelText
            : DiagramUIActionsMessages.ArrangeAction_toolbar_ArrangeSelection_ActionLabelText;
    }
    
    protected boolean calculateEnabled() {
        
        List operationSet = getOperationSet();
        
        //arrange all, always enable
        if( isArrangeAll() && !operationSet.isEmpty()){
            return true;
        }

        EditPart parentEP = getSelectionParent(operationSet);
        
        // bugzilla 156733: disable this action if the parent or selected edit parts are not editable
        if ((parentEP instanceof IEditableEditPart)
                && !((IEditableEditPart) parentEP)
                        .isEditModeEnabled()) {
            return false;
        }
        
        for (Iterator i = operationSet.iterator(); i.hasNext();) {
            Object next = i.next();
            if ((next instanceof IEditableEditPart)
                    && !((IEditableEditPart) next)
                            .isEditModeEnabled()) {
                return false;
            }
        }
        
        //arrange selection
        if (operationSet.size() >= 2) {
            if (parentEP instanceof GraphicalEditPart) {
                GraphicalEditPart parent = (GraphicalEditPart)parentEP;
                if ((parent != null) &&(parent.getContentPane().getLayoutManager() instanceof XYLayout))
                    return true;
            }
        } else if (operationSet.size() == 1) {
            if (operationSet.get(0) instanceof GroupEditPart) {
                return true;
            }
            else {
                EditPart host = (EditPart) operationSet.get(0);
                for (Iterator iterator = host.getChildren().iterator(); iterator
                    .hasNext();) {
                    Object childEP = iterator.next();
                    if (childEP instanceof CompartmentEditPart
                        && ((CompartmentEditPart) childEP).getContentPane()
                            .getLayoutManager() instanceof XYLayout) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}

