/******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.diagram.ui.logic;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.CircuitEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.LEDEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.LogicGateEditPart;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.NoteEditPart;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.type.GeoshapeType;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.requests.ArrangeRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequestFactory;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.type.DiagramNotationType;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.tests.runtime.diagram.ui.AbstractTestBase;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.AbstractPresentationTestFixture;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;


/**
 * Tests the canonical editpolicies installed on the class attribute and 
 * operation list compartments.
 * @author mhanner
 */
public class LogicCreationTests extends AbstractTestBase {

	/**
	 * Defines the statechart diagram test suite.
	 * 
	 * @return the test suite.
	 */
	public static Test suite() {
		TestSuite s = new TestSuite(LogicCreationTests.class);
		return s;
	}
	
	/** Create an instance. */
	public LogicCreationTests() {
		super("Logic Shape Creation Test Suite");//$NON-NLS-1$
	}

	/** installs the composite state test fixture. */
	protected void setTestFixture() {
		testFixture = new LogicTestFixture();
	}
	
	/** Return <code>(CanonicalTestFixture)getTestFixture();</code> */
	protected AbstractPresentationTestFixture getLogicTestFixture() {
		return (AbstractPresentationTestFixture)getTestFixture();
	}
	
	public void test_createLogicShapes() {
		Rectangle rect = new Rectangle(getDiagramEditPart().getFigure().getBounds());
		getDiagramEditPart().getFigure().translateToAbsolute(rect);
		IElementType typeLED = ElementTypeRegistry.getInstance().getType("logic.led"); //$NON-NLS-1$
		IElementType typeCircuit = ElementTypeRegistry.getInstance().getType("logic.circuit"); //$NON-NLS-1$
		IElementType typeAndGate = ElementTypeRegistry.getInstance().getType("logic.andgate"); //$NON-NLS-1$
		IElementType typeXorGate = ElementTypeRegistry.getInstance().getType("logic.xorgate"); //$NON-NLS-1$
		IElementType typeFlowContainer = ElementTypeRegistry.getInstance().getType("logic.flowcontainer"); //$NON-NLS-1$
		IElementType typeOrGate = ElementTypeRegistry.getInstance().getType("logic.orgate"); //$NON-NLS-1$
		
		Point createPt = new Point(100, 100);
		LEDEditPart ledEP = (LEDEditPart)getLogicTestFixture().createShapeUsingTool(typeLED, createPt, getDiagramEditPart());
		createPt.getTranslated(ledEP.getFigure().getSize().getExpanded(100, 100));
		
		CircuitEditPart circuitEP = (CircuitEditPart)getLogicTestFixture().createShapeUsingTool(typeCircuit, createPt, getDiagramEditPart());
		createPt.getTranslated(circuitEP.getFigure().getSize().getExpanded(100, 100));
		
		LogicGateEditPart andGateEP = (LogicGateEditPart)getLogicTestFixture().createShapeUsingTool(typeAndGate, createPt, getDiagramEditPart());
		createPt.getTranslated(andGateEP.getFigure().getSize().getExpanded(100, 100));
		
		LogicGateEditPart orGateEP = (LogicGateEditPart)getLogicTestFixture().createShapeUsingTool(typeOrGate, createPt, getDiagramEditPart());
		createPt.getTranslated(orGateEP.getFigure().getSize().getExpanded(100, 100));
		
		LogicGateEditPart xorGateEP = (LogicGateEditPart)getLogicTestFixture().createShapeUsingTool(typeXorGate, createPt, getDiagramEditPart());
		createPt.getTranslated(xorGateEP.getFigure().getSize().getExpanded(100, 100));
		
		getLogicTestFixture().createShapeUsingTool(typeFlowContainer, createPt, getDiagramEditPart());
	}
	
