/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IActionFilter;

import org.eclipse.gmf.runtime.common.ui.services.action.filter.ActionFilterService;
import org.eclipse.gmf.runtime.common.ui.services.icon.IconOptions;
import org.eclipse.gmf.runtime.common.ui.services.icon.IconService;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserOptions;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserService;
import org.eclipse.gmf.runtime.diagram.core.internal.view.IView;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationEvent;
import org.eclipse.gmf.runtime.diagram.core.listener.PresentationListener;
import org.eclipse.gmf.runtime.diagram.core.listener.PropertyChangeNotifier;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ComponentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.IncarnationUtil;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.core.util.ProxyUtil;
import com.ibm.xtools.notation.View;

/**
 * @author melaasar
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class TreeEditPart
	extends AbstractTreeEditPart
	implements PropertyChangeListener {

	/** the element parser */
	private IParser parser;

	/** the element parser */
	private IAdaptable referenceAdapter;

	/** the container view's notifier */
	private PropertyChangeNotifier viewNotifier = null;

	/** the container view's notifier */
	private PropertyChangeNotifier semanticNotifier = null;
	
	private IView view = null;

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

		viewNotifier = ViewUtil.getPropertyChangeNotifier((View)getModel());
		if (viewNotifier != null)
			viewNotifier.addPropertyChangeListener(this);

		semanticNotifier = PresentationListener.getNotifier(getSemanticElement());
		if (semanticNotifier != null)
			semanticNotifier.addPropertyChangeListener(this);
	}

	/**
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	public void deactivate() {
		if (semanticNotifier != null) {
			semanticNotifier.removePropertyChangeListener(this);
			semanticNotifier = null;
		}

		if (viewNotifier != null)
			viewNotifier.removePropertyChangeListener(this);

		super.deactivate();
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicy());
	}

	/** gets the model as an <code>IView</code>
	 * @return IView
	 */
	protected IView getView() {
		if (view==null)
			view = IncarnationUtil.incarnateView((View)getModel()); 
		return view;
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
			return getParser().getPrintString(
				referenceAdapter,
				ParserOptions.NONE.intValue());
		String name = ProxyUtil.getProxyName(((View)getModel()).getElement());
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
	public final void propertyChange(PropertyChangeEvent event) {
		// Receiving an event while a view is deleted could only happen during "undo" of view creation,
		// However, event handlers should be robust by using the event's value and not trying to read 
		// the value from the model
		if ((((View)getModel()).eResource() != null))
			handlePropertyChangeEvent(event);
	}

	/**
	 * Handles the property changed event
	 * @param event the property changed event
	 */
	protected void handlePropertyChangeEvent(PropertyChangeEvent event) {
		if (event.getPropertyName().equals(Properties.ID_SEMANTICREF)) {
			reactivateSemanticElement();
		} 
		else if (event instanceof NotificationEvent) {
			handleNotificationEvent((NotificationEvent) event);
		}
	}
	
	/**
	 * Handles the supplied notification event. 
	 * @param event
	 */
	protected void handleNotificationEvent( NotificationEvent event ) { 
		refreshVisuals();
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
}
