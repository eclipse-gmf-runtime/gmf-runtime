/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.examples.runtime.emf.clipboard.transfer;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;


/**
 * Custom data transfer implementation for the clipboard example.
 */
public class EmfTransfer
	extends ByteArrayTransfer {

	private static final String[] TYPE_NAMES = new String[] {"emfClipboardExample"}; //$NON-NLS-1$
	private static final int[] TYPE_IDS = new int[] {Transfer.registerType(TYPE_NAMES[0])};
	
	private static final EmfTransfer INSTANCE = new EmfTransfer();
	
	/**
	 * Initializes me.
	 */
	private EmfTransfer() {
		super();
	}
	
	/**
	 * Obtains the singleton instance.
	 * 
	 * @return the instance
	 */
	public static EmfTransfer getInstance() {
		return INSTANCE;
	}

	protected int[] getTypeIds() {
		return TYPE_IDS;
	}

	protected String[] getTypeNames() {
		return TYPE_NAMES;
	}

	/**
	 * Implements the serialization to the clipboard.
	 */
	protected void javaToNative(Object object, TransferData transferData) {
		if (object == null || !(object instanceof EmfTransferType)) {
			return;
		}
		
		if (isSupportedType(transferData)) {
			EmfTransferType data = (EmfTransferType) object;
			
			super.javaToNative(data.toBytes(), transferData);
		}
	}
	
	/**
	 * Implements deserialization from the clipboard.
	 */
	protected Object nativeToJava(TransferData transferData) {
		EmfTransferType result = null;
		
		if (isSupportedType(transferData)) {
			byte[] bytes = (byte[]) super.nativeToJava(transferData);
			
			if (bytes != null) {
				result = EmfTransferType.fromBytes(bytes);
			}
		}
		
		return result;
	}
}
