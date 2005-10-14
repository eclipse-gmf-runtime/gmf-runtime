/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RelativeBendpoint;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.gmf.runtime.common.ui.services.action.filter.ActionFilterService;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationListener;
import org.eclipse.gmf.runtime.diagram.core.listener.PresentationListener;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ConnectorBendpointEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ConnectorEndpointEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ConnectorLabelsEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DecorationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.PropertyHandlerEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.SemanticEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.DefaultEditableEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.IContainedEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.IEditableEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.ConnectorEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.ConnectorLineSegEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.TreeConnectorBendpointEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.editpolicy.EditPolicyService;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.EditPartService;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.ConnectionLayerEx;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.ForestRouter;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.OrthogonalRouter;
import org.eclipse.gmf.runtime.emf.core.EventTypes;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.core.util.MetaModelUtil;
import org.eclipse.gmf.runtime.emf.core.util.ProxyUtil;
import org.eclipse.gmf.runtime.gef.ui.internal.editpolicies.GraphicalEditPolicyEx;
import org.eclipse.gmf.runtime.gef.ui.internal.l10n.Cursors;
import org.eclipse.gmf.runtime.gef.ui.internal.tools.SelectConnectorEditPartTracker;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.FontStyle;
import org.eclipse.gmf.runtime.notation.JumpLinkStatus;
import org.eclipse.gmf.runtime.notation.JumpLinkType;
import org.eclipse.gmf.runtime.notation.LineStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.RelativeBendpoints;
import org.eclipse.gmf.runtime.notation.Routing;
import org.eclipse.gmf.runtime.notation.RoutingStyle;
import org.eclipse.gmf.runtime.notation.Smoothness;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionFilter;
/**
 * the base edit part that controls <code>Edge</code> views, it is the basic 
 * controller for the connector's view
 * @author mmostafa
 */
