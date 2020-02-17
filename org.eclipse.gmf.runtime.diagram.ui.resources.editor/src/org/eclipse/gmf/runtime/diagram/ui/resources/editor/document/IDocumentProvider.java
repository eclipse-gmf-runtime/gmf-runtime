/******************************************************************************
 * Copyright (c) 2000, 2005  IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.document;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;



/**
 * A document provider maps between domain elements and documents. A document provider has the
 * following responsibilities:
 * <ul>
 * <li>create and manage a content representation, i.e., a document, of a domain model element
 * <li>create and save the content of domain model elements based on given documents
 * <li>update the documents this document provider manages for domain model elements to changes
 * directly applied to those domain model elements
 * <li>notify all element state listeners about changes directly applied to domain model elements
 * this document provider manages a document for, i.e. the document provider must know which changes
 * of a domain model element are to be interpreted as element moves, deletes, etc.
 * </ul>
 * A single document provider may be shared between multiple editors; the
 * methods take the editors' input elements as a parameter.
 * <p>
 * This interface may be implemented by clients; or subclass the standard abstract base class
 * <code>AbstractDocumentProvider</code>.
 * </p>
 *
 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocument
 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.AbstractDocumentProvider
 */
public interface IDocumentProvider {

	/**
	 * Connects the given element to this document provider. This tells the provider
	 * that caller of this method is interested to work with the document provided for
	 * the given domain model element. By counting the invocations of this method and
	 * <code>disconnect(Object)</code> this provider can assume to know the
	 * correct number of clients working with the document provided for that
	 * domain model element. <p>
	 * The given element must not be <code>null</code>.
	 *
	 * @param element the element
	 * @exception CoreException if the document representation could not be created
	 */
	void connect(Object element) throws CoreException;

	/**
	 * Disconnects the given element from this document provider. This tells the provider
	 * that the caller of this method is no longer interested in working with the document
	 * provided for the given domain model element. By counting the invocations of
	 * <code>connect(Object)</code> and of this method this provider can assume to
	 * know the correct number of clients working with the document provided for that
	 * domain model element. <p>
	 * The given element must not be <code>null</code>.
	 *
	 * @param element the element
	 */
	void disconnect(Object element);

	/**
	 * Returns the document for the given element. Usually the document contains
	 * a content presentation of the content of the element, or is the element itself.
	 *
	 * @param element the element, or <code>null</code>
	 * @return the document, or <code>null</code> if none
	 */
	IDocument getDocument(Object element);

	/**
	 * Resets the given element's document to its last saved state.
	 * Element state listeners are notified both before (<code>elementContentAboutToBeReplaced</code>)
	 * and after (<code>elementContentReplaced</code>) the content is changed.
	 *
	 * @param element the element, or <code>null</code>
	 * @exception CoreException if document could not be reset for the given element
	 */
	void resetDocument(Object element) throws CoreException;

	/**
	 * Saves the given document provided for the given element.
	 *
	 * @param monitor a progress monitor to report progress and request cancellation
	 * @param element the element, or <code>null</code>
	 * @param document the document
	 * @param overwrite indicates whether overwrite should be performed
	 * 			while saving the given element if necessary
	 * @exception CoreException if document could not be stored to the given element
	 */
	void saveDocument(IProgressMonitor monitor, Object element, IDocument document, boolean overwrite) throws CoreException;

	/**
	 * Returns the modification stamp of the given element.
	 *
	 * @param element the element
	 * @return the modification stamp of the given element
	 */
	long getModificationStamp(Object element);

	/**
	 * Returns the time stamp of the last synchronization of
	 * the given element and it's provided document.
	 *
	 * @param element the element
	 * @return the synchronization stamp of the given element
	 */
	long getSynchronizationStamp(Object element);

	/**
	 * Returns whether the given element has been deleted.
	 *
	 * @param element the element
	 * @return <code>true</code> if the element has been deleted
	 */
	boolean isDeleted(Object element);

	/**
	 * Returns whether the document provided for the given element must be saved.
	 *
	 * @param element the element, or <code>null</code>
	 * @return <code>true</code> if the document must be saved, and
	 *   <code>false</code> otherwise (including the element is <code>null</code>)
	 */
	boolean mustSaveDocument(Object element);

	/**
	 * Returns whether the document provided for the given element differs from
	 * its original state which would required that it be saved.
	 *
	 * @param element the element, or <code>null</code>
	 * @return <code>true</code> if the document can be saved, and
	 *   <code>false</code> otherwise (including the element is <code>null</code>)
	 */
	boolean canSaveDocument(Object element);

