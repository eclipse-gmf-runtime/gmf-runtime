/******************************************************************************
 * Copyright (c) 2002, 2010 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.services.decorator;

import org.eclipse.draw2d.AncestorListener;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;

/**
 * This class wraps a decoration figure to provide capability common to all
 * decorations. It references the owner figure so that it can move with it. It
 * can use a locator for relocating the figure. The real decoration figure is
 * added to this figure.
 * 
 * @author cmahoney
 * @since 1.4
 */
public class Decoration
	extends Figure
	implements AncestorListener, IDecoration {

	/** locator used to put the decoration in the appropriate position */
	private Locator locator;

	/** the owner figure used for tracking movement */
	private IFigure owner;

	/**
	 * True if the decoration's visibility is not to be affected by the parent's
	 * visibility; false otherwise.
	 */
	private boolean ignoreParentVisibility;

	/**
     * Creates a new instance.
     */
    public Decoration() {
        super();
        setFocusTraversable(false);
    }

    /**
	 * Overridden to avoid having decorations show outside of resizeable
	 * compartment figures when they are not in the viewable area.
	 * 
	 * @see org.eclipse.draw2d.IFigure#isVisible()
	 */
	public boolean isVisible() {

		Rectangle decorationBounds = getBounds().getCopy();
		translateToAbsolute(decorationBounds);

		IFigure parentFigure = getOwnerFigure();
		while (parentFigure != null) {
			if (!shouldIgnoreParentVisibility() && !parentFigure.isVisible()) {
				return false;
			}
			if (parentFigure instanceof ResizableCompartmentFigure) {

				Rectangle parentClientArea = parentFigure.getClientArea()
					.getCopy();
				parentFigure.translateToAbsolute(parentClientArea);

				if (!(parentClientArea.contains(decorationBounds))) {
					return false;
				}

			}
			parentFigure = parentFigure.getParent();
		}
		return super.isVisible();
	}

	/**
	 * If a locator has been set, it is used to locate this figure.
	 * 
	 * @see org.eclipse.draw2d.IFigure#validate()
	 */
	public void validate() {
		if (getLocator() != null) {
			getLocator().relocate(this);
		}
		super.validate();
	}

	/**
	 * Gets the locator.
	 * 
	 * @return the locator
	 */
	public Locator getLocator() {
		return locator;
	}

	/**
	 * Sets the locator.
	 * 
	 * @param locator
	 *            the locator to set
	 */
	public void setLocator(Locator locator) {
		this.locator = locator;
	}

	/**
	 * Gets the owner figure.
	 * 
	 * @return the owner figure
	 */
	public IFigure getOwnerFigure() {
		return owner;
	}

	/**
	 * Sets the owner figure.
	 * 
	 * @param figure
	 *            the owner figure
	 */
	public void setOwnerFigure(IFigure figure) {
		owner = figure;
	}

	/**
	 * @see org.eclipse.draw2d.IFigure#addNotify()
	 */
	public void addNotify() {
		super.addNotify();

		// Listen to the owner figure so the decoration moves when the
		// figure moves.
		getOwnerFigure().addAncestorListener(this);
	}

	/**
	 * @see org.eclipse.draw2d.IFigure#removeNotify()
	 */
	public void removeNotify() {
		getOwnerFigure().removeAncestorListener(this);
		super.removeNotify();
	}

	/**
	 * @see org.eclipse.draw2d.AncestorListener#ancestorMoved(org.eclipse.draw2d.IFigure)
	 */
	public void ancestorMoved(IFigure ancestor) {
		revalidate();
	}

	/**
	 * @see org.eclipse.draw2d.AncestorListener#ancestorAdded(org.eclipse.draw2d.IFigure)
	 */
	public void ancestorAdded(IFigure ancestor) {
		// NULL implementation
	}

	/**
	 * @see org.eclipse.draw2d.AncestorListener#ancestorRemoved(org.eclipse.draw2d.IFigure)
	 */
	public void ancestorRemoved(IFigure ancestor) {
		// NULL implementation
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoration#setIgnoreParentVisibility(boolean)
	 */
	public void setIgnoreParentVisibility(boolean ignoreParentVisibility) {
		this.ignoreParentVisibility = ignoreParentVisibility;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoration#shouldIgnoreParentVisibility()
	 */
	public boolean shouldIgnoreParentVisibility() {
		return ignoreParentVisibility;
	}

}