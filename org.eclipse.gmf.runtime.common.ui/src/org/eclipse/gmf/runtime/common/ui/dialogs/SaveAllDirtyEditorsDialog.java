/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.ListDialog;

import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.common.ui.internal.l10n.ResourceManager;

/**
 * Dialog which displays all dirty editors and prompts user if they should be
 * saved
 * 
 * @author myee
 */
public class SaveAllDirtyEditorsDialog
	extends ListDialog {

	/**
	 * SaveAllDirtyEditorsDialog constructor
	 * 
	 * @param parent
	 *            parent shell
	 */
	public SaveAllDirtyEditorsDialog(Shell parent) {
		super(parent);

		setTitle(ResourceManager
			.getI18NString("SaveAllDirtyEditorsDialog.title")); //$NON-NLS-1$
		setMessage(ResourceManager
			.getI18NString("SaveAllDirtyEditorsDialog.message")); //$NON-NLS-1$
		setAddCancelButton(true);

		setLabelProvider(new LabelProvider() {

			public Image getImage(Object element) {
				return ((IEditorPart) element).getTitleImage();
			}

			public String getText(Object element) {
				return ((IEditorPart) element).getTitle();
			}
		});

		setContentProvider(new IStructuredContentProvider() {

			List fContents;

			public Object[] getElements(Object inputElement) {
				if (fContents != null && fContents == inputElement)
					return fContents.toArray();
				return new Object[0];
			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				if (newInput instanceof List)
					fContents = (List) newInput;
				else
					fContents = null;
			}

			public void dispose() {
				// do nothing
			}
		});

		setInput(Arrays.asList(getDirtyEditors()));
	}

	/**
	 * Returns the dirty editors
	 * 
	 * @return the dirty editors
	 */
	public static IEditorPart[] getDirtyEditors() {
		Set inputs = new HashSet();
		List result = new ArrayList(0);
		IWorkbench workbench = CommonUIPlugin.getDefault().getWorkbench();
		IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
		for (int i = 0; i < windows.length; i++) {
			IWorkbenchPage[] pages = windows[i].getPages();
			for (int x = 0; x < pages.length; x++) {
				IEditorPart[] editors = pages[x].getDirtyEditors();
				for (int z = 0; z < editors.length; z++) {
					IEditorPart ep = editors[z];
					IEditorInput input = ep.getEditorInput();
					if (!inputs.contains(input)) {
						inputs.add(input);
						result.add(ep);
					}
				}
			}
		}
		return (IEditorPart[]) result.toArray(new IEditorPart[result.size()]);
	}

}