/******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.RangeModel;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gef.tools.DeselectAllTracker;
import org.eclipse.gmf.runtime.common.ui.util.DisplayUtils;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationUtil;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ContainerEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ContainerNodeEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.PopupBarEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ShapeCompartmentDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.figures.ShapeCompartmentFigure;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.ISurfaceEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.DiagramLinkDragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.tools.RubberbandDragTracker;
import org.eclipse.gmf.runtime.diagram.ui.layout.FreeFormLayoutEx;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * A generic (sub) shape container that holds instances of
 * <code>ShapeNodeEditPart</code>s and manages the display of
 * <code>ConnectionNodeEditPart</code>s anchored to these shape editpart
 * instances.
 * 
 * @author mhanner
 */
public abstract class ShapeCompartmentEditPart
	extends ResizableCompartmentEditPart
	implements ISurfaceEditPart, PropertyChangeListener {

	/** private connection refresh manager. */
	private ConnectionRefreshMgr _crMgr;

	private boolean _refreshQueued = false;

	private boolean isSupportingViewActions = false;
    
    // Listen to editparts being added to or removed from this compartment
    // editpart so that when a figure moves within the compartment we can call
    // refreshConnections(). See bugzilla#146581.
    private EditPartListener editpartListener = new EditPartListener.Stub() {

        private FigureListener childFigureListener = new FigureListener() {

            public void figureMoved(IFigure source) {
                refreshConnections();
            }
        };

        public void childAdded(EditPart child, int index) {
            ((GraphicalEditPart) child).getFigure().addFigureListener(
                childFigureListener);
        }

        public void removingChild(EditPart child, int index) {
            ((GraphicalEditPart) child).getFigure().removeFigureListener(
                childFigureListener);

        }

    };

	/**
	 * Class used to refresh the connections associated to the shape
	 * compartment's children. This implementation will hide all connections
	 * whose endpoints are not visible inside the shape compartment.
	 */
	public static class ConnectionRefreshMgr {
		/**
		 * Cycles through all the connections associated to the editparts
		 * contained within the passed shape compartment and sets their
		 * visibility.
		 * 
		 * @see ConnectionNodeEditPart#getSourceConnectionAnchor()
		 * @see ConnectionNodeEditPart#getTargetConnectionAnchor()
		 * @param scep
		 *            edit part to consider
		 */
		protected void refreshConnections(ShapeCompartmentEditPart scep) {
			Iterator connectionNodes = getConnectionNodes(scep).iterator();
			while (connectionNodes.hasNext()) {
				ConnectionNodeEditPart cep = (ConnectionNodeEditPart) connectionNodes
					.next();
				Connection connection = (Connection) cep.getFigure();
				View connectionView = cep.getNotationView();
				if (connectionView != null && !connectionView.isVisible()) {
					/*
					 * Compartment is not responsible for refreshing a
					 * connection, the view of which is not visible
					 */
					continue;
				}
				
				IGraphicalEditPart source = (IGraphicalEditPart) getSourceEditPart(cep);
				IGraphicalEditPart target = (IGraphicalEditPart) getTargetEditPart(cep);
				if (source == null || target == null) {
					connection.setVisible(false);
					continue;
				}
				
				if (!source.getFigure().isShowing() || !target.getFigure().isShowing()) {
					connection.setVisible(false);
					continue;
				}
				
				ShapeCompartmentEditPart sContainer = getOwningShapeCompartment(source);
				ShapeCompartmentEditPart tContainer = getOwningShapeCompartment(target);
				// only deal with items contained within a shape compartment
				if (sContainer == null && tContainer == null) {
					continue;
				}
				boolean sfVisible = source != null;
				boolean tfVisible = target != null;
                
                ConnectionAnchor sc = cep.getSourceConnectionAnchor();
                ConnectionAnchor tc = cep.getTargetConnectionAnchor();
                Point sRefPoint;
                Point tRefPoint;
                List bendpoints = (List) connection.getConnectionRouter()
                    .getConstraint(connection);
                if (bendpoints != null && bendpoints.size() >= 2) {
                    sRefPoint = ((Bendpoint) bendpoints.get(0)).getLocation()
                        .getCopy();
                    connection.translateToAbsolute(sRefPoint);
                    tRefPoint = ((Bendpoint) bendpoints
                        .get(bendpoints.size() - 1)).getLocation().getCopy();
                    connection.translateToAbsolute(tRefPoint);
                } else {
                    sRefPoint = tc.getReferencePoint();
                    tRefPoint = sc.getReferencePoint();
                }
                Point sLoc = sc.getLocation(sRefPoint);
                Point tLoc = tc.getLocation(tRefPoint);
               
				Diagram diagram = ((View) scep.getModel()).getDiagram();
				Map registry = scep.getViewer().getEditPartRegistry();
				IGraphicalEditPart dep = (IGraphicalEditPart) registry
					.get(diagram);
				IFigure stopFigure = dep == null ? null
					: dep.getContentPane();
                boolean noSource = false;
                boolean noTarget = false;

				//
				// if sContainer is null, then the source connection is a child
				// of the diagram and not
				// a shape compartment. It's visibility is, therefore, true.
				if (sContainer != null) {
					ShapeCompartmentFigure fig = sContainer
						.getShapeCompartmentFigure();
                    noSource  = !fig.isVisible();
					sfVisible = isFigureVisible(fig, sLoc, stopFigure);
					if (!sfVisible) {
						sfVisible = isBorderItem(sContainer, source);
					}
				}
				//
				// if tContainer is null, then the source connection is a child
				// of the diagram and not
				// a shape compartment. It's visibility is, therefore, true.
				if (tContainer != null) {
					ShapeCompartmentFigure fig = tContainer
						.getShapeCompartmentFigure();
                    noTarget = !fig.isVisible();
                    tfVisible = isFigureVisible(fig, tLoc, stopFigure);
					if (!tfVisible) {
						tfVisible = isBorderItem(tContainer, target);
					}
				}
				// set connection visibility true iff both anchor points are
                // visible
                if (noSource || noTarget){
                  if (noSource && cep.getTarget()!=null)
                      cep.getTarget().refresh();
                  if (noTarget && cep.getSource()!=null)
                        cep.getSource().refresh();
                }else{
                    connection.setVisible(sfVisible && tfVisible);
                    refreshConnectionEnds(cep);
                }
			}
		}
        
        private void refreshConnectionEnds(ConnectionEditPart cEP){
            EditPart srcEditPart = cEP.getSource();
            EditPart trgEditPart = cEP.getTarget();
            Object model = cEP.getModel();
            if (model instanceof Edge){
                Edge edge = (Edge)model;
                View source = edge.getSource();
                View target = edge.getTarget();
                if (srcEditPart==null){
                    refreshEditPart(cEP.getViewer(), source);
                }
                if (trgEditPart==null){
                    refreshEditPart(cEP.getViewer(), target);
                }
            }
        }

        private void refreshEditPart(EditPartViewer  viewer, View view) {
            EditPart ep = (EditPart)viewer.getEditPartRegistry().get(view);
            if (ep!=null){
                ep.refresh();
            }
        }

		/**
		 * Return the set of {@link ConnectionNodeEditPart}s contained in the
		 * supplied shape compartment.
		 * 
		 * @param scep
		 *            a shape compartment.
		 * @return a {@link Set} of {@link ConnectionNodeEditPart}.
		 */

		protected Set getConnectionNodes(ShapeCompartmentEditPart scep) {
			Set endPoints = new HashSet();
			Object modelObject = scep.getModel();
			if (scep.getViewer() == null || modelObject == null 
				|| !(modelObject instanceof View)) {
				return endPoints;
			}
			
			if (((View)modelObject).getDiagram()==null)
				return endPoints;

			Diagram diagram = ((View) modelObject).getDiagram();
			Map registry = scep.getViewer().getEditPartRegistry();
			List edges = diagram.getEdges();
			Iterator edgesIterator = edges.iterator();

			while (edgesIterator.hasNext()) {
				Edge edge = (Edge) edgesIterator.next();
				EditPart endPoint = (EditPart) registry.get(edge.getSource());
				if (isChildOf(scep, endPoint)) {
					Object cep = registry.get(edge);
					if (cep != null) {
						endPoints.add(cep);
					}
					continue;
				}
				endPoint = (EditPart) registry.get(edge.getTarget());
				if (isChildOf(scep, endPoint)) {
					Object cep = registry.get(edge);
					if (cep != null) {
						endPoints.add(cep);
					}
				}
			}
			return endPoints;
		}

		/**
		 * Return <tt>true</tt> if <tt>parent</tt> child's ancestor;
		 * otherwise <tt>false</tt>
		 * 
		 * @param parent
		 *            parent to consider
		 * @param child
		 *            child to consider
		 * @return <tt>true</tt> or <tt>false</tt>
		 */
		protected boolean isChildOf(EditPart parent, EditPart child) {
			EditPart walker = child;
			while (walker != null && walker != parent) {
				walker = walker.getParent();
			}
			return walker != null;
		}

		/**
		 * gets the supplied editparts containing shape compartment.
		 * 
		 * @param ep
		 *            edit part
		 * @return <code> ShapeCompartmentEditPart</code>
		 */
		protected ShapeCompartmentEditPart getOwningShapeCompartment(EditPart ep) {
			EditPart walker = ep;
			while (walker != null
				&& !(walker instanceof ShapeCompartmentEditPart)) {
				walker = walker.getParent();
			}
			return (ShapeCompartmentEditPart) walker;
		}

		/**
         * This method can be overridden to allow connections between border
         * items around the container of the compartment to be drawn to items
         * within the interior of the compartment. This method should not return
         * true for all border items, only for those may be outside of the area
         * within the shape compartment.
         * 
         * @param scep
         * @param itemEditPart
         * @return true if the itemEditPart is a border item around or outside
         *         the shape compartment editpart passed in; false otherwise.
         */
        protected boolean isBorderItem(ShapeCompartmentEditPart scep,
                IGraphicalEditPart itemEditPart) {
            return false;
        }

		/**
		 * Returns source edit part.
		 * 
		 * @param connectionEditPart
		 * @return EditPart
		 */
		protected EditPart getSourceEditPart(
				ConnectionEditPart connectionEditPart) {
			return connectionEditPart.getSource();
		}

		/**
		 * Returns target editPart
		 * 
		 * @param connectionEditPart
		 * @return EditPart
		 */
		protected EditPart getTargetEditPart(
				ConnectionEditPart connectionEditPart) {
			return connectionEditPart.getTarget();
		}

		/**
		 * gets the source connections of the passed edit part
		 * 
		 * @param editPart
		 *            edit part to consider
		 * @return source connections
		 */
		protected List getSourceConnections(IGraphicalEditPart editPart) {
			return editPart.getSourceConnections();
		}

		/**
		 * get the target connections of the passed edit part
		 * 
		 * @param editPart
		 *            edit part to consider
		 * @return target connection
		 */
		protected List getTargetConnections(IGraphicalEditPart editPart) {
			return editPart.getTargetConnections();
		}

		/**
		 * Walks up the hierarchy to make sure that supplied figure is visible
		 * inside its figure hierarchy. <BR>
		 * Same as calling <code>isFigureVisible(figure, loc, null);</code>
		 * 
		 * @param figure
		 *            The figure under test.
		 * @param loc
		 *            the child's location in absolute coordinates.
		 * @return boolean visibility of the figure by going up the chain.
		 */
		protected boolean isFigureVisible(final IFigure figure, final Point loc) {
			return isFigureVisible(figure, loc, null);
		}

		/**
		 * Walks up the hierarchy to make sure that the point <code>loc</code> is visible
		 * inside its figure hierarchy. <BR>
		 * 
		 * @param figure
		 *            The figure under test.
		 * @param loc
		 *            the child's location in absolute coordinates.
		 * @param stopFigure
		 *            root figure in the figure hierarchy being tested.
		 * @return boolean visibility of the figure by going up the chain.
		 */
		protected boolean isFigureVisible(final IFigure figure,
				final Point loc, final IFigure stopFigure) {
			if (!(figure.isShowing())) {
				return false;
			} else {
				Rectangle bounds = figure.getBounds().getCopy();
				figure.translateToAbsolute(bounds);
				if (!(bounds.contains(loc))) {
					return false;
				}
			}

			IFigure parent = figure.getParent();
			while (parent != null && parent != stopFigure) {
				return isFigureVisible(parent, loc, stopFigure);
			}
			return true;
		}
	}

	/**
	 * Constructor for ShapeCompartmentEditPart.
	 * 
	 * @param view
	 *            the view <code>controlled</code> by this editpart.
	 */
	public ShapeCompartmentEditPart(View view) {
		super(view);
	}

	/**
	 * Returns the connection refresh manager.
	 * 
	 * @return <code>ConnectionRefreshMgr</code>
	 */
	protected final ConnectionRefreshMgr getConnectionRefreshMgr() {
		if (_crMgr == null) {
			_crMgr = createConnectionRefreshMgr();
		}
		return _crMgr;
	}

	/**
	 * Factory method to create a refresh connection. This implementation
	 * returns a {@link ShapeCompartmentEditPart.ConnectionRefreshMgr} instance.
	 * 
	 * @return <code>ConnectionRefreshMgr</code>
	 */
	protected ConnectionRefreshMgr createConnectionRefreshMgr() {
		return new ConnectionRefreshMgr();
	}

	/**
	 * Returns the layout manager to be used by this shape compartment. This
	 * implemantion returns a {@link FreeformLayout} instance.
	 * 
	 * @return a layout manager.
	 */
	protected LayoutManager getLayoutManager() {
		return new FreeFormLayoutEx();
	}

	/**
	 * Creates a scrollpane (with auto scrollbars) in which the children are
	 * drawn. The factory hint property is used to set this compartments label.
	 */
	protected IFigure createFigure() {
		ShapeCompartmentFigure scf = new ShapeCompartmentFigure(getCompartmentName(), getMapMode());
		scf.getContentPane().setLayoutManager(getLayoutManager());
        scf.getContentPane().addLayoutListener(LayoutAnimator.getDefault());

		return scf;
	}

	/**
	 * Convenience method to retrieve the shape compartment figure. Same as
	 * calling <code>(ShapeCompartmentFigure)getCompartmentFigure()</code>.
	 * 
	 * @return <code>ShapeCompartmentFigure</code>
	 */
	public ShapeCompartmentFigure getShapeCompartmentFigure() {
		return (ShapeCompartmentFigure) getCompartmentFigure();
	}

	/** Return the container in which shape editparts are added. */
	public IFigure getContentPane() {
		return getShapeCompartmentFigure().getContentPane();
	}

	/**
	 * Adds the following editpolicies: <BR>
	 * <UL>
	 * <LI> {@link EditPolicyRoles#CREATION_ROLE} :: {@link CreationEditPolicy}
	 * <LI> {@link EditPolicy#LAYOUT_ROLE} :: {@link XYLayoutEditPolicy}
	 * <LI> {@link EditPolicy#CONTAINER_ROLE} :: {@link ContainerEditPolicy}
	 * <LI> {@link EditPolicyRoles#DRAG_DROP_ROLE} ::
	 * {@link DiagramLinkDragDropEditPolicy}
	 * <LI> {@link EditPolicy#GRAPHICAL_NODE_ROLE} ::
	 * {@link ContainerNodeEditPolicy}
	 * 
	 * <LI> {@link EditPolicyRoles#SNAP_FEEDBACK_ROLE} ::
	 * {@link SnapFeedbackPolicy}
	 * <LI> {@link EditPolicyRoles#DRAG_DROP_ROLE} ::
	 * {@link ShapeCompartmentDropEditPolicy}
	 * <LI> {@link EditPolicyRoles#POPUPBAR_ROLE} :: {@link PopupBarEditPolicy}
	 * </UL>
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.CREATION_ROLE,
			new CreationEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new XYLayoutEditPolicy());
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new ContainerEditPolicy());
		// TODO: this edit policy get overriden by code at the end of this
		// function
		// may be this breaks some use cases; it needs to be checked
		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE,
			new DiagramLinkDragDropEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
			new ContainerNodeEditPolicy());
		// Install an edit policy for snap
		installEditPolicy(EditPolicyRoles.SNAP_FEEDBACK_ROLE,
			new SnapFeedbackPolicy());
		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE,
			new ShapeCompartmentDropEditPolicy());
		installEditPolicy(EditPolicyRoles.POPUPBAR_ROLE,
			new PopupBarEditPolicy());
	}

	/**
	 * Handles property change callbacks. All unrecognized events are forwarded
	 * to the parent class.
	 * 
	 * @param event
	 *            a property change event.
	 */
	protected void handlePropertyChangeEvent(PropertyChangeEvent event) {
		String pName = event.getPropertyName();
		if (RangeModel.PROPERTY_EXTENT.equals(pName)
			|| RangeModel.PROPERTY_VALUE.equals(pName)) {
			refreshConnections();
		}
	}

	/**
	 * Refreshes the connections inside the shape compartment if the supplied
	 * event is for an element inserted or removed from the editpart.
	 * 
	 * @see #refreshConnections()
	 * @param event
	 *            a model server event.
	 */
	protected void handleNotificationEvent(Notification event) {
		Object feature = event.getFeature();
		if (NotationPackage.eINSTANCE.getSize_Width().equals(feature)
			|| NotationPackage.eINSTANCE.getSize_Height().equals(feature)) {
			refreshConnections();
		} else
			super.handleNotificationEvent(event);

		if (NotificationUtil.isElementAddedToSlot(event)
			|| NotificationUtil.isElementRemovedFromSlot(event)) {
			refreshConnections();
		}
	}

	/**
	 * Refresh the connections associated the the children of this shape
	 * compartment.
	 */
	protected void refreshConnections() {
		if (!_refreshQueued) {
			_refreshQueued = true;
			DisplayUtils.getDisplay().asyncExec(new Runnable() {
				public void run() {
					forceRefreshConnections();
				}
			});
		}
	}
    
    /**
     * Refresh the connections associated the the children of this shape
     * compartment.
     */
    protected void forceRefreshConnections() {
      try {
            //
            // test if active since the editpartg may have been
            // deleted
            // by the time this method is executed.
            if (ShapeCompartmentEditPart.this.isActive()) {
                getConnectionRefreshMgr().refreshConnections(
                    ShapeCompartmentEditPart.this);
            }
        } finally {
            ShapeCompartmentEditPart.this._refreshQueued = false;
        }
        
    }

	/** Unregisters this instance as a PropertyChangeListener on its figure. */
	protected void unregister() {
		super.unregister();
		getShapeCompartmentFigure().removePropertyChangeListener(this);
		EditPartViewer viewer = getViewer();
		if (viewer != null && viewer.getControl() instanceof FigureCanvas) {
			FigureCanvas figureCanvas = (FigureCanvas) viewer.getControl();
			figureCanvas.getViewport().getVerticalRangeModel()
				.removePropertyChangeListener(this);
			figureCanvas.getViewport().getHorizontalRangeModel()
				.removePropertyChangeListener(this);
		}
	}

	/** Registers this instance as a PropertyChangeListener on its figure. */
	protected void registerVisuals() {
		super.registerVisuals();
		getShapeCompartmentFigure().addPropertyChangeListener(this);

		EditPartViewer viewer = getViewer();
		if (viewer != null && viewer.getControl() instanceof FigureCanvas) {
			FigureCanvas figureCanvas = (FigureCanvas) viewer.getControl();
			figureCanvas.getViewport().getVerticalRangeModel()
				.addPropertyChangeListener(this);
			figureCanvas.getViewport().getHorizontalRangeModel()
				.addPropertyChangeListener(this);
		}
	}

	/**
	 * Determines if the shape compartment supports drag selection of it's
	 * children. Otherwise, it will default to the core behavior of selecting
	 * the compartment itself on click on the compartment background surface.
	 * 
	 * @return <code>boolean</code> <code>true</code> if shape compartment
	 *         supports drag selection of it's children, <code>false</code>
	 *         otherwise.
	 */
	protected boolean supportsDragSelection() {
		return true;
	}

	/**
	 * @see org.eclipse.gef.EditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker(Request req) {
		if (!supportsDragSelection())
			return super.getDragTracker(req);

		if (req instanceof SelectionRequest
			&& ((SelectionRequest) req).getLastButtonPressed() == 3)
			return new DeselectAllTracker(this) {

				protected boolean handleButtonDown(int button) {
					getCurrentViewer().select(ShapeCompartmentEditPart.this);
					return true;
				}
			};
		return new RubberbandDragTracker() {

			protected void handleFinished() {
				if (getViewer().getSelectedEditParts().isEmpty())
					getViewer().select(ShapeCompartmentEditPart.this);
			}
		};
	}

	/** Also calls {@link #refreshConnections()}. */
	protected void refreshVisibility() {
		super.refreshVisibility();
        View view  = getNotationView();
        if (view !=null && !view.isVisible())
            forceRefreshConnections();
        else
            refreshConnections();
	}

	/*
	 * (non-Javadoc)
	 */
	public boolean isSupportingViewActions() {
		return this.isSupportingViewActions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.editparts.ISurfaceEditPart#setIsSupportingViewActions(boolean)
	 */
	public void setIsSupportingViewActions(boolean supportsViewActions) {
		this.isSupportingViewActions = supportsViewActions;
	}

	/**
	 * Handles the passed property changed event only if the editpart's view is
	 * not deleted.
	 */
	public final void propertyChange(PropertyChangeEvent event) {
		if (isActive())
			handlePropertyChangeEvent(event);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.editparts.ISurfaceEditPart#getPrimaryEditParts()
	 */
	public List getPrimaryEditParts() {
		List connections = new ArrayList();

		Object diagramEditPart = getViewer().getEditPartRegistry().get(
			getDiagramView());

		List shapes = getChildren();
		Set connectableEditParts = new HashSet(shapes);
		Iterator iter = shapes.iterator();
		while (iter.hasNext()) {
			getBorderItemEditParts((EditPart) iter.next(), connectableEditParts);
		}

		if (diagramEditPart instanceof DiagramEditPart) {
			Iterator diagramConnections = ((DiagramEditPart) diagramEditPart)
				.getConnections().iterator();
			while (diagramConnections.hasNext()) {
				ConnectionEditPart connection = (ConnectionEditPart) diagramConnections
					.next();
				if (connectableEditParts.contains(connection.getSource())
					|| connectableEditParts.contains(connection.getTarget()))
					connections.add(connection);
			}
		}

		if (connections.size() > 0 || shapes.size() > 0) {
			List primaryEditParts = new ArrayList();
			primaryEditParts.addAll(shapes);
			primaryEditParts.addAll(connections);
			return primaryEditParts;
		}
		return Collections.EMPTY_LIST;
	}
	
	/**
	 * This method searches an edit part for a child that is a border item edit part
	 * @param parent part needed to search
	 * @param set to be modified of border item edit parts that are direct children of the parent
	 */
	private void getBorderItemEditParts(EditPart parent, Set retval) {
		
		Iterator iter = parent.getChildren().iterator();
		while(iter.hasNext()) {
			EditPart child = (EditPart)iter.next();
			if( child instanceof IBorderItemEditPart ) {
				retval.add(child);
				retval.addAll(child.getChildren());
			}
			getBorderItemEditParts(child, retval);
		}
	}
    
    public void addNotify() {
        addEditPartListener(editpartListener);
        super.addNotify();
    }

    public void removeNotify() {
        removeEditPartListener(editpartListener);
        super.removeNotify();
    }

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ResizableCompartmentEditPart#setCollapsed(boolean, boolean)
	 */
	protected void setCollapsed(boolean collapsed, boolean animate) {
		super.setCollapsed(collapsed, animate);
		refreshConnections();
	}

}
