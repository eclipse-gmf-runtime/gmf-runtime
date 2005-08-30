/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.internal.commands;

import java.util.Collections;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLDebugOptions;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLPlugin;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLConstants;
import org.eclipse.gmf.runtime.emf.core.util.ResourceUtil;

/**
 * Encapsulation of a change to some synthetic feature of a {@link Resource}.
 * 
 * @author Christian W. Damus (cdamus)
 * @author rafikj
 */
public abstract class MSLResourceCommand
	extends MSLAbstractCommand {

	private Resource owner = null;

	private int feature = -1;

	/**
	 * Constructor.
	 */
	protected MSLResourceCommand(MSLEditingDomain domain, Resource owner,
			int feature) {

		super(domain);

		this.owner = owner;
		this.feature = feature;
	}

	public void dispose() {

		super.dispose();

		owner = null;
		feature = -1;
	}

	public boolean canExecute() {
		return ((owner != null) && (feature >= 0));
	}

	public boolean canUndo() {
		return ((owner != null) && (feature >= 0));
	}

	public void execute() {

		if (!canExecute()) {

			RuntimeException e = new IllegalStateException(
				"cannot execute EMF command"); //$NON-NLS-1$

			Trace.throwing(MSLPlugin.getDefault(),
				MSLDebugOptions.EXCEPTIONS_THROWING, getClass(), "execute", e); //$NON-NLS-1$

			throw e;
		}
	}

	public final Resource getResource() {
		return owner;
	}

	/**
	 * resolve some EObject.
	 */
	protected EObject resolve(EObject eObject) {

		if (MSLConstants.PROXY_HACK)
			return eObject;

		else {

			if (eObject == null)
				return null;

			if (!eObject.eIsProxy())
				return eObject;

			return EcoreUtil.resolve(eObject, domain.getResourceSet());
		}
	}
	
	public final int getFeatureID() {
		return feature;
	}

	public Set getParticipantTypes() {
		return owner == null ? Collections.EMPTY_SET
			: Collections.singleton(ResourceUtil.getType(owner));

	}
	
	/**
	 * Gets the value of the specified feature of a resource.
	 * 
	 * @param res the resource
	 * @param featureID the feature to get
	 * 
	 * @return the feature's value
	 */
	protected final Object eGet(Resource res, int featureID) {
		Object result = null;
		
		switch (featureID) {
		case Resource.RESOURCE__CONTENTS:
			result = res.getContents();
			break;
		case Resource.RESOURCE__ERRORS:
			result = res.getErrors();
			break;
		case Resource.RESOURCE__IS_LOADED:
			result = Boolean.valueOf(res.isLoaded());
			break;
		case Resource.RESOURCE__IS_MODIFIED:
			result = Boolean.valueOf(res.isModified());
			break;
		case Resource.RESOURCE__IS_TRACKING_MODIFICATION:
			result = Boolean.valueOf(res.isTrackingModification());
			break;
		case Resource.RESOURCE__RESOURCE_SET:
			result = res.getResourceSet();
			break;
		case Resource.RESOURCE__URI:
			result = res.getURI();
			break;
		case Resource.RESOURCE__WARNINGS:
			result = res.getWarnings();
			break;
		}
		
		return result;
	}
}