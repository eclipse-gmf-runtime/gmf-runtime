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
import org.eclipse.gmf.runtime.common.ui.services.parser.GetParserOperation;
import org.eclipse.gmf.tests.runtime.common.ui.services.provider.internal.testClasses.TestAdaptable;

/**
 * For provides test will fail the first time, then pass on subsequent times.
 * 
 * @author wdiu, Wayne Diu
 */
public class TestParserPolicy
	implements IProviderPolicy {

	/**
	 * Set to true after the provides method has been executed
	 */
	public static boolean providesExecuted = false;

	/*
	 * Returns false unless the GetParserOperation hint is TestAdaptable
	 */
	public boolean provides(IOperation operation) {
		providesExecuted = true;

		if (operation instanceof GetParserOperation) {
			return (((GetParserOperation) operation).getHint() instanceof TestAdaptable);
		}

		return false;
	}

}