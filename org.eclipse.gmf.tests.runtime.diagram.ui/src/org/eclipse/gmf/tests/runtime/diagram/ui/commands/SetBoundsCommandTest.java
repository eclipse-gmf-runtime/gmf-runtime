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
		return new SetBoundsCommand(
			"SetBounds", new EObjectAdapter(noteView), new Rectangle(0, 0, WIDTH, HEIGHT)); //$NON-NLS-1$
	}

	public void testDoExecute() {
		assertTrue(getCommand().isExecutable());
	
		assertEquals( new Integer(-1),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getSize_Width()));
		assertEquals( new Integer(-1),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getSize_Height()));
		
		getCommand().execute(new NullProgressMonitor());
		
		assertEquals( new Integer(WIDTH),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getSize_Width()));
		assertEquals( new Integer(HEIGHT),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getSize_Height()));
	}
	
	public void testMove() {
		ICommand moveCommand = new SetBoundsCommand(
			"SetBounds Move Test",new EObjectAdapter(noteView), new Point(XPOS, YPOS)); //$NON-NLS-1$
		
		assertTrue(moveCommand.isExecutable());
		
		assertEquals( new Integer(0),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getLocation_X()));
		assertEquals( new Integer(0),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getLocation_Y()));
			
		moveCommand.execute(new NullProgressMonitor());
			
		assertEquals( new Integer(XPOS),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getLocation_X()));
		assertEquals( new Integer(YPOS),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getLocation_Y()));
		assertEquals( new Integer(-1),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getSize_Width()));
		assertEquals( new Integer(-1),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getSize_Height()));
	}
	
	public void testResize() {
		ICommand resizeCommand = new SetBoundsCommand(
			"SetBounds Move Test",new EObjectAdapter(noteView), new Dimension(WIDTH, HEIGHT)); //$NON-NLS-1$
		
		assertTrue(resizeCommand.isExecutable());
		
		assertEquals( new Integer(-1),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getSize_Width()));
		assertEquals( new Integer(-1),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getSize_Height()));
			
		resizeCommand.execute(new NullProgressMonitor());
			
		assertEquals( new Integer(WIDTH),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getSize_Width()));
		assertEquals( new Integer(HEIGHT),
			ViewUtil.getStructuralFeatureValue(noteView,NotationPackage.eINSTANCE.getSize_Height()));
	}
}