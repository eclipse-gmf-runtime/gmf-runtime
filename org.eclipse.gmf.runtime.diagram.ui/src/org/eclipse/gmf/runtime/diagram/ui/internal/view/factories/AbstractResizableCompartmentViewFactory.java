/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.view.factories;

import java.util.List;

import org.eclipse.gmf.runtime.diagram.ui.view.factories.AbstractNodeViewFactory;
import com.ibm.xtools.notation.NotationFactory;

/**
 * The AbstractResizableCompartmentView Factory class 
 * @author mmostafa
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class AbstractResizableCompartmentViewFactory
	extends AbstractNodeViewFactory {

	/**
	 * @return a list of style for the newly created view or an empty list if none (do not return null)
	 */
	protected List createStyles() {
		List styles = super.createStyles();
		styles.add(NotationFactory.eINSTANCE.createDrawerStyle());
		styles.add(NotationFactory.eINSTANCE.createTitleStyle());
		return styles;
	}
}