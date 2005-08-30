/**
 * <copyright>
 * </copyright>
 *
 * $Id: Terminal.java,v 1.1 2005/08/30 13:48:50 sshaw Exp $
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Terminal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.Terminal#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage#getTerminal()
 * @model abstract="true"
 * @generated
 */
public interface Terminal extends Element{
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage#getTerminal_Id()
	 * @model default=""
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.Terminal#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

} // Terminal
