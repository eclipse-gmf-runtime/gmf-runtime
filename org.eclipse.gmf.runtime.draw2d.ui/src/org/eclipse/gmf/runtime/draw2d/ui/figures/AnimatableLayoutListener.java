/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.draw2d.ui.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutListener;

import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.AnimationFigureHelper;

/**
 * @author sshaw
 *
 * Implementation of LayoutListener interface so that generic animation of
 * figures during the layout phase of validation can be accomplished.  Clients
 * merely have to add this listener to their custom figure during creation to support
 * animation.
 */
public class AnimatableLayoutListener extends LayoutListener.Stub {
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.LayoutListener#layout(org.eclipse.draw2d.IFigure)
	 */
	public boolean layout(IFigure container) {
		return AnimationFigureHelper.getInstance().layoutManagerHook(container);
	}
}
