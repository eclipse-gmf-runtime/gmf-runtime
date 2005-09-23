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
package org.eclipse.gmf.runtime.common.ui.services.internal.elementselection;

import org.eclipse.gmf.runtime.common.ui.services.elementselection.AbstractMatchingObject;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/**
 * A standard label provider for the type selection composite. The label
 * provider will delegate to the matching object returned by the type selection
 * service.
 * 
 * @author Anthony Hunter <a href="mailto:anthonyh@ca.ibm.com">
 *         anthonyh@ca.ibm.com </a>
 */
public class ElementSelectionCompositeLabelProvider
	implements ILabelProvider {

	/**
	 * @inheritDoc
	 */
	public Image getImage(Object element) {
		assert element instanceof AbstractMatchingObject;
		return ((AbstractMatchingObject) element).getImage();
	}

	/**
	 * @inheritDoc
	 */
	public String getText(Object element) {
		assert element instanceof AbstractMatchingObject;
		return ((AbstractMatchingObject) element).getDisplayName();
	}

	/**
	 * @inheritDoc
	 */
	public void addListener(ILabelProviderListener listener) {
		/*
		 * Not implemented.
		 */
	}

	/**
	 * @inheritDoc
	 */
	public void dispose() {
		/*
		 * Not implemented.
		 */
	}

	/**
	 * @inheritDoc
	 */
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	/**
	 * @inheritDoc
	 */
	public void removeListener(ILabelProviderListener listener) {
		/*
		 * Not implemented.
		 */
	}

}
