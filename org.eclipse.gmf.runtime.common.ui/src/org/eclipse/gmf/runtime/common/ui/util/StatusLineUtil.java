/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.util;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.EditorActionBarContributor;

/**
 * Utility class for outputting text to the status line
 * 
 * @author myee
 */
public class StatusLineUtil {
	private StatusLineUtil() {
		 /* private constructor */
	}

	/**
	 * Outputs an error message to the part's status line
	 * 
	 * @param part the part
	 * @param errorMessage the error message
	 */
	public static void outputErrorMessage(IWorkbenchPart part, String errorMessage) {
		if (part instanceof IViewPart) {
			IViewPart viewPart = (IViewPart) part;

			IStatusLineManager statusLineManager =
				viewPart.getViewSite().getActionBars().getStatusLineManager();

			statusLineManager.setErrorMessage(errorMessage);

		} else if (part instanceof IEditorPart) {
			IEditorPart editorPart = (IEditorPart) part;

			IEditorActionBarContributor contributor =
				editorPart.getEditorSite().getActionBarContributor();
			if (contributor instanceof EditorActionBarContributor) {
				IStatusLineManager statusLineManager =
					((EditorActionBarContributor) contributor)
						.getActionBars()
						.getStatusLineManager();

				statusLineManager.setMessage(errorMessage);
			}
		}
	}

}
