/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers;

import java.util.Iterator;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.CircuitEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.LEDEditPart;
import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.IPopupMenuContributionPolicy;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * Class that helps to determine whether "Ports Color" menu item should be
 * contributed to the context menu
 * 
 * @author aboyko
 * 
 */
public class LogicPortsColorPopupPolicy implements IPopupMenuContributionPolicy {

	public boolean appliesTo(ISelection selection,
			IConfigurationElement configuration) {
		if (!selection.isEmpty() && selection instanceof StructuredSelection) {
			for (Iterator itr = ((StructuredSelection) selection).iterator(); itr
					.hasNext();) {
				Object obj = itr.next();
				if (!(obj instanceof LEDEditPart || obj instanceof CircuitEditPart))
					return false;
			}
			return true;
		}
		return false;
	}

}
