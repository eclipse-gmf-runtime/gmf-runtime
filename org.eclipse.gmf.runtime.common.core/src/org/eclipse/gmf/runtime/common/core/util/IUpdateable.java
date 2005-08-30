/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.core.util;

import org.eclipse.core.runtime.IAdaptable;

/**
 * Tests if an element is updatable
 * 
 * @author Michael Yee
 * @canBeSeenBy %partners
 */
public interface IUpdateable {
    /**
     * Tests if an element is updatable
     * @param adaptable element to test
     * @return boolean true if element is updatable, otherwise false
     */
    public boolean isUpdateable(IAdaptable adaptable);
}
