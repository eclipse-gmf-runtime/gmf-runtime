/******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.FontHelper;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.IUIConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbenchPage;

/**
 * Drop down combo box conbtribution item that changes font's size
 * 
 * @author Natalia Balaba
 * @canBeSeenBy %level1
 */
public class FontSizeContributionItem
	extends PropertyChangeContributionItem
	implements Listener {

	/**
	 * The current font name
	 */
	private Integer lastFontSize;


	/**
	 * Constructor for FontSizeContributionItem.
	 * @see org.eclipse.gmf.runtime.diagram.ui.ui.actions.ActionContribution#ActionContribution(IEditorPart, String)
	 */
	public FontSizeContributionItem(IWorkbenchPage workbenchPage) {
		super(workbenchPage, ActionIds.CUSTOM_FONT_SIZE,
			Properties.ID_FONTSIZE,
			DiagramUIActionsMessages.PropertyDescriptorFactory_FontSize);
		setLabel(DiagramUIActionsMessages.FontSizeContributionItem_tooltip);
	}

	/**
	 * Create a drop down combo box anb populate it with font sizes
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.ui.actions.ActionContribution#newControl(Composite)
	 */
	protected Control createControl(Composite parent) {
		Combo box = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
		box.setVisibleItemCount(IUIConstants.DEFAULT_DROP_DOWN_SIZE);
		box.setItems(FontHelper.getFontSizes());
		box.addListener(SWT.Selection, this);
		box.select(0);
		return box;
	}

	/**
	 * Assert a new proprty value to be set to the selected IView object(s) [whenever there is
	 * a mutiple selection this method will be called per valid selected item]
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.internal.PropertyChangeContributionItem#getNewPropertyValue()
	 */
	protected Object getNewPropertyValue() {
		return lastFontSize;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractContributionItem#refreshItem()
	 */
	protected void refreshItem() {
		Combo box = (Combo) getControl();
		Integer fontSize = (Integer) getOperationSetPropertyValue(getPropertyId(),false);

		if (fontSize != null) {
			int index = box.indexOf(Integer.toString(fontSize.intValue()));
			box.select(index);
		} else {
			box.clearSelection();
		}
		super.refreshItem();
	}

	/**
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		Combo box = (Combo) getControl();
		int selectedSize =
			Integer.parseInt(box.getItem(box.getSelectionIndex()));
		lastFontSize = new Integer(selectedSize);
		runWithEvent(event);
	}
    
    protected boolean isOperationHistoryListener() {
        return true;
    }

    @Override
    protected boolean digIntoGroups() {
        return true;
    }
}
