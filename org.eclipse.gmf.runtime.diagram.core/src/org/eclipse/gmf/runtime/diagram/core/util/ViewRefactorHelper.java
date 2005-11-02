/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.diagram.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.gmf.runtime.diagram.core.internal.services.view.ViewService;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.FilteringStyle;
import org.eclipse.gmf.runtime.notation.Guide;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.SortingStyle;
import org.eclipse.gmf.runtime.notation.Style;
import org.eclipse.gmf.runtime.notation.View;

/**
 * A helper class to perform notational refactoring based on a semantic refactoring.
 * The helper provides a typical or generic implementation of the refactoring operation
 * based on the Notation metamodel. However, if the notations being refactoring use other
 * extended notation metamodels, the implementation of this helper class might need to
 * be extended. This can be achieved by directly subclassing this helper class.
 * 
 * @author melaasar - Maged Elaasar
 */
public class ViewRefactorHelper {

	private PreferencesHint preferencesHint;
	
	/**
	 * Constructs a new <code>ViewRefactorHelper</code> with a given preferences hint 
	 * 
	 * @param preferencesHint The preferences hint to be used to perform refactoring
	 */
	public ViewRefactorHelper(PreferencesHint preferencesHint) {
		this.preferencesHint = preferencesHint;
	}
	
	/**
	 * Returns the preferences hint
	 * 
	 * @return The preferences hint
	 */
	public PreferencesHint getPreferencesHint() {
		return preferencesHint;
	}
	
	/**
	 * Refactors the notations associated with the old element to make them
	 * consistent with the new element.
	 * 
	 * @param oldElement The semantic element being refactored
	 * @param newElement The semantic element that replaces the refactored one
	 */
	public void refactor(EObject oldElement, EObject newElement) {

		// refactor views
		Collection views = getReferencingViews(oldElement); 
		for (Iterator i = views.iterator(); i.hasNext();) {
			View oldView = (View) i.next();
			if (oldView instanceof Node) {
				refactorNode((Node)oldView, newElement);
			} else if (oldView instanceof Edge) {
				refactorEdge((Edge)oldView, newElement);
			} else if (oldView instanceof Diagram) {
				refactorDiagram((Diagram)oldView, newElement);
			}
			EObjectUtil.destroy(oldView);
		}
		
		// refactor filtering styles
		Collection filterStyles = EObjectUtil.getReferencers(oldElement, new EReference[]{NotationPackage.eINSTANCE.getFilteringStyle_FilteredObjects()});
		for (Iterator i = filterStyles.iterator(); i.hasNext();) {
			List filteredObjects = ((FilteringStyle) i.next()).getFilteredObjects();
			filteredObjects.add(filteredObjects.indexOf(oldElement), newElement);
			filteredObjects.remove(oldElement);
		}
		
		// refactor sorting styles
		Collection sortingStyles = EObjectUtil.getReferencers(oldElement, new EReference[]{NotationPackage.eINSTANCE.getSortingStyle_SortedObjects()});
		for (Iterator i = sortingStyles.iterator(); i.hasNext();) {
			List sortingObjects = ((SortingStyle) i.next()).getSortedObjects();
			sortingObjects.add(sortingObjects.indexOf(oldElement), newElement);
			sortingObjects.remove(oldElement);
		}
	}
	
	/**
	 * Refactors an old node to a new one with the given new element
	 * 
	 * @param oldNode The old node being refactored
	 * @param newElement The replacing new element 
	 * @return A new refactored node
	 */
	protected Node refactorNode(Node oldNode, EObject newElement) {
		if (oldNode.eContainingFeature() == NotationPackage.eINSTANCE.getView_PersistedChildren()) {
			Node newNode = createNode(oldNode, newElement);

			if (newNode != null) {
				copyNodeFeatures(oldNode, newNode);
				View container = (View) oldNode.eContainer();
				container.getPersistedChildren().move(container.getPersistedChildren().indexOf(oldNode), newNode);
				refactorGuides(oldNode, newNode);
				return newNode;
			}
		}
		throw new RuntimeException("could not refactor a node for the morphed element"); //$NON-NLS-1$
	}

	/**
	 * Refactors an old edge to a new one with the given new element
	 * 
	 * @param oldEdge The old edge being refactored
	 * @param newElement The replacing new element 
	 * @return A new refactored edge
	 */
	protected Edge refactorEdge(Edge oldEdge, EObject newElement) {
		if (oldEdge.eContainingFeature() == NotationPackage.eINSTANCE.getDiagram_PersistedEdges()) {
			Edge newEdge = createEdge(oldEdge, newElement);
			
			if (newEdge != null) {
				copyEdgeFeatures(oldEdge, newEdge);
				Diagram container = (Diagram)oldEdge.eContainer();
				container.getPersistedEdges().move(container.getPersistedEdges().indexOf(oldEdge), newEdge);
				return newEdge;
			}
		}
		throw new RuntimeException("could not refactor an edge for the morphed element"); //$NON-NLS-1$
	}

