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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.internal.commands.PersistElementCommand;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.core.listener.PresentationListener;
import org.eclipse.gmf.runtime.diagram.core.listener.PropertyChangeNotifier;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.emf.core.util.MetaModelUtil;
import org.eclipse.gmf.runtime.emf.core.util.ProxyUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.Style;
import org.eclipse.gmf.runtime.notation.View;

/**
 * provides different utility functions for the notation view
 * @author mmostafa
 */
public class ViewUtil{
	
	/**
	 * the append index, this is the index you should use to append a view
	 * to a container
	 */
	public static final int APPEND = -1;
	
	
	/**
	 * create a list of View Adapters from a Notation View collection
	 * @param views a collection of Notation <code>View</code>s
	 * @return	list of <code>EObjectAdapter</code>s
	 */
	public static List makeViewsAdaptable(Collection views){
		List list = new ArrayList();
		Iterator it = views.iterator();
		while(it.hasNext()){
			list.add(new EObjectAdapter((View)it.next()));
		}
		return list;
	}
	
	/** 
	 * move the supplied view from, and all of its parents from the transient 
	 * collections to the persisted collections. This Api will modify the model
	 * and make it dirty, it needs to run within a write action or unchecked
	 * operation. 
	 * A view will get persisted if the following conditions are met
	 * <UL>
	 * <LI> this method is invoked inside an UNDO interval
	 * <LI> the supplied view is in a transient list or owned by a transient container
	 * </UL>
	 * @param view the <code>View</code> to persist
	 */
	public static void persistElement(View view) {
		assert null != view: "null view in ViewUtil.persistElement";//$NON-NLS-1$
		MEditingDomain editingDomain = MEditingDomainGetter.getMEditingDomain(view);
		if (editingDomain.isUndoIntervalOpen() &&
			!view.isMutable()) {
			// get first view needs to get persisted
			View viewToPersist = getViewToPersist(view);
			if (viewToPersist!=null){
				// now create a command to persisted the view and exectue it
				PersistElementCommand pvc = 
					new PersistElementCommand(viewToPersist);
				pvc.execute( new NullProgressMonitor() );
				editingDomain.setCanRedoCurrentInterval( false );
				CommandResult result = pvc.getCommandResult();
				view = (View)result.getReturnValue();
			}
		}
	}

	/**
	 * helper method used to get the first view needs to be persisted,
	 * starting from the passed view, it could return the passed view
	 * itself if it is a transient view, other wise it will check its
	 * parent and so on ...
	 * @param view , view to start from
	 * @return first view needs to get persisted
	 */
	static private View getViewToPersist(View view) {
		EObject container = view.eContainer();
		// if the view had no container then it can not get persisted
		if (container==null)
			return null;
		// now edges are special case, becuase they do not exist in the 
		// children lists, but in the edgs lists
		if (view instanceof Edge){
			Diagram dContainer = (Diagram)container;
			// always make sure that the feature is set before calling get
			// to avoid creating unwanted EList that will stay in the memory
			// till the model is closed
			if (dContainer.eIsSet(NotationPackage.eINSTANCE.getDiagram_TransientEdges()) &&
				dContainer.getTransientEdges().size()>0)
				return view;
			else
				return (getViewToPersist(dContainer)); 
		}
		else if (container instanceof View){
			View vContainer = (View)container;
			// always make sure that the feature is set before calling get
			// to avoid creating unwanted EList that will stay in the memory
			// till the model is closed
			if (vContainer.eIsSet(NotationPackage.eINSTANCE.getView_TransientChildren()) &&
				vContainer.getTransientChildren().size()>0)
				return view;
			else 
				return (getViewToPersist(vContainer));
		} 
		return null;
	}

	/**
	 * Destroys the supplied view notational element and remove any references
	 * this class may have to it.
	 *
	 * @param view view to destroy
	 */
	public static void destroy(View view) {
		if (view==null)
			return;
		List children = view.getChildren();
		for ( int i = 0; i < children.size(); i++ ) {
			View child = (View)children.get(i);
			destroy(child);
		}
		EObjectUtil.destroy(view);
	}
	
