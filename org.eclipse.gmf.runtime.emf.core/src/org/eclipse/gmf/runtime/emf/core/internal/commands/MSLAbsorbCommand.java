/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.emf.core.internal.commands;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.core.exceptions.MSLRuntimeException;
import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.l10n.ResourceManager;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLDebugOptions;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLPlugin;
import org.eclipse.gmf.runtime.emf.core.resources.CannotAbsorbException;
import org.eclipse.gmf.runtime.emf.core.resources.CannotSeparateException;
import org.eclipse.gmf.runtime.emf.core.resources.ILogicalResource;


/**
 * Command that undoes/redoes absorption of an element from another resource.
 *
 * @author Christian W. Damus (cdamus)
 */
public class MSLAbsorbCommand
	extends MSLBasicCommand {

	private EObject child;
	private ILogicalResource res;
	private Resource subres;
	
	/**
	 * Initializes me.
	 * 
	 * @param domain the editing domain in which I occurred
	 * @param owner the container element that notified
	 * @param feature the containment feature
	 * @param position the index in the containment feature of the absorbed
	 *     object (or -1 if not a list feature)
	 * @param resource the old physical resource of the child element
	 */
	public MSLAbsorbCommand(MSLEditingDomain domain, EObject owner,
			EStructuralFeature feature, int position, Resource resource) {
		super(domain, owner, feature);
		
		if (position < 0) {
			// must be a scalar feature
			this.child = (EObject) owner.eGet(feature);
		} else {
			// no need to worry about proxies in a containment feature
			this.child = (EObject) ((EList) owner.eGet(feature)).get(position);
		}
		
		this.res = (ILogicalResource) child.eResource();
		this.subres = resource;
	}

	public void dispose() {
		super.dispose();
		
		child = null;
		res = null;
		subres = null;
	}
	
	public Type getType() {
		return MCommand.ABSORB;
	}

	public void undo() {
		try {
			res.separate(child, subres.getURI());
		} catch (CannotSeparateException e) {
			RuntimeException re = new MSLRuntimeException(
				ResourceManager.getI18NString(
					"separation.failed_EXC_"), //$NON-NLS-1$
					e);

			Trace.throwing(MSLPlugin.getDefault(),
				MSLDebugOptions.EXCEPTIONS_THROWING, getClass(), "undo", e); //$NON-NLS-1$

			throw re;
		}
	}

	public void redo() {
		try {
			res.absorb(child);
		} catch (CannotAbsorbException e) {
			RuntimeException re = new MSLRuntimeException(
				ResourceManager.getI18NString(
					"absorption.failed_EXC_"), //$NON-NLS-1$
					e);
	
			Trace.throwing(MSLPlugin.getDefault(),
				MSLDebugOptions.EXCEPTIONS_THROWING, getClass(), "redo", e); //$NON-NLS-1$
	
			throw re;
		}
	}
}
