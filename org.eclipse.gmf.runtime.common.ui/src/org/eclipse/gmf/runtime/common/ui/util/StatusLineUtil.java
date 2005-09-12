/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
