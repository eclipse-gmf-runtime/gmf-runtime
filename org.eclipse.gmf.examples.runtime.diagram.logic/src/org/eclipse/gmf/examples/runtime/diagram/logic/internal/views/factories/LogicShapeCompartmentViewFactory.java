/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.views.factories;

import java.util.List;

import org.eclipse.gmf.runtime.diagram.ui.view.factories.ResizableCompartmentViewFactory;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.View;

/**
 * The LogicShapeCompartmentView Factory class 
 * @author mmostafa
 */
public class LogicShapeCompartmentViewFactory
	extends ResizableCompartmentViewFactory {

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.view.factories.AbstractViewFactory#createStyles(org.eclipse.gmf.runtime.notation.View)
	 */
	protected List createStyles(View view) {
		List styles = super.createStyles(view);
		styles.add(NotationFactory.eINSTANCE
			.createCanonicalStyle());
		return styles;
	}
}