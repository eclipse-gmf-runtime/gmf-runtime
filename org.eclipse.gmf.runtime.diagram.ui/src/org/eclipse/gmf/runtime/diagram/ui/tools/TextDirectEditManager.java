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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.contentassist.ContentAssistantHelper;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.TextCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.gef.ui.internal.parts.TextCellEditorEx;
import org.eclipse.gmf.runtime.gef.ui.internal.parts.WrapTextCellEditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.DeviceResourceException;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.ResourceManager;
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
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.CellEditorActionHandler;

import com.ibm.icu.util.StringTokenizer;

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
     * Cache the font descriptor when a font is created so that it can be
     * disposed later.
     */
    private List cachedFontDescriptors = new ArrayList();
    
    private IActionBars actionBars;
    private CellEditorActionHandler actionHandler;
    private IAction copy, cut, paste, undo, redo, find, selectAll, delete;
    
    private Font zoomLevelFont = null;
    
    private CellEditorLocator locator;
		
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
            WrapLabel fig = getWrapLabel();
            
            Rectangle rect = fig.getTextBounds().getCopy();
            fig.translateToAbsolute(rect);
            
            int avrWidth = FigureUtilities.getFontMetrics(text.getFont()).getAverageCharWidth();
            
            
            if (fig.isTextWrapped() && fig.getText().length() > 0)
                rect.setSize(new Dimension(rect.width, rect.height + FigureUtilities.getFontMetrics(text.getFont()).getDescent()));
            else
                rect.setSize(new Dimension(text.computeSize(SWT.DEFAULT, SWT.DEFAULT)).expand(avrWidth*2, 0));
            
            org.eclipse.swt.graphics.Rectangle newRect = text.computeTrim(rect.x, rect.y, rect.width, rect.height);
            
            Rectangle textBounds = new Rectangle(text.getBounds());
            if (!newRect.equals(textBounds)) {
                if (!(fig.getTextWrapAlignment() == PositionConstants.LEFT || fig.getTextAlignment() == PositionConstants.LEFT))
                    text.setBounds(newRect.x, newRect.y, newRect.width + avrWidth*3, newRect.height);
                else {
                    if (text.getBounds().x == 0 || Math.abs(text.getBounds().x - newRect.x) >= avrWidth)
                        text.setBounds(newRect.x, newRect.y, newRect.width + avrWidth*3, newRect.height);
                    else
                        text.setBounds(text.getBounds().x, newRect.y, newRect.width + avrWidth*3, newRect.height);
                }   
            }
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

			org.eclipse.swt.graphics.Rectangle newRect = text.computeTrim(rect.x, rect.y, rect.width, rect.height);
			if (!newRect.equals(new Rectangle(text.getBounds())))
				text.setBounds(newRect.x, newRect.y, newRect.width, newRect.height);
		}
	}

	/**
	 * constructor
	 * 
	 * @param source <code>GraphicalEditPart</code> to support direct edit of.  The figure of
	 * the <code>source</code> edit part must be of type <code>WrapLabel</code>.
	 */
	public TextDirectEditManager(ITextAwareEditPart source) {
		this(source, getTextCellEditorClass(source), getTextCellEditorLocator(source));
	}

	/**
	 * @param source
	 * @param editorType
	 * @param locator
	 */
	public TextDirectEditManager(GraphicalEditPart source, Class editorType, CellEditorLocator locator) {
		super(source, editorType, locator);
		this.locator = locator;
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

        try {
            FontDescriptor fontDescriptor = FontDescriptor.createFrom(data);
            cachedFontDescriptors.add(fontDescriptor);
            return getResourceManager().createFont(fontDescriptor);
        } catch (DeviceResourceException e) {
            Trace.catching(DiagramUIPlugin.getInstance(),
                DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
                "getScaledFont", e); //$NON-NLS-1$
            Log.error(DiagramUIPlugin.getInstance(),
                DiagramUIStatusCodes.IGNORED_EXCEPTION_WARNING, "getScaledFont", e); //$NON-NLS-1$
        }
        return JFaceResources.getDefaultFont();
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
		
		//Hook the cell editor's copy/paste actions to the actionBars so that they can
		// be invoked via keyboard shortcuts.
		actionBars = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor().getEditorSite().getActionBars();
		saveCurrentActions(actionBars);
		actionHandler = new CellEditorActionHandler(actionBars);
		actionHandler.addCellEditor(getCellEditor());
		actionBars.updateActionBars();
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
        
        for (Iterator iter = cachedFontDescriptors.iterator(); iter.hasNext();) {
            getResourceManager().destroyFont((FontDescriptor) iter.next());           
        }
        cachedFontDescriptors.clear();
        
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
     * This method obtains the fonts that are being used by the figure at its zoom level.
     * @param gep the associated <code>GraphicalEditPart</code> of the figure
     * @param actualFont font being used by the figure
     * @param display
     * @return <code>actualFont</code> if zoom level is 1.0 (or when there's an error),
     * new Font otherwise.
     */
	private Font getZoomLevelFont(Font actualFont, Display display) {
		Object zoom = getEditPart().getViewer().getProperty(ZoomManager.class.toString());
		
		if (zoom != null) {
			double zoomLevel = ((ZoomManager)zoom).getZoom();
			
			if (zoomLevel == 1.0f) 
				return actualFont;
			
			FontData[] fd = new FontData[actualFont.getFontData().length];
			FontData tempFD = null;
			
			for (int i=0; i < fd.length; i++) {
				tempFD = actualFont.getFontData()[i];
				
				fd[i] = new FontData(tempFD.getName(),(int)(zoomLevel * tempFD.getHeight()),tempFD.getStyle());
			}
			
            try {
                FontDescriptor fontDescriptor = FontDescriptor.createFrom(fd);
                cachedFontDescriptors.add(fontDescriptor);
                return getResourceManager().createFont(fontDescriptor);
            } catch (DeviceResourceException e) {
                Trace.catching(DiagramUIPlugin.getInstance(),
                    DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
                    "getZoomLevelFonts", e); //$NON-NLS-1$
                Log.error(DiagramUIPlugin.getInstance(),
                    DiagramUIStatusCodes.IGNORED_EXCEPTION_WARNING, "getZoomLevelFonts", e); //$NON-NLS-1$
                
                return actualFont;
            }
		}
		else
			return actualFont;
	}
	
	/**
	 * Gets the tex extent scaled to the mapping mode
	 */
	private Dimension getTextExtents(String s, Font font, IMapMode mm) {
		Dimension d = FigureUtilities.getTextExtents(s, font);
        // height should be set using the font height and the number of lines
        // in the string 
        int lineCount = getLineCount(s);
        d.height = FigureUtilities.getFontMetrics(font).getHeight()*lineCount;
        
     	return new Dimension(mm.DPtoLP(d.width), mm.DPtoLP(d.height));
	}

    private int getLineCount(String s) {
        StringTokenizer tokenizer = new StringTokenizer(s, "\n"); //$NON-NLS-1$
        return tokenizer.countTokens();
    }

    public void show() {
        super.show();

         if (!(getEditPart().getFigure() instanceof WrapLabel)) {
            return;
        }

        WrapLabel fig = (WrapLabel) getEditPart().getFigure();

        Control control = getCellEditor().getControl();
		this.zoomLevelFont = getZoomLevelFont(fig.getFont(), control.getDisplay());

        control.setFont(this.zoomLevelFont);

        //since the font's have been resized, we need to resize the  Text control...
        locator.relocate(getCellEditor());

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
        
        if (!(getEditPart().getFigure() instanceof WrapLabel)) {
          sendClickToCellEditor(location);
          return;        
        }
		
		WrapLabel fig = (WrapLabel) getEditPart().getFigure();
		Text textControl = (Text)getCellEditor().getControl();
		
		//we need to restore our wraplabel figure bounds after we are done
		Rectangle restoreRect = fig.getBounds().getCopy();
		
		Rectangle rect = fig.getBounds();
		fig.translateToAbsolute(rect);
        
        if (!rect.contains(new org.eclipse.draw2d.geometry.Point(location.x,location.y))) {
            textControl.setSelection(0, textControl.getText().length());
            fig.setBounds(restoreRect);
            return;
        }
		
		Rectangle iconBounds = fig.getIconBounds().getCopy();
		fig.translateToAbsolute(iconBounds);

		double avrLines =  fig.getBounds().height / (double)FigureUtilities.getFontMetrics(this.zoomLevelFont).getHeight();
		
		int xWidth = location.x - rect.x;
		
		if (fig.getIcon() != null && fig.getTextPlacement() == PositionConstants.EAST) 
			xWidth -= iconBounds.width;
		
		double yPercentage = (location.y - rect.y) / (double)rect.height;
		
		//calculate the line number the mouse clicked on.
		int lineNum = (int)Math.ceil(avrLines * yPercentage);
		
		//character count for caret positioning...
		int charCount = 0;
		
		StringTokenizer tokenizer = new StringTokenizer(fig.getSubStringText(), "\n"); //$NON-NLS-1$

		//calculate the total characters before linePos...
		for (int lineCount = 1; lineCount < lineNum; lineCount++) {
			if (tokenizer.hasMoreTokens()) {
				charCount += tokenizer.nextToken().length();
				
				//check if there is a user-inserted new line which will be accounted in the Text control...
				String newLineCheck = fig.getText().substring(charCount,charCount+1); 
				if (newLineCheck.equals("\r") || newLineCheck.equals("\n"))	//$NON-NLS-1$ //$NON-NLS-2$
					charCount++;
			}
			else {
				//our linePos calculation was wrong...revert to sending a mouse click...
				sendClickToCellEditor(location);
				fig.setBounds(restoreRect);
				return;
			}
		}
		
		//now count the last line's characters till the point where the mouse clicked...
		if (tokenizer.hasMoreTokens()) {
			String currentLineText = tokenizer.nextToken();
			
			IMapMode mm = MapModeUtil.getMapMode(fig);
			
			for (int i = 1; i <= currentLineText.length(); i++) {
				Dimension textExtent = getTextExtents(currentLineText.substring(0, i), this.zoomLevelFont, mm);
				fig.translateToAbsolute(textExtent);
				
				charCount++;
				
				if (textExtent.width >= xWidth)
					break;
			}
			
			textControl.setSelection(charCount);
			
			fig.setBounds(restoreRect);
			
		}
		else {
			//our linePos calculation was wrong...revert to sending a mouse click...
			sendClickToCellEditor(location);
			fig.setBounds(restoreRect);
		}
	}
	
	private void sendClickToCellEditor(final Point location) {
		//make sure the diagram doesn't receive the click event..
		getCellEditor().getControl().setCapture(true);
		
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
    
    /**
     * Gets the resource manager to remember the resources allocated for this
     * graphical viewer. All resources will be disposed when the graphical
     * viewer is closed if they have not already been disposed.
     * @return
     */
    protected ResourceManager getResourceManager() {
        return ((DiagramGraphicalViewer) getEditPart().getViewer())
            .getResourceManager();
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
    
    private void restoreSavedActions(IActionBars _actionBars){
    	_actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), copy);
    	_actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), paste);
    	_actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), delete);
    	_actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), selectAll);
    	_actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(), cut);
    	_actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(), find);
    	_actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undo);
    	_actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), redo);
    }

    public void setLocator(CellEditorLocator locator) {
        super.setLocator(locator);
        this.locator = locator;
    }

}