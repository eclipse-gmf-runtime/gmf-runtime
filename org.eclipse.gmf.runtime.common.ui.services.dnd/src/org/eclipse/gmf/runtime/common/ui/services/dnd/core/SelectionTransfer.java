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

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * This class can be used to transfer a <code>ISelection</code> between two
 * parts in a workbench in a drag and drop operation. This class does not
 * marshall the data.
 * 
 * @author Vishy Ramaswamy
 */
public final class SelectionTransfer
	extends ByteArrayTransfer {

	/**
	 * Create a UUID for the type name to make sure that each instance of an
	 * Eclipse application uses a different "type" of
	 * <code>SelectionTransfer</code>
	 */
	private static final String TYPE_NAME = "Element Selection Format" + (new Long(System.currentTimeMillis())).toString(); //$NON-NLS-1$

	/**
	 * Attribute for the type id
	 */
	private static final int TYPE_ID = registerType(TYPE_NAME);

	/**
	 * Create the singleton.
	 */
	private static SelectionTransfer instance = new SelectionTransfer();

	/**
	 * Return the singleton.
	 * 
	 * @return the singleton instance of the SelectionTransfer class
	 */
	public static SelectionTransfer getInstance() {
		return instance;
	}

	/**
	 * Create a <code>SelectionTransfer</code>.
	 */
	private SelectionTransfer() {
		super();
	}

	/**
	 * Attribute that holds on to the selection.
	 */
	private ISelection selection = null;

	/**
	 * Converts the <code>TYPE_NAME</code> into a <code>byte[]</code>
	 * 
	 * @see org.eclipse.swt.dnd.Transfer#javaToNative(java.lang.Object,
	 *      org.eclipse.swt.dnd.TransferData)
	 */
	public void javaToNative(Object object, TransferData transferData) {
		byte[] check = TYPE_NAME.getBytes();
		super.javaToNative(check, transferData);
	}

	/**
	 * Retrieves the <code>byte[]</code> associated with
	 * <code>TYPE_NAME</code>. Verifies it is the same as the
	 * <code>TYPE_NAME</code> and then returns the <code>selection</code>.
	 * 
	 * @return <code>Object</code>
	 * @see org.eclipse.swt.dnd.Transfer#nativeToJava(org.eclipse.swt.dnd.TransferData)
	 */
	public Object nativeToJava(TransferData transferData) {
		Object result = super.nativeToJava(transferData);
		if (isInvalidNativeType(result)) {
			return null;
		}

		return getSelection();
	}

	/**
	 * Check if the tranfer type is valid
	 * 
	 * @param result
	 *            with the transfer type to check
	 * @return true if the type is valid, false if it isn't
	 */
	private boolean isInvalidNativeType(Object result) {
		return !(result instanceof byte[])
			|| !TYPE_NAME.equals(new String((byte[]) result));
	}

	/**
	 * Returns the type ids supported by this agent
	 * 
	 * @return <code>int[]</code>
	 * @see org.eclipse.swt.dnd.Transfer#getTypeIds()
	 */
	protected int[] getTypeIds() {
		return new int[] {TYPE_ID};
	}

	/**
	 * Returns the type names supported by this agent
	 * 
	 * @return <code>String[]</code>
	 * @see org.eclipse.swt.dnd.Transfer#getTypeNames()
	 */
	protected String[] getTypeNames() {
		return new String[] {TYPE_NAME};
	}

	/**
	 * Returns the selection.
	 * 
	 * @return <code>selection</code>
	 */
	public ISelection getSelection() {
		return selection;
	}

	/**
	 * Sets the selection.
	 * 
	 * @param aSelection
	 *            The selection to set
	 */
	public void setSelection(ISelection aSelection) {
		this.selection = aSelection;
	}
}