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
