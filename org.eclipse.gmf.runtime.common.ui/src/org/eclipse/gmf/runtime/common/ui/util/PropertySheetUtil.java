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

package org.eclipse.gmf.runtime.common.ui.util;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.PropertySheetPage;

/**
 * Utility class that implements some helpful method for <code>PropertySheetViewer.</code>
 * 
 * @author ldamus
 */
public class PropertySheetUtil {
	
	/** ID of property view. */
	public static final String PROPERTY_VIEW_ID = "org.eclipse.ui.views.PropertySheet"; //$NON-NLS-1$

	/**
	 * Refreshes the model for the current page of the property sheet viewer.
	 * Ensures that the model reflects the state of the current viewer input. 
	 */
	public static void refreshCurrentPage() {
		IPage propertyPage = getCurrentPropertySheetPage();

		if (propertyPage instanceof PropertySheetPage) {
			((PropertySheetPage) propertyPage).refresh();
		}
	}

	/**
	 * Sets the selection of the current property sheet page to 
	 * <code>selection</code>. This has the effect of refreshing
	 * the property sheet viewer input with the items in 
	 * <code>selection</code>.
	 * 
	 * @param part the part where the selection occurred
	 * @param selection the new selection
	 */
	public static void setCurrentPageSelection(
		IWorkbenchPart part,
		ISelection selection) {
			
		IPage propertyPage = getCurrentPropertySheetPage();

		if (propertyPage instanceof IPropertySheetPage) {
			((IPropertySheetPage) propertyPage).selectionChanged(
				part,
				selection);
		}
	}

	/**
	 * Gets the current property sheet page, or null if there is none.
	 * 
	 * @return the current property sheet page
	 */
	private static IPage getCurrentPropertySheetPage() {
		
		IWorkbenchWindow window =
			PlatformUI.getWorkbench().getActiveWorkbenchWindow();

		if (window != null) {
			IWorkbenchPage page = window.getActivePage();

			if (page != null) {
				IViewPart view = page.findView(PROPERTY_VIEW_ID);

				if (view != null) {

					if (view instanceof PropertySheet) {
						return ((PropertySheet) view).getCurrentPage();
					}
				}
			}
		}
		return null;
	}

}
