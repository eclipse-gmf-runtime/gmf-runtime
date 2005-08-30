/**
 * <copyright>
 * </copyright>
 *
 * $Id: SemanticAdapterFactory.java,v 1.1 2005/08/30 13:48:52 sshaw Exp $
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.model.util;

import org.eclipse.gmf.examples.runtime.diagram.logic.model.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.examples.runtime.diagram.logic.model.AndGate;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Circuit;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.ContainerElement;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Element;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.FlowContainer;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Gate;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.LED;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Model;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.OrGate;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Terminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Wire;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.XORGate;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage
 * @generated
 */
public class SemanticAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static SemanticPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SemanticAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = SemanticPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch the delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SemanticSwitch modelSwitch =
		new SemanticSwitch() {
			public Object caseLED(LED object) {
				return createLEDAdapter();
			}
			public Object caseElement(Element object) {
				return createElementAdapter();
			}
			public Object caseWire(Wire object) {
				return createWireAdapter();
			}
			public Object caseCircuit(Circuit object) {
				return createCircuitAdapter();
			}
			public Object caseGate(Gate object) {
				return createGateAdapter();
			}
			public Object caseFlowContainer(FlowContainer object) {
				return createFlowContainerAdapter();
			}
			public Object caseAndGate(AndGate object) {
				return createAndGateAdapter();
			}
			public Object caseOrGate(OrGate object) {
				return createOrGateAdapter();
			}
			public Object caseXORGate(XORGate object) {
				return createXORGateAdapter();
			}
			public Object caseModel(Model object) {
				return createModelAdapter();
			}
			public Object caseContainerElement(ContainerElement object) {
				return createContainerElementAdapter();
			}
			public Object caseTerminal(Terminal object) {
				return createTerminalAdapter();
			}
			public Object caseOutputTerminal(OutputTerminal object) {
				return createOutputTerminalAdapter();
			}
			public Object caseInputTerminal(InputTerminal object) {
				return createInputTerminalAdapter();
			}
			public Object caseInputOutputTerminal(InputOutputTerminal object) {
				return createInputOutputTerminalAdapter();
			}
			public Object defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	public Adapter createAdapter(Notifier target) {
		return (Adapter)modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.LED <em>LED</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.LED
	 * @generated
	 */
	public Adapter createLEDAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.Element <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.Element
	 * @generated
	 */
	public Adapter createElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.Wire <em>Wire</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.Wire
	 * @generated
	 */
	public Adapter createWireAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.Circuit <em>Circuit</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.Circuit
	 * @generated
	 */
	public Adapter createCircuitAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.Gate <em>Gate</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.Gate
	 * @generated
	 */
	public Adapter createGateAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.FlowContainer <em>Flow Container</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.FlowContainer
	 * @generated
	 */
	public Adapter createFlowContainerAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.AndGate <em>And Gate</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.AndGate
	 * @generated
	 */
	public Adapter createAndGateAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.OrGate <em>Or Gate</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.OrGate
	 * @generated
	 */
	public Adapter createOrGateAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.XORGate <em>XOR Gate</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.XORGate
	 * @generated
	 */
	public Adapter createXORGateAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.Model <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.Model
	 * @generated
	 */
	public Adapter createModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.ContainerElement <em>Container Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.ContainerElement
	 * @generated
	 */
	public Adapter createContainerElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.Terminal <em>Terminal</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.Terminal
	 * @generated
	 */
	public Adapter createTerminalAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.OutputTerminal <em>Output Terminal</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.OutputTerminal
	 * @generated
	 */
	public Adapter createOutputTerminalAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.InputTerminal <em>Input Terminal</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.InputTerminal
	 * @generated
	 */
	public Adapter createInputTerminalAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.InputOutputTerminal <em>Input Output Terminal</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.InputOutputTerminal
	 * @generated
	 */
	public Adapter createInputOutputTerminalAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //SemanticAdapterFactory
