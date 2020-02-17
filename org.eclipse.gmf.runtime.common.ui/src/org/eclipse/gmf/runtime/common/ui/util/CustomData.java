/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
