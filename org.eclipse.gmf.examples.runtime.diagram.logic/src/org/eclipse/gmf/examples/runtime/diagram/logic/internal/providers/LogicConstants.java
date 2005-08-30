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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gmf.examples.runtime.diagram.logic.internal.l10n.LogicResourceManager;

/**
 * @author qili
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 *
 */
public class LogicConstants {

	// Supported Shapes
	public static final String TOOL_LED            = "LED"; //$NON-NLS-1$
	public static final String TOOL_FLOWCONTAINER  = "FlowContainer"; //$NON-NLS-1$
	public static final String TOOL_CIRCUIT 	   = "circuit";	//$NON-NLS-1$
	public static final String TOOL_ORGATE		   = "OrGate"; //$NON-NLS-1$
	public static final String TOOL_ANDGATE		   = "AndGate"; //$NON-NLS-1$
	public static final String TOOL_XORGATE		   = "XORGate"; //$NON-NLS-1$
	public static final String TOOL_HALFADDER	   = "HalfAdder"; //$NON-NLS-1$
	public static final String TOOL_FULLADDER	   = "FullAdder"; //$NON-NLS-1$
	public static final String LOGIC_SHAPE_COMPARTMENT   = "LogicCompartment"; //$NON-NLS-1$
	public static final String LOGIC_FLOW_COMPARTMENT   = "LogicFlowCompartment"; //$NON-NLS-1$
	
	// Logic Shape Icons
	public static final String ICON_LED            = "ledicon16.gif";  //$NON-NLS-1$
	public static final String ICON_LOGICFLOW	   = "logicflow16.gif"; //$NON-NLS-1$
	public static final String ICON_CIRCUIT		   = "circuit16.gif"; //$NON-NLS-1$
	public static final String ICON_ORGATE		   = "or16.gif"; //$NON-NLS-1$
	public static final String ICON_ANDGATE		   = "and16.gif"; //$NON-NLS-1$
	public static final String ICON_XORGATE		   = "xor16.gif"; //$NON-NLS-1$
	public static final String ICON_HALFADDER	   = "halfadder16.gif"; //$NON-NLS-1$
	public static final String ICON_FULLADDER	   = "fulladder16.gif"; //$NON-NLS-1$
	
	// Supported Connections
	public static final String TOOL_CONNECTION     = "wire"; //$NON-NLS-1$
	// Connection Icons
	public static final String ICON_CONNECTION     = "connection16.gif";  //$NON-NLS-1$
	
	public static List getSupportedShapes() {
		
		List toReturn = new ArrayList();
		
		toReturn.add( TOOL_LED );
		toReturn.add( TOOL_FLOWCONTAINER );
		toReturn.add( TOOL_CIRCUIT );
		toReturn.add( TOOL_ORGATE );
		toReturn.add( TOOL_ANDGATE );
		toReturn.add( TOOL_XORGATE );
		toReturn.add( TOOL_HALFADDER );
		toReturn.add( TOOL_FULLADDER );
		
		return toReturn;
	}
	
	public static List getSupportedConnections() {
		
		List toReturn = new ArrayList();
		
		toReturn.add( TOOL_CONNECTION );
			
		return toReturn;
	}
	
	public static String getShapeLocalizedType(String typeName) {
		return getShapeLabel(typeName).replaceFirst("&", "");//$NON-NLS-2$//$NON-NLS-1$
	}
	
	public static String getDisplayName(String label) {
		return label.replaceFirst("&", "");//$NON-NLS-2$//$NON-NLS-1$
	}
	
	public static String getShapeLabel( String toGet ) {
		
		if( toGet.equals( TOOL_LED ) )
			return LogicResourceManager.getInstance().getString("logic.LEDTool.Label"); //$NON-NLS-1$
		if( toGet.equals( TOOL_FLOWCONTAINER ) )
			return LogicResourceManager.getInstance().getString("logic.LogicFlowTool.Label"); //$NON-NLS-1$
		if( toGet.equals( TOOL_CIRCUIT ) )
			return LogicResourceManager.getInstance().getString("logic.CircuitTool.Label"); //$NON-NLS-1$
		if( toGet.equals( TOOL_ORGATE ) )
			return LogicResourceManager.getInstance().getString("logic.OrGateTool.Label"); //$NON-NLS-1$
		if( toGet.equals( TOOL_ANDGATE ) )
			return LogicResourceManager.getInstance().getString("logic.AndGateTool.Label"); //$NON-NLS-1$
		if( toGet.equals( TOOL_XORGATE ) )
			return LogicResourceManager.getInstance().getString("logic.XORGateTool.Label"); //$NON-NLS-1$
		if( toGet.equals( TOOL_HALFADDER ) )
			return LogicResourceManager.getInstance().getString("logic.HalfAdderTool.Label"); //$NON-NLS-1$
		if( toGet.equals( TOOL_FULLADDER ) )
			return LogicResourceManager.getInstance().getString("logic.FullAdderTool.Label"); //$NON-NLS-1$
		
		return LogicResourceManager.getInstance().getString("logic.Shape.Label"); //$NON-NLS-1$
	}
}
