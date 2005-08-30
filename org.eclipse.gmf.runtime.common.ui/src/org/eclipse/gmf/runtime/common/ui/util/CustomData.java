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
 * The class used to create custom data for drag and drop/clipboard operations. This
 * class implements <code>ICustomData</code> interface used within the
 * transfer agent <code>CustomDataTransfer</code>
 * 
 * @author Vishy Ramaswamy
 */
public final class CustomData implements ICustomData {
    /* Attribute for the format type */
    final private String formatType;

    /* Attribute for the data */
    final private byte[] data;

    /**
     * Constructs a CustomData.
     * 
     * @param formatType data format as <code>String</code>
     * @param data attribute for the data
     */
    public CustomData(String formatType, byte[] data) {
        super();

        assert null != formatType;
        assert null != data;
        assert (data.length > 0);

        this.formatType = formatType;
        this.data = data;
    }

    /**
     * Retrieves the format type
     * 
     * @return the format type
     * 
     * @see org.eclipse.gmf.runtime.common.ui.util.ICustomData#getFormatType()
     */
    public String getFormatType() {
        return formatType;
    }

    /**
     * Retrieves the data
     * 
     * @return the data as bytes
     * 
     * @see org.eclipse.gmf.runtime.common.ui.util.ICustomData#getData()
     */
    public byte[] getData() {
        return data;
    }
}
