/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.action.actions.global;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.gmf.runtime.common.ui.util.CustomDataTransfer;
import org.eclipse.gmf.runtime.common.ui.util.ICustomData;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;

/**
 * This class is used to add/retrieve data to/from the system clipboard. This
 * class should be used within the global action infrastructure framework.
 * 
 * @author Vishy Ramaswamy
 */
public final class ClipboardManager {
	
	/**
	 * String constant for the common format (paste on ME or a daigram (usually
	 * as a result of Harvesting)
	 */
	public static final String COMMON_FORMAT = "COMMON_FORMAT"; //$NON-NLS-1$

	/**
	 * Create the ClipboardManager
	 */
	private static ClipboardManager instance = new ClipboardManager();

	/**
	 * Attribute for the cache
	 */
	private Hashtable list = new Hashtable();

	/**
	 * The clipboard state
	 */
	private ClipboardState clipboardState = ClipboardState.NORMAL;

	/**
	 * Constructor for ClipboardManager.
	 */
	private ClipboardManager() {
		super();
	}

	/**
	 * Return the singleton.
	 * 
	 * @return a singleton instance of <code>ClipboardManager</code>
	 */
	public static ClipboardManager getInstance() {
		return instance;
	}

	/**
	 * Returns the system clipboard contents
	 * 
	 * @param dataType
	 *            The transfer agent
	 * @return Object Data associated with the transfer agent
	 */
	public Object getClipboardContents(Transfer dataType,
			ClipboardContentsHelper helper) {
		return helper.getClipboardContents(dataType);
	}

	/**
	 * Adds data to the internal cache. The clipboard state is reset to NORMAL.
	 * 
	 * @param data
	 *            The data for the transfer agent
	 * @param dataType
	 *            The transfer agent
	 */
	public void addToCache(Object data, Transfer dataType) {
		addToCache(data, dataType, ClipboardState.NORMAL);
	}

	/**
	 * Adds data to the internal cache. The clipboard state is set to the
	 * specified value.
	 * 
	 * @param data
	 *            The data for the transfer agent
	 * @param dataType
	 *            The transfer agent
	 * @param state
	 *            The clipboard state
	 */
	public void addToCache(Object data, Transfer dataType, ClipboardState state) {

		/* add it to the cache */
		if (data != null && dataType != null) {

			/* check if transfer type is custom data */
			if (dataType instanceof CustomDataTransfer) {
				/* if the data is correct */
				if (data instanceof ICustomData[]) {
					/* append to the already existing list */
					ICustomData[] array = (ICustomData[]) getList().get(
						CustomDataTransfer.getInstance());
					if (array != null) {
						List oldlist = Arrays.asList(array);
						List newlist = Arrays.asList((ICustomData[]) data);
						List compound = new ArrayList();
						compound.addAll(oldlist);
						compound.addAll(newlist);
						array = new ICustomData[compound.size()];
						compound.toArray(array);
						getList().put(CustomDataTransfer.getInstance(), array);
					} else {
						getList().put(CustomDataTransfer.getInstance(), data);
					}
					this.clipboardState = state;
				}
			} else {
				/* add to the cache */
				getList().put(dataType, data);
				this.clipboardState = state;
			}
		}
	}

	/**
	 * Flushes the chache to the system clipboard and clears the cache NOTE:
	 * visibility changed from (protected) to (public) to support Harvesting
	 * feature
	 */
	public void flushCacheToClipboard() {
		if (getList().size() > 0) {

			/* create the clipboard instance */
			Clipboard clipboard = new Clipboard(Display.getCurrent());

			/* copy the data to an array */
			ArrayList data = new ArrayList();
			data.addAll(getList().values());

			/* copy the keys to an array */
			ArrayList keys = new ArrayList();
			keys.addAll(getList().keySet());

			Transfer[] transfer = new Transfer[keys.size()];
			keys.toArray(transfer);

			/* set the clipboard contents */
			clipboard.setContents(data.toArray(), transfer);

			/* free the system resources associated with the clipboard */
			clipboard.dispose();

			/* clear the list */
			getList().clear();
		}
	}

