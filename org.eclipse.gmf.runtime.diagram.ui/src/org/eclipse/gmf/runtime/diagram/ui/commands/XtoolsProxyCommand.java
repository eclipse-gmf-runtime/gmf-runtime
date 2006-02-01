package org.eclipse.gmf.runtime.diagram.ui.commands;

import org.eclipse.gef.commands.Command;

/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


/**
 * @author mmostafa
 * @deprecated use {@link CommandProxy} instead
 *          deprecation date : Feb 01 , 06
 *          removal date : Feb 15, 06
 */
public class XtoolsProxyCommand
    extends CommandProxy {

    /**
     * Method XtoolsProxyCommand.
     * @param command
     */
    public XtoolsProxyCommand(Command command) {
        super(command);
    }
}
