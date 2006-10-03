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

package org.eclipse.gmf.runtime.diagram.ui.view.factories;

import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.NotationFactory;

/**
 * the base factory class for all label views
 * @see  org.eclipse.gmf.runtime.diagram.ui.view.factories.BasicNodeViewFactory
 * @author mmostafa
 */
abstract public class AbstractLabelViewFactory
	extends BasicNodeViewFactory {

	protected LayoutConstraint createLayoutConstraint() {
		return NotationFactory.eINSTANCE.createLocation();
	}
}