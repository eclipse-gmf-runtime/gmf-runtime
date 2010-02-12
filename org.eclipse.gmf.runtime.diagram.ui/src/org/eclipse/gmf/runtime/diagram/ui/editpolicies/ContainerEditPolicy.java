/******************************************************************************
 * Copyright (c) 2002, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.util.ObjectAdapter;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.ui.util.ICustomData;
import org.eclipse.gmf.runtime.diagram.core.commands.GroupCommand;
import org.eclipse.gmf.runtime.diagram.core.internal.commands.BringForwardCommand;
import org.eclipse.gmf.runtime.diagram.core.internal.commands.BringToFrontCommand;
import org.eclipse.gmf.runtime.diagram.core.internal.commands.SendBackwardCommand;
import org.eclipse.gmf.runtime.diagram.core.internal.commands.SendToBackCommand;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.commands.ArrangeCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.DeferredLayoutCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.PasteCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GroupEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IBorderItemEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IEditableEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ListItemEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.commands.DuplicateViewsCommand;
import org.eclipse.gmf.runtime.diagram.ui.internal.commands.RefreshEditPartCommand;
import org.eclipse.gmf.runtime.diagram.ui.internal.commands.SnapCommand;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.ISurfaceEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.WorkspaceViewerProperties;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.requests.ArrangeRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.DuplicateRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.EditCommandRequestWrapper;
import org.eclipse.gmf.runtime.diagram.ui.requests.PasteViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.requests.ZOrderRequest;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutService;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutType;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.emf.clipboard.core.ClipboardSupportUtil;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * the container edit policy
 * @author sshaw
 */
