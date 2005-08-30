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

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.diagram.ui.internal.view.factories.AbstractResizableCompartmentViewFactory;
import com.ibm.xtools.notation.View;

/**
 * This is the base factory class for all shape compartment views, it will 
 * create the view and decorate it using the default decorations
 * you can subclass it to add more decorations, or customize the 
 * way it looks, like adding new style
 * @see #createView(IAdaptable, View, String, int, boolean, String)
 * @see #decorateView(View, View, IAdaptable, String, int, boolean)
 * @see #createStyles()
 * @author mmostafa
 * 
 */
public class AbstractShapeCompartmentViewFactory
	extends AbstractResizableCompartmentViewFactory {

	// TODO: delete me
}