	/**
	 * Clear the cache
	 */
	protected void clearCache() {
		getList().clear();
	}

	/**
	 * Returns a platform specific list of the data types currently available on
	 * the system clipboard.
	 * 
	 * <p>
	 * Note: <code>getAvailableTypeNames</code> is a utility for writing a
	 * Transfer sub-class. It should NOT be used within an application because
	 * it provides platform specific information.
	 * </p>
	 * 
	 * @return a platform specific list of the data types currently available
	 *          on the system clipboard
	 */
	public String[] getAvailableTypeNames() {

		String[] types = new String[0];

		/* create the clipboard instance */
		Clipboard clipboard = new Clipboard(Display.getCurrent());

		/* copy the data to an array */
		types = clipboard.getAvailableTypeNames();

		/* free the system resources associated with the clipboard */
		clipboard.dispose();

		return types;
	}

	/**
	 * Returns the list.
	 * 
	 * @return Hashtable
	 */
	private Hashtable getList() {
		return list;
	}

	/**
	 * Removes the transfer agent from the cache
	 * 
	 * @param dataType
	 *            The transfer agent
	 */
	public void removeFromCache(Transfer dataType) {
		assert null != dataType;
		getList().remove(dataType);
	}

	/**
	 * Checks if the cache has the specified transfer agent
	 * 
	 * @param dataType
	 *            The transfer agent
	 * @return boolean
	 */
	public boolean doesCacheHaveType(Transfer dataType) {
		assert null != dataType;
		return getList().containsKey(dataType);
	}
	
	/**
	 * Checks if the system clipboard has any ICustomData with the specified
	 * format
	 * 
	 * @param format
	 *            The format
	 * @return boolean
	 */
	public boolean doesClipboardHaveData(String format, ClipboardContentsHelper contentsHelper) {
		assert null != format;

		/* get the clipboard data for the custom format */
		Object data = getClipboardContents(CustomDataTransfer.getInstance(), contentsHelper);

		/* check if the format exists */
		if (data != null && data instanceof ICustomData[]) {
			ICustomData[] array = (ICustomData[]) data;

			for (int i = 0; i < array.length; i++) {
				if (format.equals(array[i].getFormatType())) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Checks if the system clipboard has any data with the specified transfer
	 * agent
	 * 
	 * @param dataType
	 *            The transfer agent
	 * @return boolean
	 */
	public boolean doesClipboardHaveData(Transfer dataType, ClipboardContentsHelper contentsHelper) {
		assert null != dataType;

		/* get the clipboard data for the transfer */
		Object data = getClipboardContents(dataType, contentsHelper);

		/* check if the data exists */
		if (data != null) {
			return true;
		}

		return false;
	}
	
	/**
	 * Gets the ICustomData associated with the specified format from the system
	 * clipboard.
	 * 
	 * @param format
	 *            The format
	 * @return ICustomData[]
	 */
	public ICustomData[] getClipboardData(String format, ClipboardContentsHelper contentsHelper) {
		assert null != format;

		/* get the clipboard data for the custom format */
		Object data = getClipboardContents(CustomDataTransfer.getInstance(), contentsHelper);

		/* check if the format exists */
		if (data != null && data instanceof ICustomData[]) {
			ICustomData[] array = (ICustomData[]) data;

			ArrayList dataList = new ArrayList();
			for (int i = 0; i < array.length; i++) {
				if (format.equals(array[i].getFormatType())) {
					dataList.add(array[i]);
				}
			}

			/* return the array */
			if (!dataList.isEmpty()) {
				array = new ICustomData[dataList.size()];
				dataList.toArray(array);

				return array;
			}
		}

		return null;
	}

	/**
	 * @return Returns the clipboard state.
	 */
	public ClipboardState getClipboardState() {
		return clipboardState;
	}

	/**
	 * @param clipboardState
	 *            The clipboard state to set.
	 */
	public void setClipboardState(ClipboardState clipboardState) {
		this.clipboardState = clipboardState;
	}
}