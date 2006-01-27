/******************************************************************************
 * Copyright (c) 2005 - 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.figures;

import org.eclipse.draw2d.LayoutAnimator;

/**
 * @author sshaw
 *
 * Implementation of LayoutListener interface so that generic animation of
 * figures during the layout phase of validation can be accomplished.  Clients
 * merely have to add this listener to their custom figure during creation to support
 * animation.
 * 
 * @deprecated use {@link org.eclipse.draw2d.LayoutAnimator} directly
 * 			   01/25/2006 See API change documentation in bugzilla 125158
 *             (https://bugs.eclipse.org/bugs/show_bug.cgi?id=125158)
 */
public class AnimatableLayoutListener extends LayoutAnimator {

	public AnimatableLayoutListener() {
		super();
		// do nothing;
	}
}
