/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.commands.DeferredLayoutCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.SetViewMutabilityCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.figures.ICanonicalShapeCompartmentLayout;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectorViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectorViewRequest.ConnectorViewDescriptor;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;

/**
 * A specialized implementation of <code>CanonicalEditPolicy</code>.
 * This implementation will manage connectors owned by the semantic host.
 * 
 * @author mhanner / sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public abstract class CanonicalConnectionEditPolicy
	extends CanonicalEditPolicy {

	/**
	 * Return a list of semantic relationships contained inside this
	 * compartment.
	 * <P>
	 * Sample implementation - Region.
	 * 
	 * <pre>
	 *   IUML2Region regions = (IUML2Region)resolveSemanticElement();
	 *   return regions == null
	 *   ? Collections.EMPTY_LIST
	 *   : regions.getTransitions();
	 *   @return IElements list
	 * 
	 */
	abstract protected List getSemanticConnectionsList();

	/**
	 * Return the supplied relationship's source connection.
	 * <P>
	 * Sample implementation - a transition element <BR>
	 * 
	 * <pre>
	 * return ProxyUtil.resolve((InternalEObject) ((IUML2Transition) relationship)
	 * 	.getSource());
	 * </pre>
	 * 
	 * @param relationship
	 *            semantic connector
	 */
	abstract protected EObject getSourceElement(EObject relationship);

	/**
	 * Return the supplied relationship's target connection.
	 * <P>
	 * Sample implementation - a transition element <BR>
	 * 
	 * <pre>
	 * return ProxyUtil.resolve((InternalEObject) ((IUML2Transition) relationship)
	 * 	.getTarget());
	 * </pre>
	 * 
	 * @param relationship
	 *            semantic connector
	 */
	abstract protected EObject getTargetElement(EObject relationship);

	/** Returns the diagram's connector views. */
	private List getDiagramConnections() {
		Diagram dView = ((View) host().getModel()).getDiagram();
		return dView == null ? Collections.EMPTY_LIST
			: new ArrayList(dView.getEdges());
	}

	/** Return an empty list. */
	protected List getSemanticChildrenList() {
		return Collections.EMPTY_LIST;
	}

	/**
	 * Return <tt>true</tt> if the connector should be drawn between the
	 * supplied endpoints; otherwise return <tt>false</tt>.
	 * 
	 * @param a
	 *            connector's source element
	 * @param a
	 *            connector's target element
	 * @return <tt>true</tt> if both parameters are not <tt>null</tt>;
	 *         otherwise <tt>false</tt>
	 */
	protected boolean canCreateConnection(EditPart sep, EditPart tep,
			EObject connector) {
		if (sep != null && sep.isActive() && tep != null && tep.isActive()) {

			View src = (View) sep.getAdapter(View.class);
			View tgt = (View) tep.getAdapter(View.class);
			if (src != null && tgt != null) {

				return sep.getParent().getEditPolicy(
					EditPolicyRoles.CANONICAL_ROLE) != null
					&& tep.getParent().getEditPolicy(
						EditPolicyRoles.CANONICAL_ROLE) != null;
			}
		}
		return false;
	}

	/**
	 * Return the editpart mapped to the object. The editpart is retrieved from
	 * a [view,semantic] mapping. {@link #getView(IElement)}is called if the
	 * mapping cannot be found inside this manager.
	 * 
	 * @param element
	 *            an <tt>View</tt> or <tt>IElement</tt> instance.
	 * @return an editpart; <tt>null</tt> if non could be found.
	 */
	protected final EditPart getEditPartFor(Object element) {
		if (element instanceof EObject && !(element instanceof View)) {
			EObject eObject = (EObject) element;
			EditPartViewer viewer = getHost().getViewer();
			if (viewer instanceof IDiagramGraphicalViewer) {
				List parts = ((IDiagramGraphicalViewer) viewer)
					.findEditPartsForElement(EObjectUtil.getID(eObject),
						ShapeNodeEditPart.class);

				if (parts.isEmpty()) {
					// reach for the container's editpart instead and force it
					// to refresh
					EObject container = ((EObject) element).eContainer();
					EditPart containerEP = getEditPartFor(container);
					if (containerEP != null) {
						containerEP.refresh();
						parts = ((IDiagramGraphicalViewer) viewer)
							.findEditPartsForElement(EObjectUtil.getID(eObject),
								ShapeNodeEditPart.class);
					}
				}

				// Check if the part is contained with-in the host EditPart
				// since we are canonically updated the host.
				EditPart ancestor = getHost();
				while (ancestor != null) {
					EditPart ep = reachForEditPartWithAncestor(parts, ancestor);
					if (ep != null)
						return ep;
					ancestor = ancestor.getParent();
				}

				return null;
			}
		}

		return (EditPart) host().getViewer().getEditPartRegistry().get(element);
	}

	/**
	 * Walks up the container tree and tries to find the EditPart that has the
	 * given <code>EditPart</code> as an ancestor.
	 * 
	 * @param results
	 *            <code>List</code> of <code>EditPart</code> objects
	 * @param ancestor
	 *            <code>EditPart</code> to check against.
	 * @return <code>EditPart</code> that contains the <code>ancestor</code>
	 *         in it's containment hierarchy
	 */
	private EditPart reachForEditPartWithAncestor(List results,
			EditPart ancestor) {
		ListIterator li = results.listIterator();
		while (li.hasNext()) {
			EditPart ep = (EditPart) li.next();

			EditPart walker = ep.getParent();
			while (walker != null) {
				if (walker.equals(ancestor))
					return ep;
				walker = walker.getParent();
			}
		}

		return null;
	}

	/**
	 * Returns the default factory hint.
	 * 
	 * @return an empty string
	 */
	protected String getDefaultFactoryHint() {
		return "";//$NON-NLS-1$
	}

	/**
	 * Creates a connector view facde element for the supplied semantic element.
	 * An empty string is used as the default factory hint.
	 * 
	 * @param element
	 *            the semantic element
	 * @param the
	 *            connectors source editpart
	 * @param the
	 *            connectors target editpart
	 * @param index
	 *            semantic elements position
	 */
	protected final Edge createConnectionView(EObject connection, int index) {
		EditPart sep = getSourceEditPartFor(connection);
		EditPart tep = getTargetEditPartFor(connection);
		if (!canCreateConnection(sep, tep, connection)) {
			return null;
		}

		View sView = (View) sep.getModel();
		View tView = (View) tep.getModel();
		Edge model = null;
		String factoryHint = getDefaultFactoryHint();
		IAdaptable elementAdapter = new CanonicalElementAdapter(connection,
			factoryHint);
		CreateConnectorViewRequest ccr = getCreateConnectionViewRequest(
			elementAdapter, getFactoryHint(elementAdapter, factoryHint), index);

		//
		// I do not think that the following hack is necessary. The root
		// of the problem is that AbstractConnectorView#setSourceView() and
		// #setTargetView() are not persisting the source and target views
		// It used to but it was removed during a refactoring wave
		// TODO - remove hack.
		/*
		 * This is a temporary fix to the problem that if the source or the
		 * targetedit part is persisted the set connector ends command will
		 * modify the source and targets notational element by adding an edge
		 * there. For Now if either the source or the target is persisted this
		 * will create a persisted connector view.
		 */
		if (!ViewUtil.isTransient(sView) || !ViewUtil.isTransient(tView)) {
			CreateConnectorViewRequest.ConnectorViewDescriptor descriptor = ccr
				.getConnectorViewDescriptor();
			descriptor.setPersisted(true);
		}

		ccr.setType(RequestConstants.REQ_CONNECTION_START);
		ccr.setSourceEditPart(sep);
		getCreateViewCommand(ccr); // sep.getCommand(ccr); //prime the command
		ccr.setTargetEditPart(tep);
		ccr.setType(RequestConstants.REQ_CONNECTION_END);
		Command cmd = getCreateViewCommand(ccr); // tep.getCommand(ccr);
		if (cmd != null && cmd.canExecute()) {
			List viewAdapters = new ArrayList();
			viewAdapters.add(new EObjectAdapter(((View) host().getModel())
				.getDiagram()));
			viewAdapters.add(new EObjectAdapter(sView));
			viewAdapters.add(new EObjectAdapter(tView));

			SetViewMutabilityCommand.makeMutable(viewAdapters).execute();

			executeCommand(cmd);
			IAdaptable adapter = (IAdaptable) ccr.getNewObject();
			SetViewMutabilityCommand.makeMutable(adapter).execute();
			model = (Edge) adapter.getAdapter(Edge.class);
			if (model == null) {
				String eMsg = MessageFormat
					.format(
						PresentationResourceManager
							.getI18NString("CanonicalEditPolicy.create.view.failed_ERROR_"),//$NON-NLS-1$
						new Object[] {connection});
				IllegalStateException ise = new IllegalStateException(eMsg);
				Log.error(DiagramUIPlugin.getInstance(), IStatus.ERROR, eMsg,
					ise);
				throw ise;
			}
		}
		return model;
	}

	/**
	 * Calculates the <code>EditPart</code> that this connection element is
	 * connected to at it's target.
	 * 
	 * @param connector
	 *            the <code>EObject</code> element that we are canonical
	 *            trying to create a view for.
	 * @return the <code>EditPart</code> that is the source of the
	 *         <code>View</code> we want to create
	 */
	protected EditPart getTargetEditPartFor(EObject connector) {
		EObject tel;
		EditPart tep;
		tel = getTargetElement(connector);
		tep = getEditPartFor(tel);
		return tep;
	}

	/**
	 * Calculates the <code>EditPart</code> that this connection element is
	 * connected to at it's source.
	 * 
	 * @param connector
	 *            the <code>EObject</code> element that we are canonical
	 *            trying to create a view for.
	 * @return the <code>EditPart</code> that is the target of the
	 *         <code>View</code> we want to create
	 */
	protected EditPart getSourceEditPartFor(EObject connector) {
		EObject sel;
		EditPart sep;
		sel = getSourceElement(connector);
		sep = getEditPartFor(sel);
		return sep;
	}

	/**
	 * Forwards the supplied request to its source if the target is
	 * <tt>null</tt>; otherwise it is forwarded to the target. Forwards the
	 * supplied request to the editpart's <code>host</code>.
	 * 
	 * @param request
	 *            a <code>CreareConnecgtorViewRequest</code>
	 * @return Command to create the views in the request
	 */
	protected Command getCreateViewCommand(CreateRequest request) {
		if (request instanceof CreateConnectorViewRequest) {
			CreateConnectorViewRequest ccr = (CreateConnectorViewRequest) request;
			EditPart ep = ccr.getTargetEditPart() == null ? ccr
				.getSourceEditPart()
				: ccr.getTargetEditPart();
			return ep.getCommand(request);
		}

		return super.getCreateViewCommand(request);
	}

	/**
	 * Return a create view request.
	 * 
	 * @param descriptor
	 *            a {@link CreateViewRequest.ViewDescriptor}.
	 * @return a create request
	 */
	protected CreateViewRequest getCreateViewRequest(
			CreateViewRequest.ViewDescriptor descriptor) {
		return getCreateViewRequest(Collections.singletonList(descriptor));
	}

	/**
	 * Return a create view request. The request's location is set to
	 * {@link ICanonicalShapeCompartmentLayout#UNDEFINED}.
	 * 
	 * @param descriptors
	 *            a {@link CreateViewRequest.ViewDescriptor} list.
	 * @return a create request
	 */
	protected CreateViewRequest getCreateViewRequest(List descriptors) {
		CreateViewRequest cvr = super.getCreateViewRequest(descriptors);
		Point loc = ICanonicalShapeCompartmentLayout.UNDEFINED.getLocation();
		cvr.setLocation(loc);
		return cvr;
	}

	/**
	 * Return a create connector view request.
	 * 
	 * @param elementAdapter
	 *            semantic element
	 * @param viewKind
	 *            type of view to create
	 * @param hint
	 *            factory hint
	 * @param index
	 *            index
	 * @return a create <i>non-persisted </i> view request
	 */
	private CreateConnectorViewRequest getCreateConnectionViewRequest(
			IAdaptable elementAdapter, String hint, int index) {
		return new CreateConnectorViewRequest(getConnectionViewDescriptor(
			elementAdapter, hint, index));
	}

	/**
	 * Return a connector view descriptor.
	 * 
	 * @param elementAdapter
	 *            semantic element
	 * @param hint
	 *            factory hint
	 * @param index
	 *            index
	 * @return a create <i>non-persisted </i> connector view descriptor
	 */
	private ConnectorViewDescriptor getConnectionViewDescriptor(
			IAdaptable elementAdapter, String hint, int index) {
		return new ConnectorViewDescriptor(elementAdapter, hint, index, false,
			((IGraphicalEditPart) getHost()).getDiagramPreferencesHint());
	}

	/**
	 * Updates the set of connector views so that it is in sync with the
	 * semantic connectors. This method is called in response to notification
	 * from the model.
	 * <P>
	 * The update is performed by comparing the exising connector views with the
	 * set of semantic connectors returned from {@link #getSemanticConnectors()}.
	 * Views whose semantic connector no longer exists or whose semantic
	 * connector ends are <tt>null</tt> are
	 * {@link org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy#deleteViews(Iterator) removed}.
	 * New semantic children have their View
	 * {@link  #createConnectorView(IElement, EditPart, EditPart, int, String)
	 * created}. Subclasses must override <code>getSemanticConnectors()</code>.
	 * <P>
	 * This refresh routine will not reorder the view list to ensure both it and
	 * the semantic children are in the same order since it is possible that
	 * this editpolicy will only handle a specifc subset of the host's views.
	 * 
	 * This method should <em>not</em> be overridden.
	 * 
	 * @return <code>List</code> of new <code>IAdaptable</code> objects that
	 *         adapt to <code>View</code> objects that were created as a
	 *         result of the synchronization
	 */
	protected List refreshSemanticConnections() {
		Edge viewChild;
		EObject semanticChild;
		//
		// current connector views
		Collection viewChildren = getConnectionViews();
		Collection semanticChildren = new HashSet();
		semanticChildren.addAll(getSemanticConnectionsList());

		List orphaned = cleanCanonicalSemanticChildren(viewChildren,
			semanticChildren);

		//
		// create a view for each remaining semantic element.
		List viewDescriptors = new ArrayList();
		Iterator semanticChildrenIT = semanticChildren.iterator();
		while (semanticChildrenIT.hasNext()) {
			semanticChild = (EObject) semanticChildrenIT.next();
			viewChild = createConnectionView(semanticChild, ViewUtil.APPEND);
			if (viewChild != null) {
				viewDescriptors.add(new EObjectAdapter(viewChild)); //$NON-NLS-1$
			}
		}
		//
		// delete all the remaining views
		deleteViews(orphaned.iterator());

		makeViewsMutable(viewDescriptors);

		// now refresh all the connector containers to update the editparts
		HashSet ends = new HashSet();
		ListIterator li = viewDescriptors.listIterator();
		while (li.hasNext()) {
			IAdaptable adaptable = (IAdaptable) li.next();
			Edge edge = (Edge) adaptable.getAdapter(Edge.class);
			EditPart sourceEP = getEditPartFor(edge.getSource());
			if (sourceEP != null) {
				ends.add(sourceEP);
			}
			EditPart targetEP = getEditPartFor(edge.getTarget());
			if (targetEP != null) {
				ends.add(targetEP);
			}
		}

		for (Iterator iter = ends.iterator(); iter.hasNext();) {
			EditPart end = (EditPart) iter.next();
			end.refresh();
		}

		return viewDescriptors;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy#refreshSemantic()
	 */
	protected void refreshSemantic() {
		List createdViews = super.refreshSemanticChildren();
		List createdConnectorViews = refreshSemanticConnections();

		if (createdViews.size() > 1) {
			// perform a layout of the container
			DeferredLayoutCommand layoutCmd = new DeferredLayoutCommand(
				createdViews, host());
			executeCommand(new EtoolsProxyCommand(layoutCmd));
		}

		List allViews = new ArrayList(createdConnectorViews.size()
			+ createdViews.size());
		allViews.addAll(createdViews);
		allViews.addAll(createdConnectorViews);
		makeViewsImmutable(allViews);
	}

	/**
	 * Return <tt>true</tt> if this editpolicy should try and delete the
	 * supplied view; otherwise <tt>false<tt>.  
	 * The default behaviour is to return <tt>true</tt> if the view's semantic element is <tt>null</tt>.
	 * <P>
	 * Subclasses should override this method to ensure the correct behaviour.
	 * @return  <code>view.resolveSemanticElement() == null</code>
	 */
	protected boolean shouldDeleteView(View view) {
		return ViewUtil.resolveSemanticElement(view) == null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy#postProcessRefreshSemantic(java.util.List)
	 */
	protected void postProcessRefreshSemantic(List viewDescriptors) {
		makeViewsMutable(viewDescriptors);

		super.postProcessRefreshSemantic(viewDescriptors);
	}

	/**
	 * Return the list of connectors between elements contained within the host
	 * compartment. Subclasses should override
	 * {@link #shouldIncludeConnection(Edge, List)} to modify the returned
	 * collection's contents.
	 * 
	 * @return list of <code>IConnectorView</code>s.
	 */
	protected Collection getConnectionViews() {
		Collection retval = new HashSet();
		List children = getViewChildren();
		Iterator connectors = getDiagramConnections().iterator();
		while (connectors.hasNext()) {
			Edge connector = (Edge) connectors.next();
			if (connector.getSource() != null && connector.getTarget() != null
				&& shouldIncludeConnection(connector, children)) {
				retval.add(connector);
			}
		}
		return retval;
	}

	/**
	 * Called by {@link #getConnectionViews()} to determine if the underlying
	 * shape compartment is responsible for the supplied connector. By default,
	 * the following conditition must be met for the connector to be accepted:
	 * <UL>
	 * <LI> its source must not be null.
	 * <LI> its target must not be null.
	 * <LI> the shape compartment contains the source (or the source's container
	 * view).
	 * <LI> the shape compartment contains the target (or the target's container
	 * view). </LI>
	 * 
	 * @param connector
	 *            the connector view
	 * @param children
	 *            underlying shape compartment's children.
	 * @return <tt>false</tt> if supplied connector should be ignored;
	 *         otherwise <tt>true</tt>.
	 */
	protected boolean shouldIncludeConnection(Edge connector, List children) {
		View src = connector.getSource();
		View target = connector.getTarget();
		//
		// testing the src/tgt containerview in case the src/tgt are
		// some type of gate view.
		return ((src != null && target != null) && (children.contains(src)
			|| children.contains(src.eContainer()) || children.contains(target) || children
			.contains(target.eContainer())));
	}

	/**
	 * Return {@link UnexecutableCommand} if the editpolicy is enabled and a
	 * {@link DropObjectsRequest} is passed as an argument and its objects are
	 * contained in the list of semantic children.
	 */
	public Command getCommand(Request request) {
		if (understandsRequest(request)) {
			if (isEnabled() && request instanceof DropObjectsRequest) {
				return getDropCommand((DropObjectsRequest) request);
			}
		}
		return super.getCommand(request);
	}

	/**
	 * gets an <code>UnexecutableCommand</code> if the droprequest cannot be
	 * supported; the semantic host cannot contain the element being dropped or
	 * this editpolicy is enabled and it already contains of view for the
	 * elements being dropped.
	 * 
	 * @param request
	 *            the request to use
	 * @return <code>Command</code>
	 */
	protected Command getDropCommand(DropObjectsRequest request) {
		boolean enabled = isEnabled();
		List children = getSemanticChildrenList();
		Iterator dropElements = request.getObjects().iterator();
		while (dropElements.hasNext()) {
			Object dropElement = dropElements.next();
			// Allow diagram links on Canonical shapes compartments
			if (dropElement instanceof Diagram)
				continue;
			if (dropElement instanceof EObject
				&& preventDropElement(dropElement)) {
				return UnexecutableCommand.INSTANCE;
			}
			boolean containsElement = children.contains(dropElement);
			if (enabled) {
				if (containsElement || preventDropElement(dropElement)) {
					return UnexecutableCommand.INSTANCE;
				}
			}
		}
		return null;
	}

	/**
	 * Return <tt>false</tt> if the supplied element should be prevented from
	 * being dropped into this editpolicy's host; otherwise <tt>true</tt>.
	 * This method is called by {@link #getDropCommand(DropObjectsRequest)} if
	 * this editpolicy is enabled.
	 * 
	 * @param dropElement
	 *            object being dropped.
	 * @return <code>EObjectUtil.canContain(getSemanticHost(), ((EObject)dropElement).eClass(), false)</code>
	 *         if the supplied elemnt is an <code>EObject</code>; otherwise
	 *         <tt>false</tt>
	 */
	protected boolean preventDropElement(Object dropElement) {
		return dropElement instanceof EObject ? !EObjectUtil.canContain(
			getSemanticHost(), ((EObject) dropElement).eClass(), false)
			: false;
	}

	/**
	 * Understands the following:
	 * <UL>
	 * <LI>{@link DropObjectsRequest}
	 * <LI>{@link RequestConstants#REQ_DROP_OBJECTS}
	 * <LI>{@link org.eclipse.gef.RequestConstants#REQ_CREATE}
	 * </UL>
	 */
	public boolean understandsRequest(Request req) {
		return (RequestConstants.REQ_DROP_OBJECTS.equals(req.getType())
			|| req instanceof DropObjectsRequest || RequestConstants.REQ_CREATE
			.equals(req.getType())) ? true
			: super.understandsRequest(req);
	}

}
