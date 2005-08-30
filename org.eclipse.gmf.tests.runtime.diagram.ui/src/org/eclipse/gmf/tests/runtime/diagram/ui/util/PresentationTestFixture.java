/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.tests.runtime.diagram.ui.util;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUtil;
import org.eclipse.gmf.runtime.emf.core.exceptions.MSLActionAbandonedException;
import org.eclipse.gmf.runtime.emf.core.util.OperationUtil;


/**
 * This is a test fixture for presentation. It creates a non-UML diagram and a
 * diagram editpart. It does not create a project or add any shapes or
 * connectors to the diagram. This class can be subclassed or used as is.
 * 
 * @author cmahoney
 */
public class PresentationTestFixture
	extends AbstractPresentationTestFixture {

	/**
	 * @see org.eclipse.gmf.tests.runtime.diagram.ui.util.AbstractPresentationTestFixture#createProject()
	 */
	protected void createProject()
		throws Exception {

		// Do nothing. Override if a project is required.

	}

	/**
	 * @see org.eclipse.gmf.tests.runtime.diagram.ui.util.AbstractPresentationTestFixture#createDiagram()
	 */
	protected void createDiagram()
		throws Exception {

		OperationUtil.runInUndoInterval(new Runnable() {

			public void run() {
				try {
					OperationUtil.runAsWrite(new Runnable() {

						public void run() {
							setDiagram(DiagramUtil
								.createDiagram(
									null,
									PresentationTestsViewProvider.PRESENTATION_TESTS_DIAGRAM_KIND,
									getPreferencesHint()));
						}
					});
				} catch (MSLActionAbandonedException e) {
					Trace.trace(DiagramUIPlugin.getInstance(),
						DiagramUIDebugOptions.EXCEPTIONS_CATCHING,
						"MSLActionAbandonedException"); //$NON-NLS-1$
					assertFalse(false);
				}
			}
		});
	}

	/**
	 * @see org.eclipse.gmf.tests.runtime.diagram.ui.util.AbstractPresentationTestFixture#createShapesAndConnectors()
	 */
	protected void createShapesAndConnectors()
		throws Exception {

		// Override to create shapes and connectors.

	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.tests.runtime.diagram.ui.util.IPresentationTestFixture#getPreferencesHint()
	 */
	public PreferencesHint getPreferencesHint() {
		return PreferencesHint.USE_DEFAULTS;
	}
}