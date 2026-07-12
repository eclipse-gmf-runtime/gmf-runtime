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

package org.eclipse.gmf.tests.runtime.common.core.internal.util;

import static org.junit.jupiter.api.Assertions.fail;

import org.eclipse.gmf.runtime.common.core.util.Proxy;
import org.junit.jupiter.api.Test;

public class ProxyTest {

	protected static class Fixture extends Proxy {

		protected Fixture(Object realObject) {
			super(realObject);
		}

	}

	@Test
	public void test_Proxy() {
		try {
			new Fixture(null);
			fail();
		} catch (Throwable e) {
			// for our implementation, the Throwable is an AssertionError
			// nothing to do
		}
	}

}
