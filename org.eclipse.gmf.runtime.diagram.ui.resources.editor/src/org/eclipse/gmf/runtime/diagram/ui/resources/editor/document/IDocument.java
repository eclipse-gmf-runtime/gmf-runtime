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
 * An <code>IDocument</code> represents extensible content providing support
 *
 * A document allows to set its content and to manipulate it. 
 * On each document change, all registered document listeners are informed exactly once.
 * 
 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocumentListener
 */
public interface IDocument {
	/**
	 * The unknown modification stamp.
	 */
	long UNKNOWN_MODIFICATION_STAMP= -1;

	/**
	 * Returns the modification stamp of this document. The modification stamp
	 * is updated each time a modifying operation is called on this document. If
	 * two modification stamps of the same document are identical then the document
	 * content is too, however, same content does not imply same modification stamp.
	 * <p>
	 * The magnitude or sign of the numerical difference between two modification stamps
	 * is not significant.
	 * </p>
	 *
	 * @return the modification stamp of this document or <code>UNKNOWN_MODIFICATION_STAMP</code>
	 */
	long getModificationStamp();
	
	/**
	 * Registers the document listener with the document. After registration
	 * the IDocumentListener is informed about each change of this document.
	 * If the listener is already registered nothing happens.<p>
	 * An <code>IDocumentListener</code> may call back to this method
	 * when being inside a document notification.
	 *
	 * @param listener the listener to be registered
	 */
	void addDocumentListener(IDocumentListener listener);

	/**
	 * Removes the listener from the document's list of document listeners.
	 * If the listener is not registered with the document nothing happens.<p>
	 * An <code>IDocumentListener</code> may call back to this method
	 * when being inside a document notification.
	 *
	 * @param listener the listener to be removed
	 */
	void removeDocumentListener(IDocumentListener listener);

	/**
	 * Adds the given document listener as one which is notified before
	 * those document listeners added with <code>addDocumentListener</code>
	 * are notified. If the given listener is also registered using
	 * <code>addDocumentListener</code> it will be notified twice.
	 * If the listener is already registered nothing happens.<p>
	 *
	 * This method is not for public use.
	 *
	 * @param documentAdapter the listener to be added as pre-notified document listener
	 *
	 * @see #removePrenotifiedDocumentListener(IDocumentListener)
	 */
	void addPrenotifiedDocumentListener(IDocumentListener documentAdapter);

	/**
	 * Removes the given document listener from the document's list of
	 * pre-notified document listeners. If the listener is not registered
	 * with the document nothing happens. <p>
	 *
	 * This method is not for public use.
	 *
	 * @param documentAdapter the listener to be removed
	 *
	 * @see #addPrenotifiedDocumentListener(IDocumentListener)
	 */
	void removePrenotifiedDocumentListener(IDocumentListener documentAdapter);
	
	/**
	 * Returns this document's contents.
	 *
	 * @return the document's contents
	 */
	Object getContent();
	
	/**
	 * Replaces the content of the document with the given content.
	 * Sends a <code>DocumentEvent</code> to all registered <code>IDocumentListener</code>.
	 *
	 * @param documentContent the new content of the document
	 *
	 * @see DocumentEvent
	 * @see IDocumentListener
	 */
	void setContent(Object documentContent);
	
	/**
	 * Replaces the content of the document with the given text.
	 * Sends a <code>DocumentEvent</code> to all registered <code>IDocumentListener</code>.
	 *
	 * @param content the new content of the document
	 * @param modificationStamp of the document after setting the content
	 *
	 * @see DocumentEvent
	 * @see IDocumentListener
	 */
	void setContent(Object content, long modificationStamp);
	
	/**
	 * The affect of these calls is that no document listener is notified
	 * until <code>resumeListenerNotification</code> is called. This allows clients
	 * to update structure before any listener is informed about the change.<p>
	 * Listener notification can only be stopped for a single <code>replace</code> operation.
	 * Otherwise, document change notifications will be lost.
	 */
	void stopListenerNotification();

	/**
	 * Resumes the notification of document listeners which must previously
	 * have been stopped by a call to <code>stopListenerNotification</code>.
	 */
	void resumeListenerNotification();
}
