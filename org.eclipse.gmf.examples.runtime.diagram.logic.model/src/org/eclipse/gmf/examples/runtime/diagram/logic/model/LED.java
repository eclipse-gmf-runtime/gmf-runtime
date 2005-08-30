/**
 * <copyright>
 * </copyright>
 *
 * $Id: LED.java,v 1.1 2005/08/30 13:48:50 sshaw Exp $
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>LED</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.LED#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage#getLED()
 * @model 
 * @generated
 */
public interface LED extends Element{
	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(int)
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage#getLED_Value()
	 * @model default="0"
	 * @generated
	 */
	int getValue();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.LED#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(int value);

} // LED
