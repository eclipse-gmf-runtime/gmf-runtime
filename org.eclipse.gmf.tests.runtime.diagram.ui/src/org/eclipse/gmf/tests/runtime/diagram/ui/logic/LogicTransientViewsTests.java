/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.diagram.ui.logic;

/**
 * Tests the Transient Views functionality
 * @author mmostafa
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.CircuitEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Circuit;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.LED;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Terminal;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tests.runtime.diagram.ui.AbstractTestBase;


public class LogicTransientViewsTests extends AbstractTestBase{
	/**
	 * Defines the statechart diagram test suite.
	 * 
	 * @return the test suite.
	 */
	public static Test suite() {
		TestSuite s = new TestSuite(LogicTransientViewsTests.class);
		return s;
	}
	
	/** Create an instance. */
	public LogicTransientViewsTests() {
		super("Transient View Test Suite");//$NON-NLS-1$
	}

	/** installs the composite state test fixture. */
	protected void setTestFixture() {
		testFixture = new CanonicalTestFixture();
	}

	/** Return <code>(CanonicalTestFixture)getTestFixture();</code> */
	protected CanonicalTestFixture getCanonicalTestFixture() {
		return (CanonicalTestFixture)getTestFixture();
	}
	
	public void testTransientWiresCreation_AcrossTransientLeds(){
		try {
			println("testTransientWiresCreation_AcrossTransientLeds() starting ...");//$NON-NLS-1$
			CanonicalTestFixture _testFixture = getCanonicalTestFixture();
			
			IGraphicalEditPart logicCompartment = _testFixture.getCanonicalCompartment(0);
			
			LED led1 = _testFixture.createLED(ViewUtil.resolveSemanticElement(logicCompartment.getNotationView()));
			LED led2 = _testFixture.createLED(ViewUtil.resolveSemanticElement(logicCompartment.getNotationView()));
			Terminal term1 = (Terminal)led1.getOutputTerminals().get(0);
			Terminal term2 = (Terminal)led2.getInputTerminals().get(0);
			
			IElementType typeWire = ElementTypeRegistry.getInstance().getType("logic.wire"); //$NON-NLS-1$
			IElementType typeCircuit = ElementTypeRegistry.getInstance().getType("logic.circuit"); //$NON-NLS-1$
			
			CreateRelationshipRequest crr = new CreateRelationshipRequest(getTestFixture().getEditingDomain(), term1, term2, typeWire);
			ICommand createWire = typeCircuit.getEditHelper().getEditCommand(crr);
			_testFixture.execute(createWire);
			flushEventQueue();
			
			List connectorEPs = getDiagramEditPart().getConnections();
			
			assertEquals( "Unexpected Wire count.", 1, connectorEPs.size()); //$NON-NLS-1$
			final ConnectionEditPart ep = (ConnectionEditPart)connectorEPs.get(0);
			assertTransient((View)ep.getSource().getModel());
			assertTransient((View)ep.getTarget().getModel());
			assertTransient((View)ep.getModel());
			
			final TransactionalEditingDomain editingDomain = ((IGraphicalEditPart)ep).getEditingDomain();
			
			AbstractEMFOperation operation = new AbstractEMFOperation(
				editingDomain, "") { //$NON-NLS-1$

				protected IStatus doExecute(IProgressMonitor monitor,
						IAdaptable info)
					throws ExecutionException {
					
					((View)ep.getModel()).setVisible(false);
					
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
//			
//			editingDomain.runInUndoInterval(new Runnable() {
//				public void run() {
//					try {
//						editingDomain.runAsWrite(new MRunnable() {
//							public Object run() {
//								((View)ep.getModel()).setVisible(false);
//								return null;
//							}});
//					} catch (MSLActionAbandonedException e) {
//						// do nothing
//					}
//				}});
				
			assertPersisted((View)ep.getSource().getModel());
			assertPersisted((View)ep.getTarget().getModel());
			assertPersisted((View)ep.getModel());
			
			
		}
		finally {
			println("testTransientWiresCreation_AcrossTransientLeds() complete.");//$NON-NLS-1$
		}
	}
	
    private GraphicalEditPart _editPartForSemanticElement(GraphicalEditPart container, Object element){
        List children = container.getChildren();
        for (Iterator iter = children.iterator(); iter.hasNext();) {
            GraphicalEditPart ep = (GraphicalEditPart) iter.next();
            if (ep.getNotationView().getElement()==element){
                return ep;
            }else {
                ep  = _editPartForSemanticElement(ep,element);
                if (ep !=null)
                    return ep;
            }
        }
        return null;
    }
       
    public void testTransientWiresCreation_AcrossPersistedLeds(){
        try {
            println("testTransientWiresCreation_AcrossPersistedLeds() starting ...");//$NON-NLS-1$
            CanonicalTestFixture _testFixture = getCanonicalTestFixture();
            IGraphicalEditPart logicCompartment = _testFixture.getCanonicalCompartment(0);
            
            LED led1 = _testFixture.createLED(ViewUtil.resolveSemanticElement(logicCompartment.getNotationView()));
            LED led2 = _testFixture.createLED(ViewUtil.resolveSemanticElement(logicCompartment.getNotationView()));
            
            GraphicalEditPart ledEditPart = (GraphicalEditPart)getDiagramEditPart().getChildren().get(0);
            Terminal term1 = (Terminal)led1.getOutputTerminals().get(0);
            Terminal term2 = (Terminal)led2.getInputTerminals().get(0);
            
            flushEventQueue();
            
            final GraphicalEditPart ep1 = _editPartForSemanticElement(ledEditPart,term1);
            
            
            // force the led to be persisted
            final TransactionalEditingDomain editingDomain = ledEditPart.getEditingDomain();
            
			AbstractEMFOperation operation = new AbstractEMFOperation(
				editingDomain, "") { //$NON-NLS-1$

				protected IStatus doExecute(IProgressMonitor monitor,
						IAdaptable info)
					throws ExecutionException {
					
                    ((View)((View)ep1.getModel()).eContainer()).persistChildren();
					
					return Status.OK_STATUS;
				};
			};
			try {
				getDiagramEditPart().getDiagramEditDomain().getActionManager()
					.getOperationHistory().execute(operation,
						new NullProgressMonitor(), null);
			} catch (ExecutionException e) {
				e.printStackTrace();
				assertFalse(false);
			}
                
            assertPersisted((View)ep1.getModel());
            
            IElementType typeWire = ElementTypeRegistry.getInstance().getType("logic.wire"); //$NON-NLS-1$
            IElementType typeCircuit = ElementTypeRegistry.getInstance().getType("logic.circuit"); //$NON-NLS-1$
            
            CreateRelationshipRequest crr = new CreateRelationshipRequest(editingDomain, term1, term2, typeWire);
            ICommand createWire = typeCircuit.getEditHelper().getEditCommand(crr);
            _testFixture.execute(createWire);
            flushEventQueue();
            
            List connectorEPs = getDiagramEditPart().getConnections();
            
            assertEquals( "Unexpected Wire count.", 1, connectorEPs.size()); //$NON-NLS-1$
            final ConnectionEditPart ep = (ConnectionEditPart)connectorEPs.get(0);
            assertTransient((View)ep.getModel());
        }
        finally {
            println("testTransientWiresCreation_AcrossPersistedLeds() complete.");//$NON-NLS-1$
        }
    }
	
	public void testTransientLEDsCreation(){
		try {
			println("testTransientLEDsCreation() starting ...");//$NON-NLS-1$
			CanonicalTestFixture _testFixture = getCanonicalTestFixture();
			IGraphicalEditPart logicCompartment = _testFixture.getCanonicalCompartment(0);
			
			List properties = new ArrayList();
			int size = logicCompartment.getChildren().size();
			int count = 5;
			for ( int i = 0; i < count; i++ ) {
				properties.add( _testFixture.createLED(ViewUtil.resolveSemanticElement(logicCompartment.getNotationView())));
				size++;
				assertEquals( "Unexpected LED count.", size, logicCompartment.getChildren().size() );//$NON-NLS-1$
			}
			
			assertTransient(logicCompartment.getChildren());
			
			Rectangle rect = new Rectangle(logicCompartment.getFigure().getBounds());
			logicCompartment.getFigure().translateToAbsolute(rect);
			IElementType typeLED = ElementTypeRegistry.getInstance().getType("logic.led"); //$NON-NLS-1$
			getCanonicalTestFixture().createShapeUsingTool(typeLED, rect.getCenter(), logicCompartment);
			assertPersisted(logicCompartment.getChildren());
			
			LED led  = _testFixture.createLED(ViewUtil.resolveSemanticElement(logicCompartment.getNotationView()));
			List children = logicCompartment.getChildren();
			for (Iterator iter = children.iterator(); iter.hasNext();) {
				GraphicalEditPart element = (GraphicalEditPart) iter.next();
				View view = element.getNotationView();
				if (view !=null){
					Object _led = view.getElement();
					if (_led == led){
						assertTransient(view);
					} else {
						assertPersisted(view);
					}
				}
				
			}	
		}
		finally {
			println("testTransientLEDsCreation() complete.");//$NON-NLS-1$
		}
	}

	public void testTransientCircuitsCreation(){
		try {
			println("testTransientCircuitsCreation() starting ...");//$NON-NLS-1$
			CanonicalTestFixture _testFixture = getCanonicalTestFixture();
			IGraphicalEditPart logicCompartment = _testFixture.getCanonicalCompartment(0);
			
			List properties = new ArrayList();
			int size = logicCompartment.getChildren().size();
			int count = 5;
			for ( int i = 0; i < count; i++ ) {
				properties.add( _testFixture.createCircuit(ViewUtil.resolveSemanticElement(logicCompartment.getNotationView())));
				size++;
				assertEquals( "Unexpected Circuit count.", size, logicCompartment.getChildren().size() );//$NON-NLS-1$
			}
			
			assertTransient(logicCompartment.getChildren());
			
			Rectangle rect = new Rectangle(logicCompartment.getFigure().getBounds());
			logicCompartment.getFigure().translateToAbsolute(rect);
			IElementType typeCircuit = ElementTypeRegistry.getInstance().getType("logic.circuit"); //$NON-NLS-1$
			getCanonicalTestFixture().createShapeUsingTool(typeCircuit, rect.getCenter(), logicCompartment);
			assertPersisted(logicCompartment.getChildren());
			
			Circuit circuit  = _testFixture.createCircuit(ViewUtil.resolveSemanticElement(logicCompartment.getNotationView()));
			List children = logicCompartment.getChildren();
			for (Iterator iter = children.iterator(); iter.hasNext();) {
				GraphicalEditPart element = (GraphicalEditPart) iter.next();
				View view = element.getNotationView();
				if (view !=null){
					Object _circuit = view.getElement();
					if (_circuit == circuit){
						assertTransient(view);
						assertTransient(element.getChildren());
					} else {
						assertPersisted(view);
					}
				}
				
			}	
		}
		finally {
			println("testTransientCircuitsCreation() complete.");//$NON-NLS-1$
		}
	}

//	/**
//	 * Test that moving a transient LED will cause it to be persisted.
//	 */
//	public void testPersistedAfterMove(){
//		try {
//			println("test_testPersistedAfterMove() starting ...");//$NON-NLS-1$
//			CanonicalTestFixture _testFixture = getCanonicalTestFixture();
//			IGraphicalEditPart logicCompartment = _testFixture.getCanonicalCompartment(0);
//			
//			_testFixture.createLED(ViewUtil.resolveSemanticElement(logicCompartment.getNotationView()));
//			assertEquals( "Unexpected LED count.", 1, logicCompartment.getChildren().size() );//$NON-NLS-1$
//			
//			// Starts out as being transient.
//			assertTransient(logicCompartment.getChildren());
//			
//			// Move LED.
//			IGraphicalEditPart ledEP = (IGraphicalEditPart) logicCompartment.getChildren().get(0);
//			Point oldLocation = ledEP.getFigure().getBounds().getLocation();
//			ChangeBoundsRequest request = new ChangeBoundsRequest(
//				RequestConstants.REQ_MOVE);
//			request.setEditParts(ledEP);
//			request.setMoveDelta(new Point(100, 100));
//			Command cmd = ledEP.getCommand(request);
//			cmd.execute();
//			flushEventQueue();
//			assertFalse(oldLocation.equals(ledEP.getFigure().getBounds().getLocation()));		
//			
//			// Should be persisted after a move.
//			assertPersisted(logicCompartment.getChildren());				
//		}
//		finally {
//			println("test_testPersistedAfterMove() complete.");//$NON-NLS-1$
//		}
//	}
	
	private void assertPersisted(View view) {
		if (view != null){
			EStructuralFeature feature = view.eContainingFeature();
			if (feature!=null){
				assertFalse("Expected a Persisted View", feature.isTransient()); //$NON-NLS-1$
			}
		}
	}
	
	private void assertTransient(View view) {
		if (view != null){
			EStructuralFeature feature = view.eContainingFeature();
			if (feature!=null){
				assertTrue("Expected a Transient View", feature.isTransient()); //$NON-NLS-1$
			}
		}
	}
	
	private void assertPersisted(List children) {
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			GraphicalEditPart element = (GraphicalEditPart) iter.next();
			View view = element.getNotationView();
			if (view != null){
				EStructuralFeature feature = view.eContainingFeature();
				if (feature!=null){
					assertFalse("Expected a Persisted View", feature.isTransient()); //$NON-NLS-1$
				}
			}
			
		}
		
	}

	private void assertTransient(List children) {
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			GraphicalEditPart element = (GraphicalEditPart) iter.next();
			View view = element.getNotationView();
			if (view != null){
				EStructuralFeature feature = view.eContainingFeature();
				if (feature!=null){
					assertTrue("Expected a Transient View", feature.isTransient()); //$NON-NLS-1$
				}
			}
			
		}
		
	}

}
