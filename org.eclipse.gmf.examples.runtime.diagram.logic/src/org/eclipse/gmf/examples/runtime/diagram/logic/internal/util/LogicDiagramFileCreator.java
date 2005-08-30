/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.util;

import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.util.IDEEditorFileCreator;


/**
 * @author qili
 *
 * Class that generates diagram files.
 */
public class LogicDiagramFileCreator extends IDEEditorFileCreator{
	
	private static LogicDiagramFileCreator INSTANCE = new LogicDiagramFileCreator();

	/**
	 * Method getInstance.
	 * This class is a singleton that can only be accessed through this static method.
	 * @return VizDiagramFileCreator The singleton instance
	 */
	static public LogicDiagramFileCreator getInstance() {
		return INSTANCE;
	}
	
	/**
	 * @see com.ibm.xtools.uml.ui.diagram.internal.util.AbstractUMLDiagramFileCreator#getExtension()
	 */
	public String getExtension() {
		return ".logic2"; //$NON-NLS-1$
	}

}
