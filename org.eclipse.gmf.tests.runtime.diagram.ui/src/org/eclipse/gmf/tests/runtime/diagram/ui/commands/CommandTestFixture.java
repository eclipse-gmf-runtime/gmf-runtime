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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
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

		AbstractEMFOperation operation = new AbstractEMFOperation(
			getEditingDomain(), "") { //$NON-NLS-1$

			protected IStatus doExecute(IProgressMonitor monitor,
					IAdaptable info)
				throws ExecutionException {
				
				Diagram diagram = ViewService.createDiagram(
					PresentationTestsViewProvider.PRESENTATION_TESTS_DIAGRAM_KIND, PreferencesHint.USE_DEFAULTS);
				diagramView = diagram;
                setDiagram(diagram);
			
				return Status.OK_STATUS;
			};
		};
		try {
			OperationHistoryFactory.getOperationHistory().execute(operation,
					new NullProgressMonitor(), null);
		} catch (ExecutionException e) {
			e.printStackTrace();
			assertFalse(false);
		}
//		MEditingDomain.INSTANCE.runAsUnchecked(new MRunnable() {
//
//			public Object run() {
//				Diagram diagram = ViewService.createDiagram(
//					PresentationTestsViewProvider.PRESENTATION_TESTS_DIAGRAM_KIND, PreferencesHint.USE_DEFAULTS);
//				diagramView = diagram;
//                setDiagram(diagram);
//				return null;
//			}
//		});

		return null;
	}

	protected View createView() {
		final View []toCreate = new View[1];
		
		AbstractEMFOperation operation = new AbstractEMFOperation(
			getEditingDomain(), "") { //$NON-NLS-1$

			protected IStatus doExecute(IProgressMonitor monitor,
					IAdaptable info)
				throws ExecutionException {
				
				View view = ViewService.getInstance().createNode(null,diagramView,"Note",0,false, PreferencesHint.USE_DEFAULTS); //$NON-NLS-1$
				toCreate[0] = view;
				
				return Status.OK_STATUS;
			};
		};
		try {
			OperationHistoryFactory.getOperationHistory().execute(operation,
					new NullProgressMonitor(), null);
		} catch (ExecutionException e) {
			e.printStackTrace();
			assertFalse(false);
		}
//			MEditingDomain.INSTANCE.runAsUnchecked(new MRunnable() {
//
//			public Object run() {
//				View view = ViewService.getInstance().createNode(null,diagramView,"Note",0,false, PreferencesHint.USE_DEFAULTS); //$NON-NLS-1$
//				toCreate[0] = view;
//				return null;
//			}
//		});

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
	
	protected Diagram getDiagram() {
		return diagramView;
	}

}
