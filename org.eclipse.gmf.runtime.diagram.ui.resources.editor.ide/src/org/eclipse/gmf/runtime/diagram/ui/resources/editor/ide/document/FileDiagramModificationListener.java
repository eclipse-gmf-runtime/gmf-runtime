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

import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.DiagramDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.DiagramModificationListener;
import org.eclipse.gmf.runtime.emf.core.edit.MFilter;
import org.eclipse.gmf.runtime.emf.core.edit.MListener;
import org.eclipse.gmf.runtime.emf.core.util.ResourceUtil;
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

	private MListener diagramSavedListener = null;

	/**
	 * Constructs a modification listener which listens to modifications on
	 * a diagram and handles saving of files.
	 * 
	 * @param documentProvider the FileDocumentProvider to handle the document
	 * being saved
	 * @param document the DiagramDocument being saved
	 * @param input the IFileEditorInput that contains the file being saved
	 */
	public FileDiagramModificationListener(
			final FileDocumentProvider documentProvider,
			final DiagramDocument document, final IFileEditorInput input) {
		super(documentProvider, document);
		MFilter diagramResourceSavedFilter = new MFilter() {

			public boolean matches(Notification notification) {
				Diagram diagram = document.getDiagram();
				Object notifier = notification.getNotifier();
				if (diagram != null && notifier instanceof Resource) {
					Resource diagramResource = diagram.eResource();
					Resource notifierResource = (Resource) notifier;
					if (notifierResource == diagramResource) {
						if (notification.getEventType() == Notification.SET) {
							Resource resource = (Resource) notifier;
							EObject root = ResourceUtil.getFirstRoot(resource);
							int featureID = notification
								.getFeatureID(Resource.class);
							if (featureID == Resource.RESOURCE__IS_MODIFIED
								&& notification.getNewBooleanValue() == false) {
								if (resource != null && root != null
									&& root.eResource() != null
									&& root.eResource().equals(resource)
									&& resource.isLoaded()) {
									return true;
								}
							}
						}
					}
				}
				return false;
			};
		};
		if (diagramSavedListener == null) {
			diagramSavedListener = new MListener(diagramResourceSavedFilter) {

				public void onEvent(List events) {
					document.setContent(document.getContent(), documentProvider
						.computeModificationStamp(input
							.getFile()));

					// this sets the timestamp
					documentProvider
						.handleExistingDocumentSaved(input);
				}
			};
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.DiagramModificationListener#startListening()
	 */
	public void startListening() {
		super.startListening();
		diagramSavedListener.startListening();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.DiagramModificationListener#stopListening()
	 */
	public void stopListening() {
		diagramSavedListener.stopListening();
		super.stopListening();
	}

}
