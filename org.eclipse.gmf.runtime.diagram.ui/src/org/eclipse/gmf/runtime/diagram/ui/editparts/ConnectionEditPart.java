/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RelativeBendpoint;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.services.action.filter.ActionFilterService;
import org.eclipse.gmf.runtime.diagram.core.listener.DiagramEventBroker;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationListener;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ConnectionBendpointEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ConnectionLabelsEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DecorationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.PropertyHandlerEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.SemanticEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.DefaultEditableEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.IContainedEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.IEditableEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.ConnectionEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.ConnectionLineSegEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.TreeConnectionBendpointEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.l10n.DiagramFontRegistry;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.editpolicy.EditPolicyService;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramColorRegistry;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.EditPartService;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.ConnectionLayerEx;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.ForestRouter;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.OrthogonalRouter;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.gef.ui.internal.editpolicies.GraphicalEditPolicyEx;
import org.eclipse.gmf.runtime.gef.ui.internal.l10n.Cursors;
import org.eclipse.gmf.runtime.gef.ui.internal.tools.SelectConnectionEditPartTracker;
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
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionFilter;

/**
 * the base edit part that controls <code>Edge</code> views, it is the basic
 * controller for the connection's view
 * 
 * @author mmostafa
 */
abstract public class ConnectionEditPart
	extends AbstractConnectionEditPart
	implements IGraphicalEditPart, PropertyChangeListener, IContainedEditPart,
	IPrimaryEditPart, NotificationListener {

	/** A map of listener filters ids to filter data */
	private Map listenerFilters;

	/** Used for registering and unregistering the edit part */
	private String elementGuid;

	/** Used for accessibility. */
	protected AccessibleEditPart accessibleEP;
	
    /**
     * Cache the editing domain after it is retrieved.
     */
    private TransactionalEditingDomain editingDomain;

	/**
	 * gets a property change command for the passed property, using both of the
	 * old and new values
	 * 
	 * @param property
	 *            the property associated with the command
	 * @param oldValue
	 *            the old value associated with the command
	 * @param newValue
	 *            the new value associated with the command
	 * @return a command
	 */
	protected Command getPropertyChangeCommand(Object property,
			Object oldValue, Object newValue) {
		// by default return null, which means there is no special command to
		// change the property
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void activate() {
		addNotationalListeners();

		EObject semanticProxy = ((View) getModel()).getElement();
		EObject semanticElement = EMFCoreUtil.resolve(getEditingDomain(), semanticProxy);

		if (semanticElement != null)
			addSemanticListeners();
		else if (semanticProxy != null) {
			addListenerFilter("SemanticProxy", this, semanticProxy); //$NON-NLS-1$
		}
		super.activate();
	}

	/**
	 * Adds a listener filter by adding the given listener to a passed notifier
	 * 
	 * @param filterId
	 *            A unique filter id (within the same editpart instance)
	 * @param listener
	 *            A listener instance
	 * @param notifier
	 *            An element notifer to add the listener to
	 */
	protected void addListenerFilter(String filterId,
			NotificationListener listener, EObject element) {

		if (element == null)
			return;

		Assert.isNotNull(filterId);
		Assert.isNotNull(listener);

		if (listenerFilters == null)
			listenerFilters = new HashMap();

		getDiagramEventBroker().addNotificationListener(element,
			listener);
		listenerFilters.put(filterId, new Object[] {element, listener});
	}

	/**
	 * Adds a listener filter by adding the given listener to a passed notifier
	 * 
	 * @param filterId
	 *            A unique filter id (within the same editpart instance)
	 * @param listener
	 *            A listener instance
	 * @param notifier
	 *            An element notifer to add the listener to
	 */
	protected void addListenerFilter(String filterId,
			NotificationListener listener, EObject element,
			EStructuralFeature feature) {

		if (element == null)
			return;

		Assert.isNotNull(filterId);
		Assert.isNotNull(listener);

		if (listenerFilters == null)
			listenerFilters = new HashMap();

		getDiagramEventBroker().addNotificationListener(element,
			feature, listener);
		listenerFilters
			.put(filterId, new Object[] {element, feature, listener});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createChild(java.lang.Object)
	 */
	final protected EditPart createChild(Object model) {
		return EditPartService.getInstance()
			.createGraphicEditPart((View) model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createConnection(java.lang.Object)
	 */
	final protected org.eclipse.gef.ConnectionEditPart createConnection(
			Object connectionView) {
		return (org.eclipse.gef.ConnectionEditPart) createChild(connectionView);
	}

	/**
	 * Overridden to support editpolicies installed programmatically and via the
	 * <code>EditPolicyService</code>. Subclasses should override
	 * <code>createDefaultEditPolicies()</code>.
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	final protected void createEditPolicies() {
		createDefaultEditPolicies();
		EditPolicyService.getInstance().createEditPolicies(this);
	}

	/**
	 * Should be overridden to install editpolicies programmatically.
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createDefaultEditPolicies() {
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
			new SemanticEditPolicy());
		installEditPolicy(EditPolicyRoles.PROPERTY_HANDLER_ROLE,
			new PropertyHandlerEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
			new org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ROLE,
			new ConnectionEditPolicy());
		installBendpointEditPolicy();
		installEditPolicy(EditPolicyRoles.DECORATION_ROLE,
			new DecorationEditPolicy());
		installEditPolicy(EditPolicyRoles.CONNECTION_LABELS_ROLE,
			new ConnectionLabelsEditPolicy());

		installEditPolicy(EditPolicyRoles.SNAP_FEEDBACK_ROLE,
			new SnapFeedbackPolicy());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	public void deactivate() {
		boolean wasActive = isActive();
		super.deactivate();
		if (listenerFilters != null && wasActive != isActive()) {
			for (Iterator i = listenerFilters.keySet().iterator(); i.hasNext();) {
				Object[] obj = (Object[]) listenerFilters.get(i.next());
				if (obj.length > 2) {
					getDiagramEventBroker().removeNotificationListener(
						(EObject) obj[0], (EStructuralFeature) obj[1],
						(NotificationListener) obj[2]);
				} else {
					getDiagramEventBroker().removeNotificationListener(
						(EObject) obj[0], (NotificationListener) obj[1]);
				}
			}
		}
	}

	/**
	 * executes the passed command
	 * 
	 * @param command
	 *            the command to execute
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
			public Object getAdapter(Object adaptableObject, Class adapterType) {

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
				return new Class[] {IActionFilter.class, View.class};
			}

		}, IGraphicalEditPart.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getAccessibleEditPart()
	 */
	protected AccessibleEditPart getAccessibleEditPart() {
		if (accessibleEP == null) {
			accessibleEP = new AccessibleGraphicalEditPart() {

				private String getSemanticName() {
					EObject semanticElement = resolveSemanticElement();

					if (semanticElement != null) {
						String name = semanticElement.getClass().getName();
						int startIndex = name.lastIndexOf('.') + 1;
						int endIndex = name.lastIndexOf("Impl"); //$NON-NLS-1$
						return name.substring(startIndex, endIndex);
					}

					return DiagramUIMessages.Accessible_Connection_Label;
				}

				public void getName(AccessibleEvent e) {

					EditPart sourceEP = getSource();
					EditPart targetEP = getTarget();

					// Get the Connection Name
					String connectionName = getSemanticName();

					// Get the Source Name
					String sourceName = null;
					if (sourceEP != null) {
						AccessibleEditPart aEP = (AccessibleEditPart) sourceEP
							.getAdapter(AccessibleEditPart.class);
						AccessibleEvent event = new AccessibleEvent(this);
						aEP.getName(event);
						sourceName = event.result;
					}

					// Get the Target Name
					String targetName = null;
					if (targetEP != null) {
						AccessibleEditPart aEP = (AccessibleEditPart) targetEP
							.getAdapter(AccessibleEditPart.class);
						AccessibleEvent event = new AccessibleEvent(this);
						aEP.getName(event);
						targetName = event.result;
					}
					
					if (sourceName != null && targetName != null) {
						e.result = NLS
							.bind(
								DiagramUIMessages.Accessible_Connection_From_Source_To_Target,
								new Object[] {connectionName, sourceName,
									targetName});
					} else if (sourceName != null) {
						e.result = NLS
							.bind(
								DiagramUIMessages.Accessible_Connection_From_Source,
								new Object[] {connectionName, sourceName});
					} else if (targetName != null) {
						e.result = NLS.bind(
							DiagramUIMessages.Accessible_Connection_To_Target,
							new Object[] {connectionName, targetName});
					} else {
						e.result = connectionName;
					}
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

			Boolean val = (Boolean) getViewer().getProperty(
				RulerProvider.PROPERTY_RULER_VISIBILITY);
			if (val != null && val.booleanValue())
				snapStrategies.add(new SnapToGuides(this));

			val = (Boolean) getViewer().getProperty(
				SnapToGeometry.PROPERTY_SNAP_ENABLED);
			if (val != null && val.booleanValue())
				snapStrategies.add(new SnapToGrid(this));

			if (snapStrategies.size() == 0)
				return null;

			if (snapStrategies.size() == 1)
				return snapStrategies.get(0);

			SnapToHelper ss[] = new SnapToHelper[snapStrategies.size()];
			for (int i = 0; i < snapStrategies.size(); i++)
				ss[i] = (SnapToHelper) snapStrategies.get(i);
			return new CompoundSnapToHelper(ss);
		}

		Object model = getModel();

		if (View.class.isAssignableFrom(key) && key.isInstance(model) ) {
			return getModel();
		}

		if (model != null && model instanceof View) {
			// Adapt to semantic element
			EObject semanticObject = ViewUtil
				.resolveSemanticElement((View) model);
			if (key.isInstance(semanticObject)) {
				return semanticObject;
			} else if (key.isInstance(model)) {
				return model;
			}
		}
		return super.getAdapter(key);
	}

	/**
	 * Method getChildBySemanticHint.
	 * 
	 * @param semanticHint
	 * @return IGraphicalEditPart
	 */
	public IGraphicalEditPart getChildBySemanticHint(String semanticHint) {
		if (getModel() != null) {
			View view = ViewUtil.getChildBySemanticHint((View) getModel(),
				semanticHint);
			if (view != null)
				return (IGraphicalEditPart) getViewer().getEditPartRegistry()
					.get(view);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#getCommand(org.eclipse.gef.Request)
	 */
	public Command getCommand(Request _request) {
		if (!isEditModeEnabled()) {
			return UnexecutableCommand.INSTANCE;
		}

		final Request request = _request;
		try {
			Command cmd = (Command) getEditingDomain().runExclusive(
                new RunnableWithResult.Impl() {

				public void run() {
					setResult(ConnectionEditPart.super.getCommand(request));
				}

			});
			return cmd;
		} catch (InterruptedException e) {
			Trace.catching(DiagramUIPlugin.getInstance(),
				DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
				"getCommand", e); //$NON-NLS-1$
			Log.error(DiagramUIPlugin.getInstance(),
				DiagramUIStatusCodes.IGNORED_EXCEPTION_WARNING,
				"getCommand", e); //$NON-NLS-1$
			return null;
		}
	}

	/**
	 * Convenience method returning the editpart's Diagram, the Diagam that owns
	 * the edit part
	 * 
	 * @return the diagram
	 */
	protected Diagram getDiagramView() {
		return (Diagram) getRoot().getContents().getModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart#getPrimaryView()
	 */
	public View getPrimaryView() {
		for (EditPart parent = this; parent != null; parent = parent
			.getParent())
			if (parent instanceof IPrimaryEditPart)
				return (View) parent.getModel();
		return null;
	}

	/**
	 * Convenience method returning the editpart's edit domain. Same as calling
	 * <code>getRoot().getViewer().getEditDomain()</code>
	 * 
	 * @return the edit domain
	 */
	protected EditDomain getEditDomain() {
		return getRoot().getViewer().getEditDomain();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart#getDiagramEditDomain()
	 */
	public IDiagramEditDomain getDiagramEditDomain() {
		return (IDiagramEditDomain) getEditDomain();
	}

	/**
	 * Return this editpart's view (model) children.
	 * 
	 * @return list of views.
	 */
	protected List getModelChildren() {
		return ((View) getModel()).getChildren();
	}

	/**
	 * Convenience method to retreive the value for the supplied poperty from
	 * the editpart's associated view element.
	 * 
	 * @param id
	 *            the property id
	 * @return Object the value
	 * @deprecated use {@link #getStructuralFeatureValue(EStructuralFeature)}
	 *             instead
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
		return ViewUtil.getStructuralFeatureValue((View) getModel(), feature);
	}

	/**
	 * try to resolve the semantic element and Return the resolven element; if
	 * the element is unresolvable or null it will return null
	 * 
	 * @return non proxy EObject or NULL
	 */
	public EObject resolveSemanticElement() {
		try {
			return (EObject) getEditingDomain().runExclusive(
                new RunnableWithResult.Impl() {
	
				public void run() {
					setResult(ViewUtil.resolveSemanticElement((View) getModel()));
				}
			});
		} catch (InterruptedException e) {
			Trace.catching(DiagramUIPlugin.getInstance(),
				DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
				"resolveSemanticElement", e); //$NON-NLS-1$
			Log.error(DiagramUIPlugin.getInstance(),
				DiagramUIStatusCodes.IGNORED_EXCEPTION_WARNING,
				"resolveSemanticElement", e); //$NON-NLS-1$
			return null;
		}

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
	 * 
	 * @return <code>View</code>, the associated view or null if there is no
	 *         associated Notation View
	 */
	public View getNotationView() {
		Object model = getModel();
		if (model instanceof View)
			return (View) model;
		return null;
	}

	/**
	 * Handles the passed property changed event only if the editpart's view is
	 * not deleted
	 */
	public final void propertyChange(PropertyChangeEvent event) {
		if (isActive())
			handlePropertyChangeEvent(event);
	}

	/**
	 * Handles the property changed event.  Clients should override to
	 * respond to the specific notification events they are interested.
	 * 
	 * Note: This method may get called on a non-UI thread.  Clients should
	 * either ensure that their code is thread safe and/or doesn't make
	 * unsupported calls (i.e. Display.getCurrent() ) assuming they are on
	 * the main thread.  Alternatively if this is not possible, then the
	 * client can wrap their handler within the Display.synchExec runnable
	 * to ensure synchronization and subsequent execution on the main thread.
	 * 
	 * @param event
	 *            the <code>Notification</code> object that is the property changed event
	 */
	protected void handlePropertyChangeEvent(PropertyChangeEvent event) {
		if (event.getPropertyName().equals(
			Connection.PROPERTY_CONNECTION_ROUTER)) {
			installRouter();
		}
	}

	/**
	 * Method reactivateSemanticModel. This method reactivates the edit part's
	 * emantic model by: 1- removing semantic listeners 2- adding semantic
	 * listeners if the semantic reference is resolvable 3- Refreshing it
	 * 
	 * This method is called in response to IView's Properties.ID_SEMANTICREF
	 * property change event However, it will only work under the following
	 * assumptions: 1- The old and new semantic models are compatible in their
	 * kind 2- The deltas between old and new semantic models do not affect
	 * notation 3- Connections are not refereshed since they are maintained by
	 * the diagram
	 */
	public void reactivateSemanticModel() {
		removeSemanticListeners();
		if (resolveSemanticElement() != null)
			addSemanticListeners();
		refresh();
	}

	/** Finds an editpart given a starting editpart and an EObject */
	public EditPart findEditPart(EditPart epBegin, EObject theElement) {
		if (theElement == null) {
			return null;
		}
		EditPart epStart = null;
		if (epBegin == null) {
			epStart = this;
		} else {
			epStart = epBegin;
		}

		final View view = (View) ((IAdaptable) epStart).getAdapter(View.class);

		if (view != null) {
			EObject el = ViewUtil.resolveSemanticElement(view);

			if ((el != null) && el.equals(theElement)) {
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
		LineStyle style = (LineStyle) getPrimaryView().getStyle(
			NotationPackage.eINSTANCE.getLineStyle());
		if (style != null)
			setForegroundColor(DiagramColorRegistry
				.getInstance().getColor(new Integer(style.getLineColor())));
	}

	/**
	 * Refresh the editpart's figure visibility.
	 */
	protected void refreshVisibility() {
		setVisibility(((View) getModel()).isVisible());
	}

	/**
	 * Removes a listener previously added with the given id
	 * 
	 * @param filterId
	 *            the filiter ID
	 */
	protected void removeListenerFilter(String filterId) {
		if (listenerFilters == null)
			return;

		Object[] objects = (Object[]) listenerFilters.get(filterId);
		if (objects == null) {
			return;
		}

		if (objects.length > 2) {
			DiagramEventBroker.getInstance(getEditingDomain()).removeNotificationListener(
				(EObject) objects[0], (EStructuralFeature) objects[1],
				(NotificationListener) objects[2]);
		} else {
			getDiagramEventBroker().removeNotificationListener(
				(EObject) objects[0], (NotificationListener) objects[1]);
		}
		listenerFilters.remove(filterId);
	}

	/**
	 * sets the forefround color of the editpart's figure
	 * 
	 * @param color
	 *            the color
	 */
	protected void setForegroundColor(Color color) {
		getFigure().setForegroundColor(color);
	}

	/**
	 * Convenience method to set a property value.
	 * 
	 * @param id
	 * @param value
	 * @deprecated use {@link #setStructuralFeatureValue(Object, Object)}
	 *             instead
	 */
	public void setPropertyValue(Object id, Object value) {
		ViewUtil.setPropertyValue((View) getModel(), id, value);
	}

	/**
	 * Sets the passed feature if possible on this editpart's view to the passed
	 * value.
	 * 
	 * @param feature
	 *            the feature to use
	 * @param value
	 *            the value of the property being set
	 */
	public void setStructuralFeatureValue(EStructuralFeature feature,
			Object value) {
		ViewUtil.setStructuralFeatureValue((View) getModel(), feature, value);
	}

	/**
	 * sets the edit part's visibility
	 * 
	 * @param vis
	 *            the new visibilty value
	 */
	protected void setVisibility(boolean vis) {
		if (!vis && getSelected() != SELECTED_NONE)
			getViewer().deselect(this);
		getFigure().setVisible(vis);
		getFigure().revalidate();
	}

	/**
	 * This method adds all listeners to the notational world (views, figures,
	 * editpart...etc) Override this method to add more notational listeners
	 * down the hierarchy
	 */
	protected void addNotationalListeners() {
		addListenerFilter("View", this, (View) getModel());//$NON-NLS-1$
		getFigure().addPropertyChangeListener(
			Connection.PROPERTY_CONNECTION_ROUTER, this);
	}

	/**
	 * This method adds all listeners to the semantic element behind this EditPart 
	 * Override this method to add more semantic listeners down the hierarchy
	 * This method is called only if the semantic element is resolvable
	 */
	protected void addSemanticListeners() {
		addListenerFilter("SemanticModel",//$NON-NLS-1$
			this, resolveSemanticElement());
	}

	/**
	 * This method removes all listeners to the notational world (views,
	 * figures, editpart...etc) Override this method to remove notational
	 * listeners down the hierarchy
	 */
	protected void removeNotationalListeners() {
		getFigure().removePropertyChangeListener(
			Connection.PROPERTY_CONNECTION_ROUTER, this);
		removeListenerFilter("View");//$NON-NLS-1$
	}

	/**
	 * This method removes all listeners to the semantic element behind this EditPart
	 * Override this method to remove semantic listeners
	 * down the hierarchy
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
	 * a static array of appearance property ids applicable to the connections
	 */
	protected static final String[] appearanceProperties = new String[] {
		Properties.ID_FONTNAME, Properties.ID_FONTSIZE, Properties.ID_FONTBOLD,
		Properties.ID_FONTITALIC, Properties.ID_FONTCOLOR,
		Properties.ID_LINECOLOR};

	/**
	 * construcotr
	 * 
	 * @param view ,
	 *            the view the edit part will own
	 */
	public ConnectionEditPart(View view) {
		setModel(view);
		this.editableEditPart = new DefaultEditableEditPart(this);
	}

	/**
	 * Method createConnectionFigure.
	 * 
	 * @return a <code>Connection</code> figure
	 */
	abstract protected Connection createConnectionFigure();

	final protected IFigure createFigure() {
		return createConnectionFigure();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#refresh()
	 */
	public void refresh() {
		if (getSource() != null && getTarget() != null) {
			try {
                getEditingDomain().runExclusive(new Runnable() {

						public void run() {
							ConnectionEditPart.super.refresh();
							EditPolicyIterator i = getEditPolicyIterator();
							while (i.hasNext()) {
								EditPolicy policy = i.next();
								if (policy instanceof GraphicalEditPolicyEx) {
									((GraphicalEditPolicyEx) policy).refresh();
								}
							}
						}
					});
			} catch (InterruptedException e) {
				Trace.catching(DiagramUIPlugin.getInstance(),
					DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
					"refresh", e); //$NON-NLS-1$
				Log.error(DiagramUIPlugin.getInstance(),
					DiagramUIStatusCodes.IGNORED_EXCEPTION_WARNING,
					"refresh", e); //$NON-NLS-1$
			}
		}
	}

	/**
	 * utility method to get the <code>Edge</code> view
	 * 
	 * @return the <code>Edge</code>
	 */
	protected Edge getEdge() {
		return (Edge) getModel();
	}

	/*
	 * @see AbstractEditPart#getDragTracker(Request)
	 */
	public DragTracker getDragTracker(Request req) {
		return new SelectConnectionEditPartTracker(this);
	}

	/**
	 * give access to the source of the edit part's Edge
	 * 
	 * @return the source
	 */
	protected Object getModelSource() {
		return getEdge().getSource();
	}

	/**
	 * give access to the target of the edit part's Edge
	 * 
	 * @return the target
	 */
	protected Object getModelTarget() {
		return getEdge().getTarget();
	}

	/**
	 * installes a router on the edit part, depending on the
	 * <code>RoutingStyle</code>
	 */
	protected void installRouter() {
		ConnectionLayer cLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
		RoutingStyle style = (RoutingStyle) ((View) getModel())
			.getStyle(NotationPackage.eINSTANCE.getRoutingStyle());

		if (style != null && cLayer instanceof ConnectionLayerEx) {

            ConnectionLayerEx cLayerEx = (ConnectionLayerEx)cLayer;
			Routing routing = style.getRouting();
			if (Routing.MANUAL_LITERAL == routing) {
				getConnectionFigure().setConnectionRouter(
                    cLayerEx.getObliqueRouter());
			} else if (Routing.RECTILINEAR_LITERAL == routing) {
				getConnectionFigure().setConnectionRouter(
                    cLayerEx.getRectilinearRouter());
			} else if (Routing.TREE_LITERAL == routing) {
				getConnectionFigure().setConnectionRouter(
                    cLayerEx.getTreeRouter());
			}

		}

		refreshRouterChange();
	}

	/**
	 * refresh the pendpoints owned by the EditPart's <code>Edge</code>
	 */
	protected void refreshBendpoints() {
		RelativeBendpoints bendpoints = (RelativeBendpoints) getEdge()
			.getBendpoints();
		List modelConstraint = bendpoints.getPoints();
		List figureConstraint = new ArrayList();
		for (int i = 0; i < modelConstraint.size(); i++) {
			org.eclipse.gmf.runtime.notation.datatype.RelativeBendpoint wbp = (org.eclipse.gmf.runtime.notation.datatype.RelativeBendpoint) modelConstraint
				.get(i);
			RelativeBendpoint rbp = new RelativeBendpoint(getConnectionFigure());
			rbp.setRelativeDimensions(new Dimension(wbp.getSourceX(), wbp
				.getSourceY()), new Dimension(wbp.getTargetX(), wbp
				.getTargetY()));
			rbp.setWeight((i + 1) / ((float) modelConstraint.size() + 1));
			figureConstraint.add(rbp);
		}
		getConnectionFigure().setRoutingConstraint(figureConstraint);
	}

	private void installBendpointEditPolicy() {
		if (getConnectionFigure().getConnectionRouter() instanceof ForestRouter) {
			installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE,
				new TreeConnectionBendpointEditPolicy());
			getConnectionFigure().setCursor(Cursors.CURSOR_SEG_MOVE);
		} else if (getConnectionFigure().getConnectionRouter() instanceof OrthogonalRouter) {
			installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE,
				new ConnectionLineSegEditPolicy());
			getConnectionFigure().setCursor(Cursors.CURSOR_SEG_MOVE);
		} else {
			installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE,
				new ConnectionBendpointEditPolicy());
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
		PolylineConnectionEx poly = ((PolylineConnectionEx) getConnectionFigure());
		RoutingStyle style = (RoutingStyle) ((View) getModel())
			.getStyle(NotationPackage.eINSTANCE.getRoutingStyle());
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
		PolylineConnectionEx poly = ((PolylineConnectionEx) getConnectionFigure());
		RoutingStyle style = (RoutingStyle) ((View) getModel())
			.getStyle(NotationPackage.eINSTANCE.getRoutingStyle());

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
		PolylineConnectionEx poly = ((PolylineConnectionEx) getConnectionFigure());

		RoutingStyle style = (RoutingStyle) ((View) getModel())
			.getStyle(NotationPackage.eINSTANCE.getRoutingStyle());
		if (style != null) {

			boolean closestDistance = style.isClosestDistance();
			boolean avoidObstruction = style.isAvoidObstructions();

			poly.setRoutingStyles(closestDistance, avoidObstruction);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
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
		FontStyle style = (FontStyle) getPrimaryView().getStyle(
			NotationPackage.eINSTANCE.getFontStyle());
		if (style != null) {
			setFont(new FontData(style.getFontName(), style.getFontHeight(),
				(style.isBold() ? SWT.BOLD
					: SWT.NORMAL) | (style.isItalic() ? SWT.ITALIC
					: SWT.NORMAL)));
		}
	}

	/**
	 * Sets the font to the label. This method could be overriden to change the
	 * font data of the font overrides typically look like this: super.setFont(
	 * new FontData( fontData.getName(), fontData.getHeight(),
	 * fontData.getStyle() <| &> SWT.????));
	 * 
	 * @param fontData
	 *            the font data
	 */
	protected void setFont(FontData fontData) {
		getFigure().setFont(
			DiagramFontRegistry.getInstance().getFont(Display.getDefault(),
				fontData));
		getFigure().repaint();
	}

	/**
	 * Returns an array of the appearance property ids applicable to the
	 * receiver. Fro this type it is Properties.ID_FONT,
	 * Properties.ID_FONTCOLOR, Properties.ID_LINECOLOR
	 * 
	 * @return - an array of the appearane property ids applicable to the
	 *         receiver
	 */
	protected String[] getAppearancePropertyIDs() {
		return appearanceProperties;
	}

	/**
	 * Perform a request by executing a command from the target editpart of the
	 * request For the Direct_Edit request, we need to show up an editor first
	 * 
	 * @see org.eclipse.gef.EditPart#performRequest(org.eclipse.gef.Request)
	 */
	public void performRequest(Request request) {
		if (!isEditModeEnabled()) {
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
	 * 
	 * @param request
	 *            the direct edit request
	 */
	protected void performDirectEditRequest(Request request) {
		try {
			EditPart primaryChildEditPart = (EditPart) getEditingDomain()
				.runExclusive(new RunnableWithResult.Impl() {

						public void run() {
							setResult(getPrimaryChildEditPart());
						}
					});
			if (primaryChildEditPart != null) {
				primaryChildEditPart.performRequest(request);
			}

		} catch (InterruptedException e) {
			Trace.catching(DiagramUIPlugin.getInstance(),
				DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
				"performDirectEditRequest", e); //$NON-NLS-1$
			Log.error(DiagramUIPlugin.getInstance(),
				DiagramUIStatusCodes.IGNORED_EXCEPTION_WARNING,
				"performDirectEditRequest", e); //$NON-NLS-1$
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
		if (ref == null) {
			return;
		}

		elementGuid = EMFCoreUtil.getProxyID(ref);

		((IDiagramGraphicalViewer) getViewer()).registerEditPartForElement(
			elementGuid, this);
	}

	/** Remove this editpart from the EditPartForElement map. */
	protected void unregisterModel() {
		super.unregisterModel();

		((IDiagramGraphicalViewer) getViewer()).unregisterEditPartForElement(
			elementGuid, this);
	}

	/**
	 * Handles the case where the semantic reference has changed.
	 */
	protected final void handleMajorSemanticChange() {
		if (getSource() instanceof GraphicalEditPart
			&& getTarget() instanceof GraphicalEditPart) {
			((GraphicalEditPart) getSource()).refreshSourceConnection(this);
			((GraphicalEditPart) getTarget()).refreshTargetConnection(this);
		}
	}

	/**
	 * Refreshes a child editpart by removing it and refreshing children
	 * 
	 * @param child
	 */
	final void refreshChild(GraphicalEditPart child) {
		removeChild(child);
		refreshChildren();
	}

	/**
	 * check if there is a canonical edit policy installed on the edit part or
	 * not
	 * 
	 * @return <tt>true</tt> if a canonical editpolicy has been installed on
	 *         this editpart; otherwise <tt>false</tt>
	 */
	public final boolean isCanonical() {
		return getEditPolicy(EditPolicyRoles.CANONICAL_ROLE) != null;
	}

	/**
	 * checks if the edit part's figure is visible or not
	 * 
	 * @return <tt>true</tt> if the editpart's figure is visible;
	 *         <tt>false</tt> otherwise.
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
		if (!isEditModeEnabled()) {
			return;
		}

		super.showSourceFeedback(request);
	}

	/*
	 * @see org.eclipse.gef.EditPart#showTargetFeedback(org.eclipse.gef.Request)
	 */
	public void showTargetFeedback(Request request) {
		if (!isEditModeEnabled()) {
			return;
		}

		super.showTargetFeedback(request);
	}

	/*
	 * @see org.eclipse.gef.EditPart#eraseSourceFeedback(org.eclipse.gef.Request)
	 */
	public void eraseSourceFeedback(Request request) {
		if (!isEditModeEnabled()) {
			return;
		}

		super.eraseSourceFeedback(request);
	}

	/*
	 * @see org.eclipse.gef.EditPart#eraseTargetFeedback(org.eclipse.gef.Request)
	 */
	public void eraseTargetFeedback(Request request) {
		if (!isEditModeEnabled()) {
			return;
		}

		super.eraseTargetFeedback(request);
	}

	/**
	 * this method will return the primary child EditPart inside this edit part
	 * 
	 * @return the primary child view inside this edit part
	 */
	public EditPart getPrimaryChildEditPart() {
		if (getChildren().size() > 0)
			return (EditPart) getChildren().get(0);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart#getDiagramPreferencesHint()
	 */
	public PreferencesHint getDiagramPreferencesHint() {
		if (getRoot() instanceof IDiagramPreferenceSupport) {
			return ((IDiagramPreferenceSupport) getRoot()).getPreferencesHint();
		}
		return PreferencesHint.USE_DEFAULTS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.core.listener.NotificationListener#notifyChanged(org.eclipse.emf.common.notify.Notification)
	 */
	public void notifyChanged(Notification notification) {
		if (isActive()) {
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
		if (NotationPackage.eINSTANCE.getView_PersistedChildren().equals(
			feature)
			|| NotationPackage.eINSTANCE.getView_TransientChildren().equals(
				feature)) {
			refreshChildren();
		} else if (NotationPackage.eINSTANCE.getView_Visible().equals(feature)) {
            Object notifier = event.getNotifier();
            if (notifier== getModel())
			setVisibility(((Boolean) event.getNewValue()).booleanValue());
			// Reactivating in response to semantic model reference change
			// However, we need to verify that the event belongs to this
			// editpart's view
		} else if (NotationPackage.eINSTANCE.getRoutingStyle_Routing().equals(
			feature)) {
			installRouter();
		} else if (NotationPackage.eINSTANCE.getRoutingStyle_Smoothness()
			.equals(feature)
			|| NotationPackage.eINSTANCE.getRoutingStyle_AvoidObstructions()
				.equals(feature)
			|| NotationPackage.eINSTANCE.getRoutingStyle_ClosestDistance()
				.equals(feature)
			|| NotationPackage.eINSTANCE.getRoutingStyle_JumpLinkStatus()
				.equals(feature)
			|| NotationPackage.eINSTANCE.getRoutingStyle_JumpLinkType().equals(
				feature)
			|| NotationPackage.eINSTANCE.getRoutingStyle_JumpLinksReverse()
				.equals(feature)) {
			refreshVisuals();
		} else if (NotationPackage.eINSTANCE.getLineStyle_LineColor().equals(
			feature)) {
			Integer c = (Integer) event.getNewValue();
			setForegroundColor(DiagramColorRegistry
				.getInstance().getColor(c));
		} else if (NotationPackage.eINSTANCE.getRelativeBendpoints_Points()
			.equals(feature)) {
			refreshBendpoints();
		} else if (event.getFeature() == NotationPackage.eINSTANCE
			.getView_Element()
			&& ((EObject) event.getNotifier()) == getNotationView())
			handleMajorSemanticChange();
	}

	/**
	 * @return <code>IMapMode</code> that allows for the coordinate mapping from device to
	 * logical units. 
	 */
	final protected IMapMode getMapMode() {
		RootEditPart root = getRoot();
		if (root instanceof DiagramRootEditPart) {
			DiagramRootEditPart dgrmRoot = (DiagramRootEditPart)root;
			return dgrmRoot.getMapMode();
		}

		return MapModeUtil.getMapMode();
	}
	
    /**
     * Derives my editing domain from my diagram element. Subclasses may
     * override.
     */
    public TransactionalEditingDomain getEditingDomain() {
        if (editingDomain == null) {
            // try to get the editing domain for the model
            editingDomain = TransactionUtil.getEditingDomain(getModel());
            
            if (editingDomain == null) {
                // try to get the editing domain from the diagram view
                editingDomain = TransactionUtil.getEditingDomain(getDiagramView());
            }
        }
        return editingDomain;
    } 	
    
	/**
	 * Gets the diagram event broker from the editing domain.
	 * 
	 * @return the diagram event broker
	 */
	private DiagramEventBroker getDiagramEventBroker() {
        TransactionalEditingDomain theEditingDomain = getEditingDomain();
        if (theEditingDomain != null) {
            return DiagramEventBroker.getInstance(theEditingDomain);
        }
        return null;
    }
}
