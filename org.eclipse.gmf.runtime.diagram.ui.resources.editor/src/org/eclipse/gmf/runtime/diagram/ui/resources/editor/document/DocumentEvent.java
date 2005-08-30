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
 * Specification of changes applied to documents. 
 * The event contains the changed document.
 *
 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocument
 */
public class DocumentEvent {

	/** The changed document */
	public IDocument fDocument;
	
	public int fEventKind;
	
	public Object fEventInfo;
	/**
	 * The modification stamp of the document when firing this event.
	 * @since 3.1
	 */
	protected long fModificationStamp;
	
	public static final int CONTENT_REPLACED = 0;
	public static final int CONTENT_MODIFIED = 1;

	/**
	 * Creates a new document event.
	 *
	 * @param doc the changed document
	 */
	public DocumentEvent(IDocument doc, int eventKind, Object eventInfo) {

		assert doc != null;

		fDocument= doc;
		fEventKind = eventKind;
		fEventInfo = eventInfo;
		fModificationStamp = fDocument.getModificationStamp();
	}

	/**
	 * Creates a new, not initialized document event.
	 */
	public DocumentEvent() {
		//default constructor
	}

	/**
	 * Returns the changed document.
	 *
	 * @return the changed document
	 */
	public IDocument getDocument() {
		return fDocument;
	}
	/**
	 * Returns the document's modification stamp at the
	 * time when this event was sent.
	 *
	 * @return the modification stamp or {@link IDocument#UNKNOWN_MODIFICATION_STAMP}.
	 */
	public long getModificationStamp() {
		return fModificationStamp;
	}

	
	/**
	 * @return The Event Kind
	 */
	public int getEventKind() {
		return fEventKind;
	}

	/**
	 * @return The Event Information
	 */
	public Object getEventInfo() {
		return fEventInfo;
	}
}
