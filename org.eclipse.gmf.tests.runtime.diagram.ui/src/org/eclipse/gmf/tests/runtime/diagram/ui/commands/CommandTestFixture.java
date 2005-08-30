/***************************************************************************
 * Licensed Materials - Property of IBM
 * (C) Copyright IBM Corp. 2004.  All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 **************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.commands;

import org.eclipse.core.resources.IFile;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.internal.services.view.ViewService;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.tests.runtime.diagram.ui.framework.DiagramTestCase;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.PresentationTestsViewProvider;
import org.eclipse.gmf.runtime.emf.core.util.OperationUtil;
import com.ibm.xtools.notation.Diagram;
import com.ibm.xtools.notation.View;


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
				Diagram diagram = ViewService.getInstance().createDiagramView(null,
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
				View view = ViewService.getInstance().createNodeView(null,diagramView,"Note",0,false, PreferencesHint.USE_DEFAULTS); //$NON-NLS-1$
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
