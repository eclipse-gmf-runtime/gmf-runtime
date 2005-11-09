/******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
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

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.gmf.runtime.common.core.command.CommandManager;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeModelCommand;
import org.eclipse.gmf.runtime.emf.ui.properties.commands.RestoreDefaultPropertyValueCommand;
import org.eclipse.gmf.runtime.emf.ui.properties.commands.SetModelPropertyValueCommand;
import org.eclipse.gmf.runtime.emf.ui.properties.internal.l10n.EMFUIPropertiesMessages;

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
     * Constructs a new instance with a command manager for executing property
     * change commands. This instance must be the root entry because only the
     * root entry keeps track of the command manager.
     * 
     * @param manager
     *            the command manager with which this entry will execute
     *            property change commands.
     */
    public UndoableModelPropertySheetEntry(CommandManager manager) {
        commandManager = manager;
    }

    /**
     * The command manager used by this entry to execute property change
     * commands. <code>Null</code> if I am not the root entry. Only the root
     * entry keeps track of the command manager on behalf of all of the child
     * entries.
     */
    private CommandManager commandManager;

    /*
     * (non-Javadoc) Method declared on IPropertySheetEntry.
     */
    public void applyEditorValue() {
    	if (editor == null)
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
        
        /*
        Object newValue = editor.getValue();
        boolean changed = false;
        if (editValue == null) {
            if (newValue != null)
                changed = true;
        } else if (!editValue.equals(newValue))
            changed = true;
            */
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
                restoreCommand = new RestoreDefaultPropertyValueCommand(
                        propertyName, parentValues[i], source, propertyId);
                cc.compose(restoreCommand);
                executeCommand = true;
            }
        }

        if (executeCommand) {
            /* status is ok, can edit the storage units */
            getCommandManager().execute(cc);
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
     */
    protected void valueChanged(UndoableModelPropertySheetEntry child,
            CompositeCommand command) {

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
            command.execute(new NullProgressMonitor());
        }
    }

    /**
     * Gets the command manager used to execute property change commands.
     * 
     * @return <code>CommandManager</code> used to execute property change commands.
     */
    protected CommandManager getCommandManager() {
        return commandManager;
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

        return new SetModelPropertyValueCommand(propertyName, object,
                getPropertySource(object), propertyId, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.common.ui.internal.views.properties.UndoablePropertySheetEntry#getCompositeCommand(java.lang.String)
     */
    protected CompositeCommand getCompositeCommand(String propertyName) {
        return new CompositeModelCommand(propertyName);
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
        return new UndoableModelPropertySheetEntry(getCommandManager());

    }

}