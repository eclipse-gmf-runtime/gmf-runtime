/******************************************************************************
 * Copyright (c) 2005 2006 IBM Corporation and others.
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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.CircuitEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.LEDEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.TerminalEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers.LogicConstants;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.AndGate;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Circuit;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.ContainerElement;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.LED;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.SemanticPackage;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Terminal;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.figures.ICanonicalShapeCompartmentLayout;
import org.eclipse.gmf.runtime.diagram.ui.internal.actions.ZoomContributionItem;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.render.actions.CopyToImageAction;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.tests.runtime.diagram.ui.AbstractDiagramTests;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorSite;

/**
 * Diagram tests for the logic diagram and general diagrams.
 * 
 * @author cmahoney
 */
public class LogicDiagramTests
	extends AbstractDiagramTests {

	public LogicDiagramTests(String arg0) {
		super(arg0);
	}

	public static Test suite() {
		return new TestSuite(LogicDiagramTests.class);
	}

	protected void setTestFixture() {
		testFixture = new LogicTestFixture();
	}

	/** Return <code>(LogicTestFixture)getTestFixture();</code> */
	protected LogicTestFixture getLogicTestFixture() {
		return (LogicTestFixture)getTestFixture();
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		
		List children = getTestFixture().getDiagramEditPart().getChildren();
		if (children.isEmpty())
			assertFalse(true);
		
		EditPart firstEP = (EditPart)children.get(0);
		if (firstEP instanceof CircuitEditPart ) {
			CircuitEditPart circuitEditPart = (CircuitEditPart)firstEP;
			
			IElementType typeLED = ElementTypeRegistry.getInstance().getType("logic.led"); //$NON-NLS-1$
			Point pos = circuitEditPart.getFigure().getBounds().getBottomRight();
			circuitEditPart.getFigure().translateToAbsolute(pos);
			pos.translate(100, 100);
			LEDEditPart ledEP2 = (LEDEditPart)getLogicTestFixture().createShapeUsingTool(typeLED, pos, getDiagramEditPart());
			
			Terminal term1 = (Terminal)((Circuit)circuitEditPart.getNotationView().getElement()).getOutputTerminals().get(0);
			TerminalEditPart tep1 = null;
			ListIterator li = circuitEditPart.getChildren().listIterator();
			while (li.hasNext()) {
				IGraphicalEditPart gep = (IGraphicalEditPart)li.next();
				if (gep.getNotationView().getElement().equals(term1))
					tep1 = (TerminalEditPart)gep;
			}
			
			Terminal term2 = (Terminal)((LED)ledEP2.getNotationView().getElement()).getInputTerminals().get(0);
			TerminalEditPart tep2 = null;
			li = ledEP2.getChildren().listIterator();
			while (li.hasNext()) {
				IGraphicalEditPart gep = (IGraphicalEditPart)li.next();
				if (gep.getNotationView().getElement().equals(term2))
					tep2 = (TerminalEditPart)gep;
			}
			
			IElementType typeWire = ElementTypeRegistry.getInstance().getType("logic.wire"); //$NON-NLS-1$
						
			getLogicTestFixture().createConnectorUsingTool(tep1, tep2, typeWire);
			
			IGraphicalEditPart logicCompartment = circuitEditPart.getChildBySemanticHint(LogicConstants.LOGIC_SHAPE_COMPARTMENT);
			
			Rectangle rect = new Rectangle(logicCompartment.getFigure().getBounds());
			logicCompartment.getFigure().translateToAbsolute(rect);
			
			CreateRequest request = getLogicTestFixture().getCreationRequest(typeLED);
			request.setLocation(rect.getCenter());
			Command cmd = logicCompartment.getCommand(request);

			getCommandStack().execute(cmd);
			
			assertEquals( "Unexpected LED count.", 1, logicCompartment.getChildren().size() );//$NON-NLS-1$
		}
	}

	public void xtestZoomDoesntDirtyDiagram() throws Exception {
		getTestFixture().openDiagram();

	       ZoomManager zoomManager = getZoomManager();
	       // Ensure the zoom manager exists
	        assertTrue(zoomManager != null);
	        
	        this.saveDiagram();
	        
	        // Change to the another zoom level
	        if (zoomManager.canZoomIn()) {
	            zoomManager.setZoom(zoomManager.getNextZoomLevel());
	        } else {
	            zoomManager.setZoom(zoomManager.getPreviousZoomLevel());
	        }
	        
	        assertTrue(false == isDirty());                               
		}

	public void xtestSelectAllInContext() throws Exception {
		List children = getTestFixture().getDiagramEditPart().getChildren();
		if (children.isEmpty())
			assertFalse(true);
		
		CircuitEditPart circuitEP = null;
		ListIterator li = children.listIterator();
		while (li.hasNext()) {
			EditPart ep = (EditPart)li.next();
			if (ep instanceof CircuitEditPart ) {
				circuitEP = (CircuitEditPart)ep;
				
				// select the logic compartment as a target
				IGraphicalEditPart logicCompartment = circuitEP.getChildBySemanticHint(LogicConstants.LOGIC_SHAPE_COMPARTMENT);
				
				final List shapes = getSelectableShapesIn(logicCompartment);
				final List all = new ArrayList();
				all.addAll(shapes);
				
				selectAll(logicCompartment, shapes);
			}
		}
		
		final List connectors = getConnectors();
		final List shapes = getSelectableShapesIn(getDrawSurfaceEditPart());
		final List all = new ArrayList();
		all.addAll(connectors);
		all.addAll(shapes);
		
		selectAll(circuitEP, all);
	}

	/**
	 * Tests the initial enablement of the zoom toolbar entry. See Bugzilla
	 * 110815.
	 * 
	 * @throws Exception
	 */
	public void xtestZoomToolbarEnablement()
		throws Exception {

		getTestFixture().openDiagram();

		IContributionItem[] items = ((IEditorSite) getDiagramWorkbenchPart()
			.getSite()).getActionBars().getToolBarManager().getItems();
		boolean foundIt = false;
		for (int i = 0; i < items.length; i++) {
			IContributionItem item = items[i];
			if (item instanceof ZoomContributionItem) {
				foundIt = true;
				assertTrue(item.isEnabled());
			}
		}
		assertTrue(foundIt);
	}
	
	/**
	 * Tests the CTRL-D keystroke which initiates a delete from model action.
	 * See Bugzilla 115108.
	 */
	public void xtestDeleteFromModel()
		throws Exception {

		getTestFixture().openDiagram();

		List primaryEditParts = getDiagramEditPart().getPrimaryEditParts();
		int initialCount = primaryEditParts.size();

		if (initialCount < 1) {
			fail("Test requires at least one edit part on the diagram"); //$NON-NLS-1$
		}

		// Get the element to be deleted.
		IGraphicalEditPart editPartToDelete = (IGraphicalEditPart) primaryEditParts
			.get(0);
		EObject semanticElement = (EObject) editPartToDelete
			.getAdapter(EObject.class);
		EObject semanticContainer = semanticElement.eContainer();

		// Select the edit part to be deleted.
		EditPartViewer rootViewer = getDiagramEditPart().getRoot().getViewer();
		rootViewer.deselectAll();
		rootViewer.select(editPartToDelete);

		// Set the preference to not confirm the element deletion.
		((IPreferenceStore) getDiagramEditPart().getDiagramPreferencesHint()
			.getPreferenceStore()).setValue(
			IPreferenceConstants.PREF_PROMPT_ON_DEL_FROM_MODEL, false);

		// Create the CTRL-D event
		Event e = new Event();
		e.character = (char) 0x4;
		e.keyCode = 100;
		e.stateMask = SWT.CTRL;
		e.widget = editPartToDelete.getViewer().getControl();

		// Simulate the CTRL-D keystroke
		SelectionTool tool = new SelectionTool();
		tool.setEditDomain((EditDomain) getDiagramWorkbenchPart()
			.getDiagramEditDomain());
		tool.activate();
		tool.keyDown(new KeyEvent(e), rootViewer);

		// Verify that the edit part and the semantic element have been deleted.
		primaryEditParts = getDiagramEditPart().getPrimaryEditParts();

		assertTrue(
			"Size of primary edit parts should have decreased.", primaryEditParts.size() < initialCount); //$NON-NLS-1$
		
		assertFalse(
			"Primary edit part not deleted.", primaryEditParts.contains(editPartToDelete)); //$NON-NLS-1$

		assertFalse(
			"Semantic element not deleted.", semanticContainer.eContents().contains(semanticElement)); //$NON-NLS-1$
	}

	public void testAlignment()
		throws Exception {
		// TODO Auto-generated method stub
		super.testAlignment();
	}

	public void testSelect()
		throws Exception {
		// TODO Auto-generated method stub
		super.testSelect();
	}

	public void testZoom()
		throws Exception {
		// TODO Auto-generated method stub
		super.testZoom();
	}

	public void testZoomToolFunctionality()
		throws Exception {
		// TODO Auto-generated method stub
		super.testZoomToolFunctionality();
	}

	public void testCopyToImageActionEnablement()
		throws Exception {

		getTestFixture().openDiagram();

		List children = getTestFixture().getDiagramEditPart().getChildren();

		CircuitEditPart circuitEP = null;
		ListIterator li = children.listIterator();
		while (li.hasNext()) {
			Object ep = li.next();
			if (ep instanceof CircuitEditPart) {
				circuitEP = (CircuitEditPart) ep;
			}
		}

		assertNotNull(circuitEP);

		CopyToImageAction action = new CopyToImageAction(getWorkbenchPage());
		action.init();
		EditPartViewer viewer = getDiagramEditPart().getRoot().getViewer();
		viewer.deselectAll();
		viewer.select(getDiagramEditPart());
		flushEventQueue();
		assertTrue(action.isEnabled());

		viewer.deselectAll();
		viewer.select(circuitEP);
		flushEventQueue();
		assertTrue(action.isEnabled());

	}

    /**
     * Performs a <code>DropObjectsRequest</code> in a modal context thread.
     * Verifies that the diagram refreshes on the UI thread.
     * 
     * @throws Exception
     *             if an unexpected exception occurs
     */
    public void test_drop_modalContextThread()
        throws Exception {

        // Open the test fixture diagram
        getTestFixture().openDiagram();
        final DiagramEditPart diagramEditPart = getDiagramEditPart();

        //Create an AND gate in the semantic model
        ICommand andCommand = new AbstractTransactionalCommand(getTestFixture()
            .getEditingDomain(), "Create AND Gate", null) { //$NON-NLS-1$

            protected CommandResult doExecuteWithResult(
                    IProgressMonitor monitor, IAdaptable info)
                throws ExecutionException {

                AndGate newElement = (AndGate) SemanticPackage.eINSTANCE
                    .getEFactoryInstance().create(
                        SemanticPackage.eINSTANCE.getAndGate());

                ContainerElement semanticElement = (ContainerElement) diagramEditPart
                    .resolveSemanticElement();

                semanticElement.getChildren().add(newElement);
                return CommandResult.newOKCommandResult(newElement);
            }
        };

        andCommand.execute(new NullProgressMonitor(), null);
        AndGate andGate = (AndGate) andCommand.getCommandResult()
            .getReturnValue();

        // Get the initial number of edit parts on the diagram
        List primaryEditParts = diagramEditPart.getPrimaryEditParts();
        int initialEditPartCount = primaryEditParts.size();

        // Get the command to drop the AND gate onto the diagram
        Point dropLocation = ICanonicalShapeCompartmentLayout.UNDEFINED
            .getLocation();
        DropObjectsRequest request = new DropObjectsRequest();
        request.setObjects(Collections.singletonList(andGate));
        request.setAllowedDetail(DND.DROP_COPY);
        request.setLocation(dropLocation);

        Command command = diagramEditPart.getCommand(request);
        final CommandProxy proxy = new CommandProxy(command);

        //Execute the command in a forking progress monitor dialog
        IRunnableWithProgress runnable = new IRunnableWithProgress() {

            public void run(IProgressMonitor monitor)
                throws InvocationTargetException, InterruptedException {
                
                try {
                    OperationHistoryFactory.getOperationHistory().execute(
                        proxy, monitor, null);

                } catch (ExecutionException e) {
                    throw new InvocationTargetException(e);
                }
            }
        };

        new ProgressMonitorDialog(null).run(true, true, runnable);

        // Verify that a new edit part has been added to the diagram for the AND gate
        primaryEditParts = getDiagramEditPart().getPrimaryEditParts();

        assertTrue(
            "Size of primary edit parts should have increased.", primaryEditParts.size() > initialEditPartCount); //$NON-NLS-1$

        IGraphicalEditPart andGateEditPart = null;
        for (Iterator i = primaryEditParts.iterator(); i.hasNext();) {
            IGraphicalEditPart nextEditPart = (IGraphicalEditPart) i.next();

            if (andGate.equals(nextEditPart.resolveSemanticElement())) {
                andGateEditPart = nextEditPart;
                break;
            }
        }
        assertNotNull(
            "Expected a new edit part for the AND gate", andGateEditPart); //$NON-NLS-1$
    }

}
