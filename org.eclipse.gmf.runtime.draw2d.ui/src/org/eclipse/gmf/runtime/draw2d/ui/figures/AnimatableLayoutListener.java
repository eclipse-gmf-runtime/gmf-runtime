/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
