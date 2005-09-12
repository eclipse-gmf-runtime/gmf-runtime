/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIDebugOptions;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIStatusCodes;

/**
 * This class can be used to transfer an array of <code>ICustomData</code>
 * between two parts in a workbench in a drag and drop operation or used to
 * store/retrieve custom data to/from the system clipboard respectively.
 *
 * @author Vishy Ramaswamy
 */
public final class CustomDataTransfer extends ByteArrayTransfer {
    private static final String XTOOLS = "XTOOLS"; //$NON-NLS-1$
    private static final int XTOOLSID = registerType(XTOOLS);
    private static CustomDataTransfer instance = new CustomDataTransfer();

    /**
     * Return the singleton.
     * 
     * @return a singleton instance of <code>CustomDataTransfer</code>
     */
    public static CustomDataTransfer getInstance() {
        return instance;
    }

    /**
     * Constructor for ClipboardTransferAgent.
     */
    private CustomDataTransfer() {
        super();
    }

    /**
     * Converts an array of <code>ICustomData</code> into a <code>byte[]</code>
     * @see org.eclipse.swt.dnd.Transfer#javaToNative(java.lang.Object, org.eclipse.swt.dnd.TransferData)
     */
    public void javaToNative(Object object, TransferData transferData) {
        if (object == null || !(object instanceof ICustomData[]))
            return;

        if (isSupportedType(transferData)) {
            ICustomData[] myTypes = (ICustomData[]) object;
            try {
                // write data to a byte array and then ask super to convert to pMedium
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                DataOutputStream writeOut = new DataOutputStream(out);

                /* The serialization format is:
                 * (int) number of elements
                 * Then, the following for each element:
                 * (String) format type
                 * (byte[]) data
                 */

                writeOut.writeInt(myTypes.length);

                byte[] array = null;

                for (int i = 0; i < myTypes.length; i++) {
                    writeOut.writeUTF(myTypes[i].getFormatType());
                    array = myTypes[i].getData();

                    writeOut.writeInt(array.length);
                    writeOut.write(array);
                }

                byte[] buffer = out.toByteArray();
                writeOut.close();

                super.javaToNative(buffer, transferData);

            } catch (IOException e) {
				Trace.catching(CommonUIPlugin.getDefault(), CommonUIDebugOptions.EXCEPTIONS_CATCHING, getClass(), "javaToNative", e); //$NON-NLS-1$
				Log.error(CommonUIPlugin.getDefault(), CommonUIStatusCodes.SERVICE_FAILURE, "javaToNative", e); //$NON-NLS-1$
			}
        }
    }

    /**
     * Converts a <code>byte[]</code> in to an array of <code>ICustomData</code> 
     * @return <code>Object</code>
     * @see org.eclipse.swt.dnd.Transfer#nativeToJava(org.eclipse.swt.dnd.TransferData)
     */
    public Object nativeToJava(TransferData transferData) {

        if (isSupportedType(transferData)) {

            byte[] buffer = (byte[]) super.nativeToJava(transferData);

            if (buffer == null) {
                return null;
            }

            try {
                ByteArrayInputStream in = new ByteArrayInputStream(buffer);
                DataInputStream readIn = new DataInputStream(in);

                int count = readIn.readInt();
                ICustomData[] myData = new ICustomData[count];

                String type = null;
                byte[] array = null;

                for (int i = 0; i < count; i++) {
                    type = readIn.readUTF();
                    array = new byte[readIn.readInt()];
                    readIn.read(array);

                    myData[i] = new CustomData(type, array);
                }

                readIn.close();

                return myData;

            } catch (IOException ex) {
				Trace.catching(CommonUIPlugin.getDefault(), CommonUIDebugOptions.EXCEPTIONS_CATCHING, getClass(), "nativeToJava", ex); //$NON-NLS-1$
				Log.error(CommonUIPlugin.getDefault(), CommonUIStatusCodes.SERVICE_FAILURE, "nativeToJava", ex); //$NON-NLS-1$
                return null;
            }
        }

        return null;
    }

    /**
     * Returns the type ids supported by this agent
     * @return <code>int[]</code>
     * @see org.eclipse.swt.dnd.Transfer#getTypeIds()
     */
    protected int[] getTypeIds() {
        return new int[] { XTOOLSID };
    }

    /**
     * Returns the type names supported by this agent
     * @return <code>String[]</code>
     * @see org.eclipse.swt.dnd.Transfer#getTypeNames()
     */
    protected String[] getTypeNames() {
        return new String[] { XTOOLS };
    }
}
