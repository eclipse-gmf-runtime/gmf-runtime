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

package org.eclipse.gmf.runtime.diagram.ui.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.Disposable;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

/**
 * An abstract implementation of a diagram action that follows the
 * request-command architecture.
 * 
 * Notice: 1) This action retargets to the active workbench part 2) This action
 * can either be contributed programatically or through the
 * <code>ControbutionItemService</code>.
 * 
 * @author melaasar
 */
public abstract class DiagramAction
	extends AbstractActionHandler
	implements Disposable {

	/** the target request */
	private Request targetRequest;

	/** the cached operation set */
	private List _operationSet = Collections.EMPTY_LIST;

	/**
	 * Constructs a new diagram action
	 * 
	 * @param workbenchPage
	 *            The workbench page associated with this action
	 */
	public DiagramAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

	/**
	 * Constructs a new diagram action. This constructor is provided just in
	 * case a derived class needs to support both the construction of a diagram
	 * action with a workbenchpart. Typically this is only when the diagram
	 * declares its own action in additional to the one registered with the
	 * action serivce.
	 * 
	 * @param workbenchpart
	 *            The workbench part associated with this action
	 */
	protected DiagramAction(IWorkbenchPart workbenchpart) {
		super(workbenchpart);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.action.IDisposableAction#dispose()
	 */
	public void dispose() {
		targetRequest = null;
		_operationSet = null;
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#doRun(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void doRun(IProgressMonitor progressMonitor) {
		execute(getCommand(), progressMonitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.action.IRepeatableAction#refresh()
	 */
	public void refresh() {
		_operationSet = null; // invalidate the cached set
		updateTargetRequest();
		setEnabled(calculateEnabled());
	}

	/**
	 * Calculates the enblement state of the action
	 * 
	 * @return <code>true</code> if action should be enabled,
	 *         <code>false</code> otherwise
	 */
	protected boolean calculateEnabled() {
		Command command = getCommand();
		return command != null && command.canExecute();
	}

	/**
	 * Executes the given {@link Command}.
	 * 
	 * @param command
	 *            the command to execute
	 * @param progressMonitor
	 *            the progress monitor to use during execution
	 */
	protected final void execute(Command command,
			IProgressMonitor progressMonitor) {
		if (command == null || !command.canExecute())
			return;
		if (getDiagramCommandStack() != null)
			getDiagramCommandStack().execute(command, progressMonitor);
	}

	/**
	 * gives access to the diagram command stack
	 * 
	 * @return the diagram command stack
	 */
	protected DiagramCommandStack getDiagramCommandStack() {
		Object stack = getWorkbenchPart().getAdapter(CommandStack.class);
		return (stack instanceof DiagramCommandStack) ? (DiagramCommandStack) stack
			: null;
	}

	/**
	 * Gets the associated Command with this action based on the target request
	 * 
	 * @return a command
	 */
	protected Command getCommand() {
		return getCommand(getTargetRequest());
	}

	/**
	 * Gets a command to execute on the operation set based on a given request
	 * 
	 * @param request
	 *            request to use to get the command
	 * @return a command
	 */
	protected Command getCommand(Request request) {
		List operationSet = getOperationSet();
		Iterator editParts = operationSet.iterator();
		CompoundCommand command = new CompoundCommand(getCommandLabel());
		while (editParts.hasNext()) {
			EditPart editPart = (EditPart) editParts.next();
			Command curCommand = editPart.getCommand(request);
			if (curCommand != null) {
				command.add(curCommand);
			}
		}
		return command.isEmpty() || command.size() != operationSet.size() ? UnexecutableCommand.INSTANCE
			: (Command) command;
	}

	/**
	 * Gets an optional label for the action's executed command
	 * 
	 * @return An optional label for the action's executed command
	 */
	protected String getCommandLabel() {
		return null;
	}

	/**
	 * Gets a request to be addressed to the operation set
	 * 
	 * @return a target request
	 */
	protected Request getTargetRequest() {
		if (targetRequest == null)
			targetRequest = createTargetRequest();
		return targetRequest;
	}

	/**
	 * Creates a new target request
	 * 
	 * @return the new target request
	 */
	protected abstract Request createTargetRequest();

	/**
	 * updates the target request. Clients should call this method whenever the
	 * request is expected to be changed
	 */
	protected void updateTargetRequest() {
		// no def impl
	}

	/**
	 * Sets the target request to <tt>null</tt>. This will force the creation
	 * of a new target request on the next {@link #getTargetRequest()} call.
	 */
	protected void clearTargetRequest() {
		targetRequest = null;
	}

	/**
	 * A utility method to return a list of objects in the current structured
	 * selection
	 * 
	 * @return A list of objects in the current structure selection
	 */
	protected List getSelectedObjects() {
		return getStructuredSelection().toList();
	}

	/**
	 * Return the list of editparts considered the operation set after caching
	 * them
	 * 
	 * @return A list of editparts conidered the operation set
	 */
	protected final List getOperationSet() {
		if (_operationSet == null) {
			_operationSet = createOperationSet();
			if (_operationSet == null)
				_operationSet = Collections.EMPTY_LIST;
		}
		return _operationSet;
	}

	/**
	 * Filters the selected objects and returns only editparts that understands
	 * the request
	 * 
	 * @return a list of editparts selected.
	 */
	protected List createOperationSet() {
		List selection = getSelectedObjects();
		if (selection.isEmpty()
			|| !(selection.get(0) instanceof IGraphicalEditPart))
			return Collections.EMPTY_LIST;
		Iterator selectedEPs = selection.iterator();
		List targetedEPs = new ArrayList();
		while (selectedEPs.hasNext()) {
			EditPart selectedEP = (EditPart) selectedEPs.next();
			targetedEPs.addAll(getTargetEdiParts(selectedEP));
		}
		return targetedEPs.isEmpty() ? Collections.EMPTY_LIST
			: targetedEPs;
	}

	/**
	 * Given an editpart, returns a list of target editparts to the current
	 * request If no targets could be found, an empty list is returned
	 * 
	 * @param editpart
	 *            The given editpart
	 * @return a list of target editparts, or Empty list if none
	 */
	protected List getTargetEdiParts(EditPart editpart) {
		EditPart targetEP = editpart.getTargetEditPart(getTargetRequest());
		return (targetEP == null) ? Collections.EMPTY_LIST
			: Collections.singletonList(targetEP);
	}

	/**
	 * A utility method to get the <code>IDiagramEditDomain</code> from the
	 * current part if it adapts to it
	 * 
	 * @return The diagram edit domain adapter if it exists; <code>null</code>
	 *         otherwise
	 */
	protected IDiagramEditDomain getDiagramEditDomain() {
		return (IDiagramEditDomain) getWorkbenchPart().getAdapter(
			IDiagramEditDomain.class);
	}

	/**
	 * A utility method to return the active part if it implements the
	 * <code>IDiagramWorkbenchPart</code> interface
	 * 
	 * @return The current part if it implements
	 *         <code>IDiagramWorkbenchPart</code>; <code>null</code>
	 *         otherwise
	 */
	protected IDiagramWorkbenchPart getDiagramWorkbenchPart() {
		return getWorkbenchPart() instanceof IDiagramWorkbenchPart ? (IDiagramWorkbenchPart) getWorkbenchPart()
			: null;
	}

	/**
	 * A utility method to return the active <code>DiagramEditPart</code> if
	 * the current part implements <code>IDiagramWorkbenchPart</code>
	 * 
	 * @return The current diagram if the parts implements
	 *         <code>IDiagramWorkbenchPart</code>; <code>null</code>
	 *         otherwise
	 */
	protected DiagramEditPart getDiagramEditPart() {
		IDiagramWorkbenchPart part = getDiagramWorkbenchPart();
		return part != null ? part.getDiagramEditPart()
			: null;
	}

	/**
	 * A utility method to return the active <code>DiagramEditPart</code> if
	 * the current part implements <code>IDiagramWorkbenchPart</code>
	 * 
	 * @return The current diagram if the parts implements
	 *         <code>IDiagramWorkbenchPart</code>; <code>null</code>
	 *         otherwise
	 */
	protected IDiagramGraphicalViewer getDiagramGraphicalViewer() {
		IDiagramWorkbenchPart part = getDiagramWorkbenchPart();
		return part != null ? part.getDiagramGraphicalViewer()
			: null;
	}

	/**
	 * Filters the given list of EditParts so that the list only contains the
	 * EditParts that matches the given condition.
	 * 
	 * @param list
	 *            the list of edit parts to filter
	 * @param condition
	 *            the condition
	 * @return a modified list containing those editparts that matched the
	 *         condition
	 */
	protected List filterEditPartsMatching(List list,
			EditPartViewer.Conditional condition) {
		List matchList = new ArrayList();
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			EditPart ep = (EditPart) iter.next();
			if (condition.evaluate(ep))
				matchList.add(ep);
		}
		return matchList;
	}

	/**
	 * Does this action need to listen to selection change events? If the
	 * enablement state of the context menu or the operation set depends on what
	 * is selected in a diagram, then this needs to return true. If this action
	 * targets the diagram only, then it should return false.
	 */
	protected abstract boolean isSelectionListener();

	/**
	 * @param targetRequest
	 *            The targetRequest to set.
	 */
	protected void setTargetRequest(Request targetRequest) {
		this.targetRequest = targetRequest;
	}

	/**
	 * Gets the preferences hint that is to be used to find the appropriate
	 * preference store from which to retrieve diagram preference values. The
	 * preference hint is mapped to a preference store in the preference
	 * registry <@link DiagramPreferencesRegistry>.
	 * 
	 * @return the preferences hint
	 */
	protected PreferencesHint getPreferencesHint() {
		if (getDiagramEditPart() != null) {
			return getDiagramEditPart().getDiagramPreferencesHint();
		}
		return PreferencesHint.USE_DEFAULTS;
	}
}