abstract public class ConnectionEditPart
	extends AbstractConnectionEditPart
	implements IGraphicalEditPart, PropertyChangeListener, IContainedEditPart, IPrimaryEditPart, NotificationListener {

	/** A map of listener filters ids to filter data */
	private Map listenerFilters;
	
	/** Used for registering and unregistering the edit part */
	private String elementGuid;

	/** Used for accessibility. */
	protected AccessibleEditPart accessibleEP;
	
	/**
	 * gets a property change command for the passed property, using both of the 
	 * old and new values 
	 * @param property 	the property associated with the command
	 * @param oldValue  the old value associated with the command
	 * @param newValue  the new value associated with the command
	 * @return	a command
	 */
	protected Command getPropertyChangeCommand(
		Object property,
		Object oldValue,
		Object newValue) {
		// by default return null, which means there is no special command to change the property
		return null;
	}

	/** Used for handling the editable status of the edit part */
	private final IEditableEditPart editableEditPart;
	
	/**
	 * Register the adapters for the standard properties.
	 */
	static {
		registerAdapters();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void activate() {
			addNotationalListeners();
		
		EObject semanticProxy = ((View)getModel()).getElement();
		EObject semanticElement = ProxyUtil.resolve(MEditingDomainGetter.getMEditingDomain((View)getModel()), semanticProxy);
		
		if (semanticElement != null)
				addSemanticListeners();
		else if (semanticProxy != null) {
			addListenerFilter("SemanticProxy", this,semanticProxy); //$NON-NLS-1$
		}
		super.activate();
		}


	
	/**
	 * Adds a listener filter by adding the given listener to a passed notifier
	 * 
	 * @param filterId A unique filter id (within the same editpart instance)
	 * @param listener A listener instance
	 * @param notifier An element notifer to add the listener to
	 */
	protected void addListenerFilter(
		String filterId,
		NotificationListener listener,
		EObject element) {

		if (element == null)
			return;

		Assert.isNotNull(filterId);
		Assert.isNotNull(listener);

		if (listenerFilters == null)
			listenerFilters = new HashMap();

		PresentationListener.getInstance().addNotificationListener(element,listener);
		listenerFilters.put(filterId, new Object[] { element, listener });
	}
	
	/**
	 * Adds a listener filter by adding the given listener to a passed notifier
	 * 
	 * @param filterId A unique filter id (within the same editpart instance)
	 * @param listener A listener instance
	 * @param notifier An element notifer to add the listener to
	 */
	protected void addListenerFilter(
		String filterId,
		NotificationListener listener,
		EObject element,
		EStructuralFeature feature) {

		if (element == null)
			return;

		Assert.isNotNull(filterId);
		Assert.isNotNull(listener);

		if (listenerFilters == null)
			listenerFilters = new HashMap();

		PresentationListener.getInstance().addNotificationListener(element,feature,listener);
		listenerFilters.put(filterId, new Object[] { element,feature, listener });
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createChild(java.lang.Object)
	 */
	final protected EditPart createChild(Object model) {
		return EditPartService.getInstance().createGraphicEditPart(
			(View) model);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createConnection(java.lang.Object)
	 */
	final protected org.eclipse.gef.ConnectionEditPart createConnection(
		Object connectorView) {
		return (org.eclipse.gef.ConnectionEditPart) createChild(connectorView);
	}

	/**
	 * Overridden to support editpolicies installed programmatically and
	 * via the <code>EditPolicyService</code>.  Subclasses should override
	 * <code>createDefaultEditPolicies()</code>.
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	final protected void createEditPolicies() {
		createDefaultEditPolicies();
		EditPolicyService.getInstance().createEditPolicies(this);
	}

	/**
	 * Should be overridden to install editpolicies programmatically.
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createDefaultEditPolicies() {
		installEditPolicy(
			EditPolicyRoles.SEMANTIC_ROLE,
			new SemanticEditPolicy());
		installEditPolicy(
			EditPolicyRoles.PROPERTY_HANDLER_ROLE,
			new PropertyHandlerEditPolicy());
		installEditPolicy(
			EditPolicy.CONNECTION_ENDPOINTS_ROLE,
			new ConnectorEndpointEditPolicy());
		installEditPolicy(
			EditPolicy.CONNECTION_ROLE,
			new ConnectorEditPolicy());
		installBendpointEditPolicy();
		installEditPolicy(
			EditPolicyRoles.DECORATION_ROLE,
			new DecorationEditPolicy());
		installEditPolicy(
			EditPolicyRoles.CONNECTOR_LABELS,
			new ConnectorLabelsEditPolicy());
		
		installEditPolicy(EditPolicyRoles.SNAP_FEEDBACK_ROLE,
			new SnapFeedbackPolicy());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	public void deactivate() {
		boolean wasActive = isActive();
		super.deactivate();
		if (listenerFilters != null && wasActive != isActive()) {
			for (Iterator i = listenerFilters.keySet().iterator(); i.hasNext();) {
				Object[] obj = (Object[]) listenerFilters.get(i.next());
				if (obj.length>2){
					PresentationListener.getInstance().
						removeNotificationListener((EObject)obj[0],(EStructuralFeature) obj[1],(NotificationListener) obj[2]);
				} else {
					PresentationListener.getInstance().removeNotificationListener((EObject)obj[0],(NotificationListener) obj[1]);
				}
			}
		}
	}

	/**
	 * executes the passed command 
	 * @param command the command to execute
	 */
	protected void executeCommand(Command command) {
		getEditDomain().getCommandStack().execute(command);
	}

	/**
	* a function that registers this provider with the Eclipse AdapterManager
	* as an IView and an IActionFilter adapter factory for the 
	* IGraphicalEditPart nodes
	*     
	*/
	static private void registerAdapters() {
		Platform.getAdapterManager().registerAdapters(new IAdapterFactory() {

			/**
			 * @see org.eclipse.core.runtime.IAdapterFactory
			 */
			public Object getAdapter(
				Object adaptableObject,
				Class adapterType) {

				IGraphicalEditPart gep = (IGraphicalEditPart) adaptableObject;

				if (adapterType == IActionFilter.class) {
					return ActionFilterService.getInstance();
				} else if (adapterType == View.class) {
					return gep.getModel();
				} 
				return null;
			}

			/**
			 * @see org.eclipse.core.runtime.IAdapterFactory
			 */
			public Class[] getAdapterList() {
				return new Class[] { IActionFilter.class, View.class };
			}

		}, IGraphicalEditPart.class);
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getAccessibleEditPart()
	 */
	protected AccessibleEditPart getAccessibleEditPart() {

		if( accessibleEP == null ) {
			accessibleEP = new AccessibleGraphicalEditPart() {

				private String getSemanticName() {
					EObject semanticElement = resolveSemanticElement();
					
					if( semanticElement != null ) {
						String name = semanticElement.getClass().getName();
						int startIndex = name.lastIndexOf('.') + 1;
						int endIndex = name.lastIndexOf( "Impl" ); //$NON-NLS-1$
						return name.substring(startIndex, endIndex);
					}
					
					return PresentationResourceManager.getInstance().getString( "Accessible.Connection.Label" ); //$NON-NLS-1$
				}

				public void getName(AccessibleEvent e) {
					
					StringBuffer msg = new StringBuffer();
					
					EditPart sourceEP = getSource();
					EditPart targetEP = getTarget();

					// Get the Connection Name
					msg.append( getSemanticName() );

					// Get the Source Name
					if (sourceEP != null) {
						AccessibleEditPart aEP = (AccessibleEditPart)sourceEP.getAdapter(AccessibleEditPart.class);
						AccessibleEvent event = new AccessibleEvent(this);
						aEP.getName( event );
						msg.append( " " ); //$NON-NLS-1$
						msg.append( PresentationResourceManager.getInstance().getString( "Accessible.Connection.From" ) ); //$NON-NLS-1$
						msg.append( " " ); //$NON-NLS-1$
						msg.append( event.result );
					}

					// Get the Target Name
					if (targetEP != null) {
						AccessibleEditPart aEP = (AccessibleEditPart)targetEP.getAdapter(AccessibleEditPart.class);
						AccessibleEvent event = new AccessibleEvent(this);
						aEP.getName( event );
						msg.append( " " ); //$NON-NLS-1$
						msg.append( PresentationResourceManager.getInstance().getString( "Accessible.Connection.To" ) ); //$NON-NLS-1$
						msg.append( " " ); //$NON-NLS-1$
						msg.append( event.result );
					}
					
					e.result = msg.toString();
				}
			};
		}
		return accessibleEP;
	}


	/** 
	 * Adds the ability to adapt to this editpart's view class.
	 */
	public Object getAdapter(Class key) {
		Object adapter = Platform.getAdapterManager().getAdapter(this, key);
		if (adapter != null) {
			return adapter;
		}
		
		if (adapter == SnapToHelper.class) {

			List snapStrategies = new ArrayList();

			Boolean val = (Boolean)getViewer().getProperty(RulerProvider.PROPERTY_RULER_VISIBILITY);
			if (val != null && val.booleanValue())
				snapStrategies.add(new SnapToGuides(this));

			val = (Boolean)getViewer().getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED);
			if (val != null && val.booleanValue())
				snapStrategies.add(new SnapToGrid(this));

			if (snapStrategies.size() == 0)
				return null;

			if (snapStrategies.size() == 1)
				return (SnapToHelper)snapStrategies.get(0);

			SnapToHelper ss[] = new SnapToHelper[snapStrategies.size()];
			for (int i = 0; i < snapStrategies.size(); i++)
				ss[i] = (SnapToHelper)snapStrategies.get(i);
			return new CompoundSnapToHelper(ss);
		}
		
		Object model = getModel();
		
		if ( View.class.isAssignableFrom(key) && model instanceof View) {
			return getModel();
		}
		

		if (model != null &&
			model instanceof View) {
			// Adapt to semantic element
			EObject semanticObject = ViewUtil.resolveSemanticElement((View)model);
			if (key.isInstance(semanticObject)) {
				return semanticObject;
			} else if (key.isInstance(model)){
				return model;
			}
		}
		return super.getAdapter(key);
	}

	/**
	 * Method getChildBySemanticHint.
	 * @param semanticHint
	 * @return IGraphicalEditPart
	 */
	public IGraphicalEditPart getChildBySemanticHint(String semanticHint) {
		if (getModel()!=null){
			View view = ViewUtil.getChildBySemanticHint((View)getModel(),semanticHint);
			if (view != null)
				return  (IGraphicalEditPart)getViewer().getEditPartRegistry().get(view);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.EditPart#getCommand(org.eclipse.gef.Request)
	 */
	public Command getCommand(Request _request) {
		if ( !isEditModeEnabled() ) {
			return UnexecutableCommand.INSTANCE;
		}

		final Request request = _request;
		Command cmd = (Command)MEditingDomainGetter.getMEditingDomain((View)getModel()).runAsRead( new MRunnable() {
			public Object run() { 
				return ConnectionEditPart.super.getCommand(request);
			}
		});
		return cmd;
	}

	/**
	 * Convenience method returning the editpart's Diagram, the Diagam that owns the edit part 
	 * @return the diagram
	 */
	protected Diagram getDiagramView() {
		return (Diagram)getRoot().getContents().getModel();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart#getPrimaryView()
	 */
	public View getPrimaryView() {
		for (EditPart parent = this; parent != null; parent = parent.getParent())
			if (parent instanceof IPrimaryEditPart)
				return (View) parent.getModel();
		return null;
	}
	
	/**
	 * Convenience method returning the editpart's edit domain.
	 * Same as calling <code>getRoot().getViewer().getEditDomain()</code>
	 * @return the edit domain
	 */
	protected EditDomain getEditDomain() {
		return getRoot().getViewer().getEditDomain();
	}


	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart#getDiagramEditDomain()
	 */
	public IDiagramEditDomain getDiagramEditDomain() {
		return (IDiagramEditDomain) getEditDomain();
	}

	/**
	 * Return this editpart's view (model) children.
	 * @return list of views.
	 */
	protected List getModelChildren() {
		return ((View)getModel()).getChildren();
	}
	
	
	/**
	 * Convenience method to retreive the value for the supplied poperty
	 * from the editpart's associated view element.
	 * @param id the property id
	 * @return Object the value
	 * @deprecated use {@link #getStructuralFeatureValue(EStructuralFeature)} instead
	 */
	public Object getPropertyValue(Object id) {
		return ViewUtil.getPropertyValue((View) getModel(), id);
	}
	
	/**
	 * Convenience method to retreive the value for the supplied value from the
	 * editpart's associated view element. Same as calling
	 * <code> ViewUtil.getStructuralFeatureValue(getNotationView(),feature)</code>.
	 */
	public Object getStructuralFeatureValue(EStructuralFeature feature) {
		return ViewUtil.getStructuralFeatureValue((View) getModel(),feature);
	}


	/**
	 * try to resolve the semantic element and Return the resolven element; if the 
	 * element is unresolvable or null it will return null
	 * @return non proxy EObject or NULL 
	 */
	public EObject resolveSemanticElement() {
		return (EObject) MEditingDomainGetter.getMEditingDomain((View)getModel()).runAsRead( new MRunnable() {
			public Object run() {
				return ViewUtil.resolveSemanticElement((View) getModel());
			}
		});
	}
	
	/** 
	 * Walks up the editpart hierarchy to find and return the
	 * <code>TopGraphicEditPart</code> instance.
	 */
	public TopGraphicEditPart getTopGraphicEditPart() {
		EditPart editPart = this;
		while (editPart instanceof IGraphicalEditPart) {
			if (editPart instanceof TopGraphicEditPart)
				return (TopGraphicEditPart) editPart;
			editPart = editPart.getParent();
		}
		return null;
	}

	/**
	 * Return the editpart's associated Notation View.
	 * @return <code>View</code>, the associated view or null if there is no associated Notation View
	 */
	public View getNotationView() {
		Object model = getModel();
		if (model instanceof View)
			return (View)model;
		return null;
	}

	/** Handles the passed property changed event only if the editpart's view is not deleted */
	public final void propertyChange(PropertyChangeEvent event) {
		if(isActive())
			handlePropertyChangeEvent(event);
	}

	/**
	 * Handles the property changed event
	 * @param event the property changed event
	 */
	protected void handlePropertyChangeEvent(PropertyChangeEvent event) {
		if (event.getPropertyName().equals(Connection.PROPERTY_CONNECTION_ROUTER)) {
			installRouter();
		}
	}

	/**
	 * Method reactivateSemanticModel.
	 * This method reactivates the edit part's emantic model by:
	 * 1- removing semantic listeners
	 * 2- adding semantic listeners if the semantic reference is resolvable
	 * 3- Refreshing it
	 * 
	 * This method is called in response to IView's Properties.ID_SEMANTICREF property change event
	 * However, it will only work under the following assumptions:
	 * 1- The old and new semantic models are compatible in their kind
	 * 2- The deltas between old and new semantic models do not affect notation
	 * 3- Connectors are not refereshed since they are maintained by the diagram
	 */
	public void reactivateSemanticModel() {
		removeSemanticListeners();
		if (resolveSemanticElement() != null)
			addSemanticListeners();
		refresh();
	}
	
	/** Finds an editpart given a starting editpart and an EObject */
	public EditPart findEditPart(EditPart epBegin, EObject theElement) {
		if(theElement == null) {
			return null;
		}
		EditPart epStart = null;
		if(epBegin == null) {
			epStart = this;
		}
		else {
			epStart=epBegin;
		}
		
		final View view = (View) ((IAdaptable) epStart).getAdapter(View.class);
		
		if (view != null) {
			EObject el = ViewUtil.resolveSemanticElement(view);

			if ((el != null)&& el.equals(theElement)) {
				return epStart;
			}
		}

		ListIterator childLI = epStart.getChildren().listIterator();
		while (childLI.hasNext()) {
			EditPart epChild = (EditPart) childLI.next();

			EditPart elementEP = findEditPart(epChild, theElement);
			if (elementEP != null) {
				return elementEP;
			}
		}
		return null;
	}

	/**
	 * Refresh the editpart's figure foreground colour.
	 */
	protected void refreshForegroundColor() {
		LineStyle style = (LineStyle)  getPrimaryView().getStyle(NotationPackage.eINSTANCE.getLineStyle());
		if (style != null)
			setForegroundColor(PresentationResourceManager.getInstance().getColor(new Integer(style.getLineColor())));
	}

	/**
	 * Refresh the editpart's figure visibility.
	 */
	protected void refreshVisibility() {
		setVisibility(((View)getModel()).isVisible());
	}

	/**
	 * Removes a listener previously added with the given id
	 * @param filterId	the filiter ID
	 */
	protected void removeListenerFilter(String filterId) {
		if (listenerFilters == null)
			return;

		Object[] objects = (Object[]) listenerFilters.get(filterId);
		if (objects == null){
			return;
		}
			

		if (objects.length>2){
			PresentationListener.getInstance().
				removeNotificationListener((EObject) objects[0],
											 (EStructuralFeature) objects[1],
											 (NotificationListener) objects[2]);
		} else {
			PresentationListener.getInstance().
				removeNotificationListener((EObject) objects[0],
					 				 (NotificationListener) objects[1]);
		}
		listenerFilters.remove(filterId);
	}

	/**
	 * sets the forefround color of the editpart's figure
	 * @param color	the color
	 */
	protected void setForegroundColor(Color color) {
		getFigure().setForegroundColor(color);
	}

	/**
	 * Convenience method to set a property value.
	 * @param id
	 * @param value
	 * @deprecated use {@link #setStructuralFeatureValue(Object, Object)} instead
	 */
	public void setPropertyValue(Object id, Object value) {
		ViewUtil.setPropertyValue((View) getModel(), id, value);
	}
	
	/**
	 * Sets the passed feature if possible on this editpart's view
	 * to the passed value.
	 * @param feature the feature to use
	 * @param value  the value of the property being set
	 */
	public void setStructuralFeatureValue(EStructuralFeature feature, Object value) {
		ViewUtil.setStructuralFeatureValue((View) getModel(), feature, value);
	}

	/**
	 * sets the edit part's visibility
	 * @param vis	the new visibilty value
	 */
	protected void setVisibility(boolean vis) {
		if (!vis && getSelected() != SELECTED_NONE)
			getViewer().deselect(this);
		getFigure().setVisible(vis);
		getFigure().revalidate();
	}

	/**
	 * This method adds all listeners to the notational world (views, figures, editpart...etc)
	 * Override this method to add more notational listeners down the hierarchy
	 */
	protected void addNotationalListeners() {
		addListenerFilter("View", this, (View)getModel());//$NON-NLS-1$
		getFigure().addPropertyChangeListener(
			Connection.PROPERTY_CONNECTION_ROUTER,
			this);
	}

	/**
	 * This method adds all listeners to the semantic world (IUMLElement...etc)
	 * Override this method to add more semantic listeners down the hierarchy
	 * This method is called only if the semantic element is resolvable
	 */
	protected void addSemanticListeners() {
		addListenerFilter(
			"SemanticModel",//$NON-NLS-1$
			this,
			resolveSemanticElement());
	}

	/**
	 * This method removes all listeners to the notational world (views, figures, editpart...etc)
	 * Override this method to remove notational listeners down the hierarchy
	 */
	protected void removeNotationalListeners() {
		getFigure().removePropertyChangeListener(
			Connection.PROPERTY_CONNECTION_ROUTER,
			this);
		removeListenerFilter("View");//$NON-NLS-1$
	}

	/**
	 * This method removes all listeners to the semantic world (IUMLElement...etc)
	 * Override this method to remove semantic listeners down the hierarchy
	 */
	protected void removeSemanticListeners() {
		removeListenerFilter("SemanticModel");//$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.gef.EditPart#addNotify()
	 */
	public void addNotify() {
		super.addNotify();
		installRouter();
	}

	/**
	 * Return a Map of all the appearance property ids supported by the edit part and its
	 * children.
	 * 
	 * Each entry in the map is the factory hint of the edit part as key and a 
	 * dictionary of appearance properties  as
	 * values. The edit parts are the receiver itself and it's children.
	 * 
	 * For example, the connectable shape edit part with name, attribute, operation and
	 * shape compartments will return a map where:
	 * 1 entry: 
	 * 		connectable shape factory hint ->  dictionary:
	 * 										   Properties.ID_FONT  -> font data
	 * 										   Properties.ID_FONTCOLOR -> font color
	 * 										   Properties.ID_LINECOLOR -> line color
	 * 										   Properties.ID_FILLCOLOR -> fill color
	 * 2d entry: attribute compartment hint -> dictionary(empty)
	 * 3d entry: operation compartment hint -> dictionary(empty)
	 * 4d entry: shape compartment hint -> dictionary(empty)
	 * 
	 * @return Map
	 */
	public Map getAppearancePropertiesMap() {
		Map properties = new HashMap();
		fillAppearancePropertiesMap(properties);
		return properties;
	}

	/**
	 * a static array of appearance property ids  applicable to the connectors
	 */
	protected static final String[] appearanceProperties =
		new String[] {
			Properties.ID_FONTNAME,
			Properties.ID_FONTSIZE,
			Properties.ID_FONTBOLD,
			Properties.ID_FONTITALIC,
			Properties.ID_FONTCOLOR,
			Properties.ID_LINECOLOR };

	/**
	 * construcotr
	 * @param view , the view the edit part will own
	 */
	public ConnectionEditPart(View view) {
		setModel(view);
		this.editableEditPart = new DefaultEditableEditPart(this);
	}

	/**
	 * Method createConnectionFigure.
	 * @return a <code>Connection</code> figure
	 */
	abstract protected Connection createConnectionFigure();

	final protected IFigure createFigure() {
		return createConnectionFigure();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.EditPart#refresh()
	 */
	public void refresh() {
		if (getSource() != null && getTarget() != null) 
			MEditingDomainGetter.getMEditingDomain((View)getModel()).runAsRead(new MRunnable() {
					public Object run() {
					ConnectionEditPart.super.refresh();
					EditPolicyIterator i = getEditPolicyIterator();
					while (i.hasNext()) {
						EditPolicy policy = i.next();
						if (policy instanceof GraphicalEditPolicyEx) {
							((GraphicalEditPolicyEx) policy).refresh();
						}
					}
						return null; 
				}
			});
	}

	/**
	 * Method getConnectorView.
	 * 
	 * @return IConnectorView
	 */
	/**
	 * utility method to get the <code>Edge</code> view
	 * @return the <code>Edge</code>
	 */
	protected Edge getConnectorView() {
		return (Edge) getModel();
	}

	/*
	 * @see AbstractEditPart#getDragTracker(Request)
	 */
	public DragTracker getDragTracker(Request req) {
		return new SelectConnectorEditPartTracker(this);
	}

	/**
	 * give access to the source of the edit part's Edge
	 * @return the source
	 */
	protected Object getModelSource() {
		return getConnectorView().getSource();
	}

	/**
	 * give access to the target of the edit part's Edge
	 * @return the target
	 */
	protected Object getModelTarget() {
		return getConnectorView().getTarget();
	}

	/**
	 * installes a router on the edit part, depending on the <code>RoutingStyle</code>
	 */
	protected void installRouter() {
		ConnectionLayerEx cLayer =
			(ConnectionLayerEx) getLayer(LayerConstants.CONNECTION_LAYER);

		RoutingStyle style = (RoutingStyle) ((View) getModel()).getStyle(NotationPackage.eINSTANCE.getRoutingStyle());
		if (style != null) {
			
			Routing routing = style.getRouting();			
			if (Routing.MANUAL_LITERAL == routing) {
				getConnectionFigure().setConnectionRouter(
					cLayer.getObliqueRouter());
			} else if (Routing.RECTILINEAR_LITERAL == routing) {
				getConnectionFigure().setConnectionRouter(
					cLayer.getRectilinearRouter());
			} else if (Routing.TREE_LITERAL == routing) {
				getConnectionFigure().setConnectionRouter(
					cLayer.getTreeRouter()); 
			}
			
		}

		refreshRouterChange();
	}

	/**
	 * refresh the pendpoints owned by the EditPart's <code>Edge</code>
	 */
	protected void refreshBendpoints() {
		RelativeBendpoints bendpoints = (RelativeBendpoints) getConnectorView().getBendpoints();
		List modelConstraint = bendpoints.getPoints(); 
		List figureConstraint = new ArrayList();
		for (int i = 0; i < modelConstraint.size(); i++) {
			org.eclipse.gmf.runtime.notation.datatype.RelativeBendpoint wbp =
				(org.eclipse.gmf.runtime.notation.datatype.RelativeBendpoint) modelConstraint.get(i);
			RelativeBendpoint rbp =
				new RelativeBendpoint(getConnectionFigure());
			rbp.setRelativeDimensions(
				new Dimension(wbp.getSourceX(), wbp.getSourceY()),
				new Dimension(wbp.getTargetX(), wbp.getTargetY()));
			rbp.setWeight((i + 1) / ((float) modelConstraint.size() + 1));
			figureConstraint.add(rbp);
		}
		getConnectionFigure().setRoutingConstraint(figureConstraint);
	}

	private void installBendpointEditPolicy() {
		if (getConnectionFigure().getConnectionRouter()
				instanceof ForestRouter) {
				installEditPolicy(
					EditPolicy.CONNECTION_BENDPOINTS_ROLE,
					new TreeConnectorBendpointEditPolicy());
				getConnectionFigure().setCursor(Cursors.CURSOR_SEG_MOVE);
			}
		else if (getConnectionFigure().getConnectionRouter()
			instanceof OrthogonalRouter) {
			installEditPolicy(
				EditPolicy.CONNECTION_BENDPOINTS_ROLE,
				new ConnectorLineSegEditPolicy());
			getConnectionFigure().setCursor(Cursors.CURSOR_SEG_MOVE);
		}
		else {
			installEditPolicy(
				EditPolicy.CONNECTION_BENDPOINTS_ROLE,
				new ConnectorBendpointEditPolicy());
			getConnectionFigure().setCursor(Cursors.CURSOR_SEG_ADD);
		}
	}

	/**
	 * Method refreshRouterChange.
	 */
	protected void refreshRouterChange() {
		refreshBendpoints();
		installBendpointEditPolicy();
	}

	/**
	 * Method refreshSmoothness.
	 */
	protected void refreshSmoothness() {
		PolylineConnectionEx poly =
			((PolylineConnectionEx) getConnectionFigure());
		RoutingStyle style = (RoutingStyle)((View) getModel()).getStyle(NotationPackage.eINSTANCE.getRoutingStyle());
		if (style != null) {
			Smoothness smoothness = style.getSmoothness();
	
			if (Smoothness.LESS_LITERAL == smoothness) {
				poly.setSmoothness(PolylineConnectionEx.SMOOTH_LESS);
			} else if (Smoothness.NORMAL_LITERAL == smoothness) {
				poly.setSmoothness(PolylineConnectionEx.SMOOTH_NORMAL);
			} else if (Smoothness.MORE_LITERAL == smoothness) {
				poly.setSmoothness(PolylineConnectionEx.SMOOTH_MORE);
			} else if (Smoothness.NONE_LITERAL == smoothness) {
				poly.setSmoothness(PolylineConnectionEx.SMOOTH_NONE);
			}
		}
	}

	/**
	 * Method refreshJumplinks.
	 */
	protected void refreshJumplinks() {
		PolylineConnectionEx poly =
			((PolylineConnectionEx) getConnectionFigure());
		RoutingStyle style = (RoutingStyle) ((View) getModel()).getStyle(NotationPackage.eINSTANCE.getRoutingStyle());
		
		JumpLinkStatus status = JumpLinkStatus.NONE_LITERAL;
		JumpLinkType type = JumpLinkType.SEMICIRCLE_LITERAL;	
		boolean reverse = false;
		if (style != null) {
			status = style.getJumpLinkStatus();
			type = style.getJumpLinkType();
			reverse = style.isJumpLinksReverse();
		}

		int jumpType = 0;
		if (JumpLinkStatus.BELOW_LITERAL == status) {
			jumpType = PolylineConnectionEx.JUMPLINK_FLAG_BELOW;
		} else if (JumpLinkStatus.ABOVE_LITERAL == status) {
			jumpType = PolylineConnectionEx.JUMPLINK_FLAG_ABOVE;
		} else if (JumpLinkStatus.ALL_LITERAL == status) {
			jumpType = PolylineConnectionEx.JUMPLINK_FLAG_ALL;
		}

		boolean bCurved = type.equals(JumpLinkType.SEMICIRCLE_LITERAL);
		boolean bAngleIn = !type.equals(JumpLinkType.SQUARE_LITERAL);
		boolean bOnBottom = reverse;

		poly.setJumpLinks(jumpType != 0);
		poly.setJumpLinksStyles(jumpType, bCurved, bAngleIn, bOnBottom);
	}

	/**
	 * Method refreshRoutingStyles.
	 */
	protected void refreshRoutingStyles() {
		PolylineConnectionEx poly =
			((PolylineConnectionEx) getConnectionFigure());

		RoutingStyle style = (RoutingStyle) ((View) getModel()).getStyle(NotationPackage.eINSTANCE.getRoutingStyle());
		if (style != null) {
	
			boolean closestDistance = style.isClosestDistance();
			boolean avoidObstruction = style.isAvoidObstructions();
	
			poly.setRoutingStyles(closestDistance, avoidObstruction);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		super.refreshVisuals();
		refreshVisibility();
		refreshForegroundColor();
		refreshRoutingStyles();
		refreshSmoothness();
		refreshJumplinks();
		refreshBendpoints();
		refreshFont();
	}

	/**
	 * Refresh the editpart's figure font.
	 */
	protected void refreshFont() {
		FontStyle style = (FontStyle) getPrimaryView().getStyle(NotationPackage.eINSTANCE.getFontStyle());
		if (style != null) {
			setFont(new FontData(
				style.getFontName(), 
				style.getFontHeight(), 
				(style.isBold() ? SWT.BOLD : SWT.NORMAL) | 
				(style.isItalic() ? SWT.ITALIC : SWT.NORMAL)));
		}
	}

	/**
	 * Sets the font to the label.
	 * This method could be overriden to change the font data of the font
	 * overrides typically look like this:
	 * 		super.setFont(
	 *		new FontData(
	 *			fontData.getName(),
	 *			fontData.getHeight(),
	 *			fontData.getStyle() <| &> SWT.????));
	 * @param fontData the font data
	 */
	protected void setFont(FontData fontData) {
		getFigure().setFont(
			PresentationResourceManager.getInstance().getFont(
				Display.getDefault(),
				fontData));
		getFigure().repaint();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart#fillAppearancePropertiesMap(java.util.Map)
	 */
	public void fillAppearancePropertiesMap(Map properties) {

		if (getAppearancePropertyIDs().length > 0) {
			// only if there are any appearance properties
			final Dictionary local_properties = new Hashtable();
			for (int i = 0; i < getAppearancePropertyIDs().length; i++) {
				String prob = getAppearancePropertyIDs()[i];
				ENamedElement element = MetaModelUtil.getElement(prob);
				if (element instanceof EStructuralFeature &&
					ViewUtil.isPropertySupported((View) getModel(),prob)){
					local_properties.put(
						getAppearancePropertyIDs()[i],
						getStructuralFeatureValue((EStructuralFeature)element));
				}
			}
			properties.put(
				((View) getModel()).getType(),
				local_properties);
		}

		Iterator iterator = getChildren().iterator();
		while (iterator.hasNext()) {
			IGraphicalEditPart child = (IGraphicalEditPart) iterator.next();
			child.fillAppearancePropertiesMap(properties);
		}
	}

	/**
	 * Returns an array of the appearance property ids applicable to the receiver.
	 * Fro this type it is  Properties.ID_FONT, Properties.ID_FONTCOLOR, Properties.ID_LINECOLOR 
	 * 
	 * @return - an array of the appearane property ids applicable to the receiver
	 */
	protected String[] getAppearancePropertyIDs() {
		return appearanceProperties;
	}

	/**
	 * Perform a request by executing a command from the target editpart of the request
	 * For the Direct_Edit request, we need to show up an editor first
	 * @see org.eclipse.gef.EditPart#performRequest(org.eclipse.gef.Request)
	 */
	public void performRequest(Request request) {
		if ( !isEditModeEnabled() ) {
			return;
		}

		if (RequestConstants.REQ_DIRECT_EDIT == request.getType()) {
			performDirectEditRequest(request);
		} else {
			EditPart targetEditPart = getTargetEditPart(request);
			if (targetEditPart != null) {
				Command command = targetEditPart.getCommand(request);
				if (command != null) {
					getDiagramEditDomain().getDiagramCommandStack().execute(
						command);
					return;
				}
			}
		}
	}

	/**
	 * Performs a direct edit request (usually by showing some type of editor)
	 * @param request the direct edit request
	 */
	protected void performDirectEditRequest(Request request) {
		EditPart primaryChildEditPart = (EditPart)MEditingDomainGetter.getMEditingDomain((View)getModel()).runAsRead( new MRunnable()  {
			public Object run() {
				return getPrimaryChildEditPart();
			}
		});
		if (primaryChildEditPart != null) {
			primaryChildEditPart.performRequest(request);
		}
	}

	/**
	 * @see org.eclipse.gef.EditPart#understandsRequest(org.eclipse.gef.Request)
	 */
	public boolean understandsRequest(Request req) {
		return RequestConstants.REQ_DIRECT_EDIT == req.getType()
			|| super.understandsRequest(req);
	}


	/** Adds a [ref, editpart] mapping to the EditPartForElement map. */
	protected void registerModel() {
		super.registerModel();

		// Save the elements Guid to use during unregister
		EObject ref = ((View) getModel()).getElement();
		if ( ref == null ) {
			return;
		}
		
		elementGuid = ProxyUtil.getProxyID(ref);

		((IDiagramGraphicalViewer) getViewer()).registerEditPartForElement(
			elementGuid,
			this);
	}

	/** Remove this editpart from the EditPartForElement map. */
	protected void unregisterModel() {
		super.unregisterModel();

		((IDiagramGraphicalViewer) getViewer()).unregisterEditPartForElement(
			elementGuid,
			this);
	}

	/**
	 * Handles the case where the semantic reference has changed.
	 */
	protected final void handleMajorSemanticChange() {
		if (getSource() instanceof GraphicalEditPart && getTarget() instanceof GraphicalEditPart) {
			((GraphicalEditPart) getSource()).refreshSourceConnection(this);
			((GraphicalEditPart) getTarget()).refreshTargetConnection(this);
		}
	}

	/**
	 * Refreshes a child editpart by removing it and refreshing children
	 * @param child
	 */
	final void refreshChild(GraphicalEditPart child) {
		removeChild(child);
		refreshChildren();
	}

	/** 
	 * check if there is a canonical edit policy installed on the edit part
	 * or not
	 * @return <tt>true</tt> if a canonical editpolicy has been installed on
	 * this editpart; otherwise <tt>false</tt>
	 */
	public final boolean isCanonical() {
		return getEditPolicy(EditPolicyRoles.CANONICAL_ROLE) != null;
	}
	
	/** 
	 * checks if the edit part's figure is visible or not
	 * @return <tt>true</tt> if the editpart's figure is visible;
	 * <tt>false</tt> otherwise.
	 */
	public boolean isSelectable() {
		return getFigure().isVisible();
	}

	/* 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart#disableEditMode()
	 */
	public void disableEditMode() {
		this.editableEditPart.disableEditMode();		
	}
	
	/* 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart#enableEditMode()
	 */
	public void enableEditMode() {
		this.editableEditPart.enableEditMode();
	}
	
	/* 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart#isEditModeEnabled()
	 */
	public boolean isEditModeEnabled() {
		return this.editableEditPart.isEditModeEnabled();
	}
	
	/* 
	 * @see org.eclipse.gef.EditPart#showSourceFeedback(org.eclipse.gef.Request)
	 */
	public void showSourceFeedback(Request request) {
		if ( !isEditModeEnabled()) {
			return;
		}
		
		super.showSourceFeedback(request);
	}
	
	/* 
	 * @see org.eclipse.gef.EditPart#showTargetFeedback(org.eclipse.gef.Request)
	 */
	public void showTargetFeedback(Request request) {
		if ( !isEditModeEnabled()) {
			return;
		}
		
		super.showTargetFeedback(request);
	}

	/* 
	 * @see org.eclipse.gef.EditPart#eraseSourceFeedback(org.eclipse.gef.Request)
	 */
	public void eraseSourceFeedback(Request request) {
		if ( !isEditModeEnabled()) {
			return;
		}
		
		super.eraseSourceFeedback(request);
	}
	/* 
	 * @see org.eclipse.gef.EditPart#eraseTargetFeedback(org.eclipse.gef.Request)
	 */
	public void eraseTargetFeedback(Request request) {
		if ( !isEditModeEnabled()) {
			return;
		}

		super.eraseTargetFeedback(request);
	}
	
	/**
	 * this method will return the primary child EditPart inside this edit part
	 * @return the primary child view inside this edit part
	 */
	public EditPart getPrimaryChildEditPart(){
		if (getChildren().size() > 0)
			return (EditPart) getChildren().get(0);
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart#getDiagramPreferencesHint()
	 */
	public PreferencesHint getDiagramPreferencesHint() {
		if (getRoot() instanceof IDiagramPreferenceSupport) {
			return ((IDiagramPreferenceSupport) getRoot()).getPreferencesHint();
		}
		return PreferencesHint.USE_DEFAULTS;
	}
	
		/*
	 * ATTENTION!!!!: Do not remove, see below. Only update based on newer GEF framework
	 *  
	 * This function is "copied" from GEF for the following reason:
	 * GEF does not check if the connector's source or target are the same as the editpart
	 * before setting them to <code>null</code> which causes the following usecase to currently fail:
	 * 
	 * "in a model transaction, view's source connectors are detached, a new view is
	 * created, and the connectors are attached to it, then the old view is destroyed"
	 * 
	 * The reason for the problem is the filtering of Deleted/Uncreated object's events in the
	 * PresentationListener which prevents the first connector detach event from coming and 
	 * avoiding the problem
	 * 
	 * TODO: Remove this override as soon as the bugzilla <Bug 110476> is resolved or the event filtering is removed
	 * 
	 * @see org.eclipse.gef.EditPart#removeNotify()
	 */
	public void removeNotify() {
		deactivateFigure();
		if (getSelected() != SELECTED_NONE)
			getViewer().deselect(this);
		if (hasFocus())
			getViewer().setFocus(null);

		List _children = getChildren();
		for (int i = 0; i < _children.size(); i++)
			((EditPart)_children.get(i))
				.removeNotify();
		unregister();
		List conns;
		conns = getSourceConnections();
		for (int i = 0; i < conns.size(); i++) {
			ConnectionEditPart conn = (ConnectionEditPart)conns.get(i);
			if (conn.getSource() == this)
				conn.setSource(null);
		}
		conns = getTargetConnections();
		for (int i = 0; i < conns.size(); i++) {
			ConnectionEditPart conn = (ConnectionEditPart)conns.get(i);
			if (conn.getTarget() == this)
				conn.setTarget(null);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.core.listener.NotificationListener#notifyChanged(org.eclipse.emf.common.notify.Notification)
	 */
	public void notifyChanged(Notification notification) {
		if (isActive()){
			handleNotificationEvent(notification);
		}
	}

	/**
	 * Handles the property changed event
	 * 
	 * @param event
	 *            the property changed event
	 */
	protected void handleNotificationEvent(Notification event) {
		Object feature = event.getFeature();
		if (NotationPackage.eINSTANCE.getView_PersistedChildren().equals(feature)||
			NotationPackage.eINSTANCE.getView_TransientChildren().equals(feature)) {
				refreshChildren();
		}
		else if (NotationPackage.eINSTANCE.getView_Visible().equals(feature)) {
			setVisibility(((Boolean) event.getNewValue()).booleanValue());
			// Reactivating in response to semantic model reference change
			// However, we need to verify that the event belongs to this editpart's view
			// cannot do it now since property's source is (IView for RMS) and (IUMLView for EMF)
		}
		else if (NotationPackage.eINSTANCE.getRoutingStyle_Routing().equals(feature)) {
			installRouter();
		}
		else if (NotationPackage.eINSTANCE.getRoutingStyle_Smoothness().equals(feature)
			|| NotationPackage.eINSTANCE.getRoutingStyle_AvoidObstructions().equals(feature)
			|| NotationPackage.eINSTANCE.getRoutingStyle_ClosestDistance().equals(feature)
			|| NotationPackage.eINSTANCE.getRoutingStyle_JumpLinkStatus().equals(feature)
			|| NotationPackage.eINSTANCE.getRoutingStyle_JumpLinkType().equals(feature)
			|| NotationPackage.eINSTANCE.getRoutingStyle_JumpLinksReverse().equals(feature)) {
			refreshVisuals();
		}
		else if (NotationPackage.eINSTANCE.getLineStyle_LineColor().equals(feature)) {
			Integer c = (Integer) event.getNewValue();
			setForegroundColor(PresentationResourceManager.getInstance().getColor(c));
		}
		else if (NotationPackage.eINSTANCE.getRelativeBendpoints_Points().equals(feature)) {
			refreshBendpoints();
		}
		else if (event.getFeature() == NotationPackage.eINSTANCE.getView_Element()
		 && ((EObject)event.getNotifier()) == getNotationView())
			handleMajorSemanticChange();

		else if (event.getEventType() == EventTypes.UNRESOLVE 
				&& event.getNotifier() == ((View) getModel()).getElement())
			handleMajorSemanticChange();
	}
}
