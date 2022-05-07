/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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