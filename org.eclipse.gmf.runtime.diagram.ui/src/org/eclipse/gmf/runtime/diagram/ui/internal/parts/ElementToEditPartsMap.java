/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.parts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.EditPart;

/**
 * This class encapsulates the functionality required for entering
 * and retrieving from the 'element to editparts' map required by our
 * viewers.
 * 
 * @author chmahone
 */
public class ElementToEditPartsMap {

	/**
	 * A registry of editparts, mapping an element's id string
	 * to a list of <code>EditParts</code>.  
	 */
	private Map map = new HashMap();

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer#findEditPartsForElement(java.lang.String, java.lang.Class)
	 */
	public List findEditPartsForElement(
		String elementIdStr,
		Class editPartClass) {

		List allEPs = (List)map.get(elementIdStr);
		if (allEPs == null) {
			return Collections.EMPTY_LIST;
		}
		List specificEPs = new ArrayList();
		for (Iterator iter = allEPs.iterator(); iter.hasNext();) {
			Object ep = iter.next();
			if (editPartClass.isInstance(ep)) {
				specificEPs.add(ep);
			}
		}
		return specificEPs;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer#registerEditPartForElement(java.lang.String, org.eclipse.gef.EditPart)
	 */
	public void registerEditPartForElement(
		String elementIdStr,
		EditPart ep) {

		if (elementIdStr == null || ep == null) {
			return;
		}

		List epList = (List)map.get(elementIdStr);
		if (epList != null) {
			if (!epList.contains(ep)) {
				epList.add(ep);
			}
		} else {
			ArrayList newList = new ArrayList(1);
			newList.add(ep);
			map.put(elementIdStr, newList);
		}
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer#unregisterEditPartForElement(java.lang.String, org.eclipse.gef.EditPart)
	 */
	public void unregisterEditPartForElement(
		String elementIdStr,
		EditPart ep) {

		if (elementIdStr == null || ep == null) {
			return;
		}

		List epList = (List)map.get(elementIdStr);
		if (epList != null && epList.contains(ep)) {
			epList.remove(ep);
			if (epList.isEmpty()) {
				map.remove(elementIdStr);
			}
		}
	}

}
