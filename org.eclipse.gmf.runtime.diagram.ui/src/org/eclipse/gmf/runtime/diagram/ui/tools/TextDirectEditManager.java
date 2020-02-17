/******************************************************************************
 * Copyright (c) 2002, 2010, 2012 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 *    Dmitry Stadnik (Borland) - contribution for bugzilla 135694
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.tools;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gmf.runtime.common.ui.contentassist.ContentAssistantHelper;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.diagram.ui.label.ILabelDelegate;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.runtime.gef.ui.internal.parts.TextCellEditorEx;
import org.eclipse.gmf.runtime.gef.ui.internal.parts.WrapTextCellEditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.CellEditorActionHandler;

/**
 * @author melaasar
 */
public class TextDirectEditManager extends DirectEditManagerBase {

	private IAction copy, cut, paste, undo, redo, find, selectAll, delete;

	private IActionBars actionBars;

	private CellEditorActionHandler actionHandler;

	/**
	 * constructor
	 * 
	 * @param source
	 *            <code>GraphicalEditPart</code> to support direct edit of. The
	 *            figure of the <code>source</code> edit part must be of type
	 *            <code>WrapLabel</code>.
	 */
	public TextDirectEditManager(ITextAwareEditPart source) {
		super(source);
	}

	/**
	 * @param source
	 * @param editorType
	 * @param locator
	 */
	public TextDirectEditManager(GraphicalEditPart source, Class editorType, CellEditorLocator locator) {
		super(source, editorType, locator);
	}

	/**
	 * @param source
	 *            the <code>ITextAwareEditPart</code> to determine the cell
	 *            editor for
	 * @return the <code>CellEditorLocator</code> that is appropriate for the
	 *         source <code>EditPart</code>
	 */
	public static CellEditorLocator getTextCellEditorLocator(final ITextAwareEditPart source) {
		return getCellEditorLocator(source);
	}

	/**
	 * @param source
	 *            the <code>GraphicalEditPart</code> that is used to determine
	 *            which <code>CellEditor</code> class to use.
	 * @return the <code>Class</code> of the <code>CellEditor</code> to use for
	 *         the text editing.
	 * @deprecated to override the cell editor class, use
	 *             {@link #createCellEditorOn(Composite)}, this provides the
	 *             flexibility necessary to initialize the cell editor with a
	 *             style.
	 */
	public static Class getTextCellEditorClass(GraphicalEditPart source) {
		IFigure figure = source.getFigure();

		if (figure instanceof WrapLabel && ((WrapLabel) figure).isTextWrapped())
			return WrapTextCellEditor.class;

		return TextCellEditorEx.class;
	}

	protected CellEditor doCreateCellEditorOn(Composite composite) {

		ILabelDelegate label = (ILabelDelegate) getEditPart().getAdapter(ILabelDelegate.class);
		if (label != null && label.isTextWrapOn()) {
			int style = SWT.WRAP | SWT.MULTI;

			switch (label.getTextJustification()) {
			case PositionConstants.LEFT:
				style = style | SWT.LEAD;
				break;
			case PositionConstants.RIGHT:
				style = style | SWT.TRAIL;
				break;
			case PositionConstants.CENTER:
				style = style | SWT.CENTER;
				break;
			default:
				break;
			}
			return new WrapTextCellEditor(composite, style);
		} else {
			return new TextCellEditorEx(composite);
		}
	}

	/**
	 * This method is used to set the cell editors text
	 * 
	 * @param toEdit
	 *            String to be set in the cell editor
	 */
	@Override
	public void setEditText(String toEdit) {
		super.setEditText(toEdit);

		// Get the cell editor
		CellEditor cellEditor = getCellEditor();

		// IF the cell editor doesn't exist yet...
		if (cellEditor == null) {
			// Do nothing
			return;
		}

		// Get the Text control
		Text textControl = (Text) cellEditor.getControl();

		// Set the controls text and position the caret at the end of the text
		textControl.setSelection(toEdit.length());
	}

	@Override
	protected void createContentAssistant(Control control, Color proposalPopupForegroundColor, Color proposalPopupBackgroundColor, IContentAssistProcessor processor) {
		ContentAssistantHelper.createTextContentAssistant((Text) control, proposalPopupForegroundColor, proposalPopupBackgroundColor, processor);
	}

	@Override
	protected void initCellEditor() {
		super.initCellEditor();

		//Hook the cell editor's copy/paste actions to the actionBars so that they can
		// be invoked via keyboard shortcuts.
		actionBars = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorSite().getActionBars();
		saveCurrentActions(actionBars);
		actionHandler = new CellEditorActionHandler(actionBars);
		actionHandler.addCellEditor(getCellEditor());
		actionBars.updateActionBars();

	}

	/**
	 * @see org.eclipse.gef.tools.DirectEditManager#bringDown()
	 */
	@Override
	protected void bringDown() {
		super.bringDown();

		if (actionHandler != null) {
			actionHandler.dispose();
			actionHandler = null;
		}
		if (actionBars != null) {
			restoreSavedActions(actionBars);
			actionBars.updateActionBars();
			actionBars = null;
		}
	}

	private void saveCurrentActions(IActionBars _actionBars) {
		copy = _actionBars.getGlobalActionHandler(ActionFactory.COPY.getId());
		paste = _actionBars.getGlobalActionHandler(ActionFactory.PASTE.getId());
		delete = _actionBars.getGlobalActionHandler(ActionFactory.DELETE.getId());
		selectAll = _actionBars.getGlobalActionHandler(ActionFactory.SELECT_ALL.getId());
		cut = _actionBars.getGlobalActionHandler(ActionFactory.CUT.getId());
		find = _actionBars.getGlobalActionHandler(ActionFactory.FIND.getId());
		undo = _actionBars.getGlobalActionHandler(ActionFactory.UNDO.getId());
		redo = _actionBars.getGlobalActionHandler(ActionFactory.REDO.getId());
	}

	private void restoreSavedActions(IActionBars _actionBars) {
		_actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), copy);
		_actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), paste);
		_actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), delete);
		_actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), selectAll);
		_actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(), cut);
		_actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(), find);
		_actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undo);
		_actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), redo);
	}

}