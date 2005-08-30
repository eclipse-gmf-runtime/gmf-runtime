/**
 * <copyright>
 * </copyright>
 *
 * $Id: ElementImpl.java,v 1.1 2005/08/30 13:48:51 sshaw Exp $
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.gmf.examples.runtime.diagram.logic.model.Element;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.InputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.OutputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Terminal;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.ElementImpl#getTerminals <em>Terminals</em>}</li>
 *   <li>{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.ElementImpl#getOutputTerminals <em>Output Terminals</em>}</li>
 *   <li>{@link org.eclipse.gmf.examples.runtime.diagram.logic.model.impl.ElementImpl#getInputTerminals <em>Input Terminals</em>}</li>
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
		return SemanticPackage.eINSTANCE.getElement();
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
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case SemanticPackage.ELEMENT__TERMINALS:
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
			case SemanticPackage.ELEMENT__TERMINALS:
				return getTerminals();
			case SemanticPackage.ELEMENT__OUTPUT_TERMINALS:
				return getOutputTerminals();
			case SemanticPackage.ELEMENT__INPUT_TERMINALS:
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
			case SemanticPackage.ELEMENT__TERMINALS:
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
			case SemanticPackage.ELEMENT__TERMINALS:
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
			case SemanticPackage.ELEMENT__TERMINALS:
				return terminals != null && !terminals.isEmpty();
			case SemanticPackage.ELEMENT__OUTPUT_TERMINALS:
				return !getOutputTerminals().isEmpty();
			case SemanticPackage.ELEMENT__INPUT_TERMINALS:
				return !getInputTerminals().isEmpty();
		}
		return eDynamicIsSet(eFeature);
	}

} //ElementImpl
