/******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.diagram.ui.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.layout.IInternalLayoutRunnable;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.layout.LayoutNode;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutService;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

/**
 * The arrange command implementation
 * <li> Delegates to the layout provider only during the execution of the command.
 * <li> {@link #canExecute()} asks the layout service whether it can layout the nodes
 * (e.g some layouts won't layout if nodes are laid out already)    
 * 
 * @author aboyko
 * @since 1.4
 */
final public class ArrangeCommand extends AbstractTransactionalCommand {
	
	/**
	 * Nodes to layout
	 */
	private List<LayoutNode> nodes;
	
	/**
	 * <code>true</code> if only a part of the graph is being laid out,
	 * <code>false</code> - the whole graph is being laid out
	 */
	private boolean selectionArrange;
	
	/**
	 * The layout hint parameter
	 */
	private IAdaptable layoutHint;

	/**
	 * Constraucts an instance
	 * 
	 * @param domain
	 *            edititng domain
	 * @param label
	 *            command label
	 * @param affectedFiles
	 *            list of affected files
	 * @param editparts
	 *            editoparts to be arranged
	 * @param layoutHint
	 *            the layout hint parameter
	 * @param selectionArrange
	 *            <code>true</code> for part of the graph to be laid out and
	 *            <code>false</code> for the whole graph to be laid out
	 */
	public ArrangeCommand(TransactionalEditingDomain domain, String label,
			List affectedFiles, Collection<IGraphicalEditPart> editparts,
			IAdaptable layoutHint, boolean selectionArrange) {
		super(domain, label, affectedFiles);
		this.layoutHint = layoutHint;
		this.selectionArrange = selectionArrange;
		initLayoutNodes(editparts);
	}
	
	@Override
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
			IAdaptable info) throws ExecutionException {
		ICommand cmd = getCommandForExecution();
		if (cmd.canExecute()) {
			cmd.execute(monitor, info);
		}
		return CommandResult.newOKCommandResult();
	}
	
	/**
	 * Creates the list of nodes to layout from the editparts required to be arranged
	 * 
	 * @param editparts the editparts required to be arranged
	 */
	private void initLayoutNodes(Collection<IGraphicalEditPart> editparts) {
        nodes = new ArrayList<LayoutNode>(editparts.size());
        Iterator<IGraphicalEditPart> li = editparts.iterator();     
        while (li.hasNext()) {
            IGraphicalEditPart ep = li.next();      
            View view = ep.getNotationView();
            if (ep.isActive() && view != null && view instanceof Node) {
                Rectangle bounds = ep.getFigure().getBounds();
                nodes.add(new LayoutNode((Node)view, bounds.width, bounds.height));
            }
        }
	}
	
	/**
	 * Creates the actual layout command to execute. The command is created based on the {@link LayoutService} 
	 * 
	 * @return the actual graph layout command 
	 */
	private ICommand getCommandForExecution() {
        final Runnable layoutRun = LayoutService.getInstance().layoutLayoutNodes(nodes, selectionArrange, layoutHint);
        
        TransactionalEditingDomain editingDomain = getEditingDomain(); 
        CompositeTransactionalCommand ctc = new CompositeTransactionalCommand(editingDomain, StringStatics.BLANK);        
        if (layoutRun instanceof IInternalLayoutRunnable) {
        	Command cmd = ((IInternalLayoutRunnable) layoutRun).getCommand();
        	if (cmd != null) {
        		ctc.add(new CommandProxy(cmd));
        	}
        }
        else {
            ctc.add(new AbstractTransactionalCommand(editingDomain, StringStatics.BLANK, null) {
                protected CommandResult doExecuteWithResult(
                            IProgressMonitor progressMonitor, IAdaptable info)
                        throws ExecutionException {
                    layoutRun.run();
                    return CommandResult.newOKCommandResult();
                }
            });     
        }       
        return ctc;
	}

	@Override
	public boolean canExecute() {
		return LayoutService.getInstance().canLayoutNodes(nodes, selectionArrange, layoutHint);
	}

}
