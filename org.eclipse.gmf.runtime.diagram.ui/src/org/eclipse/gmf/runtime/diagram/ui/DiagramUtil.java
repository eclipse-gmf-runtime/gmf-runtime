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

package org.eclipse.gmf.runtime.diagram.ui;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.ui.services.editor.EditorService;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.diagram.core.util.ViewType;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditorInput;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNode;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutService;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.util.Assert;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Provides miscellaneous diagram utilities
 * 
 * @author melaasar, mmostafa
 */

public class DiagramUtil {

	/**
	 * Creates a diagram with the given context and kind
	 * 
	 * @param context
	 *            The diagram element context
	 * @param kind
	 *            diagram kind, check {@link ViewType} for predefined values
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 * @return A newly created <code>Diagram</code>
	 * @deprecated if there is a context EObject use @link ViewService#createDiagram(EObject, String, PreferencesHint)
	 *             if there is no context EObject use   @link ViewService#createDiagram(String, PreferencesHint)
  	 *				deprectaion date:  Dec 19 , 05
	 * 				removal date:	   Jan 31 , 06	     	
	 */
	public static Diagram createDiagram(EObject context, String kind,
			PreferencesHint preferencesHint) {
		IAdaptable viewModel = (context != null) ? new EObjectAdapter(context)
				: null;
		String viewType = (kind != null) ? kind : ""; //$NON-NLS-1$
		return ViewService.getInstance().createDiagram(viewModel, viewType,
				preferencesHint);
	}

	/**
	 * Creates a node for a given eObject and with a given type and inserts it
	 * into a given container
	 * 
	 * @param container
	 *            The node view container
	 * @param eObject
	 *            The node view object context
	 * @param type
	 *            The node view type, check {@link ViewType} for predefined
	 *            values
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 * @return A newly created <code>Node</code>
	 * @deprecated if there is a context EObject use @link ViewService#createNode(View, EObject, String, PreferencesHint)
	 *             if there is no context EObject use   @link ViewService#createNode(View, String, PreferencesHint)
	 *          	deprectaion date:  Dec 19 , 05
	 * 				removal date:	   Jan 31 , 06	     	
	 */
	public static Node createNode(View container, EObject eObject, String type,
			PreferencesHint preferencesHint) {
		Assert.isNotNull(container, "The container is null"); //$NON-NLS-1$
		IAdaptable viewModel = (eObject != null) ? new EObjectAdapter(eObject)
				: null;
		String viewType = (type != null) ? type : ""; //$NON-NLS-1$
		View view = ViewService.getInstance().createNode(viewModel, container,
				viewType, ViewUtil.APPEND, preferencesHint);
		return (view != null) ? (Node) view : null;
	}

	/**
	 * Creates an edge for a given eObject and with a given type in the given
	 * diagram
	 * 
	 * @param diagram
	 *            The container diagram
	 * @param eObject
	 *            The edge view object context
	 * @param type
	 *            The edge view type, check {@link ViewType} for predefined
	 *            values
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 * @return A newly created <code>Edge</code>
	 * @deprecated use {@link ViewService#createEdge(View, View, EObject, String, PreferencesHint)}
	 *           		deprectaion date:  Dec 19 , 05
	 * 					removal date:	   Jan 31 , 06	     	
	 */
	public static Edge createEdge(Diagram diagram, EObject eObject,
			String type, PreferencesHint preferencesHint) {
		Assert.isNotNull(diagram, "The diagram is null"); //$NON-NLS-1$
		IAdaptable viewModel = (eObject != null) ? new EObjectAdapter(eObject)
				: null;
		String viewType = (type != null) ? type : ""; //$NON-NLS-1$
		View view = ViewService.getInstance().createEdge(viewModel, diagram,
				viewType, ViewUtil.APPEND, preferencesHint);
		return (view != null) ? (Edge) view : null;
	}

