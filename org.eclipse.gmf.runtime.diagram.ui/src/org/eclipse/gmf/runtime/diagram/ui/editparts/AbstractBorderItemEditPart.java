/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.common.ui.services.parser.CommonParserHint;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.BorderItemSelectionEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.figures.IBorderItemLocator;
import org.eclipse.gmf.runtime.diagram.ui.tools.DragEditPartsTrackerEx;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * The class controls the behavior of a border item. It determines the
 * connections coming in and out. Created On: Jul 8, 2003
 * 
 * @author tisrar, jbruck, cmahoney
 */
public abstract class AbstractBorderItemEditPart
	extends ShapeNodeEditPart
	implements IBorderItemEditPart {

	/**
	 * Create an instance.
	 * 
	 * @param view
	 *            the editpart's model.
	 */
	public AbstractBorderItemEditPart(View view) {
		super(view);
	}

	/**
	 * Refresh the bounds using a <tt>locator</tt> if this editpart's lcoator
	 * is a BorderItemLocatorinstance; otherwise, the <tt>super</tt>
	 * implementation is used. Locators are used since a
	 * <tt>border item element</tt> 's position and extent properties are not
	 * persisted.
	 */
	protected void refreshBounds() {
		if (getBorderItemLocator() != null) {
			int x = ((Integer) getStructuralFeatureValue(NotationPackage.eINSTANCE
				.getLocation_X())).intValue();
			int y = ((Integer) getStructuralFeatureValue(NotationPackage.eINSTANCE
				.getLocation_Y())).intValue();
			Point loc = new Point(x, y);
			
			int width = ((Integer) getStructuralFeatureValue(NotationPackage.eINSTANCE.getSize_Width())).intValue();
			int height = ((Integer) getStructuralFeatureValue(NotationPackage.eINSTANCE.getSize_Height())).intValue();
			Dimension size = new Dimension(width, height);

			getBorderItemLocator().setConstraint(new Rectangle(
				loc, size));
		} else {
			super.refreshBounds();
		}
	}

	/**
	 * Convenience method to return the locator associated with editpart's
	 * figure via the parent figure's layout manager.
	 * 
	 * @return the <code>Locator</code>
	 */
	public IBorderItemLocator getBorderItemLocator() {
		IFigure parentFigure = getFigure().getParent();
		if (parentFigure != null && parentFigure.getLayoutManager() != null) {
			Object constraint = parentFigure.getLayoutManager().getConstraint(
				getFigure());
			if (constraint instanceof IBorderItemLocator) {
				return (IBorderItemLocator) constraint;
			}
		}
		return null;
	}

	/**
	 * Return the editpolicy to be installed as an
	 * <code>EditPolicy#PRIMARY_DRAG_ROLE</code> role. This method is
	 * typically called by <code>LayoutEditPolicy#createChildEditPolicy()</code>
	 * 
	 * @return <code>EditPolicy</code>
	 */
	public EditPolicy getPrimaryDragEditPolicy() {
		return new BorderItemSelectionEditPolicy();
	}

	/**
	 * this method will return the primary child EditPart inside this edit part
	 * 
	 * @return the primary child view inside this edit part
	 */
	public EditPart getPrimaryChildEditPart() {
		return getChildBySemanticHint(CommonParserHint.NAME);
	}

	/** Return a {@link DragTracker} instance. */
	public DragTracker getDragTracker(Request request) {
		return new DragEditPartsTrackerEx(this) {

			protected boolean isMove() {
				return true;
			}
		};
	}

}
