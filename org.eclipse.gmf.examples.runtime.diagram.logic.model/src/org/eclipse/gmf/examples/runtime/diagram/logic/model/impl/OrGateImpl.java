/**
 * <copyright>
 * </copyright>
 *
 * $Id: OrGateImpl.java,v 1.1 2005/08/30 13:48:51 sshaw Exp $
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.model.impl;

import org.eclipse.gmf.examples.runtime.diagram.logic.model.OrGate;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Or Gate</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class OrGateImpl extends GateImpl implements OrGate {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OrGateImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return SemanticPackage.eINSTANCE.getOrGate();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case SemanticPackage.OR_GATE__TERMINALS:
					return ((InternalEList)getTerminals()).basicRemove(otherEnd, msgs);
				default:
					return eDynamicInverseRemove(otherEnd, featureID, baseClass, msgs);
			}
		}
		return eBasicSetContainer(null, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case SemanticPackage.OR_GATE__TERMINALS:
				return getTerminals();
			case SemanticPackage.OR_GATE__OUTPUT_TERMINALS:
				return getOutputTerminals();
			case SemanticPackage.OR_GATE__INPUT_TERMINALS:
				return getInputTerminals();
		}
		return eDynamicGet(eFeature, resolve);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(EStructuralFeature eFeature, Object newValue) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case SemanticPackage.OR_GATE__TERMINALS:
				getTerminals().clear();
				getTerminals().addAll((Collection)newValue);
				return;
		}
		eDynamicSet(eFeature, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case SemanticPackage.OR_GATE__TERMINALS:
				getTerminals().clear();
				return;
		}
		eDynamicUnset(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case SemanticPackage.OR_GATE__TERMINALS:
				return terminals != null && !terminals.isEmpty();
			case SemanticPackage.OR_GATE__OUTPUT_TERMINALS:
				return !getOutputTerminals().isEmpty();
			case SemanticPackage.OR_GATE__INPUT_TERMINALS:
				return !getInputTerminals().isEmpty();
		}
		return eDynamicIsSet(eFeature);
	}

} //OrGateImpl
