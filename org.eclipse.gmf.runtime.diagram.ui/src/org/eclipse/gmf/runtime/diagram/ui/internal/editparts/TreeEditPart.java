/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.gmf.runtime.common.ui.services.action.filter.ActionFilterService;
import org.eclipse.gmf.runtime.common.ui.services.icon.IconOptions;
import org.eclipse.gmf.runtime.common.ui.services.icon.IconService;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserOptions;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserService;
import org.eclipse.gmf.runtime.diagram.core.listener.DiagramEventBroker;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationListener;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ComponentEditPolicy;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.PlatformUI;

/**
 * @author melaasar, mmostafa
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class TreeEditPart
	extends AbstractTreeEditPart
	implements NotificationListener, IEditingDomainProvider {

	/** the element parser */
	private IParser parser;

	/** the element parser */
	private IAdaptable referenceAdapter;
	
    /**
     * Cache the editing domain after it is retrieved.
     */
    private TransactionalEditingDomain editingDomain;

	/**
	 * @param model
	 */
	public TreeEditPart(Object model) {
		super(model);
		
		EObject reference = ((View)model).getElement();
		if (reference == null) {
		
			this.referenceAdapter = new EObjectAdapter((EObject)model);
		} else {
			this.referenceAdapter =
				new EObjectAdapter(reference);
		}
	}

	/**
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void activate() {
		super.activate();

		getDiagramEventBroker().addNotificationListener((View)getModel(),this);
		getDiagramEventBroker().addNotificationListener(getSemanticElement(),this);
	}

	/**
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	public void deactivate() {
		getDiagramEventBroker().removeNotificationListener((View)getModel(),this);
		getDiagramEventBroker().removeNotificationListener(getSemanticElement(),this);
		super.deactivate();
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicy());
	}

	/** gets the model as a <code>View</code>
	 * @return View
	 */
	protected View getNotationView() {
		if (getModel() instanceof View)
			return (View)getModel();
		return null;
	}

	/** Return the editpart's underlying semantic element. */
	protected EObject getSemanticElement() {
		return ViewUtil.resolveSemanticElement((View)getModel());
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractTreeEditPart#getImage()
	 */
	protected Image getImage() {
		if (referenceAdapter == null){
			return null;
		}
		IconOptions options = new IconOptions();
		options.set(IconOptions.GET_STEREOTYPE_IMAGE_FOR_ELEMENT);
		return IconService.getInstance().getIcon(referenceAdapter, options.intValue());
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractTreeEditPart#getText()
	 */
	protected String getText() {
		if (getParser() != null)
			return getParser().getPrintString(referenceAdapter,
				ParserOptions.NONE.intValue());
		EObject eObject = ((View) getModel()).getElement();
		if (eObject == null) {
			return ""; //$NON-NLS-1$
		}
		String name = EMFCoreUtil.getName(eObject);
		return name == null ? "" : name; //$NON-NLS-1$
	}

	/**
	 * Method getParser.
	 * @return IParser
	 */
	private IParser getParser() {
		if (parser == null) {
			if (referenceAdapter != null && referenceAdapter.getAdapter(EObject.class) != null)
				parser = ParserService.getInstance().getParser(referenceAdapter);
		}
		return parser;
	}

	/**
	 * Handles the passed property changed event only if the editpart's view is not deleted
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public final void notifyChanged(Notification event) {
		final Notification eventToHandle = event;
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Runnable#run()
			 */
			public void run() {
				// Receiving an event while a view is deleted could only happen during "undo" of view creation,
				// However, event handlers should be robust by using the event's value and not trying to read 
				// the value from the model
				if ((((View)getModel()).eResource() != null))
					handleNotificationEvent(eventToHandle);
			}
		});
	}

	/**
	 * Handles the supplied notification event. 
	 * @param event
	 */
	protected void handleNotificationEvent( Notification notification ) {
		if (NotationPackage.eINSTANCE.getView_Element()==notification.getFeature()) {
			reactivateSemanticElement();
		} else{
			refreshVisuals();
		}
	}
	
	/**
	 * deactivates, activates then refreshes the editpart
	 */
	protected void reactivateSemanticElement() {
		deactivate();
		activate();
		refresh();
	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(Class)
	 */
	public Object getAdapter(Class key) {
		// Adapt to IActionFilter
		if (key == IActionFilter.class) {
			return ActionFilterService.getInstance();
		}
		
		Object model = getModel();
		// Adapt to IView
		if (key.isInstance(model)) {
			return model;
		}

		if (model != null && model instanceof View) {
			// Adapt to semantic element
			EObject semanticObject = ViewUtil.resolveSemanticElement((View)model);
			if (key.isInstance(semanticObject)) {
				return semanticObject;
			}
		}

		Object adapter = Platform.getAdapterManager().getAdapter(this, key);
		if (adapter != null)
			return adapter;

		return super.getAdapter(key);
	}
	
    /**
     * Derives my editing domain from my diagram element. Subclasses may
     * override.
     */
    public EditingDomain getEditingDomain() {
        if (editingDomain == null) {
            editingDomain = TransactionUtil.getEditingDomain(getModel());
        }
        return editingDomain;
    }
    
    /**
     * Gets the diagram event broker from the editing domain.
     * 
     * @return the diagram event broker
     */
    private DiagramEventBroker getDiagramEventBroker() {
        EditingDomain theEditingDomain = getEditingDomain();
        if (theEditingDomain instanceof TransactionalEditingDomain) {
            return DiagramEventBroker
                .getInstance((TransactionalEditingDomain) theEditingDomain);
        }
        return null;
    }
}
