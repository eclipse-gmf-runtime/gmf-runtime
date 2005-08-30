/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Messages;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.FontHelper;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;

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
		super(workbenchPage, ActionIds.CUSTOM_FONT_SIZE, Properties.ID_FONTSIZE, Messages.getString("PropertyDescriptorFactory.FontSize")); //$NON-NLS-1$
		setLabel(Messages.getString("FontSizeContributionItem.tooltip")); //$NON-NLS-1$
	}

	/**
	 * Create a drop down combo box anb populate it with font sizes
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.ui.actions.ActionContribution#newControl(Composite)
	 */
	protected Control createControl(Composite parent) {
		Combo box = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
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
		Integer fontSize = (Integer) getOperationSetPropertyValue(getPropertyId());

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

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractContributionItem#isCommandStackListener()
	 */
	protected boolean isCommandStackListener() {
		return true;
	}

}
