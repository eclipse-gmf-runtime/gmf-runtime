/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.action.global;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.IConfigurationElement;

import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.CommonUIServicesActionDebugOptions;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.CommonUIServicesActionPlugin;
import org.eclipse.gmf.runtime.common.ui.services.action.internal.CommonUIServicesActionStatusCodes;

/**
 * A service that provides the <code>IGlobalActionHandler</code> associated
 * with a <code>IGlobalActionHandlerContext</code>. This service gets the first
 * provider of the highest priority that provides a <code>IGlobalActionHandler</code>
 * for the given <code>IGlobalActionHandlerContext</code>.
 * 
 * @author Vishy Ramaswamy
 */
public class GlobalActionHandlerService
	extends Service
	implements IGlobalActionHandlerProvider {
	/**
	 * A descriptor for <code>IGlobalActionHandlerProvider</code> defined
	 * by a configuration element.
	 * 
	 * @author Vishy Ramaswamy
	 */
	protected static class ProviderDescriptor
		extends Service.ProviderDescriptor {
		/**
		 * Attribute for maintaining the provider information
		 */
		private Hashtable partHandlerList = null;

		/**
		 * Constructs a <code>IGlobalActionHandlerProvider</code> descriptor for
		 * the specified configuration element.
		 * 
		 * @param element The configuration element describing the provider.
		 * @param partHandlerList A <code>Hashtable</code> with the provider information
		 */
		protected ProviderDescriptor(
			IConfigurationElement element,
			Hashtable partHandlerList) {
			super(element);

			assert null != partHandlerList : "partHandlerList cannot be null"; //$NON-NLS-1$

			this.partHandlerList = partHandlerList;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
		 */
		public boolean provides(IOperation operation) {
			if (!(operation instanceof GlobalActionHandlerOperation)) {
				return false;
			}

            /* Get all the context information */
			IGlobalActionHandlerContext context =
				((GlobalActionHandlerOperation) operation).getContext();
			String partId = context.getActivePart().getSite().getId();
			String actionId = context.getActionId();
			Class elementType = context.getElementType();
			boolean isCompatible = context.isCompatible();

			/* Check if the part is handled */
			Hashtable elementTypeTable =
				(Hashtable) getPartHandlerList().get(partId);
			if (elementTypeTable == null) {
				return false;
			}

			/* Get the action id list */
			List actionIdList = (List)
				elementTypeTable.get(
					isCompatible
						? getCompatibleType(
							elementTypeTable,
							elementType,
							actionId)
							.getName()
						: elementType.getName());
			if (actionIdList == null) {
				actionIdList = (List) elementTypeTable.get(NullElementType.class.getName());
				
				if (actionIdList == null) {
					return false;
				}
			}

			/* Check if the action is handled */
			if (actionIdList.contains(actionId)) {
				if (getPolicy() != null) {
					return getPolicy().provides(operation);
				}
				return true;
			}

			return false;
		}

		/**
		 * Returns the <code>Hashtable</code> containing the provider information
		 * 
		 * @return Return the <code>partHandlerList</code> instance variable
		 */
		private Hashtable getPartHandlerList() {
			return partHandlerList;
		}

		/**
		 * Returns the element type from the element type table that is
		 * assignable from the specified element type if the element type 
		 * also has the correct action with it.
		 * 
		 * @param elementTypeTable The table of element types
		 * @param elementType The specified element type
		 * @param actionId the action string trying to match
		 * @return Return the compatible type
		 */
		private Class getCompatibleType(
			Hashtable elementTypeTable,
			Class elementType,
			String actionId) {
			/* Enumerate through the element types and check if
			 * if the class or interface is either the same as, or
			 * is a superclass or superinterface of, the class or
			 * interface represented by the specified element type
			 */
			Class newClass = null;
			String className = null;
			Enumeration enumeration = elementTypeTable.keys();
			while (enumeration.hasMoreElements()) {
				className = (String) enumeration.nextElement();
				List actionIdList = (List)elementTypeTable.get(className);
				if (actionIdList == null || !actionIdList.contains(actionId)) {
					continue;
				}

				try {
					newClass =
						Class.forName(
							className,
							false,
							elementType.getClassLoader());
				} catch (ClassNotFoundException e) {
					// Trace only. Logging should not be done because this
					// is a normal condition for the class loader to fail.
					Trace.catching(CommonUIServicesActionPlugin.getDefault(), CommonUIServicesActionDebugOptions.EXCEPTIONS_CATCHING, getClass(), "getCompatibleType", e); //$NON-NLS-1$
				}

				if (newClass != null
					&& newClass.isAssignableFrom(elementType)) {
					return newClass;
				}
			}

			return elementType;
		}
	}

	/**
	 * The GlobalActionHandlerService constructor
	 */
	private final static GlobalActionHandlerService instance =
		new GlobalActionHandlerService();

	/**
	 * The GlobalActionHandlerService constructor
	 */
	protected GlobalActionHandlerService() {
		super(false);
	}

	/**
	 * Gets the instance of GlobalActionHandlerService
	 * @return Returns the <code>instance</code> variable
	 */
	public static GlobalActionHandlerService getInstance() {
		return instance;
	}

	/**
	 * Executes the <code>GlobalActionHandlerOperation</code> operation
	 * using the FIRST strategy
	 * 
	 * @param operation The <code>GlobalActionHandlerOperation</code> operation
	 * 
	 * @return Returns a <code>Object</code>
	 */
	private Object execute(GlobalActionHandlerOperation operation) {
		List results = execute(ExecutionStrategy.FIRST, operation);
		return results.isEmpty() ? null : results.get(0);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandlerProvider#getGlobalActionHandler(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionHandlerContext)
	 */
	public IGlobalActionHandler getGlobalActionHandler(IGlobalActionHandlerContext context) {
		assert null != context;

		return (IGlobalActionHandler) execute(
			new GetGlobalActionHandlerOperation(context));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.internal.service.Service#newProviderDescriptor(org.eclipse.core.runtime.IConfigurationElement)
	 */
	protected Service.ProviderDescriptor newProviderDescriptor(
		IConfigurationElement element) {
		return new ProviderDescriptor(
			element,
			getGlobalActionHandlerProviderInfo(element));
	}

	/**
	 * Captures all the <code>IGlobalActionHandlerProvider</code> information.
	 * 
	 * @param element The configuration element associated with the provider
	 * 
	 * @return Returns a <code>Hashtable</code>
	 */
	private Hashtable getGlobalActionHandlerProviderInfo(IConfigurationElement element) {
		Hashtable providerInfo = new Hashtable();
		try {
			/* get the view id children */
			IConfigurationElement[] viewChildren = element.getChildren("ViewId"); //$NON-NLS-1$
			for (int i = 0; i < viewChildren.length; i++) {
				/* get the view element */
				IConfigurationElement viewConfig = viewChildren[i];

				/* get the view id attribute */
				String id = viewConfig.getAttribute("id"); //$NON-NLS-1$
				if (id == null) {
					handleInvalidElement(viewConfig);
					continue;
				}
				/* add a placeholder in the table */
				providerInfo.put(id, new Hashtable());

				/* get all the element types */
				IConfigurationElement[] elementTypeChildren =
					viewConfig.getChildren();
				for (int j = 0; j < elementTypeChildren.length; j++) {
					IConfigurationElement elementTypeConfig =
						elementTypeChildren[j];

					/* get the class attribute */
					String elementTypeClass = elementTypeConfig.getAttribute("class"); //$NON-NLS-1$
					if (elementTypeClass == null) {
						elementTypeClass = NullElementType.class.getName();
					}

					/* add a placeholder for the element type */
					Hashtable table = (Hashtable) providerInfo.get(id);
					table.put(elementTypeClass, new Vector());

					/* get the action id children */
					Vector listOfActionId = new Vector();
					IConfigurationElement[] actionIdChildren =
						elementTypeConfig.getChildren();
					for (int k = 0; k < actionIdChildren.length; k++) {
						IConfigurationElement actionIdConfig =
							actionIdChildren[k];

						/* get the action id attributes */
						String actionId = actionIdConfig.getAttribute("actionId"); //$NON-NLS-1$
						if (actionId == null) {
							handleInvalidElement(actionIdConfig);
							continue;
						}

						/* add to the list */
						listOfActionId.addElement(actionId);
					}

					/* add the element type and its mapped vector */
					Vector list =
						(Vector) ((Hashtable) providerInfo.get(id)).get(
							elementTypeClass);
					list.addAll(listOfActionId);
				}
			}
		} catch (Exception e) {
			Trace.catching(CommonUIServicesActionPlugin.getDefault(), CommonUIServicesActionDebugOptions.EXCEPTIONS_CATCHING, getClass(), "getGlobalActionHandlerProviderInfo", e); //$NON-NLS-1$
			Log.error(
				CommonUIServicesActionPlugin.getDefault(),
				CommonUIServicesActionStatusCodes.SERVICE_FAILURE,
				MessageFormat.format(
					INVALID_ELEMENT_MESSAGE_PATTERN,
					new Object[] { element.getName()}),
				e);
		}
		return providerInfo;
	}
	
	/**
	 * Traces and logs a message to indicate that the XML element is invalid.
	 * @param element the invalid XML element
	 */
	private void handleInvalidElement(IConfigurationElement element) {

		String message =
			MessageFormat.format(
				INVALID_ELEMENT_MESSAGE_PATTERN,
				new Object[] {
					element.getDeclaringExtension().toString()
						+ StringStatics.COLON
						+ element.getName()});

		Trace.trace(CommonUIServicesActionPlugin.getDefault(), CommonUIServicesActionDebugOptions.SERVICES_CONFIG, message);
		Log.error(
			CommonUIServicesActionPlugin.getDefault(),
			CommonUIServicesActionStatusCodes.SERVICE_FAILURE,
			message);
	}
}
