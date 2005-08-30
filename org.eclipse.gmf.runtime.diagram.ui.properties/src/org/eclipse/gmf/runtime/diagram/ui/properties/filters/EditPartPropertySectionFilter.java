/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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