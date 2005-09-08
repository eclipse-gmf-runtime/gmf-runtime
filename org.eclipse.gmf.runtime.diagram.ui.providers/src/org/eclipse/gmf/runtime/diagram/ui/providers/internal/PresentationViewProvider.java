/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.providers.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.ui.services.parser.CommonParserHint;
import org.eclipse.gmf.runtime.diagram.core.providers.AbstractViewProvider;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.BasicNodeViewFactory;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.ConnectorViewFactory;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.NoteViewFactory;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.TextShapeViewFactory;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Supports the creation of <b>presentation</b> view elements.  
 * Presentation elemenst are commonly used by all plugins.
 * 
 * @author schafe
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.providers.*
 */
public class PresentationViewProvider extends AbstractViewProvider {
	
	private static PresentationViewProvider instance;

	public PresentationViewProvider() {
		instance = this;
	}

	/** list of supported shape views. */
	private Map nodeMap = new HashMap();
	{
		nodeMap.put(CommonParserHint.DESCRIPTION, BasicNodeViewFactory.class);
		nodeMap.put(Properties.DIAGRAM_NAME, BasicNodeViewFactory.class);
		nodeMap.put(Properties.NOTE, NoteViewFactory.class);
		nodeMap.put(Properties.TEXT, TextShapeViewFactory.class);
		nodeMap.put(NotationPackage.eINSTANCE.getDiagram(), NoteViewFactory.class);	
	}
	/** list of supported connector views. */
	static private Map connectorMap = new HashMap();
	{
		connectorMap.put(
			Properties.NOTEATTACHMENT,
			ConnectorViewFactory.class);
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
		return (Class)nodeMap.get(getSemanticEClass(semanticAdapter));
	}

	/**
	 * Returns the connector view class to instantiate based on the passed params
	 * @param semanticAdapter
	 * @param containerView
	 * @param semanticHint
	 * @return Class
	 */
	protected Class getConnectorViewClass(
		IAdaptable semanticAdapter,
		View containerView,
		String semanticHint) {
		return (Class) connectorMap.get(semanticHint);
	}
	
	
	public static boolean isNoteView(View view) {
		if ((instance != null) && (view != null)) {
			return (instance.nodeMap.get(view.getType()) instanceof NoteViewFactory);
		}
		return false;
	}
	
	public static boolean isTextView(View view){
		if ((instance != null) && (view != null)) {
			return (instance.nodeMap.get(view.getType()) instanceof TextShapeViewFactory);
		}
		return false;
	}
}
