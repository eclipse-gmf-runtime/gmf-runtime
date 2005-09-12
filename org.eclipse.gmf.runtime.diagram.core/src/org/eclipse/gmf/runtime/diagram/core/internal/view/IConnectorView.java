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

package org.eclipse.gmf.runtime.diagram.core.internal.view;

import org.eclipse.gmf.runtime.notation.Anchor;
import org.eclipse.gmf.runtime.notation.Bendpoints;

/**
 * @deprecated View Facades are deprectaed and will be removed soon; use the 
 * notation view instead; you can reach it by calling getModel on a view EditPart
 * or by using the view service to create a new view
 * A facade inteface for all connector views
 * @author melaasar
 * @canBeSeenBy %level1
 */
public interface IConnectorView  {

	/**
	 *Resolves the source view of the connector
	 * @return IConnectableView the source view (if unresovable, <code>null</code> is returned)
	 */
	public IView getSourceView();
	
	/**
	 * Resolves the destination view of the connector
	 * @return IConnectableView the target view (if unresovable, <code>null</code> is returned)
	 */
	public IView getTargetView();
	
	/**
	 * Sets the source view of the connector
	 * @param view the source view (can be <code>null</code>)
	 */
	public void setSourceView(IView view);
	
	/**
	 * Sets the target view of the connector
	 * @param view the target view (can be <code>null</code>)
	 */
	public void setTargetView(IView view);

	/**
	 * Gets the connector's source anchor
	 * @return The connector source <code>Anchor</code>
	 */
	public Anchor getSourceAnchor();
	
	/**
	 * Gets the connector's target anchor
	 * @return The connector target <code>Anchor</code>
	 */
	public Anchor getTargetAnchor();
	
	/**
	 * Sets the connector's source anchor
	 * @param anchor The connector source <code>Anchor</code>
	 */
	public void setSourceAnchor(Anchor anchor);
	
	/**
	 * Sets the target view of the connector
	 * @param view the target view (can be <code>null</code>)
	 */
	public void setTargetAnchor(Anchor anchor);

	/**
	 * Method getBendpoints.
	 * gets the connector's bendpoints
	 * @return Bendpoints
	 */
	public Bendpoints getBendpoints();
	
}
