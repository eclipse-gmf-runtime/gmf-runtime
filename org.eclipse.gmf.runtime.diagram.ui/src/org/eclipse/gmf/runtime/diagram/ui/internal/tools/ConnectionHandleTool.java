/******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.tools;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.common.ui.dialogs.ExpansionType;
import org.eclipse.gmf.runtime.common.ui.util.DispatchingProgressDialogUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.PopupMenuCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.handles.ConnectionHandle;
import org.eclipse.gmf.runtime.diagram.ui.internal.commands.ElementTypeLabelProvider;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.menus.PopupMenu;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.requests.ArrangeRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateUnspecifiedTypeConnectionRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.requests.ShowRelatedElementsRequest;
import org.eclipse.gmf.runtime.diagram.ui.tools.ConnectionCreationTool;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.ModelingAssistantService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;

/**
 * This tool is responsible for reacting to mouse events on the connection
 * handles. It will get a command to create a connection when the user clicks
 * and drags the handle. It will get a command to expand elements, when the user
 * clicks the handle. It also adds support to create relationships from target
 * to source.
 * 
 * @author cmahoney
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class ConnectionHandleTool
	extends ConnectionCreationTool
	implements DragTracker {

	/** Time in ms to display error icon when there are no related elements. */
	private static final int NO_RELATED_ELEMENTS_DISPLAY_TIME = 2000;

	/** the connection handle containing required information */
	private ConnectionHandle connectionHandle;

	/**
	 * Constructor for ConnectionHandleTool.
	 * 
	 * @param connectionHandle
	 *            the connection handle
	 */
	public ConnectionHandleTool(ConnectionHandle connectionHandle) {
		this.connectionHandle = connectionHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.tools.TargetingTool#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		if (getConnectionHandle().isIncoming()) {
			CreateUnspecifiedTypeConnectionRequest request = new CreateUnspecifiedTypeConnectionRequest(
				ModelingAssistantService.getInstance().getRelTypesOnTarget(
					getConnectionHandle().getOwner()), true,
				getPreferencesHint());
			request.setDirectionReversed(true);
			return request;
		} else {
			return new CreateUnspecifiedTypeConnectionRequest(
				ModelingAssistantService.getInstance().getRelTypesOnSource(
					getConnectionHandle().getOwner()), true,
				getPreferencesHint());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.tools.AbstractTool#getCommand()
	 */
	protected Command getCommand() {
		if (getConnectionHandle().isIncoming()) {
			CreateUnspecifiedTypeConnectionRequest unspecifiedTypeRequest = (CreateUnspecifiedTypeConnectionRequest) getTargetRequest();
			unspecifiedTypeRequest.setDirectionReversed(true);
		}

		return super.getCommand();
	}

	/**
	 * When a double-click occurs, this is called first on the first mouse
	 * button up. In the case where a double-click is going to occur, we do not
	 * want the default behavior here (which is to create a self-connection).
	 * Therefore, we will only permit self-connections if the user has moved the
	 * mouse.
	 * 
	 * @see org.eclipse.gef.tools.AbstractTool#handleButtonUp(int)
	 */
	protected boolean handleButtonUp(int button) {
		if (getDragMoveDelta().equals(0, 0)) {
			return true; // ignore this button up
		}
		return super.handleButtonUp(button);
	}

	/**
	 * On a double-click, the related elements are expanded.
	 * 
	 * @see org.eclipse.gef.tools.AbstractTool#handleDoubleClick(int)
	 */
	protected boolean handleDoubleClick(int button) {
		// When a connection is to be created, a dialog box may appear which
		// will cause
		// this tool to be deactivated. This behavior is overridden by setting
		// the
		// avoid deactivation flag.
		eraseSourceFeedback();
		setAvoidDeactivation(true);

		List relatedShapes = executeShowRelatedElementsCommand();
		if (relatedShapes != null && relatedShapes.size() < 2) {
			signalNoRelatedElements();
		}
		setAvoidDeactivation(false);
		deactivate();

		return true;
	}

	/**
	 * Gets the command to show related elements and arrange the new views.
	 * Prompts the user for the relationship types to choose if there are
	 * multiple types. Executes the command with a progress monitor.
	 * 
	 * @return the list of related shapes
	 */
	protected List executeShowRelatedElementsCommand() {
		IGraphicalEditPart targetEP = (IGraphicalEditPart) getTargetEditPart();

		DiagramEditPart diagramEP = ((DiagramEditDomain) targetEP
			.getDiagramEditDomain()).getDiagramEditorPart()
			.getDiagramEditPart();

		List popupContent = getConnectionHandle().isIncoming() ? ModelingAssistantService
			.getInstance().getRelTypesForSREOnTarget(targetEP)
			: ModelingAssistantService.getInstance().getRelTypesForSREOnSource(
				targetEP);

		if (popupContent.isEmpty()) {
			return null;
		}

		// TODO: Get this working
		// popupContent.add(0, DiagramResourceManager
		// .getI18NString("ConnectionHandle.Popup.ShowRelatedElementsDialog"));
		// //$NON-NLS-1$

		LabelProvider labelProvider = new ElementTypeLabelProvider() {

			public String getText(Object element) {
				String elementName = super.getText(element);
				if (element instanceof IElementType) {
					String theInputStr = DiagramUIMessages.ConnectionHandle_Popup_ShowRelatedXRelationships;
					String text = NLS.bind(theInputStr, elementName);
					return text;
				}
				return elementName;
			}
		};

		PopupMenu popupMenu = new PopupMenu(popupContent, labelProvider);
		PopupMenuCommand popupCmd = new PopupMenuCommand("", Display //$NON-NLS-1$
			.getCurrent().getActiveShell(), popupMenu);
		popupCmd.execute(null);
		if (!popupCmd.getCommandResult().getStatus().isOK()) {
			// user cancelled gesture
			return null;
		}
		Object result = popupCmd.getCommandResult().getReturnValue();

		if (result instanceof IElementType) {
			ExpansionType expansionType = (getConnectionHandle().isIncoming()) ? ExpansionType.INCOMING
				: ExpansionType.OUTGOING;
			ShowRelatedElementsRequest showRelatedRequest = new ShowRelatedElementsRequest(
				Collections.singletonList(targetEP), Collections
					.singletonList(result), false, 1, expansionType);

			Command sreCommand = diagramEP.getCommand(showRelatedRequest);

			if (sreCommand == null) {
				return null;
			}

			final CompoundCommand cc = new CompoundCommand(sreCommand
				.getLabel());

			cc.add(sreCommand);

			ArrangeRequest request = new ArrangeRequest(
				RequestConstants.REQ_ARRANGE_DEFERRED);
			request.setViewAdaptersToArrange(showRelatedRequest
				.getRelatedShapes());
			Command arrangeCommand = diagramEP.getCommand(request);

			cc.add(arrangeCommand);
			executeWithProgressMonitor(cc);
			return showRelatedRequest.getRelatedShapes();
		} else {
			// TODO: Pop up SRE dialog.
			MessageDialog.openInformation(
				Display.getCurrent().getActiveShell(), "To Be Implemented", //$NON-NLS-1$
				"The Show Related Elements Dialog will popup."); //$NON-NLS-1$
			return null;
		}
	}

	/**
	 * Executes the command using a dispatching progress dialog utility - in
	 * order to get a cancellable progress monitor
	 * 
	 * @param cmd
	 *            the <code>ICommand</code> to execute
	 */
	protected void executeWithProgressMonitor(final Command command) {

		final DiagramCommandStack commandStack = ((IGraphicalEditPart) getTargetEditPart())
			.getDiagramEditDomain().getDiagramCommandStack();

		DispatchingProgressDialogUtil
			.runWithDispatchingProgressDialog(new IRunnableWithProgress() {

				public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
					commandStack.execute(command, monitor);
				}
			});
	}

	/**
	 * Temporary shows a red X over the connection handle to indicate that there
	 * are no related elements to be expanded.
	 */
	protected void signalNoRelatedElements() {
		getConnectionHandle().addErrorIcon();
		Display.getCurrent().timerExec(NO_RELATED_ELEMENTS_DISPLAY_TIME,
			new Runnable() {

				public void run() {
					getConnectionHandle().removeErrorIcon();
				}
			});
	}

	/**
	 * Returns the connection handle.
	 * 
	 * @return the connection handle
	 */
	protected ConnectionHandle getConnectionHandle() {
		return connectionHandle;
	}

}