	/**
	 * Informs this document provider about upcoming changes of the given element.
	 * The changes might cause change notifications specific for the type of the given element.
	 * If this provider manages a document for the given element, the document provider
	 * must not change the document because of the notifications received after <code>
	 * aboutToChange</code> has been and before <code>changed</code> is called. In this case,
	 * it is assumed that the document is already up to date, e.g., a save operation is a
	 * typical case. <p>
	 * The concrete nature of the change notification depends on the concrete type of the
	 * given element. If the element is, e.g., an <code>IResource</code> the notification
	 * is a resource delta.
	 *
	 * @param element the element, or <code>null</code>
	 */
	void aboutToChange(Object element);

	/**
	 * Informs this document provider that the given element has been changed.
	 * All notifications have been sent out. If this provider manages a document
	 * for the given element, the document provider  must from now on change the
	 * document on the receipt of change notifications. The concrete nature of the change
	 * notification depends on the concrete type of the given element. If the element is,
	 * e.g., an <code>IResource</code> the notification is a resource delta.
	 *
	 * @param element the element, or <code>null</code>
	 */
	void changed(Object element);

	/**
	 * Adds the given element state listener to this document provider.
	 * Has no effect if an identical listener is already registered.
	 *
	 * @param listener the listener
	 */
	void addElementStateListener(IElementStateListener listener);

	/**
	 * Removes the given element state listener from this document provider.
	 * Has no affect if an identical listener is not registered.
	 *
	 * @param listener the listener
	 */
	void removeElementStateListener(IElementStateListener listener);


	/**
	 * Returns whether the document provider thinks that the given element is read-only.
	 * If this method returns <code>true</code>, <code>saveDocument</code> could fail.
	 * This method does not say anything about the document constructed from the given
	 * element. If the given element is not connected to this document provider, the return
	 * value is undefined. Document providers are allowed to use a cache to answer this
	 * question, i.e. there can be a difference between the "real" state of the element and
	 * the return value.
	 *
	 * @param element the element
	 * @return <code>true</code> if the given element is read-only, <code>false</code> otherwise
	 */
	boolean isReadOnly(Object element);

	/**
	 * Returns whether the document provider thinks that the given element can persistently be modified.
	 * This is orthogonal to <code>isReadOnly</code> as read-only elements may be modifiable and
	 * writable elements may not be modifiable. If the given element is not connected to this document
	 * provider, the result is undefined. Document providers are allowed to use a cache to answer this
	 * question, i.e. there can be a difference between the "real" state of the element and the return
	 * value.
	 *
	 * @param element the element
	 * @return <code>true</code> if the given element is modifiable, <code>false</code> otherwise
	 */
	boolean isModifiable(Object element);

	/**
	 * Validates the state of the given element. This method  may change the "real" state of the
	 * element. If using, it also updates the internal caches, so that this method may also change
	 * the results returned by <code>isReadOnly</code> and <code>isModifiable</code>. If the
	 * given element is not connected to this document provider, the effect is undefined.
	 *
	 * @param element the element
	 * @param computationContext the context in which the computation is performed, e.g., a SWT shell
	 * @exception CoreException if validating fails
	 */
	void validateState(Object element, Object computationContext) throws CoreException;

	/**
	 * Returns whether the state of the given element has been validated.
	 *
	 * @param element the element
	 * @return <code>true</code> if the state has been validated
	 */
	boolean isStateValidated(Object element);

	/**
	 * Updates the state cache for the given element. This method may change the result returned
	 * by <code>isReadOnly</code> and <code>isModifiable</code>. If the given element is not
	 * connected to this document provider, the effect is undefined.
	 *
	 * @param element the element
	 * @exception CoreException if validating fails
	 */
	void updateStateCache(Object element) throws CoreException;

	/**
	 * Marks the document managed for the given element as saveable. I.e.
	 * <code>canBeSaved(element)</code> will return <code>true</code>
	 * afterwards.
	 *
	 * @param element the element
	 */
	void setCanSaveDocument(Object element);

	/**
	 * Returns the status of the given element.
	 *
	 * @param element the element
	 * @return the status of the given element
	 */
	IStatus getStatus(Object element);

	/**
	 * Synchronizes the document provided for the given element with the
	 * given element. After that call <code>getSynchronizationTimeStamp</code>
	 * and <code>getModificationTimeStamp</code> return the same value.
	 *
	 * @param element the element
	 * @exception CoreException  if the synchronization could not be performed
	 */
	void synchronize(Object element) throws CoreException;


	/**
	 * Sets this providers progress monitor.
	 * @param progressMonitor
	 */
	void setProgressMonitor(IProgressMonitor progressMonitor);

	/**
	 * Returns this providers progress monitor.
	 * @return IProgressMonitor
	 */
	IProgressMonitor getProgressMonitor();

	/**
	 * Returns whether the information provided for the given element is in sync with the element.
	 *
	 * @param element the element
	 * @return <code>true</code> if the information is in sync with the element, <code>false</code> otherwise
	 */
	boolean isSynchronized(Object element);
}
