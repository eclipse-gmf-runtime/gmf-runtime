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
package org.eclipse.gmf.tests.runtime.common.ui.services.provider.internal.policies;

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProviderPolicy;
import org.eclipse.gmf.runtime.common.ui.services.icon.GetIconOperation;
import org.eclipse.gmf.runtime.common.ui.services.icon.IIconOperation;
import org.eclipse.gmf.tests.runtime.common.ui.services.provider.internal.testClasses.TestAdaptable;

/**
 * Basic icon policy that does not throw exceptions.
 * 
 * @author wdiu, Wayne Diu
 */
public class TestNoExceptionsAdaptableIconPolicy
	implements IProviderPolicy {

	public boolean provides(IOperation operation) {
		if (operation instanceof GetIconOperation) {
			return (((IIconOperation) operation).getHint() instanceof TestAdaptable);
		}
		return false;
	}

}
