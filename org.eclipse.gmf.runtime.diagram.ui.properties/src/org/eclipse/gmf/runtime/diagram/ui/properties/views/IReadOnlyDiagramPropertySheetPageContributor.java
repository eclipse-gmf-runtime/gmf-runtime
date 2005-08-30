/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.properties.views;

import org.eclipse.wst.common.ui.properties.internal.provisional.ITabbedPropertySheetPageContributor;

/**
 * A interface to indicate that the property sheet pages should be read only
 * independant of the selected object and its related file status.
 * 
 * For example, this interface is implemented by topic and browse diagrams. Even
 * though the selected object can be modified, we want the properties to be read
 * only for these diagrams.
 * 
 * @author Anthony Hunter <a
 *         href="mailto:anthonyh@ca.ibm.com">anthonyh@ca.ibm.com </a>
 */
public interface IReadOnlyDiagramPropertySheetPageContributor
	extends ITabbedPropertySheetPageContributor {
	/*
	 * there are no methods
	 */
}