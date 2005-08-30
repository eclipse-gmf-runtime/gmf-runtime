/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.jface.util.Assert;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import org.eclipse.gmf.runtime.common.core.util.ObjectAdapter;
import org.eclipse.gmf.runtime.common.ui.services.editor.EditorService;
import org.eclipse.gmf.runtime.diagram.core.internal.services.view.ViewService;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.layout.LayoutNode;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.layout.LayoutService;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditorInput;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNode;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;


/**
 * Provides miscellaneous diagram utilities
 * 
 * @author melaasar
 */

public class DiagramUtil {

	/**
	 * Creates a diagram with the given context and kind
	 * 
	 * @param context The diagram element context
	 * @param kind diagram kind
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 * @return A newly created <code>Diagram</code>
	 */
	public static Diagram createDiagram(EObject context, String kind, PreferencesHint preferencesHint) {
		IAdaptable viewModel = (context != null) ? new EObjectAdapter(context) : null;
		String viewType = (kind != null) ? kind : ""; //$NON-NLS-1$
		/*IDiagramView view = ViewService.getInstance().createDiagramView(viewModel, viewType);
		Diagram diagram = (view != null) ? (Diagram) view.getAdapter(Diagram.class) : null;
		return diagram;*/
		return ViewService.getInstance().createDiagramView(viewModel, viewType, preferencesHint);
	}

	/**
	 * Creates a node for a given eObject and with a given type and inserts it into a given container
	 * 
	 * @param container The node view container
	 * @param eObject The node view object context
	 * @param type The node view type
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 * @return A newly created <code>Node</code>
	 */
	public static Node createNode(View container, EObject eObject, String type, PreferencesHint preferencesHint) {
		Assert.isNotNull(container, "The container is null"); //$NON-NLS-1$
		IAdaptable viewModel = (eObject != null) ? new EObjectAdapter(eObject) : null;
		String viewType = (type != null) ? type : ""; //$NON-NLS-1$
		View view = ViewService.getInstance().createNodeView(viewModel, container, viewType, ViewUtil.APPEND, preferencesHint);
		return (view != null) ? (Node)view : null;
	}

	/**
	 * Creates an edge for a given eObject and with a given type in the given diagram
	 *
	 * @param diagram The container diagram 
	 * @param eObject The edge view object context
	 * @param type The edge view type
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 * @return A newly created <code>Edge</code>
	 */
	public static Edge createEdge(Diagram diagram, EObject eObject, String type, PreferencesHint preferencesHint) {
		Assert.isNotNull(diagram, "The diagram is null"); //$NON-NLS-1$
		IAdaptable viewModel = (eObject != null) ? new EObjectAdapter(eObject) : null;
		String viewType = (type != null) ? type : ""; //$NON-NLS-1$
		View view = ViewService.getInstance().createConnectorView(viewModel, diagram, viewType, ViewUtil.APPEND, preferencesHint);
		return (view != null) ? (Edge) view : null;
	}
	
	/**
	 * Creates an edge for a given eObject and with a given type and connects it between a given source and a given target
	 * 
	 * @param source The edge's source view
	 * @param target The edge's target view
	 * @param eObject The edge view object context
	 * @param type The edge view type
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 * @return A newly created <code>Edge</code>
	 */
	public static Edge createEdge(View source, View target, EObject eObject, String type, PreferencesHint preferencesHint) {
		Assert.isNotNull(source, "The source is null"); //$NON-NLS-1$
		Assert.isNotNull(target, "The target is null"); //$NON-NLS-1$
		Assert.isNotNull(source.getDiagram(), "The source is detached"); //$NON-NLS-1$
		Assert.isNotNull(target.getDiagram(), "The target is detached"); //$NON-NLS-1$
		Edge edge = createEdge(source.getDiagram(), eObject, type, preferencesHint);
		if (edge != null) {
			edge.setSource(source);
			edge.setTarget(target);
		}
		return edge;
	}

	/**
	 * Destroys a given view
	 * 
	 * @param view The view to be destroyed
	 */
	public static void destroyView(View view) {
		EObjectUtil.destroy(view);
	}
	
	/**
	 * Opens an editor for a given diagram
	 * @param diagram The diagram to be opened
	 */
	public static void openDiagramEditor(Diagram diagram) {
		EditorService.getInstance().openEditor(new DiagramEditorInput(diagram));
	}
	
	/**
	 * Gets the <code>ILayoutNode</code> in order to retrieve the actual size
	 * of the Node object irrespective of the autosize properties.
	 * 
	 * @param node Node to get the layout node equivalent from
	 * @return <code>ILayoutNode</code>object
	 * 
	 * @throws NullPointerException
	 *             <code>node</code> is <code>null</code>
	 */
	public static ILayoutNode getLayoutNode(Node node) {
		if (null == node) {
			throw new NullPointerException("Argument 'node' is null"); //$NON-NLS-1$
		}
		
		Diagram diagram = node.getDiagram(); 
		DiagramEditPart diagramEP = OffscreenEditPartFactory.getInstance().createDiagramEditPart(diagram);
		Map registry = diagramEP.getViewer().getEditPartRegistry();
		GraphicalEditPart gep = (GraphicalEditPart)registry.get(node);
		Dimension size = gep.getFigure().getBounds().getSize();
		
		return new LayoutNode(node, size.width, size.height);
	}
	
