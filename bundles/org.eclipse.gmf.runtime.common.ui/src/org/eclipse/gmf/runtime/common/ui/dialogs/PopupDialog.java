/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.dialogs;

import java.util.List;

import org.eclipse.gmf.runtime.common.ui.internal.l10n.CommonUIMessages;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ListDialog;

/**
 * A simple dialog with a list from which the user can select one item.
 * 
 * @author ldamus
 */
public class PopupDialog
	extends ListDialog {

	/**
	 * Content Provider.
	 */
	private IStructuredContentProvider contentProvider = new IStructuredContentProvider() {

		private List contents;

		public void dispose() {
			// nothing to dispose
		}

		public Object[] getElements(Object inputElement) {
			if (contents != null && contents == inputElement) {
				return contents.toArray();
			}
			return new Object[0];
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if (newInput instanceof List) {
				contents = (List) newInput;
			} else {
				contents = null;
			}
		}
	};

	/**
	 * Constructs a new instance
	 * 
	 * @param parent
	 *            the shell
	 * @param contents
	 *            the elements to present in the list
	 * @param labelProvider
	 *            the label provider
	 */
	public PopupDialog(Shell parent, List contents, ILabelProvider labelProvider) {
		super(parent);
		setLabelProvider(labelProvider);
		setContentProvider(contentProvider);
		setInput(contents);
		setTitle(CommonUIMessages.PopupDialog_title);
		setMessage(CommonUIMessages.PopupDialog_message);
	}

}