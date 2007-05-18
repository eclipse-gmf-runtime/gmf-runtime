/******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

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
import org.eclipse.gmf.runtime.diagram.ui.commands.DeferredLayoutCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.SetViewMutabilityCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GroupEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.INodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.figures.ICanonicalShapeCompartmentLayout;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest.ConnectionViewDescriptor;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.core.util.PackageUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.osgi.util.NLS;

/**
 * A specialized implementation of <code>CanonicalEditPolicy</code>.
 * This implementation will manage connections owned by the semantic host.
 * 
 * @author mhanner / sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public abstract class CanonicalConnectionEditPolicy
	extends CanonicalEditPolicy {

	/**
	 * Return a list of semantic relationships contained inside this
	 * compartment.
	 * @return EObject list
	 * 
	 */
	abstract protected List getSemanticConnectionsList();

	/**
	 * Return the supplied relationship's source element
	 * @param relationship semantic connection
     * @return EObject the source EObject
	 */
	abstract protected EObject getSourceElement(EObject relationship);

	/**
	 * Return the supplied relationship's target element.
	 * 
	 * @param relationship semantic connection
     * @return EObject the target EObject
	 */
	abstract protected EObject getTargetElement(EObject relationship);

	/** Return an empty list. */
	protected List getSemanticChildrenList() {
		return Collections.EMPTY_LIST;
	}

	/* 
	 * Override to ensure that all owned editparts are activated before the editpolicy refresh
	 * is invoked that will try to canonically create connections and shapes.
	 */
	protected void refreshOnActivate() {
		// need to activate editpart children before invoking the canonical refresh
		List c = getHost().getChildren();
		for (int i = 0; i < c.size(); i++)
			((EditPart)c.get(i)).activate();

		refresh();
	}
	
	/**
	 * Return <tt>true</tt> if the connection should be drawn between the
	 * supplied endpoints; otherwise return <tt>false</tt>.
	 * 
	 * @param a
	 *            connection's source element
	 * @param a
	 *            connection's target element
	 * @return <tt>true</tt> if both parameters are not <tt>null</tt>;
	 *         otherwise <tt>false</tt>
	 */
	protected boolean canCreateConnection(EditPart sep, EditPart tep,
			EObject connection) {
		if (sep != null && sep.isActive() && tep != null && tep.isActive()) {

			View src = (View) sep.getAdapter(View.class);
			View tgt = (View) tep.getAdapter(View.class);
			if (src != null && tgt != null) {

                EditPart sourceParent = sep.getParent();
                while (sourceParent instanceof GroupEditPart) {
                    sourceParent = sourceParent.getParent();
                }
                EditPart targetParent = sep.getParent();
                while (targetParent instanceof GroupEditPart) {
                    targetParent = targetParent.getParent();
                }
				
                return sourceParent.getEditPolicy(
					EditPolicyRoles.CANONICAL_ROLE) != null
					&& targetParent.getEditPolicy(
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
	 *            an <tt>View</tt> or <tt>EObject</tt> instance.
	 * @param context
	 * 			  an <code>EObject</code> that is the context for the element.  Typically,
	 * 			  this will be either <code>null</code> or it will the <code>Edge</code>
	 * 			  that is connected to the <code>element</code> to find the <code>EditPart</code> of.
	 * @return an editpart; <tt>null</tt> if non could be found.
	 */
	private EditPart getEditPartFor(EObject element, EObject context) {
		if (element != null && !(element instanceof View)) {
			EditPartViewer viewer = getHost().getViewer();
			if (viewer instanceof IDiagramGraphicalViewer) {
				List parts = ((IDiagramGraphicalViewer) viewer)
					.findEditPartsForElement(EMFCoreUtil.getProxyID(element),
						INodeEditPart.class);

				if (parts.isEmpty()) {
					// reach for the container's editpart instead and force it
					// to refresh
					EObject container = element.eContainer();
					EditPart containerEP = getEditPartFor(container, null);
					if (containerEP != null) {
						containerEP.refresh();
						parts = ((IDiagramGraphicalViewer) viewer)
							.findEditPartsForElement(EMFCoreUtil.getProxyID(element),
								INodeEditPart.class);
					}
				}

				// Check if the part is contained with-in the host EditPart
				// since we are canonically updated the host.
				return findEditPartForElement(element, context, parts);
			}
		}

		return (EditPart) host().getViewer().getEditPartRegistry().get(element);
	}

	/**
	 * Finds the specific <code>EditPart</code> from a <code>List</code> of editparts
	 * that is the exact representation of the given <code>element</code> in the 
	 * host context.
	 * 
	 * @param element
	 *            an <tt>View</tt> or <tt>EObject</tt> instance.
	 * @param context
	 * 			  an <code>EObject</code> that is the context for the element.  Typically,
	 * 			  this will be either <code>null</code> or it will the <code>Edge</code>
	 * 			  that is connected to the <code>element</code> to find the <code>EditPart</code> of.
	 * @param parts
	 * 			  a <code>List</code> of <code>EditPart</codes> to search for a specific
	 * 			  instance that is the exact representation of <code>element</code>
	 * 			  in the host context.
	 * @return an editpart; <tt>null</tt> if non could be found.
	 */
	protected EditPart findEditPartForElement(EObject element, EObject context, List parts) {
		EditPart ancestor = getHost();
		while (ancestor != null) {
			EditPart ep = reachForEditPartWithAncestor(parts, ancestor);
			if (ep != null) {
				return ep;
			}
			ancestor = ancestor.getParent();
		}
		
		return null;
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
	 * Creates a connection view facde element for the supplied semantic element.
	 * An empty string is used as the default factory hint.
	 * 
	 * @param element
	 *            the semantic element
	 * @param the
	 *            connections source editpart
	 * @param the
	 *            connections target editpart
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
		CreateConnectionViewRequest ccr = getCreateConnectionViewRequest(
			elementAdapter, getFactoryHint(elementAdapter, factoryHint), index);

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
				String eMsg = NLS
					.bind(
						DiagramUIMessages.CanonicalEditPolicy_create_view_failed_ERROR_,
						connection);
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
	 * @param connection
	 *            the <code>EObject</code> element that we are canonical
	 *            trying to create a view for.
	 * @return the <code>EditPart</code> that is the source of the
	 *         <code>View</code> we want to create
	 */
	protected EditPart getTargetEditPartFor(EObject connection) {
		EObject tel;
		EditPart tep;
		tel = getTargetElement(connection);
		tep = getEditPartFor(tel, connection);
		return tep;
	}

	/**
	 * Calculates the <code>EditPart</code> that this connection element is
	 * connected to at it's source.
	 * 
	 * @param connection
	 *            the <code>EObject</code> element that we are canonical
	 *            trying to create a view for.
	 * @return the <code>EditPart</code> that is the target of the
	 *         <code>View</code> we want to create
	 */
	protected EditPart getSourceEditPartFor(EObject connection) {
		EObject sel;
		EditPart sep;
		sel = getSourceElement(connection);
		sep = getEditPartFor(sel, connection);
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
		if (request instanceof CreateConnectionViewRequest) {
			CreateConnectionViewRequest ccr = (CreateConnectionViewRequest) request;
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
	 * Return a create connection view request.
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
	private CreateConnectionViewRequest getCreateConnectionViewRequest(
			IAdaptable elementAdapter, String hint, int index) {
		return new CreateConnectionViewRequest(getConnectionViewDescriptor(
			elementAdapter, hint, index));
	}

	/**
	 * Return a connection view descriptor.
	 * 
	 * @param elementAdapter
	 *            semantic element
	 * @param hint
	 *            factory hint
	 * @param index
	 *            index
	 * @return a create <i>non-persisted </i> connection view descriptor
	 */
	private ConnectionViewDescriptor getConnectionViewDescriptor(
			IAdaptable elementAdapter, String hint, int index) {
		return new ConnectionViewDescriptor(elementAdapter, hint, index, false,
			((IGraphicalEditPart) getHost()).getDiagramPreferencesHint());
	}

	/**
	 * Updates the set of connection views so that it is in sync with the
	 * semantic connections. This method is called in response to notification
	 * from the model.
	 * <P>
	 * The update is performed by comparing the exising connection views with the
	 * set of semantic connections returned from {@link #getSemanticConnections()}.
	 * Views whose semantic connection no longer exists or whose semantic
	 * connection ends are <tt>null</tt> are
	 * {@link org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy#deleteViews(Iterator) removed}.
	 * New semantic children have their View
	 * {@link  #createEdge(IElement, EditPart, EditPart, int, String)
	 * created}. Subclasses must override <code>getSemanticConnections()</code>.
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
		// current connection views
		Collection viewChildren = getConnectionViews();
		Collection semanticChildren = new HashSet();
		semanticChildren.addAll(getSemanticConnectionsList());

		List orphaned = cleanCanonicalSemanticChildren(viewChildren,
			semanticChildren);
		//
		// delete all the remaining views
		deleteViews(orphaned.iterator());

		//
		// create a view for each remaining semantic element.
		List viewDescriptors = new ArrayList();
		Iterator semanticChildrenIT = semanticChildren.iterator();
		while (semanticChildrenIT.hasNext()) {
			semanticChild = (EObject) semanticChildrenIT.next();
			viewChild = createConnectionView(semanticChild, ViewUtil.APPEND);
			if (viewChild != null) {
				viewDescriptors.add(new EObjectAdapter(viewChild)); 
			}
		}
		
		makeViewsMutable(viewDescriptors);

		// now refresh all the connection containers to update the editparts
		HashSet ends = new HashSet();
		ListIterator li = viewDescriptors.listIterator();
		while (li.hasNext()) {
			IAdaptable adaptable = (IAdaptable) li.next();
			Edge edge = (Edge) adaptable.getAdapter(Edge.class);
			EditPart sourceEP = getEditPartFor(edge.getSource(), edge);
			if (sourceEP != null) {
				ends.add(sourceEP);
			}
			EditPart targetEP = getEditPartFor(edge.getTarget(), edge);
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
    
    protected boolean isOrphaned(Collection semanticChildren, View view) {
        EObject element = view.getElement();
        if (semanticChildren.contains(element)) {
            if (view instanceof Edge) {
                Edge edge = (Edge) view;
                if (edge.getSource().getElement() != getSourceElement(element)
                    || edge.getTarget().getElement() != getTargetElement(element))
                    return true;
            }
        } else
            return true;
        return false;
    }
    

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy#refreshSemantic()
	 */
	protected void refreshSemantic() {
		List createdViews = super.refreshSemanticChildren();
		List createdConnectionViews = refreshSemanticConnections();

		if (createdViews.size() > 1) {
			// perform a layout of the container
			DeferredLayoutCommand layoutCmd = new DeferredLayoutCommand(host().getEditingDomain(),
				createdViews, host());
			executeCommand(new ICommandProxy(layoutCmd));
		}

		List allViews = new ArrayList(createdConnectionViews.size()
			+ createdViews.size());
		allViews.addAll(createdViews);
		allViews.addAll(createdConnectionViews);
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
	 * Return the list of connections between elements contained within the host
	 * compartment. 
	 * 
	 * @return list of <code>Edge</code>s.
	 */
	protected Collection getConnectionViews() {
		Collection children = getViewChildren();
		Set connections = new HashSet();
		if (getHost() instanceof IGraphicalEditPart) {
			IGraphicalEditPart gep = (IGraphicalEditPart)getHost();
			getConnectionViews(connections, gep.getNotationView(), children);
		}
		
		return connections;
	}
	
	/**
	 * Add all connections that are attached to the given node and any of it's
	 * children.
	 * 
	 * @param connections
	 * @param node
	 */
	private void getConnectionViews(Set connections, View view, Collection viewChildren ) {
		IGraphicalEditPart gep = (IGraphicalEditPart)getHost();
		View hostView = gep.getNotationView();
		if (hostView != view) {
			if (!shouldCheckForConnections(view, viewChildren))
				return;
		}
		
		Iterator sourceIter = view.getSourceEdges().listIterator();
		while (sourceIter.hasNext()) {
			Edge sourceEdge = (Edge)sourceIter.next();
			if (shouldIncludeConnection(sourceEdge, viewChildren))
				connections.add(sourceEdge);
		}
		
		Iterator targetIter = view.getTargetEdges().listIterator();
		while (targetIter.hasNext()) {
			Edge targetEdge = (Edge)targetIter.next();
			if (shouldIncludeConnection(targetEdge, viewChildren))
				connections.add(targetEdge);
		}
		
		List children = view.getChildren();
		Iterator iter = children.listIterator();
		while (iter.hasNext()) {
			View viewChild = (View)iter.next();
			if (viewChild instanceof Node) {
				getConnectionViews(connections, viewChild, viewChildren );
			}
		}
	}

	/**
	 * Determines if a given view should be checked to see if any attached connections should be considered
	 * by the canonical synchronization routine.  By default it will consider views that are 2 levels deep from the
	 * container in order to allow for connections that are attached to border items on children views in the 
	 * container.
	 * 
	 * @param view a <code>View</code> to check to see if attached connections should be considered.
	 * @param viewChildren a <code>Collection</code> of view children of the host notation view, that can be used
	 * as a context to determine if the given view's attached connections should be considered.
	 * @return a <code>boolean</code> <code>true</code> if connections on the view are used as part of the 
	 * canonical synchronization.  <code>false</code> if the view's attached connections are to be ignored.
	 */
	protected boolean shouldCheckForConnections(View view, Collection viewChildren) {
		return (view != null && 
			(viewChildren.contains(view) || viewChildren.contains(view.eContainer())));
	}
	
	/**
	 * Called by {@link #getConnectionViews()} to determine if the underlying
	 * shape compartment is responsible for the supplied connection. By default,
	 * the following conditition must be met for the connection to be accepted:
	 * <UL>
	 * <LI> its source must not be null.
	 * <LI> its target must not be null.
	 * <LI> the shape compartment contains the source (or the source's container
	 * view).
	 * <LI> the shape compartment contains the target (or the target's container
	 * view). </LI>
	 * 
	 * @param connection
	 *            the connection view
	 * @param children
	 *            underlying shape compartment's children.
	 * @return <tt>false</tt> if supplied connection should be ignored;
	 *         otherwise <tt>true</tt>.
	 */
	protected boolean shouldIncludeConnection(Edge connection, Collection children) {
		return shouldCheckForConnections(connection.getSource(), children) ||
				shouldCheckForConnections(connection.getTarget(), children);
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
			if (allowDropElement(dropElement))
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
	 * @return <code>PackageUtil.canContain(getSemanticHost().eClass(), ((EObject)dropElement).eClass(), false)</code>
	 *         if the supplied elemnt is an <code>EObject</code>; otherwise
	 *         <tt>false</tt>
	 */
	protected boolean preventDropElement(Object dropElement) {
		return dropElement instanceof EObject ? !PackageUtil.canContain(
			getSemanticHost().eClass(), ((EObject) dropElement).eClass(), false)
			: false;
	}

    /**
     * Return <tt>true</tt> if the supplied element should be able to be
     * dropped into this editpolicy's host; otherwise <tt>false</tt>. This
     * method is called by {@link #getDropCommand(DropObjectsRequest)} if this
     * editpolicy is enabled. Returning false will necessarily prevent the
     * element from being dropped; the <code>getDropCommand</code> method will
     * also invoke <code>preventDropElement</code>.
     * 
     * @param dropElement
     *            object being dropped.
     * @return true if dropping the supplied element is supported, false
     *         otherwise.
     */
    protected boolean allowDropElement(Object dropElement) {
        return dropElement instanceof Diagram;
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
