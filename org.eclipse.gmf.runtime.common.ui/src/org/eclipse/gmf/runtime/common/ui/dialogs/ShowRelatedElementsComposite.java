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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIDebugOptions;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.common.ui.internal.dialogs.SelectableElementTreeSelectionChangedAndMouseAndKeyListener;
import org.eclipse.gmf.runtime.common.ui.internal.dialogs.SelectableElementsContentProvider;
import org.eclipse.gmf.runtime.common.ui.internal.dialogs.SelectableElementsTriStateLabelProvider;
import org.eclipse.gmf.runtime.common.ui.internal.l10n.CommonUIMessages;

/**
 * A composite intended to be used in the Show Related Elements dialog. It
 * contains the details of determining which elements to show.
 * 
 * @author wdiu, schafe
 */
public class ShowRelatedElementsComposite
	extends Composite {

	/**
	 * The viewer on the left side of the box
	 */
	private TreeViewer viewer;

	/**
	 * Expansion group on right side of the box
	 */
	private Composite expansionComposite;

	/**
	 * The selection to consumer radio
	 */
	private Button consumerToSelection;

	/**
	 * The selection to supplier radio
	 */
	private Button selectionToSupplier;

	/**
	 * The both radio, for both selectionToConsumer and selectionToSupplier
	 */
	private Button both;

	/**
	 * The all connected radio
	 */
	private Button allConnected;

	/**
	 * For now, not all SRE implementors are supporting all connected. Later on,
	 * this variable will be removed and we will always show "All connected"
	 * because it will be required to be supported.
	 */
	protected boolean showAllConnected = false;

	/**
	 * Number of levels to add related elements
	 */
	private Text levels;

	/**
	 * Expand indefinitely checkbox
	 */
	private Button expandIndefinitely;

	/**
	 * The root element
	 */
	private SelectableElement rootElement;

	/**
	 * Value of the expand indefinitely checkbox
	 */
	private boolean cachedExpandIndefinitely;

	/**
	 * Value of the number of levels to expand box
	 */
	private int cachedExpandLevels;

	/**
	 * The expansion type
	 */
	private ExpansionType cachedExpansionType = ExpansionType.INCOMING;

	/**
	 * Show Related Elements image 1 for the right side of the dialog
	 */
	private Image showRelatedElementsImage1;

	/**
	 * Show Related Elements image 2 for the right side of the dialog
	 */
	private Image showRelatedElementsImage2;

	/**
	 * Show Related Elements image 3 for the right side of the dialog
	 */
	private Image showRelatedElementsImage3;

	/**
	 * True if you need to see the expansion controls, false otherwise
	 */
	private boolean needsExpansionControls;

	/**
	 * Listener for details being changed
	 */
	protected IShowRelatedElementsWithDetails detailsChangedListener;

	/**
	 * Contributed the expansion group.
	 */
	protected Composite contributedExpansionGroupComposite = null;

	/**
	 * Default value of the levels box
	 */
	private static final int DEFAULT_LEVELS = 1;

	/**
	 * Viewer width that's passed in from the constructor We don't have to
	 * calculate it.
	 */
	protected int viewerWidth = -1;

	/**
	 * Constructor for ShowRelatedElementsComposite.
	 * 
	 * @param parent
	 *            parent Composite
	 * @param aRootElement
	 *            root SelectableElement
	 * @param preferredViewerWidth
	 *            the preferred viewer width hint
	 */
	public ShowRelatedElementsComposite(Composite parent,
			SelectableElement aRootElement, int preferredViewerWidth) {

		this(parent, aRootElement, true, preferredViewerWidth);
	}

	/**
	 * List of SelectableElement objects
	 */
	private List selectedSelectableElements;

	/**
	 * Constructor for ShowRelatedElementsComposite.
	 * 
	 * @param parent
	 *            parent Composite
	 * @param aRootElement
	 *            root SelectableElement
	 * @param createExpansion
	 *            true to create the expansion group, false not to create it
	 * @param preferredViewerWidth
	 *            the preferred viewer width hint
	 */
	public ShowRelatedElementsComposite(Composite parent,
			SelectableElement aRootElement, boolean createExpansion,
			int preferredViewerWidth) {

		super(parent, SWT.NONE);
		this.needsExpansionControls = createExpansion;
		this.rootElement = aRootElement.makeCopy();

		selectedSelectableElements = new ArrayList();
		SelectableElement.getAllChildrenOfType(this.rootElement,
			SelectedType.LEAVE, selectedSelectableElements);

		this.viewerWidth = preferredViewerWidth;
		createContents();

		// add listener for dispose of images
		addListener(SWT.Dispose, new Listener() {

			public void handleEvent(Event e) {
				onDispose();
			}
		});
	}

	/**
	 * Creates content for this composite.
	 */
	protected void createContents() {

		this.setLayoutData(new GridData(GridData.FILL_BOTH));
		this.setLayout(new GridLayout(2, false));

		createViewer();

		//only create the expansion group if specified
		//to do so
		if (this.needsExpansionControls) {
			createExpansionControls();
		}

		reset();
		viewer.refresh();
	}

	/**
	 * Return the viewerWidth calculated in createContents, which is called by
	 * the constructor.
	 * 
	 * @return int viewerWidth calculated in createContents, which is called by
	 *         the constructor.
	 */
	public int getViewerWidth() {
		return this.viewerWidth;
	}

	/**
	 * Reset button click handler, called to initialize the dialog
	 */
	protected void reset() {

		if (this.needsExpansionControls) {
			setBoth();
			setExpand(DEFAULT_LEVELS);
		}

		SelectableElement.setSelectedTypeForSelecteableElementAndChildren(
			rootElement, SelectedType.SELECTED);

		assert null != selectedSelectableElements;
		Iterator it = selectedSelectableElements.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			assert (obj instanceof SelectableElement);
			SelectableElement.setSelectedTypeForSelecteableElementAndChildren(
				(SelectableElement) obj, SelectedType.LEAVE);
		}

		viewer.expandToLevel(2);
		viewer.refresh();
	}

	/**
	 * Returns the tree viewer
	 * 
	 * @return CheckboxTreeViewer on the left side of the dialog
	 */
	public TreeViewer getTreeViewer() {
		return this.viewer;
	}

	/**
	 * Method getLevels.
	 * 
	 * @return Text Control for levels
	 */
	public Text getLevels() {
		return this.levels;
	}

	/**
	 * Gets cached value for expandIndefinitely.
	 * 
	 * @return boolean true if expand indefinitely was checked when cached,
	 *         false if it wasn't checked when cached.
	 */
	public boolean getExpandIndefinitely() {
		return this.cachedExpandIndefinitely;
	}

	/**
	 * Gets cached value for expandLevels.
	 * 
	 * @return int cached expand levels
	 */
	public int getExpandLevel() {
		return this.cachedExpandLevels;
	}

	/**
	 * Return the user's expansion type choice
	 * 
	 * @return ExpansionType the ExpansionType that was saved when cached
	 */
	public ExpansionType getExpansionType() {
		return cachedExpansionType;
	}

	/**
	 * Gets cached value for consumerToSelection. This will be deprecated when
	 * all SRE implementors implement the all connected option. Use
	 * getExpansionType() instead.
	 * 
	 * @return boolean true if at least one of the incoming or both checkboxes
	 *         were selected when cached, false if none were selected when
	 *         cached.
	 */
	public boolean getConsumerToSelection() {
		return cachedExpansionType.equals(ExpansionType.INCOMING)
			|| cachedExpansionType.equals(ExpansionType.BOTH);
	}

	/**
	 * Gets cached value for selectiontoSupplier. This will be deprecated when
	 * all SRE implementors implement the all connected option. Use
	 * getExpansionType() instead.
	 * 
	 * @return boolean true if at least one of the outgoing or both checkboxes
	 *         were selected when cached, false if none were selected when
	 *         cached.
	 */
	public boolean getSelectionToSupplier() {
		return cachedExpansionType.equals(ExpansionType.OUTGOING)
			|| cachedExpansionType.equals(ExpansionType.BOTH);
	}

	/**
	 * Gets cached value for all connected. This will be deprecated when all SRE
	 * implementors implement the all connected option. Use getExpansionType()
	 * instead.
	 * 
	 * @return boolean true if the all connected checkbox was selected when
	 *         cached, false if it was not selected when cached.
	 */
	public boolean getAllConnected() {
		return cachedExpansionType.equals(ExpansionType.ALL);
	}

	/**
	 * Returns a list of the selected relationship types.
	 * 
	 * @return List of relationship types that were from SelectableElement
	 *         objects under the root that had a SelectedType of SELECTED.
	 */
	public List getSelectedRelationshipTypes() {
		if (this.rootElement == null) {
			return null;
		}
		return this.rootElement.getSelectedElementTypes();
	}

	/**
	 * Sets the tree viewer
	 * 
	 * @param aViewer
	 *            is the CheckboxTreeViewer
	 */
	protected void setTreeViewer(TreeViewer aViewer) {
		this.viewer = aViewer;
	}

	/**
	 * Create the tree viewer in this composite. The tree viewer is used to
	 * display relationships.
	 */
	protected void createViewer() {
		Label label = new Label(this, SWT.NULL);
		label.setText(
			CommonUIMessages.ShowRelatedElementsDialog_RelationshipTypes);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		label.setLayoutData(gridData);

		setTreeViewer(new TreeViewer(this, SWT.SINGLE | SWT.V_SCROLL
			| SWT.BORDER));

		gridData = new GridData(GridData.FILL_BOTH);
		gridData.widthHint = this.viewerWidth;
		//gridData.heightHint = VIEWER_HEIGHT;
		gridData.horizontalSpan = 2;
		Tree tree = getTreeViewer().getTree();
		tree.setLayoutData(gridData);
		//tree.setLayout(new GridLayout(1, true));

		getTreeViewer().setUseHashlookup(true);

		getTreeViewer().setLabelProvider(
			new SelectableElementsTriStateLabelProvider());
		getTreeViewer().setContentProvider(
			new SelectableElementsContentProvider());

		getTreeViewer().setInput(this.rootElement);

		//I just set the input to the root, so I make these asserts
		TreeItem[] treeItems = tree.getItems();
		assert null != treeItems;
		assert (treeItems.length == 1);
		tree.setSelection(treeItems);

		SelectableElementTreeSelectionChangedAndMouseAndKeyListener listener = new SelectableElementTreeSelectionChangedAndMouseAndKeyListener(
			getTreeViewer()) {

			protected void switchCheckType(SelectableElement element) {
				detailsChanged();

				if (element.getSelectedType() == SelectedType.LEAVE) {
					element.setSelectedType(SelectedType.SELECTED);
				} else if (element.getSelectedType() == SelectedType.SELECTED) {
					element.setSelectedType(SelectedType.LEAVE);
				}
			}
		};

		getTreeViewer().addSelectionChangedListener(listener);
		tree.addMouseListener(listener);
		tree.addKeyListener(listener);
	}

	/**
	 * Contribute to the expansion group composite
	 * 
	 * @param parent
	 *            the parent expansion group composite that we are going to
	 *            contribute to.
	 */
	protected void contributeToExpansionGroupComposite(Composite parent) {
		//do not add anything
	}

	/**
	 * Create group on left side of composite, to select expansion criteria.
	 */
	protected void createExpansionControls() {
		this.expansionComposite = new Composite(this, SWT.NULL);

		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		this.expansionComposite.setLayoutData(gridData);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = gridLayout.marginWidth = 0;
		this.expansionComposite.setLayout(gridLayout);

		new Label(this.expansionComposite, SWT.NULL).setText(
			CommonUIMessages.ShowRelatedElementsDialog_ExpansionGroup_Text);

		consumerToSelection = new Button(this.expansionComposite, SWT.RADIO);
		consumerToSelection.setText(
			CommonUIMessages.ShowRelatedElementsDialog_Incoming); 
		consumerToSelection.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				detailsChanged();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		new Label(this.expansionComposite, SWT.NULL);

		selectionToSupplier = new Button(this.expansionComposite, SWT.RADIO);
		selectionToSupplier.setText(
			CommonUIMessages.ShowRelatedElementsDialog_Outgoing);
		selectionToSupplier.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				detailsChanged();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		if (showAllConnected) {
			new Label(this.expansionComposite, SWT.NULL);
		}

		//TODO temporarily hide the all connected radio button
		allConnected = new Button(this.expansionComposite, SWT.RADIO);
		allConnected.setText(
			CommonUIMessages.ShowRelatedElementsDialog_AllConnected);
		allConnected.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				detailsChanged();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		if (!showAllConnected) {
			allConnected.setVisible(false);
		} else {
			new Label(this.expansionComposite, SWT.NULL);
		}

		both = new Button(this.expansionComposite, SWT.RADIO);
		both
			.setText(
				CommonUIMessages.ShowRelatedElementsDialog_ExpansionGroup_RadioButton_Both);
		both.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				detailsChanged();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		//TODO all connected radio should be moved here when fully implemented

		//subclasses will override this method if they need to contribute
		contributeToExpansionGroupComposite(this.expansionComposite);

		new Label(expansionComposite, SWT.NULL)
			.setText(
				CommonUIMessages.ShowRelatedElementsDialog_ExpansionGroup_Label_Levels);

		levels = new Text(this.expansionComposite, SWT.BORDER);
		gridData = new GridData();
		//just pick a small, reasonable number, no need to externalize levels
		// size
		gridData.widthHint = 50;
		levels.setLayoutData(gridData);
		//don't use a modify listener
		//this will reasonably handle almost all of the cases
		levels.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				// do nothing
			}

			public void keyReleased(KeyEvent e) {
				detailsChanged();
			}
		});

		new Label(this.expansionComposite, SWT.NULL);

		expandIndefinitely = new Button(this.expansionComposite, SWT.CHECK);
		expandIndefinitely
			.setText(
				CommonUIMessages.ShowRelatedElementsDialog_ExpansionGroup_CheckBox_Expand_Indefinitely); 
		expandIndefinitely.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				widgetDefaultSelected(e);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				checkAndAskExpandIndefinitely();
				detailsChanged();
			}
		});

	}

	/**
	 * Performs validation. For now, only levels needs to be validated.
	 * 
	 * @param alert
	 *            is true to alert the user when it fails, false to not alert
	 *            the user
	 * @return String null if it validated, the explanation message String if it
	 *         did not validate
	 */
	public String validate(boolean alert) {
		if (!validateLevels()) {
			if (alert) {
				performValidateFailed();
			}
			return 
				CommonUIMessages.ShowRelatedElementsDialog_LevelsValidation_Message;
		}

		//subclasses may add more validation here by returning the
		//explanation String when the first validate is false
		return null;
	}

	/**
	 * Validates the levels text box for an integer.
	 * 
	 * @return true if it validated, false if it did not validate
	 */
	private boolean validateLevels() {
		if (levels.getText() == StringStatics.BLANK) {
			return false;
		} else {
			int result;
			try {
				result = Integer.parseInt(levels.getText());
				if (result < 0) {
					return false;
				}
				return true;
			} catch (NumberFormatException exception) {
				//not an error, do not log
				return false;
			}
		}
		//you should not be here
	}

	/**
	 * Call this when the validate for the levels text box fails. Displays a
	 * message saying that the validation failed. Sets focus to the levels
	 * control.
	 */
	private void performValidateFailed() {
		MessageBox validate = new MessageBox(getShell(), SWT.ICON_ERROR);
		validate.setText(
			CommonUIMessages.ShowRelatedElementsDialog_LevelsValidation_Title);
		validate
			.setMessage(
				CommonUIMessages.ShowRelatedElementsDialog_LevelsValidation_Message);
		validate.open();
		levels.setFocus();
	}

	/**
	 * Saves the user input, so that it is available after the widgets have been
	 * disposed.
	 */
	public void saveCachedValues() {

		//if no expansion group, no values to be
		//cached
		if (!this.needsExpansionControls) {
			return;
		}
		cachedExpandIndefinitely = expandIndefinitely.getSelection();

		try {
			cachedExpandLevels = Integer.parseInt(levels.getText());
		} catch (NumberFormatException e) {
			//this is expected when the user does not enter an integer
			Trace.catching(CommonUIPlugin.getDefault(),
				CommonUIDebugOptions.EXCEPTIONS_CATCHING,
				ShowRelatedElementsComposite.class, "saveCachedValues", e); //$NON-NLS-1$
			cachedExpandLevels = DEFAULT_LEVELS;
		}

		if (consumerToSelection.getSelection()) {
			cachedExpansionType = ExpansionType.INCOMING;
		} else if (selectionToSupplier.getSelection()) {
			cachedExpansionType = ExpansionType.OUTGOING;
		} else if (both.getSelection()) {
			cachedExpansionType = ExpansionType.BOTH;
		} else if (allConnected.getSelection()) {
			cachedExpansionType = ExpansionType.ALL;
		}

	}

	/**
	 * Asks the user if they really want to expand indefinitely. This method
	 * could clear the checkbox if it was already checked. If the expand
	 * indefinitely checkbox is not checked, then it does not do anything
	 */

	public void checkAndAskExpandIndefinitely() {
		if (expandIndefinitely.getSelection()) {
			MessageBox messageBox = new MessageBox(getShell(), SWT.YES | SWT.NO
				| SWT.ICON_QUESTION);
			messageBox
				.setText(
					CommonUIMessages.ShowRelatedElementsDialog_ExpandIndefinitelyMessageBox_Title);
			messageBox
				.setMessage(
					CommonUIMessages.ShowRelatedElementsDialog_ExpandIndefinitelyMessageBox_Message_Sentence1 
					+ "\n" + CommonUIMessages.ShowRelatedElementsDialog_ExpandIndefinitelyMessageBox_Message_Sentence2); //$NON-NLS-1$ 
			if (messageBox.open() == SWT.NO) {
				expandIndefinitely.setSelection(false);
			}
		}
		levels.setEnabled(!expandIndefinitely.getSelection());
	}

	/**
	 * Method called on composite dispose. Disposes of composite created images.
	 */
	private void onDispose() {
		disposeImages();
	}

	/**
	 * Disposes the images for the legend box.
	 */
	private void disposeImages() {
		if (showRelatedElementsImage1 != null
			&& !showRelatedElementsImage1.isDisposed())
			showRelatedElementsImage1.dispose();
		if (showRelatedElementsImage2 != null
			&& !showRelatedElementsImage2.isDisposed())
			showRelatedElementsImage2.dispose();
		if (showRelatedElementsImage3 != null
			&& !showRelatedElementsImage3.isDisposed())
			showRelatedElementsImage3.dispose();
	}

	/**
	 * Resets the root SelectableElement.
	 * 
	 * @param newRootElement
	 *            the new SelectableElement to be used for the viewer's input
	 */
	public void resetRootElement(SelectableElement newRootElement) {
		//set the rootElement to be new selectable element
		this.rootElement = newRootElement;
		viewer.setInput(newRootElement);
		reset();
	}

	/**
	 * Set the both radio button for expansion.
	 */
	public void setBoth() {
		both.setSelection(true);
		consumerToSelection.setSelection(false);
		selectionToSupplier.setSelection(false);
		allConnected.setSelection(false);
	}

	/**
	 * Set the consumer to selection radio button for expansion.
	 */
	public void setConsumerToSelection() {
		consumerToSelection.setSelection(true);
		both.setSelection(false);
		selectionToSupplier.setSelection(false);
		allConnected.setSelection(false);
	}

	/**
	 * Set the selection to supplier radio button for expansion.
	 */
	public void setSelectionToSupplier() {
		selectionToSupplier.setSelection(true);
		both.setSelection(false);
		consumerToSelection.setSelection(false);
		allConnected.setSelection(false);
	}

	/**
	 * Set the selection to supplier radio button for expansion.
	 */
	public void setAllConnected() {
		allConnected.setSelection(true);
		selectionToSupplier.setSelection(false);
		both.setSelection(false);
		consumerToSelection.setSelection(false);
	}

	/**
	 * Set the expand levels. Set to less than 0 for expand indefinitely.
	 * 
	 * @param expandLevels
	 *            less than 0 for expand indefinitely, max int is 99.
	 */
	public void setExpand(int expandLevels) {
		if (expandLevels >= 0) {
			assert (expandLevels <= 99);
			expandIndefinitely.setSelection(false);
			levels.setEnabled(true);
			levels.setText(Integer.toString(expandLevels));
		} else {
			expandIndefinitely.setSelection(true);
			levels.setEnabled(false);
		}
	}

	/**
	 * Method ExpandIndefinitely
	 * 
	 * @param enable
	 *            true to expand indefinitely and disable the levels text box
	 */
	public void ExpandIndefinitely(boolean enable) {
		expandIndefinitely.setSelection(enable);
		levels.setEnabled(!enable);
	}

	/**
	 * Return the root element for the viewer
	 * 
	 * @return the root SelectableElement for the viewer
	 */
	public SelectableElement getRootElement() {
		return rootElement;
	}

	/**
	 * Enables or disables the controls that let the user choose the expansion
	 * direction. These are the incoming, outgoing, and both controls.
	 * 
	 * @param enable
	 *            true to enable, false to disable
	 */
	public void enableDirectionControls(boolean enable) {
		consumerToSelection.setEnabled(enable);
		both.setEnabled(enable);
		selectionToSupplier.setEnabled(enable);
	}

	/**
	 * Enables or disables the controls that let the user choose the number of
	 * levels to expand. These are the expand indefinitely checkbox and the
	 * levels text control.
	 * 
	 * @param enable
	 *            true to enable, false to disable
	 */
	public void enableExpandLevels(boolean enable) {
		expandIndefinitely.setEnabled(enable);
		levels.setEnabled(enable);
	}

	/**
	 * Select the SelectableElement objects with hints in the list.
	 * 
	 * @param list
	 *            List of hints
	 * @param selectableElement
	 *            root SelectableElement which we call this method recursively
	 *            on its children
	 * 
	 * @return true if it is selected if this is a leaf. If it's not a leaf,
	 *         return true if all its children are selected. Return false if not
	 *         all selected or leave.
	 */
	private boolean select(List list, SelectableElement selectableElement) {

		if (selectableElement.getNumberOfChildren() == 0) {
			if (list.contains(selectableElement.getHint())) {
				selectableElement.setSelectedType(SelectedType.SELECTED);
				return true;
			} else {
				selectableElement.setSelectedType(SelectedType.LEAVE);
				return false;
			}
		} else {
			boolean childrenSelected = true;
			for (int i = 0; i < selectableElement.getNumberOfChildren(); i++) {
				if (!select(list, selectableElement.getChild(i))) {
					childrenSelected = false;
				}
			}

			if (childrenSelected) {
				selectableElement.setSelectedType(SelectedType.SELECTED);
				return true;
			} else {
				selectableElement.setSelectedType(SelectedType.LEAVE);
				return false;
			}
		}
	}

	/**
	 * One time initialize of the dialog's starting settings
	 * 
	 * @param cts
	 *            consumer to selection, true if we want to show consumer to
	 *            selection
	 * @param sts
	 *            selection to supplier, true if we want to show selection to
	 *            supplier
	 * @param expandLevel
	 *            int with levels to expand, -1 for indefinite or 0 to 99.
	 * @param selectedHints
	 *            List of SelectableElement hints
	 */
	public void initializeSettings(boolean cts, boolean sts, int expandLevel,
			List selectedHints) {
		if (cts && sts) {
			setBoth();
		} else if (cts) {
			setConsumerToSelection();
		} else if (sts) {
			setSelectionToSupplier();
		} else {
			//must have selected consumerToSelection or selectionToSupplier
			//or both
			assert (false);
		}

		setExpand(expandLevel);

		select(selectedHints, rootElement);

		viewer.refresh();

		expandIndefinitely.setFocus();
	}

	/**
	 * Update the relationships with the information contained in the preset
	 * 
	 * @param preset
	 *            ShowRelatedElementsPreset that contains the information that I
	 *            will be updating with.
	 */
	public void updateRelationships(ShowRelatedElementsPreset preset) {

		assert null != rootElement;

		if (preset == null) {
			reset();
			return;
		}

		SelectableElement.setSelectedTypeForSelecteableElementAndChildren(
			rootElement, SelectedType.LEAVE);

		//now go and select the ones that we need to select
		SelectableElement
			.setSelectedTypeForMatchingSelecteableElementAndChildren(
				rootElement, SelectedType.SELECTED, preset.getIds());

		if (preset.getExpansionType() == ExpansionType.INCOMING.getOrdinal()) {
			setConsumerToSelection();
		} else if (preset.getExpansionType() == ExpansionType.OUTGOING
			.getOrdinal()) {
			setSelectionToSupplier();
		} else if (preset.getExpansionType() == ExpansionType.BOTH.getOrdinal()) {
			setBoth();
		} else if (preset.getExpansionType() == ExpansionType.ALL.getOrdinal()) {
			setAllConnected();
		} else {
			//should be 1 of the 4 radio buttons
			assert (false);
		}

		if (preset.getLevels() == -1) {
			levels.setText("1"); //$NON-NLS-1$, just pick a reasonable number
			expandIndefinitely.setSelection(true);
			levels.setEnabled(false);
		} else {
			expandIndefinitely.setSelection(false);
			levels.setText(Integer.toString(preset.getLevels()));
			levels.setEnabled(true);
		}

		getTreeViewer().refresh();

	}

	/**
	 * Return the immediate selected IDs for presisting the relationships that
	 * the user selected.
	 * 
	 * @return Immediate selected IDs.
	 */
	public List getImmediateIds() {
		return getRootElement().getSelectedElementIds();
	}

	/**
	 * Return the custom data. Default implementation is to return null.
	 * Subclasses should override this to return the data that would correspond
	 * to data returned from the contributed composite.
	 * 
	 * @return custom data.
	 */
	protected Object getImmediateCustomData() {
		//subclasses may override
		return null;
	}

	/**
	 * Return the immediate expansion levels.
	 * 
	 * @return immediate expansion levels.
	 */
	protected int getImmediateExpansionLevels() {
		int numLevels = 0;

		if (expandIndefinitely.getSelection()) {
			numLevels = -1;
		} else {
			try {
				numLevels = Integer.parseInt(levels.getText().trim());
			} catch (NumberFormatException e) {
				// do nothing.
			}
		}

		return numLevels;
	}

	/**
	 * Return the immediate expansion type.
	 * 
	 * @return immedate expansion type. See the ExpansionType EnumeratedType for
	 *         more information.
	 */
	protected int getImmediateExpansionType() {
		if (consumerToSelection.getSelection())
			return ExpansionType.INCOMING.getOrdinal();
		if (selectionToSupplier.getSelection())
			return ExpansionType.OUTGOING.getOrdinal();
		if (both.getSelection())
			return ExpansionType.BOTH.getOrdinal();
		if (allConnected.getSelection())
			return ExpansionType.ALL.getOrdinal();
		return -1;
	}

	/**
	 * Return the unnamed current settings that make these details correspond to
	 * a preset.
	 * 
	 * @return the unnamed current settings for the preset.
	 */
	public ShowRelatedElementsPreset getCurrentSettings() {
		//it has no name yet
		return new ShowRelatedElementsPreset(null, false,
			getImmediateExpansionType(), getImmediateExpansionLevels(),
			getImmediateIds(), getImmediateCustomData());
	}

	/**
	 * Notify the listener that the details of this composite were changed.
	 */
	public void detailsChanged() {
		if (detailsChangedListener != null) {
			detailsChangedListener.detailsChanged();
		}
	}

	/**
	 * Set the details changed listener for listening to changes of details.
	 * 
	 * @param newDetailsChangedListener
	 *            IShowRelatedElementsWithDetails
	 */
	public void setDetailsChangedListener(
			IShowRelatedElementsWithDetails newDetailsChangedListener) {
		detailsChangedListener = newDetailsChangedListener;
	}

	//TODO rename getXX to getCachedXX where appropriate

}