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

package org.eclipse.gmf.runtime.diagram.ui.internal.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.RelativeBendpoints;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.datatype.RelativeBendpoint;

/**
 * @author melaasar
 * @canBeSeenBy %level1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SetConnectorBendpointsCommand extends AbstractModelCommand {
	private IAdaptable connectorAdapter;
	private PointList newPointList;
	private Point sourceRefPoint;
	private Point targetRefPoint;
	
	/**
	 * @see java.lang.Object#Object()
	 */
	public SetConnectorBendpointsCommand() {
		super(PresentationResourceManager.getI18NString("Commands.SetBendpointsCommand.Label"),  null); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#getAffectedObjects()
	 */
	public Collection getAffectedObjects() {
		View view = (View) connectorAdapter.getAdapter(View.class);
		if (view != null)
			return getWorkspaceFilesFor(view);
		return super.getAffectedObjects();
	}

	/**
	 * Returns the connectorAdaptor.
	 * @return IAdaptable
	 */
	public IAdaptable getConnectorAdaptor() {
		return connectorAdapter;
	}

	/**
	 * Returns the targetRefPoint.
	 * @return Point
	 */
	public Point getTargetRefPoint() {
		return targetRefPoint;
	}

	/**
	 * Returns the newPointList.
	 * @return PointList
	 */
	public PointList getNewPointList() {
		return newPointList;
	}

	/**
	 * Returns the sourceRefPoint.
	 * @return Point
	 */
	public Point getSourceRefPoint() {
		return sourceRefPoint;
	}

	/**
	 * Sets the connectorAdaptor.
	 * @param connectorAdapter The connectorAdaptor to set
	 */
	public void setConnectorAdapter(IAdaptable connectorAdapter) {
		this.connectorAdapter = connectorAdapter;
	}

	/**
	 * Method setNewPointList.
	 * @param newPointList
	 * @param sourceRefPoint
	 * @param targetRefPoint
	 */
	public void setNewPointList(
		PointList newPointList,
		Point sourceRefPoint,
		Point targetRefPoint) {
		this.newPointList = new PointList(newPointList.size());
		for (int i = 0; i < newPointList.size(); i++) {
			this.newPointList.addPoint(newPointList.getPoint(i));
		}
		this.sourceRefPoint = sourceRefPoint;
		this.targetRefPoint = targetRefPoint;
	}

	/**
	 * set a new point list
	 * @param newPointList	the new point list to set
	 * @param sourceAnchor	
	 * @param targetAnchor
	 */
	public void setNewPointList(
		PointList newPointList,
		ConnectionAnchor sourceAnchor,
		ConnectionAnchor targetAnchor) {

		this.newPointList = new PointList(newPointList.size());
		for (int i = 0; i < newPointList.size(); i++) {
			this.newPointList.addPoint(newPointList.getPoint(i));
		}
		if (sourceAnchor != null) {
			sourceRefPoint = sourceAnchor.getReferencePoint();
			sourceAnchor.getOwner().translateToRelative(sourceRefPoint);
		}
		if (targetAnchor != null) {
			targetRefPoint = targetAnchor.getReferencePoint();
			targetAnchor.getOwner().translateToRelative(
				targetRefPoint);
		}
	}

	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		Assert.isNotNull(newPointList);
		Assert.isNotNull(sourceRefPoint);
		Assert.isNotNull(targetRefPoint);

		Edge connectorView =
			(Edge) getConnectorAdaptor().getAdapter(Edge.class);
		Assert.isNotNull(connectorView);

		List newBendpoints = new ArrayList();
		int numOfPoints = newPointList.size();
		for (short i = 0; i < numOfPoints; i++) {
			Dimension s = newPointList.getPoint(i).getDifference(sourceRefPoint);
			Dimension t = newPointList.getPoint(i).getDifference(targetRefPoint);
			newBendpoints.add(new RelativeBendpoint(s.width, s.height, t.width, t.height));
		}

		RelativeBendpoints points = (RelativeBendpoints) connectorView.getBendpoints();
		points.setPoints(newBendpoints);
		return newOKCommandResult();
	}


}
