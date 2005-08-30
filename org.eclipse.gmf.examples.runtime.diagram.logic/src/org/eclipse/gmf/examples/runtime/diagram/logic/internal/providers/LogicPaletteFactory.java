/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.Tool;

import org.eclipse.gmf.examples.runtime.diagram.logic.model.util.LogicSemanticType;
import org.eclipse.gmf.runtime.diagram.ui.tools.ConnectorCreationTool;
import org.eclipse.gmf.runtime.diagram.ui.tools.CreationTool;
import org.eclipse.gmf.runtime.gef.ui.palette.PaletteFactory;

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
		connectorMap.put( LogicConstants.TOOL_CONNECTION, new ConnectorCreationTool(LogicSemanticType.WIRE) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rational.xtools.gef.ui.palette.PaletteFactory#createTool(java.lang.String)
	 */
	public Tool createTool(String toolId) {
		
		if (toolId.equals(LogicConstants.TOOL_CONNECTION)) {
			return (ConnectorCreationTool)connectorMap.get(toolId);
		} else {
			return (CreationTool)toolMap.get(toolId);
		}
	}
}