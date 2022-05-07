/******************************************************************************
 * Copyright (c) 2006, 2010 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.figures;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * An Expandable Figure
 * 
 * @author mmostafa
 * @deprecated Use {org.eclipse.gmf.runtime.diagram.ui.figures.IExpandableFigure}
 */
public interface IExpandableFigure extends
		org.eclipse.gmf.runtime.diagram.ui.figures.IExpandableFigure {

	Rectangle getExtendedBounds();

}
