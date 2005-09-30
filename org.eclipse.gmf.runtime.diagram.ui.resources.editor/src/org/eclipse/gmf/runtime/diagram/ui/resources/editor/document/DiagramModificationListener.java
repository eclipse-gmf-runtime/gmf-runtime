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

import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.emf.core.edit.MFilter;
import org.eclipse.gmf.runtime.emf.core.edit.MListener;
import org.eclipse.gmf.runtime.emf.core.util.ResourceUtil;
import org.eclipse.gmf.runtime.notation.Diagram;


/**
 * Listener for modification events on a diagram.
 * 
 * This listener is well suited for detecting events that make a diagram dirty. 
 * 
 * @author mgoyal, wdiu
 */
public class DiagramModificationListener {
	
	private MListener diagramChangeListener = null;
	
	/**
	 * Constructs a modification listener which listens to modifications on
	 * a diagram.
	 * 
	 * @param documentProvider the FileDocumentProvider to handle the document
	 * being dirtied
	 * @param document the DiagramDocument being dirtied
	 * @param element the IFileEditorInput that contains the file being saved
	 */
	public DiagramModificationListener(final AbstractDocumentProvider documentProvider, final DiagramDocument document) {
		MFilter diagramResourceModifiedFilter = new MFilter() {
			public boolean matches(Notification notification) {
				Diagram diagram = document.getDiagram();
				Object notifier = notification.getNotifier();
				if(diagram != null && notifier instanceof Resource) {
					Resource diagramResource = diagram.eResource();
					Resource notifierResource = (Resource)notifier;
					if(notifierResource == diagramResource) {
						if (notification.getEventType() == Notification.SET) {
							Resource resource = (Resource) notifier;
							EObject root = ResourceUtil.getFirstRoot(resource);
							int featureID = notification.getFeatureID(Resource.class);
							if (featureID == Resource.RESOURCE__IS_MODIFIED
								&& notification.getNewBooleanValue() == true && notification
									.getOldBooleanValue() == false) {
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
		if(diagramChangeListener == null) {
			diagramChangeListener = new MListener(diagramResourceModifiedFilter) {
				public void onEvent(List events) {
					document.setContent(document.getContent());
				}
			};
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocument#enableDiagramListener()
	 */
	public void startListening() {
		diagramChangeListener.startListening();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocument#disableDiagramListener()
	 */
	public void stopListening() {
		diagramChangeListener.stopListening();
	}
}