	public void test_bugzilla124678() {
		final Command cc = getLongProgressCommand();
		
		IRunnableWithProgress runnable = new IRunnableWithProgress() {

			public void run(final IProgressMonitor monitor)
				throws InvocationTargetException, InterruptedException {
				
				((DiagramCommandStack)getCommandStack()).execute(cc, monitor);
			}

		};
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(PlatformUI
			.getWorkbench().getActiveWorkbenchWindow().getShell());
		try {
			dialog.run(true, true, runnable);
		} catch (InvocationTargetException e) {
			assertTrue(false);
		} catch (InterruptedException e) {
			assertTrue(false);
		}
	}
	
	private Command getLongProgressCommand() {
		CompoundCommand cc = new CompoundCommand("Add Multiple Octagons"); //$NON-NLS-1$
		ArrayList newViews = new ArrayList();

		EditPart containerEditPart = getDiagramEditPart();
		
		for (int x = 0; x < 500; x = x + 55) {
			for (int y = 0; y < 500; y = y + 55) {
				CreateViewRequest createOctagon = CreateViewRequestFactory
					.getCreateShapeRequest(GeoshapeType.OCTAGON,
						PreferencesHint.USE_DEFAULTS);

				createOctagon.setLocation(new Point(x, y));
				Command createCmd = containerEditPart.getCommand(createOctagon);
				cc.add(createCmd);

				Object obj = createOctagon.getNewObject();
				if (obj instanceof Collection) {
					Iterator iter = ((Collection)obj).iterator();
					while (iter.hasNext()) {
						newViews.add(iter.next());
					}
				}
				else
					newViews.add(createOctagon.getNewObject());
			}
		}

		ArrangeRequest arrangeRequest = new ArrangeRequest(
		RequestConstants.REQ_ARRANGE_DEFERRED);
		arrangeRequest.setViewAdaptersToArrange(newViews);
		Command arrangeCommand = containerEditPart.getCommand(arrangeRequest);
		cc.add(arrangeCommand);

		return cc;
	}
    
    public void test_reorientingNoteAttachments() {
        
        // Add two LEDs.
        IElementType typeLED = ElementTypeRegistry.getInstance().getType("logic.led"); //$NON-NLS-1$        
        LEDEditPart led1 = (LEDEditPart)getLogicTestFixture().createShapeUsingTool(typeLED, new Point(100, 10), getDiagramEditPart());        
        LEDEditPart led2 = (LEDEditPart)getLogicTestFixture().createShapeUsingTool(typeLED, new Point(200, 10), getDiagramEditPart());

        // Add two notes.
        NoteEditPart note1 = (NoteEditPart)getLogicTestFixture().createShapeUsingTool(DiagramNotationType.NOTE, new Point(100, 100), getDiagramEditPart());        
        NoteEditPart note2 = (NoteEditPart)getLogicTestFixture().createShapeUsingTool(DiagramNotationType.NOTE, new Point(200, 100), getDiagramEditPart());

        // Create a note attachment from note1 to led1.
        ConnectionEditPart noteAttachment = getLogicTestFixture()
            .createConnectorUsingTool(note1,
                led1, DiagramNotationType.NOTE_ATTACHMENT);
        
        // Reorient the note attachment to led2.
        reorientConnectionTarget(noteAttachment, led2, true);

        // Reorient the note attachment to note2.
        reorientConnectionSource(noteAttachment, note2, true);
                
        // Test that we cannot reorient a note attachment between two LEDs.
        reorientConnectionSource(noteAttachment, led1, false);
        
        // Now test this all again but creating the note attachment from the LED
        // to the note.
        
        // Create a note attachment from led1 to note1.
        noteAttachment = getLogicTestFixture()
            .createConnectorUsingTool(led1,
                note1, DiagramNotationType.NOTE_ATTACHMENT);
        
        // Reorient the note attachment to led2.
        reorientConnectionSource(noteAttachment, led2, true);

        // Reorient the note attachment to note2.
        reorientConnectionTarget(noteAttachment, note2, true);
                
        // Test that we cannot reorient a note attachment between two LEDs.
        reorientConnectionTarget(noteAttachment, led1, false);
    }

}

