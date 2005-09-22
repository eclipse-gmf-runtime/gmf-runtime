/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.services.editpart;

import java.lang.reflect.Constructor;
import java.text.MessageFormat;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.RootEditPart;
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart.IEditPartProvider;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.emf.core.util.ProxyUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;


/**
 * An AbstractFactory implementation of the <code>IEditPartProvider</code> interface.  
 * This implementations invokes the approrpiate factory method by inspecting the supplied
 * operation and provides the actual editpart creation functionality.
 * @see #provides(IOperation)
 * 
 */
/*
 * @canBeSeenBy %partners
 */
public abstract class AbstractEditPartProvider extends AbstractProvider
	implements IEditPartProvider, Properties {

	/**
	 * create an instance of <code>GraphicEditPaty</code>.   The instance is created
	 * via reflection and the supplied paramter is the editpart's constructor paramter.
	 * @param view the view to be controlled by the created editpart.
	 */
	public IGraphicalEditPart createGraphicEditPart(View view) {
		Class editpartClass = getEditPartClass(view);
		IGraphicalEditPart graphicEditPart = createNewGraphicEditPart(editpartClass, new Object[] {view});
		Assert.isNotNull(graphicEditPart);
		return graphicEditPart;
	}
	
	/**
	 * Cycles through the various operations supported by this provider and  
	 * invokes the appropriate <code>setXXXEditPartClass</code> method.
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(IOperation)
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.editpart.CreateGraphicEditPartOperation#getView()
	 * @param operation an instance of a <code>CreateGraphicEditPartOperation</code>
	 * @return <tt>true</tt> if an editpart class was set, otherwise <tt>false</tt>. (i.e., 
	 * <pre>return getEditPartClass() != null; </pre>
	 */
	public boolean provides(IOperation operation) {
		if ( operation instanceof CreateGraphicEditPartOperation ) {
			View view = (( CreateGraphicEditPartOperation)operation).getView();
			return getEditPartClass(view) != null;
		}
		return false;
	}

	/**
	 * Gets an editpart class for the given view
	 * @param view
	 * @return <code>Class</code>
	 */
	private Class getEditPartClass(View view) {
		if ( view instanceof Diagram)
			return getDiagramEditPartClass(view );
		else if ( view instanceof Edge )
			return getConnectorEditPartClass(view);
		else if (view instanceof Node)
			return getNodeEditPartClass(view);
		return null;
	}
	
	/** 
	 * Gets a connector's editpart class. 
	 * This method should be overridden by a provider if it wants to provide this service. 
	 * @param view the view to be <i>controlled</code> by the created editpart
	 * @return <code>Class</code>
	 */
	protected Class getConnectorEditPartClass(View view ) {
		return null;
	}

	/**
	 * Gets a diagram's editpart class.
	 * This method should be overridden by a provider if it wants to provide this service. 
	 * @param view the view to be <i>controlled</code> by the created editpart
	 * @return <code>Class</code> 
	 */
	protected Class getDiagramEditPartClass(View view ) {
		return null;
	}
	
	/**
	 * Gets a Node's editpart class.
	 * This method should be overridden by a provider if it wants to provide this service. 
	 * @param view the view to be <i>controlled</code> by the created editpart
	 * @return <code>Class</code>
	 */
	protected Class getNodeEditPartClass(View view ) {
		return null;
	}
	
	/**
	 * Creates an editpart via reflection.
	 * @param constructorParams the editpart's constructor paramters.
	 */
	private IGraphicalEditPart createNewGraphicEditPart(Class editpartClass, Object[] constructorParams) {
		try {
			Constructor constructor = getCreationConstructor(editpartClass);
			Assert.isNotNull(constructor);

			return (constructor == null)? null : (IGraphicalEditPart) constructor.newInstance(constructorParams);
		} 
		catch (Throwable e) {
			String eMsg = MessageFormat.format(  
				PresentationResourceManager.getInstance().getString("AbstractEditPartProvider.new.graphicaleditpart.failed_ERROR_"),//$NON-NLS-1$
				new Object[] {editpartClass});
			Log.warning(DiagramUIPlugin.getInstance(), IStatus.WARNING, eMsg, e);
			return null;
		}
	}

	/** 
	 * Return the appropriate constuctor for the cached editpart class. 
	 * @return a constructor (<tt>null</tt> if none could be found).
	 */
	private Constructor getCreationConstructor(Class editpartClass) {
		Assert.isNotNull(editpartClass);

		if (editpartClass != null) {
			Constructor[] consts = editpartClass.getConstructors();
			if (consts.length != 0)
				return consts[0];
		}
		return null;
	}

	
	/**
	 * gets the supplied view's underlying notation element's eClass
	 * @param view 
	 * @return the view's <code>EClass</code>
	 */
	protected static EClass getReferencedElementEClass(View view ) {
		return ProxyUtil.getProxyClass(view.getElement());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart.IEditPartProvider#createRootEditPart()
	 */
	public RootEditPart createRootEditPart() {
		return null;
	}
}

