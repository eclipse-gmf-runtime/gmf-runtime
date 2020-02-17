/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.l10n;

import org.eclipse.osgi.util.NLS;

/**
 * An accessor class for externalized strings.
 *
 * @author cmahoney
 */
public final class ExampleDiagramLogicMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.examples.runtime.diagram.logic.internal.l10n.ExampleDiagramLogicMessages";//$NON-NLS-1$

	private ExampleDiagramLogicMessages() {
		// Do not instantiate
	}

	public static String LogicVisualizer_DefaultLogicDiagramFileName;
	public static String LogicVisualizer_DefaultSavedBrowseDiagramDiagramProject;
	public static String CreationWizard_New_Logic_Diagram;
	public static String LogicWizardPage_Title;
	public static String LogicWizardPage_Description;
	public static String LogicTopicDiagramEditor_Unable_To_Save_Viz_Diagram_Title;
	public static String LogicTopicDiagramEditor_Unable_To_Save_Viz_Diagram_Text;
	public static String LogicVisualizerTopicDiagramEditor_Viz_Diagram_Exists;
	public static String LogicVisualizerTopicDiagramEditor_Viz_Diagram_Exists_Text;
	public static String LogicFlowCompartmentEditPart_Title;
	public static String LogicVisualizerEditor_EDITOR_OPEN_EXC_;
	public static String logic_ConnectionTool_Label;
	public static String logic_ConnectionTool_Description;
	public static String logic_LEDTool_Label;
	public static String logic_LEDTool_Description;
	public static String logic_LogicFlowTool_Label;
	public static String logic_LogicFlowTool_Description;
	public static String logic_CircuitTool_Label;
	public static String logic_CircuitTool_Description;
	public static String logic_OrGateTool_Label;
	public static String logic_OrGateTool_Description;
	public static String logic_AndGateTool_Label;
	public static String logic_AndGateTool_Description;
	public static String logic_XORGateTool_Label;
	public static String logic_XORGateTool_Description;
	public static String logic_HalfAdderTool_Label;
	public static String logic_HalfAdderTool_Description;
	public static String logic_FullAdderTool_Label;
	public static String logic_FullAdderTool_Description;
	public static String logic_Shape_Label;
	public static String SetLocationCommand_Label_Resize;
	public static String LogicWizardPage_StoreSemanticsSeparately;
	public static String LogicWizardPage_BrowseSemanticResource;
	public static String LogicWizardPage_BrowseSemanticDialogTitle;
	public static String LogicWizardPage_ModelOptions_GroupName;
	public static String LogicWizardPage_ModelOptions_EmptyModelName;
	public static String LogicWizardPage_ModelOptions_FourBitAdderModelName;
	public static String LogicPortsColor_Action_Label;
	public static String LogicPortsColor_Action_Tooltip;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, ExampleDiagramLogicMessages.class);
	}
}
