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

package org.eclipse.gmf.runtime.diagram.core.commands;

import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.IdentityAnchor;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Sets a connections end points.
 * 
 * @author melaasar
 * 
 */
public class SetConnectionAnchorsCommand
	extends AbstractTransactionalCommand {

	private IAdaptable edgeAdaptor;

	private String newSourceTerminal;

	private String newTargetTerminal;

	/**
	 * constructor
	 * 
     * @param editingDomain
     *            the editing domain through which model changes are made
	 * @param label
	 *            the command label
	 */
	public SetConnectionAnchorsCommand(TransactionalEditingDomain editingDomain, String label) {
		super(editingDomain, label, null);
	}

	public List getAffectedFiles() {
		View view = (View) edgeAdaptor.getAdapter(View.class);
		if (view != null)
			return getWorkspaceFiles(view);
		return super.getAffectedFiles();
	}

	/**
	 * gets the edge adaptor.
	 * 
	 * @return IAdaptable the edge adapter
	 */
	public IAdaptable getEdgeAdaptor() {
		return edgeAdaptor;
	}

	/**
	 * gets for the new source terminal.
	 * 
	 * @return String the newSourceTerminal.
	 */
	public String getNewSourceTerminal() {
		return newSourceTerminal;
	}

	/**
	 * gets for the new target terminal.
	 * 
	 * @return String the newTargetTerminal
	 */
	public String getNewTargetTerminal() {
		return newTargetTerminal;
	}

	/**
	 * Sets the edge adaptor.
	 * 
	 * @param edgeAdaptor
	 *            The edgeAdaptor to set
	 */
	public void setEdgeAdaptor(IAdaptable edgeAdaptor) {
		this.edgeAdaptor = edgeAdaptor;
	}

	/**
	 * Sets the new source terminal.
	 * 
	 * @param newSourceTerminal
	 *            The new source terminal to set
	 */
	public void setNewSourceTerminal(String newSourceTerminal) {
		this.newSourceTerminal = newSourceTerminal;
	}

	/**
	 * Sets the new target terminal.
	 * 
	 * @param newTargetTerminal
	 *            The new target terminal to set
	 */
	public void setNewTargetTerminal(String newTargetTerminal) {
		this.newTargetTerminal = newTargetTerminal;
	}

	protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {
        
		assert null != edgeAdaptor : "Null child in SetConnectionAnchorsCommand";//$NON-NLS-1$		

		Edge edge = (Edge) getEdgeAdaptor().getAdapter(Edge.class);
		assert null != edge : "Null edge in SetConnectionAnchorsCommand";//$NON-NLS-1$		

		if (getNewSourceTerminal() != null) {
			if (getNewSourceTerminal().length() == 0)
				edge.setSourceAnchor(null);
			else {
				IdentityAnchor a = (IdentityAnchor) edge.getSourceAnchor();
				if (a == null)
					a = NotationFactory.eINSTANCE.createIdentityAnchor();
				a.setId(getNewSourceTerminal());
				edge.setSourceAnchor(a);
			}
		}
		if (getNewTargetTerminal() != null) {
			if (getNewTargetTerminal().length() == 0)
				edge.setTargetAnchor(null);
			else {
				IdentityAnchor a = (IdentityAnchor) edge.getTargetAnchor();
				if (a == null)
					a = NotationFactory.eINSTANCE.createIdentityAnchor();
				a.setId(getNewTargetTerminal());
				edge.setTargetAnchor(a);
			}

		}
		return CommandResult.newOKCommandResult();
	}

}
