/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.view;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;

import org.eclipse.gmf.runtime.diagram.core.internal.services.view.ViewService;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.core.internal.view.IContainerView;
import org.eclipse.gmf.runtime.diagram.core.internal.view.IDiagramView;
import org.eclipse.gmf.runtime.diagram.core.internal.view.IView;
import org.eclipse.gmf.runtime.diagram.core.listener.PresentationListener;
import org.eclipse.gmf.runtime.diagram.core.listener.PropertyChangeNotifier;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.IncarnationUtil;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.emf.core.edit.MObjectState;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.emf.core.util.MetaModelUtil;
import org.eclipse.gmf.runtime.emf.core.util.ProxyUtil;
import com.ibm.xtools.notation.NotationPackage;
import com.ibm.xtools.notation.Style;
import com.ibm.xtools.notation.View;

/**
 * The basic implementation of a view facade object
 * @author melaasar
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public abstract class AbstractView implements IView, IContainerView, Properties {

	/**
	 * The encapsulated view element
	 */
	private View view = null;

	
	/**
	 * Wraps a view element
	 */
	public AbstractView() {
		// need to be revisited
	}
	
	/**
	 * Wraps a view element
	 * @param view
	 */
	public AbstractView(View view) {
		Assert.isNotNull(view);
		this.view = view;
	}

	/**
	 * @return the notational element
	 */
	final View getViewElement() {
		return view;
	}
	
	/**
	 * @return a list of style for the newly created view or an empty list if none (do not return null)
	 */
	protected List createStyles() {
		return new ArrayList();
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#getIdStr()
	 */
	public final String getIdStr() {
		return EObjectUtil.getID( view );
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#getResource()
	 */
	public final Resource getResource() {
		return getViewElement().eResource();
	}

	/**
	 * Adapts to itself and to the wrapped view element
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(Class)
	 */
	public final Object getAdapter(Class adapter) {
		if (adapter == null)
			return null;
		if (adapter.isInstance(getViewElement()))
			return getViewElement();
		if (adapter.isInstance(this))
			return this;
		return null;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#destroy()
	 */
	public final void destroy() {
		ViewUtil.destroy(view);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#isDetached()
	 */
	public final boolean isDetached() {
		// Check with the persistence manager to see if the element is deleted...
		View element = getViewElement();
		//if (!PersistenceManager.getSingleton().isDeleted(element))
		//	return false;
		return EObjectUtil.getState(element).equals(MObjectState.DETACHED);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.view.IView#getContainerDiagramView()
	 */
	public final IDiagramView getDiagramView() {
		for (IView parent = this; parent != null; parent = parent.getContainerView())
			if (parent instanceof IDiagramView)
				return (IDiagramView) parent;
		return null;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#getContainerView()
	 */
	public final IContainerView getContainerView() {
		return (IContainerView)incarnateView(ViewUtil.getContainerView(getViewElement()));
	}


	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#getPropertyChangeNotifier()
	 */
	public final PropertyChangeNotifier getPropertyChangeNotifier() {
		return PresentationListener.getNotifier(getViewElement());
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#getReferencedModel()
	 */
	public final EObject getSemanticElement() {
		return getViewElement().getElement();
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#setSemanticElement(org.eclipse.emf.ecore.EObject)
	 */
	public final void setSemanticElement(EObject element) {
		getViewElement().setElement(element);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#resolveSemanticElement()
	 */
	public final EObject resolveSemanticElement() {
	    EObject element = getSemanticElement();
	    return element == null ? null : ProxyUtil.resolve(MEditingDomainGetter.getMEditingDomain(getViewElement()), element);
	}

	/** 
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#getTypeId()
	 */
	public final String getSemanticElementClassId() {
		EObject element = getSemanticElement();
	    return element == null ? null : ProxyUtil.getProxyClassID(element);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#getSemanticType()
	 */
	public final String getSemanticType() {
		return getViewElement().getType();
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#setSemanticType(java.lang.String)
	 */
	public final void setSemanticType(String type) {
		getViewElement().setType(type);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#isVisible()
	 */
	public final boolean isVisible() {
		return getViewElement().isVisible();
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#setVisible(boolean)
	 */
	public final void setVisible(boolean visible) {
		getViewElement().setVisible(visible);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#isPropertySupported(Object)
	 */
	public final boolean isPropertySupported(Object id) {
		Assert.isTrue(id instanceof String);
		EStructuralFeature feature = (EStructuralFeature) MetaModelUtil.getElement((String)id);
		if (feature != null) {
			return isPropertySupported(feature, feature.getEContainingClass());
		}
		return false;
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#isPropertySupported(Object)
	 */
	protected boolean isPropertySupported(EStructuralFeature feature, EClass featureClass) {
		View element = getViewElement();

		// check if the id belongs to the view
		if (element.getStyle(featureClass) != null)
			return true;
		
		// check if the id belongs to a style owned by the view
		return featureClass.isInstance(element);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#getPropertyValue(java.lang.Object)
	 */
	public final Object getPropertyValue(Object id) {
		Assert.isTrue(id instanceof String);
		EStructuralFeature feature = (EStructuralFeature) MetaModelUtil.getElement((String)id);
		if (feature != null) {
			return getPropertyValue(feature, feature.getEContainingClass());
		}
		// property is not supported
		Assert.isTrue(false, "Property ("+id+") is not supported"); //$NON-NLS-1$ //$NON-NLS-2$
		return null;
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#getPropertyValue(java.lang.Object)
	 */
	protected Object getPropertyValue(EStructuralFeature feature, EClass featureClass) {
		View element = getViewElement();

		// check if the id belongs to a style owned by the view
		Style style = element.getStyle(featureClass);
		if (style != null)
			return style.eGet(feature);
		
		// check if the id belongs to the view
		if (featureClass.isInstance(element))
			return element.eGet(feature);

		return feature.getDefaultValue(); // for extra robustness
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	public final void setPropertyValue(Object id, Object value) {
		Assert.isTrue(id instanceof String);
		EStructuralFeature feature = (EStructuralFeature) MetaModelUtil.getElement((String)id);
		if (feature != null) {
			setPropertyValue(feature, feature.getEContainingClass(), value);
			return;
		}
		// property is not supported
		Assert.isTrue(false, "Property ("+id+") is not supported"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	protected void setPropertyValue(EStructuralFeature feature, EClass featureClass, Object value) {
		View element = getViewElement();

		// check if the id belongs to a style owned by the view
		Style style = element.getStyle(featureClass);
		if (style != null) {
			style.eSet(feature, value);
			return;
		}

		// check if the id belongs to the view
		if (featureClass.isInstance(element)) {
			element.eSet(feature, value);
			return;
		}
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#getStyle(org.eclipse.emf.ecore.EClass)
	 */
	public Style getStyle(EClass eClass) {
		return getViewElement().getStyle(eClass);
	}

	/**
	 * Initialize the newly created view from the preference store
	 * @param store the preference store
	 */
	protected void initializeFromPreferences() {
		
		IPreferenceStore store = (IPreferenceStore) PreferencesHint.MODELING.getPreferenceStore();

		// line color
		RGB lineRGB =
			PreferenceConverter.getColor(
				store,
				IPreferenceConstants.PREF_LINE_COLOR);
		setPreferncePropertyValue(Properties.ID_LINECOLOR, FigureUtilities.RGBToInteger(lineRGB));

		//default font
		FontData fontData =
			PreferenceConverter.getFontData(
				store,
				IPreferenceConstants.PREF_DEFAULT_FONT);
		setPreferncePropertyValue(Properties.ID_FONTNAME, fontData.getName());
		setPreferncePropertyValue(Properties.ID_FONTSIZE, new Integer(fontData.getHeight()));
		setPreferncePropertyValue(Properties.ID_FONTBOLD, Boolean.valueOf((fontData.getStyle() & SWT.BOLD) != 0));
		setPreferncePropertyValue(Properties.ID_FONTITALIC, Boolean.valueOf((fontData.getStyle() & SWT.ITALIC) != 0));

		//font color
		RGB fontRGB =
			PreferenceConverter.getColor(
				store,
				IPreferenceConstants.PREF_FONT_COLOR);
		setPreferncePropertyValue(Properties.ID_FONTCOLOR, FigureUtilities.RGBToInteger(fontRGB));
	}

	/**
	 * A utility method to set a property value from the preference
	 * "only" if it was found to be supported by the view. This is needed
	 * since some derived views could decide to shadow certain properties
	 * @param id the preference property id
	 * @param value the preference property value
	 */
	protected final void setPreferncePropertyValue(Object id, Object value) {
		if (isPropertySupported(id))
			setPropertyValue(id, value);
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IContainerView#getChildByIdStr(String)
	 */
	public final IView getChildByIdStr(String idStr) {
		Assert.isNotNull(idStr);
		for(Iterator children = getChildViewElements().iterator();children.hasNext();) {
			View child = (View)children.next();
			if ( idStr.equals( EObjectUtil.getID(child))) {
				return incarnateView( child );
			}
		}
		return null;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IContainerView#getChildren()
	 */
	public final List getChildren() {
		List children = getChildViewElements();
		List retval = new ArrayList(children.size());
		
		Iterator it = children.iterator();
		while( it.hasNext() ) {
			View child = (View)it.next();
//			IView iChild = incarnateView(child);
			if (child != null) {
				retval.add(child );
			}
		}
		return retval;
	}

	/**
	 * Returns gets the container view's notation children 
	 * @return List of children
	 */
	List getChildViewElements() {
		return getViewElement().getChildren();
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IContainerView#insertChild(IView)
	 */
	public final void insertChild(IView child) {
		insertChildAt(child, APPEND);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IContainerView#insertChildAt(IView,
	 *      int)
	 */
	public final void insertChildAt(IView child, int index) {
		Assert.isNotNull(child);
		ViewUtil.persistElement(((AbstractView)child).getViewElement());
		if (index!=APPEND)
			getViewElement().insertChildAt(((AbstractView) child).getViewElement(),
										index);
		else
			getViewElement().insertChild(((AbstractView) child).getViewElement());
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IContainerView#repositionChildAt(IView, int)
	 */
	public final void repositionChildAt(IView child, int newIndex) {
		ViewUtil.persistElement(((AbstractView)child).getViewElement());
		View container = getViewElement();
		View vChild = ((AbstractView)child).getViewElement();
		
		if (vChild.eContainer() != container)
			throw new IllegalArgumentException("child is not an existing child of the view"); //$NON-NLS-1$
		if (newIndex == APPEND)
			throw new IllegalArgumentException("append position is not allowed for reposition"); //$NON-NLS-1$
		
		container.removeChild(vChild);
		container.insertChildAt(vChild,newIndex);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#pasteFromString(String)
	 */
	public final List pasteFromString(String clipboard) {
	    ArrayList retval = new ArrayList();
	    Iterator pastedElements = EObjectUtil.deserialize( getViewElement(), clipboard, Collections.EMPTY_MAP).iterator();
        while( pastedElements.hasNext() ) {
            Object element = pastedElements.next();
            if (element instanceof View) {
	            IView newView = IncarnationUtil.incarnateView((View)element);
	            if ( newView != null ) {
	                retval.add(newView);
	            }
            }
        }
        return retval;
	}

	/** 
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#getSourceConnections()
	 */
	public final List getSourceConnections() {
		View element = getViewElement();
		if (!element.eIsSet(NotationPackage.eINSTANCE.getView_SourceEdges()))
			return Collections.EMPTY_LIST;
		return createViewListFromReferences(element.getSourceEdges());
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.view.IView#getTargetConnections()
	 */
	public final List getTargetConnections() {
		View element = getViewElement();
		if (!element.eIsSet(NotationPackage.eINSTANCE.getView_TargetEdges()))
			return Collections.EMPTY_LIST;
		return createViewListFromReferences(element.getTargetEdges());
	}

	/**
	 * Factory method to insert a newly created notational element. A persisted notational
	 * element is returned if the <tt>persisted</tt> flag is <tt>true</tt>;
	 * otherwise a non-persisted element is returned.
	 * 
	 * @param viewKind
	 *            the NotationLanguageElementKind value identifying the
	 *            notational element to create
	 * @param containerView
	 *            the notational element's container
	 * @param index
	 *            the notational element's position within the container's list
	 * @param persisted
	 *            flag indicating the type of notational element to create
	 */
	static void insertChildView(
		AbstractView containerView,
		View childView,
		int index,
		boolean persisted) {
		ViewUtil.insertChildView(containerView.getViewElement(),childView,index, persisted);
	}	

	/**
	 * Converts the supplied list of reference to a list of views.
	 * @param viewReferences - a collection of IReferences to view elements
	 * @return a list of connector views.
	 */
	private static List createViewListFromReferences(List viewReferences) {
		List viewList = new ArrayList( viewReferences.size() );
		Iterator refs = viewReferences.iterator();
		while( refs.hasNext() ) {
			View ref = (View)refs.next();
			viewList.add( IncarnationUtil.incarnateView(ref) );
		}
		return viewList;
	}

	/**
	 * Method findChildView. performs a breadth-first search on the children
	 * tree to find a view of the given type and/or with the given factory hint
	 * 
	 * @param parentView
	 *            the parent View
	 * @param childViewType
	 *            the child type or <code>null<code> if not required
	 * @param semanticHint the child semantic hint or <code>null<code> if not required
	 * @param searchDepth the number of children levels to search or -1 if all levels required 
	 * @return IView the child view if found or <code>null<code>
	 */
	static IView findChildView(
		IContainerView parentView,
		Class childViewType,
		String semanticHint,
		int searchDepth) {

		Assert.isTrue(childViewType != null || semanticHint != null);

		List childrenList = parentView.getChildren();
		for (Iterator children = childrenList.iterator(); children.hasNext();) {
			IView child = (IView) children.next();
			if ((childViewType == null) || (childViewType.isInstance(child)))
				if ((semanticHint == null) || semanticHint.equals(child.getSemanticType()))
					return child;
		}
		
		if ((searchDepth == -1) || (--searchDepth > 0)) {
			for (Iterator children = childrenList.iterator(); children.hasNext();) {
				Object nextChild = children.next();
				IView child = 
					findChildView(
						(IContainerView) nextChild,
						childViewType,
						semanticHint,
						searchDepth);
				if (child != null)
					return child;
			}
		}
		return null;
	}

	/**
	 * Method getViewService.
	 * a utility function to return the view service instance
	 * @return ViewService
	 */
	protected static ViewService getViewService() {
		return ViewService.getInstance();
	}
	
	/**
	 * Method incarnateView.
	 * incarnates the given viewElement into a view facade object
	 * @param viewElement
	 * @return View
	 */
	static IView incarnateView(View viewElement) {
		return IncarnationUtil.incarnateView(viewElement);
	}
	
	/**
	 * sets the view element
	 * @param view
	 */
	final void setView(com.ibm.xtools.notation.View view){
		Assert.isNotNull(view);
		this.view = view;
	}
	
	/**
	 * indicates if the view requires an element inside it or it can use
	 * its container's element
	 * 
	 * @param semanticAdapter
	 * @param containerView
	 * @return
	 */
	protected boolean requiresElement(IAdaptable semanticAdapter, IContainerView containerView) {
		EObject containerSemanticElement = containerView.getSemanticElement();
		EObject semanticElement = null;
		if (semanticAdapter!=null)
			semanticElement = (EObject)semanticAdapter.getAdapter(EObject.class);
		if (containerSemanticElement==semanticElement)
			return false;
		return true;
	}

}
