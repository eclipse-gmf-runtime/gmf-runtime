/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.providers.internal;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;

import org.eclipse.gmf.runtime.diagram.ui.editparts.IPrimaryEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.figures.HashedCircle;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.providers.internal.l10n.DiagramProvidersResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.AbstractDecorator;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
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

	/** The radius of the decoration hashed circle */
	private static final int RADIUS = MapMode.DPtoLP(8);

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

		if (editPart instanceof ShapeEditPart && element == null) {
			HashedCircle circle = new HashedCircle(HashedCircle.HashType.X,
				RADIUS);
			circle.setFill(false);
			setDecoration(getDecoratorTarget().addShapeDecoration(circle,
				IDecoratorTarget.Direction.NORTH_EAST, MapMode.DPtoLP(-4),
				false));

		} else if (view instanceof Edge) {
			Edge connectorView = (Edge)view;
			if (element == null) {
				HashedCircle circle = new HashedCircle(HashedCircle.HashType.X,
					RADIUS);
				circle.setFill(false);
				setDecoration(getDecoratorTarget().addConnectionDecoration(
					circle, 50, false));
			} else if ((connectorView.getTarget() != null)&&(ViewUtil
				.resolveSemanticElement(connectorView.getTarget()) == null)) {
				HashedCircle circle = new HashedCircle(
					HashedCircle.HashType.BACKSLASH, RADIUS);
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
				DiagramProvidersResourceManager.getInstance().getColor(new Integer(style.getLineColor())));
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
