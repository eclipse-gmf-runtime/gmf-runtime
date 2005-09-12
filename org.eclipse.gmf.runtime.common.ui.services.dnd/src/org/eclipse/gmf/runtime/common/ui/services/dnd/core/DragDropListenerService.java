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

package org.eclipse.gmf.runtime.common.ui.services.dnd.core;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.IConfigurationElement;

import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drag.IDragSourceListener;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drop.IDropTargetListener;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.CommonUIServicesDNDDebugOptions;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.CommonUIServicesDNDPlugin;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.CommonUIServicesDNDStatusCodes;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.core.GetDragListenerOperation;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.core.GetDropListenerOperation;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.core.IListenerContext;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.core.ListenerOperation;

/**
 * A service that provides the <code>IDragSourceListener</code> and
 * <code>IDropTargetListener</code>. This service gets the all the providers
 * that provide listeners for the specified context.
 * 
 * @author Vishy Ramaswamy
 */
public class DragDropListenerService
	extends Service
	implements IDragDropListenerProvider {

	/**
	 * A descriptor for <code>IDragDropListenerProvider</code> defined by a
	 * configuration element.
	 * 
	 * @author Vishy Ramaswamy
	 */
	protected static class ProviderDescriptor
		extends Service.ProviderDescriptor {

		/**
		 * Attribute for maintaining the provider information
		 */
		private Hashtable partInfo = null;

		/**
		 * Constructs a <code>IDragDropListenerProvider</code> descriptor for
		 * the specified configuration element.
		 * 
		 * @param element
		 *            The configuration element describing the provider.
		 * @param aPartInfo
		 *            A <code>Hashtable</code> with the provider information
		 */
		protected ProviderDescriptor(IConfigurationElement element,
				Hashtable aPartInfo) {
			super(element);

			assert null != aPartInfo : "aPartInfo cannot be null"; //$NON-NLS-1$
			
			this.partInfo = aPartInfo;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
		 */
		public boolean provides(IOperation operation) {
			/* Check the operations */
			if (!(operation instanceof GetDragListenerOperation)
				&& !(operation instanceof GetDropListenerOperation)) {
				return false;
			}

			/* Get all the context information */
			Class elementType = null;
			String partId = null;
			String operationType = null;
			String transferId = null;
			boolean isCompatible = false;

			if (operation instanceof GetDragListenerOperation) {
				elementType = ((IDragListenerContext) ((GetDragListenerOperation) operation)
					.getContext()).getSelectedElementType();
				partId = ((IDragListenerContext) ((GetDragListenerOperation) operation)
					.getContext()).getActivePart().getSite().getId();
				operationType = ((GetDragListenerOperation) operation)
					.getContext().getOperationType();
				transferId = ((IDragListenerContext) ((GetDragListenerOperation) operation)
					.getContext()).getTransferId();
				isCompatible = ((IDragListenerContext) ((GetDragListenerOperation) operation)
					.getContext()).isCompatible();
			}

			if (operation instanceof GetDropListenerOperation) {
				elementType = ((IDropListenerContext) ((GetDropListenerOperation) operation)
					.getContext()).getTargetElementType();
				partId = ((IDropListenerContext) ((GetDropListenerOperation) operation)
					.getContext()).getActivePart().getSite().getId();
				operationType = ((GetDropListenerOperation) operation)
					.getContext().getOperationType();
				transferId = ((IDropListenerContext) ((GetDropListenerOperation) operation)
					.getContext()).getTransferId();
				isCompatible = ((IDropListenerContext) ((GetDropListenerOperation) operation)
					.getContext()).isCompatible();
			}

			if (Trace.shouldTrace(CommonUIServicesDNDPlugin.getDefault(),
				CommonUIServicesDNDDebugOptions.DND)) {
				Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
					CommonUIServicesDNDDebugOptions.DND,
					"DNDS:provides::Element Type is " + elementType.getName()); //$NON-NLS-1$
				Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
					CommonUIServicesDNDDebugOptions.DND,
					"DNDS:provides::Part Id is " + partId); //$NON-NLS-1$
				Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
					CommonUIServicesDNDDebugOptions.DND,
					"DNDS:provides::Operation Type is " + operationType); //$NON-NLS-1$
				Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
					CommonUIServicesDNDDebugOptions.DND,
					"DNDS:provides::Transfer id is " + transferId); //$NON-NLS-1$				
				Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
					CommonUIServicesDNDDebugOptions.DND,
					"DNDS:provides::Compatible is " + isCompatible); //$NON-NLS-1$				
			}

			/* Check if the part is handled */
			Hashtable elementTypeTable = (Hashtable) getPartInfo().get(partId);
			if (elementTypeTable == null) {
				return false;
			}

			/* Check if the operation type is handled */
			Hashtable operationTypeTable = (Hashtable) elementTypeTable
				.get(isCompatible ? getCompatibleType(elementTypeTable,
					elementType).getName()
					: elementType.getName());
			if (operationTypeTable == null) {
				return false;
			}

			/* Get the transfer id list */
			List transferIdList = (List) operationTypeTable.get(operationType);
			if (transferIdList == null) {
				return false;
			}

			/* Check if the requested transfer is available */
			return (transferId.equals(IListenerContext.ALL_TRANSFERS)) ? true
				: transferIdList.contains(transferId);
		}

		/**
		 * Returns the <code>Hashtable</code> containing the provider
		 * information
		 * 
		 * @return Return the <code>partInfo</code> instance variable
		 */
		private Hashtable getPartInfo() {
			return partInfo;
		}

		/**
		 * Returns the element type from the element type table that is
		 * assignable from the specified element type
		 * 
		 * @param elementTypeTable
		 *            The table of element types
		 * @param elementType
		 *            The specified element type
		 * @return Return the compatible type
		 */
		private Class getCompatibleType(Hashtable elementTypeTable,
				Class elementType) {
			/*
			 * Enumerate through the element types and check if if the class or
			 * interface is either the same as, or is a superclass or
			 * superinterface of, the class or interface represented by the
			 * specified element type
			 */
			Class newClass = null;
			String className = null;
			Enumeration enumeration = elementTypeTable.keys();
			while (enumeration.hasMoreElements()) {
				className = (String) enumeration.nextElement();

				try {
					newClass = Class.forName(className, false, elementType
						.getClassLoader());
				} catch (ClassNotFoundException e) {
					// This is an expected exception. Do not log - only trace.
					Trace.catching(CommonUIServicesDNDPlugin.getDefault(),
						CommonUIServicesDNDDebugOptions.EXCEPTIONS_CATCHING,
						getClass(), "getCompatibleType", e); //$NON-NLS-1$
				}

				if (newClass != null && newClass.isAssignableFrom(elementType)) {
					return newClass;
				}
			}

			return elementType;
		}
	}

	/**
	 * The DragDropListenerService constructor
	 */
	private final static DragDropListenerService instance = new DragDropListenerService();

	/**
	 * The DragDropListenerService constructor
	 */
	protected DragDropListenerService() {
		super(false);
	}

	/**
	 * Gets the instance of DragDropListenerService
	 * 
	 * @return Returns the <code>instance</code> variable
	 */
	public static DragDropListenerService getInstance() {
		return instance;
	}

	/**
	 * Executes the <code>ListenerOperation</code> operation using the FORWARD
	 * strategy
	 * 
	 * @param operation
	 *            The operation
	 * 
	 * @return Returns a <code>Object</code>
	 */
	private Object execute(ListenerOperation operation) {
		List results = execute(ExecutionStrategy.FORWARD, operation);

		if (operation instanceof GetDragListenerOperation) {
			return results.isEmpty() ? null
				: combineArraysInList(results, new IDragSourceListener[0]);
		} else if (operation instanceof GetDropListenerOperation) {
			return results.isEmpty() ? null
				: combineArraysInList(results, new IDropTargetListener[0]);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.internal.service.Service#newProviderDescriptor(org.eclipse.core.runtime.IConfigurationElement)
	 */
	protected Service.ProviderDescriptor newProviderDescriptor(
			IConfigurationElement element) {
		return new ProviderDescriptor(element,
			getDragDropListenerProviderInfo(element));
	}

	/**
	 * Captures all the <code>IDragDropListenerProvider</code> information.
	 * 
	 * @param element
	 *            The configuration element associated with the provider
	 * 
	 * @return Returns a <code>Hashtable</code>
	 */
	private Hashtable getDragDropListenerProviderInfo(
			IConfigurationElement element) {
		/* Get the view id children */
		Hashtable providerInfo = new Hashtable();
		try {
			IConfigurationElement[] viewChildren = element
				.getChildren("ViewId"); //$NON-NLS-1$
			for (int i = 0; i < viewChildren.length; i++) {
				/* Get the view element */
				IConfigurationElement viewConfig = viewChildren[i];

				/* Get the view id attribute */
				String id = viewConfig.getAttribute("id"); //$NON-NLS-1$
				if (id == null) {
					handleInvalidElement(viewConfig);
					continue;
				}

				/* Add a placeholder in the table */
				providerInfo.put(id, new Hashtable());

				/* Get all the element types */
				IConfigurationElement[] elementTypeChildren = viewConfig
					.getChildren();
				for (int j = 0; j < elementTypeChildren.length; j++) {
					IConfigurationElement elementTypeConfig = elementTypeChildren[j];

					/* Get the class attribute */
					String elementTypeClass = elementTypeConfig
						.getAttribute("class"); //$NON-NLS-1$
					if (elementTypeClass == null) {
						handleInvalidElement(elementTypeConfig);
						continue;
					}
					/* Add a placeholder for the element type */
					Hashtable table = (Hashtable) providerInfo.get(id);
					table.put(elementTypeClass, new Hashtable());

					/* Get the operation type children */
					IConfigurationElement[] operationTypeChildren = elementTypeConfig
						.getChildren();
					for (int k = 0; k < operationTypeChildren.length; k++) {
						IConfigurationElement operationTypeConfig = operationTypeChildren[k];

						/* Get the operation type attributes */
						String operationType = operationTypeConfig
							.getAttribute("operation"); //$NON-NLS-1$
						if (operationType == null) {
							handleInvalidElement(operationTypeConfig);
							continue;
						}
						/* Get the transfer type children */
						Vector listOftransferId = new Vector();
						IConfigurationElement[] transferTypeChildren = operationTypeConfig
							.getChildren();
						for (int l = 0; l < transferTypeChildren.length; l++) {
							IConfigurationElement transferIdConfig = transferTypeChildren[l];

							/* Get the transfer id attributes */
							String transferId = transferIdConfig
								.getAttribute("transferId"); //$NON-NLS-1$
							if (transferId == null) {
								handleInvalidElement(transferIdConfig);
								continue;
							}
							/* Add to the list */
							listOftransferId.addElement(transferId);

						}

						/* Add a placeholder for the operation type */
						table = (Hashtable) ((Hashtable) providerInfo.get(id))
							.get(elementTypeClass);
						table.put(operationType, listOftransferId);
					}
				}
			}
		} catch (Exception e) {
			Trace.catching(CommonUIServicesDNDPlugin.getDefault(),
				CommonUIServicesDNDDebugOptions.EXCEPTIONS_CATCHING,
				getClass(), "getDragDropListenerProviderInfo", e); //$NON-NLS-1$
			Log.error(CommonUIServicesDNDPlugin.getDefault(),
				CommonUIServicesDNDStatusCodes.SERVICE_FAILURE, MessageFormat
					.format(INVALID_ELEMENT_MESSAGE_PATTERN,
						new Object[] {element.getName()}), e);
		}

		return providerInfo;
	}

	/**
	 * Traces and logs a message to indicate that the XML element is invalid.
	 * 
	 * @param element
	 *            the invalid XML element
	 */
	private void handleInvalidElement(IConfigurationElement element) {

		String message = MessageFormat.format(INVALID_ELEMENT_MESSAGE_PATTERN,
			new Object[] {element.getDeclaringExtension().toString()
				+ StringStatics.COLON + element.getName()});

		Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
			CommonUIServicesDNDDebugOptions.SERVICES_CONFIG, message);
		Log.error(CommonUIServicesDNDPlugin.getDefault(),
			CommonUIServicesDNDStatusCodes.SERVICE_FAILURE, message);
	}

	/**
	 * Utility to get all the registered transfer ids against a part id for an
	 * operation type
	 * 
	 * @param partId
	 *            the part id
	 * @param operationType
	 *            the type of operation (drag or drop)
	 * @return array of transfer ids
	 */
	public String[] getAllTransferIds(String partId, String operationType) {
		assert null != partId : "partId cannot be null"; //$NON-NLS-1$
		assert null != operationType : "operationType cannot be null"; //$NON-NLS-1$

		/* Get all the providers */
		List list = getAllProviders();

		/* Iterate through all the providers */
		ArrayList transferIds = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			/* Get the next descriptor */
			ProviderDescriptor descriptor = (ProviderDescriptor) it.next();

			/* Check if the part is handled */
			Hashtable elementTypeTable = (Hashtable) descriptor.getPartInfo()
				.get(partId);
			if (elementTypeTable == null) {
				continue;
			}

			/* Go through all the element types */
			Enumeration e = elementTypeTable.elements();
			while (e.hasMoreElements()) {
				/* Get the operation type table */
				Hashtable operationTypeTable = (Hashtable) e.nextElement();

				/* Check if the operation is handled */
				if (operationTypeTable.containsKey(operationType)) {
					/* Get the transfer id list */
					List transferIdList = (List) operationTypeTable
						.get(operationType);

					/* Get the transfer id */
					Iterator transfer = transferIdList.iterator();
					while (transfer.hasNext()) {
						/*
						 * Get the id and add it to the list if it does not
						 * exist
						 */
						String transferId = (String) transfer.next();
						if (!transferIds.contains(transferId)) {
							transferIds.add(transferId);
						}
					}
				}
			}
		}

		/* Return the list of ids */
		return !transferIds.isEmpty() ? (String[]) transferIds
			.toArray(new String[transferIds.size()])
			: null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.IDragDropListenerProvider#getDragSourceListeners(org.eclipse.gmf.runtime.common.ui.services.dnd.core.IDragListenerContext)
	 */
	public IDragSourceListener[] getDragSourceListeners(
			IDragListenerContext context) {
		assert null != context : "context cannot be null"; //$NON-NLS-1$

		return (IDragSourceListener[]) execute(new GetDragListenerOperation(
			context));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.IDragDropListenerProvider#getDropTargetListeners(org.eclipse.gmf.runtime.common.ui.services.dnd.core.IDropListenerContext)
	 */
	public IDropTargetListener[] getDropTargetListeners(
			IDropListenerContext context) {
		assert null != context : "context cannot be null"; //$NON-NLS-1$

		return (IDropTargetListener[]) execute(new GetDropListenerOperation(
			context));
	}

	/**
	 * Combines the arrays in the passed list into one array.
	 * 
	 * @param list
	 *            The List of arrays to combine together
	 * @param resultingArrayType
	 *            the Object[] used to define the type of the combined array
	 * 
	 * @return Object[] the combined array of the proper type
	 */
	private final Object[] combineArraysInList(List list,
			Object[] resultingArrayType) {
		List listEntry = null;
		Iterator it = list.iterator();
		ArrayList results = new ArrayList();
		while (it.hasNext()) {
			Object[] array = (Object[]) it.next();
			if (array != null && array.length > 0) {
				listEntry = Arrays.asList(array);
				results.addAll(listEntry);
			}
		}

		return results.toArray(resultingArrayType);
	}
}