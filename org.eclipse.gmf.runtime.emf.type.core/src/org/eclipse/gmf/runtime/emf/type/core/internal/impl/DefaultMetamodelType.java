/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core.internal.impl;

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.gmf.runtime.emf.type.core.MetamodelType;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelper;
import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.MetamodelTypeDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.EMFTypeCoreMessages;

/**
 * A default metamodel type for elements for which no type is registered.  This
 * ensures at least basic editing support for any {@link EObject}
 * 
 * @author Christian W. Damus (cdamus)
 */
public class DefaultMetamodelType
	extends MetamodelType {

	public static final String ID = "org.eclipse.gmf.runtime.emf.type.core.default"; //$NON-NLS-1$
	
	private static final DefaultMetamodelType INSTANCE = new DefaultMetamodelType();
	private static final MetamodelTypeDescriptor DESCRIPTOR_INSTANCE =
		new MetamodelTypeDescriptor(INSTANCE);
	
	/**
	 * Initializes me.
	 */
	private DefaultMetamodelType() {
		super(
				ID,
				null,
				EMFTypeCoreMessages.defaultEditHelper_name,
				EcorePackage.Literals.EOBJECT,
				new DefaultEditHelper());
	}
	
	/**
	 * Obtains the singleton default metamodel type instance.
	 * 
	 * @return the singleton instance
	 */
	public static DefaultMetamodelType getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Obtains the singleton default metamodel type descriptor instance.
	 * 
	 * @return the singleton descriptor instance
	 */
	public static MetamodelTypeDescriptor getDescriptorInstance() {
		return DESCRIPTOR_INSTANCE;
	}

	/**
	 * Edit helper for the default element type.
	 *
	 * @author Christian W. Damus (cdamus)
	 */
	private static class DefaultEditHelper extends AbstractEditHelper {
		DefaultEditHelper() {
			super();
		}
	}
}
