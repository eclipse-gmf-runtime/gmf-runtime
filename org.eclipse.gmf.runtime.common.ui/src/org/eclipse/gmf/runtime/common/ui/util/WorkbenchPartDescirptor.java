/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.util;

import org.eclipse.ui.IWorkbenchPage;

/**
 * A standard implementation of <code>IWorkbenchPartDescriptor</code> interface
 * @author melaasar
 */
public class WorkbenchPartDescirptor implements IWorkbenchPartDescriptor {

	/** the part's id */
	private final String partId;
	/** the part's class */
	private final Class partClass;
	/** the part's page */
	private final IWorkbenchPage partPage;

	/**
	 * Creates a new instance of the workbench part descriptor
	 * 
	 * @param partId The workbench part id
	 * @param partClass The workbench part class
	 * @param partPage The workbench part page
	 */
	public WorkbenchPartDescirptor(
		String partId,
		Class partClass,
		IWorkbenchPage partPage) {
		assert null != partId;
		assert null != partClass;
		assert null != partPage;
		this.partId = partId;
		this.partClass = partClass;
		this.partPage = partPage;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor#getPartId()
	 */
	public String getPartId() {
		return partId;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor#getPartClass()
	 */
	public Class getPartClass() {
		return partClass;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor#getPartPage()
	 */
	public IWorkbenchPage getPartPage() {
		return partPage;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof WorkbenchPartDescirptor) {
			WorkbenchPartDescirptor descriptor = (WorkbenchPartDescirptor) obj;
			return descriptor.getPartId().equals(getPartId())
				&& descriptor.getPartClass() == getPartClass()
				&& descriptor.getPartPage() == getPartPage();
		}
		return false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int idCode = getPartId().hashCode();
		int classCode = getPartClass().hashCode();
		int pageCode = getPartPage().hashCode();
		return (idCode + classCode + pageCode)
			+ (idCode | ~classCode | ~pageCode);
	}

}
