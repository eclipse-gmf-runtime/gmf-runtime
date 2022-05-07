/*
 * Copyright (c) 2006 Borland Software Corporation
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *    Dmitry Stadnik (Borland) - initial API and implementation
 *	  Steve Shaw (IBM) - added java doc and modified method name
 */
package org.eclipse.gmf.runtime.diagram.ui.editparts;

import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserOptions;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.viewers.ICellEditorValidator;

/**
 * Edit part that contains editable text.
 * 
 * @author dstadnik
 */
public interface ITextAwareEditPart extends org.eclipse.gef.GraphicalEditPart {

	/**
	 * Shortcut method to obtain edit text from parser.
	 */
	public String getEditText();

	/**
	 * Called when edit text was modified and edit part
	 * should update itself to reflect the changes.
	 */
	public void setLabelText(String text);

    /**
     * Returns a validator for the user's edit text
     * @return a <code>ICellEditorValidator</code> for validating a cell editor's input.
     */
	public ICellEditorValidator getEditTextValidator();

    /**
     * Gets the parser options. The result is passed as a parameter to the 
     * parser's getPrintString() and isAffectingEvent() methods
     * 
     * @return ParserOptions the parser options
     */
	public ParserOptions getParserOptions();

    /**
     * Method getParser.
     * @return the <code>IParser</code> that is used to retrieve the proper formating 
     * of the edit text and to create the actual editing command which will modify the model.
     */
	public IParser getParser();

    /**
     * @return the <code>IContentAssistProcessor</code> that is a content assist processor proposes 
     * completions and computes context information for a particular content type 
     */
	public IContentAssistProcessor getCompletionProcessor();
}