	/**
	 * Returns the container view, or null if the container is not a view or null
	 * @param eObject  a notation view
	 * @return the container <code>View</code>
	 */
	static public View getContainerView(View eObject) {
		EObject container = eObject.eContainer();
		if ( container instanceof View ) {
			return (View)container;
		}
		return null;
	}
	
	
	/**
	 * inserts a child  <code>View</code> in a container. the view will be inserted
	 * in the persisted collection if the <tt>persisted</tt> flag is <tt>true</tt>;
	 * otherwise it will be added to the transied collection.
	 * inserting a transient child does not dirty the model, inserting a persisted
	 * child will dirty the model 
	 * 
	 * @param containerView the view's container
	 * @param childView notation <code>View</Code> to insert 
	 * @param index the view's position within the container's list
	 * @param persisted indicats the persisted state of the view
	 * 
	 */
	public static void insertChildView(
		View containerView,
		View childView,
		int index,
		boolean persisted) {
		if (persisted) {
			insertPersistedElement(containerView,childView,index);
		}
		else {
			insertTransientElement(containerView,childView);
		}
	}	
	
	/**
	 * inserts a child into the transient list, inserting a transient child
	 * does not dirty
	 * @param child , the child to insert
	 * @param container notational element's container
	 */
	static private void insertTransientElement(final View container,final View child) {
	  	if (child instanceof Edge){
	 		Diagram diagram = (Diagram)container;
	 		diagram.insertEdge((Edge)child,false);
	 	} else {
	 		container.insertChild(child,false);
	 	}
	 	return; 
	}

	/**
	 * inserts a child into the persisted list 
	 * @param container the notational element's container
	 * @param child , the child to insert
	 * @param index the notational element's position within the container.
	 */
	static private void insertPersistedElement(View container,View child, int index) {
		if (child instanceof Edge){
	 		Diagram diagram = (Diagram)container;
	 		if (index==-1)
	 			diagram.insertEdge((Edge)child);
	 		else
	 			diagram.insertEdgeAt((Edge)child,index);
	 	} else {
	 		if (index==-1)
	 			container.insertChild(child);
	 		else
	 			container.insertChildAt(child,index);
	 	}
	}
	
	 /**
	  * checks if the passed view is transient or exists in a transient branch
	  * @param view <code>View</code> to check
	  * @return true if transient otherwise false
	  */
	static public boolean isTransient(EObject view) {
		EStructuralFeature sFeature = view.eContainingFeature();
		// root element will have a null containing feature
		if (sFeature==null)
			return false;
		if (sFeature.isTransient()){
			return true;
		}
		EObject container  = view.eContainer();
		if (container!=null){
			return isTransient(container);
		}
		return false;
	}
	
	/**
	 * gets a the first child in the passed <code>View</code> that had the same type as
	 * the passed semantic hint. 
	 * @param view	the view to search inside
	 * @param semanticHint	the semantic hint to look for
	 * @return	the found view or null if none is found
	 */
	public static View getChildBySemanticHint(View view,String semanticHint) {
		for(Iterator children = view.getChildren().iterator();children.hasNext();) {
			View child = (View)children.next();
			if ( semanticHint.equals(child.getType())) {
				return child ;
			}
		}
		return null;
	}
	
	
	/**
	 * checks if the passed property is supported bythe passed view
	 * @param view  the view to use for the search
	 * @param id	the property to look for
	 * @return boolean	<tt>true</tt> if supported otherwise <tt>false</tt>
	 */
	public static boolean isPropertySupported(View view,Object id) {
		if(id instanceof String){
			EStructuralFeature feature = (EStructuralFeature) MetaModelUtil.getElement((String)id);
			if (feature != null) {
				return isPropertySupported(view,feature, feature.getEContainingClass());
			}
		}
		return false;
	}
	