	/**
	 * Refactors an old diagram to a new one with the given new element
	 * 
	 * @param oldDiagram The old diagram being refactored
	 * @param newElement The replacing new element 
	 * @return A new refactored diagram
	 */
	protected Diagram refactorDiagram(Diagram oldDiagram, EObject newElement) {
		if (oldDiagram.eContainingFeature() == EcorePackage.eINSTANCE.getEAnnotation_Contents()) {
			Diagram newDiagram = createDiagram(oldDiagram, newElement);
			
			if (newDiagram != null) {
				copyDiagramFeatures(oldDiagram, newDiagram);
				EAnnotation container = (EAnnotation) oldDiagram.eContainer(); 
				container.getContents().add(container.getContents().indexOf(oldDiagram), newDiagram);
				return newDiagram;
			}
		}
		throw new RuntimeException("could not refactor a diagram for the morphed element"); //$NON-NLS-1$
	}

	/**
	 * Copies the notational features of the old node to the new node
	 * 
	 * @param oldNode The old node to copy features from
	 * @param newNode The new node to copy features to
	 */
	protected void copyNodeFeatures(Node oldNode, Node newNode) {
		newNode.setLayoutConstraint(oldNode.getLayoutConstraint());
		copyViewFeatures(oldNode, newNode);
	}

	/**
	 * Copies the notational features of the old edge to the new edge
	 * 
	 * @param oldEdge The old edge to copy features from
	 * @param newEdge The new edge to copy features to
	 */
	protected void copyEdgeFeatures(Edge oldEdge, Edge newEdge) {
		newEdge.setBendpoints(oldEdge.getBendpoints());
		newEdge.setSourceAnchor(oldEdge.getSourceAnchor());
		newEdge.setTargetAnchor(oldEdge.getTargetAnchor());
		copyViewFeatures(oldEdge, newEdge);
	}

	/**
	 * Copies the notational features of the old diagram to the new diagram
	 * 
	 * @param oldDiagram The old diagram to copy features from
	 * @param newDiagram The new diagram to copy features to
	 */
	protected void copyDiagramFeatures(Diagram oldDiagram, Diagram newDiagram) {
		newDiagram.setName(oldDiagram.getName());
		newDiagram.getPersistedEdges().addAll(oldDiagram.getPersistedEdges());
		copyViewFeatures(oldDiagram, newDiagram);
	}

	/**
	 * Copies the notational features of the old view to the new view
	 * 
	 * @param oldView The old view to copy features from
	 * @param newView The new view to copy features to
	 */
	protected void copyViewFeatures(View oldView, View newView) {
		newView.setVisible(oldView.isVisible());
		copyViewStyles(oldView, newView);
		newView.getSourceEdges().addAll(oldView.getSourceEdges());
		newView.getTargetEdges().addAll(oldView.getTargetEdges());
		copyViewChildren(oldView, newView);
	}

	/**
	 * Copies the style features of the old view to the new view
	 * 
	 * @param oldView The old view to copy style features from
	 * @param newView The new view to copy style features to
	 */
	protected void copyViewStyles(View oldView, View newView) {
		for (Iterator i = oldView.getStyles().iterator(); i.hasNext();) {
			Style oldStyle = (Style) i.next();

			// since the same structural feature may appear in styles with different eClass(s)
			// we really need to get the new style that has the feature; which could be of different 
			// eClass than the source style
			
			Map eClassMap = new HashMap();
			for (Iterator j = oldStyle.eClass().getEAllStructuralFeatures().iterator(); j.hasNext();) {
				EStructuralFeature feature = (EStructuralFeature) j.next();
				Style newStyle;
				if (eClassMap.containsKey(feature.getEContainingClass())) {
					newStyle = (Style) eClassMap.get(feature.getEContainingClass());
				} else {
					eClassMap.put(feature.getEContainingClass(), newStyle = newView.getStyle(feature.getEContainingClass()));
				}
				if (newStyle != null) {
					newStyle.eSet(feature, oldStyle.eGet(feature));
				}
			}
		}
	}
	
	/**
	 * Copies the notational properties of the old view children to the new view children
	 * 
	 * @param oldView The old view to copy children notational features from
	 * @param newView The new view to copy children notational features to
	 */
	protected void copyViewChildren(View oldView, View newView) {
		for (Iterator j = new ArrayList(oldView.getPersistedChildren()).iterator(); j.hasNext();) {
			Node oldChildNode = (Node) j.next();
			copyViewChild(oldView, newView, oldChildNode);
		}
	}
	
	/**
	 * Copies the notational properties of the old child node to a corresponding one on under the new view
	 * 
	 * @param oldView The old view to copy children notational features from
	 * @param newView The new view to copy children notational features to
	 * @param oldChildNode A child node of the old view
	 */
	protected void copyViewChild(View oldView, View newView, Node oldChildNode) {
		if (oldView.getElement() == oldChildNode.getElement() && oldChildNode.getType() != null) {
			Node newChildNode = (Node) ViewUtil.getChildBySemanticHint(newView, oldChildNode.getType());
			if (newChildNode != null) {
				copyNodeFeatures(oldChildNode, newChildNode);
			}
		} else
			newView.getPersistedChildren().add(oldChildNode);
	}

