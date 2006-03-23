/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.semantic.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.provider.LogicsemanticEditPlugin;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.ContainerElement;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.SemanticFactory;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.SemanticPackage;

/**
 * This is the item provider adapter for a {@link org.eclipse.gmf.examples.runtime.diagram.logic.semantic.ContainerElement} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ContainerElementItemProvider
	extends ElementItemProvider
	implements	
		IEditingDomainItemProvider,	
		IStructuredItemContentProvider,	
		ITreeItemContentProvider,	
		IItemLabelProvider,	
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ContainerElementItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addChildrenPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Children feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addChildrenPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ContainerElement_children_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ContainerElement_children_feature", "_UI_ContainerElement_type"),
				 SemanticPackage.Literals.CONTAINER_ELEMENT__CHILDREN,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Collection getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(SemanticPackage.Literals.CONTAINER_ELEMENT__CHILDREN);
		}
		return childrenFeatures;
	}

	/**
	 * This returns ContainerElement.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ContainerElement"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getText(Object object) {
		return getString("_UI_ContainerElement_type");
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(ContainerElement.class)) {
			case SemanticPackage.CONTAINER_ELEMENT__CHILDREN:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds to the collection of {@link org.eclipse.emf.edit.command.CommandParameter}s
	 * describing all of the children that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void collectNewChildDescriptors(Collection newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(SemanticPackage.Literals.CONTAINER_ELEMENT__CHILDREN,
				 SemanticFactory.eINSTANCE.createLED()));

		newChildDescriptors.add
			(createChildParameter
				(SemanticPackage.Literals.CONTAINER_ELEMENT__CHILDREN,
				 SemanticFactory.eINSTANCE.createWire()));

		newChildDescriptors.add
			(createChildParameter
				(SemanticPackage.Literals.CONTAINER_ELEMENT__CHILDREN,
				 SemanticFactory.eINSTANCE.createCircuit()));

		newChildDescriptors.add
			(createChildParameter
				(SemanticPackage.Literals.CONTAINER_ELEMENT__CHILDREN,
				 SemanticFactory.eINSTANCE.createFlowContainer()));

		newChildDescriptors.add
			(createChildParameter
				(SemanticPackage.Literals.CONTAINER_ELEMENT__CHILDREN,
				 SemanticFactory.eINSTANCE.createAndGate()));

		newChildDescriptors.add
			(createChildParameter
				(SemanticPackage.Literals.CONTAINER_ELEMENT__CHILDREN,
				 SemanticFactory.eINSTANCE.createOrGate()));

		newChildDescriptors.add
			(createChildParameter
				(SemanticPackage.Literals.CONTAINER_ELEMENT__CHILDREN,
				 SemanticFactory.eINSTANCE.createXORGate()));

		newChildDescriptors.add
			(createChildParameter
				(SemanticPackage.Literals.CONTAINER_ELEMENT__CHILDREN,
				 SemanticFactory.eINSTANCE.createModel()));

		newChildDescriptors.add
			(createChildParameter
				(SemanticPackage.Literals.CONTAINER_ELEMENT__CHILDREN,
				 SemanticFactory.eINSTANCE.createOutputTerminal()));

		newChildDescriptors.add
			(createChildParameter
				(SemanticPackage.Literals.CONTAINER_ELEMENT__CHILDREN,
				 SemanticFactory.eINSTANCE.createInputTerminal()));

		newChildDescriptors.add
			(createChildParameter
				(SemanticPackage.Literals.CONTAINER_ELEMENT__CHILDREN,
				 SemanticFactory.eINSTANCE.createInputOutputTerminal()));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceLocator getResourceLocator() {
		return LogicsemanticEditPlugin.INSTANCE;
	}

}