	/**
	 * checks if the passed feature is supported by the passed view
	 * @param view  the view to use for the search
	 * @param feature the feature to look for
	 * @param featureClass the feature's <code>EClass</code>
	 * @return boolean	<tt>true</tt> if supported otherwise <tt>false</tt>
	 */
	public static  boolean isPropertySupported(View view,EStructuralFeature feature, EClass featureClass) {
		// check if the id belongs to the view
		if (view.getStyle(featureClass) != null)
			return true;
		
		if (view instanceof Node){
			LayoutConstraint constraint = ((Node)view).getLayoutConstraint();
			if (constraint != null && featureClass.isInstance(constraint))
				return true;
		}
		
		// check if the id belongs to a style owned by the view
		return featureClass.isInstance(view);
	}

	/**
	 * Returns the value of the property with the given id inside the passed view
	 * @param view the view to use to get the value
	 * @param id the id of the property to get
	 * @return the value of the property, or <code>null</code>
	 */
	static public final Object getPropertyValue(View view, Object id) {
		if(id instanceof String){
			EStructuralFeature feature = (EStructuralFeature) MetaModelUtil.getElement((String)id);
			if (feature != null) {
				return ViewUtil.getPropertyValue(view,feature, feature.getEContainingClass());
			}
		}
		return null;
	}
	
	/**
	 * Returns the value of the featrue inside a specific EClass within the passed view
	 * @param view the view to use to get the value
	 * @param feature the featrue to use to get the value
	 * @param featureClass the <code>EClass</code> to use to get the feature
	 * @return the value of the feature, or <code>null</code>
	 */
	public static  Object getPropertyValue(View view,EStructuralFeature feature, EClass featureClass) {
		// check if the id belongs to a style owned by the view
		Style style = view.getStyle(featureClass);
		if (style != null)
			return style.eGet(feature);
		
		if (view instanceof Node){
			LayoutConstraint constraint = ((Node)view).getLayoutConstraint();
			if (constraint != null && featureClass.isInstance(constraint))
				return constraint.eGet(feature);
		}
		
		// check if the id belongs to the view
		if (featureClass.isInstance(view))
			return view.eGet(feature);

		return feature.getDefaultValue(); // for extra robustness
	}

	/**
	 * Sets the property with the given id if possible on the passed view
	 * to the passed value.
	 * @param view the view to set the value on 
	 * @param id  the id of the property being set
	 * @param value  the value of the property being set
	 */
	public static void setPropertyValue(View view,Object id, Object value) {
		if(id instanceof String){
			EStructuralFeature feature = (EStructuralFeature) MetaModelUtil.getElement((String)id);
			if (feature != null) {
				ViewUtil.setPropertyValue(view,feature, feature.getEContainingClass(), value);
				return;
			}
		}
	}

	/**
	 * Sets the passed featrue on the passed EClass inside the passed view
	 * to the new value if possible
	 * @param view the view to set the value on 
	 * @param feature the feature to set
	 * @param featureClass <code> EClass </code> that owns the feature
	 * @param value  the value of the feature being set
	 */
	public static void setPropertyValue(View view,EStructuralFeature feature, EClass featureClass, Object value) {
		if( view == null )
			return;
		// check if the id belongs to a style owned by the view
		Style style = view.getStyle(featureClass);
		if (style != null) {
			style.eSet(feature, value);
			return;
		} 
		
		if (view instanceof Node){
			Node node = (Node)view;
			LayoutConstraint constraint = node.getLayoutConstraint();
			if (constraint != null & featureClass.isInstance(constraint)) {
				constraint.eSet(feature, value);
				return;
			}
		}

		// check if the id belongs to the view
		if (featureClass.isInstance(view)) {
			view.eSet(feature, value);
			return;
		}
	}
	
