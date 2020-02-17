/******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.geoshapes.type;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gmf.runtime.diagram.ui.util.INotationType;
import org.eclipse.gmf.runtime.emf.type.core.AbstractElementTypeEnumerator;

/**
 * Element types for the notation elements defined in the Geoshape plugin.
 * 
 * <p>
 * If a new shape type is added, it should also be added to the list returned in
 * <code>getShapeTypes()</code>.
 * </p>
 * 
 * @author cmahoney, ldamus
 */
public class GeoshapeType
	extends AbstractElementTypeEnumerator {

	public static final INotationType OVAL = (INotationType) getElementType("org.eclipse.gmf.runtime.diagram.ui.geoshapes.oval"); //$NON-NLS-1$

	public static final INotationType TRIANGLE = (INotationType) getElementType("org.eclipse.gmf.runtime.diagram.ui.geoshapes.triangle"); //$NON-NLS-1$

	public static final INotationType RECTANGLE = (INotationType) getElementType("org.eclipse.gmf.runtime.diagram.ui.geoshapes.rectangle"); //$NON-NLS-1$

	public static final INotationType SHADOWRECTANGLE = (INotationType) getElementType("org.eclipse.gmf.runtime.diagram.ui.geoshapes.shadowRectangle"); //$NON-NLS-1$

	public static final INotationType THREEDRECTANGLE = (INotationType) getElementType("org.eclipse.gmf.runtime.diagram.ui.geoshapes.3DRectangle"); //$NON-NLS-1$

	public static final INotationType ROUNDRECTANGLE = (INotationType) getElementType("org.eclipse.gmf.runtime.diagram.ui.geoshapes.roundRectangle"); //$NON-NLS-1$

	public static final INotationType HEXAGON = (INotationType) getElementType("org.eclipse.gmf.runtime.diagram.ui.geoshapes.hexagon"); //$NON-NLS-1$

	public static final INotationType OCTAGON = (INotationType) getElementType("org.eclipse.gmf.runtime.diagram.ui.geoshapes.octagon"); //$NON-NLS-1$

	public static final INotationType PENTAGON = (INotationType) getElementType("org.eclipse.gmf.runtime.diagram.ui.geoshapes.pentagon"); //$NON-NLS-1$

	public static final INotationType DIAMOND = (INotationType) getElementType("org.eclipse.gmf.runtime.diagram.ui.geoshapes.diamond"); //$NON-NLS-1$

	public static final INotationType CYLINDER = (INotationType) getElementType("org.eclipse.gmf.runtime.diagram.ui.geoshapes.cylinder"); //$NON-NLS-1$

	public static final INotationType LINE = (INotationType) getElementType("org.eclipse.gmf.runtime.diagram.ui.geoshapes.line"); //$NON-NLS-1$

	/**
	 * Gets a list of all the possible shape types. This is used by the
	 * connection handles.
	 * 
	 * @return a list of shape types
	 */
	public static List getShapeTypes() {
		List shapes = new ArrayList();

		shapes.add(OVAL);
		shapes.add(TRIANGLE);
		shapes.add(RECTANGLE);
		shapes.add(SHADOWRECTANGLE);
		shapes.add(THREEDRECTANGLE);
		shapes.add(ROUNDRECTANGLE);
		shapes.add(HEXAGON);
		shapes.add(OCTAGON);
		shapes.add(PENTAGON);
		shapes.add(DIAMOND);
		shapes.add(CYLINDER);

		return shapes;
	}

}