/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.diagram.ui.logic;

import java.util.Collection;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers.LogicConstants;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.AndGate;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Circuit;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.FlowContainer;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.LED;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Model;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.OrGate;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Terminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.XORGate;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.util.ViewRefactorHelper;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.FillStyle;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tests.runtime.diagram.ui.AbstractTestBase;
import org.eclipse.swt.graphics.RGB;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * A collection of tests for the view refactor helper in the context of logic diagrams
 * 
 * @author melaasar
 */
public class LogicViewRefactorTests extends AbstractTestBase {

	private class LogicRefactorTestFixture extends LogicTestFixture {

		private IGraphicalEditPart andEP;
		
		public IGraphicalEditPart getAndEP() {
			return andEP;
		}
		
		protected void createShapesAndConnectors() throws Exception {
			// create Circuit
			IElementType typeCircuit = ElementTypeRegistry.getInstance().getType("logic.circuit"); //$NON-NLS-1$

			CreateViewAndElementRequest shapeRequest =
				new CreateViewAndElementRequest(typeCircuit, getPreferencesHint());
			
			shapeRequest.setLocation(new Point(20, 20));
			shapeRequest.setSize(new Dimension(200, 200));
	  
			execute(getDiagramEditPart().getCommand(shapeRequest));
			assertFalse("Circuit creation failed", getDiagramEditPart().getChildren().isEmpty()); //$NON-NLS-1$

			IGraphicalEditPart circuitEP = (IGraphicalEditPart) getDiagramEditPart().getChildren().get(0);
			
			IGraphicalEditPart logicCompartmentEP = circuitEP.getChildBySemanticHint(LogicConstants.LOGIC_SHAPE_COMPARTMENT);
			
			// create Led
			IElementType typeLed = ElementTypeRegistry.getInstance().getType("logic.led"); //$NON-NLS-1$

			shapeRequest =	new CreateViewAndElementRequest(typeLed, getPreferencesHint());
			
			shapeRequest.setLocation(new Point(30, 30));
	  
			execute(logicCompartmentEP.getCommand(shapeRequest));
			assertTrue("Led creation failed",logicCompartmentEP.getChildren().size() == 1);//$NON-NLS-1$

			// create And Gate
			IElementType typeAnd = ElementTypeRegistry.getInstance().getType("logic.andgate"); //$NON-NLS-1$

			shapeRequest = new CreateViewAndElementRequest(typeAnd, getPreferencesHint());
			
			shapeRequest.setLocation(new Point(30, 110));
	  
			execute(logicCompartmentEP.getCommand(shapeRequest));
			assertTrue("And gate creation failed", logicCompartmentEP.getChildren().size() == 2);//$NON-NLS-1$

			// create Or Gate
			IElementType typeOr = ElementTypeRegistry.getInstance().getType("logic.orgate"); //$NON-NLS-1$

			shapeRequest =	new CreateViewAndElementRequest(typeOr, getPreferencesHint());
			
			shapeRequest.setLocation(new Point(100, 110));
	  
			execute(logicCompartmentEP.getCommand(shapeRequest));
			assertTrue("Or gate creation failed", logicCompartmentEP.getChildren().size() == 3);//$NON-NLS-1$

			// create Wire between Led and And gate and between And and Or gate
			flushEventQueue();
			IGraphicalEditPart ledEP = (IGraphicalEditPart) logicCompartmentEP.getChildren().get(0);
			LED led = (LED) ledEP.getAdapter(LED.class);

			andEP = (IGraphicalEditPart) logicCompartmentEP.getChildren().get(1);
			AndGate andGate = (AndGate) andEP.getAdapter(AndGate.class);

			IGraphicalEditPart orEP = (IGraphicalEditPart) logicCompartmentEP.getChildren().get(2);
			OrGate orGate = (OrGate) orEP.getAdapter(OrGate.class);

			IElementType typeWire = ElementTypeRegistry.getInstance().getType("logic.wire"); //$NON-NLS-1$

			Terminal outTerminal1 = (Terminal) led.getOutputTerminals().get(3);
			IGraphicalEditPart outTerminalEP1 = (IGraphicalEditPart) ledEP.findEditPart(null, outTerminal1);

			Terminal inTerminal1 = (Terminal) andGate.getInputTerminals().get(0);
			IGraphicalEditPart inTerminalEP1 = (IGraphicalEditPart) andEP.findEditPart(null, inTerminal1);
			
			createConnectorUsingTool(outTerminalEP1, inTerminalEP1, typeWire);
			assertTrue("Led to And gate connection failed", getDiagramEditPart().getConnections().size() == 1);//$NON-NLS-1$

			Terminal outTerminal2 = (Terminal) andGate.getOutputTerminals().get(0);
			IGraphicalEditPart outTerminalEP2 = (IGraphicalEditPart) andEP.findEditPart(null, outTerminal2);

			Terminal inTerminal2 = (Terminal) orGate.getInputTerminals().get(0);
			IGraphicalEditPart inTerminalEP2 = (IGraphicalEditPart) orEP.findEditPart(null, inTerminal2);

			createConnectorUsingTool(outTerminalEP2, inTerminalEP2, typeWire);
			assertTrue("And to Or gate connection failed", getDiagramEditPart().getConnections().size() == 2);//$NON-NLS-1$
		}
		
