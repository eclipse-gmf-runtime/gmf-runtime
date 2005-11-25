/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gmf.examples.runtime.diagram.logic.internal.l10n.ExampleDiagramLogicMessages;

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
			return ExampleDiagramLogicMessages.logic_LEDTool_Label;
		if( toGet.equals( TOOL_FLOWCONTAINER ) )
			return ExampleDiagramLogicMessages.logic_LogicFlowTool_Label;
		if( toGet.equals( TOOL_CIRCUIT ) )
			return ExampleDiagramLogicMessages.logic_CircuitTool_Label; 
		if( toGet.equals( TOOL_ORGATE ) )
			return ExampleDiagramLogicMessages.logic_OrGateTool_Label; 
		if( toGet.equals( TOOL_ANDGATE ) )
			return ExampleDiagramLogicMessages.logic_AndGateTool_Label; 
		if( toGet.equals( TOOL_XORGATE ) )
			return ExampleDiagramLogicMessages.logic_XORGateTool_Label; 
		if( toGet.equals( TOOL_HALFADDER ) )
			return ExampleDiagramLogicMessages.logic_HalfAdderTool_Label; 
		if( toGet.equals( TOOL_FULLADDER ) )
			return ExampleDiagramLogicMessages.logic_FullAdderTool_Label; 
		
		return ExampleDiagramLogicMessages.logic_Shape_Label;
	}
}
