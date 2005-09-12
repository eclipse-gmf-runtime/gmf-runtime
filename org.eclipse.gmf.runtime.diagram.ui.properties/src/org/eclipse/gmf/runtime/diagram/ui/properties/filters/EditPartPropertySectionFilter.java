/******************************************************************************
 * Copyright (c) 2004, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.properties.filters;

import org.eclipse.wst.common.ui.properties.internal.provisional.ITypeMapper;

import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;

/**
 * Filter to retain edit part properties
 * 
 * @author Natalia Balaba
 */
public class EditPartPropertySectionFilter
	implements ITypeMapper {

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.properties.ITypeMapper#remapType(java.lang.Object,
	 *      java.lang.Class)
	 */
	public Class remapType(Object object, Class effectiveType) {
		if (object instanceof IGraphicalEditPart)
			return effectiveType;

		return null;
	}
}