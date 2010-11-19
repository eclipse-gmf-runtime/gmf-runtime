/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.printing.internal.util;


import java.util.GregorianCalendar;

import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.WorkspaceViewerProperties;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.ibm.icu.text.DateFormat;

/**
 * Helper for header and footers. 
 * 
 * @author wdiu, Wayne Diu
 */
public class HeaderAndFooterHelper {
	
	/**
	 * Header margin from top of page in device units
	 */
	public static final int TOP_MARGIN_DP = 5;

	/**
	 * Header and footer Margin from left of page in device units
	 */
	public static final int LEFT_MARGIN_DP = 5;

	/**
	 * Footer Margin from bottom of page in device units
	 */
	public static final int BOTTOM_MARGIN_DP = 20;
	
	/**
	 * Separator
	 */
	private static final String HEADER_AND_FOOTER_SPACES =
		StringStatics.SPACE + StringStatics.SPACE + StringStatics.SPACE;
	
	/**
	 * Returns a string for the header or footer.  It will vary depending on 
	 * the user's page setup preferences.
	 * @param prefix, pass in IDialogSettingsConstants.FOOTER_PREFIX or
	 * IDialogSettingsConstants.HEADER_PREFIX
	 * @param rowIndex, an integer starting from 1
	 * @param colIndex, an integer starting from 1
	 * @return a string for the header or footer
	 */
	public static String makeHeaderOrFooterString(
		String prefix,
		int rowIndex,
		int colIndex,
		DiagramEditPart dgrmEP) {
		//the string may have a trailing space, this is OK

		IPreferenceStore preferences; 
		preferences =
			((DiagramGraphicalViewer) dgrmEP.getViewer())
				.getWorkspaceViewerPreferenceStore();
		String string =
			preferences.getString(
				prefix + WorkspaceViewerProperties.PRINT_TEXT_SUFFIX);
		if (string == null) {
			string = StringStatics.BLANK;
		}

		if (!string.equals(StringStatics.BLANK)) {
			string += HEADER_AND_FOOTER_SPACES;
		}

		if (preferences
			.getBoolean(prefix + WorkspaceViewerProperties.PRINT_TITLE_SUFFIX)) {
			String name = dgrmEP.getDiagramView().getDiagram().getName(); 
			string += (name == null ? StringStatics.BLANK : name) + HEADER_AND_FOOTER_SPACES;
		}

		//format according to locale
		if (preferences
			.getBoolean(prefix + WorkspaceViewerProperties.PRINT_DATE_SUFFIX)) {
			string
				+= DateFormat.getDateInstance().format(
					new GregorianCalendar().getTime())
				+ HEADER_AND_FOOTER_SPACES;
		}

		if (preferences
			.getBoolean(prefix + WorkspaceViewerProperties.PRINT_PAGE_SUFFIX)) {
			string += rowIndex + "-" + colIndex; //$NON-NLS-1$
		}
		return string;
	}
}
