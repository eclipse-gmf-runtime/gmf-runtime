/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.tests.runtime.diagram.ui.commands;

import org.eclipse.core.resources.IFile;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.internal.services.view.ViewService;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUtil;
import org.eclipse.gmf.runtime.emf.core.util.OperationUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tests.runtime.diagram.ui.framework.DiagramTestCase;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.PresentationTestsViewProvider;


/**
 * This test fixture can be used to test various presentation commands
 *
 * @author jschofie
 */
public abstract class CommandTestFixture extends DiagramTestCase {

	protected ICommand command;
	protected Diagram diagramView;
	protected View noteView;
	
	/**
	 * Constructor
	 */
	public CommandTestFixture() {
		super("CommandTestFixture"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.tests.runtime.diagram.ui.framework.DiagramTestCase#createDiagram()
	 */
	protected IFile createDiagram()
		throws Exception {

		OperationUtil.runAsUnchecked(new Runnable() {

			public void run() {
				Diagram diagram = DiagramUtil.createDiagram(null,
					PresentationTestsViewProvider.PRESENTATION_TESTS_DIAGRAM_KIND, PreferencesHint.USE_DEFAULTS); //$NON-NLS-1$
				diagramView = diagram;
			}
		});

		return null;
	}

	protected View createView() {
		final View []toCreate = new View[1];
				
		OperationUtil.runAsUnchecked(new Runnable() {

			public void run() {
				View view = ViewService.getInstance().createNode(null,diagramView,"Note",0,false, PreferencesHint.USE_DEFAULTS); //$NON-NLS-1$
				toCreate[0] = view;
			}
		});

		return toCreate[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.tests.runtime.diagram.ui.framework.DiagramTestCase#createShapesAndConnectors()
	 */
	protected void createShapesAndConnectors()
		throws Exception {

		noteView = createView();
		command = createCommand();
		assertNotNull("Failed to create command", command); //$NON-NLS-1$
	}
	
	protected ICommand getCommand() {
		return command;
	}

	protected abstract ICommand createCommand();

	public abstract void testDoExecute();

	public void testIsUndoable() {
		assertTrue(getCommand().isUndoable());
	}

	public void testIsRedoable() {
		assertTrue(getCommand().isRedoable());
	}
	
	protected Diagram getDiagram() {
		return diagramView;
	}
}