		public ConnectionEditPart createConnectorUsingTool(
				final IGraphicalEditPart sourceEditPart,
				final IGraphicalEditPart targetEditPart, 
				IElementType elementType) {

			class ConnectorCreationTool	extends	org.eclipse.gmf.runtime.diagram.ui.tools.ConnectionCreationTool {

				public ConnectorCreationTool(IElementType theElementType) {
					super(theElementType);
				}

				/** Make public. */
				public Request createTargetRequest() {
					return super.createTargetRequest();
				}
				
				protected PreferencesHint getPreferencesHint() {
					return PreferencesHint.USE_DEFAULTS;
				}
			}

			ConnectorCreationTool tool = new ConnectorCreationTool(elementType);
			CreateConnectionRequest request = (CreateConnectionRequest) tool.createTargetRequest();
			request.setTargetEditPart(sourceEditPart);
			request.setType(RequestConstants.REQ_CONNECTION_START);
			sourceEditPart.getCommand(request);
			request.setSourceEditPart(sourceEditPart);
			request.setTargetEditPart(targetEditPart);
			request.setType(RequestConstants.REQ_CONNECTION_END);
			Command cmd = targetEditPart.getCommand(request);

			getCommandStack().execute(cmd);

			Object newView = ((IAdaptable) request.getNewObject()).getAdapter(View.class);
			assertNotNull(newView);

			ConnectionEditPart newConnector = (ConnectionEditPart) getDiagramEditPart()
				.getViewer().getEditPartRegistry().get(newView);
			assertNotNull(newConnector);

			return newConnector;
		}
	}
	
	/**
	 * Defines the statechart diagram test suite.
	 * 
	 * @return the test suite.
	 */
	public static Test suite() {
		TestSuite s = new TestSuite(LogicViewRefactorTests.class);
		return s;
	}

	public LogicViewRefactorTests() {
		super("Logic View Refactor Test Suite");//$NON-NLS-1$
	}

	protected void setTestFixture() {
		testFixture = new LogicRefactorTestFixture();
	}

	protected LogicRefactorTestFixture getLogicRefactorTestFixture() {
		return (LogicRefactorTestFixture) getTestFixture();
	}
	
	/**
	 * Tests the notational refactoring of an AND gate to an XOR gate
	 */
	public void test_RefactorANDIntoXOR() {
		try {
			final IGraphicalEditPart andEP = getLogicRefactorTestFixture().getAndEP();
			
			final int color = FigureUtilities.RGBToInteger(new RGB(255, 0, 255)).intValue();
			
            TransactionalEditingDomain editingDomain = getLogicRefactorTestFixture().getEditingDomain();
            
			// do notational changes to the And gate
			getLogicRefactorTestFixture().execute(
                new AbstractTransactionalCommand(editingDomain, "", null) { //$NON-NLS-1$
                    
				protected CommandResult doExecuteWithResult(
                            IProgressMonitor progressMonitor, IAdaptable info)
                        throws ExecutionException {
                    
					Node node = (Node) andEP.getNotationView();
					Bounds bounds = (Bounds) node.getLayoutConstraint();
					bounds.setX(800);
					bounds.setY(3000);
					FillStyle fStyle = (FillStyle) node.getStyle(NotationPackage.eINSTANCE.getFillStyle());
					fStyle.setFillColor(color);
					return CommandResult.newOKCommandResult();
				}
			});
			
			
			// create new Xor gate
			final IElementType typeXor = ElementTypeRegistry.getInstance().getType("logic.xorgate"); //$NON-NLS-1$
	
			final Circuit circuit = (Circuit) ViewUtil.getContainerView(andEP.getNotationView()).getElement();
			CreateElementRequest createRequest = new CreateElementRequest(editingDomain, circuit, typeXor);
            ICommand command = typeXor.getEditHelper().getEditCommand(createRequest);
			getLogicRefactorTestFixture().execute(command);
			assertTrue("Xor creation failed", circuit.getChildren().size() == 6); //$NON-NLS-1$

			// do the notation morphing
			Collection results = getLogicRefactorTestFixture().execute(new AbstractTransactionalCommand(editingDomain, "", null) {//$NON-NLS-1$
				protected CommandResult doExecuteWithResult(
                            IProgressMonitor progressMonitor, IAdaptable info)
                        throws ExecutionException {
					AndGate oldObject = (AndGate) circuit.getChildren().get(1);
					XORGate newObject = (XORGate) circuit.getChildren().get(5);

					newObject.getTerminals().clear();
					newObject.getTerminals().addAll(oldObject.getTerminals());

					new ViewRefactorHelper(PreferencesHint.USE_DEFAULTS).refactor(oldObject, newObject);
					DestroyElementCommand.destroy(oldObject);
					
					return CommandResult.newOKCommandResult(newObject);
				}
			});
			
			// validate the morphing
			XORGate xorGate = (XORGate) results.iterator().next();
			Collection newNodes = EMFCoreUtil.getReferencers(xorGate, new EReference[]{NotationPackage.eINSTANCE.getView_Element()});
			assertFalse("morphing view failed", newNodes.isEmpty());//$NON-NLS-1$
			
			Node newNode = (Node) newNodes.iterator().next();
			Bounds bounds = (Bounds) newNode.getLayoutConstraint();
			assertEquals(800, bounds.getX());
			assertEquals(3000, bounds.getY());
			FillStyle fStyle = (FillStyle) newNode.getStyle(NotationPackage.eINSTANCE.getFillStyle());
			assertEquals(color, fStyle.getFillColor());
			assertEquals(1, ((Node)newNode.getChildren().get(0)).getTargetEdges().size());
			assertEquals(1, ((Node)newNode.getChildren().get(2)).getSourceEdges().size());
			
		} catch (Exception e) {
			assertTrue(e.toString(), false);
		}
	}

