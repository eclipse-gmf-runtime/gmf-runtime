/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.clipboard.core.internal;

/**
 * types representing the reason a given object was serialized (copied)
 *  
 * @author Yasser Lulu 
 */
public class ObjectCopyType {

	/**
	 * 
	 */
	private ObjectCopyType() {
		//private
	}

	public static final String OBJ_COPY_TYPE_PARENT = "OCT_PARENT"; //$NON-NLS-1$

	public static final String OBJ_COPY_TYPE_ALWAYS = "OCT_ALWAYS"; //$NON-NLS-1$

	static final String OBJ_COPY_TYPE_ORIGINAL = "OCT_ORIGINAL"; //$NON-NLS-1$

}