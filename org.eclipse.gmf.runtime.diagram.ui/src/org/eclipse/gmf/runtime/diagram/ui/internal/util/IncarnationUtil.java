/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.diagram.ui.internal.util;

import org.eclipse.gmf.runtime.diagram.core.internal.view.IView;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;


/**
 * @author mmostafa
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IncarnationUtil {
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.service.notation.IViewProvider#incarnateView(View)
	 */
	public static IView incarnateView(View viewElement) {
		if (viewElement == null)
			return null;

		IView incarnatedView = null;

		if (viewElement instanceof Diagram) {
			return new DummyDiagramView(viewElement);
		} else if (viewElement instanceof Edge) {
			return new DummyConnectorView(viewElement);
		} else if (viewElement instanceof Node) {
			return new DummyNodeView(viewElement);
		}

		return incarnatedView;
	}

}
