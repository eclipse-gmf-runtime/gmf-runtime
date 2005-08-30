/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.view.factories; 

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.internal.view.factories.AbstractViewFactory;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.core.edit.MRunOption;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import com.ibm.xtools.notation.LayoutConstraint;
import com.ibm.xtools.notation.Node;
import com.ibm.xtools.notation.NotationFactory;
import com.ibm.xtools.notation.View;

/**
 * This is the base factory class for all Node views, it will 
 * create the view and decorate it using the default decorations
 * you can subclass it to add more decorations, or customize the 
 * way it looks, like adding new style
 * @see #createView(IAdaptable, View, String, int, boolean, String)
 * @see #decorateView(View, View, IAdaptable, String, int, boolean)
 * @see #createStyles()
 * @author mmostafa
 * 
 */
public class AbstractNodeViewFactory extends AbstractViewFactory {

	/**
	 * factory method, that will be called by the view service to creat
	 * the view
	 * @param semanticAdapter semanitc element of the view, it can be null
     * @param containerView the view to contain the connector
     * @param semanticHint a semantic hint to reflect the view type, it can be empty 
     * @param index position with parent's child collection 
     * @param persisted persisted flag, this will indicate if the created view
	 * will be a presisted or transient view, transient views never get serialized
	 */
	public View createView(final IAdaptable semanticAdapter,
						   final View containerView,
						   final String semanticHint,
						   final int index,
		boolean persisted, final PreferencesHint preferencesHint) {
		setPreferencesHint(preferencesHint);
		final Node node = createNode();
		node.getStyles().addAll(createStyles());
		node.setLayoutConstraint(createLayoutConstraint());

		if (requiresElement(semanticAdapter,containerView)){
			if (semanticAdapter==null)
				// enforce a set to NULL
				node.setElement(null);
			else
				node.setElement((EObject)semanticAdapter.getAdapter(EObject.class));
		}
		
		node.setType(semanticHint);
		
	    // decorate view assumes that the view had been inserted already, so 
		// we had to call insert child before calling decorate view
		ViewUtil.insertChildView(containerView,node,index, persisted);
		int options = MRunOption.UNCHECKED | MRunOption.SILENT;
		MEditingDomainGetter.getMEditingDomain(containerView).runWithOptions(new MRunnable() {
			public Object run() {
				// decorate view had to run as a silent operation other wise
				//it will generate too many events 
				decorateView(containerView,node, semanticAdapter, semanticHint, index, true);
				return null;
			}
		},options);
		return node;
	}
	
	/**
	 * This method is responsible for decorating the created view, it get called
	 * by the Factory method @link #createView(IAdaptable, View, String, int, boolean),
	 * it will intiliaze the view with the default preferences also it will create 
	 * the default elements of the <code>View</code> if it had any
	 * @param containerView the container of the view
	 * @param view the view itself
	 * @param semanticAdapter the semantic elemnent of the view (it could be null)
	 * @param semanticHint the semantic hint of the view
	 * @param index the index of the view
	 * @param persisted flag indicating the the view was created as persisted or not
	 */
	protected void decorateView(View containerView,
								View view,
								IAdaptable semanticAdapter,
								String semanticHint,
								int index,
								boolean persisted){
		initializeFromPreferences(view);
	}

	/**
	 * Method used to create the layout constraint that will get set on the 
	 * created view. You can override this method in your own factory to change
	 * the default constraint. This method is called by @link #createView(IAdaptable, View, String, int, boolean) 
	 * @return a new layout constraint for the view
	 */
	protected LayoutConstraint createLayoutConstraint() {
		return null;
	}

	
	/**
	 * method to create the Notation meta model <code>Node</code>
	 * object that represents the view, you can override this method in your
	 * own factories to create you own node while extends the <code>Node</code> class
	 * this method is invoked by @link #createView(IAdaptable, View, String, int, boolean)
	 * @return the created <code>Node</code>
	 */
	protected Node createNode(){
		return NotationFactory.eINSTANCE.createNode();
	}
	

}