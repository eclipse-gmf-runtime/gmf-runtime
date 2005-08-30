/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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