	/**
	 * Refactors the diagram guides to reference the new node instead of the old one
	 * 
	 * @param oldNode The old node being refactored
	 * @param newNode The replacing new node
	 */
	protected final void refactorGuides(Node oldNode, Node newNode) {
		Collection guides = EObjectUtil.getReferencers(oldNode, new EReference[]{NotationPackage.eINSTANCE.getNodeEntry_Key()});
		for (Iterator i = guides.iterator(); i.hasNext();) {
			EMap nodeMap =  ((Guide) ((EObject) i.next()).eContainer()).getNodeMap();
			nodeMap.put(newNode, nodeMap.get(oldNode));
			nodeMap.remove(oldNode);
		}
	}

	/**
	 * A utility to get all the views of the given element to be refactored. The implementation
	 * of method delegated to a reverse look up map to get those views. Override if you have
	 * a more efficient way of getting those view or to cover more or less views.
	 * 
	 * @param element The element referenced by views to be refactored
	 * @return A collection of views that reference the given element to refactor
	 */
	protected Collection getReferencingViews(EObject element) {
		return EObjectUtil.getReferencers(element, new EReference[]{NotationPackage.eINSTANCE.getView_Element()});
	}

	/**
	 * A utility to create a new node for the given new element that would replace the given old node.
	 * The method uses default parameters to create the new node for the element. Override and change 
	 * this method if you think this is not the proper way to create a node of this new element.
	 * 
	 * @param oldNode The old node being refactored
	 * @param newElement The new element to create a node on
	 * @return A new node that references the given new element
	 */
	protected Node createNode(Node oldNode, EObject newElement) {
		return createNode(ViewUtil.getContainerView(oldNode), newElement, getNewViewType(oldNode, newElement));	
	}
	
	/**
	 * A utility to create a new edge for the given new element that would replace the given old edge.
	 * The method uses default parameters to create the new edge for the element. Override and change 
	 * this method if you think this is not the proper way to create a edge of this new element.
	 * 
	 * @param oldEdge The old edge being refactored
	 * @param newElement The new element to create a edge on
	 * @return A new edge that references the given new element
	 */
	protected Edge createEdge(Edge oldEdge, EObject newElement) {
		return createEdge(oldEdge.getSource(), oldEdge.getTarget(), newElement, getNewViewType(oldEdge, newElement));	
	}

	/**
	 * A utility to create a new diagram for the given new element that would replace the given old diagram.
	 * The method uses default parameters to create the new diagram for the element. Override and change 
	 * this method if you think this is not the proper way to create a diagram of this new element.
	 * 
	 * @param oldDiagram The old diagram being refactored
	 * @param newElement The new element to create a diagram on
	 * @return A new diagram that references the given new element
	 */
	protected Diagram createDiagram(Diagram oldDiagram, EObject newElement) {
		return createDiagram(newElement, getNewViewType(oldDiagram, newElement));	
	}
	
	/**
	 * Returns the type of the new view that replaces the old one 
	 * 
	 * @param oldView The old view being replaced
	 * @param newElement The new element of the new view
	 * @return The type of the new view
	 */
	protected String getNewViewType(View oldView, EObject newElement) {
		if (oldView instanceof Diagram)
			return ((Diagram)oldView).getType();
		return null;
	}
	
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
	private Diagram createDiagram(EObject context, String kind) {
		IAdaptable viewModel = (context != null) ? new EObjectAdapter(context) : null;
		String viewType = (kind != null) ? kind : ""; //$NON-NLS-1$
		return ViewService.getInstance().createDiagram(viewModel, viewType, preferencesHint);
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
	private Node createNode(View container, EObject eObject, String type) {
		IAdaptable viewModel = (eObject != null) ? new EObjectAdapter(eObject) : null;
		String viewType = (type != null) ? type : ""; //$NON-NLS-1$
		View view = ViewService.getInstance().createNode(viewModel, container, viewType, ViewUtil.APPEND, preferencesHint);
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
	private Edge createEdge(Diagram diagram, EObject eObject, String type) {
		IAdaptable viewModel = (eObject != null) ? new EObjectAdapter(eObject) : null;
		String viewType = (type != null) ? type : ""; //$NON-NLS-1$
		View view = ViewService.getInstance().createEdge(viewModel, diagram, viewType, ViewUtil.APPEND, preferencesHint);
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
	private Edge createEdge(View source, View target, EObject eObject, String type) {
		Edge edge = createEdge(source.getDiagram(), eObject, type);
		if (edge != null) {
			edge.setSource(source);
			edge.setTarget(target);
		}
		return edge;
	}
}
