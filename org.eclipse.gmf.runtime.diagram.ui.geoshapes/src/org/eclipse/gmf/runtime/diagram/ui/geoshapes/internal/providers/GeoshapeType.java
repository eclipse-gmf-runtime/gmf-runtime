/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.providers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gmf.runtime.emf.type.core.AbstractElementTypeEnumerator;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;

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

	public static final IElementType OVAL = getElementType("org.eclipse.gmf.runtime.diagram.ui.geoshapes.oval"); //$NON-NLS-1$

	public static final IElementType TRIANGLE = getElementType("org.eclipse.gmf.runtime.diagram.ui.geoshapes.triangle"); //$NON-NLS-1$

	public static final IElementType RECTANGLE = getElementType("org.eclipse.gmf.runtime.diagram.ui.geoshapes.rectangle"); //$NON-NLS-1$

	public static final IElementType SHADOWRECTANGLE = getElementType("org.eclipse.gmf.runtime.diagram.ui.geoshapes.shadowRectangle"); //$NON-NLS-1$

	public static final IElementType THREEDRECTANGLE = getElementType("org.eclipse.gmf.runtime.diagram.ui.geoshapes.3DRectangle"); //$NON-NLS-1$

	public static final IElementType ROUNDRECTANGLE = getElementType("org.eclipse.gmf.runtime.diagram.ui.geoshapes.roundRectangle"); //$NON-NLS-1$

	public static final IElementType HEXAGON = getElementType("org.eclipse.gmf.runtime.diagram.ui.geoshapes.hexagon"); //$NON-NLS-1$

	public static final IElementType OCTAGON = getElementType("org.eclipse.gmf.runtime.diagram.ui.geoshapes.octagon"); //$NON-NLS-1$

	public static final IElementType PENTAGON = getElementType("org.eclipse.gmf.runtime.diagram.ui.geoshapes.pentagon"); //$NON-NLS-1$

	public static final IElementType DIAMOND = getElementType("org.eclipse.gmf.runtime.diagram.ui.geoshapes.diamond"); //$NON-NLS-1$

	public static final IElementType CYLINDER = getElementType("org.eclipse.gmf.runtime.diagram.ui.geoshapes.cylinder"); //$NON-NLS-1$

	public static final IElementType LINE = getElementType("org.eclipse.gmf.runtime.diagram.ui.geoshapes.line"); //$NON-NLS-1$

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