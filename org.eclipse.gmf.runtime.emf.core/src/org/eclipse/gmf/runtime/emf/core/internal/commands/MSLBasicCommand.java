/******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.internal.commands;

import java.util.Collections;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLDebugOptions;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLPlugin;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;

/**
 * A basic command is a non-composite command that acts on an EObject at a
 * particular structural feature.
 * 
 * @author rafikj
 */
public abstract class MSLBasicCommand
	extends MSLAbstractCommand {

	private EObject owner = null;

	private EStructuralFeature feature = null;

	/**
	 * Constructor.
	 */
	protected MSLBasicCommand(MSLEditingDomain domain) {
		super(domain);
	}

	/**
	 * Constructor.
	 */
	public MSLBasicCommand(MSLEditingDomain domain, EObject owner,
			EStructuralFeature feature) {

		super(domain);

		this.owner = owner;
		this.feature = feature;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#dispose()
	 */
	public void dispose() {

		super.dispose();

		owner = null;
		feature = null;
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#canExecute()
	 */
	public boolean canExecute() {
		return ((owner != null) && (feature != null));
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#canUndo()
	 */
	public boolean canUndo() {
		return ((owner != null) && (feature != null));
	}

	/**
	 * @see org.eclipse.emf.common.command.Command#execute()
	 */
	public void execute() {

		if (!canExecute()) {

			RuntimeException e = new IllegalStateException(
				"cannot execute EMF command"); //$NON-NLS-1$

			Trace.throwing(MSLPlugin.getDefault(),
				MSLDebugOptions.EXCEPTIONS_THROWING, getClass(), "execute", e); //$NON-NLS-1$

			throw e;
		}
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getOwner()
	 */
	public final EObject getOwner() {
		return owner;
	}

	/**
	 * resolve some EObject.
	 */
	protected EObject resolve(EObject eObject) {
		return eObject;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getFeature()
	 */
	public final EStructuralFeature getFeature() {
		return feature;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getParticipantTypes()
	 */
	public Set getParticipantTypes() {
		return owner == null ? Collections.EMPTY_SET
			: Collections.singleton(EObjectUtil.getType(owner));
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand#getOwner()
	 */
	protected final EObject resolveOwner() {
		owner = resolve(owner);
		return owner;
	}
}