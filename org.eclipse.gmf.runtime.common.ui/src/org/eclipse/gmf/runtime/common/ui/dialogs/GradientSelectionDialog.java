/******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.common.ui.dialogs;

import java.util.ArrayList;

import org.eclipse.gmf.runtime.common.ui.internal.l10n.CommonUIMessages;
import org.eclipse.gmf.runtime.common.ui.util.WindowUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * Dialog that allows selection of two gradient colors and gradient style, and
 * optionally background transparency.
 * 
 * @author lgrahek
 * @since 1.2
 * 
 */
public class GradientSelectionDialog extends Dialog {

	protected Button color1Button, color2Button;	
	protected Canvas canvas;	
	private Button verticalStyle, horizontalStyle;
	private Button okButton, cancelButton, clearButton;
	
	private RGB gradientColor1;
	private RGB gradientColor2;	
	private int gradientStyle;
	
	private int transparency;
	
	/**
	 * value to be returned by open(), set to SWT.OK if the ok button has been pressed
	 */
	int returnVal; 			  
							 		
	ArrayList<Resource> resources;	
	
	
	/**
	 * Initializes dialog with the gradient and transparency data.
	 * 
	 * @param parent
	 *            the parent shell
	 * @param style
	 *            shell style
	 * @param initColor1
	 *            initial gradient color 1
	 * @param initColor2
	 *            initial gradient color 2
	 * @param defaultGradientStyle
	 *            initial gradient style
	 * @param transparency
	 *            transparency used when drawing the sample. It is ignored if
	 *            not within [0, 100] range
	 */
	public GradientSelectionDialog(Shell parent, int style, 
			RGB initColor1, RGB initColor2, int defaultGradientStyle,
			int transparency) {
		super(parent, style);
		setGradientColor1(initColor1);
		setGradientColor2(initColor2);
		setGradientStyle(defaultGradientStyle);
		if (transparency < 0 || transparency > 100) {
			setTransparency(0);
		} else {
			setTransparency(transparency);
		}
		returnVal = SWT.CANCEL;
		resources = new ArrayList<Resource>();
		
	}
	
	/**
	 * Opens the dialog. Location is not given and the dialog will be placed at the center of the screen
	 */
	public int open() {
		return open(null);
	}
	
