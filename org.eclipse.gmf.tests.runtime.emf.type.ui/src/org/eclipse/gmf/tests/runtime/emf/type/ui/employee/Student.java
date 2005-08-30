/**
 * <copyright>
 * </copyright>
 *
 * $Id: Student.java,v 1.1 2005/08/30 13:53:35 sshaw Exp $
 */
package org.eclipse.gmf.tests.runtime.emf.type.ui.employee;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Student</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.gmf.tests.runtime.emf.type.ui.employee.Student#isCoop <em>Coop</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.gmf.tests.runtime.emf.type.ui.employee.EmployeePackage#getStudent()
 * @model
 * @generated
 */
public interface Student extends Employee{
	/**
	 * Returns the value of the '<em><b>Coop</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Coop</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Coop</em>' attribute.
	 * @see #setCoop(boolean)
	 * @see org.eclipse.gmf.tests.runtime.emf.type.ui.employee.EmployeePackage#getStudent_Coop()
	 * @model required="true"
	 * @generated
	 */
	boolean isCoop();

	/**
	 * Sets the value of the '{@link org.eclipse.gmf.tests.runtime.emf.type.ui.employee.Student#isCoop <em>Coop</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Coop</em>' attribute.
	 * @see #isCoop()
	 * @generated
	 */
	void setCoop(boolean value);

} // Student
