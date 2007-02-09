/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.ui.IWorkbenchPage;

/**
 * 
 * @author melaasar
 */
public class AutoSizeAction extends DiagramAction {

	/**
	 * @param workbenchPage
	 */
	public AutoSizeAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);

		setText(DiagramUIActionsMessages.AutoSizeAction_ActionLabelText);
		setId(ActionIds.ACTION_AUTOSIZE);
		setToolTipText(DiagramUIActionsMessages.AutoSizeAction_ActionToolTipText);
		
		setImageDescriptor(DiagramUIActionsPluginImages.DESC_AUTOSIZE);
		setDisabledImageDescriptor(DiagramUIActionsPluginImages.DESC_AUTOSIZE_DISABLED);
		setHoverImageDescriptor(DiagramUIActionsPluginImages.DESC_AUTOSIZE);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		return new Request(RequestConstants.REQ_AUTOSIZE);
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}

    protected void doRun(IProgressMonitor progressMonitor) {
        List operationSet = getOperationSet();
        Iterator editParts = operationSet.iterator();
        while (editParts.hasNext()) {
            IGraphicalEditPart ep = (IGraphicalEditPart) editParts.next();
            System.out.println("------------------");
            System.out.println(ep.getFigure().getBounds());
            for (Iterator iter = ep.getSourceConnections().iterator(); iter.hasNext();) {
                ConnectionEditPart connectionEP = (ConnectionEditPart) iter.next();
                PointList points = ((PolylineConnectionEx)connectionEP.getFigure()).getPoints();
                for (int i = 0; i < points.size(); i++) {
                    Point pt = points.getPoint(i);
                    System.out.println("  " + pt);           
                }
                System.out.println(((IGraphicalEditPart)connectionEP.getTarget()).getFigure().getBounds());
            }
            for (Iterator iter = ep.getTargetConnections().iterator(); iter.hasNext();) {
                ConnectionEditPart connectionEP = (ConnectionEditPart) iter.next();
                PointList points = ((PolylineConnectionEx)connectionEP.getFigure()).getPoints();
                for (int i = 0; i < points.size(); i++) {
                    Point pt = points.getPoint(i);
                    System.out.println("  " + pt);           
                }
                System.out.println(((IGraphicalEditPart)connectionEP.getSource()).getFigure().getBounds());
            }
        }
        
        super.doRun(progressMonitor);
    }

}
