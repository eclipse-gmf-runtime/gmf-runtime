/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.draw2d.ui.internal.figures;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Dimension;

/*
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.*
 */
public class DelegatingLayout extends org.eclipse.draw2d.DelegatingLayout {
	private Map constraints = new HashMap();

	protected Dimension calculatePreferredSize(IFigure parent, int wHint, int hHint) {
		List children = parent.getChildren();
		Dimension d = new Dimension();
		for (int i = 0; i < children.size(); i++) {
			IFigure child = (IFigure) children.get(i);
			d.union(child.getPreferredSize(wHint, hHint));
		}
		return d;
	}

	public void layout(IFigure parent) {

		List children = parent.getChildren();
		for (int i = 0; i < children.size(); i++) {
			IFigure child = (IFigure) children.get(i);
			child.setSize(child.getPreferredSize());
			Object locator = constraints.get(child);
			if (locator != null && locator instanceof Locator) {
				((Locator) locator).relocate(child);
			}
		}
	}

	public void setConstraint(IFigure figure, Object constraint) {
		super.setConstraint(figure, constraint);
		if (constraint != null)
			constraints.put(figure, constraint);
	}
}
