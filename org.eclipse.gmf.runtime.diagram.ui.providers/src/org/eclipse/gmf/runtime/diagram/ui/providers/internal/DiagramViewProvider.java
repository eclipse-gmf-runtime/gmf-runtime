/******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.providers.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gmf.runtime.common.ui.services.parser.CommonParserHint;
import org.eclipse.gmf.runtime.diagram.core.providers.AbstractViewProvider;
import org.eclipse.gmf.runtime.diagram.core.util.ViewType;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.NoteAttachmentViewFactory;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.NoteViewFactory;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.TextShapeViewFactory;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.optimal.BasicDecorationViewFactory;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Supports the creation of <b>diagram</b> view elements.  
 * Diagram elements are commonly used by all plugins.
 * 
 * @author schafe, cmahoney
 */
public class DiagramViewProvider extends AbstractViewProvider {

	/** list of supported shape views. */
    static private final Map nodeMap = new HashMap();
	static {
		nodeMap.put(CommonParserHint.DESCRIPTION, BasicDecorationViewFactory.class);
		nodeMap.put(ViewType.DIAGRAM_NAME, BasicDecorationViewFactory.class);
		nodeMap.put(ViewType.NOTE, NoteViewFactory.class);
		nodeMap.put(ViewType.TEXT, TextShapeViewFactory.class);
	}
	/** list of supported connection views. */
	static private final Map connectionMap = new HashMap();
    static {
		connectionMap.put(
			ViewType.NOTEATTACHMENT,
			NoteAttachmentViewFactory.class);
	}

	/**
	 * Returns the shape view class to instantiate based on the passed params
	 * @param semanticAdapter
	 * @param containerView
	 * @param semanticHint
	 * @return Class
	 */
	protected Class getNodeViewClass(
		IAdaptable semanticAdapter,
		View containerView,
		String semanticHint) {
		if (semanticHint != null && semanticHint.length() > 0)
			return (Class)nodeMap.get(semanticHint);
		EClass semanticEClass = getSemanticEClass(semanticAdapter);
		if (NotationPackage.eINSTANCE.getDiagram().isSuperTypeOf(semanticEClass)) {
			return NoteViewFactory.class;
		}
		return (Class)nodeMap.get(semanticEClass);
	}

	/**
	 * Returns the connection view class to instantiate based on the passed params
	 * @param semanticAdapter
	 * @param containerView
	 * @param semanticHint
	 * @return Class
	 */
	protected Class getEdgeViewClass(
		IAdaptable semanticAdapter,
		View containerView,
		String semanticHint) {
		return (Class) connectionMap.get(semanticHint);
	}
	
	public static boolean isNoteView(View view) {
		if ((view != null)) {
			return (NoteViewFactory.class.equals(nodeMap.get(view.getType())));
		}
		return false;
	}

    public static boolean isTextView(View view){
		if ((view != null)) {
			return (TextShapeViewFactory.class.equals(nodeMap.get(view.getType())));
		}
		return false;
	}
}
