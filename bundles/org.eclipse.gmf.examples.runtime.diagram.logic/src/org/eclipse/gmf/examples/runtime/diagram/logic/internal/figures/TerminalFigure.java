/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemLocator;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;

/**
 * @author qili
 *
 * To manage fixed connection anchors
 */
public class TerminalFigure extends NodeFigure{
	
	protected FixedConnectionAnchor fixedAnchor;
	protected Dimension prefSize;
	
	/**
	 * @author sshaw
	 *
	 * Override for BorderItemLocator that will fix the location for the connection point based on 
	 * an initial position.  This locator will also scale the location of the connection if the
	 * parent figure changes.
	 */
	static public class TerminalLocator extends BorderItemLocator {
		 
		private Dimension initDim; 
		public void relocate(IFigure target) {
			Rectangle parentRect = getParentBorder();
			float xRatio = parentRect.width / (float)initDim.width;
			float yRatio = parentRect.height / (float)initDim.height;
			
			Rectangle targetRect = target.getBounds();
			Point ptLoc = this.getAbsoluteToBorder(getConstraint().getLocation());
			ptLoc = ptLoc.getTranslated(-parentRect.x, -parentRect.y);
			ptLoc.scale(xRatio, yRatio);
			ptLoc = ptLoc.getTranslated(parentRect.x, parentRect.y);
			
			target.setBounds(new Rectangle(ptLoc.x - targetRect.width / 2, ptLoc.y - targetRect.height / 2, targetRect.width, targetRect.height));
		}
		
		/**
		 * @param gate
		 * @param parentFigure
		 */
		public TerminalLocator(IFigure parentFigure, Dimension initDim, int side) {
			super(parentFigure, side);
			this.initDim = initDim;
		}
	}
	
	/**
	 * @param preferredSide
	 */
	public TerminalFigure(Dimension prefSize) {
		super();
		this.prefSize = new Dimension(prefSize);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure#getSourceConnectionAnchorAt(org.eclipse.draw2d.geometry.Point)
	 */
	public ConnectionAnchor getSourceConnectionAnchorAt(Point p) {
		if (p == null) {
			return getConnectionAnchor(szAnchor);
		}
		return fixedAnchor;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure#getTargetConnectionAnchorAt(org.eclipse.draw2d.geometry.Point)
	 */
	public ConnectionAnchor getTargetConnectionAnchorAt(Point p) {
		if (p == null) {
			return getConnectionAnchor(szAnchor);
		}
		return fixedAnchor;
	}

	/**
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		return new Dimension(prefSize);
	}
}
