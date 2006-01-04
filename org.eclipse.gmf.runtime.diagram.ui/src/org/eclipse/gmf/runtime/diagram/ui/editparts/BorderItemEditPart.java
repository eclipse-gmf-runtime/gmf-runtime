/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import java.util.Collection;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.common.ui.services.parser.CommonParserHint;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.BorderItemSelectionEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ConnectionLabelsEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemContainerFigure;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemFigure;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderedFigure;
import org.eclipse.gmf.runtime.diagram.ui.figures.IBorderItemLocator;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.tools.DragEditPartsTrackerEx;
import org.eclipse.gmf.runtime.diagram.ui.util.DrawConstant;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * The class controls the behavior of a border item. It determines the
 * connections coming in and out. Created On: Jul 8, 2003
 * 
 * @author tisrar
 * @author jbruck
 * @deprecated 01/04/2006 See API change documentation in bugzilla 111935
 *             (https://bugs.eclipse.org/bugs/show_bug.cgi?id=111935)
 */
public class BorderItemEditPart
	extends ShapeNodeEditPart implements IBorderItemEditPart { // inherit from BorderedShapeEditPart eventually

	/**
	 * Create an instance.
	 * 
	 * @param view
	 *            the editpart's model.
	 */
	public BorderItemEditPart(View view) {
		super(view);
	}

	/**
	 * Refresh the bounds using a <tt>locator</tt> if this editpart's figure
	 * is a {@link BorderItemFigure}instance; otherwise, the <tt>super</tt>
	 * implementation is used. Locators are used since a
	 * <tt>border item element</tt> 's position and extent properties are not
	 * persisted.
	 */
	protected void refreshBounds() {
		if (getFigure() instanceof BorderItemFigure) {
			BorderItemFigure.BorderItemLocator locator = (BorderItemFigure.BorderItemLocator) getLocator();
			if (locator != null) {
				int x = ((Integer) getStructuralFeatureValue(NotationPackage.eINSTANCE
					.getLocation_X())).intValue();
				int y = ((Integer) getStructuralFeatureValue(NotationPackage.eINSTANCE
					.getLocation_Y())).intValue();
				Point loc = new Point(x, y);
				locator.resetPosition(new Rectangle(loc, getFigure()
					.getPreferredSize()));
			} else {
				super.refreshBounds();
			}
		} else {
			super.refreshBounds();
		}
	}

	/** Create a border item figure. */
	protected NodeFigure createNodeFigure() {
		return new BorderItemFigure(DrawConstant.EAST);
	}

	/**
	 * Creates this editpart's main figure.
	 * 
	 * @return the created <code>NodeFigure</code>
	 */
	protected NodeFigure createMainFigure() {
		return new BorderItemFigure(DrawConstant.EAST);
	}

	/**
	 * sets the locator for the figure. This locator locates the figure when
	 * ever the validation is called on the shape.
	 * 
	 * @param locator
	 *            The locator which will be called when the parent figure is
	 *            validated.
	 */
	public void setLocator(Locator locator) {
		((BorderItemFigure) getFigure()).setLocator(locator);
	}

	/**
	 * Convenience method to return this border item figure's locator.
	 * 
	 * @return the <code>Locator</code>
	 */
	public Locator getLocator() {
		return ((BorderItemFigure) getFigure()).getLocator();
	}

	public IBorderItemLocator getBorderItemLocator() {
		Locator locator = getLocator();
		if (locator instanceof IBorderItemLocator) {
			return (IBorderItemLocator) locator;
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
	 * get this edit part's main figure
	 * 
	 * @return the <code>IFigure</code>
	 */
	public final IFigure getMainFigure() {
		if (getFigure() instanceof BorderedFigure) {
			return ((BorderedFigure) getFigure()).getElementPane();
		}
		return getFigure();
	}

	/**
	 * gets this editpart's bordered figure.
	 * 
	 * @return the <codE>BorderedFigure</code>
	 */
	protected final BorderedFigure getBorderedFigure() {
		return (BorderedFigure) getFigure();
	}

	protected IFigure getContentPaneFor(IGraphicalEditPart editPart) {
		if (editPart instanceof BorderItemEditPart) {
			return getBorderedFigure().getBorderItemContainer();
		} else {
			return getMainFigure();
		}
	}

	/**
	 * Adds the supplied child to the editpart's border item figure if it is an
	 * instanceof {@link BorderItemEditPart} and its figure is an instanceof
	 * {@link BorderItemFigure}.
	 */
	protected void addChildVisual(EditPart childEditPart, int index) {

		IFigure childFigure = ((GraphicalEditPart) childEditPart).getFigure();
		if (childEditPart instanceof BorderItemEditPart
			&& childFigure instanceof BorderItemFigure) {
			BorderItemFigure borderItem = (BorderItemFigure) childFigure;
			BorderItemContainerFigure borderItemContainer = (BorderItemContainerFigure) getContentPaneFor((IGraphicalEditPart) childEditPart);
			if (borderItem.getLocator() != null) {
				borderItemContainer.addBorderItem(borderItem, borderItem
					.getLocator());
			} else {
				borderItemContainer.addBorderItem(borderItem,
					new BorderItemFigure.BorderItemLocator(borderItem,
						getMainFigure()));
			}
		} else {
			IFigure fig = getContentPaneFor((IGraphicalEditPart) childEditPart);
			fig.add(childFigure, index);
		}
	}

	/**
	 * Remove the supplied child editpart's figure from this editpart's figure.
	 */
	protected void removeChildVisual(EditPart child) {
		IFigure childFigure = ((GraphicalEditPart) child).getFigure();
		if (child instanceof BorderItemEditPart
			&& childFigure instanceof BorderItemFigure) {
			BorderItemFigure borderItem = (BorderItemFigure) childFigure;
			BorderItemContainerFigure borderItemContainer = (BorderItemContainerFigure) getContentPaneFor((IGraphicalEditPart) child);
			borderItemContainer.removeBorderItem(borderItem);
		} else {
			IFigure fig = getContentPaneFor((IGraphicalEditPart) child);
			fig.remove(childFigure);
		}
	}

	/** Include the border items's parent's parent to the list. */
	Collection disableCanonicalFor(final Request request) {
		Collection disabled = super.disableCanonicalFor(request);
		if ((request instanceof CreateConnectionViewRequest)) {
			CreateConnectionViewRequest ccvr = (CreateConnectionViewRequest) request;
			if (ccvr.getSourceEditPart() instanceof BorderItemEditPart) {
				disabled.add(ccvr.getSourceEditPart().getParent().getParent());
			}
			if (ccvr.getTargetEditPart() instanceof BorderItemEditPart) {
				disabled.add(ccvr.getTargetEditPart().getParent().getParent());
			}
		}
		return disabled;
	}

	public void setLayoutConstraint(EditPart child, IFigure childFigure,
			Object constraint) {
		getContentPaneFor((IGraphicalEditPart) child).setConstraint(
			childFigure, constraint);
	}

	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.CONNECTION_LABELS_ROLE,
			new ConnectionLabelsEditPolicy()); // enable the +/- for floating
		// labels.

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