/**
 * <copyright>
 * </copyright>
 *
 * $Id: Band.java,v 1.1 2005/08/30 13:53:35 sshaw Exp $
 */
package org.eclipse.gmf.tests.runtime.emf.type.ui.employee;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Band</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.gmf.tests.runtime.emf.type.ui.employee.EmployeePackage#getBand()
 * @model
 * @generated
 */
public final class Band extends AbstractEnumerator {
	/**
	 * The '<em><b>Junior</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #JUNIOR_LITERAL
	 * @model name="junior"
	 * @generated
	 * @ordered
	 */
	public static final int JUNIOR = 0;

	/**
	 * The '<em><b>Senior</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SENIOR_LITERAL
	 * @model name="senior"
	 * @generated
	 * @ordered
	 */
	public static final int SENIOR = 1;

	/**
	 * The '<em><b>Manager</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MANAGER_LITERAL
	 * @model name="manager"
	 * @generated
	 * @ordered
	 */
	public static final int MANAGER = 2;

	/**
	 * The '<em><b>Director</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DIRECTOR_LITERAL
	 * @model name="director"
	 * @generated
	 * @ordered
	 */
	public static final int DIRECTOR = 3;

	/**
	 * The '<em><b>Executive</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #EXECUTIVE_LITERAL
	 * @model name="executive"
	 * @generated
	 * @ordered
	 */
	public static final int EXECUTIVE = 4;

	/**
	 * The '<em><b>Junior</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Junior</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #JUNIOR
	 * @generated
	 * @ordered
	 */
	public static final Band JUNIOR_LITERAL = new Band(JUNIOR, "junior"); //$NON-NLS-1$

	/**
	 * The '<em><b>Senior</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Senior</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SENIOR
	 * @generated
	 * @ordered
	 */
	public static final Band SENIOR_LITERAL = new Band(SENIOR, "senior"); //$NON-NLS-1$

	/**
	 * The '<em><b>Manager</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Manager</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MANAGER
	 * @generated
	 * @ordered
	 */
	public static final Band MANAGER_LITERAL = new Band(MANAGER, "manager"); //$NON-NLS-1$

	/**
	 * The '<em><b>Director</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Director</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DIRECTOR
	 * @generated
	 * @ordered
	 */
	public static final Band DIRECTOR_LITERAL = new Band(DIRECTOR, "director"); //$NON-NLS-1$

	/**
	 * The '<em><b>Executive</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Executive</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #EXECUTIVE
	 * @generated
	 * @ordered
	 */
	public static final Band EXECUTIVE_LITERAL = new Band(EXECUTIVE, "executive"); //$NON-NLS-1$

	/**
	 * An array of all the '<em><b>Band</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final Band[] VALUES_ARRAY =
		new Band[] {
			JUNIOR_LITERAL,
			SENIOR_LITERAL,
			MANAGER_LITERAL,
			DIRECTOR_LITERAL,
			EXECUTIVE_LITERAL,
		};

	/**
	 * A public read-only list of all the '<em><b>Band</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Band</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Band get(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			Band result = VALUES_ARRAY[i];
			if (result.toString().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Band</b></em>' literal with the specified value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Band get(int value) {
		switch (value) {
			case JUNIOR: return JUNIOR_LITERAL;
			case SENIOR: return SENIOR_LITERAL;
			case MANAGER: return MANAGER_LITERAL;
			case DIRECTOR: return DIRECTOR_LITERAL;
			case EXECUTIVE: return EXECUTIVE_LITERAL;
		}
		return null;	
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private Band(int value, String name) {
		super(value, name);
	}

} //Band
