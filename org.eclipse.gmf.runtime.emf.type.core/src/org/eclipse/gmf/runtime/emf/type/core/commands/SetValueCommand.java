/******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
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
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;

/**
 * Command to set the value of a feature of a model element.
 * 
 * @author ldamus, mmostafa
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

	protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
	    throws ExecutionException {

        EObject elementToEdit = getElementToEdit();
        boolean many = FeatureMapUtil.isMany(elementToEdit,feature);
        if (many) {
            Collection collection = ((Collection)elementToEdit.eGet(feature));
            if (value instanceof List){
                List values = (List)value;
                collection.clear();
                collection.addAll(values);
            }else {
                collection.add(value);
            }
        } else {
            getElementToEdit().eSet(feature, value);
        }
        return CommandResult.newOKCommandResult();
	}
    
    /**
     * Checks that the feature is a modifiable feature of the element whose
     * value will be set by this command. Also checks that the new value is of
     * the correct type for the feature.
     */
    public boolean canExecute() {
        EObject elementToEdit = getElementToEdit();
        if (elementToEdit == null || !super.canExecute()) {
            return false;
        }
        boolean many = FeatureMapUtil.isMany(elementToEdit, feature);
        if (value == null && many) {
            return false;
        }
        List allFeatures = getElementToEdit().eClass()
            .getEAllStructuralFeatures();
        if (allFeatures.contains(feature) && feature.isChangeable()){
            if (!many && (value==null || feature.getEType().isInstance(value)))
                return true;
            else {
                return verifyMany();
            }
        }
        return false;
    }
    
    private boolean verifyMany() {
        if (value instanceof List){
            List values = (List)value;
            for (Iterator iter = values.iterator(); iter.hasNext();) {
                Object element = iter.next();
                if (!feature.getEType().isInstance(element))
                    return false;
            }
            return true;
        }
        return feature.getEType().isInstance(value);
    }

}