/******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.dialogs.sortfilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ListCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.dialogs.sortfilter.SortFilterContentProvider;
import org.eclipse.gmf.runtime.diagram.ui.internal.dialogs.sortfilter.SortFilterDialog;
import org.eclipse.gmf.runtime.diagram.ui.internal.l10n.DiagramUIPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.requests.ChangeSortFilterRequest;
import org.eclipse.gmf.runtime.notation.Filtering;
import org.eclipse.gmf.runtime.notation.FilteringStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.Sorting;
import org.eclipse.gmf.runtime.notation.SortingDirection;
import org.eclipse.gmf.runtime.notation.SortingStyle;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * SortFilterPage extends <code>PropertyPage</code> by adding a Table and 
 * Filter controls for SortFilterPage.CHILD_PAGE types and only a Filter control
 * for SortFilterPage.ROOT_PAGE types.  
 * 
 * @author jcorchis
 */
public class SortFilterPage extends PropertyPage {

	/** Menu strings */
	static final private String MOVE_UP_TOOL_TIP = DiagramUIMessages.SortFilter_moveItemUp;
	static final private String MOVE_DOWN_TOOL_TIP = DiagramUIMessages.SortFilter_moveItemDown;

	/** filter list labels */
	static private final String FILTER_ITEMS_CONTAINING = DiagramUIMessages.SortFilter_filterItemsListLabel;	
	static private final String FILTER_ITEMS_LIST = DiagramUIMessages.SortFilter_fitlerListLabel;

	/** Tool tips and labels for the filter buttons */
	static private final String ADD_TO = DiagramUIMessages.SortFilter_addTo;
	private final String ADD_TO_LABEL = "<"; //$NON-NLS-1$
	static private final String REMOVE_FROM = DiagramUIMessages.SortFilter_removeFrom;
	private final String REMOVE_FROM_LABEL = ">"; //$NON-NLS-1$	
	static private final String ADD_ALL = DiagramUIMessages.SortFilter_addAll;
	private final String ADD_ALL_LABEL = "<<"; //$NON-NLS-1$
	static private final String REMOVE_ALL = DiagramUIMessages.SortFilter_removeAll;
	private final String REMOVE_ALL_LABEL = ">>"; //$NON-NLS-1$	

	/** the collection's elements (rows) */
	private List elementCollection = null;
	private List baseElements = null;

	/** table viewer */
	private TableViewer tableViewer = null;

	/** ToolItem widgets */
	private ToolItem moveUpToolItem = null;
	private ToolItem moveDownToolItem = null;

	/** List item widgets */
	private org.eclipse.swt.widgets.List filterList = null;
	private org.eclipse.swt.widgets.List filters = null;
	private Button addTo = null;
	private Button removeFrom = null;
	private Button addAllTo = null;
	private Button removeAllFrom = null;

	/** Filter button IDs */
	private final int ADD_TO_ID = 0;
	private final int REMOVE_FROM_ID = 1;
	private final int ADD_ALL_TO_ID = 2;
	private final int REMOVE_ALL_FROM_ID = 3;

	/** Height (in list items) for the filter items lists */
	private int LIST_HEIGHT = 8;

	/** The collection Column list for this page */
	private List collectionColumns = null;
	private SortFilterLabelProvider labelProvider = null;
	private Map filterMap = null;
	private String[] filterStrings = null;
	private String filterAppliesTo = null;
	
	private Sorting _sorting = Sorting.NONE_LITERAL;
	private List _sortedObjects = Collections.EMPTY_LIST;	
	private Map _sortingKeys = Collections.EMPTY_MAP;
	private String _sortColumn;
	private SortingDirection _sortingDirection = SortingDirection.ASCENDING_LITERAL;

	private Filtering _filtering = Filtering.NONE_LITERAL;
	private List _filteredObjects = Collections.EMPTY_LIST;
	private List _filteringKeys = Collections.EMPTY_LIST;
	
	private List elementCollectionBackUp = Collections.EMPTY_LIST;
	private Sorting _sortingBackUp = _sorting;
	private Filtering _filteringBackUp = _filtering;
	
	

	/**
	 * List of items that are shown as an alternate view. This is a list of 
	 * EE that are shown as an alternate view. 
	 * (e.g. an attribute shown as an association).  These are not to appear 
	 * in the dialog at all so the user cannot filter/unfilter them.
	 */
	private List _shownAsAlternateViewItems = Collections.EMPTY_LIST;

	private GraphicalEditPart editPart = null;

	/**
	 * the root page
	 */
	public static final String ROOT_PAGE = "root_page"; //$NON-NLS-1$
	/**
	 * the child page
	 */
	public static final String CHILD_PAGE = "child_page"; //$NON-NLS-1$

	private String pageType;

	/**
	 * Constructor for a Sort/Filter page.
	 * @param pageType either ROOT_PAGE or CHILD_PAGE
	 * @param editPart an instance of <code>ListCompartmentEditPart</code>. Null for ROOT_PAGE types.
	 * @param collectionColumns a list of <code>SortFilterColumns</code> that define 
	 * the Sort/Filter table. Null for ROOT_PAGE types
	 * @param labelProvider <code>SorFilterLabelProvider</code> which provides the data for the table
	 */
	public SortFilterPage(
		String pageType,
		GraphicalEditPart editPart,
		List collectionColumns,
		SortFilterLabelProvider labelProvider) {

		this.pageType = pageType;
		this.editPart = editPart;

		if (pageType.equals(CHILD_PAGE)) {
			Assert.isTrue(editPart instanceof ListCompartmentEditPart);
			Assert.isTrue(labelProvider != null);
			// Loads the current state for the sorting/filtering
			tokenizeSortProperty();
			tokenizeFilterProperty();
		}

		this.collectionColumns = collectionColumns;
		this.labelProvider = labelProvider;
	}

