/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.internal.contentassist;

import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.widgets.Display;

/**
 * Presents given text in bold
 * 
 * @author myee
 */
public class TextPresenter
	implements DefaultInformationControl.IInformationPresenter {

	/**
	 * The tag used to start displaying text in boldface. Text between the start
	 * and end tags will be in boldface.
	 */
	static public final String BOLD_START_TAG = "<b>"; //$NON-NLS-1$

	/**
	 * The tag used to end displaying text in boldface. Text between the start
	 * and end tags will be in boldface.
	 */
	static public final String BOLD_END_TAG = "</b>"; //$NON-NLS-1$

	/**
	 * Update text with bold style.
	 * 
	 * @param display
	 *            the SWT display
	 * @param hoverInfo
	 *            the String to be presented
	 * @param presentation
	 *            the TextPresentation to be updated with the bold style
	 * @param maxWidth
	 *            the maximum width
	 * @param maxHeight
	 *            the maximum height
	 * 
	 * @return the hoverInfo String with the bold tags removed
	 */
	public String updatePresentation(Display display, String hoverInfo,
			TextPresentation presentation, int maxWidth, int maxHeight) {
		// parse the bold tags
		int start = hoverInfo.indexOf(BOLD_START_TAG);
		int end = hoverInfo.lastIndexOf(BOLD_END_TAG);
		assert (start != -1 && end != -1);

		// bold the text in the given range
		StyleRange styleRange = new StyleRange(start, end - start
			- BOLD_START_TAG.length(), null, null, SWT.BOLD);
		presentation.addStyleRange(styleRange);

		// return the text with the bold tags removed
		String parsedText = hoverInfo.substring(0, start)
			+ hoverInfo.substring(start + BOLD_START_TAG.length(), end)
			+ hoverInfo.substring(end + BOLD_END_TAG.length());
		return parsedText;
	}
}