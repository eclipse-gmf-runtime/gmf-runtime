/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.l10n.ResourceManager;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLPlugin;
import org.eclipse.gmf.runtime.emf.core.internal.resources.MResource;
import org.eclipse.gmf.runtime.emf.core.internal.util.ConstraintStatusAdapter;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLUtil;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.emf.core.util.ProxyUtil;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;

/**
 * The implementation of the <code>IEObjectHelper</code> interface.
 * 
 * @canBeSeenBy %partners
 */
public class EObjectHelper
	implements IEObjectHelper {
	
	/** Localized message key for the "multiple violations" message. */
	private static final String MULTI_VIOLATIONS_KEY = "validation.multi"; //$NON-NLS-1$
	
	/** Localized message key for the "no validation required" message. */
	private static final String NO_VALIDATION_KEY = "validation.none"; //$NON-NLS-1$
	
	private static final IStatus OK_STATUS = new Status(IStatus.OK, MSLPlugin
		.getPluginId(), 0, ResourceManager.getI18NString(NO_VALIDATION_KEY),
		null);

	/**
	 * The <code>EditingDomain</code> that owns the elements managed by the
	 * helper
	 */
	private MEditingDomain editingDomain;

	private IBatchValidator validator;
	
	/**
	 * Constructor
	 * 
	 * @param editingDomainIn
	 *            The <code>EditingDomain</code> owning the elements
	 */
	public EObjectHelper(MEditingDomain editingDomainIn) {
		editingDomain = editingDomainIn;
	}

	/**
	 * Returns the <code>editingDomain</code>
	 * 
	 * @return The <code>EditingDomain</code>
	 */
	private MEditingDomain getEditingDomain() {
		return editingDomain;
	}

	/*
	 * Object Utilities
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.core.IEObjectHelper#destroy(org.eclipse.emf.ecore.EObject)
	 */
	public void destroy(EObject eObject) {

		EObject container = eObject.eContainer();

		if (container != null) {

			Resource resource = container.eResource();

			if (resource instanceof MResource)
				((MResource) resource).getHelper().destroy(getEditingDomain(),
					eObject);
			else
				MSLUtil.destroy((MSLEditingDomain) getEditingDomain(), eObject,
					0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.core.IEObjectHelper#getID(org.eclipse.emf.ecore.EObject)
	 */
	public String getID(EObject eObject) {
		return EObjectUtil.getID(eObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.core.IEObjectHelper#getName(org.eclipse.emf.ecore.EObject)
	 */
	public String getName(EObject eObject) {
		return EObjectUtil.getName(eObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.core.IEObjectHelper#setName(org.eclipse.emf.ecore.EObject,
	 *      java.lang.String)
	 */
	public boolean setName(EObject eObject, String name) {
		return EObjectUtil.setName(eObject, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.core.IEObjectHelper#getQName(org.eclipse.emf.ecore.EObject,
	 *      boolean)
	 */
	public String getQName(EObject eObject, boolean formatted) {
		return EObjectUtil.getQName(eObject, formatted);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.core.IEObjectHelper#getReferencers(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecore.EReference[])
	 */
	public Collection getReferencers(EObject eObject, EReference[] features) {

		return EObjectUtil.getReferencers(eObject, features);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.core.IEObjectHelper#validate(java.util.Collection,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IStatus validate(Collection objects, IProgressMonitor monitor) {

		if (null == objects) {
			throw new NullPointerException("Argument 'objects' is null"); //$NON-NLS-1$
		}
		
		Collection statuses = new ArrayList();
		
		// let clients be uninterested in progress
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		
		// We will now call upon the EMF Validation service
		//  to validate this action and return the status of
		//  that validation.
		statuses.add(ConstraintStatusAdapter
			.wrapStatus(getValidator().validate(objects,monitor)));
		
		return combineValidationResults(statuses);
	}
	
	private IBatchValidator getValidator() {
		if (validator == null) {
			validator = (IBatchValidator) ModelValidationService
				.getInstance().newValidator(EvaluationMode.BATCH);
			
			validator.setIncludeLiveConstraints(true);
			validator.setReportSuccesses(false);
			validator.putClientData("editingDomain", getEditingDomain()); //$NON-NLS-1$
		}
		
		return validator;
	}
	
	/**
	 * Combines a collection of {@link IStatus} results coming
	 *  from various validations into a single status.
	 * 
	 * @param statuses a collection of {@link IStatus}es
	 * @return a single, all-encompassing, status object
	 */
	private static IStatus combineValidationResults(Collection statuses) {
		List result = new java.util.ArrayList();

		for (Iterator iter = statuses.iterator(); iter.hasNext();) {
			IStatus next = (IStatus) iter.next();
			if( next != null ) {
				if (next.isMultiStatus()) {
					IStatus[] children = next.getChildren();
					for (int i = 0; i < children.length; i++) {
						result.add(children[i]);
					}
				} else {
					result.add(next);
				}
			}
		}

		if (result.isEmpty()) {
			return OK_STATUS;
		} else if (result.size() == 1) {
			return (IStatus) result.get(0);
		} else {
			return new MultiStatus(MSLPlugin.getPluginId(), 0,
				(IStatus[]) result.toArray(new IStatus[result.size()]),
				ResourceManager.getI18NString(MULTI_VIOLATIONS_KEY), null);
		}
	}

	/*
	 * Proxy Utilities
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.core.IEObjectHelper#getProxyName(org.eclipse.emf.ecore.InternalEObject)
	 */
	public String getProxyName(InternalEObject eObject) {
		return ProxyUtil.getProxyName(eObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.core.IEObjectHelper#getProxyQName(org.eclipse.emf.ecore.InternalEObject)
	 */
	public String getProxyQName(InternalEObject eObject) {
		return ProxyUtil.getProxyQName(eObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.core.IEObjectHelper#getProxyID(org.eclipse.emf.ecore.InternalEObject)
	 */
	public String getProxyID(InternalEObject eObject) {
		return ProxyUtil.getProxyID(eObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.core.IEObjectHelper#getProxyClass(org.eclipse.emf.ecore.InternalEObject)
	 */
	public EClass getProxyClass(InternalEObject eObject) {
		return ProxyUtil.getProxyClass(eObject);
	}
}