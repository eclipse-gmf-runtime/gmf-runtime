/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.examples.runtime.diagram.logic.internal.util;

import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.util.IDEEditorFileCreator;


/**
 * @author qili
 *
 * Class that generates diagram files.
 */
public class LogicDiagramFileCreator extends IDEEditorFileCreator {
	
	private static LogicDiagramFileCreator INSTANCE = new LogicDiagramFileCreator();

	/**
	 * Method getInstance.
	 * This class is a singleton that can only be accessed through this static method.
	 * @return VizDiagramFileCreator The singleton instance
	 */
	static public LogicDiagramFileCreator getInstance() {
		return INSTANCE;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.util.DiagramFileCreator#getExtension()
	 */
	public String getExtension() {
		return ".logic2"; //$NON-NLS-1$
	}

}
