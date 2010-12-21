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

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.AccessibleHandleProvider;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.internal.commands.IPropertyValueDeferred;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.SetBoundsCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ChangeBoundsDeferredRequest;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * A resizable editpolicy for resizable shape editparts 1- It customizes the
 * selection handles 2- It handles the autosize and deferred change bounds
 * request
 * 
 * @author melaasar
 */
public class ResizableShapeEditPolicy
	extends ResizableEditPolicyEx {

	/**
	 * Cfreates a new AutoSize comamnd
	 * 
	 * @param request
	 * @return command
	 */
	protected Command getAutoSizeCommand(Request request) {
        TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost()).getEditingDomain();
		ICommand resizeCommand = new SetBoundsCommand(editingDomain, 
			DiagramUIMessages.SetAutoSizeCommand_Label,
			new EObjectAdapter((View) getHost().getModel()), new Dimension(-1,
				-1));
		return new ICommandProxy(resizeCommand);
	}

	/**
	 * Method getMoveDeferredCommand.
	 * 
	 * @param request
	 * @return Command
	 */
	protected Command getMoveDeferredCommand(ChangeBoundsDeferredRequest request) {
		final class SetDeferredPropertyCommand
			extends AbstractTransactionalCommand {

			private IAdaptable newValue;

			private IAdaptable viewAdapter;

			/**
			 * constructor
			 * 
             * @param editingDomain
             * the editing domain through which model changes are made
			 * @param label
			 * @param viewAdapter
			 * @param newValue
			 */
			public SetDeferredPropertyCommand(TransactionalEditingDomain editingDomain, String label,
					IAdaptable viewAdapter, IAdaptable newValue) {
				super(editingDomain, label, null);
				this.viewAdapter = viewAdapter;
				this.newValue = newValue;
			}

			protected CommandResult doExecuteWithResult(
                    IProgressMonitor progressMonitor, IAdaptable info)
                throws ExecutionException {
                
				if (null == viewAdapter || null == newValue)
					return CommandResult.newCancelledCommandResult();

				View view = (View) viewAdapter.getAdapter(View.class);
				Point p = (Point) newValue
					.getAdapter(IPropertyValueDeferred.class);
				ViewUtil.setStructuralFeatureValue(view, NotationPackage.eINSTANCE.getLocation_X(),
						Integer.valueOf(p.x));
				ViewUtil.setStructuralFeatureValue(view, NotationPackage.eINSTANCE.getLocation_Y(),
						Integer.valueOf(p.y));

				// clear for garbage collection
				viewAdapter = null;
				newValue = null;
				return CommandResult.newOKCommandResult();
			}
		}
        View view = (View) getHost().getModel();
        
        TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
            .getEditingDomain();
        
        SetDeferredPropertyCommand cmd = new SetDeferredPropertyCommand(editingDomain,
			DiagramUIMessages.ResizableShapeEditPolicy_MoveDeferredCommand_label,
			new EObjectAdapter(view), request
				.getLocationAdapter());
		return new ICommandProxy(cmd);
	}

	public Command getCommand(Request request) {
		if (RequestConstants.REQ_AUTOSIZE.equals(request.getType()))
			return getAutoSizeCommand(request);
		if (RequestConstants.REQ_MOVE_DEFERRED.equals(request.getType()))
			return getMoveDeferredCommand((ChangeBoundsDeferredRequest) request);
		return super.getCommand(request);
	}

	public boolean understandsRequest(Request request) {
		if (RequestConstants.REQ_AUTOSIZE.equals(request.getType())
			|| RequestConstants.REQ_MOVE_DEFERRED.equals(request.getType()))
			return true;
		return super.understandsRequest(request);
	}

	/**
	 * Creates the figure used for feedback.
	 * 
	 * @return the new feedback figure
	 */
	protected IFigure createDragSourceFeedbackFigure() {
		// Use a ghost rectangle for feedback
		RectangleFigure r = new RectangleFigure();
		FigureUtilities.makeGhostShape(r);
		r.setLineStyle(Graphics.LINE_DOT);
		r.setForegroundColor(ColorConstants.white);
		r.setBounds(getInitialFeedbackBounds());
		addFeedback(r);
		return r;
	}

	/**
	 * Override for AccessibleHandleProvider when deactivated
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=69316
	 */
	public Object getAdapter(Class key) {
		if (key == AccessibleHandleProvider.class)
			// handles == null when deactivated
			if (handles == null) {
				return null;
			}
		return super.getAdapter(key);
	}
}
