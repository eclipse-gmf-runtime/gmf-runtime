/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.view.factories;

import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.NotationFactory;

/**
 * the base factory class for all label views
 * @see  org.eclipse.gmf.runtime.diagram.ui.view.factories.AbstractNodeViewFactory
 * @author mmostafa
 */
public class AbstractLabelViewFactory
	extends AbstractNodeViewFactory {

	protected LayoutConstraint createLayoutConstraint() {
		return NotationFactory.eINSTANCE.createLocation();
	}
}