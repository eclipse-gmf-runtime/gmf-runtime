/******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *    Nicolas Rouquette (NASA) - Fix for Bug 260812. 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.requests.ArrangeRequest;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutType;
import org.eclipse.gmf.runtime.diagram.ui.util.EditPartUtil;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This command is used to arrange editparts on a diagram, when only the view
 * adapters are available at the time of creating the command. It is necessary
 * to have the editparts when creating a layout command so this command defers
 * the creation of the layout command until execution time at which point it can
 * get the editparts from the editpart registry using the view adapters.
 * 
 * @author cmahoney
 */
public class DeferredLayoutCommand
	extends AbstractTransactionalCommand {

	/** the type of layout to be performed */
	protected String layoutType;

	/** the IAdaptables from which an View can be retrieved */
	protected List viewAdapters;

	/** the diagram editpart used to get the editpart registry */
	protected IGraphicalEditPart containerEP;

	/**
	 * Constructor for <code>DeferredLayoutCommand</code>.
	 * 
     * @param editingDomain
     *            the editing domain through which model changes are made
	 * @param viewAdapters
	 *            the IAdaptables from which an IView can be retrieved
	 * @param containerEP
	 *            the container editpart used to get the editpart registry
	 */
	public DeferredLayoutCommand(TransactionalEditingDomain editingDomain, List viewAdapters,
		IGraphicalEditPart containerEP) {
		this(editingDomain, viewAdapters, containerEP, LayoutType.DEFAULT);
	}

	/**
	 * Constructor for <code>DeferredLayoutCommand</code>.
	 * 
     * @param editingDomain
     *            the editing domain through which model changes are made
	 * @param viewAdapters
	 *            the IAdaptables from which an IView can be retrieved
	 * @param containerEP
	 *            the container editpart used to get the editpart registry
	 * @param commandLayoutType
	 *            the type of layout to be performed
	 */
	public DeferredLayoutCommand(TransactionalEditingDomain editingDomain, List viewAdapters,
		IGraphicalEditPart containerEP, String commandLayoutType) {

		super(editingDomain,
            DiagramUIMessages.Command_Deferred_Layout, null);
		if (commandLayoutType != null) {
			this.layoutType = commandLayoutType;
		} else {
			this.layoutType = LayoutType.DEFAULT;
		}
		this.viewAdapters = viewAdapters;
		this.containerEP = containerEP;
	}

	public List getAffectedFiles() {
		if (containerEP != null) {
			View view = (View)containerEP.getModel();
			if (view != null) {
				IFile f = WorkspaceSynchronizer.getFile(view.eResource());
				return f != null ? Collections.singletonList(f)
					: Collections.EMPTY_LIST;
			}
		}
		return super.getAffectedFiles();
	}

	/**
	 * Executes a layout command with all the editparts for the view adapters.
	 * 
	 */
	protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {

		RunnableWithResult refreshRunnable = new RunnableWithResult() {
			
			private IStatus status;
            private Object result;

            public Object getResult() {
                return result;
            }

            public void setStatus(IStatus status) {
                this.status = status;
            }

            public IStatus getStatus() {
                return status;
            }
            
			public void run() {
				containerEP.refresh();
				
				// The layout command requires that the figure world is updated.
				getContainerFigure().invalidate();
				getContainerFigure().validate();

				List editParts = new ArrayList(viewAdapters.size());
				Map epRegistry = containerEP.getRoot().getViewer()
					.getEditPartRegistry();
				for (Iterator iter = viewAdapters.iterator(); iter.hasNext();) {
					IAdaptable ad = (IAdaptable) iter.next();
					View view = (View) ad.getAdapter(View.class);
					Object ep = epRegistry.get(view);
					if (ep != null) {
						editParts.add(ep);
					}
				}

				if (editParts.isEmpty()) {
					result = editParts;
					return;
				}

				Set layoutSet = new HashSet(editParts.size());
				layoutSet.addAll(editParts);
				
				// refresh source and target connections of any shapes in the container not being considered for layout
				Iterator iter = containerEP.getChildren().iterator();
				while (iter.hasNext()) {
					Object obj = iter.next();
					if (!layoutSet.contains(obj) && obj instanceof IGraphicalEditPart) {
						IGraphicalEditPart ep = (IGraphicalEditPart)obj;
						ep.refresh();
					}
				}
				
				result = editParts;
			}
		};
		
		EditPartUtil.synchronizeRunnableToMainThread(containerEP, refreshRunnable);
		List editParts = (List)refreshRunnable.getResult();
		if (editParts == null || editParts.isEmpty()) {
			return CommandResult.newOKCommandResult();
		}
		
		//	add an arrange command, to layout the related shapes
		ArrangeRequest request = new ArrangeRequest(
			ActionIds.ACTION_ARRANGE_SELECTION, layoutType);
		request.setPartsToArrange(editParts);
		Command layoutCmd = containerEP.getCommand(request);

		if (layoutCmd != null && layoutCmd.canExecute()) {
            ICommand optimizedCommand = optimizeCommand(layoutCmd);
			optimizedCommand.execute(progressMonitor, info);
            optimizedCommand = null;
		}
		return CommandResult.newOKCommandResult();
	}

    private ICommand optimizeCommand(Command command) {
        if (command instanceof ICommandProxy) {
            ICommand icmd = ((ICommandProxy)command).getICommand();
            return icmd; 
        } else {
            if(command instanceof CompoundCommand) {
                CompositeTransactionalCommand transactionalCommand = new CompositeTransactionalCommand(getEditingDomain(), command.getLabel());
                transactionalCommand.setTransactionNestingEnabled(false);
                
                CompoundCommand compound = (CompoundCommand)command;
                Iterator commandIter = compound.getCommands().iterator();
                while(commandIter.hasNext()) {
                    Command iteredCommand = (Command)commandIter.next();
                    ICommand iteredICommand = optimizeCommand(iteredCommand);
                    transactionalCommand.compose(iteredICommand);
                }
                return transactionalCommand;
            }
            if (null != command)
            	return new CommandProxy(command);
            else 
            	return null;
        }
    }
	protected void cleanup() {
		containerEP = null;//for garbage collection
		viewAdapters = null;
		super.cleanup();
	}

	/**
	 * gets the container edit part's figure
	 * @return the container figure
	 */
	protected IFigure getContainerFigure() {
		return containerEP.getFigure();
	}

	/**
	 * gets the container edit part
	 * @return the container edit part
	 */
	protected IGraphicalEditPart getContainerEP() {
		return containerEP;
	}

	/**
	 * gets a list of <code>IAdaptable</code> that can adapt to <code>
	 * View</code>
	 * @return view adapters
	 */
	protected List getViewAdapters() {
		return viewAdapters;
	}
}