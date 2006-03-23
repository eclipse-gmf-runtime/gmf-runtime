/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.semantic;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Element#getTerminals <em>Terminals</em>}</li>
 *   <li>{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Element#getOutputTerminals <em>Output Terminals</em>}</li>
 *   <li>{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Element#getInputTerminals <em>Input Terminals</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.SemanticPackage#getElement()
 * @model abstract="true"
 * @generated
 */
public interface Element extends EObject {
	/**
	 * Returns the value of the '<em><b>Terminals</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Terminal}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Terminals</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Terminals</em>' containment reference list.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.SemanticPackage#getElement_Terminals()
	 * @model type="org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Terminal" containment="true"
	 * @generated
	 */
	EList getTerminals();

	/**
	 * Returns the value of the '<em><b>Output Terminals</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Terminal}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Output Terminals</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Output Terminals</em>' reference list.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.SemanticPackage#getElement_OutputTerminals()
	 * @model type="org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Terminal" transient="true" changeable="false" volatile="true"
	 * @generated
	 */
	EList getOutputTerminals();

	/**
	 * Returns the value of the '<em><b>Input Terminals</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Terminal}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Input Terminals</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Input Terminals</em>' reference list.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.SemanticPackage#getElement_InputTerminals()
	 * @model type="org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Terminal" transient="true" changeable="false" volatile="true"
	 * @generated
	 */
	EList getInputTerminals();

} // Element
