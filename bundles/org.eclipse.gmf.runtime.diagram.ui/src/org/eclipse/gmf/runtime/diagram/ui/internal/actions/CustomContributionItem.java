/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gmf.runtime.common.ui.action.AbstractContributionItem;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.ui.IWorkbenchPage;

/**
 * @author melaasar
 * 
 * An abstract implementation of a custom toolbar contribution item for a diagram
 * engine-based part
 *  
 */
public abstract class CustomContributionItem
	extends AbstractContributionItem {

	/** the target request */
	private Request targetRequest;
	/** the cached operation set */
	private List _operationSet = Collections.EMPTY_LIST;

	/**
	 * Constructs a new custom contribution item
	 * 
	 * @param workbenchPage The workbench page
	 * @param id The item's id
	 */
	public CustomContributionItem(
		IWorkbenchPage workbenchPage,
		String id) {
		super(workbenchPage, id);
	}

	/**
	 * @see org.eclipse.jface.action.IContributionItem#dispose()
	 */
	public void dispose() {
		targetRequest = null;
		_operationSet = null;
		super.dispose();
	}

	protected void doRun(IProgressMonitor progressMonitor) {
		updateTargetRequest();
		execute(getCommand(), progressMonitor);
	}

	public void refresh() {
		_operationSet = null; // invalidate the cached set
		updateTargetRequest();
		super.refresh();
	}

	protected boolean calculateEnabled() {
		Command command = getCommand();
		return command != null && command.canExecute();
	}

	/**
	 * Executes the given {@link Command}.
	 * @param command the command to execute
	 * @param progressMonitor the progress monitor
	 */
	protected final void execute(
		Command command,
		IProgressMonitor progressMonitor) {
		if (command == null || !command.canExecute())
			return;
		if (getDiagramCommandStack() != null)
			getDiagramCommandStack().execute(command, progressMonitor);
	}

	/**
	 * gets the part's diagram command stack.
	 * @return the <code>DiagramCommandStack</code>
	 */
	protected DiagramCommandStack getDiagramCommandStack() {
		Object stack = getWorkbenchPart().getAdapter(CommandStack.class);
		return (stack instanceof DiagramCommandStack)
			? (DiagramCommandStack) stack
			: null;
	}

	/**
	 * Gets a command to execute on the operation set based on the target request
	 * 
	 * @return a command to execute
	 */
	protected Command getCommand() {
		return getCommand(getTargetRequest());
	}
	
	/**
	 * Gets a command to execute on the operation set based on a given request
	 * @param request the request
	 * @return a command to execute
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
		return command.isEmpty()
			|| command.size() != operationSet.size()
				? UnexecutableCommand.INSTANCE
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
	 * updates the target request. 
	 * Clients should call this method whenever the request 
	 * is expected to be changed
	 */
	protected void updateTargetRequest() {
		// no def impl
	}

	/**
	 * A utility method to return a list of objects in the current structured selection
	 * 
	 * @return A list of objects in the current structure selection
	 */
	protected List getSelectedObjects() {
		return getStructuredSelection().toList();
	}

	/**
	 * Return the list of editparts considered the operation set after caching them
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
	 * Filters the selected objects and returns only editparts that understands the request
	 * @return a list of editparts selected.
	 * 
	 */
	protected List createOperationSet() {
		List selection = getSelectedObjects();
		if (selection.isEmpty() || !(selection.get(0) instanceof IGraphicalEditPart))
			return Collections.EMPTY_LIST;
		Iterator selectedEPs = selection.iterator();
		List targetedEPs = new ArrayList();
		while (selectedEPs.hasNext()) {
			EditPart selectedEP = (EditPart) selectedEPs.next();
			targetedEPs.addAll(getTargetEditParts(selectedEP));
		}
		return targetedEPs.isEmpty() ? Collections.EMPTY_LIST : targetedEPs;
	}

	/**
	 * Given an editpart, returns a list of target editparts to the current request
	 * If no targets could be found, an empty list is returned 
	 *  
	 * @param editpart The given editpart
	 * @return a list of target editparts, or Empty list if none
	 */
	protected List getTargetEditParts(EditPart editpart) {
		EditPart targetEP = editpart.getTargetEditPart(getTargetRequest());
		return (targetEP == null)
			? Collections.EMPTY_LIST
			: Collections.singletonList(targetEP);
	}

	/**
	 * A utility method to get the <code>IDiagramEditDomain</code> from the 
	 * current part if it adapts to it
	 * 
	 * @return The diagram edit domain adapter if it exists; <code>null</code> otherwise
	 */
	protected IDiagramEditDomain getDiagramEditDomain() {
		return (IDiagramEditDomain) getWorkbenchPart().getAdapter(
			IDiagramEditDomain.class);
	}

	/**
	 * A utility method to return the active part if it implements the 
	 * <code>IDiagramWorkbenchPart</code> interface
	 *  
	 * @return The current part if it implements <code>IDiagramWorkbenchPart</code>; <code>null</code> otherwise
	 */
	protected IDiagramWorkbenchPart getDiagramWorkbenchPart() {
		return getWorkbenchPart() instanceof IDiagramWorkbenchPart
			? (IDiagramWorkbenchPart) getWorkbenchPart()
			: null;
	}

	/**
	 * A utility method to return the active <code>DiagramEditPart</code>
	 * if the current part implements <code>IDiagramWorkbenchPart</code>
	 *  
	 * @return The current diagram if the parts implements 
	 * <code>IDiagramWorkbenchPart</code>; <code>null</code> otherwise
	 */
	protected DiagramEditPart getDiagramEditPart() {
		IDiagramWorkbenchPart editor = getDiagramWorkbenchPart();
		return editor != null ? editor.getDiagramEditPart() : null;
	}

	/**
	 * A utility method to return the active <code>DiagramEditPart</code>
	 * if the current part implements <code>IDiagramWorkbenchPart</code>
	 *  
	 * @return The current diagram if the parts implements 
	 * <code>IDiagramWorkbenchPart</code>; <code>null</code> otherwise
	 */
	protected IDiagramGraphicalViewer getDiagramGraphicalViewer() {
		IDiagramWorkbenchPart part = getDiagramWorkbenchPart();
		return part != null ? part.getDiagramGraphicalViewer() : null;
	}

	/**
	 * Filters the given list of EditParts so that the list only contains the EditParts that 
	 * matches the given condition when called on the given editpart).
	 * @param list the list of edit parts to filter
	 * @param condition the condition 
	 * @return a modified list containing those editparts that matched the condition
	 */
	protected List filterEditPartsMatching(
		List list,
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
}