	/**
	 * Tests the notational refactoring of an Circuit to a Logic Flow
	 */
	public void test_RefactorCircuitIntoLogicFlow() {
		try {
			final IGraphicalEditPart circuitEP = (IGraphicalEditPart) getLogicRefactorTestFixture().getAndEP().getParent().getParent();
			
            TransactionalEditingDomain editingDomain = getLogicRefactorTestFixture().getEditingDomain();
            
			final int color = FigureUtilities.RGBToInteger(new RGB(255, 0, 255)).intValue();
			
			// do notational changes to the And gate
			getLogicRefactorTestFixture().execute(new AbstractTransactionalCommand(editingDomain, "", null) {//$NON-NLS-1$
				protected CommandResult doExecuteWithResult(IProgressMonitor progressMonitor, IAdaptable info) throws ExecutionException {
					Node node = (Node) circuitEP.getNotationView();
					Bounds bounds = (Bounds) node.getLayoutConstraint();
					bounds.setX(1000);
					bounds.setY(1000);
					FillStyle fStyle = (FillStyle) node.getStyle(NotationPackage.eINSTANCE.getFillStyle());
					fStyle.setFillColor(color);
					return CommandResult.newOKCommandResult();
				}
			});
			
			// create new Xor gate
			final IElementType typeFlowContainer = ElementTypeRegistry.getInstance().getType("logic.flowcontainer"); //$NON-NLS-1$
	
			final Model model = (Model) ViewUtil.getContainerView(circuitEP.getNotationView()).getElement();
			CreateElementRequest createRequest = new CreateElementRequest(editingDomain, model, typeFlowContainer);
			getLogicRefactorTestFixture().execute(typeFlowContainer.getEditHelper().getEditCommand(createRequest));
			assertTrue("Flow Container creation failed", model.getChildren().size() == 2);//$NON-NLS-1$

			// do the notation morphing
			Collection results = getLogicRefactorTestFixture().execute(new AbstractTransactionalCommand(editingDomain, "", null) {//$NON-NLS-1$
				protected CommandResult doExecuteWithResult(IProgressMonitor progressMonitor, IAdaptable info) throws ExecutionException {
					final Circuit oldObject = (Circuit) model.getChildren().get(0);
					FlowContainer newObject = (FlowContainer) model.getChildren().get(1);

					newObject.getChildren().clear();
					newObject.getChildren().addAll(oldObject.getChildren());

					new ViewRefactorHelper(PreferencesHint.USE_DEFAULTS) {
						protected void copyViewChild(View oldView, View newView, Node oldChildNode) {
							if (oldChildNode.getType().equals(LogicConstants.LOGIC_SHAPE_COMPARTMENT)) {
								Node newChildNode = (Node) ViewUtil.getChildBySemanticHint(newView, LogicConstants.LOGIC_FLOW_COMPARTMENT);
								if (newChildNode != null) {
									copyNodeFeatures(oldChildNode, newChildNode);
								}
							} else {
								super.copyViewChild(oldView, newView, oldChildNode);
							}
						}						
					}.refactor(oldObject, newObject);
					DestroyElementCommand.destroy(oldObject);
					
					return CommandResult.newOKCommandResult(newObject);
				}
			});
			
			// validate the morphing
			FlowContainer flowContainer = (FlowContainer) results.iterator().next();
			Collection newNodes = EMFCoreUtil.getReferencers(flowContainer, new EReference[]{NotationPackage.eINSTANCE.getView_Element()});
			assertFalse("morphing view failed", newNodes.isEmpty());//$NON-NLS-1$
			
			Node newNode = (Node) newNodes.iterator().next();
			Bounds bounds = (Bounds) newNode.getLayoutConstraint();
			assertEquals(1000, bounds.getX());
			assertEquals(1000, bounds.getY());
			FillStyle fStyle = (FillStyle) newNode.getStyle(NotationPackage.eINSTANCE.getFillStyle());
			assertEquals(color, fStyle.getFillColor());
			
		} catch (Exception e) {
			assertTrue(e.toString(), false);
		}
	}
}
