/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.common.ui.services.action.contributionitem;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * This defines the workbench part that the test contribution item providers
 * will contribute to. This is referenced in the plugin.xml.
 *
 * @author cmahoney
 */
public class MyWorkbenchPart implements IWorkbenchPart {

	@Override
	public void addPropertyListener(IPropertyListener listener) {
		// do nothing
	}

	@Override
	public void createPartControl(Composite parent) {
		// do nothing
	}

	@Override
	public void dispose() {
		// do nothing
	}

	@Override
	public IWorkbenchPartSite getSite() {
		return null;
	}

	@Override
	public String getTitle() {
		return null;
	}

	@Override
	public Image getTitleImage() {
		return null;
	}

	@Override
	public String getTitleToolTip() {
		return null;
	}

	@Override
	public void removePropertyListener(IPropertyListener listener) {
		// do nothing
	}

	@Override
	public void setFocus() {
		// do nothing
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

}
