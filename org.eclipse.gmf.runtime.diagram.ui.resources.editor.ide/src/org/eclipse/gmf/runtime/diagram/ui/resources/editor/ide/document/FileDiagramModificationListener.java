/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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
import org.eclipse.emf.transaction.NotificationFilter;
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

	private NotificationFilter diagramResourceModifiedFilter;
	
	private FileDocumentProvider documentProvider;
	
	private IFileEditorInput input;
	
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
		this.documentProvider = documentProvider;
		this.input = input;
		
		Diagram diagram = document.getDiagram();

		diagramResourceModifiedFilter = NotificationFilter
            .createNotifierFilter(diagram.eResource()).and(
                NotificationFilter.createEventTypeFilter(Notification.SET).or(
                    NotificationFilter
                        .createEventTypeFilter(Notification.UNSET))).and(
                NotificationFilter.createFeatureFilter(Resource.class,
                    Resource.RESOURCE__IS_MODIFIED));
	}

	public boolean isAdapterForType(Object type) {
		return type == FileDiagramModificationListener.class
			|| super.isAdapterForType(type);
	}

	public void notifyChanged(Notification notification) {
		super.notifyChanged(notification);
		if (diagramResourceModifiedFilter.matches(notification)) {
			// provide further filtering not available with the
			// NotificationFilter
			if (getDiagramDocument().getDiagram() != null
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
	
							getDiagramDocument().setContent(getDiagramDocument().getContent(),
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
	}
}
