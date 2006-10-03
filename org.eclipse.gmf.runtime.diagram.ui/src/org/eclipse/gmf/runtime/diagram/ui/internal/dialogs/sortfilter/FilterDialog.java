/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.dialogs.sortfilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ListCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.SortFilterContentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.requests.ChangePropertyValueRequest;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Filtering;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;


/**
 * Simple dialog that support filtering of list compartment items through visibililty
 * 
 * @author jcorchis
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class FilterDialog
	extends Dialog {
	
	/** dialog title prefix */
	private final String title = DiagramUIMessages.SortFilterDialog_title;
	
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
	static private final String APPLY = DiagramUIMessages.SortFilter_apply;

	/** List item widgets */
	private org.eclipse.swt.widgets.List filterList = null;
	private org.eclipse.swt.widgets.List filters = null;
	private Button addTo = null;
	private Button removeFrom = null;
	private Button addAllTo = null;
	private Button removeAllFrom = null;

	/** Button IDs */
	private final int ADD_TO_ID = 1000;
	private final int REMOVE_FROM_ID = ADD_TO_ID + 1;
	private final int ADD_ALL_TO_ID = ADD_TO_ID + 2;
	private final int REMOVE_ALL_FROM_ID = ADD_TO_ID + 3;	
	private final int APPLY_ID = 5000;

	/** Height (in list items) for the filter items lists */
	private int LIST_HEIGHT = 8;

	/** The collection Column list for this page */
	private Map filterMap = null;
	private String[] filterStrings = null;
	
	/** the default fitlering settings */
	private Filtering _filtering = Filtering.NONE_LITERAL;
	private List _filteringKeys = Collections.EMPTY_LIST;
	
	/** List of editpart selected */
	private List selection;	
	
	/**  
	 * Allows the user to initial apply changes and then
	 * only after filtering has changed.
	 */
	private boolean changeToApply = true;

	
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
	 * @param parentShell
	 */
	protected FilterDialog(Shell parentShell, List selection, Map filterMap) {
		super(parentShell);
		this.selection  = selection;
		this.filterMap = filterMap;
	}

		
	/**
	 * Adds an apply button and the filter control 
	 * @see org.eclipse.jface.dialogs.Dialog#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {		
		getShell().setText(title);
		createFilterLists(parent);				
		return parent;
	}
	
	
	/**
	 * Adds the apply button
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		createButton(parent, APPLY_ID, APPLY, true);		
		
	}
	
	/**
	 * Creates the filtering list widgets
	 * @param parent
	 */
	private void createFilterLists(Composite ancestor) {
		// Do not show the filter lists if not filter criteria 
		// is defined.
		if (filterMap == null || filterMap.isEmpty())	
			return;
		
		Object[] filterArray = filterMap.keySet().toArray();
		this.filterStrings = new String[filterArray.length];
		for (int i = 0; i < filterArray.length; i++) {
			filterStrings[i] = (String) filterArray[i];
		}	
		
		//setup layout 
		Composite parent = new Composite(ancestor, SWT.NULL);

		GridLayout layout = new GridLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		layout.numColumns = 3;
		parent.setLayout(layout);

		// Create the possible filter items list
		Label filterItemsLabel = new Label(parent, SWT.LEFT);
		filterItemsLabel.setText(FILTER_ITEMS_CONTAINING);
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
		filterItemsLabel.setLayoutData(gd);

		// Create the possible filter items list
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
		removeFrom.setData(new Integer(REMOVE_FROM_ID));
		removeFrom.addSelectionListener(buttonSelectionAdapter);
		removeFrom.setEnabled(false);

		addTo = new Button(buttonComposite, SWT.PUSH);
		addTo.setText(ADD_TO_LABEL);
		addTo.setToolTipText(ADD_TO);
		addTo.setLayoutData(makeArrowButtonGridData(addTo));
		addTo.setData(new Integer(ADD_TO_ID));
		addTo.addSelectionListener(buttonSelectionAdapter);
		addTo.setEnabled(false);

		removeAllFrom = new Button(buttonComposite, SWT.PUSH);
		removeAllFrom.setText(REMOVE_ALL_LABEL);
		removeAllFrom.setToolTipText(REMOVE_ALL);
		removeAllFrom.setLayoutData(makeArrowButtonGridData(removeAllFrom));
		removeAllFrom.setData(new Integer(REMOVE_ALL_FROM_ID));
		removeAllFrom.addSelectionListener(buttonSelectionAdapter);
		addAllTo = new Button(buttonComposite, SWT.PUSH);

		addAllTo.setText(ADD_ALL_LABEL);
		addAllTo.setToolTipText(ADD_ALL);
		addAllTo.setLayoutData(makeArrowButtonGridData(addAllTo));
		addAllTo.setData(new Integer(ADD_ALL_TO_ID));
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

		initFilterLists();
	}
	
	/**
	 * Populates the filter lists based on the _filteringKeys
	 * and the filter criteria.
	 */
	private void initFilterLists() {
		// TODO match the filtering key for all the list compartments
		if (filterMap != null && !filterMap.isEmpty()) {
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
		}
	}
	
	/**
	 * Handles the button pressed event on the filter criteria.  Move the items
	 * between the lists based on the selection and but button pressed.
	 * @see org.eclipse.jface.dialogs.Dialog#buttonPressed(int)
	 */
	protected void buttonPressed(int buttonId) {
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
				changeToApply = true;
				break;
			case REMOVE_FROM_ID :
				items = filters.getSelection();
				for (int i = 0; i < items.length; i++) {
					filterList.add(items[i]);
					filters.remove(items[i]);
				}
				removeFrom.setEnabled(false);
				changeToApply = true;
				break;
			case ADD_ALL_TO_ID :
				items = filterList.getItems();
				for (int i = 0; i < items.length; i++) {
					filters.add(items[i]);
					filterList.remove(items[i]);
				}
				changeToApply = true;
				break;
			case REMOVE_ALL_FROM_ID :
				items = filters.getItems();
				for (int i = 0; i < items.length; i++) {
					filters.remove(items[i]);
					filterList.add(items[i]);
				}
				changeToApply = true;
				break;
			case IDialogConstants.OK_ID :
				okPressed();
				break;
			case APPLY_ID :
				applyPressed();
				break;
			default:
				super.buttonPressed(buttonId);
		}
	}	
	
	/**
	 * 
	 */
	protected void okPressed() {
		if (changeToApply)
			applyPressed();
		super.okPressed();		
	}
	
	/**
	 * Executes a sort filter command for every list compartment that understands
	 * the sort filter request.
	 */
	protected void applyPressed() {	
		if (!changeToApply)
			return;
		if (filters.getItemCount() > 0) { 
			_filteringKeys = new ArrayList();
			_filtering = Filtering.AUTOMATIC_LITERAL;
			for (int i = 0; i < filters.getItemCount(); i++) {
				_filteringKeys.add(filters.getItems()[i]);
			}
		} else {
			_filtering = Filtering.NONE_LITERAL;
			_filteringKeys = Collections.EMPTY_LIST;
		}

		ChangePropertyValueRequest filterTypeRequest = new ChangePropertyValueRequest(
			Properties.ID_FILTERING, Properties.ID_FILTERING,
			_filtering);
		ChangePropertyValueRequest filterKeysRequest = new ChangePropertyValueRequest(
			Properties.ID_FILTERING_KEYS, Properties.ID_FILTERING_KEYS,
			_filteringKeys);

		// Run the command	
        TransactionalEditingDomain editingDomain = null;
        List childCommands = new ArrayList();
		Iterator iter = selection.iterator();
		while (iter.hasNext()) {
			GraphicalEditPart ep = (GraphicalEditPart) iter.next();
            
            if (editingDomain == null) {
                editingDomain = ep.getEditingDomain();
            }
            
			List children = ep.getChildren();
			for (int i = 0; i < children.size(); i++) {
				if (children.get(i) instanceof ListCompartmentEditPart) {
					ListCompartmentEditPart editPart = (ListCompartmentEditPart) children
						.get(i);
					
					if (filterMap != null && filterMap.equals(getFilterMapFromEditPart(editPart))) {					
                        childCommands.add(new CommandProxy(editPart
							.getCommand(filterTypeRequest)));
                        childCommands.add(new CommandProxy(editPart
							.getCommand(filterKeysRequest)));
					}
				}
			}
		}
        
        CompositeTransactionalCommand cc = new CompositeTransactionalCommand(editingDomain, 
            DiagramUIMessages.Command_SortFilterCommand, childCommands);   
        
		((IGraphicalEditPart) selection.get(0)).getRoot().getViewer()
			.getEditDomain().getCommandStack().execute(
				new ICommandProxy(cc));	
		
		changeToApply = false;
	}
	
	/**
	 * Returns the ListCompartment level filter map or Collections.EMPTY_MAP
	 * if an EditPolicyRoles.SORT_FILTER_CONTENT_ROLE is not installed
	 * on the ListCompartment. 
	 * @param editPart
	 * @return Map the filter map
	 */
	private Map getFilterMapFromEditPart(ListCompartmentEditPart editPart) {
		
		EditPolicy ep = editPart.getEditPolicy(EditPolicyRoles.SORT_FILTER_CONTENT_ROLE);
		if (ep instanceof SortFilterContentEditPolicy) { 
			return ((SortFilterContentEditPolicy)ep).getFilter();
		}
		
		return Collections.EMPTY_MAP;
		
	}
}

