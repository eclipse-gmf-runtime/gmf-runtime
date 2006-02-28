/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.document;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.DemultiplexingListener;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.DiagramDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.DiagramModificationListener;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.ui.IFileEditorInput;

/**
 * Listener for modification events on a diagram.
 * 
 * This listener is well suited for detecting save events. 
 * 
 * @author mgoyal, wdiu
 */
public class FileDiagramModificationListener
	extends DiagramModificationListener {

	private ResourceSetListener diagramSavedListener = null;

	/**
	 * Constructs a modification listener which listens to modifications on a
	 * diagram and handles saving of files.
	 * 
	 * @param documentProvider
	 *            the FileDocumentProvider to handle the document being saved
	 * @param document
	 *            the DiagramDocument being saved
	 * @param input
	 *            the IFileEditorInput that contains the file being saved
	 */
	public FileDiagramModificationListener(
			final FileDocumentProvider documentProvider,
			final DiagramDocument document, final IFileEditorInput input) {

		super(documentProvider, document);

		final Diagram diagram = document.getDiagram();

		NotificationFilter diagramResourceModifiedFilter = NotificationFilter
			.createNotifierFilter(diagram.eResource()).and(
				NotificationFilter.createEventTypeFilter(Notification.SET))
			.and(
				NotificationFilter.createFeatureFilter(Resource.class,
					Resource.RESOURCE__IS_MODIFIED));

		if (diagramSavedListener == null) {
			diagramSavedListener = new DemultiplexingListener(
				diagramResourceModifiedFilter) {

				protected void handleNotification(TransactionalEditingDomain domain,
						Notification notification) {
					// provide further filtering not available with the
					// NotificationFilter
					if (diagram != null
						&& notification.getNotifier() instanceof Resource) {
						Resource notifierResource = (Resource) notification
							.getNotifier();

						EList contents = notifierResource.getContents();
						if (!contents.isEmpty()) {
							Object root = contents.get(0);

							if (notification.getNewBooleanValue() == false) {
								if (root instanceof EObject
									&& ((EObject) root).eResource() != null
									&& ((EObject) root).eResource().equals(
										notifierResource)
									&& notifierResource.isLoaded()) {

									document.setContent(document.getContent(),
										documentProvider
											.computeModificationStamp(input
												.getFile()));

									// this sets the timestamp
									documentProvider
										.handleExistingDocumentSaved(input);
								}
							}
						}
					}
				}
			};
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.DiagramModificationListener#startListening()
	 */
	public void startListening() {
		super.startListening();
		getEditingDomain().addResourceSetListener(diagramSavedListener);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.DiagramModificationListener#stopListening()
	 */
	public void stopListening() {
		getEditingDomain().removeResourceSetListener(diagramSavedListener);
		super.stopListening();
	}

}
