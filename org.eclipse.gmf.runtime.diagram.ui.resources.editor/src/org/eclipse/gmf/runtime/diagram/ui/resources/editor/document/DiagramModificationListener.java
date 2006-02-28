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
package org.eclipse.gmf.runtime.diagram.ui.resources.editor.document;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.DemultiplexingListener;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.notation.Diagram;


/**
 * Listener for modification events on a diagram.
 * 
 * This listener is well suited for detecting events that make a diagram dirty. 
 * 
 * @author mgoyal, wdiu
 */
public class DiagramModificationListener {
	
	private ResourceSetListener diagramChangeListener = null;
	
	private TransactionalEditingDomain editingDomain;
	
	/**
	 * Constructs a modification listener which listens to modifications on
	 * a diagram.
	 * 
	 * @param documentProvider the FileDocumentProvider to handle the document
	 * being dirtied
	 * @param document the DiagramDocument being dirtied
	 * @param element the IFileEditorInput that contains the file being saved
	 */
	public DiagramModificationListener(
			final AbstractDocumentProvider documentProvider,
			final DiagramDocument document) {
		
		final Diagram diagram = document.getDiagram();
		editingDomain = TransactionUtil.getEditingDomain(diagram);

		NotificationFilter diagramResourceModifiedFilter = NotificationFilter
			.createNotifierFilter(diagram.eResource()).and(
				NotificationFilter.createEventTypeFilter(Notification.SET))
			.and(
				NotificationFilter.createFeatureFilter(Resource.class,
					Resource.RESOURCE__IS_MODIFIED));

		if (diagramChangeListener == null) {
			diagramChangeListener = new DemultiplexingListener(
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
							if (notification.getNewBooleanValue() == true
								&& notification.getOldBooleanValue() == false) {
								if (root instanceof EObject
									&& ((EObject) root).eResource() != null
									&& ((EObject) root).eResource().equals(
										notifierResource)
									&& notifierResource.isLoaded()) {

									document.setContent(document.getContent());
								}
							}
						}
					}
				}

			};
		}
	}

	public void startListening() {
		getEditingDomain().addResourceSetListener(diagramChangeListener);
	}
	
	public void stopListening() {
		getEditingDomain().removeResourceSetListener(diagramChangeListener);
	}
	
	/**
	 * Gets the editingDomain.
	 * @return Returns the editingDomain.
	 */
	protected TransactionalEditingDomain getEditingDomain() {
		return editingDomain;
	}

}
