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

package org.eclipse.gmf.runtime.emf.type.core.commands;

import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;

/**
 * Command to set the value of a feature of a model element.
 * 
 * @author ldamus
 */
public class SetValueCommand
	extends EditElementCommand {

	/**
	 * The feature whose value should be set.
	 */
	private final EStructuralFeature feature;

	/**
	 * The new value.
	 */
	private final Object value;

	/**
	 * Constructs a new command to set the value of a feature of a model
	 * element.
	 * 
	 * @param request
	 *            the set value request
	 */
	public SetValueCommand(SetRequest request) {
		super(request.getLabel(), request.getElementToEdit(), request);

		this.feature = request.getFeature();
		this.value = request.getValue();
	}

	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		if (feature.isMany()) {
			((Collection) getElementToEdit().eGet(feature)).add(value);

		} else {
			getElementToEdit().eSet(feature, value);
		}
		return newOKCommandResult();
	}

}