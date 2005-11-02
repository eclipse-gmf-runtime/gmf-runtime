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

package org.eclipse.gmf.runtime.diagram.ui.internal.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.swt.graphics.Image;

import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramResourceManager;

/**
 * @author sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 *
 * Figure used to represent the collapse handle in the corner of a list compartment or shape compartment.
 */
public class CollapseFigure extends RectangleFigure {

	private boolean collapsed = false;
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Shape#fillShape(org.eclipse.draw2d.Graphics)
	 */
	protected void fillShape(Graphics graphics) {
		
		Image img;
		
		if (isCollapsed())
			img = DiagramResourceManager.getInstance().getImage(DiagramResourceManager.IMAGE_HANDLE_EXPAND);
		else
			img = DiagramResourceManager.getInstance().getImage(DiagramResourceManager.IMAGE_HANDLE_COLLAPSE);
		
		graphics.drawImage(img, getBounds().x, getBounds().y);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Shape#outlineShape(org.eclipse.draw2d.Graphics)
	 */
	protected void outlineShape(Graphics graphics) {
		// do nothing
	}
	
	/**
	 * isCollapsed
	 * Utility method to determine if the IFigure is collapse or not.
	 * 
	 * @return true if collapse, false otherwise.
	 */
	public boolean isCollapsed() {
		return collapsed;
	}

	/**
	 * setCollapsed
	 * Setter method to change collapsed state of the figure.  Will force update
	 * to repaint the figure to reflect the changes.
	 * 
	 * @param b boolean true to set collapsed, false to uncollapse.
	 */
	public void setCollapsed(boolean b) {
		collapsed = b;
		revalidate();
		repaint();
	}

}
