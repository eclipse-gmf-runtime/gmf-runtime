/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.internal.resources;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLLoad;
import org.eclipse.emf.ecore.xmi.XMLSave;

import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;

/**
 * Companion sub-unit class for the MSL resource implementation.
 *
 * @author Christian W. Damus (cdamus)
 */
public class MSLResourceUnit
	extends LogicalResourceUnit {

	private MSLResource logicalMslResource;
	
	/**
	 * Initializes me.
	 */
	public MSLResourceUnit(URI uri, LogicalResource logicalResource) {
		super(MEditingDomain.INSTANCE.convertURI(uri), logicalResource);
		
		logicalMslResource = (MSLResource) logicalResource;
	}

	protected boolean useIDAttributes() {
		return logicalMslResource.useIDAttributes();
	}

	protected boolean useUUIDs() {
		return logicalMslResource.useUUIDs();
	}

	protected XMLHelper createXMLHelper() {
		return new MSLHelper(this);
	}

	protected XMLLoad createXMLLoad() {
		return new MSLLoad(createXMLHelper());
	}

	protected XMLSave createXMLSave() {
		return new MSLSave(createXMLHelper());
	}

	public void setURI(URI uri) {

		MEditingDomain domain = MEditingDomain.getEditingDomain(this);

		if (domain == null)
			domain = MEditingDomain.INSTANCE;

		setRawURI(domain.convertURI(uri));
	}

	/**
	 * Set the URI of the resource without processing it.
	 */
	public void setRawURI(URI uri) {

		URI oldURI = getURI();

		if ((uri == oldURI) || ((uri != null) && (uri.equals(oldURI))))
			return;

		super.setURI(uri);
	}
}