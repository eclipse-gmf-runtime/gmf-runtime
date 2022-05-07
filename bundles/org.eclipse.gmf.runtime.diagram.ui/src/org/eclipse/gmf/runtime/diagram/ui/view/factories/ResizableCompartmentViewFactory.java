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

import java.util.List;

import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.View;

/**
 * The factory class responsible for creating the Resizable Compartment View 
 * @author mmostafa
 */
public class ResizableCompartmentViewFactory
	extends BasicNodeViewFactory {

	/**
	 * @return a list of style for the newly created view or an empty list if none (do not return null)
	 */
	protected List createStyles(View view) {
		List styles = super.createStyles(view);
		styles.add(NotationFactory.eINSTANCE.createDrawerStyle());
		styles.add(NotationFactory.eINSTANCE.createTitleStyle());
		return styles;
	}
}