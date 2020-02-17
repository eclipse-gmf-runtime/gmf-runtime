/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.transaction.NotificationFilter;
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
public class DiagramModificationListener extends EContentAdapter {

	private TransactionalEditingDomain editingDomain;

	private NotificationFilter diagramResourceModifiedFilter;
	
	private DiagramDocument document;
	
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
			AbstractDocumentProvider documentProvider,
			DiagramDocument document) {
		this.document = document;
		Diagram diagram = document.getDiagram();
		editingDomain = TransactionUtil.getEditingDomain(diagram);

		diagramResourceModifiedFilter = NotificationFilter
            .createNotifierFilter(diagram.eResource()).and(
                NotificationFilter.createEventTypeFilter(Notification.SET).or(
                    NotificationFilter
                        .createEventTypeFilter(Notification.UNSET))).and(
                NotificationFilter.createFeatureFilter(Resource.class,
                    Resource.RESOURCE__IS_MODIFIED));
	}

	public void startListening() {
		EList adapters = getEditingDomain().getResourceSet().eAdapters();
		if (!adapters.contains(this)) {
				adapters.add(this);
		}
	}
	
	public void stopListening() {
		getEditingDomain().getResourceSet().eAdapters().remove(this);
	}
	
	/**
	 * Gets the editingDomain.
	 * @return Returns the editingDomain.
	 */
	protected TransactionalEditingDomain getEditingDomain() {
		return editingDomain;
	}

	protected DiagramDocument getDiagramDocument() {
		return document;
	}
	
	public boolean isAdapterForType(Object type) {
		return type == DiagramModificationListener.class;
	}

	public void notifyChanged(Notification notification) {
		if (notification.getNotifier() instanceof ResourceSet) {
			super.notifyChanged(notification);
		}
		
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
					if (notification.getNewBooleanValue() == true
						&& notification.getOldBooleanValue() == false) {
						if (root instanceof EObject
							&& ((EObject) root).eResource() != null
							&& ((EObject) root).eResource().equals(
								notifierResource)
							&& notifierResource.isLoaded()) {
	
							getDiagramDocument().setContent(getDiagramDocument().getContent());
						}
					}
				}
			}
		}
	}

	public void unsetTarget(Notifier oldTarget) {
		if (oldTarget instanceof ResourceSet) {
		    super.unsetTarget(oldTarget);
		}
	}

	public Notifier getTarget() {
		return null;
	}

	public void setTarget(Notifier newTarget) {
		if (newTarget instanceof ResourceSet) {
		    super.setTarget(newTarget);
		}
	}
	
}
