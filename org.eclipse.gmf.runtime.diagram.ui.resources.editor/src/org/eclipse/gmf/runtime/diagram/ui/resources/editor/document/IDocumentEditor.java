/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gmf.runtime.diagram.ui.resources.editor.document;


/**
 * An interface to be implemented by all the editors displaying a diagram
 */
public interface IDocumentEditor {

	/**
	 * Returns this text editor's document provider.
	 *
	 * @return the document provider or <code>null</code> if none, e.g. after closing the editor
	 */
	IDocumentProvider getDocumentProvider();

	/**
	 * Closes this text editor after optionally saving changes.
	 *
	 * @param save <code>true</code> if unsaved changed should be saved, and
	 *   <code>false</code> if unsaved changed should be discarded
	 */
	void close(boolean save);

	/**
	 * Returns whether the text in this text editor can be changed by the user.
	 *
	 * @return <code>true</code> if it can be edited, and <code>false</code> if it is read-only
	 */
	boolean isEditable();

	/**
	 * Returns whether the editor's input is read-only. The semantics of
	 * this method is orthogonal to <code>isEditable</code> as it talks about the
	 * editor input, i.e. the domain element, and <b>not</b> about the editor
	 * document.
	 *
	 * @return <code>true</code> if the editor input is read-only
	 */
	boolean isEditorInputReadOnly();


	/**
	 * Returns whether the editor's input can be persistently be modified.
	 * This is orthogonal to <code>ITextEditorExtension.isEditorInputReadOnly</code> as read-only elements may be modifiable and
	 * writable elements may not be modifiable. If the given element is not connected to this document
	 * provider, the result is undefined. Document providers are allowed to use a cache to answer this
	 * question, i.e. there can be a difference between the "real" state of the element and the return
	 * value.
	 *
	 * @return <code>true</code> if the editor input is modifiable
	 */
	boolean isEditorInputModifiable();

	/**
	 * Validates the state of the given editor input. The predominate intent
	 * of this method is to take any action probably necessary to ensure that
	 * the input can persistently be changed.
	 *
	 * @return <code>true</code> if the input was validated, <code>false</code> otherwise
	 */
	boolean validateEditorInputState();
}