	/**
	 * Adds the filter list and the table for the Table's input.
	 * @param ancestor the parent <code>Composite</code>
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite ancestor) {

		if (pageType == ROOT_PAGE)
			noDefaultAndApplyButton();

		// setup layout
		Composite parent = new Composite(ancestor, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 2;
		parent.setLayout(layout);
		// setup child widgets
		if (filterStrings != null && filterStrings.length != 0)
			createFilterLists(parent);
		if (this.pageType == CHILD_PAGE) {
			createTable(parent);
			createToolBar(parent);

			// set focus and selection
			if (elementCollection != null && !elementCollection.isEmpty()) {
				// set the selection to the first row
				tableViewer.setSelection(
					new StructuredSelection(elementCollection.get(0)));
			}

			if (_filtering == Filtering.AUTOMATIC_LITERAL)
				refreshList();
			
			tableViewer.getTable().setFocus();
			handleSelection();
		}
		if (ROOT_PAGE.equals(pageType)) {
			PlatformUI.getWorkbench().getHelpSystem().setHelp(ancestor,
					"org.eclipse.gmf.runtime.diagram.ui.egmf0400"); //$NON-NLS-1$
		} else {
			PlatformUI.getWorkbench().getHelpSystem().setHelp(ancestor,
					"org.eclipse.gmf.runtime.diagram.ui.egmf0500"); //$NON-NLS-1$			
		}
		return parent;
	}

	/**
	 * Creates the filtering list widgets
	 * @param ancestor the ancestor
	 */
	private void createFilterLists(Composite ancestor) {
		// Do not show the filter lists if not filter criteria 
		// is defined.
		if (filterStrings.length == 0)
			return;
		//setup layout 
		Composite parent = new Composite(ancestor, SWT.NULL);
		Composite dummy = new Composite(ancestor, SWT.NULL);
		GridData dgd = new GridData();
		dgd.widthHint = 0;
		dummy.setLayoutData(dgd);

		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 3;
		parent.setLayout(layout);

		// Create the possible filter items list
		Label filterItemsLabel = new Label(parent, SWT.LEFT);
		filterItemsLabel.setText(FILTER_ITEMS_CONTAINING);
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
		filterItemsLabel.setLayoutData(gd);

		// Create the possible filter items list
		//Label emptyLabel = 
		new Label(parent, SWT.LEFT);

		// Create the possible filter items list		
		Label filterItemLabel = new Label(parent, SWT.LEFT);
		filterItemLabel.setText(FILTER_ITEMS_LIST);
		GridData gd2 = new GridData();
		gd2.horizontalAlignment = GridData.BEGINNING;
		filterItemLabel.setLayoutData(gd2);

		// Create the possible filter items list
		filters =
			new org.eclipse.swt.widgets.List(
				parent,
				SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		GridData gridData = new GridData(GridData.VERTICAL_ALIGN_FILL);
		gridData.verticalSpan = 1;
		gridData.widthHint = 80;
		int listHeight = filters.getItemHeight() * LIST_HEIGHT;
		Rectangle trim = filters.computeTrim(0, 0, 0, listHeight);
		gridData.heightHint = trim.height;
		filters.setLayoutData(gridData);

		// Create a new composite for the buttons and add
		// stack them vertically	  				  	
		Composite buttonComposite = new Composite(parent, SWT.NULL);
		GridLayout buttonLayout = new GridLayout();
		buttonLayout.marginHeight = 0;
		buttonLayout.marginWidth = 0;
		buttonLayout.numColumns = 1;
		buttonComposite.setLayout(buttonLayout);

		GridData buttGD =
			new GridData(GridData.FILL_VERTICAL | GridData.CENTER);
		buttGD.horizontalSpan = 1;
		buttGD.widthHint = 30;
		buttonComposite.setLayoutData(buttGD);

		removeFrom = new Button(buttonComposite, SWT.PUSH);
		removeFrom.setText(REMOVE_FROM_LABEL);
		removeFrom.setToolTipText(REMOVE_FROM);
		removeFrom.setLayoutData(makeArrowButtonGridData(removeFrom));
		removeFrom.setData(Integer.valueOf(REMOVE_FROM_ID));
		removeFrom.addSelectionListener(buttonSelectionAdapter);
		removeFrom.setEnabled(false);

		addTo = new Button(buttonComposite, SWT.PUSH);
		addTo.setText(ADD_TO_LABEL);
		addTo.setToolTipText(ADD_TO);
		addTo.setLayoutData(makeArrowButtonGridData(addTo));
		addTo.setData(Integer.valueOf(ADD_TO_ID));
		addTo.addSelectionListener(buttonSelectionAdapter);
		addTo.setEnabled(false);

		removeAllFrom = new Button(buttonComposite, SWT.PUSH);
		removeAllFrom.setText(REMOVE_ALL_LABEL);
		removeAllFrom.setToolTipText(REMOVE_ALL);
		removeAllFrom.setLayoutData(makeArrowButtonGridData(removeAllFrom));
		removeAllFrom.setData(Integer.valueOf(REMOVE_ALL_FROM_ID));
		removeAllFrom.addSelectionListener(buttonSelectionAdapter);
		addAllTo = new Button(buttonComposite, SWT.PUSH);

		addAllTo.setText(ADD_ALL_LABEL);
		addAllTo.setToolTipText(ADD_ALL);
		addAllTo.setLayoutData(makeArrowButtonGridData(addAllTo));
		addAllTo.setData(Integer.valueOf(ADD_ALL_TO_ID));
		addAllTo.addSelectionListener(buttonSelectionAdapter);

		// Add the possible list of filter items
		this.filterList =
			new org.eclipse.swt.widgets.List(
				parent,
				SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		GridData gridData2 = new GridData(GridData.VERTICAL_ALIGN_FILL);
		gridData2.verticalSpan = 1;
		gridData2.widthHint = 80;
		int listHeight2 = filterList.getItemHeight() * LIST_HEIGHT;
		Rectangle trim2 = filterList.computeTrim(0, 0, 0, listHeight2);
		gridData.heightHint = trim2.height;
		filterList.setLayoutData(gridData2);

		filters.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				removeFrom.setEnabled(filters.getSelectionCount() > 0);
			}
		});

