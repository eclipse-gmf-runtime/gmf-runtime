/******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
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
	 */
	public ViewRefactorHelper() {
		this(PreferencesHint.USE_DEFAULTS);
	}

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
			DestroyElementCommand.destroy(oldView);
		}
		
		// refactor filtering styles
		Collection filterStyles = EMFCoreUtil.getReferencers(oldElement, new EReference[]{NotationPackage.eINSTANCE.getFilteringStyle_FilteredObjects()});
		for (Iterator i = filterStyles.iterator(); i.hasNext();) {
			List filteredObjects = ((FilteringStyle) i.next()).getFilteredObjects();
			if (!filteredObjects.contains(oldElement))
				filteredObjects.add(filteredObjects.indexOf(oldElement), newElement);
			filteredObjects.remove(oldElement);
		}
		
		// refactor sorting styles
		Collection sortingStyles = EMFCoreUtil.getReferencers(oldElement, new EReference[]{NotationPackage.eINSTANCE.getSortingStyle_SortedObjects()});
		for (Iterator i = sortingStyles.iterator(); i.hasNext();) {
			List sortingObjects = ((SortingStyle) i.next()).getSortedObjects();
			if (!sortingObjects.contains(oldElement))
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
		return null;
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
		return null;
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
				refactorDiagramLinks(oldDiagram, newDiagram);
				return newDiagram;
			}
		}
		return null;
	}

	protected void refactorDiagramLinks(Diagram oldDiagram, Diagram newDiagram) {
		Collection links = EMFCoreUtil.getReferencers(oldDiagram, new EReference[]{NotationPackage.eINSTANCE.getView_Element()});
		for (Iterator i = links.iterator(); i.hasNext();) {
			View view = (View) i.next();
			view.setElement(newDiagram);
		}
	}
	
	/**
	 * Copies the notational features of the old node to the new node
	 * 
	 * @param oldNode The old node to copy features from
	 * @param newNode The new node to copy features to
	 */
	protected void copyNodeFeatures(Node oldNode, Node newNode) {
		if (oldNode.eIsSet(NotationPackage.eINSTANCE.getNode_LayoutConstraint())) {
			newNode.setLayoutConstraint(oldNode.getLayoutConstraint());
		}
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
		if (oldDiagram.eIsSet(NotationPackage.eINSTANCE.getDiagram_PersistedEdges())) {
			newDiagram.getPersistedEdges().addAll(oldDiagram.getPersistedEdges());
		}
		copyViewFeatures(oldDiagram, newDiagram);
	}

	/**
	 * Copies the notational features of the old view to the new view
	 * 
	 * @param oldView The old view to copy features from
	 * @param newView The new view to copy features to
	 */
	protected void copyViewFeatures(View oldView, View newView) {
		copyViewAppearance(oldView, newView, new ArrayList());
		if (oldView.eIsSet(NotationPackage.eINSTANCE.getView_SourceEdges())) {
			newView.getSourceEdges().addAll(oldView.getSourceEdges());
		}
		if (oldView.eIsSet(NotationPackage.eINSTANCE.getView_TargetEdges())) {
			newView.getTargetEdges().addAll(oldView.getTargetEdges());
		}
		copyViewChildren(oldView, newView);
	}

	/**
	 * Copies the appearance of the old view to the new view.  Typically this means copying the visibility
	 * and the styles of the root and it's children.
	 * 
	 * @param oldView The old view to copy style features from
	 * @param newView The new view to copy style features to
	 * @param excludeStyles the <code>List</code> of <code>Style.eClass</code> types to exclude
	 * from the copy operation.
	 */
	public void copyViewAppearance(View oldView, View newView, final List excludeStyles) {
		newView.setVisible(oldView.isVisible());
		copyViewStyles(oldView, newView, excludeStyles);
		copyViewAppearanceProperties(oldView, newView, excludeStyles);
		
		for (Iterator j = new ArrayList(oldView.getPersistedChildren()).iterator(); j.hasNext();) {
			Node oldChildNode = (Node) j.next();
			if (oldView.getElement() == oldChildNode.getElement() && oldChildNode.getType() != null) {
				Node newChildNode = (Node) ViewUtil.getChildBySemanticHint(newView, oldChildNode.getType());
				if (newChildNode != null) {
					copyViewAppearance(oldChildNode, newChildNode, excludeStyles);
				}
			}
		}
	}
	
	private void copyViewAppearanceProperties(View oldView, View newView, final List excludeStyles) {
		for (EAttribute attribute : oldView.eClass().getEAllAttributes()) {
			EClass containingEClass = attribute.getEContainingClass();
			if (NotationPackage.Literals.STYLE.isSuperTypeOf(containingEClass) && !excludeStyles.contains(containingEClass)) {
				EObject newViewStyle = newView.getStyle(containingEClass);
				if (newViewStyle != null) {
					if (!oldView.eIsSet(attribute)) {
						if (newViewStyle.eIsSet(attribute)) {
							newViewStyle.eUnset(attribute);
						}
					} else {
						newViewStyle.eSet(attribute, oldView.eGet(attribute));
					}
				}
			}
		}
	}
	
	/**
	 * Copies the style features of the old view to the new view
	 * 
	 * @param oldView The old view to copy style features from
	 * @param newView The new view to copy style features to
	 */
	protected void copyViewStyles(View oldView, View newView) {
		copyViewStyles(oldView, newView, new ArrayList());
	}
	
	/**
	 * Copies all styles feature from the old view to the new view
	 * 
	 * @param oldView The old view to copy style features from
	 * @param newView The new view to copy style features to
	 * @param excludeStyles the <code>List</code> of <code>Style.eClass</code> types to exclude
	 * from the copy operation.
	 */
	protected void copyViewStyles(View oldView, View newView, List excludeStyles) {
		if (oldView.eIsSet(NotationPackage.eINSTANCE.getView_Styles())) {
			for (Style oldStyle : (List<Style>) oldView.getStyles()) {
				copyViewStyle(oldView, newView, oldStyle, excludeStyles);
			}
		}
	}
	
	/**
	 * Copies the given style features of the old view to the new view
	 * 
	 * @param oldView The old view to copy style features from
	 * @param newView The new view to copy style features to
	 * @param oldStyle The old style to copy
	 * @param excludeStyles the list of <code>Style.eClass</code> types to exclude
	 */
	protected void copyViewStyle(View oldView, View newView, Style oldStyle, List excludeStyles) {
		// since the same structural feature may appear in styles with different eClass(s)
		// we really need to get the new style that has the feature; which could be of different 
		// eClass than the source style
		
		Map<EClass, Style> eClassMap = new HashMap<EClass, Style>();
		for (EStructuralFeature feature : oldStyle.eClass().getEAllStructuralFeatures()) {
			Style newStyle;
			
			EClass containingStyleEClass = feature.getEContainingClass();
			if (excludeStyles.contains(containingStyleEClass))
				continue;
			
			if (eClassMap.containsKey(feature.getEContainingClass())) {
				newStyle = (Style) eClassMap.get(feature.getEContainingClass());
			} else {
				eClassMap.put(feature.getEContainingClass(), newStyle = newView.getStyle(feature.getEContainingClass()));
			}
			if (newStyle != null) {
				if (!oldStyle.eIsSet(feature)) {
					if (!newStyle.eIsSet(feature)) {
						// both features are null; nothing to do
						continue;
					} else {
						// new style's feature is set and must be cleared
						newStyle.eUnset(feature);
					}
				} else {
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
	 * If the child view has the same element as the parent and also has a type, it is considered a subview
	 * and therefore only its properties are copied to matching subviews (if any) of the new parent.
	 * Otherwise, the default behavior is for the child view to be moved to the new parent
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
		Collection guides = EMFCoreUtil.getReferencers(oldNode, new EReference[]{NotationPackage.eINSTANCE.getNodeEntry_Key()});
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
		Collection views = EMFCoreUtil.getReferencers(element, new EReference[]{NotationPackage.eINSTANCE.getView_Element()});

		// remove subviews since they will be refactored with their parent
		for (Iterator i = views.iterator(); i.hasNext();) {
			View view = (View) i.next();
			
			EObject parent = null;
			while ((parent = view.eContainer()) instanceof View) { 
				if (views.contains(parent)) {
					i.remove();
					break;
				}
				view = (View) parent;
			}
		}
		return views;
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
		return ViewService.getInstance().createNode(
			new EObjectAdapter(newElement), 
			(View)oldNode.eContainer(), 
			getNewViewType(oldNode, newElement), 
			ViewUtil.APPEND, 
			preferencesHint);
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
		Edge edge = (Edge) ViewService.getInstance().createEdge(
			new EObjectAdapter(newElement), 
			oldEdge.getDiagram(), 
			getNewViewType(oldEdge, newElement), 
			ViewUtil.APPEND, 
			preferencesHint);
		if (edge != null) {
			edge.setSource(oldEdge.getSource());
			edge.setTarget(oldEdge.getTarget());
		}
		return edge;
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
		return ViewService.getInstance().createDiagram(
			new EObjectAdapter(newElement), 
			getNewViewType(oldDiagram, newElement),
			preferencesHint);
	}

	/**
	 * Returns the type of the new view that replaces the old one 
	 * 
	 * @param oldView The old view being replaced
	 * @param newElement The new element of the new view
	 * @return The type of the new view
	 */
	protected String getNewViewType(View oldView, EObject newElement) {
		return oldView.getType();
	}
	
}
