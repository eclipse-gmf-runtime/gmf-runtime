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

package org.eclipse.gmf.tests.runtime.common.ui.internal.action;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.jface.action.Action;
import org.junit.jupiter.api.Test;

/**
 * Tests the action classes that remove the mnemonics and specifically removes
 * the Korean (&X). See RATLC00530818.
 *
 * @author cmahoney
 */
public class RemoveMnemonicTests {
	@Test
	public void test_RATLC00530818() {
		String cmd = "My Command"; //$NON-NLS-1$
		String cmdEnglish = "My &Command"; //$NON-NLS-1$
		String cmdKorean = "My Command(&C)"; //$NON-NLS-1$

		assertEquals(cmd, Action.removeMnemonics(cmdEnglish));
		assertEquals(cmd, Action.removeMnemonics(cmdKorean));

		assertEquals(cmd, Action.removeMnemonics(cmdEnglish));
		assertEquals(cmd, Action.removeMnemonics(cmdKorean));
	}
}