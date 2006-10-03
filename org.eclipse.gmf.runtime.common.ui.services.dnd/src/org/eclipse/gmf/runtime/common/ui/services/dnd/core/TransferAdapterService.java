/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.dnd.core;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drag.ITransferDragSourceListener;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drop.ITransferDropTargetListener;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.CommonUIServicesDNDDebugOptions;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.CommonUIServicesDNDPlugin;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.core.GetTransferAdapterOperation;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.core.ITransferAdapterProvider;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.core.ListenerOperation;

/**
 * A service that provides transfer adapters for a given transfer id.
 * 
 * @author Vishy Ramaswamy
 */
public class TransferAdapterService
	extends Service
	implements ITransferAdapterProvider {

	/**
	 * A descriptor for <code>ITransferAdapterProvider</code> defined by a
	 * configuration element.
	 * 
	 * @author Vishy Ramaswamy
	 */
	protected static class ProviderDescriptor
		extends Service.ProviderDescriptor {

		/**
		 * Attribute for maintaining the provider information
		 */
		private Hashtable adapterInfo = null;

		/**
		 * Constructs a <code>ITransferAdapterProvider</code> descriptor for
		 * the specified configuration element.
		 * 
		 * @param element
		 *            The configuration element describing the provider.
		 * @param anAdapterInfo
		 *            A <code>Hashtable</code> with the provider information
		 */
		protected ProviderDescriptor(IConfigurationElement element,
				Hashtable anAdapterInfo) {
			super(element);

			assert null != anAdapterInfo : "anAdapterInfo cannot be null"; //$NON-NLS-1$
			this.adapterInfo = anAdapterInfo;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
		 */
		public boolean provides(IOperation operation) {
			/* Check the operation */
			if (!(operation instanceof GetTransferAdapterOperation)) {
				return false;
			}

			/* Get all the context information */
			String operationType = ((GetTransferAdapterOperation) operation)
				.getContext().getOperationType();
			String transferId = ((GetTransferAdapterOperation) operation)
				.getContext().getTransferId();

			if (Trace.shouldTrace(CommonUIServicesDNDPlugin.getDefault(),
				CommonUIServicesDNDDebugOptions.DND)) {
				Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
					CommonUIServicesDNDDebugOptions.DND,
					"TAS:provides::Operation Type is " + operationType); //$NON-NLS-1$
				Trace.trace(CommonUIServicesDNDPlugin.getDefault(),
					CommonUIServicesDNDDebugOptions.DND,
					"TAS:provides::Transfer id is " + transferId); //$NON-NLS-1$				
			}

			/* Check if an id exists */
			List transferIdList = (List) getAdapterInfo().get(
				new Integer(operationType.hashCode()));
			if (transferIdList == null) {
				return false;
			}

			return transferIdList.contains(new Integer(transferId.hashCode()));
		}

		/**
		 * Returns the <code>Hashtable</code> containing the provider
		 * information
		 * 
		 * @return Return the <code>adapterInfo</code> instance variable
		 */
		private Hashtable getAdapterInfo() {
			return adapterInfo;
		}
	}

	/**
	 * Attribute for the singleton
	 */
	private final static TransferAdapterService instance = new TransferAdapterService();

	static {
		instance.configureProviders(CommonUIServicesDNDPlugin.getPluginId(), "transferAdapterProviders"); //$NON-NLS-1$
	}

	/**
	 * Return the singleton.
	 * 
	 * @return singleton instance of the TransferAdapterService class
	 */
	public static TransferAdapterService getInstance() {
		return instance;
	}

	/**
	 * Constructor for TransferAdapterService.
	 */
	protected TransferAdapterService() {
		super(false);
	}

	/**
	 * Executes the <code>ListenerOperation</code> operation using the FIRST
	 * strategy
	 * 
	 * @param operation
	 *            The operation
	 * @return Returns a <code>Object</code>
	 */
	private Object execute(ListenerOperation operation) {
		List results = execute(ExecutionStrategy.FIRST, operation);
		return results.isEmpty() ? null
			: results.get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.internal.service.Service#newProviderDescriptor(org.eclipse.core.runtime.IConfigurationElement)
	 */
	protected Service.ProviderDescriptor newProviderDescriptor(
			IConfigurationElement element) {
		return new ProviderDescriptor(element,
			getTransferAdapterProviderInfo(element));
	}

	/**
	 * Captures all the <code>ITransferAdapterProvider</code> information.
	 * 
	 * @param element
	 *            The configuration element associated with the provider
	 * @return Returns a <code>Hashtable</code>
	 */
	private Hashtable getTransferAdapterProviderInfo(
			IConfigurationElement element) {
		/* Get the transfer id children */
		Hashtable providerInfo = new Hashtable();
		IConfigurationElement[] adapterTypeChildren = element
			.getChildren("AdapterType"); //$NON-NLS-1$
		for (int i = 0; i < adapterTypeChildren.length; i++) {
			IConfigurationElement adapterTypeConfig = adapterTypeChildren[i];

			/* Get the operation type attributes */
			String operationType = adapterTypeConfig.getAttribute("operation"); //$NON-NLS-1$

			Vector listOftransferIds = new Vector();
			IConfigurationElement[] transferChildren = adapterTypeConfig
				.getChildren();
			for (int j = 0; j < transferChildren.length; j++) {
				/* Get the transfer id element */
				IConfigurationElement transferConfig = transferChildren[j];

				/* Get the transfer id attribute */
				String id = transferConfig.getAttribute("id"); //$NON-NLS-1$

				/* Get the transfer ids */

				/* Add to the list */
				listOftransferIds.addElement(new Integer(id.hashCode()));
			}

			/* Add a placeholder in the table */
			providerInfo.put(new Integer(operationType.hashCode()),
				listOftransferIds);
		}

		return providerInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAdapterProvider#getTransferDragSourceAdapter(java.lang.String)
	 */
	public ITransferDragSourceListener getTransferDragSourceAdapter(
			String transferId) {
		assert null != transferId : "transferId cannot be null"; //$NON-NLS-1$

		/* Create a placeholder */
		final String id = transferId;

		/* Return the adapter */
		return (ITransferDragSourceListener) execute(new GetTransferAdapterOperation(
			new IListenerContext() {

				public String getOperationType() {
					return IListenerContext.DRAG;
				}

				public String getTransferId() {
					return id;
				}
			}));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAdapterProvider#getTransferDropTargetAdapter(java.lang.String)
	 */
	public ITransferDropTargetListener getTransferDropTargetAdapter(
			String transferId) {
		assert null != transferId : "transferId cannot be null"; //$NON-NLS-1$

		/* Create a placeholder */
		final String id = transferId;

		/* Return the adapter */
		return (ITransferDropTargetListener) execute(new GetTransferAdapterOperation(
			new IListenerContext() {

				public String getOperationType() {
					return IListenerContext.DROP;
				}

				public String getTransferId() {
					return id;
				}
			}));
	}
}