	private static void checkValidNodes(List nodes) {
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
				throw new IllegalArgumentException("Argument 'nodes' contains objects which aren't of type 'Node'"); //$NON-NLS-1$
			Node node = (Node)next;
			
			if (parent == null)
				parent =  ViewUtil.getContainerView(node);
			else
				if (ViewUtil.getContainerView(node) != parent)
					throw new IllegalArgumentException("Argument 'nodes' contains objects which have a different parent containment"); //$NON-NLS-1$
		} 
	}
	
	/**
	 * @param diagramEP	the diagram edit part to use
	 * @param nodes List of <code>Node</code> objects 
	 * @return List of <code>ILayoutNodes</code> objects
	 */
	public static List getLayoutNodes(DiagramEditPart diagramEP, List nodes) {
		checkValidNodes(nodes);
		
		if (diagramEP == null){
			Diagram diagram = ((Node)nodes.get(0)).getDiagram(); 
			diagramEP = OffscreenEditPartFactory.getInstance().createDiagramEditPart(diagram);
		}
		Map registry = diagramEP.getViewer().getEditPartRegistry();
		
		List layoutNodes = new ArrayList(nodes.size());
		ListIterator li = nodes.listIterator();
		while (li.hasNext()) {
			Node node = (Node)li.next();
			GraphicalEditPart gep = (GraphicalEditPart)registry.get(node);
			Dimension size = gep.getFigure().getBounds().getSize();
			
			layoutNodes.add(new LayoutNode(node,size.width, size.height));
		}
		
		return layoutNodes;
	}
	
	/**
	 * Utility method to layout the children of a view container.
	 * 
	 * @param view View object that is the container whose children will be laid out.
	 * @param hint String representing a hint for what kind of layout will be applied.  Value can be one of
	 * <code>org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodesProvider.DEFAULT_LAYOUT</code> or 
	 * <code>org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodesProvider.RADIAL_LAYOUT</code>.  
	 * Other values would have to understand by custom providers.  
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodesProvider
	 * @throws NullPointerException <code>View</code> is <code>null</code>
	 * @throws NullPointerException <code>hint</code> is <code>null</code>
	 */
	public static void layout(View view, String hint) {
		if (null == view) {
			throw new NullPointerException("Argument 'view' is null"); //$NON-NLS-1$
		}
		if (null == hint) {
			throw new NullPointerException("Argument 'hint' is null"); //$NON-NLS-1$
		}
				
		Diagram diagram = view.getDiagram();
		DiagramEditPart diagramEP = OffscreenEditPartFactory.getInstance().createDiagramEditPart(diagram);
		
		List hints = new ArrayList(2);
		hints.add(hint);
		hints.add(diagramEP);
		IAdaptable layoutHint = new ObjectAdapter(hints);
		final Runnable layoutRun =  LayoutService.getInstance().layoutNodes(getLayoutNodes(diagramEP,view.getChildren()), false, layoutHint);
		layoutRun.run();
	}
	
	/**
	 * Utility method to layout a list of Node children on a diagram.
	 * 
	 * @param nodes List of Node objects
	 * @param hint String representing a hint for what kind of layout will be applied.  Value can be one of
	 * <code>org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodesProvider.DEFAULT_LAYOUT</code> 
	 * <code>org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodesProvider.RADIAL_LAYOUT</code>.  
     * Other values would have to understand by custom providers.
	 * @see   org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodesProvider
	 * @throws NullPointerException  <code>nodes</code> is <code>null</code>
	 * @throws IllegalArgumentException nodes is an empty list
	 * @throws NullPointerException  <code>hint</code> is <code>null</code>
	 * @throws IllegalArgumentException Argument <code>nodes</code> contains objects which aren't of type <code>Node</code>
	 * @throws IllegalArgumentException Argument <code>nodes</code> contains objects which have a different parent containment
	 */
	public static void layoutNodes(List nodes, String hint) {
		checkValidNodes(nodes);
		
		if (null == hint) {
			throw new NullPointerException("Argument 'hint' is null"); //$NON-NLS-1$
		} 
		
		Node nodeFirst = (Node)nodes.get(0);
		Diagram diagram = nodeFirst.getDiagram();
		DiagramEditPart diagramEP = OffscreenEditPartFactory.getInstance().createDiagramEditPart(diagram);
		
		List hints = new ArrayList(2);
		hints.add(hint);
		hints.add(diagramEP);
		IAdaptable layoutHint = new ObjectAdapter(hints);
		final Runnable layoutRun =  LayoutService.getInstance().layoutNodes(getLayoutNodes(diagramEP,nodes), true, layoutHint);
		layoutRun.run();
	}


	
	/**
	 * Rerturns an open editor for the given diagram in the given workbench window
	 * if the window is null, the active window in the platform is considered
	 * 
	 * @param diagram The given diagram
	 * @param window The given window (or null to mean the active one)
	 * @return An <code>IDiagramWorkbenchPart</code>
	 */
	public static IDiagramWorkbenchPart getOpenedDiagramEditor(Diagram diagram, IWorkbenchWindow window) {
		if (null == diagram)
			throw new NullPointerException("Argument 'diagram' is null"); //$NON-NLS-1$
		if (window == null)
			window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		
		List editors = EditorService.getInstance().getRegisteredEditorParts();
		for (Iterator j = editors.iterator(); j.hasNext();) {
			IEditorPart editor = (IEditorPart) j.next();
			if (editor.getEditorSite().getWorkbenchWindow() == window) {
				IDiagramWorkbenchPart de = (IDiagramWorkbenchPart) editor;
				if (de.getDiagram() == diagram)
					return de;
			}
		}

		return null;
	}	

}