	/**
	 * Creates an edge for a given eObject and with a given type and connects it
	 * between a given source and a given target
	 * 
	 * @param source
	 *            The edge's source view
	 * @param target
	 *            The edge's target view
	 * @param eObject
	 *            The edge view object context
	 * @param type
	 *            The edge view type, check {@link ViewType} for predefined
	 *            values
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 * @return A newly created <code>Edge</code>
	 * @deprecated if there is a context EObject use @link ViewService#createEdge(View, View, EObject, String, PreferencesHint)
	 *             if there is no context EObject use  @link ViewService#createEdge(View, View, String, PreferencesHint)
	 *           		deprectaion date:  Dec 19 , 05
	 * 					removal date:	   Jan 31 , 06	     	
	 */
	public static Edge createEdge(View source, View target, EObject eObject,
			String type, PreferencesHint preferencesHint) {
		return  ViewService.createEdge(source,target, eObject, type,
				preferencesHint);
	}

	/**
	 * Destroys a given view
	 * 
	 * @param view
	 *            The view to be destroyed
 	 *			deprectaion date:  Dec 19 , 05
	 * 			removal date:	   Jan 31 , 06	     	
	 */
	public static void destroyView(View view) {
		EObjectUtil.destroy(view);
	}

	/**
	 * Opens an editor for a given diagram
	 * 
	 * @param diagram
	 *            The diagram to be opened
	 */
	public static void openDiagramEditor(Diagram diagram) {
		EditorService.getInstance().openEditor(new DiagramEditorInput(diagram));
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
	 * @deprecated use
	 *             {@link org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutService#getLayoutNode(Node)}
	 *             Will be removed on December 16th / 2005
	 */
	public static ILayoutNode getLayoutNode(Node node) {
		return LayoutService.getInstance().getLayoutNode(node);
	}

	/**
	 * @param diagramEP
	 *            the diagram edit part to use
	 * @param nodes
	 *            List of <code>Node</code> objects
	 * @return List of <code>ILayoutNodes</code> objects
	 * @deprecated use
	 *             {@link org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutService#getLayoutNodes(DiagramEditPart, List)}
	 *             Will be removed on December 16th / 2005
	 */
	public static List getLayoutNodes(DiagramEditPart diagramEP, List nodes) {
		return LayoutService.getInstance().getLayoutNodes(diagramEP, nodes);
	}

	/**
	 * Utility method to layout the children of a view container.
	 * 
	 * @param view
	 *            View object that is the container whose children will be laid
	 *            out.
	 * @param hint
	 *            String representing a hint for what kind of layout will be
	 *            applied. Value can be one of
	 *            <code>org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodesProvider.DEFAULT_LAYOUT</code>
	 *            or
	 *            <code>org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodesProvider.RADIAL_LAYOUT</code>.
	 *            Other values would have to understand by custom providers.
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodesProvider
	 * @throws NullPointerException
	 *             <code>View</code> is <code>null</code>
	 * @throws NullPointerException
	 *             <code>hint</code> is <code>null</code>
	 * @deprecated use
	 *             {@link org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutService#layout(View, String)}
	 *             Will be removed on December 16th / 2005
	 */
	public static void layout(View view, String hint) {
		LayoutService.getInstance().layout(view, hint);
	}

	/**
	 * Utility method to layout a list of Node children on a diagram.
	 * 
	 * @param nodes
	 *            List of Node objects
	 * @param hint
	 *            String representing a hint for what kind of layout will be
	 *            applied. Value can be one of
	 *            <code>org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodesProvider.DEFAULT_LAYOUT</code> 
	 * <code>org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodesProvider.RADIAL_LAYOUT</code>.
	 *            Other values would have to understand by custom providers.
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodesProvider
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
	 * @deprecated use
	 *             {@link org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutService#layout(List, String)}
	 *             Will be removed on December 16th / 2005
	 */
	public static void layoutNodes(List nodes, String hint) {
		LayoutService.getInstance().layoutNodes(nodes, true, hint);
	}

	/**
	 * Rerturns an open editor for the given diagram in the given workbench
	 * window if the window is null, the active window in the platform is
	 * considered
	 * 
	 * @param diagram
	 *            The given diagram
	 * @param window
	 *            The given window (or null to mean the active one)
	 * @return An <code>IDiagramWorkbenchPart</code>
	 */
	public static IDiagramWorkbenchPart getOpenedDiagramEditor(Diagram diagram,
			IWorkbenchWindow window) {
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
