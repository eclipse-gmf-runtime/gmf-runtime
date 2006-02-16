/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.ui.properties.descriptors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.ui.celleditor.ExtendedComboBoxCellEditor;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptorDecorator;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.emf.transaction.impl.InternalTransaction;
import org.eclipse.emf.transaction.impl.InternalTransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.common.ui.services.properties.descriptors.ICompositeSourcePropertyDescriptor;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLAdapterFactoryManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

/**
 * The descriptor that works with the <code>EMFCompositePropertySource</code>
 * object.
 * <P>
 * When the property value is set while an EMF transaction is open on the
 * editing domain, I will not execute the set command through the editing domain
 * command stack. In this case, I rely on whoever opened the EMF transaction to
 * put their operation on the operation history.
 * 
 * @author nbalaba
 */
public class EMFCompositeSourcePropertyDescriptor extends PropertyDescriptor
        implements ICompositeSourcePropertyDescriptor {

    protected String category = null;

    protected String[] filterFlags;

    protected boolean readOnly = false;

    protected CellEditor propertyEditor = null;

    /**
     * The object to validate the values in the cell editor, or
     * <code>null</code> if none (the default).
     */
    protected ICellEditorValidator validator;

    /**
     * A convinience method to create an instance of a ILabelProvider using
     * IItemPropertyDescriptor
     * 
     * @param itemPropertyDescriptor
     * @return label provider
     */
    public static ILabelProvider createLabelProvider(
            IItemPropertyDescriptor itemPropertyDescriptor) {
        final IItemLabelProvider itemLabelProvider = itemPropertyDescriptor
                .getLabelProvider(null);
        return new LabelProvider() {
            public String getText(Object object) {
                return itemLabelProvider.getText(object);
            }

            public Image getImage(Object object) {
                return ExtendedImageRegistry.getInstance().getImage(
                        itemLabelProvider.getImage(object));
            }
        };
    }

    /**
     * @param object
     * @param itemPropertyDescriptor
     */
    public EMFCompositeSourcePropertyDescriptor(Object object,
            IItemPropertyDescriptor itemPropertyDescriptor) {
        super(object, itemPropertyDescriptor);

        filterFlags = itemPropertyDescriptor.getFilterFlags(object);
        filterFlags = filterFlags == null ? new String[0] : filterFlags;

    }

    /**
     * Creates an instance of <code>EMFCompositeSourcePropertyDescriptor</code>.
     * 
     * @param object
     * @param itemPropertyDescriptor
     * @param category 
     */
    public EMFCompositeSourcePropertyDescriptor(Object object,
            IItemPropertyDescriptor itemPropertyDescriptor, String category) {
        this(object, itemPropertyDescriptor);
        setCategory(category);
    }

    /**
     * Sets the category.
     * 
     * @param category The category to set.
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.IPropertyDescriptor#getCategory()
     */
    public String getCategory() {

        String aCategory = super.getCategory();
        return aCategory == null ? this.category : aCategory;

    }

    /**
     * @return Returns the filterFlags.
     */
    public String[] getFilterFlags() {
        return filterFlags;
    }

    /**
     * Add filter flag (@see IPropertySheetEntry.FILTER_ID_EXPERT)
     * 
     * @param flag
     */
    public void addFilterFlag(String flag) {
        if (!Arrays.asList(filterFlags).contains(flag)) {
            String[] flags = new String[filterFlags.length + 1];
            System.arraycopy(filterFlags, 0, flags, 0, filterFlags.length);
            flags[filterFlags.length] = flag;
            filterFlags = flags;
        }
    }

    /**
     * This returns the cell editor that will be used to edit the value of this
     * property. This default implementation determines the type of cell editor
     * from the nature of the structural feature.
     */
    public CellEditor createPropertyEditor(Composite composite) {

        if (isReadOnly())
            return null;

        if (getPropertyEditor() != null)
            return getPropertyEditor();

        return doCreateEditor(composite);
    }

    /*
     * A cxell editor creation method - after the assertions. Allows subclasses
     * override just the part where the editor actually is created, without
     * having to repeat preliminary assertions in every subclass
     * 
     * @param composite @return
     */
    protected CellEditor doCreateEditor(Composite composite) {
        CellEditor result = null;
        Object genericFeature = getFeature();

        if (genericFeature instanceof EReference[]) {
            result = createComboBoxCellEditor(composite);
        } else if (genericFeature instanceof EStructuralFeature) {

            final EStructuralFeature feature = (EStructuralFeature) genericFeature;
            final EClassifier eType = feature.getEType();
            final Collection choiceOfValues = getChoiceOfValues();

            if (!choiceOfValues.isEmpty()) {

                if (feature.isMany()) {
                    boolean valid = true;
                    for (Iterator i = choiceOfValues.iterator(); i.hasNext();) {
                        Object choice = i.next();
                        if (!eType.isInstance(choice)) {
                            valid = false;
                            break;
                        }
                    }
                    if (valid)
                        result = createComboBoxCellEditor(composite);
                }
                if (result == null)
                    result = createComboBoxCellEditor(composite);

            } else {
                if (eType instanceof EDataType) {

                    EDataType eDataType = (EDataType) eType;
                    if (eDataType.isSerializable()) {
                        if (feature.isMany()) {
                            result = createComboBoxCellEditor(composite);
                        } else if (eDataType == EcorePackage.eINSTANCE
                                .getEBoolean()
                                || eDataType == EcorePackage.eINSTANCE
                                        .getEBooleanObject()
                                || eDataType.getInstanceClass() == EcorePackage.eINSTANCE
                                        .getEBoolean().getInstanceClass())

                        {
                            result = createBooleanCellEditor(composite);
                        } else {
                            result = createDataTypeCellEditor(composite);
                        }
                    }
                }
            }
        }

        return result == null ? super.createPropertyEditor(composite) : result;
    }

    /**
     * Returns boolean - an indicator either or not this property can be set or
     * re-set. It was either inherited from our IItemPropertyDescriptor or set
     * in the context of the application
     * 
     * @return - true if either IItemPropertyDescriptor can not set it or our
     *         descriptor
     */
    public boolean isReadOnly() {
        return (!itemPropertyDescriptor.canSetProperty(object)) || readOnly;
    }

    /**
     * Sets the specified value to be read only.
     * 
     * @param value
     *            The readOnly to set.
     */
    public void setReadOnly(boolean value) {
        this.readOnly = value;
    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertyDescriptor#isCompatibleWith(IPropertyDescriptor)
     */

    public boolean isCompatibleWith(IPropertyDescriptor anotherProperty) {

        if (this == anotherProperty)
            return true;

        if (!(anotherProperty instanceof EMFCompositeSourcePropertyDescriptor))
            return false;

        EMFCompositeSourcePropertyDescriptor descriptor = (EMFCompositeSourcePropertyDescriptor) anotherProperty;

        if (getFeature() == descriptor.getFeature())
            //   && isCompatibleTypes(descriptor))
            return (getCategory().equals(descriptor.getCategory()));

        return false;

    }

/*    private boolean isCompatibleTypes(
            EMFCompositeSourcePropertyDescriptor descriptor) {
        return ((EObject) getObject()).eClass().isInstance(
                (descriptor.getObject()))
                || ((EObject) descriptor.getObject()).eClass().isInstance(
                        (getObject()));
    }
*/
    /**
     * Retrieve an object for which this class is a property source.
     * 
     * @return <code>Object</code> for which this class is a property source 
     */
    protected Object getObject() {
        return object;
    }

    /**
     * @return the item descriptor
     */
    public IItemPropertyDescriptor getItemDescriptor() {
        return itemPropertyDescriptor;
    }

    /**
     * Retrieves the feature.
     * 
     * @return feature
     */
    public Object getFeature() {
        return itemPropertyDescriptor.getFeature(getObject());
    }

    /**
     * @return Returns the propertyEditor.
     */
    protected CellEditor getPropertyEditor() {
        return propertyEditor;
    }

    /**
     * @param propertyEditor
     *            The propertyEditor to set.
     */
    public void setPropertyEditor(CellEditor propertyEditor) {
        this.propertyEditor = propertyEditor;
    }

    /**
     * @return Returns the validator.
     */
    public ICellEditorValidator getValidator() {
        return validator;
    }

    /**
     * @param validator
     *            The validator to set.
     */
    public void setValidator(ICellEditorValidator validator) {
        this.validator = validator;
    }

    /*
     * @param composite @return
     */
    protected CellEditor createComboBoxCellEditor(Composite composite) {

        return new ExtendedComboBoxCellEditor(composite, new ArrayList(
                getChoiceOfValues()), getLabelProvider(), true);

    }

    /*
     * @param composite @return
     */
    protected CellEditor createBooleanCellEditor(Composite composite) {
        return new ExtendedComboBoxCellEditor(composite,
                Arrays.asList(new Object[] { Boolean.FALSE,
                          Boolean.TRUE }), getLabelProvider(), true);
    }

    /*
     * @param composite @return
     */
    protected CellEditor createDataTypeCellEditor(Composite composite) {
        Object genericFeature = itemPropertyDescriptor.getFeature(object);
        if (genericFeature instanceof EStructuralFeature) {

            EClassifier eType = ((EStructuralFeature) genericFeature)
                    .getEType();
            if (eType instanceof EDataType) {
                return new EDataTypeCellEditor((EDataType) eType, composite) {
                    protected void focusLost() {
                        if (isActivated()) {                     
                            deactivate();
                        }
                    }
                };
            }
        }
        return null;
    }

    /**
     * @return - value of the property stored in the EMF descriptor
     */
    public Object getPropertyValue() {

        return getEditableValue(); //getItemDescriptor().getPropertyValue(getObject());//
    }

    /**
     * This is a temporary method. If we want to keep the recursive properties
     * this become getPropertyValue().
     * 
     * @return the editable value
     */
    protected Object getEditableValue() {
        Object aValue = getItemDescriptor().getPropertyValue(getObject());
        //		 see if we should convert the value to an editable value

        IItemPropertySource itemSource = getPropertySource(aValue);
        if (itemSource != null) {
            aValue = itemSource.getEditableValue(aValue);
        }

        return aValue;
    }

    /**
     * @param value
     *            value of the property
     */
    public void setPropertyValue(final Object value) {
        //if (value == null) // hack - due to the bug in EMF
        //return;

        Object oldValue = getEditableValue();

        // here we get into the infinite recursive loop
        // because of the emf edit even generation - need to stop
        // if the new value is the same as the old one
        if ((oldValue != null && oldValue.equals(value))
                || (oldValue == null && value == null))
            return;
        
        
        // TODO Temporary fix for https://bugs.eclipse.org/bugs/show_bug.cgi?id=127528
        // 
        // Once https://bugs.eclipse.org/bugs/show_bug.cgi?id=128117 is fixed, 
        // TransactionalOperationItemPropertyDescriptor can be deleted and the 
        // following line can be used instead:
        //
        // getItemDescriptor().setPropertyValue(getObject(), value);
        //
        
        InternalTransactionalEditingDomain editingDomain = (InternalTransactionalEditingDomain) TransactionUtil
            .getEditingDomain(getObject());

        InternalTransaction transaction = editingDomain.getActiveTransaction();

        if (transaction != null && !transaction.isReadOnly()) {
            // we're executing an EMF transactional operation, so use a property
            // descriptor that won't execute the EMF set command through the
            // editing domain command stack
            ItemPropertyDescriptorDecorator decorator = new TransactionalOperationItemPropertyDescriptor(
                editingDomain, getObject(), itemPropertyDescriptor);
            decorator.setPropertyValue(getObject(), value);

        } else {
            // no transaction open so execute the EMF set command through the
            // editing domain command stack
            getItemDescriptor().setPropertyValue(getObject(), value);
        } 
    }

    /**
     * 
     */
    public void resetPropertyValue() {

        getItemDescriptor().resetPropertyValue(getObject());

    }

    /**
     * Returns an property source for the given value.
     * 
     * @object an object for which to obtain a property source or
     *         <code>null</code> if a property source is not available
     * @return an property source for the given object
     */
    protected IItemPropertySource getPropertySource(Object value) {

        return (IItemPropertySource) MSLAdapterFactoryManager
                .getAdapterFactory().adapt(value, IItemPropertySource.class);

    }

    /*
     * Override superclass behavior by returning a feature as a property id. The
     * superclass takes ItemPropertyDescritor id as property id. The
     * ItemPropertyDescriptor returns a display name as a property id. The
     * Notation properties will contain Styles - which is a list of style
     * objects. The Styles are presented as a single object with flattened
     * properties - each style object on the list contributes to the properties
     * of the Style. For that each property must have a unique id. This is why
     * we need to override and return a feature - since feature objects are
     * singletons and quaranteed to be unique.
     * 
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.IPropertyDescriptor#getId()
     */
    public Object getId() {
        return getFeature();
    }

    /**
     * Return choice of values for the given property.
     * 
     * @return list containing list of values
     */
    public List getChoiceOfValues() {
        Collection types = itemPropertyDescriptor
                .getChoiceOfValues(getObject());
        return types == null ? new ArrayList() : new ArrayList(types);
    }

    /**
     * Resets the property to specified value.
     * 
     * @param value the new property value
     */
    public void resetPropertyValue(Object value) {
      getItemDescriptor().resetPropertyValue(getObject());
        
    }
    
    /**
     * Property descriptor decorator that overrides
     * {@link #setPropertyValue(Object, Object)} to execute the EMF commands
     * directly, rather than through the EMF command stack.
     * <P>
     * This decorator should only be used when the properties are being changed
     * through an {@link AbstractTransactionalCommand}.
     * 
     * @author ldamus
     */
    protected class TransactionalOperationItemPropertyDescriptor
        extends ItemPropertyDescriptorDecorator {

        private final EditingDomain editingDomain;

        /**
         * Initializes me with my editing domain, the object whose properties I
         * describe and my item property descriptor delegate.
         * 
         * @param editingDomain
         *            the editing domain
         * @param object
         *            the object whose properties I describe
         * @param itemPropertyDescriptor
         *            the delegate
         */
        public TransactionalOperationItemPropertyDescriptor(
                EditingDomain editingDomain, Object object,
                IItemPropertyDescriptor itemPropertyDescriptor) {

            super(object, itemPropertyDescriptor);
            this.editingDomain = editingDomain;
        }

        /**
         * Sets the property value without executing commands on the editing
         * domain command stack.
         */
        public void setPropertyValue(Object thisObject, Object newValue) {

            if (editingDomain == null) {
                // no editing domain, so no my delegate will not execute a
                // command through the command stack
                itemPropertyDescriptor.setPropertyValue(thisObject, newValue);
                return;
            }

            EObject eObject = (EObject) this.object;

            Object owner = null;

            if (getItemDescriptor() instanceof ItemPropertyDescriptor) {
                owner = ((ItemPropertyDescriptor) getItemDescriptor())
                    .getCommandOwner();
            }

            Object commandOwner = (owner != null) ? owner
                : eObject;

            Object featureObject = getFeature(eObject);

            if (featureObject instanceof EReference[]) {
                EReference[] parentReferences = (EReference[]) featureObject;
                Command removeCommand = null;

                for (int i = 0; i < parentReferences.length; ++i) {
                    Object formerValue = eObject.eGet(parentReferences[i]);

                    if (formerValue != null) {
                        final EReference parentReference = parentReferences[i];

                        if (formerValue == newValue) {
                            return;

                        } else if (parentReference.getEType().isInstance(
                            newValue)) {

                            SetCommand.create(editingDomain, commandOwner,
                                parentReference, newValue).execute();
                            return;

                        } else {
                            removeCommand = SetCommand.create(editingDomain,
                                commandOwner, parentReference, null);
                            break;
                        }
                    }
                }

                for (int i = 0; i < parentReferences.length; ++i) {
                    final EReference parentReference = parentReferences[i];

                    if (parentReference.getEType().isInstance(newValue)) {
                        if (removeCommand != null) {
                            final CompoundCommand compoundCommand = new CompoundCommand(
                                CompoundCommand.LAST_COMMAND_ALL);
                            compoundCommand.append(removeCommand);
                            compoundCommand.append(SetCommand.create(
                                editingDomain, commandOwner, parentReference,
                                newValue));
                            compoundCommand.execute();

                        } else {
                            SetCommand.create(editingDomain, commandOwner,
                                parentReference, newValue).execute();
                        }
                        break;
                    }
                }
            } else {
                SetCommand.create(editingDomain, commandOwner, featureObject,
                    newValue).execute();
            }
        }
    }
}