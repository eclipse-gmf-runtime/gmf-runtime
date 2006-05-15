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

package org.eclipse.gmf.runtime.diagram.ui.providers.internal;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IPrimaryEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramColorRegistry;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.AbstractDecorator;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget;
import org.eclipse.gmf.runtime.draw2d.ui.figures.HashedCircle;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.LineStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;


/**
 * Provides the unresolved view adornment for the views 
 * on a diagram that expect a non-null model reference.
 * 
 * @author cmahoney
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.providers.*
 */
public class UnresolvedViewDecorator
	extends AbstractDecorator {

	/**
	 * Creates a new <code>UnresolvedViewDecorator</code>.
	 * @param decoratorTarget
	 */
	public UnresolvedViewDecorator(IDecoratorTarget decoratorTarget) {
		super(decoratorTarget);
	}

	/**
	 * Creates the appropriate unresolved view decoration if all the
	 * criteria is satisfied by the view passed in.
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecorator#refresh()
	 */
	public void refresh() {
		removeDecoration();
		EditPart editPart  = (EditPart)getDecoratorTarget().getAdapter(EditPart.class);
		View view = (View) getDecoratorTarget().getAdapter(View.class);
		if (!(editPart instanceof IPrimaryEditPart)) {
			return;
		}

		EObject element = ViewUtil.resolveSemanticElement(view);

		int radius = MapModeUtil.getMapMode(((GraphicalEditPart)editPart).getFigure()).DPtoLP(8);

		if (editPart instanceof ShapeEditPart && element == null) {
			HashedCircle circle = new HashedCircle(HashedCircle.HashType.X,
				radius);
			circle.setFill(false);
			setDecoration(getDecoratorTarget().addShapeDecoration(circle,
				IDecoratorTarget.Direction.NORTH_EAST, MapModeUtil.getMapMode(((ShapeEditPart)editPart).getFigure()).DPtoLP(-4),
				false));

		} else if (view instanceof Edge) {
			Edge connectorView = (Edge)view;
			if (element == null) {
				HashedCircle circle = new HashedCircle(HashedCircle.HashType.X,
					radius);
				circle.setFill(false);
				setDecoration(getDecoratorTarget().addConnectionDecoration(
					circle, 50, false));
			} else if ((connectorView.getTarget() != null)&&(ViewUtil
				.resolveSemanticElement(connectorView.getTarget()) == null)) {
				HashedCircle circle = new HashedCircle(
					HashedCircle.HashType.BACKSLASH, radius);
				circle.setFill(false);
				setDecoration(getDecoratorTarget().addConnectionDecoration(
					circle, 70, false));
			}

		}

		if (getDecoration() == null) {
			return;
		}

		if (ViewUtil.isPropertySupported(view,Properties.ID_LINECOLOR)) {
			LineStyle style = (LineStyle) view.getStyle(NotationPackage.eINSTANCE.getLineStyle());
			getDecoration().setForegroundColor(
				DiagramColorRegistry.getInstance().getColor(new Integer(style.getLineColor())));
		}
	}

	/** 
	 * Adds decoration if applicable.
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecorator#activate()
	 */
	public void activate() {
		refresh();
	}

}
