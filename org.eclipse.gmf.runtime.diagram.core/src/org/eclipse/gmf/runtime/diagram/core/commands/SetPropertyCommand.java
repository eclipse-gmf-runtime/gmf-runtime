/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.commands;


import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.internal.l10n.DiagramCoreMessages;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.util.PackageUtil;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.osgi.util.NLS;

/**
 * A command to a set a property of a view
 *  
 * @author melaasar
 */
public class SetPropertyCommand extends AbstractTransactionalCommand {

	static final private String CHANGE_PROPERTY_PATTERN = DiagramCoreMessages.Command_ChangeViewProperty_ChangePropertyPattern; 

	private IAdaptable viewAdapter;
	private String propertyName;
	private String propertyId;
	private Object newValue;

	/**
	 * Creates a set property command with a given label
     * @param editingDomain the editing domain
	 * @param label the command label
	 * @param viewAdapter	the view adapter of the <code>View<code> that owns the property
	 * @param propertyId	the property Id of the property to set
	 * @param newValue		the new value of the property
	 */
	public SetPropertyCommand(TransactionalEditingDomain editingDomain, String label,
            IAdaptable viewAdapter, String propertyId, Object newValue) {
		super(editingDomain, label, null);
		this.viewAdapter = viewAdapter;
		this.propertyId = propertyId;
		this.newValue = newValue;
	}
	
	/**
	 * Creates a set property command with a default label based on property name
	 * @param editingDomain the editing domain
	 * @param viewAdapter	the view adapter of the <code>View<code> that owns the property
	 * @param propertyId	the property Id of the property to set
	 * @param propertyName  the property name 
	 * @param newValue		the new value of the property
	 */
	public SetPropertyCommand(TransactionalEditingDomain editingDomain, IAdaptable viewAdapter, String propertyId, String propertyName, Object newValue) {
		this(editingDomain, "", viewAdapter, propertyId, newValue);
		this.propertyName = propertyName;
	}

	public List getAffectedFiles() {

		if (viewAdapter != null) {
			View view = (View) viewAdapter.getAdapter(View.class);
			if (view != null)
				return getWorkspaceFiles(view);
		}
		return super.getAffectedFiles();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#getLabel()
	 */
	public String getLabel() {
		String label = super.getLabel();
		return (label != null) ? label
			: (NLS.bind(CHANGE_PROPERTY_PATTERN, propertyName));
	}

	protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {
        
		View view = (View) viewAdapter.getAdapter(View.class);
		if (view != null){
			ENamedElement namedElement =  PackageUtil.getElement(propertyId);
			if (namedElement instanceof EStructuralFeature)
				ViewUtil.setStructuralFeatureValue(view,(EStructuralFeature)namedElement, newValue);
		}
		return CommandResult.newOKCommandResult();
	}

	/**
	 * Returns the new value of the property.
	 * @return the new value
	 */
	protected Object getNewValue() {
		return newValue;
	}

	/**
	 * Returns the property Id.
	 * @return property Id
	 */
	protected Object getPropertyId() {
		return propertyId;
	}

	/**
	 * Returns the view Adapter that owns the property.
	 * @return the view adapter
	 */
	protected IAdaptable getViewAdapter() {
		return viewAdapter;
	}

	/**
	 * Gets the property name
	 * @return the property name
	 */
	protected String getPropertyName() {
		return propertyName;
	}

	/**
	 * Sets the newValue that will be set for the property.
	 * @param newValue The newValue to set
	 */
	protected void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

	/**
	 * Sets the propertyId of the property.
	 * @param propertyId The propertyId to set
	 */
	protected void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	/**
	 * Sets the viewAdapter that owns the property.
	 * @param viewAdapter The viewAdapter to set
	 */
	protected void setViewAdapter(IAdaptable viewAdapter) {
		this.viewAdapter = viewAdapter;
	}

	/**
	 * Sets the property name
	 * @param string the property name
	 */
	protected void setPropertyName(String string) {
		propertyName = string;
	}
    
    /**
     * Returns the value of the feature of the property
     * @param view the view to use to get the value
     * @param feature the feature to use
     * @return the value of the property, or <code>null</code>
     */
    protected EStructuralFeature getPropertyStructuralFeature() {
        if (getPropertyId() instanceof String) {
            ENamedElement namedElement = PackageUtil
                .getElement((String) getPropertyId());
            if (namedElement instanceof EStructuralFeature) {
                return (EStructuralFeature) namedElement;
            }
        }
        return null;
    }

}
