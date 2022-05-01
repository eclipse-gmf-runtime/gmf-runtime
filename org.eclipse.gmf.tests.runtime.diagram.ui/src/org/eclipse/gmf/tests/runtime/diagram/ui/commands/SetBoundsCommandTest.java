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
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.SetBoundsCommand;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.NotationPackage;

/**
 * @author jschofie
 */
public class SetBoundsCommandTest
	extends CommandTestFixture {

	private int XPOS = 500;
	private int YPOS = 500;
	private int WIDTH = 50;
	private int HEIGHT = 50;

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.tests.runtime.diagram.ui.commands.CommandTestFixture#createCommand()
	 */
	protected ICommand createCommand() {
		return new SetBoundsCommand(getEditingDomain(), 
			"SetBounds", new EObjectAdapter(noteView), new Rectangle(0, 0, WIDTH, HEIGHT)); //$NON-NLS-1$
	}

	public void testDoExecute() {
		assertTrue(getCommand().canExecute());
	
		assertEquals( Integer.valueOf(-1),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getSize_Width()));
		assertEquals( Integer.valueOf(-1),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getSize_Height()));
		
        try {
            getCommand().execute(new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e.getLocalizedMessage());
        }
		
		assertEquals( Integer.valueOf(WIDTH),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getSize_Width()));
		assertEquals( Integer.valueOf(HEIGHT),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getSize_Height()));
	}
	
	public void testMove() {
		ICommand moveCommand = new SetBoundsCommand(getEditingDomain(),
			"SetBounds Move Test",new EObjectAdapter(noteView), new Point(XPOS, YPOS)); //$NON-NLS-1$
		
		assertTrue(moveCommand.canExecute());
		
		assertEquals( Integer.valueOf(0),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getLocation_X()));
		assertEquals( Integer.valueOf(0),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getLocation_Y()));
			
        try {
            moveCommand.execute(new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e.getLocalizedMessage());
        }
			
		assertEquals( Integer.valueOf(XPOS),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getLocation_X()));
		assertEquals( Integer.valueOf(YPOS),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getLocation_Y()));
		assertEquals( Integer.valueOf(-1),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getSize_Width()));
		assertEquals( Integer.valueOf(-1),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getSize_Height()));
	}
	
	public void testResize() {
		ICommand resizeCommand = new SetBoundsCommand(getEditingDomain(), 
			"SetBounds Move Test",new EObjectAdapter(noteView), new Dimension(WIDTH, HEIGHT)); //$NON-NLS-1$
		
		assertTrue(resizeCommand.canExecute());
		
		assertEquals( Integer.valueOf(-1),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getSize_Width()));
		assertEquals( Integer.valueOf(-1),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getSize_Height()));
			
        try {
            resizeCommand.execute(new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            fail(e.getLocalizedMessage());
        }
			
		assertEquals( Integer.valueOf(WIDTH),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getSize_Width()));
		assertEquals( Integer.valueOf(HEIGHT),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getSize_Height()));
	}
}