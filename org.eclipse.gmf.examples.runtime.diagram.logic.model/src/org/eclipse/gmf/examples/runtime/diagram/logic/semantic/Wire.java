/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.semantic;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Wire</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Wire#getSource <em>Source</em>}</li>
 *   <li>{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Wire#getTarget <em>Target</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.SemanticPackage#getWire()
 * @model
 * @generated
 */
public interface Wire extends Element {
	/**
	 * Returns the value of the '<em><b>Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' reference.
	 * @see #setSource(OutputTerminal)
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.SemanticPackage#getWire_Source()
	 * @model required="true"
	 * @generated
	 */
	OutputTerminal getSource();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Wire#getSource <em>Source</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' reference.
	 * @see #getSource()
	 * @generated
	 */
	void setSource(OutputTerminal value);

	/**
	 * Returns the value of the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' reference.
	 * @see #setTarget(InputTerminal)
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.semantic.SemanticPackage#getWire_Target()
	 * @model required="true"
	 * @generated
	 */
	InputTerminal getTarget();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Wire#getTarget <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(InputTerminal value);

} // Wire
