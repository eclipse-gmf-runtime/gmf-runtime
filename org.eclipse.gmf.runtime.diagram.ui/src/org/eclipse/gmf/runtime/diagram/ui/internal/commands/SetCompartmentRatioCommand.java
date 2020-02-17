/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.commands.SetPropertyCommand;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.emf.core.util.PackageUtil;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.Ratio;
import org.eclipse.gmf.runtime.notation.View;


/**
 * Sets the ratio for the resizable compartment, where the default ratio is -1
 * This command might create/destroy the Ratio layout constraint of the compartment
 * It will destroy it if the ratio is set to default and there is a ratio
 * It will create it if the ratio is not default and there was no ratio 
 * otherwise it will just set the ratio's value
 * 
 * @author melaasar
 */
public class SetCompartmentRatioCommand extends SetPropertyCommand {

	/**
	 * constructor
     * @param editingDomain the editing domain
	 * @param viewAdapter adapter that can adapt to <code>View.class</code>
	 * @param newValue the new value of the ratio
	 */
	public SetCompartmentRatioCommand(TransactionalEditingDomain editingDomain,
            IAdaptable viewAdapter, double newValue) {
		super(editingDomain, viewAdapter, Properties.ID_RATIO, Properties.ID_RATIO, new Double(newValue));
	}

	protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {
        
		View view = (View)getViewAdapter().getAdapter(View.class);
		if (view != null) {
			Node node = (Node)view;
			Ratio ratio = (Ratio) node.getLayoutConstraint(); 

			if (((Double)getNewValue()).doubleValue() == -1) {
				if (ratio != null)
					DestroyElementCommand.destroy(ratio);
			} else {
				if (ratio == null) {
					ratio = NotationFactory.eINSTANCE.createRatio();
					node.setLayoutConstraint(ratio);
				}
				if (getPropertyId() instanceof String){
					ENamedElement namedElement = PackageUtil.getElement((String)getPropertyId());
					if (namedElement instanceof EStructuralFeature)
						ViewUtil.setStructuralFeatureValue(view,(EStructuralFeature)namedElement, getNewValue());
				}
				
			}
		}
		return CommandResult.newOKCommandResult();
	}

}