		filterList.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				addTo.setEnabled(filterList.getSelectionCount() > 0);
			}
		});

		populateFilterLists();
	}

	/**
	 * SelectionAdapter for the filtering buttons. Calls <code>buttonPressed()</code>
	 * to handle the action.
	 * 
	 * @author jcorchis
	 */
	class ButtonSelectionAdapter extends SelectionAdapter {
		public void widgetSelected(SelectionEvent event) {
			buttonPressed(((Integer) event.widget.getData()).intValue());
		}
	}

	/** Instance of the ButtonSelectionAdapter used for all filtering buttons. */
	private ButtonSelectionAdapter buttonSelectionAdapter =
		new ButtonSelectionAdapter();

	/**
	 * Creates GridData for the moveup and movedown toolbar buttons.
	 * @param control button
	 * @return the <code>GridData</code>
	 */
	protected GridData makeArrowButtonGridData(Control control) {
		GC gc = new GC(control);
		gc.setFont(control.getFont());
		//fill horizontal to make them all the same size
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 24;
		gc.dispose();
		return gridData;
	}

	/**
	 * Handles the button pressed event on the filter criteria.  Move the items
	 * between the lists based on the selection and but button pressed.
	 */
	private void buttonPressed(int buttonId) {
		String[] items = {
		};
		switch (buttonId) {
			case ADD_TO_ID :
				items = filterList.getSelection();
				for (int i = 0; i < items.length; i++) {
					filters.add(items[i]);
					filterList.remove(items[i]);
				}
				addTo.setEnabled(false);
				break;
			case REMOVE_FROM_ID :
				items = filters.getSelection();
				for (int i = 0; i < items.length; i++) {
					filterList.add(items[i]);
					filters.remove(items[i]);
				}
				removeFrom.setEnabled(false);
				break;
			case ADD_ALL_TO_ID :
				items = filterList.getItems();
				for (int i = 0; i < items.length; i++) {
					filters.add(items[i]);
					filterList.remove(items[i]);
				}
				break;
			case REMOVE_ALL_FROM_ID :
				items = filters.getItems();
				for (int i = 0; i < items.length; i++) {
					filters.remove(items[i]);
					filterList.add(items[i]);
				}
				break;
		}
		updateFilteringKeysFromControls();
		if (pageType == CHILD_PAGE) {
			refreshList();			
		} else if (pageType == ROOT_PAGE) {
			PreferenceManager preferenceManager =
				((SortFilterDialog) getContainer()).getPreferenceManager();
			Iterator nodes =
				preferenceManager
					.getElements(PreferenceManager.PRE_ORDER)
					.iterator();
            while (nodes.hasNext()) {
				PreferenceNode node = (PreferenceNode) nodes.next();
				SortFilterPage page = (SortFilterPage) node.getPage();
				if (page == this) {
					continue;
				}

				if (Arrays.equals(filterStrings, page.getFilterList())) {
					page._filteringKeys = new ArrayList(_filteringKeys.size());
					page._filteringKeys.addAll(_filteringKeys);
					page._filtering = _filtering;
					page.populateFilterLists();
					page.refreshList();
					page.updateApplyButton();
				}				
				
            }
			
		}
		updateApplyButton();
	}

	/**
	 * Helper method which returns an <code>String[]</code> of column names
	 * which some of the TableViewer class expects.
	 * @return the String[] of column names
	 */
	private String[] getColumnProperties() {
		String[] columnProperties =
			new String[tableViewer.getColumnProperties().length];
		Object[] columnNames = tableViewer.getColumnProperties();
		for (int i = 0; i < columnNames.length; i++) {
			columnProperties[i] = (String) columnNames[i];
		}
		return columnProperties;
	}

	private void updateFilteringKeysFromControls() {
		String[] theFilterStrings = filters.getItems();
		
		if (theFilterStrings.length == 0) {
			_filtering = Filtering.NONE_LITERAL;
			_filteringKeys = Collections.EMPTY_LIST;
		} else {
			_filtering = Filtering.AUTOMATIC_LITERAL;
			if (_filteringKeys != null) {				
				_filteringKeys = new ArrayList();
			}
			for (int i = 0; i < theFilterStrings.length; i++) {
				_filteringKeys.add(theFilterStrings[i]);
			}
		}
	}
	
	/**
	 * Updates the table items to reflect the change in the filtering lists.
	 */
	void refreshList() {
		int filterColumn = findColumnIndexFromProperty(filterAppliesTo);
		if (filterColumn == -1)
			return;
		// Set the matching items in this list as not visible
		for (int j = 0; j < elementCollection.size(); j++) {
			String cell = labelProvider.getColumnText(elementCollection.get(j),
					filterColumn);
			((SortFilterElement) elementCollection.get(j))
					.setVisible(!_filteringKeys.contains(cell));
		}
		if (tableViewer != null)
			tableViewer.refresh();
	}

	/**
	 * Creates table viewer and builds table contents
	 * @param parent the parent composite
	 */
	private void createTable(Composite parent) {
		tableViewer =
			new TableViewer(
				parent,
				SWT.MULTI
					| SWT.FULL_SELECTION
					| SWT.H_SCROLL
					| SWT.V_SCROLL
					| SWT.BORDER);
		// setup table widget
		tableViewer.setUseHashlookup(true);
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		data.horizontalSpan = 1;
		data.widthHint = convertWidthInCharsToPixels(30);
		table.setLayoutData(data);
		table.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				handleSelection();
			}
		});

		// Add the columns to the table
		final String[] columnNames = new String[collectionColumns.size()];
		int i = 0;
		Iterator iter = collectionColumns.iterator();
		while (iter.hasNext()) {
			SortFilterCollectionColumn columnInfo =
				(SortFilterCollectionColumn) iter.next();
			TableColumn column =
				new TableColumn(table, columnInfo.getAlignment());
			column.setText(columnInfo.getCaption());
			column.setResizable(columnInfo.isResizable());
			column.setWidth(columnInfo.getWidth());
			column.setAlignment(columnInfo.getAlignment());
			if (columnInfo.getColumnSorter() != null) {
				column.addSelectionListener(
					new HeaderSelectionListener(
						(SortFilterViewerSorter) columnInfo.getColumnSorter(),
						tableViewer));
				// Intialize the table sorter
				if (_sortColumn != null && column.getText().equals(_sortColumn)) {
					SortFilterViewerSorter sorter =
						(SortFilterViewerSorter) columnInfo.getColumnSorter();
					if (sorter != null) {
						if (SortingDirection.ASCENDING_LITERAL
							.equals(_sortingDirection)) {
							// Use the ascending image
							Image image = DiagramUIPluginImages.DESC_SORT_ARROW_UP.createImage();
							column.setImage(image);
							column.pack();
						} else if (SortingDirection.DESCENDING_LITERAL
							.equals(_sortingDirection)) {
							sorter.toggleSortingDirection();
							// Use the descending image
							Image image = DiagramUIPluginImages.DESC_SORT_ARROW_DN.createImage();
							column.setImage(image);
							column.pack();
						}
					}
					tableViewer.setSorter(sorter);

				}
			}
			columnNames[i++] = columnInfo.getCaption();

		}
		// setup table viewer
		tableViewer.setContentProvider(new SortFilterContentProvider());
		if (labelProvider != null)
			tableViewer.setLabelProvider(labelProvider);
		tableViewer.setColumnProperties(columnNames);
		tableViewer.setCellModifier(new SortFilterCellModifier());

		// Can only changes the first column - the visible column
		CellEditor[] editors = new CellEditor[collectionColumns.size()];
		editors[0] = new CheckboxCellEditor(table);
		for (i = 1; i < collectionColumns.size(); i++) {
			editors[i] = null;
		}
		tableViewer.setCellEditors(editors);

		if (elementCollection != null && !elementCollection.isEmpty())
			tableViewer.setInput(elementCollection);

		// Update the model
		TableItem[] tableItems = tableViewer.getTable().getItems();
		List newModel = new ArrayList();
		for (i = 0; i < tableItems.length; i++) {
			SortFilterElement ey = (SortFilterElement) tableItems[i].getData();
			newModel.add(i, ey);
		}
		tableViewer.setInput(newModel);
		elementCollection = newModel;
		createBackUp();
		// pack column widths
		TableColumn[] tableColumns = table.getColumns();
		for (i = 0; i < tableColumns.length; i++) {
			tableColumns[i].pack();
		}
	}

	/**
	 * Creates toolbar
	 * @param ancestor the parent composite
	 */
	private void createToolBar(Composite ancestor) {
		Composite parent = new Composite(ancestor, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		parent.setLayout(layout);

		GridData gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		parent.setLayoutData(gridData);

		//to simulate a vertical toolbar (not possible), create a separate toolbar for each button
		ToolBar toolBar = new ToolBar(parent, SWT.FLAT);
		Image imageUp = DiagramUIPluginImages.DESC_UP_PATH.createImage();
		moveUpToolItem = new ToolItem(toolBar, SWT.PUSH);
		moveUpToolItem.setEnabled(false);
		moveUpToolItem.setToolTipText(MOVE_UP_TOOL_TIP);
		moveUpToolItem.setImage(imageUp);
		moveUpToolItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				moveUpElements();
			}
		});
		ToolBar toolBarDown = new ToolBar(parent, SWT.FLAT);
		Image imageDown = DiagramUIPluginImages.DESC_DOWN_PATH.createImage();
		moveDownToolItem = new ToolItem(toolBarDown, SWT.PUSH);
		moveDownToolItem.setEnabled(false);
		moveDownToolItem.setToolTipText(MOVE_DOWN_TOOL_TIP);
		moveDownToolItem.setImage(imageDown);
		moveDownToolItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				moveDownElements();
			}
		});
	}
	
	/**
	 * Move up the selected items from the table
	 */
	private void moveUpElements() {
		int c = tableViewer.getTable().getSelectionCount();
		if (c == 0)
			return;

		int[] selectionIndexes = tableViewer.getTable().getSelectionIndices();
		List model = (ArrayList) ((ArrayList) tableViewer.getInput());
		for (int i = 0; i < selectionIndexes.length; i++) {
			SortFilterElement element =
				(SortFilterElement) model.get(selectionIndexes[i] - 1);
			model.set(selectionIndexes[i] - 1, model.get(selectionIndexes[i]));
			model.set(selectionIndexes[i], element);
		}
		tableViewer.setSorter(null);
		tableViewer.refresh();

		_sorting = isSameOrder(elementCollection, baseElements) ? Sorting.NONE_LITERAL
				: Sorting.MANUAL_LITERAL;
		_sortingKeys = Collections.EMPTY_MAP;

		handleSelection();

		updateApplyButton();
	}

	/**
	 * Move down the selected items from the table
	 */
	private void moveDownElements() {

		int selectionCount = tableViewer.getTable().getSelectionCount();
		if (selectionCount == 0)
			return;

		List model = (ArrayList) ((ArrayList) tableViewer.getInput());
		int[] selectionIndexes = tableViewer.getTable().getSelectionIndices();
		for (int i = selectionIndexes.length - 1; i >= 0; i--) {
			SortFilterElement element =
				(SortFilterElement) model.get(selectionIndexes[i] + 1);
			model.set(selectionIndexes[i] + 1, model.get(selectionIndexes[i]));
			model.set(selectionIndexes[i], element);
		}
		tableViewer.setSorter(null);
		tableViewer.refresh();
		
		_sorting = isSameOrder(elementCollection, baseElements) ? Sorting.NONE_LITERAL
				: Sorting.MANUAL_LITERAL;
		_sortingKeys = Collections.EMPTY_MAP;
		
		handleSelection();

		updateApplyButton();
	}

	/**
	 * Compares two sort filter element lists based on the equality of data fields
	 * only. 
	 * @param elements1 <code>List</code> of sort filter elements
	 * @param elements2 <code>List</code> of sort filter elements
	 * @return <code>true</code> if both lists are e
	 */
	private boolean isSameOrder(List elements1, List elements2) {
		Iterator itr1 = elements1.iterator();
		Iterator itr2 = elements2.iterator();
		while (itr1.hasNext() && itr2.hasNext()) {
			SortFilterElement element1 = (SortFilterElement)itr1.next();
			SortFilterElement element2 = (SortFilterElement)itr2.next();
			if (!element1.getData().equals(element2.getData()))
				return false;
		}
		return !itr1.hasNext() && !itr2.hasNext();
	}

	/**
	 * The table viewer selection has changed. Update the toolbar and menu enablements
	 */
	private void handleSelection() {
		ISelection selection = tableViewer.getSelection();
		if (selection == null
			|| !(selection instanceof IStructuredSelection)) {
			return;
		}
		IStructuredSelection structuredSelection =
			(IStructuredSelection) selection;
		List selectionList = structuredSelection.toList();
		boolean selectionEmpty = structuredSelection.isEmpty();
		boolean firstRowSelected = true;
		boolean lastRowSelected = true;
		if (!selectionEmpty) {
			SortFilterElement element =
				(SortFilterElement) selectionList.get(0);
			if (tableViewer.getElementAt(0).equals(element)) {
				firstRowSelected = true;
			} else {
				firstRowSelected = false;
			}
			element =
				(SortFilterElement) selectionList.get(selectionList.size() - 1);
			if (tableViewer
				.getElementAt(tableViewer.getTable().getItemCount() - 1)
				.equals(element)) {
				lastRowSelected = true;
			} else {
				lastRowSelected = false;
			}
		}
		if (moveUpToolItem != null) {
			moveUpToolItem.setEnabled(!firstRowSelected && !selectionEmpty);
		}
		if (moveDownToolItem != null) {
			moveDownToolItem.setEnabled(!lastRowSelected && !selectionEmpty);
		}
	}
	
	/**
	 * Used to populate to
	 * @return the list used to populate the possible filter list
	 */
	private String[] getFilterList() {
		return filterStrings;
	}

	/**
	 * 
	 * @param filterMap
	 * @param property
	 */
	public void setFilter(Map filterMap, String property) {
		if (filterMap != null) {
			this.filterMap = filterMap;
			Object[] filterArray = filterMap.keySet().toArray();
			this.filterStrings = new String[filterArray.length];
			for (int i = 0; i < filterArray.length; i++) {
				filterStrings[i] = (String) filterArray[i];
			}
			this.filterAppliesTo = property;
		}
	}

	/**
	 * find the column index using a property
	 * @param property the proerty to use
	 * @return the column index
	 */
	private int findColumnIndexFromProperty(String property) {
		for (int i = 0; i < collectionColumns.size(); i++) {
			if (((SortFilterCollectionColumn)collectionColumns.get(i)).getCaption().equals(property))
				return i;
		}
		return -1;
	}

	/**
	 * Sets the input for the table.
	 * @param sortFilterElements the semantic children for thie <code>IView</code>
	 */
	public void setInput(List sortFilterElements) {

		// Remove elements that are not to be shown in the dialog at all.
		List updatedSortFilterElements = new ArrayList();
		for (Iterator iter = sortFilterElements.iterator(); iter.hasNext();) {
			SortFilterElement element = (SortFilterElement) iter.next();
				updatedSortFilterElements.add(element);
		}

		// Cache the provided elements for the Reset Default button
		baseElements = new ArrayList(updatedSortFilterElements.size());
		for (Iterator itr = updatedSortFilterElements.iterator(); itr.hasNext();) {
			baseElements.add(new SortFilterElement(((SortFilterElement)itr.next()).getData()));
		}

		// For the manual Ahoc sorting and filtering we manually initialize the
		// items
		if (_filtering == Filtering.MANUAL_LITERAL) {
			// Use the list of filtered items to initialize the table.
			// Any new items will not be filtered.
			if (!updatedSortFilterElements.isEmpty()) {
				if (_filteredObjects != null && _filteredObjects.size() > 0) {
					for (int i = 0; i < _filteredObjects.size(); i++) {
						EObject eObject = (EObject)_filteredObjects.get(i);
						for (int j = 0; j < updatedSortFilterElements.size(); j++) {
							SortFilterElement element = (SortFilterElement) updatedSortFilterElements
								.get(j);
							if (eObject.equals(element.getData())) {
								element.setVisible(false);
							}
						}
					}
				}
			}
		}

		if (_sorting == Sorting.MANUAL_LITERAL) { // Add hoc sorting
			if (!updatedSortFilterElements.isEmpty()) {
				if (_sortedObjects != null && _sortedObjects.size() > 0) {
					// Order the elements by the specified index
					elementCollection =
						new ArrayList(updatedSortFilterElements.size());
					for (int i = 0; i < _sortedObjects.size(); i++) {
						EObject element = (EObject) _sortedObjects.get(i);
						for (int j = 0; j < updatedSortFilterElements.size(); j++) {
							SortFilterElement e = (SortFilterElement) updatedSortFilterElements
								.get(j);
							if (element.equals(e.getData())) {
								elementCollection.add(e);
							}
						}
					}
					// Add any new children to the end of the list
					// so they will appear at the botton of the
					// list compartment
                    List missingElements = new ArrayList(updatedSortFilterElements);
                    missingElements.removeAll(elementCollection);
                    elementCollection.addAll(missingElements);
				}
			}

		} else { // Use semantic order
			elementCollection = updatedSortFilterElements;
		}
		createBackUp();
	}
	
	/**
	 * 
	 * @param sortFilterElements
	 * @param hiddenContents
	 */
	public void setContents(List sortFilterElements, List hiddenContents) {
		setInput(sortFilterElements);
		// Cache the items not to be shown in the dialog, but are otherwise filtered.
		this._shownAsAlternateViewItems = hiddenContents;
	}

	/**
	 * Resets all element to visible and uses the model storage
	 * ordering for the sorting.
	 */
	protected void performDefaults() {
		_filtering = Filtering.NONE_LITERAL;
		_filteredObjects = Collections.EMPTY_LIST;
			
		// Reset the filter list if they exist
		if (filterStrings != null) {
			if (!_filteringKeys.isEmpty()) {
				_filteringKeys = Collections.EMPTY_LIST;
				populateFilterLists();
			}
		}
		
		_sorting = Sorting.NONE_LITERAL;
		_sortingKeys = Collections.EMPTY_MAP;
		_sortedObjects = Collections.EMPTY_LIST;
		
		tableViewer.setSorter(null);
		TableColumn[] columns = tableViewer.getTable().getColumns();
		for (int i = 0; i < columns.length; i++) {
			columns[i].setImage(null);
		}

		List input = (List)tableViewer.getInput();
		input.clear();
		for (int i=0; i<baseElements.size(); i++) {
			input.add(new SortFilterElement( ((SortFilterElement)baseElements.get(i)).getData() ));
		}
		tableViewer.refresh();

		updateApplyButton();

	}

	/**
	 * Writes the sorting/filtering specified by the dialog
	 */
	protected void performApply() {
		Command sortAndFilteringCommand = getApplyCommand();
		if (sortAndFilteringCommand != null
				&& sortAndFilteringCommand.canExecute()) {
			editPart.getRoot().getViewer().getEditDomain().getCommandStack()
					.execute(sortAndFilteringCommand);
			createBackUp();
		}
		updateApplyButton();            
	}
	
	/**
	 * Creates the command that needs to be executed for this page when "Ok" is
	 * pressed. It's different from {@link #getApplyCommand()}, because it checks
	 * whether the page is dirty or not.
	 * @return <code>Command</code> to be executed per this page
	 */
	public Command getCommand() {
		if (isDirty())
			return getApplyCommand();
		return null;
	}
	
	/**
	 * Returns a <code>Command</code> that set both the sorting and filtering
	 * for this particular list compartment.
	 * 
	 * @return the command
	 */
	public Command getApplyCommand() {
		Command cmd = UnexecutableCommand.INSTANCE;
		if (CHILD_PAGE.equals(pageType)) {
			List newSortedObjects = Collections.EMPTY_LIST; 
			if (_sorting.equals(Sorting.MANUAL_LITERAL)) {
					newSortedObjects = new ArrayList();
					for (Iterator itr = elementCollection.iterator(); itr.hasNext();) {
						SortFilterElement element = (SortFilterElement) itr.next();
						newSortedObjects.add(element.getData());
					}
			}
			
			List newFilteredObjects = Collections.EMPTY_LIST;
			if (_filtering.equals(Filtering.MANUAL_LITERAL)) {
				newFilteredObjects = new ArrayList();
				for (Iterator itr = elementCollection.iterator(); itr.hasNext();) {
					SortFilterElement element = (SortFilterElement) itr.next();
					if (!element.isVisible()) {
						newFilteredObjects.add(element.getData());
					}
				}
				if (_filtering.equals(Filtering.MANUAL_LITERAL) && newFilteredObjects.size() == 0) {
					_filtering = Filtering.NONE_LITERAL;
				}
			}	
			
			// Add the objects filtered otherwise.
			if (!_shownAsAlternateViewItems.isEmpty()
				&& Collections.EMPTY_LIST.equals(newFilteredObjects)) {
				newFilteredObjects = new ArrayList();
			}
			newFilteredObjects.addAll(_shownAsAlternateViewItems);
	
			ChangeSortFilterRequest request = new ChangeSortFilterRequest(
				_filtering, newFilteredObjects, _filteringKeys, _sorting,
				newSortedObjects, _sortingKeys);
			
			cmd = editPart.getCommand(request);		
		}
		return cmd;
	}

	/**
	 * Populates the filter lists based on the _filteringKeys
	 * and the filter criteria.
	 */
	private void populateFilterLists() {
		if (filterMap != null && !filterMap.isEmpty() && filterList != null && filters != null) {
			filterList.removeAll();
			filters.removeAll();
			Set keySet = filterMap.keySet();
			Iterator i = keySet.iterator();
			if (_filtering == Filtering.AUTOMATIC_LITERAL) {
				// Set the values of the filtered and unfiltered string
				while (i.hasNext()) {
					String filterString = (String) i.next();
					if (_filteringKeys.contains(filterString)) {
						filters.add(filterString);						
					} else {
						filterList.add(filterString);
					}
				}
			} else {
				// Add all filter strings to the possible filter list
				while (i.hasNext()) {
					String filterString = (String) i.next();
					filterList.add(filterString);
				}
			}
		}
	}


	/**
	 * Caches ID_FILTERING, ID_FILTERED_OBJECTS and ID_FILTERING_KEYS values for local access.
	 */
	private void tokenizeFilterProperty() {
		Object model = editPart.getModel();
		if (model instanceof View){
			View view = (View)model;
			FilteringStyle style = (FilteringStyle) view.getStyle(NotationPackage.eINSTANCE.getFilteringStyle());
			if (style != null) {
				_filtering = style.getFiltering();	
				_filteredObjects = style.eIsSet(NotationPackage.eINSTANCE.getFilteringStyle_FilteredObjects())
					? new ArrayList(style.getFilteredObjects())
					: Collections.EMPTY_LIST;
				_filteringKeys = style.eIsSet(NotationPackage.eINSTANCE.getFilteringStyle_FilteringKeys())
					? new ArrayList(style.getFilteringKeys())
					: Collections.EMPTY_LIST;
			}
		}
	}

	/**
	 * Caches ID_SORTING, ID_SORTED_OBJECTS and ID_SORTING_KEYS values for local access.
	 */
	private void tokenizeSortProperty() {
		Object model = editPart.getModel();
		if (model instanceof View){
			View view = (View)model;
			SortingStyle style = (SortingStyle)view.getStyle(NotationPackage.eINSTANCE.getSortingStyle());
			if (style != null) {
				_sorting =	style.getSorting();		
				_sortedObjects = style.eIsSet(NotationPackage.eINSTANCE.getSortingStyle_SortedObjects())
					? new ArrayList(style.getSortedObjects())
					: Collections.EMPTY_LIST;
				_sortingKeys = style.eIsSet(NotationPackage.eINSTANCE.getSortingStyle_SortingKeys())
					? new HashMap(style.getSortingKeys())
					: Collections.EMPTY_MAP;
						
				// Currently, only one sorting column can be defined.
				if (_sortingKeys.size() > 0) {
					Set keySet = _sortingKeys.keySet();
					Iterator iter = keySet.iterator();
					if (iter.hasNext()) {
						_sortColumn = (String) iter.next();
						_sortingDirection = (SortingDirection) _sortingKeys.get(_sortColumn);
					}
						
				}
			}
		}

	}

	/**
	 * SortFilterCellModifiers. Simple cell modifiers for the first column only
	 * 
	 * @author jcorchis
	 */
	public class SortFilterCellModifier implements ICellModifier {

		/**
		 * Only allows the visibility property to be modified.
		 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
		 */
		public boolean canModify(Object element, String property) {
			int columnIndex = findPropertyIndex(property);
			if (columnIndex == 0) {
				return true;
			}
			return true;
		}

		/**
		 * Gets the value of the table's cell.
		 * @param element the SortFilterElement
		 * @param property the table property
		 * @return the cell's value
		 * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
		 */
		public Object getValue(Object element, String property) {
			SortFilterElement item = (SortFilterElement) element;

			// Find the index of the column
			Object result = null;
			int columnIndex = findPropertyIndex(property);
			if (columnIndex == 0) {
				result = Boolean.valueOf(item.isVisible());
			}
			return result;
		}

		/**
		 * Modifies the visibility of the compartment item.
		 * @param element the SortFilterElement
		 * @param property the table property
		 * @param value the cell's new value
		 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
		 */
		public void modify(Object element, String property, Object value) {
			boolean newValue = ((Boolean) value).booleanValue();
			TableItem tableItem = (TableItem) element;
			SortFilterElement item = (SortFilterElement) tableItem.getData();
			int columnIndex = findPropertyIndex(property);
			if (columnIndex == 0) {
				TableItem[] tableItems = tableViewer.getTable().getItems();
				for (int j = 0; j < tableItems.length; j++) {
					SortFilterElement data =
						(SortFilterElement) tableItems[j].getData();
					if (data.equals(item)) {
						(
							(SortFilterElement) tableViewer.getElementAt(
								j)).setVisible(
							newValue);
						tableViewer.update(
							new Object[] { tableViewer.getElementAt(j)},
							new String[] { getColumnProperties()[0] });
					}
				}

				tableViewer.update(
					new Object[] { element },
					new String[] { getColumnProperties()[0] });

				// Remove all the filtering criteria since the user
				// is filtering in an ad hoc manner
				if (filterStrings != null) {
					String[] items = filters.getItems();
					for (int i = 0; i < items.length; i++) {
						filterList.add(items[i]);
						filters.remove(items[i]);
					}
				}
				_filteringKeys = Collections.EMPTY_LIST;
				
				_filtering = newValue ? Filtering.NONE_LITERAL : Filtering.MANUAL_LITERAL;
				if (newValue) {
					Iterator itr = elementCollection.iterator();
					while (itr.hasNext()) {
						if (!baseElements.contains(itr.next())) {
							_filtering = Filtering.MANUAL_LITERAL;
							break;
						}
					}
				}
				updateApplyButton();
			}
		}

		/**
		 * Maps the column property to the index the property appears.
		 * @param property the column property
		 * @return the appearance index of the property
		 */
		private int findPropertyIndex(String property) {
			Object[] columnNames = tableViewer.getColumnProperties();
			for (int i = 0; i < columnNames.length; i++) {
				if (((String) columnNames[i]).equals(property))
					return i;
			}
			return -1;

		}

	}

	/**
	 * <code>SelectionListener</code> implementation for selection on the Table's column
	 * headers. Header selection event will sort the table by the column selected,
	 * both ascending and descending, as well as insert the an image in the selected column
	 * header to indicate the sort order. This listener treats both a single and double clicks
	 * as the same to support different OS selection firing.
	 * 
	 * @author jcorchis
	 */
	class HeaderSelectionListener implements SelectionListener {
		private SortFilterViewerSorter sorter;
		private TableViewer _tableViewer;
		/**
		 * Constructor for the HeaderSelectionListener
		 * @param sorter the <code>TableViewer</code> sorter
		 * @param tableViewer the <code>TableViewer</code>
		 */
		public HeaderSelectionListener(
			SortFilterViewerSorter sorter,
			TableViewer tableViewer) {
			this.sorter = sorter;
			_tableViewer = tableViewer;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		public void widgetSelected(SelectionEvent e) {
			// Handle click to sort
			handleEvent(e);
		}
		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		public void widgetDefaultSelected(SelectionEvent e) {
			// Handle double click to sort
			handleEvent(e);
		}

		private void handleEvent(SelectionEvent e) {
			// Clear any existing header images
			TableColumn[] columns = _tableViewer.getTable().getColumns();
			for (int i = 0; i < columns.length; i++) {
				columns[i].setImage(null);
			}
			// Add the image based on the sort
			int columnIndex = 0;
			if (e.widget instanceof TableColumn) {
				TableColumn column = (TableColumn) e.widget;
				columnIndex = findColumnIndexFromProperty(column.getText());
				if (columnIndex != -1) {
					Image image = null;
					SortFilterViewerSorter newSorter =
						(SortFilterViewerSorter) _tableViewer.getSorter();
					if (newSorter != null) {
						newSorter.toggleSortingDirection();
						if (newSorter == null
							|| SortingDirection.ASCENDING_LITERAL
								.equals(newSorter.getSortingDirection())) {
							// Use the ascending image
							image = DiagramUIPluginImages.DESC_SORT_ARROW_UP
								.createImage();
						} else {
							// Use the descending image
							image = DiagramUIPluginImages.DESC_SORT_ARROW_DN
								.createImage();
						}
						columns[columnIndex].setImage(image);
					}
				}
			}

			if (sorter != null) {
				// Does the actual sorting				
				_tableViewer.setSorter(null);
				// Necessary to use ascending/descending sorting
				_tableViewer.setSorter(getSorter());

				// Update the model
				TableItem[] tableItems = _tableViewer.getTable().getItems();
				List newModel = new ArrayList();
				for (int i = 0; i < tableItems.length; i++) {
					SortFilterElement ey =
						(SortFilterElement) tableItems[i].getData();
					newModel.add(i, ey);
				}
				elementCollection.clear();
				elementCollection.addAll(newModel);
				_tableViewer.refresh();

				_sorting = Sorting.AUTOMATIC_LITERAL;
				Object[] columnNames = tableViewer.getColumnProperties();
				if (_sortingKeys != Collections.EMPTY_MAP) {
					_sortingKeys.clear();
						
				} else {
					_sortingKeys = new HashMap();
				}
				_sortingKeys.put(columnNames[columnIndex], getSorter().getSortingDirection());				

				handleSelection();

				updateApplyButton();
			}

		}
		
		/**
		 * gets the sorter
		 * @return the sorter
		 */
		public SortFilterViewerSorter getSorter() {
			return sorter;
		}
	}

	/** 
	 * Method to allow the ROOT page to set the possible filter
	 * items for this page.
	 * @param filterCriteriaList
	 */
	void setFilterCriteria(String[] filterCriteriaList) {
		filters.setItems(filterCriteriaList);
	}

	/**
	 * Checks whether this page is filtering it's contents.
	 * @return <code>true</code> if this page is filtering it's contents.
	 */
	boolean isFiltering() {
		return filterStrings == null ? false : (filters.getItems().length != 0);
	}

	/**
	 * Method to allows the ROOT page to set the filter criteria of a CHILD page.
	 * @param criteriaList the filter criteria
	 */
	void setCriteria(String[] criteriaList) {
		filterList.setItems(criteriaList);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#updateApplyButton()
	 */
	protected void updateApplyButton() {
		if (getApplyButton() != null)
			getApplyButton().setEnabled(isValid() && isDirty());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		super.createControl(parent);
		updateApplyButton();
	}
	
	/**
	 * Determines whether the page contains changes that can be applied.
	 * @return <code>true</code> if page is dirty
	 */
	protected boolean isDirty() {
		if (pageType == ROOT_PAGE
			|| _filteringBackUp != _filtering
			|| _sortingBackUp != _sorting)
			return true;
		
		return !elementCollection.equals(elementCollectionBackUp);
	}
	
	/**
	 * Creates the back up of the initial data to determine whether the page
	 * has applicable changes later on.
	 */
	private void createBackUp() {
		_filteringBackUp = _filtering;
		_sortingBackUp = _sorting;
		elementCollectionBackUp = new ArrayList(elementCollection.size());
		for (Iterator itr = elementCollection.iterator(); itr.hasNext();) {
			SortFilterElement element = (SortFilterElement) itr.next();
			elementCollectionBackUp.add(new SortFilterElement(element
					.isVisible(), element.getData()));
		}
	}

}
