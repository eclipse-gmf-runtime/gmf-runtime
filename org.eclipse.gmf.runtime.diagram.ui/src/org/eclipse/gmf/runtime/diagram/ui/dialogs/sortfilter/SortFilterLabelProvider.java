/******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.dialogs.sortfilter;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramResourceManager;

/**
 * @author jcorchis
 */
public abstract class SortFilterLabelProvider
	extends LabelProvider
	implements ITableLabelProvider {

	private static final String CHECKED_IMAGE = "checkboxselected.gif"; //$NON-NLS-1$
	private static final String UNCHECKED_IMAGE = "checkboxcleared.gif"; //$NON-NLS-1$
	private static Image checkedImage = DiagramResourceManager.getInstance().createImage(CHECKED_IMAGE);
	private static Image uncheckedImage = DiagramResourceManager.getInstance().createImage(UNCHECKED_IMAGE);

	
	/**
	 * constructor
	 */
	public SortFilterLabelProvider() {
	    // empty ctor
	}

	/**
	 * returns the image
	 * @param isSelected , determin if the returned image will be the selected image or not
	 * @return the image
	 */
	protected Image getImage(boolean isSelected) {
		return isSelected ? checkedImage : uncheckedImage;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		return (columnIndex == 0)
			? getImage(((SortFilterElement) element).isVisible())
			: null;
	}	

}
