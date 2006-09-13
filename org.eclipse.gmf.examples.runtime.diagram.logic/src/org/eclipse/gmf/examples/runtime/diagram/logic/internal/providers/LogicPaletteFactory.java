/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.Tool;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.util.LogicSemanticType;
import org.eclipse.gmf.runtime.diagram.ui.services.palette.PaletteFactory;
import org.eclipse.gmf.runtime.diagram.ui.tools.ConnectionCreationTool;
import org.eclipse.gmf.runtime.diagram.ui.tools.CreationTool;

/**
 * A palette factory for Logic Entries
 * 
 * @author qili
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 */
public class LogicPaletteFactory
	extends PaletteFactory.Adapter {
	
	/** list of supported tool types. */
	private Map toolMap = new HashMap();
	{
		toolMap.put( LogicConstants.TOOL_LED, new CreationTool(LogicSemanticType.LED) );
		toolMap.put( LogicConstants.TOOL_CIRCUIT, new CreationTool(LogicSemanticType.CIRCUIT) );
		toolMap.put( LogicConstants.TOOL_ORGATE, new CreationTool(LogicSemanticType.ORGATE) );
		toolMap.put( LogicConstants.TOOL_ANDGATE, new CreationTool(LogicSemanticType.ANDGATE) );
		toolMap.put( LogicConstants.TOOL_XORGATE, new CreationTool(LogicSemanticType.XORGATE) );
		toolMap.put( LogicConstants.TOOL_FLOWCONTAINER, new CreationTool(LogicSemanticType.FLOWCONTAINER) );
	}
	
	/** list of supported tool types. */
	private Map connectorMap = new HashMap();
	{
		connectorMap.put( LogicConstants.TOOL_CONNECTION, new ConnectionCreationTool(LogicSemanticType.WIRE) );
	}

	/*
	 *  Create the tool according to type  	 
	 */
	public Tool createTool(String toolId) {
		
		if (toolId.equals(LogicConstants.TOOL_CONNECTION)) {
			return (ConnectionCreationTool)connectorMap.get(toolId);
		} else {
			return (CreationTool)toolMap.get(toolId);
		}
	}
}