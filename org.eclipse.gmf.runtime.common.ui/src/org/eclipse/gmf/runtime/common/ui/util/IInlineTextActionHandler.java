/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
