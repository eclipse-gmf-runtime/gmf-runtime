/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.util;

/**
 * Interface for handling text manipulation actions for inline editor
 * @author Yasser Lulu
 * 
 */
public interface IInlineTextActionHandler {
    /**
     * hooks (enable)the text action handlers
     */
    void hookHandlers();
    /**
     * unhooks (disable)the text action handlers
     */
    void unHookHandlers();
    /**
     * diposes this action-handler
     */
    void dispose();
}
