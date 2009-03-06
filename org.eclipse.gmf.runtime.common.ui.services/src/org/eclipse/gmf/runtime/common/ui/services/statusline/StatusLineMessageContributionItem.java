/******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.common.ui.services.statusline;

import org.eclipse.gmf.runtime.common.ui.services.util.CommonLabelProvider;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/**
 * A default implementation of a contribution item that provides content for the
 * message contribution in the status line. The default status line manager
 * provides the ability to display an icon and message. This class is not really
 * a ContributionItem but rather a label provider that is returned by the
 * {@link StatusLineService}
 * 
 * @author Anthony Hunter
 * @since 1.2
 */
public class StatusLineMessageContributionItem extends ContributionItem
		implements ILabelProvider {

	private ILabelProvider labelProvider;

	/**
	 * Constructor for a StatusLineMessageContributionItem.
	 * 
	 * @param workbenchPage
	 *            - workbench page to be used
	 * @param id
	 *            - contribution item id
	 */
	public StatusLineMessageContributionItem() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.
	 * jface.viewers.ILabelProviderListener)
	 */
	public void addListener(ILabelProviderListener listener) {
		// Not implemented
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object element) {
		return getLabelProvider().getImage(element);
	}

	/**
	 * Get the label provider. If no label provider has been initialized by
	 * setLabelProvider(), we create a new {@link CommonLabelProvider}.
	 * 
	 * @return the label provider.
	 */
	public ILabelProvider getLabelProvider() {
		if (labelProvider == null) {
			labelProvider = new CommonLabelProvider();
		}
		return labelProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object element) {
		return getLabelProvider().getText(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang
	 * .Object, java.lang.String)
	 */
	public boolean isLabelProperty(Object element, String property) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse
	 * .jface.viewers.ILabelProviderListener)
	 */
	public void removeListener(ILabelProviderListener listener) {
		// Not implemented
	}

	/**
	 * Set the label provider.
	 * 
	 * @param aLabelProvider
	 *            the label provider.
	 */
	public void setLabelProvider(ILabelProvider aLabelProvider) {
		this.labelProvider = aLabelProvider;
	}

}
