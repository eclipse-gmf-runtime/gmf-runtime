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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;

/**
 * An interface used to provide the string to edit for tree items when using the 
 * tree-inline-text-editor
 * 
 * @author Yasser Lulu
 * 
 */
public interface IEditStringProvider {
    /**
     * Determines if node can be edited.
     * 
     * @param element The data associated with the tree-node being edited
     * @return boolean <code>true</code> if node can be edited, otherwise <code>false</code>
     */
	boolean canEdit(Object element);
    
	/**
     * Returns the initial to-be-edited string to diplay in the text-editor.
     * for a given tree-node
     * @param element The data associated with the tree-node being edited
     * @return String the editable string to display for the user
     */
    String getEditString(Object element);
    
    /**
     * Handles the newly edited string entered by the user for the given element.
     * 
     * @param element The data associated with the tree-node being edited
     * @param newString The new string entered by the user 
     * @return IStatus the status of string editing operation
     */
    IStatus setEditString(Object element, String newString);
    
    /**
     * Retrieve <code>IContentAssistProcessor</code> for specified object.
     *  
     * @param element an object for which completion processor is to be retrieved
     * @return the completion processor
     */
    IContentAssistProcessor getCompletionProcessor(Object element);
}
