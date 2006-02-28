/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.providers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.diagram.core.internal.DiagramPlugin;
import org.eclipse.gmf.runtime.diagram.core.internal.DiagramStatusCodes;
import org.eclipse.gmf.runtime.diagram.core.internal.l10n.DiagramCoreMessages;
import org.eclipse.gmf.runtime.diagram.core.internal.services.view.CreateDiagramViewOperation;
import org.eclipse.gmf.runtime.diagram.core.internal.services.view.CreateEdgeViewOperation;
import org.eclipse.gmf.runtime.diagram.core.internal.services.view.CreateNodeViewOperation;
import org.eclipse.gmf.runtime.diagram.core.internal.services.view.CreateViewForKindOperation;
import org.eclipse.gmf.runtime.diagram.core.internal.services.view.CreateViewOperation;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.osgi.util.NLS;

/**
 * Implements of the <code>IViewProvider</code> interface. This
 * implementations invokes the approrpiate factory method by inspecting the
 * supplied operation and provides the actual view creation functionality.
 */
public class AbstractViewProvider
	extends AbstractProvider
	implements IViewProvider {

	private static String viewCreationMethodName = "createView"; //$NON-NLS-1$

	private static String diagramCreationMethodName = "createDiagram"; //$NON-NLS-1$
	
	static protected class ClassToCreationMethodMap
		extends HashMap {
		static final long serialVersionUID = 1;
		
		public void addMethod(Class clazz, Method method) {
			if (get(clazz) == null)
				put(clazz, method);
		}

		public Method getCreationMethod(Class clazz) {
			return (Method) get(clazz);
		}
	}

	public static ClassToCreationMethodMap classToCreateMethod = new ClassToCreationMethodMap();

	/**
	 * Determines whether this view provider can provide for the required
	 * operation It inspects the type of view operation and calls the
	 * corresponding <code>provides</code> method.
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(IOperation)
	 */
	public final boolean provides(IOperation operation) {
		/* if this is the CreateViewForKindOperation operation */
		if (operation instanceof CreateViewForKindOperation)
			return provides((CreateViewForKindOperation) operation);

		/* Make sure it is a view operation */
		assert operation instanceof CreateViewOperation : "operation is not CreateViewOperation in AbstractViewProvider";//$NON-NLS-1$

		/* call the specific provides method */
		if (operation instanceof CreateDiagramViewOperation)
			return provides((CreateDiagramViewOperation) operation);
		else if (operation instanceof CreateEdgeViewOperation)
			return provides((CreateEdgeViewOperation) operation);
		else if (operation instanceof CreateNodeViewOperation)
			return provides((CreateNodeViewOperation) operation);
		return false;
	}

	public final Diagram createDiagram(IAdaptable contextElement,
			String diagramKind, PreferencesHint preferencesHint) {
		return (Diagram) createNewView(getDiagramViewClass(contextElement,
			diagramKind), new Object[] {contextElement, diagramKind,
			preferencesHint});
	}

	public final Edge createEdge(IAdaptable semanticAdapter,
			View containerView, String semanticHint, int index,
			boolean persisted, PreferencesHint preferencesHint) {

		return (Edge) createNewView(getEdgeViewClass(semanticAdapter,
			containerView, semanticHint), new Object[] {semanticAdapter,
			containerView, semanticHint, new Integer(index),
			Boolean.valueOf(persisted), preferencesHint});
	}

	public final Node createNode(IAdaptable semanticAdapter,
			View containerView, String semanticHint, int index,
			boolean persisted, PreferencesHint preferencesHint) {

		return (Node) createNewView(getNodeViewClass(semanticAdapter,
			containerView, semanticHint), new Object[] {semanticAdapter,
			containerView, semanticHint, new Integer(index),
			Boolean.valueOf(persisted), preferencesHint});
	}

	/**
	 * Determines whether this provider can provide for the specified view
	 * creation operation
	 * 
	 * @param operation
	 *            Contains a semantic kind and a containerView
	 * @return boolean
	 */
	protected boolean provides(CreateViewForKindOperation op) {
		if (op.getViewKind() == Node.class)
			return getNodeViewClass(op.getSemanticAdapter(), op
				.getContainerView(), op.getSemanticHint()) != null;
		if (op.getViewKind() == Edge.class)
			return getEdgeViewClass(op.getSemanticAdapter(), op
				.getContainerView(), op.getSemanticHint()) != null;
		return true;
	}

	/**
	 * Determines whether this provider can provide for the specified diagram
	 * view operation
	 * 
	 * @param operation
	 * @return boolean
	 */
	protected boolean provides(CreateDiagramViewOperation operation) {
		return getDiagramViewClass(operation.getSemanticAdapter(), operation
			.getSemanticHint()) != null;
	}

	/**
	 * Determines whether this provider can provide for the specified edge view
	 * operation
	 * 
	 * @param operation
	 * @return boolean
	 */
	protected boolean provides(CreateEdgeViewOperation operation) {
		return (getEdgeViewClass(operation.getSemanticAdapter(), operation
			.getContainerView(), operation.getSemanticHint()) != null);
	}

	/**
	 * Determines whether this provider can provide for the specified node view
	 * operation
	 * 
	 * @param operation
	 * @return boolean
	 */
	protected boolean provides(CreateNodeViewOperation operation) {
		return (getNodeViewClass(operation.getSemanticAdapter(), operation
			.getContainerView(), operation.getSemanticHint()) != null);
	}

	/**
	 * Returns the diagram view class to instantiate based on the passed params
	 * 
	 * @param semanticAdapter
	 *            TODO
	 * @param semanticAdapter
	 * @return Class
	 */
	protected Class getDiagramViewClass(IAdaptable semanticAdapter,
			String diagramKind) {
		return null;
	}

	/**
	 * Returns the edge view class to instantiate based on the passed params
	 * 
	 * @param semanticAdapter
	 * @param containerView
	 * @param semanticHint
	 * @return Class
	 */
	protected Class getEdgeViewClass(IAdaptable semanticAdapter,
			View containerView, String semanticHint) {
		return null;
	}

	/**
	 * Returns the node view class to instantiate based on the passed params
	 * 
	 * @param semanticAdapter
	 * @param containerView
	 * @param semanticHint
	 * @return Class
	 */
	protected Class getNodeViewClass(IAdaptable semanticAdapter,
			View containerView, String semanticHint) {
		return null;
	}

	/**
	 * creates a view instance via reflection.
	 * 
	 * @param constructorParams
	 *            the view's constructor parameters.
	 */
	private View createNewView(Class viewClass, Object[] constructorParams) {
		try {
			assert null != viewClass : "Null viewClass in AbstractProvider";//$NON-NLS-1$			
			assert null != constructorParams : "Null constructorParams in AbstractProvider";//$NON-NLS-1$

			Constructor constructor = getFactoryConstructor(viewClass);
			if (constructor == null) {
				Log
					.error(
						DiagramPlugin.getInstance(),
						DiagramStatusCodes.SERVICE_FAILURE,
						"View (" + viewClass + ") is missing a proper creation Factory"); //$NON-NLS-1$//$NON-NLS-2$
				return null;
			}

			Object factory = constructor.newInstance(null);
			Method method = getCreationMethod(viewClass, constructorParams);
			if (method == null) {
				Log
					.error(
						DiagramPlugin.getInstance(),
						DiagramStatusCodes.SERVICE_FAILURE,
						"View (" + viewClass + ") is missing a proper creation Factory"); //$NON-NLS-1$//$NON-NLS-2$
				return null;
			}
			return (View) method.invoke(factory, constructorParams);
		} catch (Throwable e) {
			String eMsg = NLS
				.bind(
					DiagramCoreMessages.AbstractViewProvider_create_view_failed_ERROR_,
					viewClass.getName());
			Log.warning(DiagramPlugin.getInstance(), IStatus.WARNING, eMsg, e);
			return null;
		}
	}

	private Constructor getFactoryConstructor(Class viewClass) {
		if (viewClass != null) {
			Constructor[] ctors = viewClass.getConstructors();
			for (int i = 0; i < ctors.length; i++) {
				if (ctors[i].getParameterTypes().length == 0) {
					return ctors[i];
				}
			}
		}
		return null;
	}

	/** Return the <i>creation</i> constructor for the cached view class. */
	private Method getCreationMethod(Class viewClass, Object[] params) {
		if (viewClass != null) {
			Method method = classToCreateMethod.getCreationMethod(viewClass);
			if (method != null)
				return method;

			Method[] methods = viewClass.getMethods();
			for (int i = 0; i < methods.length; i++) {
				method = methods[i];
				String methodName = method.getName();
				if (methodName.equals(viewCreationMethodName)
					|| methodName.equals(diagramCreationMethodName)) {
					classToCreateMethod.addMethod(viewClass, method);
					return method;
				}
			}
		}
		return null;
	}

	/**
	 * Returns the EClass associated with the semantic adapter
	 * 
	 * @param semanticAdapter
	 * @return EClass
	 */
	protected EClass getSemanticEClass(IAdaptable semanticAdapter) {
		if (semanticAdapter == null)
			return null;
		EObject eObject = (EObject) semanticAdapter.getAdapter(EObject.class);
		if (eObject != null)
			return EMFCoreUtil.getProxyClass(eObject);
		IElementType type = (IElementType) semanticAdapter
			.getAdapter(IElementType.class);
		if (type != null)
			return type.getEClass();
		return null;
	}

	/**
	 * Returns the semantic element associated with the semantic adapter
	 * 
	 * @param semanticAdapter
	 * @return EClass
	 */
	protected EObject getSemanticElement(IAdaptable semanticAdapter) {
		if (semanticAdapter == null)// TODO which MEditingDomain to use?
			return null;
		EObject eObject = (EObject) semanticAdapter.getAdapter(EObject.class);
		if (eObject != null)
			return EMFCoreUtil.resolve(TransactionUtil.getEditingDomain(eObject), eObject);
		return null;
	}

	/**
	 * Returns the semantic element associated with the semantic adapter
	 * 
	 * @param semanticAdapter
	 * @return EClass
	 */
	protected EObject getSemanticElement(IAdaptable semanticAdapter,
			TransactionalEditingDomain domain) {
		if (semanticAdapter == null)
			return null;
		EObject eObject = (EObject) semanticAdapter.getAdapter(EObject.class);
		if (eObject != null)
			return EMFCoreUtil.resolve(domain, eObject);
		return null;
	}

}
