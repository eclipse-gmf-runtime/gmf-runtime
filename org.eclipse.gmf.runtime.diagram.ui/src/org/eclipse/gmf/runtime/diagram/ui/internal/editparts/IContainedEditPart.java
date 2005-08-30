/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;


/**
 * Interface implemented to get around problems with
 * contribution item service.  All objects that are not
 * "diagram edit parts" will implement this interface.
 * 
 * @author schafe
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public interface IContainedEditPart {
	//marker to denote all edit parts that are not diagram edit parts
	//TODO: remove when contribution item service supports notValue for objectclass
}
