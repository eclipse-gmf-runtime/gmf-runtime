/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.clipboard.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.gmf.runtime.emf.clipboard.core.internal.SerializationEMFResource;

/**
 * @author Yasser Lulu
 */
class LoadingEMFResource
	extends SerializationEMFResource {

	private final ResourceSet rset;
	
	private Map idToEObjectMapCopy;

	private Map eObjectToIDMapCopy;

	LoadingEMFResource(ResourceSet rset, String encoding, Map defaultLoadOptions,
		IClipboardSupport clipboardOperationHelper) {
		super(encoding, clipboardOperationHelper);
		this.rset = rset;
		this.defaultLoadOptions = defaultLoadOptions;
	}

	/**
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl#doSave(java.io.OutputStream,
	 *      java.util.Map)
	 */
	public void doSave(OutputStream outputStream, Map options)
		throws IOException {
		throwUnsupportedOperationException("doSave", //$NON-NLS-1$
			new UnsupportedOperationException(
				"Can't call save on deserializing resource")); //$NON-NLS-1$
	}

	public void doLoad(InputStream inputStream, Map options) throws IOException {
		NonResolvingResourceSet nonResolvingResourceSet =
			new NonResolvingResourceSet(rset);
		nonResolvingResourceSet.getResources().add(this);
		IOException exception = null;
		try {
			super.doLoad(inputStream, options);
		} catch (IOException ex) {
			exception = ex;
		}
		nonResolvingResourceSet.getResources().remove(this);
		getIDToEObjectMapCopy().putAll(getIDToEObjectMap());
		getEObjectToIDMapCopy().putAll(getEObjectToIDMap());
		if (exception != null) {
			throw exception;
		}
	}

	Map getIDToEObjectMapCopy() {
		if (idToEObjectMapCopy == null) {
			idToEObjectMapCopy = new HashMap();
		}
		return idToEObjectMapCopy;
	}

	Map getEObjectToIDMapCopy() {
		if (eObjectToIDMapCopy == null) {
			eObjectToIDMapCopy = new HashMap();
		}
		return eObjectToIDMapCopy;
	}
}