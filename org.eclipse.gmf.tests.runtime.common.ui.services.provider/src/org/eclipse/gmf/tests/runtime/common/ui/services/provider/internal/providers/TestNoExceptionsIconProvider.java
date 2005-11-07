/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.common.ui.services.provider.internal.providers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProviderChangeListener;
import org.eclipse.gmf.runtime.common.ui.services.icon.GetIconOperation;
import org.eclipse.gmf.runtime.common.ui.services.icon.IIconProvider;
import org.eclipse.gmf.tests.runtime.common.ui.services.provider.internal.testClasses.TestAdaptable;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * Base test icon provider.
 * 
 * @author wdiu, Wayne Diu
 */
public class TestNoExceptionsIconProvider
	implements IIconProvider {

	public boolean provides(IOperation operation) {
		if (operation instanceof GetIconOperation) {
			if (((GetIconOperation) operation).getHint() instanceof TestAdaptable) {
				return true;
			}
		}
		return false;
	}

	public Image getIcon(IAdaptable hint, int flags) {
		return new Image(Display.getDefault(), 10, 10);
	}

	public void addProviderChangeListener(IProviderChangeListener listener) {
		//do nothing
	}

	public void removeProviderChangeListener(IProviderChangeListener listener) {
		//do nothing
	}

}
