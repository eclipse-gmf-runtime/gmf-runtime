/******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.dialogs.sortfilter;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.diagram.ui.dialogs.sortfilter.SortFilterPage;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.swt.widgets.Display;

/**
 * Utility class that creates and opens the actual dialog with the data the 
 * EditPolicy must provide before the dialog is invoked.
 * 
 * @author jcorchis
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class SortFilterDialogUtil {
	
	private final static String ROOT_NAME = DiagramUIMessages.SortFilterDialog_rootTitle;

	/**
	 * Constructor for CollectionEditorUtil.  No need instantiate this class.
	 */
	private SortFilterDialogUtil() {
	    // utility class.
	}

	/**
	 * Invokes the Sort/Filter with the given <b>CollectionPages</b>
	 * @param view the  the element
	 * @return the window return code OK or CANCEL
	 */
	static public int invokeDialog(
		GraphicalEditPart editPart,
		SortFilterPage rootPage,
		List collectionPages) {
			
		SortFilterDialog sortFilterDialog =
			new SortFilterDialog(
				Display.getCurrent().getActiveShell());
				
		rootPage.setTitle(ROOT_NAME);

		sortFilterDialog.getPreferenceManager().addToRoot(
			new SortFilterRootPreferenceNode(ROOT_NAME, rootPage, sortFilterDialog));
		// Add the child pages
		Iterator iter = collectionPages.iterator();
		while (iter.hasNext()) {
			sortFilterDialog.getPreferenceManager().addTo(
			ROOT_NAME,
				new PreferenceNode(
					StringStatics.BLANK,
					(SortFilterPage) iter.next()));
		}

		sortFilterDialog.create();
		return sortFilterDialog.open();
	}
	
	/**
	 * Invokes the simple filter dialog which allows
	 * multiple elements to be filtered.
	 * @param selection
	 * @return
	 */
	static public int invokeFilterDialog(
			List selection, Map filterMap) {
		FilterDialog filterDialog =
			new FilterDialog(Display.getCurrent().getActiveShell(), selection, filterMap);
		
		return filterDialog.open();
	}	
}
