/******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers;

import org.eclipse.gef.Tool;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.util.LogicSemanticType;
import org.eclipse.gmf.runtime.diagram.ui.services.palette.PaletteFactory;
import org.eclipse.gmf.runtime.diagram.ui.tools.ConnectionCreationTool;
import org.eclipse.gmf.runtime.diagram.ui.tools.CreationTool;

/**
 * A palette factory for Logic Entries
 * 
 * @author qili, mmostafa
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 */
public class LogicPaletteFactory
	extends PaletteFactory.Adapter {
	
	/*
	 *  Create the tool according to type  	 
	 */
	public Tool createTool(String toolId) {
        if (toolId.equals(LogicConstants.TOOL_LED)){
            return new CreationTool(LogicSemanticType.LED);
        }else if (toolId.equals(LogicConstants.TOOL_CIRCUIT)) {
            return new CreationTool(LogicSemanticType.CIRCUIT);
        }else if (toolId.equals(LogicConstants.TOOL_ORGATE)) {
            return new CreationTool(LogicSemanticType.ORGATE);
        }else if (toolId.equals(LogicConstants.TOOL_ANDGATE)) {
            return new CreationTool(LogicSemanticType.ANDGATE);
        }else if (toolId.equals(LogicConstants.TOOL_XORGATE)) {
            return new CreationTool(LogicSemanticType.XORGATE);
        }else if (toolId.equals(LogicConstants.TOOL_FLOWCONTAINER)) {
            return new CreationTool(LogicSemanticType.FLOWCONTAINER);
        
        }else if (toolId.equals(LogicConstants.TOOL_CONNECTION)) {
            return new ConnectionCreationTool(LogicSemanticType.WIRE);
        }
        return null;
	}
}