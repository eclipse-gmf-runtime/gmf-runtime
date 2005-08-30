/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal.util;

import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.util.IDEEditorFileCreator;


/**
 * @author qili
 *
 * Class that generates diagram files.
 */
public class DiagramFileCreator extends IDEEditorFileCreator{
	
	private static DiagramFileCreator INSTANCE = new DiagramFileCreator();

	/**
	 * Method getInstance.
	 * This class is a singleton that can only be accessed through this static method.
	 * @return VizDiagramFileCreator The singleton instance
	 */
	static public DiagramFileCreator getInstance() {
		return INSTANCE;
	}
	
	/**
	 * @see com.ibm.xtools.uml.ui.diagram.internal.util.AbstractUMLDiagramFileCreator#getExtension()
	 */
	public String getExtension() {
		return ".geo"; //$NON-NLS-1$
	}

}
