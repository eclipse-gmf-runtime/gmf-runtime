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

package org.eclipse.gmf.runtime.diagram.ui.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.emf.core.util.PackageUtil;
import org.eclipse.gmf.runtime.notation.View;

/**
 * provides different utility functions for the EditPart
 * @author mmostafa
 */

public class EditPartUtil {
	
	/** 
	 * gets the <code>Editpart</code>'s semantic element Class Id, this could be used to
	 * check the semantic element type
	 * @param editpart the owner of the semantic element
	 * @return the semantic element class Id
	 */
	public static String getSemanticEClassName(IGraphicalEditPart editPart) {
		if (editPart.getModel() instanceof View){
			View view = (View)editPart.getModel();
			EObject element = view.getElement();
			return element == null ? null : PackageUtil.getID(EMFCoreUtil.getProxyClass(element));
		}
		return null;
	}
}
