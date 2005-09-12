/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.commands;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import org.eclipse.gmf.runtime.notation.View;


/**
 * @author melaasar
 *
 * A command to set the bounds (location/size) of a <code>View</code>
 */
public class SetBoundsCommand extends AbstractModelCommand {

	private IAdaptable  adapter;
	private Point location;
	private Dimension size;
	
	/**
	 * Creates a <code>SetBoundsCommand</code> for the given view adapter with a given bounds.
	 * 
	 * @param label The command label
	 * @param adapter An adapter to the <code>View</code>
	 * @param bounds The new bounds
	 */
	public SetBoundsCommand(String label,IAdaptable adapter, Rectangle bounds) {
		super(label,adapter);
		Assert.isNotNull(adapter, "view cannot be null"); //$NON-NLS-1$
		Assert.isNotNull(bounds, "bounds cannot be null"); //$NON-NLS-1$
		this.adapter = adapter;
		this.location = bounds.getLocation();
		this.size = bounds.getSize();
	}
	
	/**
	 * Creates a <code>SetBoundsCommand</code> for the given view adapter with a given location.
	 * 
	 * @param label The command label
	 * @param adapter An adapter to the <code>View</code>
	 * @param location The new location
	 */
	public SetBoundsCommand (String label,IAdaptable adapter, Point location) {
		super(label,adapter);
		Assert.isNotNull(adapter, "view cannot be null"); //$NON-NLS-1$
		Assert.isNotNull(location, "location cannot be null"); //$NON-NLS-1$
		this.adapter = adapter;
		this.location = location;
	}

	/**
	 * Creates a <code>SetBoundsCommand</code> for the given view adapter with a given size.
	 * 
	 * @param label The command label
	 * @param adapter An adapter to the <code>View</code>
	 * @param size The new size
	 */
	public SetBoundsCommand (String label, IAdaptable adapter, Dimension size) {
		super(label, adapter);
		Assert.isNotNull(adapter, "view cannot be null"); //$NON-NLS-1$
		Assert.isNotNull(size, "size cannot be null"); //$NON-NLS-1$
		this.adapter = adapter;
		this.size = size;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		if (adapter == null)
			return newErrorCommandResult("SetBoundsCommand: viewAdapter does not adapt to IView.class"); //$NON-NLS-1$
		
		View view  = (View)adapter.getAdapter(View.class);
		
		if (location != null) {
			ViewUtil.setPropertyValue(view,Properties.ID_POSITIONX, new Integer(location.x));
			ViewUtil.setPropertyValue(view,Properties.ID_POSITIONY, new Integer(location.y));
		}
		if (size != null) {
			ViewUtil.setPropertyValue(view,Properties.ID_EXTENTX, new Integer(size.width));
			ViewUtil.setPropertyValue(view,Properties.ID_EXTENTY, new Integer(size.height));
		}
		return newOKCommandResult();
	}
}