	/**
	 * Sets up the dialog and opens it at given location.
	 * 
	 * @param location
	 *            Initial location of the dialog
	 * @return SWT.OK if ok button is clicked, SWT.CANCEL if cancel button is
	 *         clicked
	 */
	public int open(Point location) {
		final Shell dialog = new Shell(getParent(), SWT.DIALOG_TRIM | getStyle());
		dialog.setText(CommonUIMessages.GradientSelection_Gradient);
				
		GridLayout gridLayout = new GridLayout();
	    gridLayout.numColumns = 1;
	    gridLayout.marginHeight = 10;
	    gridLayout.marginWidth = 10;
	    dialog.setLayout(gridLayout);
	    
	    createDialogControls(dialog);		
		dialog.setDefaultButton (okButton);
		dialog.pack();
			
		Rectangle bounds = dialog.getBounds();
		// if location is not given, place the dialog at the center of the screen
		if (location == null) {
			Rectangle rect = getParent().getMonitor().getBounds();
			dialog.setLocation(rect.x + (rect.width - bounds.width) / 2, rect.y + (rect.height - bounds.height) / 2);
		} else {
			dialog.setLocation(location);
		}
		dialog.setMinimumSize(bounds.width, bounds.height);
		
		dialog.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {
				for (int i = 0; i < resources.size(); i++) {
					Object obj = resources.get(i);
					if (obj != null && obj instanceof Resource) {
						((Resource) obj).dispose();
					}
				}
				dialog.dispose();
			}
		});	
				
		dialog.open ();
		
		Display display = getParent().getDisplay();
		while (!dialog.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		
		return returnVal;
	}
	
	/**
	 * Creates the controls of the dialog.
	 * 
	 * @param parent
	 *            Parent shell
	 */
	protected void createDialogControls(Shell parent) {
	    GridLayout layout = new GridLayout();
	    layout.numColumns = 3;	
	    layout.marginHeight = 0;
	    layout.marginWidth = 0;
	   	Composite topComposite = new Composite(parent, SWT.NONE);
	   	topComposite.setLayout(layout);
	   	
		createColorGroup(parent, topComposite);
		createStyleGroup(topComposite);
		createSample(parent, topComposite);
		createOkCancelClearButtons(parent, topComposite);
	}
	
	/**
	 * Creates controls in the color group
	 * 
	 * @param parent
	 *            Parent shell
	 * @param topComposite
	 *            Parent composite
	 */
	protected void createColorGroup(Shell parent, Composite topComposite) {
		final Display display = parent.getDisplay();

		Group colorGroup = new Group(topComposite, SWT.NONE);
		colorGroup.setText(CommonUIMessages.GradientSelection_Colors);
	    GridLayout layout = new GridLayout();
	    layout.numColumns = 2;
	    GridData gridData = new GridData();
	    gridData.verticalAlignment = SWT.FILL;
	    gridData.grabExcessVerticalSpace = true;
	    colorGroup.setLayout(layout);
	    colorGroup.setLayoutData(gridData);	
	    
	    Label label = new Label(colorGroup, SWT.NONE);
	    label.setText(CommonUIMessages.GradientSelection_Color1);
	    label = new Label(colorGroup, SWT.NONE);
	    label.setText(CommonUIMessages.GradientSelection_Color2);	    
	        
		// button for choosing gradientColor1
		color1Button = new Button(colorGroup, SWT.PUSH);

		// set the button image (current gradientColor1)
		setButtonImage(color1Button, gradientColor1, display);
		color1Button.addListener(SWT.Selection, new Listener() { 
			public void handleEvent(Event event) {
				RGB color = changeColor(color1Button, display, gradientColor1);
				if (color != null) {
					setGradientColor1(color);
				}
			}
		});
		
		// button for choosing gradientColor2
		color2Button = new Button(colorGroup, SWT.PUSH);

		// set the button image (current gradientColor1)
		setButtonImage(color2Button, gradientColor2, display);
		color2Button.addListener(SWT.Selection, new Listener() { 
			public void handleEvent(Event event) {
				RGB color = changeColor(color2Button, display, gradientColor2);
				if (color != null) {
					setGradientColor2(color);
				}
			}
		});
	}
	
	/**
	 * Creates controls in the gradient style group
	 * 
	 * @param topComposite
	 *            Parent composite
	 */
	protected void createStyleGroup(Composite topComposite) {	
		Group styleGroup = new Group(topComposite, SWT.NONE);
		styleGroup.setText(CommonUIMessages.GradientSelection_ShadingStyle);
	    GridLayout layout = new GridLayout();
	    layout.numColumns = 1;
	    GridData gridData = new GridData();
	    gridData.horizontalAlignment = SWT.FILL;
	    gridData.verticalAlignment = SWT.FILL;
	    gridData.grabExcessHorizontalSpace = true;
	    gridData.grabExcessVerticalSpace = true;
	    styleGroup.setLayout(layout);
	    styleGroup.setLayoutData(gridData);
	    
	    verticalStyle = new Button(styleGroup, SWT.RADIO);
	    verticalStyle.setText(CommonUIMessages.GradientSelection_Vertical);
	    verticalStyle.setSelection(gradientStyle == 0);
	    horizontalStyle = new Button(styleGroup, SWT.RADIO);
	    horizontalStyle.setText(CommonUIMessages.GradientSelection_Horizontal);
	    horizontalStyle.setSelection(gradientStyle == 1);
		
	    SelectionListener selectionListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (!((Button)event.widget).getSelection()) {
					// don't react on de-selection
					return;
				}
				changeStyle();
			}
		};
		verticalStyle.addSelectionListener(selectionListener);
		horizontalStyle.addSelectionListener(selectionListener);	
	}

	/**
	 * Creates controls in the sample group
	 * 
	 * @param parent
	 *            Parent shell
	 * @param topComposite
	 *            Parent composite
	 */
	protected void createSample(Shell parent, Composite topComposite) {		
		final Display display = parent.getDisplay();
		
		Composite sampleComposite = new Composite(topComposite, SWT.NONE);
		GridLayout layout = new GridLayout();
	    layout.numColumns = 1;
	    GridData gridData = new GridData();
	    gridData.verticalAlignment = SWT.FILL;
	    gridData.grabExcessVerticalSpace = true;	    
	    gridData.verticalAlignment = SWT.BOTTOM;
		sampleComposite.setLayout(layout);	
		sampleComposite.setLayoutData(gridData);
		
		// message
		Label message = new Label(sampleComposite, SWT.NONE); 
		message.setText(CommonUIMessages.GradientSelection_Sample);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		//gridData.horizontalSpan = 2;		
		message.setLayoutData(gridData);

		// canvas
		canvas = new Canvas(sampleComposite, SWT.NONE);
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.widthHint = 35;
		gridData.heightHint = 35;
		canvas.setLayoutData(gridData);
		canvas.addListener (SWT.Paint, new Listener () {
			public void handleEvent (Event e) {
				Point size = canvas.getSize();
				Color color1 = new Color(display, gradientColor1);
				Color color2 = new Color(display, gradientColor2);
				
				Image sample = new Image(display, size.x, size.y);
				GC gc = new GC(sample);
				Rectangle rect = sample.getBounds();
				gc.setForeground(color1);
				gc.setBackground(color2);
				gc.setAlpha(255-transparency*255/100);
				gc.fillGradientRectangle(rect.x, rect.y, rect.width, rect.height, gradientStyle==0);				
				gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
				gc.drawRectangle(rect.x, rect.y, rect.width-1, rect.height-1);

				if (sample != null) {
					e.gc.drawImage (sample, 0, 0);
				}				
				
				gc.dispose();				
				sample.dispose();
				color1.dispose();
				color2.dispose();
			}
		});	
	}
	    		
	/**
	 * Creates ok and cancel buttons
	 * 
	 * @param parent
	 *            Parent shell
	 * @param topComposite
	 *            Parent composite
	 */
	protected void createOkCancelClearButtons(Shell parent, Composite topComposite) {
		Composite okCancelComp = new Composite(topComposite, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		okCancelComp.setLayout(layout);
		GridData gridData = new GridData();
		gridData.verticalAlignment = SWT.BOTTOM;
		gridData.horizontalSpan = 3;
		okCancelComp.setLayout(layout);
		okCancelComp.setLayoutData(gridData);
		
		// ok button left of cancel button
		createOkButton(okCancelComp, parent);
		createCancelButton(okCancelComp, parent);
		createClearButton(okCancelComp, parent);
	}
	
	/**
	 * Creates cancel button as part of the given okCancelComp composite
	 * 
	 * @param okCancelComp
	 *            Parent composite
	 * @param parent
	 *            Parent shell
	 */
	protected void createCancelButton(Composite okCancelComp, final Shell parent) {
		// cancel button
		cancelButton = new Button (okCancelComp, SWT.PUSH);
		cancelButton.setText(CommonUIMessages.ShowHideRelationshipsDialog_Button_Cancel);
		cancelButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				parent.close();
			}
		});
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		cancelButton.setLayoutData(gridData);
	}
	
	/**
	 * Creates ok button as part of the given okCancelComp composite
	 * 
	 * @param okCancelComp
	 *            Parent composite
	 * @param parent
	 *            Parent shell
	 */
	protected void createOkButton(Composite okCancelComp, final Shell parent) {
		// OK button
		okButton = new Button (okCancelComp, SWT.PUSH);
		okButton.setText(CommonUIMessages.ShowHideRelationshipsDialog_Button_OK);
		okButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				returnVal = SWT.OK;
				parent.close();
			}
		});
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		okButton.setLayoutData(gridData);				
	}
	
	protected void createClearButton(Composite okCancelComp, final Shell parent) {
		// OK button
		clearButton = new Button (okCancelComp, SWT.PUSH);
		clearButton.setText(CommonUIMessages.ClearAction_label);
		clearButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				returnVal = -1;
				parent.close();
			}
		});
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		clearButton.setLayoutData(gridData);				
	}	
	
	/**
	 * Opens color chooser dialog, and if user selects a color applies it to the
	 * button and returns it.
	 * 
	 * @param button
	 *            Color button whose color will change
	 * @param display
	 * @param currentColor
	 *            Current color that given button represents
	 * @return Newly selected RGB color
	 */
	protected RGB changeColor(Button button, Display display, RGB currentColor) {
		ColorDialog dialog = new ColorDialog(Display.getCurrent().getActiveShell());

		dialog.setRGB(currentColor);
		WindowUtil.centerDialog(dialog.getParent(), Display.getCurrent()
				.getActiveShell());
		dialog.open();

		RGB selectedColor = dialog.getRGB();
		if (selectedColor != null) {
			setButtonImage(button, selectedColor, display);
			canvas.redraw();
		}
		return selectedColor;
	}
	
	/**
	 * Sets the image for a color button (square filled with the color that
	 * button represents)
	 */
	protected void setButtonImage(Button btn, RGB rgbColor, Display display) {
		// First, dispose the current image, if any
        Image image = btn.getImage();
        if (image != null) {
        	resources.remove(image);
        	image.dispose();
        }
		// Now set the new image based on rgbColor
		Color color1 = new Color(display, rgbColor);
		image = new Image(display, 16, 16);
		GC gc = new GC(image);
		gc.setBackground(color1);
		Rectangle rect = image.getBounds();
		gc.fillRectangle(rect);
		gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));		
		gc.drawRectangle(rect.x, rect.y, rect.width - 1, rect.height - 1);
		gc.dispose();		
		color1.dispose();
		btn.setImage(image);
		resources.add(image);		
	}
	
	/**
	 * @return the canvas used for drawing sample
	 */
	protected Canvas getSampleCanvas() {
		return canvas;
	}
	
	/**
	 * Changes gradienStyle and redraws canvas based on style selection.
	 */
	private void changeStyle() {
		if (verticalStyle.getSelection()) {
			gradientStyle = 0;
		} else {
			gradientStyle = 1;
		}
		canvas.redraw();
	}


	/**
	 * @return the gradientColor1
	 */
	public RGB getGradientColor1() {
		return gradientColor1;
	}


	/**
	 * @param gradientColor1 the gradientColor1 to set
	 */
	public void setGradientColor1(RGB gradientColor1) {
		this.gradientColor1 = gradientColor1;
	}


	/**
	 * @return the gradientColor2
	 */
	public RGB getGradientColor2() {
		return gradientColor2;
	}


	/**
	 * @param gradientColor2 the gradientColor2 to set
	 */
	public void setGradientColor2(RGB gradientColor2) {
		this.gradientColor2 = gradientColor2;
	}


	/**
	 * @return the gradientStyle
	 */
	public int getGradientStyle() {
		return gradientStyle;
	}


	/**
	 * @param gradientStyle the gradientStyle to set
	 */
	public void setGradientStyle(int gradientStyle) {
		this.gradientStyle = gradientStyle;
	}


	/**
	 * @return the transparency
	 */
	public int getTransparency() {
		return transparency;
	}


	/**
	 * @param transparency the transparency to set
	 */
	public void setTransparency(int transparency) {
		this.transparency = transparency;
	}

}
