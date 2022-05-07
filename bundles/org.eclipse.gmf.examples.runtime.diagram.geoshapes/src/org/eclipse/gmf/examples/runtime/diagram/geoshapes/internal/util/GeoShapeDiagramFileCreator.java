/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal.util;

import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.util.IDEEditorFileCreator;


/**
 * @author qili
 *
 * Class that generates diagram files.
 */
public class GeoShapeDiagramFileCreator extends IDEEditorFileCreator {
	
	private static GeoShapeDiagramFileCreator INSTANCE = new GeoShapeDiagramFileCreator();

	/**
	 * Method getInstance.
	 * This class is a singleton that can only be accessed through this static method.
	 * @return VizDiagramFileCreator The singleton instance
	 */
	static public GeoShapeDiagramFileCreator getInstance() {
		return INSTANCE;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.util.DiagramFileCreator#getExtension()
	 */
	public String getExtension() {
		return ".geo"; //$NON-NLS-1$
	}

}
