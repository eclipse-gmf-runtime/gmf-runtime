/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

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
