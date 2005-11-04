/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.decorator.provider;

import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import org.eclipse.gmf.examples.runtime.diagram.decorator.DecoratorPlugin;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoration;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecorator;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget;
import org.eclipse.gmf.runtime.emf.core.IOperationEvent;
import org.eclipse.gmf.runtime.emf.core.OperationListener;
import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.notation.DescriptionStyle;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author sshaw
 * 
 * Example provider for the Decorator service that will selectively decorate
 * depending on what the content of the description style field is.
 * 
 * The example will annotate a node view with an icon on the top middle of the shape
 * depending on the following conditions:
 * 
 * 1. If the description style contains the word "Passed" a checkbox will appear on top of the shape
 * 2. If the description style contains the word "Failed" an error box will appear on top of the shape
 * 3. If the description style doesn't meet the conditions in (1.) or (2.) then no decoration will appear.
 */
public class ReviewDecorator implements IDecorator { 

	/** the object to be decorated */
	private IDecoratorTarget decoratorTarget;

	/** the decoration being displayed */
	private IDecoration decoration;

	private static final Image ICON_FAILED;

	private static final Image ICON_PASSED;

	static {
		/*
		 * prefix path with "$nl$" and use Plugin.find() to search for the
		 * locale specific file
		 */
		IPath path = new Path("$nl$").append( //$NON-NLS-1$
				"icons//failed.gif"); //$NON-NLS-1$
		URL url = DecoratorPlugin.getDefault().find(path);
		ImageDescriptor imgDesc = ImageDescriptor.createFromURL(url);
		ICON_FAILED = imgDesc.createImage();

		path = new Path("$nl$").append( //$NON-NLS-1$
				"icons//passed.gif"); //$NON-NLS-1$
		url = DecoratorPlugin.getDefault().find(path);
		imgDesc = ImageDescriptor.createFromURL(url);
		ICON_PASSED = imgDesc.createImage();
	}

	/**
	 * Creates a new <code>AbstractDecorator</code> for the decorator target
	 * passed in.
	 * 
	 * @param decoratorTarget
	 *            the object to be decorated
	 */
	public ReviewDecorator(IDecoratorTarget decoratorTarget) {
		this.decoratorTarget = decoratorTarget;
	}

	/**
	 * Gets the object to be decorated.
	 * 
	 * @return Returns the object to be decorated
	 */
	protected IDecoratorTarget getDecoratorTarget() {
		return decoratorTarget;
	}

	/**
	 * @return Returns the decoration.
	 */
	public IDecoration getDecoration() {
		return decoration;
	}

	/**
	 * @param decoration
	 *            The decoration to set.
	 */
	public void setDecoration(IDecoration decoration) {
		this.decoration = decoration;
	}

	/**
	 * Removes the decoration if it exists and sets it to null.
	 */
	protected void removeDecoration() {
		if (decoration != null) {
			decoratorTarget.removeDecoration(decoration);
			decoration = null;
		}
	}

	/**
	 * getDecoratorTargetClassifier Utility method to determine if the
	 * decoratorTarget is a supported type for this decorator and return the
	 * associated Classifier element.
	 * 
	 * @param decoratorTarget
	 *            IDecoratorTarget to check and return valid Classifier target.
	 * @return node Node if IDecoratorTarget can be supported, null
	 *         otherwise.
	 */
	static public Node getDecoratorTargetNode(
			IDecoratorTarget decoratorTarget) {
		DescriptionStyle descStyle = null;
		View node = (View) decoratorTarget.getAdapter(View.class);
		if (node != null && node.eContainer() instanceof Diagram) { 
			descStyle = (DescriptionStyle)node.getStyle(NotationPackage.eINSTANCE.getDescriptionStyle());			 
			
			if (descStyle != null) {
				return (Node)node;
			}
		}
		return null;

	}


	/**
	 * Creates the appropriate review decoration if all the criteria is
	 * satisfied by the view passed in.
	 */
	public void refresh() {
		removeDecoration();

		Node node = getDecoratorTargetNode(getDecoratorTarget());

		if (node != null) {
			DescriptionStyle descStyle = getDescriptionStyle(node);
			if (descStyle != null) {
				boolean passed = descStyle.getDescription().matches("Passed*"); //$NON-NLS-1$
				boolean failed = descStyle.getDescription().matches("Failed*"); //$NON-NLS-1$
				if (passed || failed)
					setDecoration(getDecoratorTarget().addShapeDecoration(
							passed ? ICON_PASSED : ICON_FAILED,
							IDecoratorTarget.Direction.NORTH, 75, false));
			}
		}
	}

	/**
	 * getDescriptionStyle
	 * Accessor to retrieve the description style from a Node.
	 * 
	 * @param node Node to retrieve the description style from.
	 * @return DescriptionStyle style object
	 */
	private DescriptionStyle getDescriptionStyle(Node node) {
		return (DescriptionStyle)node.getStyle(NotationPackage.eINSTANCE.getDescriptionStyle());			 
	}

	private OperationListener operationListener = new OperationListener() {
		public void done(IOperationEvent event) {
			refresh();
		}

		public void redone(IOperationEvent event) {
			refresh();
		}

		public void undone(IOperationEvent event) {
			refresh();
		}
	};
	
	/**
	 * Adds decoration if applicable.
	 */
	public void activate() {
		MSLEditingDomain.INSTANCE.addOperationListener(operationListener);
	}

	/**
	 * Removes the decoration.
	 */
	public void deactivate() {
		removeDecoration();

		MSLEditingDomain.INSTANCE.removeOperationListener(operationListener);
	}
	
}