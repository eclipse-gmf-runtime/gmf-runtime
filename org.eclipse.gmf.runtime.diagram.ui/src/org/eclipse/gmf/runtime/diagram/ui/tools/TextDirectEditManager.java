/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 * 	  Dmitry Stadnik (Borland) - contribution for bugzilla 135694
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.tools;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.gmf.runtime.common.ui.contentassist.ContentAssistantHelper;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.TextCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.l10n.DiagramFontRegistry;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.gef.ui.internal.parts.TextCellEditorEx;
import org.eclipse.gmf.runtime.gef.ui.internal.parts.WrapTextCellEditor;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

/**
 * @author melaasar
 */
public class TextDirectEditManager
	extends DirectEditManager {
	
	/**
	 * content assist background color
	 */
	private Color proposalPopupBackgroundColor = null;

	/**
	 * content assist foreground color
	 */
	private Color proposalPopupForegroundColor = null;
	
	private boolean committed = false;
	
	/**
	 * flag used to avoid unhooking listeners twice if the UI thread is blocked
	 */
	private boolean listenersAttached = true;


	/** String buffer to hold initial characters **/
	private StringBuffer initialString = new StringBuffer();
		
	/**
	 * the text cell editor locator
	 * @author mmostafa
	 *
	 */
	static private class TextCellEditorLocator implements CellEditorLocator {

		private WrapLabel wrapLabel;
		
		public TextCellEditorLocator(WrapLabel wrapLabel) {
			super();
			this.wrapLabel = wrapLabel;
		}

		
		public WrapLabel getWrapLabel() {
			return wrapLabel;
		}

		public void relocate(CellEditor celleditor) {
			Text text = (Text) celleditor.getControl();
			Rectangle rect = getWrapLabel().getTextBounds().getCopy();
			getWrapLabel().translateToAbsolute(rect);
			
			if (getWrapLabel().isTextWrapped() && getWrapLabel().getText().length() > 0)
				rect.setSize(new Dimension(text.computeSize(rect.width, SWT.DEFAULT)));
			else {
				int avr = FigureUtilities.getFontMetrics(text.getFont()).getAverageCharWidth();
				rect.setSize(new Dimension(text.computeSize(SWT.DEFAULT, SWT.DEFAULT)).expand(avr*2, 0));
			}

			if (!rect.equals(new Rectangle(text.getBounds())))
				text.setBounds(rect.x, rect.y, rect.width, rect.height);
		}

	}

	private static class LabelCellEditorLocator implements CellEditorLocator {

		private Label label;

		public LabelCellEditorLocator(Label label) {
			this.label = label;
		}

		public Label getLabel() {
			return label;
		}

		public void relocate(CellEditor celleditor) {
			Text text = (Text) celleditor.getControl();
			Rectangle rect = getLabel().getTextBounds().getCopy();
			getLabel().translateToAbsolute(rect);

			int avr = FigureUtilities.getFontMetrics(text.getFont()).getAverageCharWidth();
			rect.setSize(new Dimension(text.computeSize(SWT.DEFAULT, SWT.DEFAULT)).expand(avr * 2, 0));

			if (!rect.equals(new Rectangle(text.getBounds())))
				text.setBounds(rect.x, rect.y, rect.width, rect.height);
		}
	}

	/**
	 * constructor
	 * 
	 * @param source <code>GraphicalEditPart</code> to support direct edit of.  The figure of
	 * the <code>source</code> edit part must be of type <code>WrapLabel</code>.
	 */
	public TextDirectEditManager(ITextAwareEditPart source) {
		super(source, getTextCellEditorClass(source), getTextCellEditorLocator(source));
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
     * @param source the <code>ITextAwareEditPart</code> to determine the cell editor for
     * @return the <code>CellEditorLocator</code> that is appropriate for the source <code>EditPart</code>
     */
    public static CellEditorLocator getTextCellEditorLocator(ITextAwareEditPart source){
               
        if (source instanceof TextCompartmentEditPart)
            return new TextCellEditorLocator(((TextCompartmentEditPart)source).getLabel());
        else {
            IFigure figure = source.getFigure();
            assert figure instanceof Label;
            return new LabelCellEditorLocator((Label)figure);
        }
    }
    
	/**
	 * @param source the <code>GraphicalEditPart</code> that is used to determine which
     * <code>CellEditor</code> class to use.
	 * @return the <code>Class</code> of the <code>CellEditor</code> to use for the text editing.
	 */
	public static Class getTextCellEditorClass(GraphicalEditPart source){
		IFigure figure = source.getFigure();
				
		if (figure instanceof WrapLabel && ((WrapLabel) figure).isTextWrapped())
			return WrapTextCellEditor.class;
		
		return TextCellEditorEx.class;
	}
	

	/**
	 * Given a <code>WrapLabel</code> object, this will calculate the 
	 * correct Font needed to display into screen coordinates, taking into 
	 * account the current mapmode.  This will typically be used by direct
	 * edit cell editors that need to display independent of the zoom or any
	 * coordinate mapping that is taking place on the drawing surface.
	 * 
	 * @param label the <code>WrapLabel</code> to use for the font calculation
	 * @return the <code>Font</code> that is scaled to the screen coordinates.  
	 * Note: the returned <code>Font</code> should not be disposed since it is
	 * cached by a common resource manager.
	 */
	protected Font getScaledFont(IFigure label) {
		Font scaledFont = label.getFont();
		FontData data = scaledFont.getFontData()[0];
		Dimension fontSize = new Dimension(0, MapModeUtil.getMapMode(label).DPtoLP(data.getHeight()));
		label.translateToAbsolute(fontSize);
		
		if( Math.abs( data.getHeight() - fontSize.height ) < 2 )
			fontSize.height = data.getHeight();

		data.setHeight(fontSize.height);
		Font newFont = DiagramFontRegistry.getInstance().getFont(null, data);
		return newFont;
	}
	
	protected void initCellEditor() {
		committed = false;

		// Get the Text Compartments Edit Part
		ITextAwareEditPart textEP = (ITextAwareEditPart) getEditPart();

		setEditText(textEP.getEditText());

		IFigure label = textEP.getFigure();
		Assert.isNotNull(label);
		Text text = (Text) getCellEditor().getControl();
		// scale the font accordingly to the zoom level
		text.setFont(getScaledFont(label));
		
		
		// register a validator on the cell editor
		getCellEditor().setValidator(textEP.getEditTextValidator());

		if (textEP.getParser() != null) {
			IContentAssistProcessor processor = textEP.getCompletionProcessor();
			if (processor != null) {
				// register content assist
				proposalPopupBackgroundColor = new Color(getCellEditor()
					.getControl().getShell().getDisplay(), new RGB(254, 241,
					233));
				proposalPopupForegroundColor = new Color(getCellEditor()
					.getControl().getShell().getDisplay(), new RGB(0, 0, 0));

				ContentAssistantHelper.createTextContentAssistant(text,
					proposalPopupForegroundColor, proposalPopupBackgroundColor,
					processor);
			}
		}
	}

	/**
	 * @see org.eclipse.gef.tools.DirectEditManager#commit()
	 */
	protected void commit() {
		Shell activeShell = Display.getCurrent().getActiveShell();
		if (activeShell != null
			&& getCellEditor().getControl().getShell().equals(
				activeShell.getParent())) {
			Control[] children = activeShell.getChildren();
			if (children.length == 1 && children[0] instanceof Table) {
				/*
				 * CONTENT ASSIST: focus is lost to the content assist pop up -
				 * stay in focus
				 */
				getCellEditor().getControl().setVisible(true);
				((TextCellEditorEx) getCellEditor()).setDeactivationLock(true);
				return;
			}
		}

		// content assist hacks
		if (committed) {
			bringDown();
			return;
		}
		committed = true;
		super.commit();
	}

	/**
	 * @see org.eclipse.gef.tools.DirectEditManager#bringDown()
	 */
	protected void bringDown() {
		if (proposalPopupForegroundColor != null) {
			proposalPopupForegroundColor.dispose();
			proposalPopupForegroundColor = null;
		}
		if (proposalPopupBackgroundColor != null) {
			proposalPopupBackgroundColor.dispose();
			proposalPopupBackgroundColor = null;
		}

		// myee - RATLC00523014: crashes when queued in asyncExec()
		eraseFeedback();
		
		initialString = new StringBuffer();
		
		Display.getCurrent().asyncExec(new Runnable() {

			public void run() {
				// Content Assist hack - allow proper cleanup on childen
				// controls
				TextDirectEditManager.super.bringDown();
			}
		});
	}

	/**
	 * This method is used to set the cell editors text
	 * 
	 * @param toEdit
	 *            String to be set in the cell editor
	 */
	public void setEditText(String toEdit) {

		// Get the cell editor
		CellEditor cellEditor = getCellEditor();

		// IF the cell editor doesn't exist yet...
		if (cellEditor == null) {
			// Do nothing
			return;
		}

		// Get the Text Compartment Edit Part
		ITextAwareEditPart textEP = (ITextAwareEditPart) getEditPart();

		// Get the Text control
		Text textControl = (Text) cellEditor.getControl();

		// Set the Figures text
		textEP.setLabelText(toEdit);
		
		
		// See RATLC00522324
		if (cellEditor instanceof TextCellEditorEx){
			((TextCellEditorEx)cellEditor).setValueAndProcessEditOccured(toEdit);
		} else {
			cellEditor.setValue(toEdit);
		}
		
		// Set the controls text and position the caret at the end of the text
		textControl.setSelection(toEdit.length());
	}

	/**
	 * Performs show and sets the edit string to be the initial character or string
	 * @param initialChar
	 */
	public void show(char initialChar) {
		initialString = initialString.append(initialChar);
		show();
		if (SWT.getPlatform() != "carbon") { //$NON-NLS-1$ 
			// Set the cell editor text to the initial character
			setEditText(initialString.toString());
		}

	}

	/**
	 * 
	 * Performs show and sends an extra mouse click to the point location so 
	 * that cursor appears at the mouse click point
	 * 
	 * The Text control does not allow for the cursor to appear at point location but
	 * at a character location
	 * 
	 * @param location
	 */
	public void show(Point location) {		
		show();
		// Send another click if the previous one is within the editor bounds 
		if (getCellEditor() != null && getCellEditor().getControl().getBounds().contains(location))
			sendMouseClick(location);	
	}

	
	/**
	 * 
	 * Sends a SWT MouseUp and MouseDown event to the point location 
	 * to the current Display
	 * 
	 * @param location
	 */
	private void sendMouseClick(final Point location) {		
		
		final Display currDisplay = Display.getCurrent();
		
		new Thread() {
			Event event;
			public void run() {
					event = new Event();
					event.type = SWT.MouseDown;
					event.button = 1;
					event.x = location.x;
					event.y = location.y;
					currDisplay.post(event);
					event.type = SWT.MouseUp;
					currDisplay.post(event);
			}
		}.start();
	}

	/* 
	 * Overrides super unhookListeners to set listeners attached flag
	 * This method prevents unhooking listeners twice if the UI thread is blocked.
	 * For example, a validation dialog may block the thread
	 */
	protected void unhookListeners() {
		if (listenersAttached) {
			listenersAttached = false;
			super.unhookListeners();
		}
	}

	/* 
	 * Sets the listeners attached flag if the cell editor exists
	 */
	protected void setCellEditor(CellEditor editor) {
		super.setCellEditor(editor);
		if (editor != null) {
			listenersAttached = true;
		}
	}

	public void showFeedback() {
		try {
			getEditPart().getRoot();
			super.showFeedback();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}