/******************************************************************************
 * Copyright (c) 2002, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.services.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.common.core.util.ObjectAdapter;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.OffscreenEditPartFactory;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.layout.CanLayoutNodesOperation;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.layout.LayoutNode;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.layout.LayoutNodesOperation;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.widgets.Shell;

/**
 * A service that provides for diagram layout.
 * 
 * @author schafe / sshaw
 */
final public class LayoutService extends Service implements
		ILayoutNodeProvider {

	private final static LayoutService instance = new LayoutService();

	private LayoutService() {
		super(); // no caching for now
		configureProviders(DiagramUIPlugin.getPluginId(), "layoutProviders"); //$NON-NLS-1$
	}

	public static LayoutService getInstance() {
		return instance;
	}

	/**
	 * Executes the specified layout operation using the <code>ExecutionStrategy.FIRST</code> 
	 * execution strategy.
	 * 
	 * @param operation
	 * @return Object
	 */
	private Object execute(LayoutNodesOperation operation) {
		List results = execute(ExecutionStrategy.FIRST, operation);
		return results.isEmpty() ? null : results.get(0);
	}

	/**
	 * Gets the <code>ILayoutNode</code> in order to retrieve the actual size
	 * of the Node object irrespective of the autosize properties.
	 * 
	 * @param node
	 *            Node to get the layout node equivalent from
	 * @return <code>ILayoutNode</code>object
	 * 
	 * @throws NullPointerException
	 *             <code>node</code> is <code>null</code>
	 */
	public ILayoutNode getLayoutNode(Node node) {
		if (null == node) {
			throw new NullPointerException("Argument 'node' is null"); //$NON-NLS-1$
		}

        Shell shell = new Shell();
        try {
            Diagram diagram = node.getDiagram();
            DiagramEditPart diagramEP = OffscreenEditPartFactory.getInstance()
                .createDiagramEditPart(diagram, shell);
            Map registry = diagramEP.getViewer().getEditPartRegistry();
            GraphicalEditPart gep = (GraphicalEditPart) registry.get(node);
            Dimension size = gep.getFigure().getBounds().getSize();

            return new LayoutNode(node, size.width, size.height);
        } finally {
            shell.dispose();
        }
	}

	private void checkValidLayoutNodes(List nodes) {
		if (null == nodes) {
			throw new NullPointerException("Argument 'nodes' is null"); //$NON-NLS-1$
		}
		if (nodes.size() == 0) {
			throw new IllegalArgumentException("Argument 'nodes' is empty"); //$NON-NLS-1$
		}

		ListIterator li = nodes.listIterator();
		EObject parent = null;
		while (li.hasNext()) {
			Object next = li.next();
			if (!(next instanceof ILayoutNode))
				throw new IllegalArgumentException(
						"Argument 'nodes' contains objects which aren't of type 'ILayoutNode'"); //$NON-NLS-1$
			ILayoutNode node = (ILayoutNode) next;

			if (parent == null)
				parent = ViewUtil.getContainerView(node.getNode());
			else if (ViewUtil.getContainerView(node.getNode()) != parent)
				throw new IllegalArgumentException(
						"Argument 'nodes' contains objects which have a different parent containment"); //$NON-NLS-1$
		}
	}
	
	private void checkValidNodes(List nodes) {
		if (null == nodes) {
			throw new NullPointerException("Argument 'nodes' is null"); //$NON-NLS-1$
		}
		if (nodes.size() == 0) {
			throw new IllegalArgumentException("Argument 'nodes' is empty"); //$NON-NLS-1$
		}

		ListIterator li = nodes.listIterator();
		EObject parent = null;
		while (li.hasNext()) {
			Object next = li.next();
			if (!(next instanceof Node))
				throw new IllegalArgumentException(
						"Argument 'nodes' contains objects which aren't of type 'Node'"); //$NON-NLS-1$
			Node node = (Node) next;

			if (parent == null)
				parent = ViewUtil.getContainerView(node);
			else if (ViewUtil.getContainerView(node) != parent)
				throw new IllegalArgumentException(
						"Argument 'nodes' contains objects which have a different parent containment"); //$NON-NLS-1$
		}
	}

	/**
	 * @param diagramEP
	 *            the diagram edit part to use
	 * @param nodes
	 *            List of <code>Node</code> objects
	 * @return List of <code>ILayoutNodes</code> objects
	 */
	public List getLayoutNodes(DiagramEditPart diagramEP, List nodes) {
		checkValidNodes(nodes);

        Shell shell = null;
        try {
            if (diagramEP == null) {
                shell = new Shell();
                Diagram diagram = ((Node) nodes.get(0)).getDiagram();
                diagramEP = OffscreenEditPartFactory.getInstance()
                    .createDiagramEditPart(diagram, shell);
            }
            Map registry = diagramEP.getViewer().getEditPartRegistry();

            List layoutNodes = new ArrayList(nodes.size());
            ListIterator li = nodes.listIterator();
            while (li.hasNext()) {
                Node node = (Node) li.next();
                GraphicalEditPart gep = (GraphicalEditPart) registry.get(node);
                Dimension size = gep.getFigure().getBounds().getSize();

                layoutNodes.add(new LayoutNode(node, size.width, size.height));
            }

            return layoutNodes;
        } finally {
            if (shell != null) {
                shell.dispose();
            }
        }
	}

	/**
	 * Utility method to layout the children of a view container.
	 * 
	 * @param container
	 *            <code>View</code> object that is the container whose children will be laid
	 *            out.
	 * @param hint
	 *            <code>String</code> representing a hint for what kind of layout will be
	 *            applied. Value can be one of
	 *            <code>ILayoutType.DEFAULT</code>, <code>ILayoutType.RADIAL</code>.
	 *            Other values would have to understand by custom providers.
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodeProvider
	 * @throws NullPointerException
	 *             <code>View</code> is <code>null</code>
	 * @throws NullPointerException
	 *             <code>hint</code> is <code>null</code>
	 */
	public void layout(View container, String hint) {
		if (null == container) {
			throw new NullPointerException("Argument 'view' is null"); //$NON-NLS-1$
		}
		if (null == hint) {
			throw new NullPointerException("Argument 'hint' is null"); //$NON-NLS-1$
		}

        Shell shell = new Shell();
        try {
            Diagram diagram = container.getDiagram();
            DiagramEditPart diagramEP = OffscreenEditPartFactory.getInstance()
                .createDiagramEditPart(diagram, shell);

            List hints = new ArrayList(2);
            hints.add(hint);
            hints.add(diagramEP);
            IAdaptable layoutHint = new ObjectAdapter(hints);
            final Runnable layoutRun = LayoutService.getInstance()
                .layoutLayoutNodes(
                    getLayoutNodes(diagramEP, container.getChildren()), false,
                    layoutHint);
            layoutRun.run();
        } finally {
            shell.dispose();
        }
    }

	/**
	 * Utility method to layout a list of Node children on a diagram.
	 * 
	 * @param nodes
	 *            <code>List</code> of {@link org.eclipse.gmf.runtime.notation.Node} objects or
	 *            <code>List</code> of {@link org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNode} objects
	 * @param offsetFromBoundingBox
	 *            <code>boolean</code> indicating whether the Nodes should be
	 *            laid out relative to the bounding box of the Nodes in the
	 *            nodesToSizes Map.       
	 * @param hint
	 *            String representing a hint for what kind of layout will be
	 *            applied. Value can be one of
	 *            <code>ILayoutType.DEFAULT</code>, <code>ILayoutType.RADIAL</code>.
	 *            Other values can be provided but would have to be understand by custom providers.
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodeProvider
	 * @throws NullPointerException
	 *             <code>nodes</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             nodes is an empty list
	 * @throws NullPointerException
	 *             <code>hint</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             Argument <code>nodes</code> contains objects which aren't
	 *             of type <code>Node</code>
	 * @throws IllegalArgumentException
	 *             Argument <code>nodes</code> contains objects which have a
	 *             different parent containment
	 */
	public void layoutNodes(List nodes, boolean offsetFromBoundingBox, String hint) {
		checkValidNodes(nodes);

		if (null == hint) {
			throw new NullPointerException("Argument 'hint' is null"); //$NON-NLS-1$
		}

        Shell shell = new Shell();
        try {
            Node nodeFirst = (Node) nodes.get(0);
            Diagram diagram = nodeFirst.getDiagram();
            DiagramEditPart diagramEP = OffscreenEditPartFactory.getInstance()
                .createDiagramEditPart(diagram, shell);

            List hints = new ArrayList(2);
            hints.add(hint);
            hints.add(diagramEP);
            IAdaptable layoutHint = new ObjectAdapter(hints);
            final Runnable layoutRun = LayoutService.getInstance()
                .layoutLayoutNodes(getLayoutNodes(diagramEP, nodes), true,
                    layoutHint);
            layoutRun.run();
        } finally {
            shell.dispose();
        }
    }
	
	public Runnable layoutLayoutNodes(List layoutNodes,
			boolean offsetFromBoundingBox, IAdaptable layoutHint) {
		if (null == layoutHint) {
			throw new NullPointerException("Argument 'layoutHint' is null"); //$NON-NLS-1$
		}
		checkValidLayoutNodes(layoutNodes);
		
		Assert.isNotNull(layoutNodes);
		Assert.isNotNull(layoutHint);
		return (Runnable) execute(new LayoutNodesOperation(layoutNodes,
				offsetFromBoundingBox, layoutHint));
	}

	/**
	 * @since 1.3 (1.3.1)
	 */
	public boolean canLayoutNodes(List layoutNodes,
			boolean shouldOffsetFromBoundingBox, IAdaptable layoutHint) {
		return execute(new CanLayoutNodesOperation(layoutNodes, shouldOffsetFromBoundingBox, layoutHint)) == Boolean.TRUE;
	}
}
