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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Abstract implementation of <code>IDocument</code>. This class
 * provides implementation for registering <code>IDocumentListener</code>s.
 * Additionally it also handles firing of <code>DocumentEvent</code> to
 * the document listeners. 
 */
public abstract class AbstractDocument
	implements IDocument {

	/**
	 * Content of this document.
	 */
	protected Object content;
	
	/**
	 * The current modification stamp.
	 */
	private long fModificationStamp= IDocument.UNKNOWN_MODIFICATION_STAMP;

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.IDocument#getModificationStamp()
	 */
	public long getModificationStamp() {
		return fModificationStamp;
	}

	/** The registered document listeners */
	private List fDocumentListeners;
	/** The registered pre-notified document listeners */
	private List fPrenotifiedDocumentListeners;

	/**
	 * Indicates whether the notification of listeners has been stopped.
	 */
	private int fStoppedListenerNotification= 0;
	
	/**
	 * The document event to be sent after listener notification has been resumed.
	 */
	private DocumentEvent fDeferredDocumentEvent;

	/**
	 * Initializes document listeners, positions, and position updaters.
	 * Must be called inside the constructor after the implementation plug-ins
	 * have been set.
	 */
	protected void completeInitialization() {
		fDocumentListeners= new ArrayList();
		fPrenotifiedDocumentListeners= new ArrayList();
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.IDocument#addDocumentListener(org.eclipse.gmf.runtime.diagram.ui.editor.IDocumentListener)
	 */
	public void addDocumentListener(IDocumentListener listener) {
		assert listener != null;
		if (! fDocumentListeners.contains(listener))
			fDocumentListeners.add(listener);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.IDocument#removeDocumentListener(org.eclipse.gmf.runtime.diagram.ui.editor.IDocumentListener)
	 */
	public void removeDocumentListener(IDocumentListener listener) {
		assert listener != null;
		fDocumentListeners.remove(listener);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.IDocument#addPrenotifiedDocumentListener(org.eclipse.gmf.runtime.diagram.ui.editor.IDocumentListener)
	 */
	public void addPrenotifiedDocumentListener(IDocumentListener listener) {
		assert listener != null;
		if (! fPrenotifiedDocumentListeners.contains(listener))
			fPrenotifiedDocumentListeners.add(listener);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.IDocument#removePrenotifiedDocumentListener(org.eclipse.gmf.runtime.diagram.ui.editor.IDocumentListener)
	 */
	public void removePrenotifiedDocumentListener(
			IDocumentListener listener) {
		assert listener != null;
		fPrenotifiedDocumentListeners.remove(listener);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.IDocument#getContent()
	 */
	public Object getContent() {
		return content;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.IDocument#setContent(java.lang.Object)
	 */
	public void setContent(Object documentContent) {
		setContent(documentContent, getNextModificationStamp());
	}
	
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.IDocument#setContent(java.lang.Object, long)
	 */
	public void setContent(Object documentContent, long modificationStamp) {
		DocumentEvent changed= new DocumentEvent(this, DocumentEvent.CONTENT_REPLACED, documentContent);
		DocumentEvent changing= new DocumentEvent(this, DocumentEvent.CONTENT_REPLACED, content);
		fireDocumentAboutToBeChanged(changed);

		content = documentContent;

		fModificationStamp= modificationStamp;
		changed.fModificationStamp= fModificationStamp;
		changing.fModificationStamp = fModificationStamp;

		fireDocumentChanged(changing);
	}

	private long getNextModificationStamp() {
		if (fModificationStamp == Long.MAX_VALUE || fModificationStamp == IDocument.UNKNOWN_MODIFICATION_STAMP)
			return 0;

		return fModificationStamp + 1;
	}

	/**
	 * Fires the given document event to all registers document listeners informing them
	 * about the forthcoming document manipulation. Uses a robust iterator.
	 *
	 * @param event the event to be sent out
	 */
	protected void fireDocumentAboutToBeChanged(DocumentEvent event) {

		if (fPrenotifiedDocumentListeners.size() > 0) {

			List list= new ArrayList(fPrenotifiedDocumentListeners);
			Iterator e= list.iterator();
			while (e.hasNext()) {
				IDocumentListener l= (IDocumentListener) e.next();
				l.documentAboutToBeChanged(event);
			}
		}

		if (fDocumentListeners.size() > 0) {

			List list= new ArrayList(fDocumentListeners);
			Iterator e= list.iterator();
			while (e.hasNext()) {
				IDocumentListener l= (IDocumentListener) e.next();
				l.documentAboutToBeChanged(event);
			}
		}
	}
	/**
	 * Updates the internal document structures and informs all document listeners
	 * if listener notification has been enabled. Otherwise it remembers the event
	 * to be sent to the listeners on resume.
	 *
	 * @param event the document event to be sent out
	 */
	protected void fireDocumentChanged(DocumentEvent event) {
		if (fStoppedListenerNotification == 0)
			doFireDocumentChanged(event);
		else
			fDeferredDocumentEvent= event;
	}

	/**
	 * Notifies all listeners about the given document change. Uses a robust
	 * iterator.
	 * <p>
	 * Executes all registered post notification replace operation.
	 * <p>
	 * This method will be renamed to <code>doFireDocumentChanged</code>.
	 *
	 * @param event the event to be sent out
	 */
	protected void doFireDocumentChanged(DocumentEvent event) {

		if (fPrenotifiedDocumentListeners.size() > 0) {
			List list= new ArrayList(fPrenotifiedDocumentListeners);
			Iterator e= list.iterator();
			while (e.hasNext()) {
				IDocumentListener l= (IDocumentListener) e.next();
				l.documentChanged(event);
			}
		}

		if (fDocumentListeners.size() > 0) {

			List list= new ArrayList(fDocumentListeners);
			Iterator e= list.iterator();
			while (e.hasNext()) {
				IDocumentListener l= (IDocumentListener) e.next();
				l.documentChanged(event);
			}
		}
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.IDocument#resumeListenerNotification()
	 */
	public void resumeListenerNotification() {
		-- fStoppedListenerNotification;
		if (fStoppedListenerNotification == 0) {
			resumeDocumentListenerNotification();
		}
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.IDocument#stopListenerNotification()
	 */
	public void stopListenerNotification() {
		++ fStoppedListenerNotification;
	}

	/**
	 * Resumes the document listener notification by sending out the remembered
	 * partition changed and document event.
	 */
	private void resumeDocumentListenerNotification() {
		if (fDeferredDocumentEvent != null) {
			DocumentEvent event= fDeferredDocumentEvent;
			fDeferredDocumentEvent= null;
			doFireDocumentChanged(event);
		}
	}

}