	/**
	 * resolves the passed <code>View<code>'s semantic element, and returns it.
	 * If the semantic element is unresolvable the method will returns <code>null</code>
	 * @param view the view to use to get the semantic element
	 * @return the semanticelement or null if there is no semantic element or if it is unresolvable
	 */
	public static EObject resolveSemanticElement(View view) {
	    EObject element = view.getElement();
	    if (element!=null && element.eIsProxy())
	    	return ProxyUtil.resolve(MEditingDomainGetter.getMEditingDomain(view), element);
	    return element;
	}
	
	/** 
	 * gets the <code>View</code>'s semantic element Class Id, this could be used to
	 * check the semantic element type
	 * @param view the owner of the semantic element
	 * @return the semantic element class Id
	 */
	public static String getSemanticElementClassId(View view) {
		EObject element = view.getElement();
	    return element == null ? null : ProxyUtil.getProxyClassID(element);
	}
	
	/**
	 * Retrieves the view's property change notifier object
	 * @param view the view to use to get the notifier
	 * @return the property change notifier
	 * @deprecated the PropertyChangeNotifier is deprecated, to add a listner to the 
	 * <code>PresentationListener</code>use <code>PresentationListener.addPropertyChangeListener()</code>
	 */
	public static PropertyChangeNotifier getPropertyChangeNotifier(View view) {
		return PresentationListener.getNotifier(view);
	}
	
	/**
	 * gets all the <code>Edge</code>'s whose source is this view
	 * @param view the view to use
	 * @return List	the edges list
	 */
	public static List getSourceConnections(View view) {
		if (!view.eIsSet(NotationPackage.eINSTANCE.getView_SourceEdges()))
			return Collections.EMPTY_LIST;
		return view.getSourceEdges();
	}
	
	/**
	 * gets all the <code>Edge</code>'s whose target is this view
	 * @param view the view to use
	 * @return List	the edges list
	 */
	public static List getTargetConnections(View view) {
		if (!view.eIsSet(NotationPackage.eINSTANCE.getView_TargetEdges()))
			return Collections.EMPTY_LIST;
		return view.getTargetEdges();
	}
	
	/**
	 * return eClass Name of the view's semantic element, this method 
	 * works only if the semantic element is a NameElement, otherwise 
	 * it will return an Empty String 
	 * @param  view	the view object
	 * @return the eClass name
	 */
	public static String getSemanticEClassName(View view){
		EObject eObject = view.getElement();
		if (eObject != null)
			return ProxyUtil.getProxyClassID(eObject);
		return ""; //$NON-NLS-1$
	}
	
	/**
	 * returns the unique GUID of the view
	 * @param view the view
	 * @return String the GUID of a view (constant)
	 */
	public static String getIdStr(View view) {
		return EObjectUtil.getID( view );
	}
	
	/**
	 * reorders the child at the oldIndex to the newIndex
	 * @param container the view's container
	 * @param child the child to reposition
	 * @param newIndex (zero-based)
	 * @throws IndexOutOfBoundsException if index is out of bounds
	 * @throws IllegalArgumentException if the child is not contianed by the container,
	 * or if the new position is the <code>ViewUtil.APPEND</code>position 
	 */
	static public void repositionChildAt(View container , View child, int newIndex) {
		ViewUtil.persistElement(child);
		if (child.eContainer() != container)
			throw new IllegalArgumentException("child is not an existing child of the view"); //$NON-NLS-1$
		if (newIndex == APPEND)
			throw new IllegalArgumentException("append position is not allowed for reposition"); //$NON-NLS-1$
		container.removeChild(child);
		container.insertChildAt(child,newIndex);
	}
	
	/**
	 * returns the first child whose id matched the given id
	 * @param view the view to search in 
	 * @param idStr the child's id
	 * @return View the first matching child or null if no one was found
	 */
	static public View getChildByIdStr(View view, String idStr) {
		for(Iterator children = view.getChildren().iterator();children.hasNext();) {
			View child = (View)children.next();
			if ( idStr.equals( EObjectUtil.getID(child))) {
				return child;
			}
		}
		return null;
	}
	
}
