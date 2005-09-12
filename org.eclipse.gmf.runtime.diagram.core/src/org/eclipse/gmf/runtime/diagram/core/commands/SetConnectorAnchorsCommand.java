/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.commands;

import java.util.Collection;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.IdentityAnchor;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Sets a connectors end points.
 * @author melaasar
 *
 */
public class SetConnectorAnchorsCommand extends AbstractModelCommand {

	private IAdaptable connectorAdaptor;
	private String newSourceTerminal;
	private String newTargetTerminal;
	
	/**
	 * constructor 
	 * @param label	the command label
	 */
	public SetConnectorAnchorsCommand(String label) {
		super(label, null);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#getAffectedObjects()
	 */
	public Collection getAffectedObjects() {
		View view = (View) connectorAdaptor.getAdapter(View.class);
		if (view != null)
			return getWorkspaceFilesFor(view);
		return super.getAffectedObjects();
	}

	/**
	 * gets the connector adaptor.
	 * @return IAdaptable	the connector adapter
	 */
	public IAdaptable getConnectorAdaptor() {
		return connectorAdaptor;
	}

	/**
	 * gets for the new source terminal.
	 * @return String the newSourceTerminal.
	 */
	public String getNewSourceTerminal() {
		return newSourceTerminal;
	}

	/**
	 * gets for the new target terminal.
	 * @return String the newTargetTerminal
	 */
	public String getNewTargetTerminal() {
		return newTargetTerminal;
	}


	/**
	 * Sets the connector adaptor.
	 * @param connectorAdaptor The connectorAdaptor to set
	 */
	public void setConnectorAdaptor(IAdaptable connectorAdaptor) {
		this.connectorAdaptor = connectorAdaptor;
	}

	/**
	 * Sets the new source terminal.
	 * @param newSourceTerminal The new source terminal to set
	 */
	public void setNewSourceTerminal(String newSourceTerminal) {
		this.newSourceTerminal = newSourceTerminal;
	}

	/**
	 * Sets the new target terminal.
	 * @param newTargetTerminal The new target terminal to set
	 */
	public void setNewTargetTerminal(String newTargetTerminal) {
		this.newTargetTerminal = newTargetTerminal;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		assert null != connectorAdaptor : "Null child in SetConnectorAnchorsCommand";//$NON-NLS-1$		

		Edge connectorView = (Edge) getConnectorAdaptor().getAdapter(Edge.class);
		assert null != connectorView : "Null connectorView in SetConnectorAnchorsCommand";//$NON-NLS-1$		

		if (getNewSourceTerminal() != null) {
			if (getNewSourceTerminal().length() == 0)
				connectorView.setSourceAnchor(null);
			else {
				IdentityAnchor a = (IdentityAnchor) connectorView.getSourceAnchor();
				if (a == null)
					a = NotationFactory.eINSTANCE.createIdentityAnchor();
				a.setId(getNewSourceTerminal());
				connectorView.setSourceAnchor(a);
			}
		}
		if (getNewTargetTerminal() != null) {
			if (getNewTargetTerminal().length() == 0)
				connectorView.setTargetAnchor(null);
			else {
				IdentityAnchor a = (IdentityAnchor) connectorView.getTargetAnchor();
				if (a == null)
					a = NotationFactory.eINSTANCE.createIdentityAnchor();
				a.setId(getNewTargetTerminal());
				connectorView.setTargetAnchor(a);
			}
			
		}
		return newOKCommandResult();
	}

}
