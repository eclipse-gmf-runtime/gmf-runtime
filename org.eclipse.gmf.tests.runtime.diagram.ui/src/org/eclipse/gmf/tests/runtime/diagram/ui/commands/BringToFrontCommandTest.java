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


package org.eclipse.gmf.tests.runtime.diagram.ui.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.internal.commands.BringToFrontCommand;
import org.eclipse.gmf.runtime.notation.View;


/**
 * Test the BringToFront ZOrder command
 * 
 * @author jschofie
 */
public class BringToFrontCommandTest
	extends CommandTestFixture {

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.tests.runtime.diagram.ui.commands.CommandTestFixture#createCommand()
	 */
	protected ICommand createCommand() {
		return new BringToFrontCommand(getEditingDomain(), noteView);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.tests.runtime.diagram.ui.commands.CommandTestFixture#testDoExecute()
	 */
	public void testDoExecute() {
		assertEquals(getDiagram().getChildren().size(), 1);
		View firstNote  = (View)getDiagram().getChildren().get(0);

		ICommand zorderCommand = new BringToFrontCommand(getEditingDomain(), firstNote);
		
        try {
            zorderCommand.execute(new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail("command execution failure: " + e.getLocalizedMessage()); //$NON-NLS-1$
        }

		assertEquals(getDiagram().getChildren().get(0), firstNote);
	}
	
	public void testFirstEntry() {
		createView();
		assertEquals(getDiagram().getChildren().size(), 2);
		View firstNote  = (View)getDiagram().getChildren().get(0);
		View secondNote = (View)getDiagram().getChildren().get(1);

		ICommand zorderCommand = new BringToFrontCommand(getEditingDomain(), firstNote);
		
        try {
            zorderCommand.execute(new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail("command execution failure: " + e.getLocalizedMessage()); //$NON-NLS-1$
        }

		assertEquals(getDiagram().getChildren().get(0), secondNote);
		assertEquals(getDiagram().getChildren().get(1), firstNote);
	}
	
	public void testMiddleEntry() {
		createView();
		createView();
		assertEquals(getDiagram().getChildren().size(), 3);
		View firstNote  = (View)getDiagram().getChildren().get(0);
		View secondNote = (View)getDiagram().getChildren().get(1);
		View thirdNote  = (View)getDiagram().getChildren().get(2);

		ICommand zorderCommand = new BringToFrontCommand(getEditingDomain(), secondNote);
		
        try {
            zorderCommand.execute(new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail("command execution failure: " + e.getLocalizedMessage()); //$NON-NLS-1$
        }

		assertEquals(getDiagram().getChildren().get(0), firstNote);
		assertEquals(getDiagram().getChildren().get(1), thirdNote);
		assertEquals(getDiagram().getChildren().get(2), secondNote);
	}

}
