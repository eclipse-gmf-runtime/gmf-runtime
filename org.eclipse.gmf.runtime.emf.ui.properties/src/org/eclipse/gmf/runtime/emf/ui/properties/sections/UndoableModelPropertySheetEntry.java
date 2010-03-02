/******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.ui.properties.sections;

import java.text.MessageFormat;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.TriggeredOperations;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.command.ICompositeCommand;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.ui.properties.commands.RestoreDefaultPropertyValueCommand;
import org.eclipse.gmf.runtime.emf.ui.properties.commands.SetModelPropertyValueCommand;
import org.eclipse.gmf.runtime.emf.ui.properties.internal.EMFPropertiesDebugOptions;
import org.eclipse.gmf.runtime.emf.ui.properties.internal.EMFPropertiesPlugin;
import org.eclipse.gmf.runtime.emf.ui.properties.internal.EMFPropertiesStatusCodes;
import org.eclipse.gmf.runtime.emf.ui.properties.internal.l10n.EMFUIPropertiesMessages;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * An property sheet entry for elements in the model. The changes to the model
 * element property are done through a model command executed by the
 * <code>CommandManager</code> so that the changes can be undone by undo
 * interval ID.
 * 
 * @author ldamus
 * @author nbalaba
 */
public class UndoableModelPropertySheetEntry extends PropertySheetEntry {

    /**
     * The operation history used by this entry to execute property change
     * commands. <code>Null</code> if I am not the root entry. Only the root
     * entry keeps track of the history on behalf of all of the child
     * entries.
     */
    private final IOperationHistory operationHistory;
    
    /**
     * My editing domain.
     */
    private TransactionalEditingDomain editingDomain;
    
    /**
     * Intializes me with an operation history through
     * which property change commands will be executed, undone and redone.
     * 
     * @param operationHistory
     *            my operation history
     */
    public UndoableModelPropertySheetEntry(IOperationHistory operationHistory) {
        this.operationHistory = operationHistory;
    }
    
    /**
     * Sets my editing domain.
     * 
     * @param editingDomain
     *            my editing domain
     */
    public void setEditingDomain(TransactionalEditingDomain editingDomain) {
        this.editingDomain = editingDomain;
    }
    
    /**
     * Gets my editing domain. The root entry stores the editing domain.
     * 
     * @return my editing domain
     */
    public TransactionalEditingDomain getEditingDomain() {

        UndoableModelPropertySheetEntry parentEntry = getParentEntry();

        if (parentEntry == null || editingDomain != null) {
            return editingDomain;
        }

        return parentEntry.getEditingDomain();
    }

    /*
     * (non-Javadoc) Method declared on IPropertySheetEntry.
     */
    public void applyEditorValue() {
    	if (editor == null || !editor.isActivated())
    	 	return;
        
    	 if (!editor.isValueValid()) {
            setErrorText(editor.getErrorMessage());
            return;
        } else 
            setErrorText(null);


        // See if the value changed and if so update
        
        Object newValue = editor.getValue();
        boolean changed = false;
        if (values.length > 1) {
            changed = true;
        } else if (editValue == null) {
            if (newValue != null)
                changed = true;
        } else if (!editValue.equals(newValue))
            changed = true;

        // Set the editor value
        if (changed)
            setValue(newValue);

    }

    /*
     * (non-Javadoc) Method declared on IUndoablePropertySheetEntry.
     */
    public void resetPropertyValue() {

        // The root entry does not have a default value
        if (parent == null) {
            return;
        }

        //Use our parent's values to reset our values.
        String propertyName = getDescriptor().getDisplayName();
        Object propertyId = getDescriptor().getId();
        CompositeCommand cc = new CompositeCommand(propertyName);
        RestoreDefaultPropertyValueCommand restoreCommand;
        boolean executeCommand = false;
        Object[] parentValues = getParentEntry().getValues();

        for (int i = 0; i < parentValues.length; i++) {

            IPropertySource source = getPropertySource(parentValues[i]);

            if (source.isPropertySet(propertyId)) {
                restoreCommand = new RestoreDefaultPropertyValueCommand(getEditingDomain(), 
                        propertyName, parentValues[i], source, propertyId);
                cc.compose(restoreCommand);
                executeCommand = true;
            }
        }

        if (executeCommand) {
            /* status is ok, can edit the storage units */
            try {
                TriggeredOperations triggerOperation = 
                    new TriggeredOperations(cc, getOperationHistory());
                getOperationHistory().execute(triggerOperation, new NullProgressMonitor(), null);
                
            } catch (ExecutionException e) {
                Trace.catching(EMFPropertiesPlugin.getDefault(),
                    EMFPropertiesDebugOptions.EXCEPTIONS_CATCHING,
                    UndoableModelPropertySheetEntry.class,
                    "resetPropertyValue", e); //$NON-NLS-1$
                Log.error(EMFPropertiesPlugin.getDefault(),
                    EMFPropertiesStatusCodes.COMMAND_FAILURE, e
                        .getLocalizedMessage(), e);
            }
            refreshValues();
        }
    }

