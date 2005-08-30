/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.printing.internal.util;

import java.text.DateFormat;
import java.util.GregorianCalendar;

import org.eclipse.jface.preference.IPreferenceStore;

import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.WorkspaceViewerProperties;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;

/**
 * Helper for header and footers. 
 * 
 * @author wdiu, Wayne Diu
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.printing.*
 */
public class HeaderAndFooterHelper {
	
	/**
	 * Header margin from top of page in LP units
	 */
	public static final int TOP_MARGIN = MapMode.DPtoLP(5);

	/**
	 * Header and footer Margin from left of page in LP units
	 */
	public static final int LEFT_MARGIN = MapMode.DPtoLP(5);

	/**
	 * Footer Margin from bottom of page in LP units
	 */
	public static final int BOTTOM_MARGIN = MapMode.DPtoLP(20);
	
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
