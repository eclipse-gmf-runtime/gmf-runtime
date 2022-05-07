/******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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

import org.eclipse.emf.common.ui.celleditor.ExtendedComboBoxCellEditor;
import org.eclipse.emf.common.ui.celleditor.ExtendedDialogCellEditor;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.celleditor.FeatureEditorDialog;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.common.ui.services.properties.descriptors.ICompositeSourcePropertyDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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
            final List choiceOfValues = getChoiceOfValues();

            if (!choiceOfValues.isEmpty()) {
            	if (getItemDescriptor().isMany(getObject())) {
                    boolean valid = true;
                    for (Iterator i = choiceOfValues.iterator(); i.hasNext();) {
                        Object choice = i.next();
                        if (!eType.isInstance(choice)) {
                            valid = false;
                            break;
                        }
                    }
                    if (valid) {
                    	result = createDialogCellEditor(composite, feature, choiceOfValues);
					}
                }
                if (result == null)
                    result = createComboBoxCellEditor(composite);

            } else {
                if (eType instanceof EDataType) {

                    EDataType eDataType = (EDataType) eType;
                    if (eDataType.isSerializable()) {
                        if (getItemDescriptor().isMany(getObject())) {
                        	result = createDialogCellEditor(composite, feature, choiceOfValues);
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

        if (getFeature() == descriptor.getFeature()) {
        	if (getCategory() == null && descriptor.getCategory() == null) {
        		return true;
        	} else if (getCategory() != null) {
                return (getCategory().equals(descriptor.getCategory()));
        	}
        }

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
    public Object getObject() {
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
    
    /**
	 * Creates a dialog cell editor for editing multivalued features.
	 * 
	 * @param composite
	 *            the composite to contain the new cell editor
	 * @param feature
	 *            the feature being edited
	 * @param choiceOfValues
	 *            the possible values for that feature
	 * @return the new cell editor
	 */
    protected CellEditor createDialogCellEditor(Composite composite,
			final EStructuralFeature feature, final List choiceOfValues) {
    	
		return new ExtendedDialogCellEditor(composite, getEditLabelProvider()) {
			protected Object openDialogBox(Control cellEditorWindow) {
				FeatureEditorDialog dialog = new FeatureEditorDialog(
						cellEditorWindow.getShell(), getLabelProvider(),
						getObject(), feature.getEType(),
						(List) ((IItemPropertySource) itemPropertyDescriptor
								.getPropertyValue(object))
								.getEditableValue(object), getDisplayName(),
						choiceOfValues, false, false, choiceOfValues != null);
				dialog.open();
				return dialog.getResult();
			}
		};
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
        
        getItemDescriptor().setPropertyValue(getObject(), value);
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
        if (value instanceof IItemPropertySource) {
            return (IItemPropertySource) value;
        }
        
        TransactionalEditingDomain editingDomain = TransactionUtil
            .getEditingDomain(getObject());

        if (editingDomain instanceof AdapterFactoryEditingDomain) {
            return (IItemPropertySource) ((AdapterFactoryEditingDomain) editingDomain)
                .getAdapterFactory().adapt(value, IItemPropertySource.class);
        }
        return null;
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
}