/******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers;

import org.eclipse.gef.Tool;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.util.LogicSemanticType;
import org.eclipse.gmf.runtime.diagram.ui.services.palette.PaletteFactory;
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
            return new CreationTool(LogicSemanticType.WIRE);
        }
        return null;
	}
}