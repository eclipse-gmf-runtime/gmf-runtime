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

package org.eclipse.gmf.runtime.common.ui.services.parser;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;

/**
 * The interface for all parsers
 * 
 * @author Michael Yee
 */
public interface IParser {

    /**
     * Gets the string that is a subject to edit.
     * 
     * @param element	the element for which the edit string is requested
     * @param flags  	the bitflag of parser options
     * @return String	current contents of the edit string
     */
    public String getEditString(IAdaptable element, int flags);
    
    /**
     * Tests if the user-entered string is in a valid form that can be parsed
     * 
     * @param element the element for which the edit string is applied to
     * @param editString the user-entered string
     * @return IParserEditStatus <code>EDITABLE_STATUS</code> if the edit string is valid, otherwise
     * 				  a status that includes information describing why it is an invalid edit string.
     */
    public IParserEditStatus isValidEditString(IAdaptable element, String editString);

    /**
     * Gets the command that applies the new edit string
     * 
     * @param element		the element for which the parse command is requested
     * @param newString	to be set by the command
     * @param flags		the bitflag of parser options
     * @return ICommand	command that when executed will result in
     * 					  	setting the newString
     */
    public ICommand getParseCommand(
        IAdaptable element,
        String newString,
        int flags);
        

    /**
     * Returns a string intended for display. That might be a different string that the one
     * returned by getEditString. E.g., for stereotypes, the print string will be adorned
     * with <<>> brackets while edit string will not be adorned. 
     * 
     * @param element	the element for which the print string is requested
     * @param flags	the bitflag of parser options
     * @return String	the print string
     */
    public String getPrintString(IAdaptable element, int flags);

    /**
     * Determines if the event requires action from the parser point of view.
     * The action usually involves some visual refreshment.
     * 
     * @param event	the event, an instance of IElementEvent
     * @param flags	the bitflag of parser options
     * @return boolean	<code>true</code> if the event affects the parser,
     * 					otherwise <code>false</code>.
     */
    public boolean isAffectingEvent(Object event, int flags);
    
    /**
     * Returns the parser's content assist processor
     *  
     * @param element the element
     * @return the content assist processor
     */
    public IContentAssistProcessor getCompletionProcessor(IAdaptable element);

}
