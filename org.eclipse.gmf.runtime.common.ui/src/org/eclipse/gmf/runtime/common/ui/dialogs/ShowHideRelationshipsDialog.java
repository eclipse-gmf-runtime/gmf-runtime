/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.dialogs;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIDebugOptions;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIIconNames;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIStatusCodes;
import org.eclipse.gmf.runtime.common.ui.internal.dialogs.SelectableElementTreeSelectionChangedAndMouseAndKeyListener;
import org.eclipse.gmf.runtime.common.ui.internal.dialogs.SelectableElementsContentProvider;
import org.eclipse.gmf.runtime.common.ui.internal.dialogs.SelectableElementsTriStateLabelProvider;
import org.eclipse.gmf.runtime.common.ui.internal.l10n.CommonUIMessages;
import org.eclipse.gmf.runtime.common.ui.internal.l10n.ResourceManager;
import org.eclipse.gmf.runtime.common.ui.util.WindowUtil;

/**
 * Show Hide Relationships dialog box, called from an action.
 * 
 * When OK is clicked, returns Window.OK and result of the dialog is saved.
 * 
 * When Cancel is clicked, returns Window.Cancel and result of dialog is not
 * saved.
 * 
 * @author Wayne Diu, wdiu
 */
public class ShowHideRelationshipsDialog
	extends Dialog {

	/* Controls */

	/**
	 * The viewer on the left side of the box
	 */
	private TreeViewer viewer;

	/**
	 * OK button
	 */
	private Button ok;

	/**
	 * Cancel button
	 */
	private Button cancel;

	/* Data for the view control */

	/**
	 * The root element
	 */
	private SelectableElement rootElement;

	/**
	 * The context sensitive help id
	 */
	private String helpContextId;

	/* Statics */

	/**
	 * Tree viewer control's width for the hint
	 */
	private static int MAX_VIEWER_WIDTH = 400;

	/**
	 * Tree viewer control's height for the hint
	 */
	private static int MAX_VIEWER_HEIGHT = 400;

	/**
	 * Expansion group's width for the hint
	 */
	private static int TEXT_AREA_HEIGHT = 46;

	static {
		try {
			MAX_VIEWER_WIDTH = Integer.parseInt(
				CommonUIMessages.ShowHideRelationshipsDialog_MAX_VIEWER_WIDTH);
			MAX_VIEWER_HEIGHT = Integer.parseInt(
				CommonUIMessages.ShowHideRelationshipsDialog_MAX_VIEWER_HEIGHT);
			TEXT_AREA_HEIGHT = Integer.parseInt(
				CommonUIMessages.ShowHideRelationshipsDialog_TEXT_AREA_HEIGHT);
		} catch (NumberFormatException e) {
			/* already initialized with defaults */
			Trace.catching(CommonUIPlugin.getDefault(),
				CommonUIDebugOptions.EXCEPTIONS_CATCHING, CommonUIPlugin
					.getDefault().getClass(), "NumberFormatException", e); //$NON-NLS-1$
			Log
				.error(
					CommonUIPlugin.getDefault(),
					CommonUIStatusCodes.RESOURCE_FAILURE,
					"Failed to parse Show Hide Relationships Dialog's localized size", e); //$NON-NLS-1$
		}
	}

	/**
	 * The real viewer width
	 */
	private int viewerWidth = MAX_VIEWER_WIDTH;

	/* Legend icons */

	/**
	 * Selected icon image
	 */
	Image selectedIcon;

	/**
	 * Unselected icon image
	 */
	Image unselectedIcon;

	/**
	 * Cleared icon image
	 */
	Image clearedIcon;

	/**
	 * Constructor takes the parent shell and data to add into the viewer
	 * 
	 * @param parentShell
	 *            the parent Shell
	 * @param aRootElement
	 *            the root SelectableElement to add into the viewer
	 */
	public ShowHideRelationshipsDialog(Shell parentShell,
			SelectableElement aRootElement) {
		super(parentShell);
		this.rootElement = aRootElement.makeCopy();

		try {
			selectedIcon = ResourceManager.getInstance().createImage(
				CommonUIIconNames.IMG_CHECKBOX_SELECTED); //$NON-NLS-1$
			unselectedIcon = ResourceManager.getInstance().createImage(
				CommonUIIconNames.IMG_CHECKBOX_UNSELECTED); //$NON-NLS-1$
			clearedIcon = ResourceManager.getInstance().createImage(
				CommonUIIconNames.IMG_CHECKBOX_CLEARED); //$NON-NLS-1$
		} catch (Exception e) {
			Trace.catching(CommonUIPlugin.getDefault(),
				CommonUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
				"ShowHideRelationshipsDialog", e); //$NON-NLS-1$
			Log
				.error(
					CommonUIPlugin.getDefault(),
					CommonUIStatusCodes.RESOURCE_FAILURE,
					"Failed to get legend icons for Show Hide Relationships Dialog", e); //$NON-NLS-1$
			//if even one is bad, then I'm not going to display the legend
			disposeImages();
			selectedIcon = null;
			/*
			 * don't have to do the rest, I'm just checking selectedIcon
			 * unselectedIcon = null; clearedIcon = null;
			 */
		}

	}

	/**
	 * Returns the tree viewer
	 * 
	 * @return TreeViewer on the left side of the dialog
	 */
	protected TreeViewer getTreeViewer() {
		return viewer;
	}

	/**
	 * Sets the tree viewer
	 * 
	 * @param aViewer
	 *            is the TreeViewer on the left side of the dialog
	 */
	protected void setTreeViewer(TreeViewer aViewer) {
		this.viewer = aViewer;
	}

	/**
	 * Make the tree viewer on the left side of the dialog
	 * 
	 * @param parent
	 *            the parent Composite
	 */
	protected void createViewer(Composite parent) {
		setTreeViewer(new TreeViewer(parent, SWT.SINGLE | SWT.V_SCROLL
			| SWT.BORDER));

		GridData gridData = new GridData(GridData.FILL_BOTH);

		gridData.widthHint = viewerWidth;

		//calculate the tree viewer height
		int viewerHeight = SelectableElement
			.calculateNumberOfChildren(rootElement);
		GC gc = new GC(getTreeViewer().getTree());
		Point size = gc.textExtent(StringStatics.BLANK);
		//the buffer is 64
		viewerHeight = (viewerHeight * size.y) + 64;
		gc.dispose();
		if (viewerHeight > MAX_VIEWER_HEIGHT)
			viewerHeight = MAX_VIEWER_HEIGHT;
		gridData.heightHint = viewerHeight;

		Tree tree = getTreeViewer().getTree();
		tree.setLayoutData(gridData);
		tree.setLayout(new GridLayout(1, true));

		getTreeViewer().setUseHashlookup(true);

		SelectableElementTreeSelectionChangedAndMouseAndKeyListener listener = new SelectableElementTreeSelectionChangedAndMouseAndKeyListener(
			getTreeViewer()) {

			protected void switchCheckType(SelectableElement element) {
				if (element.getSelectedType() == SelectedType.LEAVE) {
					element.setSelectedType(SelectedType.UNSELECTED);
				} else if (element.getSelectedType() == SelectedType.UNSELECTED) {
					element.setSelectedType(SelectedType.SELECTED);
				} else if (element.getSelectedType() == SelectedType.SELECTED) {
					element.setSelectedType(SelectedType.LEAVE);
				}
			}
		};

		getTreeViewer().addSelectionChangedListener(listener);
		getTreeViewer().getTree().addMouseListener(listener);
		getTreeViewer().getTree().addKeyListener(listener);

	}

	/**
	 * Makes the buttons at the bottom of the dialog
	 * 
	 * @param parent
	 *            the parent Composite
	 */
	protected void makeButtons(Composite parent) {
		Composite empty = new Composite(parent, SWT.NULL);
		GridData gridData = new GridData();
		gridData.heightHint = 20;
		empty.setLayoutData(gridData);

		Composite right = new Composite(parent, SWT.NULL);
		right.setLayout(new GridLayout(3, false));
		right.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		ok = new Button(right, SWT.PUSH);
		ok.setText(
			CommonUIMessages.ShowHideRelationshipsDialog_Button_OK);
		ok.setLayoutData(WindowUtil.makeFixedButtonData(ok));
		ok.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				performOk();
			}
		});
		cancel = new Button(right, SWT.PUSH);
		cancel.setText(
			CommonUIMessages.ShowHideRelationshipsDialog_Button_Cancel);
		cancel.setLayoutData(WindowUtil.makeFixedButtonData(cancel));
		cancel.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				setReturnCode(Window.CANCEL);
				disposeImages();
				close();
			}
		});
	}

	/**
	 * Creates the line of text at the top of the dialog
	 * 
	 * @param parent
	 *            the parent Composite
	 */
	private void createLineOfTextAtTop(Composite parent) {
		Composite top = new Composite(parent, SWT.NULL);

		GridData gridData = new GridData(GridData.FILL_HORIZONTAL
			| GridData.FILL_VERTICAL);

		//Assume the fonts in the tree control and in the composite are the
		// same.
		//I calculate viewerWidth by doing
		//text length in pixels + beginning + buffer
		//text length in pixels + 32 + 64
		viewerWidth = SelectableElement.calculateLongestStringLength(
			rootElement, top) + 96;
		if (viewerWidth > MAX_VIEWER_WIDTH)
			viewerWidth = MAX_VIEWER_WIDTH;

		//the width will be smaller by a little bit, but that's ok
		gridData.widthHint = viewerWidth;
		gridData.heightHint = TEXT_AREA_HEIGHT;

		top.setLayoutData(gridData);
		top.setLayout(new GridLayout(1, false));

		Label label = new Label(top, SWT.WRAP);

		label.setText(
			CommonUIMessages.ShowHideRelationshipsDialog_Description);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
			| GridData.FILL_VERTICAL));
	}

	/**
	 * Makes a little blank box
	 * 
	 * @param parent
	 *            the parent Composite
	 */
	private void makeBlankBox(Composite parent) {
		Composite blankBox = new Composite(parent, SWT.NULL);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 10;
		blankBox.setLayoutData(gridData);
	}

	/**
	 * Creates the legend at the bottom of the dialog just above the buttons
	 * 
	 * @param parent
	 *            the parent Composite
	 */
	private void createLegend(Composite parent) {
		final int NUM_BOXES = 3;

		makeBlankBox(parent);

		Label label = new Label(parent, SWT.NULL);
		label.setText(
			CommonUIMessages.ShowHideRelationshipsDialog_Label_Legend);

		Composite legendBox = new Composite(parent, SWT.BORDER);

		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);

		legendBox.setLayoutData(gridData);
		legendBox.setLayout(new GridLayout(3, true));

		Composite[] elements = new Composite[NUM_BOXES];

		for (int i = 0; i < NUM_BOXES; i++) {
			elements[i] = new Composite(legendBox, SWT.NULL);

			gridData = new GridData(GridData.FILL_HORIZONTAL);

			elements[i].setLayoutData(gridData);
			elements[i].setLayout(new GridLayout(2, false));
		}

		//1
		label = new Label(elements[0], SWT.NULL);
		label.setImage(selectedIcon);

		label = new Label(elements[0], SWT.NULL);
		label.setText(
			CommonUIMessages.ShowHideRelationshipsDialog_Label_LegendShow);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
			| GridData.FILL_VERTICAL));

		//2
		label = new Label(elements[1], SWT.NULL);
		label.setImage(unselectedIcon);

		label = new Label(elements[1], SWT.NULL);
		label.setText(
			CommonUIMessages.ShowHideRelationshipsDialog_Label_LegendHide);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
			| GridData.FILL_VERTICAL));

		//3
		label = new Label(elements[2], SWT.NULL);
		label.setImage(clearedIcon);

		label = new Label(elements[2], SWT.NULL);
		label.setText(
			CommonUIMessages.ShowHideRelationshipsDialog_Label_LegendLeave);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
			| GridData.FILL_VERTICAL));

		makeBlankBox(parent);
	}

	/**
	 * Makes the dialog
	 * 
	 * @param parent
	 *            the parent Composite
	 * @return Control the parent Composite
	 */
	protected Control createContents(Composite parent) {

		//if you want a line of text at the top of the dialog
		createLineOfTextAtTop(parent);

		Composite bottom = new Composite(parent, SWT.NULL);

		bottom.setLayoutData(new GridData(GridData.FILL_BOTH));
		bottom.setLayout(new GridLayout(1, false));

		createViewer(bottom);

		/*
		 * don't have to do the rest, I'm just checking selectedIcon
		 * unselectedIcon != null && clearedIcon != null
		 */
		if (selectedIcon != null)
			createLegend(bottom);

		makeButtons(bottom);

		viewer.setLabelProvider(new SelectableElementsTriStateLabelProvider());
		viewer.setContentProvider(new SelectableElementsContentProvider());

		getTreeViewer().setInput(rootElement);

		Tree tree = getTreeViewer().getTree();

		//I just set the input to the root, so I make these asserts
		TreeItem[] treeItems = tree.getItems();
		assert null != treeItems : "treeItems cannot be null"; //$NON-NLS-1$
		assert treeItems.length == 1 : "treeItems cannot be empty"; //$NON-NLS-1$
		tree.setSelection(treeItems);

		ok.setFocus();

		resetDialog();

		viewer.refresh();

		getShell().setText(
			CommonUIMessages.ShowHideRelationshipsDialog_Title); 

		//set context sensitive help
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, helpContextId);
		
		return parent;

	}

	/**
	 * OK button click handler
	 */
	protected void performOk() {
		disposeImages();
		setReturnCode(Window.OK);
		close();
	}

	/**
	 * Reset button click handler, called to initialize the dialog
	 */
	void resetDialog() {
		viewer.expandAll();
	}

	/**
	 * Returns the root element of the RelatedElements that were in the viewer
	 * control at the left of the dialog.
	 * 
	 * @return SelectableElement with the root element.
	 */
	public SelectableElement getRootElement() {
		return rootElement;
	}

	/**
	 * Returns a list of the selected relationship types.
	 * 
	 * @return List of the selected relationship types
	 */
	public List getSelectedRelationshipTypes() {
		if (this.rootElement == null) {
			return null;
		}
		return this.rootElement.getSelectedElementTypes();
	}

	/**
	 * Returns a list of the unselected relationship types.
	 * 
	 * @return List of the unselected relationship types
	 */
	public List getUnselectedRelationshipTypes() {
		if (this.rootElement == null) {
			return null;
		}
		return this.rootElement.getUnSelectedElementTypes();
	}

	/**
	 * Initialize the context sensitive help id.
	 * 
	 * @param helpId
	 *            the help context id string
	 */
	public void initHelpContextId(String helpId) {
		this.helpContextId = helpId;
	}

	/**
	 * Disposes the images for the legend box
	 */
	public void disposeImages() {
		if (selectedIcon != null && !selectedIcon.isDisposed())
			selectedIcon.dispose();
		if (unselectedIcon != null && !unselectedIcon.isDisposed())
			unselectedIcon.dispose();
		if (clearedIcon != null && !clearedIcon.isDisposed())
			clearedIcon.dispose();
	}

}