public class ContainerEditPolicy
    extends org.eclipse.gef.editpolicies.ContainerEditPolicy {

    protected Command getAddCommand(GroupRequest request) {
        return null;
    }

    /**
     * gets a delete dependant command  
     * @param request the request
     * @return command
     */
    protected Command getDeleteDependantCommand(Request request) {
        return null;
    }

    protected Command getCreateCommand(CreateRequest request) {
        return null;
    }

    public Command getOrphanChildrenCommand(GroupRequest request) {
        return null;
    }

    /**
     * Returns a command to paste the views
     * @param request The PasteViewRequest
     * @return Command the command to execute
     */
    protected Command getPasteCommand(PasteViewRequest request) {
        /* Get the view context */
        IGraphicalEditPart editPart = (IGraphicalEditPart) getHost();
        View viewContext = (View) ((IAdaptable)editPart).getAdapter(View.class);

        /* Get the clipboard data */
        ICustomData[] data = request.getData();

        /* Return the paste command */
        if (data != null
            && viewContext != null
            && editPart instanceof ISurfaceEditPart) {
            return new ICommandProxy(new PasteCommand(editPart
                .getEditingDomain(), DiagramUIMessages.PasteCommand_Label,
                viewContext, data, MapModeUtil
                    .getMapMode(((org.eclipse.gef.GraphicalEditPart) getHost())
                        .getFigure())));
        }

        return null;
    }
    
    private class EditPartComparator implements Comparator {
        
        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Object arg0, Object arg1) {

            EditPart ep0 = (EditPart)arg0;
            EditPart ep1 = (EditPart)arg1;
            
            EditPart parent = ep0.getParent();
            int ep0Index = parent.getChildren().indexOf( ep0 );
            int ep1Index = parent.getChildren().indexOf( ep1 );
            
            return ep0Index - ep1Index;
        }
        
    }

    private List sortSelection( List editPartsToSort ) {

        // IF the list to be sorted is less than 2...
        if( editPartsToSort.size() < 2 ) {
            // Return the original list
            return editPartsToSort;
        }

        List toReturn = new ArrayList( editPartsToSort.size() );
        toReturn.addAll( editPartsToSort );

        Collections.sort( toReturn, new EditPartComparator() );
    
        return toReturn;
    }

    private List reverseSortSelection( List toSort ) {
        List toReturn = sortSelection( toSort );
        
        Collections.reverse( toReturn );
        return toReturn;
    }

    /**
     * Returns a command that moves the selected views to the front
     * @param request the ZOrder Request
     * @return the command to execute
     */
    protected Command getBringToFrontCommand( ZOrderRequest request ) {
        
        CompositeCommand toReturn = new CompositeCommand( "" ); //$NON-NLS-1$
        
        // Create commands for each view to move
        for (Iterator iter = sortSelection( request.getPartsToOrder() ).iterator();
            iter.hasNext(); ) {
            
            IGraphicalEditPart element = (IGraphicalEditPart) iter.next();
            toReturn.compose(new BringToFrontCommand(
                element.getEditingDomain(), (View) element.getModel()));
        }
        
        return new ICommandProxy( toReturn );
    }

    /**
     * Returns a command the moves the selected views one step toward the front
     * @param request the ZOrder Request
     * @return the command to execute
     */
    protected Command getBringForwardCommand( ZOrderRequest request ) {
        
        CompositeCommand toReturn = new CompositeCommand( "" ); //$NON-NLS-1$
    
        // Create commands for each view to move
        for (Iterator iter = reverseSortSelection( request.getPartsToOrder() ).iterator(); iter.hasNext();) {
            IGraphicalEditPart toOrder = (IGraphicalEditPart) iter.next();
            
            toReturn.compose(new BringForwardCommand(
                toOrder.getEditingDomain(), (View) toOrder.getModel()));
        }
        
        return new ICommandProxy( toReturn );
    }

    /**
     * Returns a command the moves the selected views to the back
     * @param request the ZOrder Request
     * @return the command to execute
     */
    protected Command getSendToBackCommand( ZOrderRequest request ) {
        
        CompositeCommand toReturn = new CompositeCommand( "" ); //$NON-NLS-1$
        
        // Create commands for each view to move
        for (Iterator iter = reverseSortSelection(request.getPartsToOrder()).iterator(); iter.hasNext();) {
            IGraphicalEditPart toOrder = (IGraphicalEditPart) iter.next();
            
            toReturn.compose(new SendToBackCommand(toOrder.getEditingDomain(),
                (View) toOrder.getModel()));
        }
        
        return new ICommandProxy( toReturn );
    }
    
    /**
     * Returns a command the moves the selected views one step toward the back
     * @param request the ZOrder Request
     * @return the command to execute
     */
    protected Command getSendBackwardCommand( ZOrderRequest request ) {

        CompositeCommand toReturn = new CompositeCommand( "" ); //$NON-NLS-1$
        
        // Create commands for each view to move
        for (Iterator iter = sortSelection(request.getPartsToOrder()).iterator(); iter.hasNext();) {
            IGraphicalEditPart toOrder = (IGraphicalEditPart) iter.next();
            
            toReturn.compose(new SendBackwardCommand(
                toOrder.getEditingDomain(), (View) toOrder.getModel()));
        }
        
        return new ICommandProxy( toReturn );
    }

    /**
     * gets an arrange command 
     * @param request
     * @return command
     */
    protected Command getArrangeCommand(ArrangeRequest request) {
        
        if (RequestConstants.REQ_ARRANGE_DEFERRED.equals(request.getType())) {
            String layoutType = request.getLayoutType();
            TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
                .getEditingDomain();
            return new ICommandProxy(
                new DeferredLayoutCommand(editingDomain,
                    request.getViewAdaptersToArrange(),
                    (IGraphicalEditPart) getHost(),
                    layoutType));
        }
        
        String layoutDesc = request.getLayoutType() != null ? request.getLayoutType() : LayoutType.DEFAULT;
        
        boolean offsetFromBoundingBox = false;
        List<IGraphicalEditPart> editparts = new ArrayList<IGraphicalEditPart>();
        
        if ( (ActionIds.ACTION_ARRANGE_ALL.equals(request.getType())) || 
             (ActionIds.ACTION_TOOLBAR_ARRANGE_ALL.equals(request.getType()))) {
            editparts = ((IGraphicalEditPart)getHost()).getChildren();          
            request.setPartsToArrange(editparts);
        }
        if ( (ActionIds.ACTION_ARRANGE_SELECTION.equals(request.getType())) ||
             (ActionIds.ACTION_TOOLBAR_ARRANGE_SELECTION.equals(request.getType()))) {
            editparts = request.getPartsToArrange();
            offsetFromBoundingBox = true;
        } 
        if (RequestConstants.REQ_ARRANGE_RADIAL.equals(request.getType())) {
            editparts = request.getPartsToArrange();
            offsetFromBoundingBox = true;
            layoutDesc = LayoutType.RADIAL;
        }
        
        if (editparts.isEmpty())
            return null;
        
        List hints = new ArrayList(2);
        hints.add(layoutDesc);
        hints.add(getHost());
        IAdaptable layoutHint = new ObjectAdapter(hints);
        
        TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
        	.getEditingDomain(); 
        
        CompositeTransactionalCommand ctc = new CompositeTransactionalCommand(editingDomain, StringStatics.BLANK);
        ctc.add(new ArrangeCommand(editingDomain, StringStatics.BLANK, null, editparts, layoutHint, offsetFromBoundingBox));
        
        //retrieves the preference store from the first edit part
        IGraphicalEditPart firstEditPart = (IGraphicalEditPart)editparts.get(0);
        if (firstEditPart.getViewer() instanceof DiagramGraphicalViewer){           
             IPreferenceStore preferenceStore = ((DiagramGraphicalViewer)firstEditPart.getViewer())
            .getWorkspaceViewerPreferenceStore();
             if (preferenceStore != null && preferenceStore.getBoolean(WorkspaceViewerProperties.SNAPTOGRID)){
             	Command snapCmd = getSnapCommand(request);
            	if (snapCmd != null) {
            		ctc.add(new CommandProxy(getSnapCommand(request)));
            	}
             }      
        }   
        
        return new ICommandProxy(ctc);
    }
    
    /**
     * @param offsetFromBoundingBox
     * @param nodes
     * @param layoutHint
     * @return runnable
     * 
     * @deprecated call the {@link LayoutService} directly
     */
    public Runnable layoutNodes(List nodes, boolean offsetFromBoundingBox, IAdaptable layoutHint) {
        final Runnable layoutRun =  LayoutService.getInstance().layoutLayoutNodes(nodes, offsetFromBoundingBox, layoutHint);
        return layoutRun;
    }

    /**
     * Returns a command to to duplicate views and their underlying semantic
     * elements (if applicable) of the given editparts.
     * 
     * @param request
     *            the <code>DuplicateElementsRequest</code> whose list of duplicated
     *            views will be populated when the command is executed
     * @return the command to perform the duplication
     */
    private Command getDuplicateCommand(DuplicateRequest request) {
        List notationViewsToDuplicate = new ArrayList();
        Set elementsToDuplicate = new HashSet();

        for (Iterator iter = request.getEditParts().iterator(); iter.hasNext();) {
            Object ep = iter.next();
            
            // Disable duplicate on groups for now.  See bugzilla 182972.
            if (ep instanceof GroupEditPart) {
                return UnexecutableCommand.INSTANCE;
            }
            
            if (ep instanceof ConnectionEditPart || ep instanceof ShapeEditPart
                || ep instanceof ListItemEditPart) {
                                
                View notationView = (View)((IGraphicalEditPart) ep).getModel();
                if (notationView != null) {
                    notationViewsToDuplicate.add(notationView);
                }
            }
        }
        
        // Remove views whose container view is getting copied.
        ClipboardSupportUtil.getCopyElements(notationViewsToDuplicate);
        
        TransactionalEditingDomain editingDomain = ((IGraphicalEditPart)getHost()).getEditingDomain();
        
        for (Iterator iter = notationViewsToDuplicate.iterator(); iter
                .hasNext();) {
            View view = (View) iter.next();
            EObject element = view.getElement();

            if (element != null) {
                EObject resolvedElement = EMFCoreUtil.resolve(editingDomain,
                        element);
                if (resolvedElement != null) {
                    elementsToDuplicate.add(resolvedElement);
                }
            }
        }

        /*
         * We must append all inner edges of a node being duplicated. Edges are non-containment
         * references, hence they won't be duplicated for free. Therefore, we add them here to
         * the list views to duplicate.
         * We don't add semantic elements of the edges to the list of semantic elements to duplicate
         * since we assume that their semantic elements are owned by source or target or their semantic
         * containers. 
         */
        /**
         * Until duplicate views action enablement is driven by the created duplicate views command,
         * we can't look for edges to duplicate. It's a performance hit.
         */
//        List<Edge> allInnerEdges = new LinkedList<Edge>();
//        for (Iterator itr = notationViewsToDuplicate.iterator(); itr.hasNext();) {
//        	allInnerEdges.addAll(ViewUtil.getAllInnerEdges((View) itr.next()));
//        }
//        notationViewsToDuplicate.addAll(allInnerEdges);
        
        if (!notationViewsToDuplicate.isEmpty()) {
            if (!elementsToDuplicate.isEmpty()) {
                org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest duplicateElementsRequest = new DuplicateElementsRequest(
                    editingDomain, new ArrayList(elementsToDuplicate));
                
                Command duplicateElementsCommand = getHost().getCommand(
                    new EditCommandRequestWrapper(duplicateElementsRequest, request.getExtendedData()));
                if (duplicateElementsCommand != null
                    && duplicateElementsCommand.canExecute()) {
                    CompositeCommand cc = new CompositeCommand(
                        DiagramUIMessages.Commands_Duplicate_Label);
                    cc
                        .compose(new CommandProxy(
                            duplicateElementsCommand));
                    
                    cc.compose(new DuplicateViewsCommand(editingDomain,
                        DiagramUIMessages.Commands_Duplicate_Label,
                        request, notationViewsToDuplicate,
                        duplicateElementsRequest.getAllDuplicatedElementsMap(), getDuplicateViewsOffset(request)));
                    return new ICommandProxy(cc);
                }
            } else {
                return new ICommandProxy(new DuplicateViewsCommand(editingDomain,
                    DiagramUIMessages.Commands_Duplicate_Label,
                    request, notationViewsToDuplicate, getDuplicateViewsOffset(request)));
            }
        }
        return null;
    }
    
    private Point getDuplicateViewsOffset(DuplicateRequest request) {
        if (request.getOffset() != null) {
            return request.getOffset();
        }
        int offset = MapModeUtil.getMapMode(
            ((org.eclipse.gef.GraphicalEditPart) getHost()).getFigure())
            .DPtoLP(10);
        return new Point(offset, offset);
    }
    
    private Command getSnapCommand(Request request){
            
        List editparts = null;
        if (request instanceof GroupRequest){           
            editparts =  ((GroupRequest)request).getEditParts();
        }   
        else if (request instanceof ArrangeRequest){
            editparts = ((ArrangeRequest)request).getPartsToArrange();
        }
        
        TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
                .getEditingDomain();
        if (editparts != null){
            return new ICommandProxy(new SnapCommand(editingDomain, editparts));
        }       
        return null;
    }
    
    /**
     * @see org.eclipse.gef.EditPolicy#getCommand(Request)
     */
    public Command getCommand(Request request) {
        if (ActionIds.ACTION_GROUP.equals(request.getType())
            && request instanceof GroupRequest) {
            return getGroupCommand((GroupRequest) request);
        } 
        else if (request instanceof ArrangeRequest) {
            return getArrangeCommand((ArrangeRequest)request);
        }       
        
        if (RequestConstants.REQ_SNAP_TO_GRID.equals(request.getType())){
            return getSnapCommand(request);
        }
        
        if (RequestConstants.REQ_REFRESH.equals(request.getType())) {
            IGraphicalEditPart containerEP = (IGraphicalEditPart) getHost();

            CompositeCommand cc = new CompositeCommand(""); //$NON-NLS-1$
            ListIterator li = containerEP.getChildren().listIterator();
            while (li.hasNext()) {
                cc.compose(
                    new RefreshEditPartCommand(
                        (IGraphicalEditPart) li.next(),
                        false));
            }
            cc.compose(
                new RefreshEditPartCommand(
                    (IGraphicalEditPart) getHost(),
                    true));

            return new ICommandProxy(cc);
        }

        if (RequestConstants.REQ_PASTE.equals(request.getType())) {
            return getPasteCommand((PasteViewRequest) request);
        }

        if (RequestConstants.REQ_DUPLICATE.equals(request.getType())) {
            return getDuplicateCommand(((DuplicateRequest) request));
        }

        if (ZOrderRequest.REQ_BRING_TO_FRONT.equals(request.getType())) {
            return getBringToFrontCommand((ZOrderRequest) request);
        }
        
        if (ZOrderRequest.REQ_BRING_FORWARD.equals(request.getType())) {
            return getBringForwardCommand((ZOrderRequest) request);
        }
        
        if (ZOrderRequest.REQ_SEND_TO_BACK.equals(request.getType())) {
            return getSendToBackCommand((ZOrderRequest) request);
        }
        
        if (ZOrderRequest.REQ_SEND_BACKWARD.equals(request.getType())) {
            return getSendBackwardCommand((ZOrderRequest) request);
        }

        return super.getCommand(request);
    }

    /**
     * Returns a command to group the editparts in the request.
     * 
     * @param request
     *            the request containing the editparts to be grouped.
     * @return the command to perform the grouping
     */
    protected Command getGroupCommand(GroupRequest request) {
        List shapeViews = new LinkedList();
        IGraphicalEditPart parentEP = null;
        for (Iterator iter = request.getEditParts().iterator(); iter.hasNext();) {
            Object editpart = iter.next();

            if (editpart instanceof ShapeEditPart) {

                if (!((IEditableEditPart) editpart).isEditModeEnabled()) {
                    return null;
                }
                
                if (editpart instanceof IBorderItemEditPart) {
                    return null;
                }

                if (parentEP != null) {
                    if (parentEP != ((ShapeEditPart) editpart).getParent()) {
                        // can only group shapes with the same parent
                        return null;
                    }
                } else {
                    parentEP = (IGraphicalEditPart) ((ShapeEditPart) editpart)
                        .getParent();
                }

                if (((ShapeEditPart) editpart).getModel() instanceof Node) {
                    shapeViews.add(((ShapeEditPart) editpart).getModel());
                }
            }
        }

        if (parentEP == null || !parentEP.isEditModeEnabled()) {
            return null;
        }

        GroupCommand cmd = new GroupCommand(((IGraphicalEditPart) getHost())
            .getEditingDomain(), shapeViews);
        return new ICommandProxy(cmd);
    }

    /**
     * @see org.eclipse.gef.EditPolicy#getTargetEditPart(org.eclipse.gef.Request)
     */
    public EditPart getTargetEditPart(Request request) {
        return understandsRequest(request) ? getHost() : null;
    }

    /**
     * @see org.eclipse.gef.EditPolicy#understandsRequest(org.eclipse.gef.Request)
     */
    public boolean understandsRequest(Request request) {
        return (
            ActionIds.ACTION_ARRANGE_ALL.equals(request.getType())
                || ActionIds.ACTION_TOOLBAR_ARRANGE_ALL.equals(request.getType())
                || ActionIds.ACTION_ARRANGE_SELECTION.equals(request.getType())
                || ActionIds.ACTION_GROUP.equals(request.getType())
                || ActionIds.ACTION_TOOLBAR_ARRANGE_SELECTION.equals(request.getType())             
                || RequestConstants.REQ_ARRANGE_RADIAL.equals(request.getType())
                || RequestConstants.REQ_ARRANGE_DEFERRED.equals(request.getType())
                || RequestConstants.REQ_REFRESH.equals(request.getType())
                || RequestConstants.REQ_PASTE.equals(request.getType())
                || RequestConstants.REQ_DUPLICATE.equals(request.getType())
                || RequestConstants.REQ_SNAP_TO_GRID.equals(request.getType())
                || ZOrderRequest.REQ_BRING_TO_FRONT.equals(request.getType())
                || ZOrderRequest.REQ_BRING_FORWARD.equals(request.getType())
                || ZOrderRequest.REQ_SEND_TO_BACK.equals(request.getType())
                || ZOrderRequest.REQ_SEND_BACKWARD.equals(request.getType()));
    }

}