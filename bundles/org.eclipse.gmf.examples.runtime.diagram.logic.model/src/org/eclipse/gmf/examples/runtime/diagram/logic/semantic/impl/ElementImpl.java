/******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Element;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.InputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.OutputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.SemanticPackage;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Terminal;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.ElementImpl#getTerminals <em>Terminals</em>}</li>
 *   <li>{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.ElementImpl#getOutputTerminals <em>Output Terminals</em>}</li>
 *   <li>{@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.impl.ElementImpl#getInputTerminals <em>Input Terminals</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class ElementImpl extends EObjectImpl implements Element {
	/**
	 * The cached value of the '{@link #getTerminals() <em>Terminals</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTerminals()
	 * @generated
	 * @ordered
	 */
	protected EList terminals = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return SemanticPackage.Literals.ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getTerminals() {
		if (terminals == null) {
			terminals = new EObjectContainmentEList(Terminal.class, this, SemanticPackage.ELEMENT__TERMINALS);
		}
		return terminals;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList getOutputTerminals() {
		ArrayList outputTerminals = new ArrayList();
		for (Iterator iter = getTerminals().iterator(); iter.hasNext(); ) {
			Terminal terminal = (Terminal)iter.next();
			if (terminal instanceof OutputTerminal)
				outputTerminals.add(terminal);
		}
		return new EcoreEList.UnmodifiableEList(this,
						SemanticPackage.eINSTANCE.getElement_InputTerminals(),
						outputTerminals.size(), outputTerminals.toArray());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList getInputTerminals() {
		ArrayList inputTerminals = new ArrayList();
		for (Iterator iter = getTerminals().iterator(); iter.hasNext(); ) {
			Terminal terminal = (Terminal)iter.next();
			if (terminal instanceof InputTerminal)
				inputTerminals.add(terminal);
		}
		return new EcoreEList.UnmodifiableEList(this,
						SemanticPackage.eINSTANCE.getElement_InputTerminals(),
						inputTerminals.size(), inputTerminals.toArray());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SemanticPackage.ELEMENT__TERMINALS:
				return ((InternalEList)getTerminals()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SemanticPackage.ELEMENT__TERMINALS:
				return getTerminals();
			case SemanticPackage.ELEMENT__OUTPUT_TERMINALS:
				return getOutputTerminals();
			case SemanticPackage.ELEMENT__INPUT_TERMINALS:
				return getInputTerminals();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case SemanticPackage.ELEMENT__TERMINALS:
				getTerminals().clear();
				getTerminals().addAll((Collection)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
			case SemanticPackage.ELEMENT__TERMINALS:
				getTerminals().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case SemanticPackage.ELEMENT__TERMINALS:
				return terminals != null && !terminals.isEmpty();
			case SemanticPackage.ELEMENT__OUTPUT_TERMINALS:
				return !getOutputTerminals().isEmpty();
			case SemanticPackage.ELEMENT__INPUT_TERMINALS:
				return !getInputTerminals().isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ElementImpl
