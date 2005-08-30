/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.util;

/**
 * Interface for defining custom data used within clipboard/DND operations by
 * the transfer agent <code>CustomDataTransfer</code>
 * 
 * @author Vishy Ramaswamy
 */
public interface ICustomData {
    /**
     * Retrieves the format type
     * 
     * @return the format type
     */
    public String getFormatType();

    /**
     * Retrieves the data
     * 
     * @return the data as bytes
     */
    public byte[] getData();
}
