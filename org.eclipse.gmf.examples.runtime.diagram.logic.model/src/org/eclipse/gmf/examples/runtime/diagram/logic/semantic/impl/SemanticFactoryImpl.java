/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SemanticFactoryImpl extends EFactoryImpl implements SemanticFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SemanticFactory init() {
		try {
			SemanticFactory theSemanticFactory = (SemanticFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/gmf/examples/runtime/1.0.0/logicsemantic"); 
			if (theSemanticFactory != null) {
				return theSemanticFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new SemanticFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SemanticFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case SemanticPackage.LED: return createLED();
			case SemanticPackage.WIRE: return createWire();
			case SemanticPackage.CIRCUIT: return createCircuit();
			case SemanticPackage.FLOW_CONTAINER: return createFlowContainer();
			case SemanticPackage.AND_GATE: return createAndGate();
			case SemanticPackage.OR_GATE: return createOrGate();
			case SemanticPackage.XOR_GATE: return createXORGate();
			case SemanticPackage.MODEL: return createModel();
			case SemanticPackage.OUTPUT_TERMINAL: return createOutputTerminal();
			case SemanticPackage.INPUT_TERMINAL: return createInputTerminal();
			case SemanticPackage.INPUT_OUTPUT_TERMINAL: return createInputOutputTerminal();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LED createLED() {
		LEDImpl led = new LEDImpl();
		return led;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Wire createWire() {
		WireImpl wire = new WireImpl();
		return wire;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Circuit createCircuit() {
		CircuitImpl circuit = new CircuitImpl();
		return circuit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FlowContainer createFlowContainer() {
		FlowContainerImpl flowContainer = new FlowContainerImpl();
		return flowContainer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AndGate createAndGate() {
		AndGateImpl andGate = new AndGateImpl();
		return andGate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OrGate createOrGate() {
		OrGateImpl orGate = new OrGateImpl();
		return orGate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public XORGate createXORGate() {
		XORGateImpl xorGate = new XORGateImpl();
		return xorGate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Model createModel() {
		ModelImpl model = new ModelImpl();
		return model;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OutputTerminal createOutputTerminal() {
		OutputTerminalImpl outputTerminal = new OutputTerminalImpl();
		return outputTerminal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InputTerminal createInputTerminal() {
		InputTerminalImpl inputTerminal = new InputTerminalImpl();
		return inputTerminal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InputOutputTerminal createInputOutputTerminal() {
		InputOutputTerminalImpl inputOutputTerminal = new InputOutputTerminalImpl();
		return inputOutputTerminal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SemanticPackage getSemanticPackage() {
		return (SemanticPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static SemanticPackage getPackage() {
		return SemanticPackage.eINSTANCE;
	}

} //SemanticFactoryImpl
