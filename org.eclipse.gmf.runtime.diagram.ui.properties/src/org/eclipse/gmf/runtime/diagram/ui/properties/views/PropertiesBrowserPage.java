/******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.properties.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.wst.common.ui.properties.internal.provisional.ITabbedPropertySheetPageContributor;
import org.eclipse.wst.common.ui.properties.internal.provisional.TabbedPropertySheetPage;

import org.eclipse.gmf.runtime.common.ui.action.actions.global.GlobalActionManager;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalActionId;
import org.eclipse.gmf.runtime.emf.core.edit.DemuxingMListener;
import org.eclipse.gmf.runtime.emf.core.edit.IDemuxedMListener;
import org.eclipse.gmf.runtime.emf.core.edit.MFilter;
import org.eclipse.gmf.runtime.emf.core.edit.MUndoInterval;
import org.eclipse.gmf.runtime.emf.ui.internal.MslUIPlugin;

/**
 * A property sheet page for modeler.
 * 
 * @author Anthony Hunter <a
 *         href="mailto:anthonyh@ca.ibm.com">anthonyh@ca.ibm.com </a>
 */
public class PropertiesBrowserPage
	extends TabbedPropertySheetPage
	implements IDemuxedMListener, IPropertyChangeListener {

	/**
	 * the contributor for this property sheet page
	 */
	private ITabbedPropertySheetPageContributor contributor;

	/**
	 * model event listener
	 */
	private DemuxingMListener eventListener = new DemuxingMListener(this);

	/**
	 * save reference to the workbench window if there is a
	 * modelerViewActivationListener.
	 */
	private IWorkbenchWindow modelerViewWorkbenchWindow;

	/**
	 * the current selection from model explorer.
	 */
	private IStructuredSelection modelerViewSelection = null;

	/**
	 * When the property sheet page is active for the model explorer (or other
	 * modeler view such as diagram navigator) and when you close a model using
	 * CTRL + F4 or File + Close All, a selection change event is not sent to
	 * the workbench, but the model explorer selection has changed. This could
	 * result in a model being closed, but the tabs still being displayed for
	 * that model based on the old selection (resulting in NPE when you do
	 * something). This listener is similar to what the PageRecBook does,
	 * listens for the editor close and then gets the next model explorer
	 * selection change event to refresh.
	 */
	private IPartListener modelerViewActivationListener = new IPartListener() {

		private boolean propertiesBrowserActive = false;

		public void partActivated(IWorkbenchPart part) {
			/*
			 * keep track whether the properties view is active.
			 */
			propertiesBrowserActive = (part instanceof PropertySheet && ((PropertySheet) part)
				.getCurrentPage().equals(PropertiesBrowserPage.this));
		}

		public void partBroughtToTop(IWorkbenchPart part) {
			/* not implemented */
		}

		public void partClosed(IWorkbenchPart part) {
			if (part instanceof IEditorPart && propertiesBrowserActive) {
				/*
				 * The properties view is active and we closed an editor.
				 */
				final IViewPart modelerView = (IViewPart) contributor;
				modelerView.getSite().getSelectionProvider()
					.addSelectionChangedListener(
						new ISelectionChangedListener() {

							public void selectionChanged(
									SelectionChangedEvent event) {
								IStructuredSelection newModelerViewSelection = (IStructuredSelection) modelerView
									.getSite().getSelectionProvider()
									.getSelection();
								modelerView.getSite().getSelectionProvider()
									.removeSelectionChangedListener(this);

								if (!newModelerViewSelection
									.equals(modelerViewSelection)) {
									/*
									 * the closed editor caused a selection
									 * change in the modeler view, send this to
									 * the property sheet page.
									 */
									PropertiesBrowserPage.this
										.selectionChanged(modelerView,
											newModelerViewSelection);
								}
							}
						});
			}
		}

		public void partDeactivated(IWorkbenchPart part) {
			/* not implemented */
		}

		public void partOpened(IWorkbenchPart part) {
			/* not implemented */
		}

	};

	private IStructuredSelection selectedElements;



	/**
	 * Constructor
	 * @param contributor the <code>ITabbedPropertySheetPageContributor</code> 
	 *  for this property sheet page
	 */
	public PropertiesBrowserPage(ITabbedPropertySheetPageContributor contributor) {
		super(contributor);

		this.contributor = contributor;

		// preference listener
		MslUIPlugin.getDefault().getPreferenceStore()
			.addPropertyChangeListener(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#dispose()
	 */
	public void dispose() {
		super.dispose();
		eventListener.stopListening();
		/**
		 * Remove the part activation listener.
		 */
		if (modelerViewWorkbenchWindow != null) {
			modelerViewWorkbenchWindow.getPartService().removePartListener(
				modelerViewActivationListener);
			modelerViewWorkbenchWindow = null;
		}

		/**
		 * Remove the preference listener
		 */
		MslUIPlugin.getDefault().getPreferenceStore()
			.removePropertyChangeListener(this);
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#setActionBars(org.eclipse.ui.IActionBars)
	 */
	public void setActionBars(IActionBars actionBars) {

		if (contributor != null && contributor instanceof IWorkbenchPart) {

			/*
			 * Override the undo and redo global action handlers to use the
			 * contributors action handlers
			 */
			IAction action = GlobalActionManager.getInstance()
				.getGlobalActionHandler((IWorkbenchPart) contributor,
					GlobalActionId.UNDO);
			if (action != null) {
				actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(),
					action);
			}

			action = GlobalActionManager.getInstance().getGlobalActionHandler(
				(IWorkbenchPart) contributor, GlobalActionId.REDO);
			if (action != null) {
				actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(),
					action);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.core.edit.IDemuxedMListener#getFilter()
	 */
	public MFilter getFilter() {
		return MFilter.ELEMENT_MODIFIED_FILTER;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.core.edit.IDemuxedMListener#handleResourceLoadedEvent(org.eclipse.emf.common.notify.Notification, org.eclipse.emf.ecore.resource.Resource)
	 */
	public void handleResourceLoadedEvent(Notification notification,
			Resource resource) {
		/* not implemented */
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.core.edit.IDemuxedMListener#handleResourceUnloadedEvent(org.eclipse.emf.common.notify.Notification, org.eclipse.emf.ecore.resource.Resource, org.eclipse.emf.ecore.EObject)
	 */
	public void handleResourceUnloadedEvent(Notification notification,
			Resource resource, EObject modelRoot) {
		/* not implemented */
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.core.edit.IDemuxedMListener#handleResourceDirtiedEvent(org.eclipse.emf.common.notify.Notification, org.eclipse.emf.ecore.resource.Resource)
	 */
	public void handleResourceDirtiedEvent(Notification notification,
			Resource resource) {
		/* not implemented */
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.core.edit.IDemuxedMListener#handleResourceImportedEvent(org.eclipse.emf.common.notify.Notification, org.eclipse.emf.ecore.resource.Resource)
	 */
	public void handleResourceImportedEvent(Notification notification,
			Resource resource) {
		/* not implemented */
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.core.edit.IDemuxedMListener#handleResourceExportedEvent(org.eclipse.emf.common.notify.Notification, org.eclipse.emf.ecore.resource.Resource)
	 */
	public void handleResourceExportedEvent(Notification notification,
			Resource resource) {
		/* not implemented */
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.core.edit.IDemuxedMListener#handleResourceSavedEvent(org.eclipse.emf.common.notify.Notification, org.eclipse.emf.ecore.resource.Resource)
	 */
	public void handleResourceSavedEvent(Notification notification,
			Resource resource) {
		/* not implemented */
	}
				
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.core.edit.IDemuxedMListener#handleElementCreatedEvent(org.eclipse.emf.common.notify.Notification, org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject)
	 */
	public void handleElementCreatedEvent(Notification notification,
			EObject owner, EObject newElement) {
		/* not implemented */
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.core.edit.IDemuxedMListener#handleElementDeletedEvent(org.eclipse.emf.common.notify.Notification, org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject)
	 */
	public void handleElementDeletedEvent(Notification notification,
			EObject owner, EObject oldElement) {
		/* not implemented */
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.core.edit.IDemuxedMListener#handleElementModifiedEvent(org.eclipse.emf.common.notify.Notification, org.eclipse.emf.ecore.EObject)
	 */
	public void handleElementModifiedEvent(Notification notification,
			EObject element) {
		/* not implemented */
	}


	/* (non-Javadoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		/* not implemented */
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.core.edit.IDemuxedMListener#handleUndoIntervalClosedEvent(org.eclipse.emf.common.notify.Notification, org.eclipse.gmf.runtime.emf.core.edit.MUndoInterval)
	 */
	public void handleUndoIntervalClosedEvent(Notification notification,
			MUndoInterval undoInterval) {
		/* not implemented */
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.core.edit.IDemuxedMListener#handleUndoIntervalsFlushedEvent(org.eclipse.emf.common.notify.Notification, org.eclipse.gmf.runtime.emf.core.edit.MUndoInterval)
	 */
	public void handleUndoIntervalsFlushedEvent(Notification notification,
			MUndoInterval undoInterval) {
		/* not implemented */
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#setFocus()
	 */
	public void setFocus() {
		getControl().setFocus();
	}


	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		super.createControl(parent);

		if (contributor instanceof IViewPart) {
			/**
			 * If this is the modeler view, add the special part activation
			 * listener.
			 */
			modelerViewWorkbenchWindow = getSite().getWorkbenchWindow();
			modelerViewWorkbenchWindow.getPartService().addPartListener(
				modelerViewActivationListener);
		}
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		super.selectionChanged(part, selection);

		if (selection instanceof IStructuredSelection)
			selectedElements = (IStructuredSelection) selection;

		if (modelerViewWorkbenchWindow != null && part.equals(contributor)) {
			/*
			 * save the current selection.
			 */
			IViewPart modelerView = (IViewPart) part;
			modelerViewSelection = (IStructuredSelection) modelerView.getSite()
				.getSelectionProvider().getSelection();
		}
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILabelProviderListener#labelProviderChanged(org.eclipse.jface.viewers.LabelProviderChangedEvent)
	 */
	public void labelProviderChanged(LabelProviderChangedEvent event) {
		
		if(event.getElements() == null){
			super.labelProviderChanged(event);
			return;
		}

		IStructuredSelection structuredSelection = getSelectedElements();
		if (structuredSelection == null) {
			return;
		}

		List selection = new ArrayList();
		for (Iterator e = structuredSelection.iterator(); e.hasNext();) {
			Object next = e.next();
			if (next instanceof IAdaptable) {
				Object object = ((IAdaptable) next).getAdapter(EObject.class);
				if (object != null)
					selection.add(object);
			} else if (next instanceof EObject) {
				selection.add(next);
			}
		}


		if (selection.isEmpty()) // no point in expensive calculations if there
								 // are no elements
			return;
		
		List elementsAffected = new ArrayList();
		for (int i = 0; i < event.getElements().length; i++) {
			Object next = event.getElements()[i];
			if (next instanceof IAdaptable) {
				Object object = ((IAdaptable) next).getAdapter(EObject.class);
				if (object != null)
					elementsAffected.add(object);
			} else if (next instanceof EObject) {
				elementsAffected.add(next);
			}
		}

		selection.retainAll(elementsAffected);
		if (!selection.isEmpty())
			super.labelProviderChanged(event);

	}
	
	/**
	 * Get the property sheet page contributor.
	 * 
	 * @return the property sheet page contributor.
	 */
	public ITabbedPropertySheetPageContributor getContributor() {
		return contributor;
	}
	/**
	 * @return Returns the selectedElements.
	 */
	protected IStructuredSelection getSelectedElements() {
		return selectedElements;
	}

}