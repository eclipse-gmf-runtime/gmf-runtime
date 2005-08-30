/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.commands;

import java.util.Collection;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import com.ibm.xtools.notation.Edge;
import com.ibm.xtools.notation.View;

/**
 * Command that sets a connectors end points.
 * @author melaasar
 *
 */
public class SetConnectorEndsCommand extends AbstractModelCommand {

	private IAdaptable connectorAdaptor;
	private IAdaptable newSourceAdaptor;
	private IAdaptable newTargetAdaptor;
	
	/**
	 * constructor
	 * @param label the command label
	 */
	public SetConnectorEndsCommand(String label) {
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
	 * @return the connector adapter
	 */
	public IAdaptable getConnectorAdaptor() {
		return connectorAdaptor;
	}

	/**
	 * gets the new source adaptor.
	 * @return new source adaptor.
	 */
	public IAdaptable getNewSourceAdaptor() {
		return newSourceAdaptor;
	}

	/**
	 * gets the new target adaptor.
	 * @return the new target adaptor.
	 */
	public IAdaptable getNewTargetAdaptor() {
		return newTargetAdaptor;
	}

	/**
	 * Sets the connector adaptor.
	 @param connectorAdaptor the connectoradaptor to set
	 */
	public void setConnectorAdaptor(IAdaptable connectorAdaptor) {
		this.connectorAdaptor = connectorAdaptor;
	}

	/**
	 * Sets the new source adaptor.
	 * @param newSourceAdaptor The newSourceAdaptor to set
	 */
	public void setNewSourceAdaptor(IAdaptable newSourceAdaptor) {
		this.newSourceAdaptor = newSourceAdaptor;
	}

	/**
	 * Sets the new target adaptor.
	 * @param newTargetAdaptor The newTargetAdaptor to set
	 */
	public void setNewTargetAdaptor(IAdaptable newTargetAdaptor) {
		this.newTargetAdaptor = newTargetAdaptor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {		
		assert null != connectorAdaptor : "Null child in SetConnectorEndsCommand";//$NON-NLS-1$

		Edge connectorView = (Edge) getConnectorAdaptor().getAdapter(Edge.class);
		assert null != connectorView : "Null connectorView in SetConnectorEndsCommand";//$NON-NLS-1$

		if (getNewSourceAdaptor() != null) {
			View newSourceView = (View) getNewSourceAdaptor().getAdapter(View.class);
			connectorView.setSource(newSourceView);
		}
		if (getNewTargetAdaptor() != null) {
			View newTargetView = (View) getNewTargetAdaptor().getAdapter(View.class);
			connectorView.setTarget(newTargetView);
		}
		return newOKCommandResult();
	}

}
