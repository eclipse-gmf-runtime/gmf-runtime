/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.examples.runtime.emf.clipboard.transfer;

import java.io.UnsupportedEncodingException;


/**
 * Clipboard transfer data type for the clipboard example.
 */
public final class EmfTransferType {
	private String string;
	
	/**
	 * Initializes me.
	 * 
	 * @param string the string form of a user selection of
	 *     {@link org.eclipse.emf.ecore.EObject}s.  Must not be <code>null</code>
	 */
	public EmfTransferType(String string) {
		assert string != null;
		
		this.string = string;
	}
	
	/**
	 * Gets the string representation of the
	 * {@link org.eclipse.emf.ecore.EObject}s retrieved from the clipboard.
	 * 
	 * @return the string (will not be <code>null</code>)
	 */
	public String getString() {
		return string;
	}

	/**
	 * Converts my data to bytes for the clipboard.
	 * 
	 * @return the bytes
	 */
	byte[] toBytes() {
		try {
			return string.getBytes("UTF-8"); //$NON-NLS-1$
		} catch (UnsupportedEncodingException e) {
			// UTF-8 is always supported by the Java platform
			throw new Error("UTF-8 encoding not supported by String class", e); //$NON-NLS-1$
		}
	}
	
	/**
	 * Converts bytes from the clipboard to my data.
	 * 
	 * @param bytes the bytes
	 * @return the transfer type
	 */
	static EmfTransferType fromBytes(byte[] bytes) {
		try {
			return new EmfTransferType(new String(bytes, "UTF-8")); //$NON-NLS-1$
		} catch (UnsupportedEncodingException e) {
			// UTF-8 is always supported by the Java platform
			throw new Error("UTF-8 encoding not supported by String class", e); //$NON-NLS-1$
		}
	}
}
