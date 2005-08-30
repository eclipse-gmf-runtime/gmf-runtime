/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.type.core;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelper;

/**
 * Descriptor for a metamodel element type that has been defined in XML using
 * the <code>elementTypes</code> extension point.
 * 
 * @author ldamus
 */
public interface IMetamodelTypeDescriptor extends IElementTypeDescriptor {

	/**
	 * Gets the metaclass that this type represents.
	 * 
	 * @return the metaclass
	 */
	public abstract EClass getEClass();

	/**
	 * Gets the edit helper. May activate the plugin in which the edit helper is
	 * defined.
	 * 
	 * @return the edit helper.
	 */
	public abstract IEditHelper getEditHelper();
}