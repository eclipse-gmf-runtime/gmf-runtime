/*
 * Created on Mar 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.gmf.tests.runtime.diagram.ui;

import java.util.Date;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.gef.commands.Command;

import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.requests.ArrangeRequest;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.AnimationFigureHelper;


/**
 * @author sshaw
 *
 * Diagram Tests
 */
public class DiagramTests extends AbstractDiagramTests {

	/**
	 * @param arg0
	 */
	public DiagramTests(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	public static Test suite() {
		return new TestSuite(DiagramTests.class);
	}
	
	protected void setTestFixture() {
		testFixture = new DiagramTestFixture();
	}
	
	
	public void testAlignment()
		throws Exception {
		// do nothing since we can't open an editor for the default diagram case.
	}
	
	public void testSelect()
		throws Exception {
		// do nothing since we can't open an editor for the default diagram case.
	}
	
	public void testAnimatedLayout() throws Exception {
		ArrangeRequest request = new ArrangeRequest(
			ActionIds.ACTION_ARRANGE_ALL);
		Command layoutCmd = getDiagramEditPart().getCommand(request);
		
		assertTrue((layoutCmd != null && layoutCmd.canExecute()));
		getCommandStack().execute(layoutCmd);
		
		long startTime = new Date().getTime();
		AnimationFigureHelper animationFigureHelper = AnimationFigureHelper.getInstance();
		animationFigureHelper.animate(getDiagramEditPart().getFigure());
		long endTime = new Date().getTime();
		
		assertTrue((endTime - startTime) >= animationFigureHelper.getTotalDuration());
	}
}
