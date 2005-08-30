/*
 * Created on Sep 17, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.commands;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.commands.SetPropertyCommand;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
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
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class SetCompartmentRatioCommand extends SetPropertyCommand {

	/**
	 * constructor
	 * @param viewAdapter adapter that can adapt to <code>View.class</code>
	 * @param newValue the new value of the ratio
	 */
	public SetCompartmentRatioCommand(IAdaptable viewAdapter, double newValue) {
		super(viewAdapter, Properties.ID_RATIO, Properties.ID_RATIO, new Double(newValue));
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		View view = (View)getViewAdapter().getAdapter(View.class);
		if (view != null) {
			Node node = (Node)view;
			Ratio ratio = (Ratio) node.getLayoutConstraint(); 

			if (((Double)getNewValue()).doubleValue() == -1) {
				if (ratio != null)
					EObjectUtil.destroy(ratio);
			} else {
				if (ratio == null) {
					ratio = NotationFactory.eINSTANCE.createRatio();
					node.setLayoutConstraint(ratio);
				}
				ViewUtil.setPropertyValue(view,getPropertyId(), getNewValue());
			}
		}
		return newOKCommandResult();
	}

}
