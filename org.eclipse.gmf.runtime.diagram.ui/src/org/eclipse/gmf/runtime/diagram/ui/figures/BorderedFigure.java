/******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.figures;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.util.DrawConstant;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.AnimationFigureHelper;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;

/**
 * Wrapper that contains the real figure and the border items. This is required
 * to allow the main figure's layout manager to ignore the border items. The
 * border item container applies a delegating layout manager to allow border
 * item children to lay themselves out.
 * 
 * @author jbruck
 */
public class BorderedFigure
	extends BorderItemFigure {

	private BorderItemContainerFigure borderItemContainer;

	private IFigure elementFigure;

	/**
	 * Creates a new ClassiferNodeFigire figure.
	 * 
	 * @param elementFig
	 *            the figure to use with this figure
	 */
	public BorderedFigure(IFigure elementFig) {
		super(DrawConstant.INVALID);
		if (elementFig instanceof BorderItemFigure) {
			setPreferredSide(((BorderItemFigure) elementFig).getPreferredSide());
		}

		setOpaque(false); // set transparent by default
		setBorder(null);
		setLayoutManager(null);
		this.elementFigure = elementFig;

		add(getElementPane());
		add(getBorderItemContainer());

		setBounds(elementFig.getBounds().getCopy());
		getBorderItemContainer().setBounds(new Rectangle(0, 0, 1, 1));
	}

	/**
	 * For animated layout.
	 * 
	 * @author jbruck
	 */
	public class AnimatableDelegatingLayout
		extends DelegatingLayout {

		public void layout(IFigure container) {
			if (!AnimationFigureHelper.getInstance().layoutManagerHook(
				container)) {
				super.layout(container);
			}
		}
	}

	/**
	 * 
	 * @return The border item container figure
	 */
	public BorderItemContainerFigure getBorderItemContainer() {
		if (borderItemContainer == null) {
			borderItemContainer = new BorderItemContainerFigure();
			borderItemContainer
				.setLayoutManager(new AnimatableDelegatingLayout());
			borderItemContainer.setVisible(true);
		}
		return borderItemContainer;
	}

	/**
	 * 
	 * @return The "main" figure
	 */
	public IFigure getElementPane() {
		return elementFigure;
	}

	public Rectangle getClientArea(Rectangle rect) {
		if (elementFigure != null) {
			return elementFigure.getClientArea(rect);
		}
		return super.getClientArea(rect);
	}

	/**
	 * gets the handle bounds of the main figure
	 * 
	 * @return the hnalde bounds
	 */
	public Rectangle getHandleBounds() {
		if (elementFigure instanceof NodeFigure) {
			return ((NodeFigure) elementFigure).getHandleBounds().getCopy();
		} else {
			return elementFigure.getBounds().getCopy();
		}
	}

	/**
	 * Give the main figure the entire bounds of the wrapper.
	 */
	protected void layout() {
		if (!this.getBounds().equals(elementFigure.getBounds())) {
			elementFigure.setBounds(this.getBounds().getCopy());
		}
		// When parent resizes, cause the border items to be relocated.
		getBorderItemContainer().invalidateTree();
		erase();
	}

	/**
	 * We need to override this for smooth painting of border items.
	 */
	public boolean containsPoint(int x, int y) {
		if (borderItemContainer.containsPoint(x, y)) {
			return true;
		}
		return super.containsPoint(x, y);
	}

	protected void primTranslate(int dx, int dy) {
		super.primTranslate(dx, dy);
		erase();
	}

	public void erase() {
		super.erase();
		if (borderItemContainer != null)
			borderItemContainer.erase();
	}

	/**
	 * Refresh adornments
	 */
	public void repaint() {
		super.repaint();
		if (borderItemContainer != null)
			borderItemContainer.repaint();
	}

	public IFigure findFigureAt(int x, int y, TreeSearch search) {
		IFigure result = borderItemContainer.findFigureAt(x, y, search);
		if (result != null) {
			return result;
		}
		return elementFigure.findFigureAt(x, y, search);
	}

	public IFigure findMouseEventTargetAt(int x, int y) {
		IFigure borderItemFigure = borderItemContainer.findMouseEventTargetAt(
			x, y);
		if (borderItemFigure != null)
			return borderItemFigure;
		return super.findMouseEventTargetAt(x, y);
	}

	public boolean intersects(Rectangle rect) {
		if (borderItemContainer.intersects(rect)) {
			return true;
		}
		return super.intersects(rect);
	}

	public Dimension getMinimumSize(int wHint, int hHint) {
		return elementFigure.getMinimumSize(wHint, hHint);
	}

	public Dimension getPreferredSize(int wHint, int hHint) {
		return elementFigure.getPreferredSize(wHint, hHint);
	}

	public IFigure getToolTip() {
		return elementFigure.getToolTip();
	}

	public void setToolTip(IFigure f) {
		elementFigure.setToolTip(f);
	}

	/**
	 * Returns a new anchor for this node figure.
	 * 
	 * @param p
	 *            Point on the figure that gives a hint which anchor to return.
	 * @return ConnectionAnchor reference to an anchor associated with the given
	 *         point on the figure.
	 */
	public ConnectionAnchor getSourceConnectionAnchorAt(Point p) {
		if (elementFigure instanceof NodeFigure)
			return ((NodeFigure) elementFigure).getSourceConnectionAnchorAt(p);
		return super.getSourceConnectionAnchorAt(p);
	}

	/*
	 * @see org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure#getTargetConnectionAnchorAt(org.eclipse.draw2d.geometry.Point)
	 */
	public ConnectionAnchor getTargetConnectionAnchorAt(Point p) {
		if (elementFigure instanceof NodeFigure)
			return ((NodeFigure) elementFigure).getTargetConnectionAnchorAt(p);
		return super.getTargetConnectionAnchorAt(p);
	}

	/*
	 * @see org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure#getConnectionAnchor(java.lang.String)
	 */
	public ConnectionAnchor getConnectionAnchor(String terminal) {
		if (elementFigure instanceof NodeFigure)
			return ((NodeFigure) elementFigure).getConnectionAnchor(terminal);
		return super.getConnectionAnchor(terminal);
	}

	/*
	 * @see org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure#getConnectionAnchorTerminal(org.eclipse.draw2d.ConnectionAnchor)
	 */
	public String getConnectionAnchorTerminal(ConnectionAnchor c) {
		if (elementFigure instanceof NodeFigure)
			return ((NodeFigure) elementFigure).getConnectionAnchorTerminal(c);
		return super.getConnectionAnchorTerminal(c);
	}

}
