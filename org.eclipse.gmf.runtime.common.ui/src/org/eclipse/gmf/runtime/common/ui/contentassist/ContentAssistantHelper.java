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

package org.eclipse.gmf.runtime.common.ui.contentassist;

import org.eclipse.jface.contentassist.SubjectControlContentAssistant;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.contentassist.ContentAssistHandler;

import org.eclipse.gmf.runtime.common.ui.internal.contentassist.TextPresenter;

/**
 * Helper class for content assist
 * 
 * @author myee
 */
public class ContentAssistantHelper {

	/**
	 * Prevent calling the constructor
	 */
	private ContentAssistantHelper() {
		// do nothing
	}

	/**
	 * Returns a content assistant for a given text control and content assist
	 * processor
	 * 
	 * @param text
	 *            the text control
	 * @param processor
	 *            the content assist processor
	 * @return the content assistant
	 */
	public static ContentAssistHandler createTextContentAssistant(
			final Text text, IContentAssistProcessor processor) {
		return createTextContentAssistant(text, null, null, processor);
	}

	/**
	 * Returns a content assistant for a given text control and content assist
	 * processor
	 * 
	 * @param text
	 *            the text control
	 * @param processor
	 *            the content assist processor
	 * @param foreground
	 *            the foreground color
	 * @param background
	 *            the background color
	 * @return the content assistant
	 */
	public static ContentAssistHandler createTextContentAssistant(
			final Text text, Color foreground, Color background,
			IContentAssistProcessor processor) {
		return ContentAssistHandler.createHandlerForText(text,
			createContentAssistant(processor, foreground, background));
	}

	/**
	 * Returns a content assistant for a given control and content assist
	 * processor
	 * 
	 * @param processor
	 *            the content assist processor
	 * @param foreground
	 *            the foreground color, or <code>null</code> for default color
	 * @param background
	 *            the background color, or <code>null</code> for default color
	 * @return the content assistant
	 */
	private static SubjectControlContentAssistant createContentAssistant(
			IContentAssistProcessor processor, Color foreground,
			Color background) {
		final SubjectControlContentAssistant contentAssistant = new SubjectControlContentAssistant();

		if (foreground != null) {
			contentAssistant.setProposalSelectorForeground(foreground);
		}
		if (background != null) {
			contentAssistant.setProposalSelectorBackground(background);
		}

		contentAssistant.setContentAssistProcessor(processor,
			IDocument.DEFAULT_CONTENT_TYPE);

		contentAssistant.enableAutoActivation(true);
		contentAssistant.enableAutoInsert(true);

		contentAssistant
			.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
		contentAssistant
			.setInformationControlCreator(new IInformationControlCreator() {

				public IInformationControl createInformationControl(Shell parent) {
					return new DefaultInformationControl(parent, SWT.NONE,
						new TextPresenter());
				}
			});

		return contentAssistant;
	}
}