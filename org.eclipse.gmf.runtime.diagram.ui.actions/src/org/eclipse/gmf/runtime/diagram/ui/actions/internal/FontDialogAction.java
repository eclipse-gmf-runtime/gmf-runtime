/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.internal.actions.PropertyChangeAction;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.requests.ChangePropertyValueRequest;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;

/**
 * @author melaasar
 * @canBeSeenBy %level1
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class FontDialogAction extends PropertyChangeAction {

	/**
	 * @param workbenchPage
	 * @param propertyId
	 * @param propertyName
	 */
	public FontDialogAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage, Properties.ID_FONTCOLOR,
			DiagramUIActionsMessages.PropertyDescriptorFactory_Font);
		setId(ActionIds.ACTION_FONT_DIALOG);
		setText(DiagramUIActionsMessages.FontAction_text);
		setToolTipText(DiagramUIActionsMessages.FontAction_tooltip);
		setImageDescriptor(DiagramUIActionsPluginImages.DESC_FONT_COLOR);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.actions.PropertyChangeAction#getNewPropertyValue()
	 */
	protected Object getNewPropertyValue() {
		return null;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#doRun(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void doRun(IProgressMonitor progressMonitor) {
		String name = (String) getOperationSetPropertyValue(Properties.ID_FONTNAME);
		Integer height = (Integer) getOperationSetPropertyValue(Properties.ID_FONTSIZE);
		Boolean bold = (Boolean) getOperationSetPropertyValue(Properties.ID_FONTBOLD);
		Boolean italic = (Boolean) getOperationSetPropertyValue(Properties.ID_FONTITALIC);
		int style = (bold.booleanValue()? SWT.BOLD : SWT.NORMAL) | (italic.booleanValue()? SWT.ITALIC : SWT.NORMAL);
		FontData initFontData = new FontData(name, height.intValue(), style);

		Integer color = (Integer) getOperationSetPropertyValue(Properties.ID_FONTCOLOR);
		RGB initFontColor = FigureUtilities.integerToRGB(color);
		
		Shell shell = getDiagramGraphicalViewer().getControl().getShell();
		FontDialog fontDialog = new FontDialog(shell);
		fontDialog.setFontList(new FontData[] {initFontData});
		fontDialog.setRGB(initFontColor);
		FontData fData = fontDialog.open();
		RGB fColor = fontDialog.getRGB();

		if (fData != null && fColor != null) {
			CompoundCommand cc = new CompoundCommand(DiagramUIActionsMessages.PropertyDescriptorFactory_Font);
			cc.add(getCommand(new ChangePropertyValueRequest(Properties.ID_FONTNAME, Properties.ID_FONTNAME, fData.getName())));
			cc.add(getCommand(new ChangePropertyValueRequest(Properties.ID_FONTSIZE, Properties.ID_FONTSIZE, new Integer(fData.getHeight()))));
			cc.add(getCommand(new ChangePropertyValueRequest(Properties.ID_FONTBOLD, Properties.ID_FONTBOLD, Boolean.valueOf((fData.getStyle() & SWT.BOLD) != 0))));
			cc.add(getCommand(new ChangePropertyValueRequest(Properties.ID_FONTITALIC, Properties.ID_FONTITALIC, Boolean.valueOf((fData.getStyle() & SWT.ITALIC) != 0))));
			cc.add(getCommand(new ChangePropertyValueRequest(Properties.ID_FONTCOLOR, Properties.ID_FONTCOLOR, FigureUtilities.RGBToInteger(fColor))));
			execute(cc, progressMonitor);
		}
	}

}
