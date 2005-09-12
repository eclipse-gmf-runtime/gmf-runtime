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


package org.eclipse.gmf.tests.runtime.diagram.ui.util;

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;


/**
 * A simple editor for using in JUnit tests that require UI interaction.
 *
 * @author Christian W. Damus (cdamus)
 */
public class DiagramTestEditor
	extends DiagramEditor {

	/** The editor ID for use in opening the editor. */
	public static final String ID = "org.eclipse.gmf.tests.runtime.diagram.ui.DiagramTestEditor"; //$NON-NLS-1$
	
	public void doSave(IProgressMonitor monitor) {
		// no need to implement this
	}

	public void doSaveAs() {
		// no need to implement this
	}

	public boolean isDirty() {
		return false;
	}

	public boolean isSaveAsAllowed() {
		return false;
	}

}
