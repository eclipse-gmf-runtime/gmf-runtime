/**
 * <copyright>
 * </copyright>
 *
 * $Id: ContainerElement.java,v 1.1 2005/08/30 13:48:50 sshaw Exp $
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.model;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Container Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.ContainerElement#getChildren <em>Children</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage#getContainerElement()
 * @model abstract="true"
 * @generated
 */
public interface ContainerElement extends Element{
	/**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.gmf.examples.runtime.diagram.logic.model.Element}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' containment reference list.
	 * @see org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage#getContainerElement_Children()
	 * @model type="org.eclipse.gmf.examples.runtime.diagram.logic.model.Element" containment="true"
	 * @generated
	 */
	EList getChildren();

} // ContainerElement
