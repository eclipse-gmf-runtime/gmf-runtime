/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
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
import org.eclipse.gmf.runtime.notation.View;

/**
 * Command that sets a connections end points.
 * 
 * @author melaasar
 * 
 */
public class SetConnectionEndsCommand
	extends AbstractTransactionalCommand {

	private IAdaptable edgeAdaptor;

	private IAdaptable newSourceAdaptor;

	private IAdaptable newTargetAdaptor;

	/**
	 * constructor
	 * 
     * @param editingDomain
     *            the editing domain through which model changes are made
	 * @param label
	 *            the command label
	 */
	public SetConnectionEndsCommand(TransactionalEditingDomain editingDomain, String label) {
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
	 * @return the edge adapter
	 */
	public IAdaptable getEdgeAdaptor() {
		return edgeAdaptor;
	}

	/**
	 * gets the new source adaptor.
	 * 
	 * @return new source adaptor.
	 */
	public IAdaptable getNewSourceAdaptor() {
		return newSourceAdaptor;
	}

	/**
	 * gets the new target adaptor.
	 * 
	 * @return the new target adaptor.
	 */
	public IAdaptable getNewTargetAdaptor() {
		return newTargetAdaptor;
	}

	/**
	 * Sets the edge adaptor.
	 * 
	 * @param edgeAdaptor
	 *            the edgeAdaptor to set
	 */
	public void setEdgeAdaptor(IAdaptable edgeAdaptor) {
		this.edgeAdaptor = edgeAdaptor;
	}

	/**
	 * Sets the new source adaptor.
	 * 
	 * @param newSourceAdaptor
	 *            The newSourceAdaptor to set
	 */
	public void setNewSourceAdaptor(IAdaptable newSourceAdaptor) {
		this.newSourceAdaptor = newSourceAdaptor;
	}

	/**
	 * Sets the new target adaptor.
	 * 
	 * @param newTargetAdaptor
	 *            The newTargetAdaptor to set
	 */
	public void setNewTargetAdaptor(IAdaptable newTargetAdaptor) {
		this.newTargetAdaptor = newTargetAdaptor;
	}

	protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {
        
		assert null != edgeAdaptor : "Null child in SetConnectionEndsCommand";//$NON-NLS-1$

		Edge edge = (Edge) getEdgeAdaptor().getAdapter(Edge.class);
		assert null != edge : "Null edge in SetConnectionEndsCommand";//$NON-NLS-1$

		if (getNewSourceAdaptor() != null) {
			View newSourceView = (View) getNewSourceAdaptor().getAdapter(
				View.class);
			edge.setSource(newSourceView);
		}
		if (getNewTargetAdaptor() != null) {
			View newTargetView = (View) getNewTargetAdaptor().getAdapter(
				View.class);
			edge.setTarget(newTargetView);
		}

		setEdgeAdaptor(null);
		setNewSourceAdaptor(null);
		setNewTargetAdaptor(null);

		return CommandResult.newOKCommandResult();
	}

}
