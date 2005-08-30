/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.resources.editor.document;

import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.edit.MFilter;
import org.eclipse.gmf.runtime.emf.core.edit.MListener;
import org.eclipse.gmf.runtime.emf.core.util.ResourceUtil;
import org.eclipse.gmf.runtime.notation.Diagram;


/**
 * An implementation of <code>IDiagramDocument</code>,
 * 
 * @author mgoyal
 */
public class DiagramDocument
	extends AbstractDocument
	implements IDiagramDocument {

	boolean fListenerEnabled = true;
	
	public DiagramDocument() {
		completeInitialization();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.IDiagramDocument#getDiagram()
	 */
	public Diagram getDiagram() {
		return (Diagram)getContent();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.diagramdocument.IDiagramDocument#detachDiagram()
	 */
	public Diagram detachDiagram() {
		Object oldContent = content;
		content = null;
		return (Diagram)oldContent;
	}
	
	// very expensive listener
	MFilter diagramResourceFilter = new MFilter() {
		public boolean matches(Notification notification) {
			Diagram diagram = getDiagram();
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
							&& notification.getNewBooleanValue() != notification
								.getOldBooleanValue()) {
							if (resource != null && root != null
								&& root.eResource() != null
								&& root.eResource().equals(resource)
								&& resource.isLoaded()) {

								if (notification.getNewBooleanValue()) {
									return true;
//								} else {
//									eventListener.handleResourceSavedEvent(
//										notification, resource);
								}
							}
						}				
					}
				}
			}
			return false;
		};
	};
	
	MListener diagramChangeListener = null;
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocument#enableDiagramListener()
	 */
	public boolean enableDiagramListener() {
		boolean oldState = fListenerEnabled;
		if(!oldState) {
			diagramChangeListener.startListening();
		}
		fListenerEnabled = true;
		return oldState;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocument#disableDiagramListener()
	 */
	public boolean disableDiagramListener() {
		boolean oldState = fListenerEnabled;
		if(oldState) {
			diagramChangeListener.stopListening();
		}
		fListenerEnabled = false;
		return oldState;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.AbstractDocument#completeInitialization()
	 */
	protected void completeInitialization() {
		super.completeInitialization();
		if(fListenerEnabled) {
			if(diagramChangeListener == null) {
				diagramChangeListener = new MListener(diagramResourceFilter) {
					public void onEvent(List events) {
						fireDocumentChanged(new DocumentEvent(DiagramDocument.this, DocumentEvent.CONTENT_MODIFIED, events));
					}
				};
			}
			diagramChangeListener.startListening();
		}
	}

	/**
	 * The editing domain for this document.
	 */
	private MEditingDomain fDomain = null;
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocument#getEditingDomain()
	 */
	public MEditingDomain getEditingDomain() {
		return fDomain;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocument#setEditingDomain(org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain)
	 */
	public void setEditingDomain(MEditingDomain domain) {
		if(fDomain != domain) {
			if(diagramChangeListener != null)
				diagramChangeListener.stopListening();
			
			diagramChangeListener = new MListener(domain, diagramResourceFilter) {
				public void onEvent(List events) {
					fireDocumentChanged(new DocumentEvent(DiagramDocument.this, DocumentEvent.CONTENT_MODIFIED, events));
				}
			};
			if(fListenerEnabled)
				diagramChangeListener.startListening();
		}
		fDomain = domain;
	}
}