    /**
     * Set the value for this entry.
     * <p>
     * We set the given value as the value for all our value objects. We then
     * call our parent to update the property we represent with the given value.
     * We then trigger a model refresh.
     * <p>
     * 
     * @param newValue
     *            the new value
     */
    protected void setValue(Object newValue) {

        // Set the value
        for (int i = 0; i < values.length; i++)
            values[i] = newValue;

        // Inform our parent
        getParentEntry().valueChanged(
                this,
                getCompositeCommand(MessageFormat.format(
                		EMFUIPropertiesMessages.UndoablePropertySheetEntry_commandName,
                        new String[] { getDescriptor().getDisplayName() })));
        
        // Remember the new value so that we don't apply this same value more
        // than once.
        editValue = newValue;
    }

    /**
     * The value of the given child entry has changed. Therefore we must set
     * this change into our value objects.
     * <p>
     * We must inform our parent so that it can update its value objects
     * </p>
     * <p>
     * Subclasses may override to set the property value in some custom way.
     * </p>
     * 
     * @param child
     *            the child entry that changed its value
     * @param command
     *            the command into which to compose my property chnage command
     */
    protected void valueChanged(UndoableModelPropertySheetEntry child,
            ICommand command) {

        String propertyName = child.getDescriptor().getDisplayName();

        Object propertyId = child.getDescriptor().getId();

        for (int i = 0; i < values.length; i++)
            command.compose(getPropertyCommand(propertyName, values[i],
                    propertyId, child.getEditValue(i)));

        // inform our parent
        if (getParentEntry() != null) {
            getParentEntry().valueChanged(this, command);
        } else {
            //I am the root entry
            try {
                TriggeredOperations triggerOperation = 
                    new TriggeredOperations(command, getOperationHistory());
                getOperationHistory().execute(triggerOperation, new NullProgressMonitor(), null);
          
            } catch (ExecutionException e) {
                Trace.catching(EMFPropertiesPlugin.getDefault(),
                    EMFPropertiesDebugOptions.EXCEPTIONS_CATCHING,
                    UndoableModelPropertySheetEntry.class,
                    "valueChanged", e); //$NON-NLS-1$
                Log.error(EMFPropertiesPlugin.getDefault(),
                    EMFPropertiesStatusCodes.COMMAND_FAILURE, e
                        .getLocalizedMessage(), e);
            }
        }
    }
    
    /**
     * Extracts the editing domain from the <code>objects</code> if I am the
     * root entry.
     */
    public void setValues(Object[] objects) {
        super.setValues(objects);

        if (getParentEntry() == null) {
            // I'm the root
            for (int i = 0; i < objects.length; i++) {
                EObject eObject = null;

                if (objects[i] instanceof EObject) {
                    eObject = (EObject) objects[i];

                } else if (objects[i] instanceof IAdaptable) {
                    eObject = (EObject) ((IAdaptable) objects[i])
                        .getAdapter(EObject.class);
                }

                if (eObject != null) {
                    setEditingDomain(TransactionUtil.getEditingDomain(eObject));
                }
            }
        }
    }
    
    /**
     * Gets my operation history.
     * 
     * @return my operation history
     */
    protected final IOperationHistory getOperationHistory() {
        return operationHistory;
    }

    /**
     * Returns the parent. This can be another <code>PropertySheetEntry</code>
     * or <code>null</code>.
     */
    protected UndoableModelPropertySheetEntry getParentEntry() {
        return (UndoableModelPropertySheetEntry) parent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.common.ui.internal.views.properties.UndoablePropertySheetEntry#getPropertyCommand(java.lang.String,
     *      org.eclipse.ui.views.properties.IPropertySource, java.lang.Object,
     *      java.lang.Object)
     */
    protected ICommand getPropertyCommand(String propertyName, Object object,
            Object propertyId, Object value) {

        return new SetModelPropertyValueCommand(getEditingDomain(), propertyName, object,
                getPropertySource(object), propertyId, value);
    }

    protected ICompositeCommand getCompositeCommand(String propertyName) {
        return new CompositeTransactionalCommand(getEditingDomain(), propertyName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.common.ui.internal.views.properties.UndoablePropertySheetEntry#createChildEntries(int)
     */
    protected PropertySheetEntry[] createChildEntries(int size) {
        return new UndoableModelPropertySheetEntry[size];
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.common.ui.internal.views.properties.UndoablePropertySheetEntry#createChildEntry()
     */
    protected PropertySheetEntry createChildEntry() {
        return new UndoableModelPropertySheetEntry(getOperationHistory());

    }

}