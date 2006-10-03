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
package org.eclipse.gmf.runtime.common.ui.services.elementselection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.ui.services.internal.l10n.CommonUIServicesMessages;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

/**
 * The element selection composite. The composite functional similar to the JDT
 * select type dialog. There is a filter field and a table containing a list of
 * elements to select from.
 * <p>
 * The element selection composite requires an IElementSelectionInput as input
 * for the element selection service.
 * <p>
 * Subclasses must override the {@link #isValidSelection}and
 * {@link #handleSelection(boolean)} to provide custom validation.
 * 
 * @author Anthony Hunter
 */
public abstract class ElementSelectionComposite
    implements IElementSelectionListener {

    /**
     * The title to display at the top of the element selection composite.
     */
    private final String title;

    /**
     * The elements that have been selected by the user.
     */
    private final List selectedElements = new ArrayList();

    /**
     * Text control to display the filter text.
     */
    private Text filterText = null;

    /**
     * The table viewer to display list of matching objects.
     */
    private TableViewer tableViewer = null;

    /**
     * The progress bar when searching for matching objects.
     */
    private ProgressMonitorPart progressBar;

    /**
     * The input for the element selection service.
     */
    private AbstractElementSelectionInput input;

    /**
     * The job running the element selection service.
     */
    private ElementSelectionServiceJob job;
    
    /**
     * The element selection service to use to search for elements.
     */
    private final ElementSelectionService elementSelectionService;

    /**
     * Control character for the filter.
     * <p>
     * When the user enters the first character into the filterText, element
     * selection service is called. When the user enters the second character
     * after the first, we can use the existing results returned by the service.
     * If the user enters text such that the first character has been changed,
     * we need to query the service again.
     * <p>
     * For example, if the user enters "a" then "ab", we can use the existing
     * results from "a". If the user enters "a" then "b", then we must query a
     * second time.
     * <p>
     * We also must remember if the service has already been called. If the user
     * enters "a" and then "b", we must cancel "a" and wait before calling the
     * service for "b".
     */
    private char firstCharacter = Character.MIN_VALUE;
    private String lastSearchedFor = StringStatics.BLANK;
    private int lastScopeSearchedFor = 0;

    /**
     * matching objects from the element selection service.
     */
    private List matchingObjects = new ArrayList();

    /**
     * Pattern for the input filter.
     */
    private Pattern pattern;
    
    /**
     * Constructs a new instance that will create the new composite.  I will use
     * the default {@linkplain ElementSelectionService#getInstance() selection service}
     * to process the <tt>input</tt>.
     * 
     * @param title
     *            the dialog title
     * @param input
     *            the element selection input.
     */
    public ElementSelectionComposite(String title,
            AbstractElementSelectionInput input) {
        this(title, input, ElementSelectionService.getInstance());
    }
    
    /**
     * Constructs a new instance that will create the new composite.
     * 
     * @param title the dialog title
     * @param input the element selection input
     * @param elementSelectionService the selection service to use to process the
     *     <tt>input</tt>
     */
    public ElementSelectionComposite(String title,
    		AbstractElementSelectionInput input,
    		ElementSelectionService elementSelectionService) {
    	super();
    	this.title = title;
    	this.input = input;
    	this.elementSelectionService = elementSelectionService;
        this.lastScopeSearchedFor = input.getScope().intValue();
    }

    /**
     * Determines if the selected elements are a valid selection.
     * 
     * @param currentSelectedElements
     *            the selected list of Elements
     * @return <code>true</code> if the selected elements are a valid
     *         selection
     */
    abstract protected boolean isValidSelection(List currentSelectedElements);

    /**
     * Handle a selection change, where the validity of the new selection is
     * encoded in <code>isValid</code>.
     * 
     * @param isValid
     *            <code>true</code> if the new selection is valid,
     *            <code>false</code> otherwise.
     */
    protected abstract void handleSelection(boolean isValid);

    /**
     * Creates the composite.
     * 
     * @param parent
     *            the parent composite
     * @return the new composite
     */
    public Composite createComposite(Composite parent) {

        Composite result = new Composite(parent, SWT.NONE);
        result.setLayout(new GridLayout());
        result.setLayoutData(new GridData(GridData.FILL_BOTH));

        // Add the selection title label
        Label label = new Label(result, SWT.NONE);
        label.setText(title);

        // Add the element selection text widget
        filterText = new Text(result, SWT.SINGLE | SWT.BORDER);
        filterText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        filterText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                handleFilterChange();
            }

        });

        // Add the table viewer
        int selectStyle = SWT.SINGLE;
        tableViewer = new TableViewer(result, selectStyle | SWT.H_SCROLL
            | SWT.V_SCROLL | SWT.BORDER);
        tableViewer.setUseHashlookup(true);

        Table table = tableViewer.getTable();
        GridData gridData = new GridData(GridData.FILL_BOTH);
        GC gc = new GC(result);
        gc.setFont(JFaceResources.getDefaultFont());
        FontMetrics fontMetrics = gc.getFontMetrics();
        gc.dispose();
        gridData.widthHint = Dialog
            .convertWidthInCharsToPixels(fontMetrics, 80);
        gridData.heightHint = table.getItemHeight() * 15;
        table.setLayoutData(gridData);

        table.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                handleSelectionChange();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                // method not implemented
            }
        });

        progressBar = new ProgressMonitorPart(result, new GridLayout());
        progressBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        progressBar.setVisible(false);

        tableViewer.setLabelProvider(new LabelProvider() {

            public Image getImage(Object element) {
                assert element instanceof AbstractMatchingObject;
                return ((AbstractMatchingObject) element).getImage();
            }

            public String getText(Object element) {
                assert element instanceof AbstractMatchingObject;
                return ((AbstractMatchingObject) element).getDisplayName();
            }
        });
        tableViewer.setSorter(new ViewerSorter() {
            public int compare(Viewer viewer, Object e1, Object e2) {
                if (e1 instanceof IMatchingObject && e2 instanceof IMatchingObject)
                    return ((IMatchingObject)e1).getName().toLowerCase().compareTo(
                        ((IMatchingObject)e2).getName().toLowerCase());
                
                return super.compare(viewer, e1, e2);
            }
        });

        createCompositeAdditions(result);

        return result;
    }

    /**
     * The method is provided so that clients can add additional fields to the
     * bottom of the selection composite. For example, clients may want to a
     * checkbox button to the bottom of the composite.
     * 
     * @param parent
     *            the parent composite
     */
    protected void createCompositeAdditions(Composite parent) {
        /* clients are expected to override this method */
    }

    /**
     * Handles a filter change.
     */
    public void handleFilterChange() {
        if (filterText.getText().equals(StringStatics.BLANK)) {
            /* no filter, no results */
            cancel();
            matchingObjects.clear();
            tableViewer.getTable().removeAll();            
            firstCharacter = Character.MIN_VALUE;
            return;
        }
        
        String filter = validatePattern(filterText.getText());
        pattern = Pattern.compile(filter);
        if (firstCharacter != filterText.getText().charAt(0) ||
                this.input.getScope().intValue() != this.lastScopeSearchedFor ||
                !filterText.getText().startsWith(lastSearchedFor)) {
            //scope changes, start from scratch...
            cancel();
            matchingObjects.clear();
            tableViewer.getTable().removeAll();
            
            firstCharacter = filterText.getText().charAt(0);
            this.lastScopeSearchedFor = this.input.getScope().intValue();
            
            startElementSelectionService();
        } else {
            /*
             * clear the existing matches in the table and refilter results we have
             * received
             */
            tableViewer.getTable().removeAll();
            for (Iterator i = matchingObjects.iterator(); i.hasNext();) {
                IMatchingObject matchingObject = (IMatchingObject) i.next();
                Matcher matcher = pattern.matcher(matchingObject.getName()
                    .toLowerCase());
                if (matcher.matches()) {
                    tableViewer.add(matchingObject);
                    setSelection();
                }
            }
        }
    }

    /**
     * Fill the table viewer with results from the element selection service.
     */
    private void startElementSelectionService() {
        /*
         * Initialize all possible matching objects from the select element
         * service.
         */
        input.setInput(filterText.getText());
        lastSearchedFor = filterText.getText();
        
        progressBar.setVisible(true);
        progressBar.beginTask(
            CommonUIServicesMessages.ElementSelectionService_ProgressName,
            IProgressMonitor.UNKNOWN);

        job = elementSelectionService.getMatchingObjects(input, this);
    }

    /**
     * Handles a selection change and validates the new selection.
     */
    private void handleSelectionChange() {
        StructuredSelection selection = (StructuredSelection) tableViewer
            .getSelection();
        if (selection.size() == 0) {
            // nothing selected
            selectedElements.clear();
            handleSelection(false);
            return;
        }

        List selectionList = selection.toList();

        // get the current selected elements
        List currentSelectedElements = new ArrayList();
        for (Iterator iter = selectionList.iterator(); iter.hasNext();) {
            AbstractMatchingObject matchingObject = (AbstractMatchingObject) iter
                .next();
            currentSelectedElements.add(matchingObject);
        }

        // validate selection
        boolean isValidSelection = isValidSelection(currentSelectedElements);

        // store the selection
        selectedElements.clear();
        if (isValidSelection) {
            selectedElements.addAll(currentSelectedElements);
        }

        // update UI based on selection
        handleSelection(isValidSelection);

    }

    /**
     * Gets the user selected elements.
     * 
     * @return the user selected elements
     */
    public List getSelectedElements() {
        List result = new ArrayList();
        for (Iterator iter = selectedElements.iterator(); iter.hasNext();) {
            IMatchingObject matchingObject = (IMatchingObject) iter.next();
            IElementSelectionProvider provider = matchingObject.getProvider();
            Object object = provider.resolve(matchingObject);
            result.add(object);
        }
        return result;
    }

    public void matchingObjectEvent(IMatchingObjectEvent matchingObjectEvent) {
        if (!progressBar.isDisposed()) {
            if (matchingObjectEvent.getEventType() == MatchingObjectEventType.END_OF_MATCHES) {
                progressBar.done();
                progressBar.setVisible(false);
                job = null;
            } else {
                IMatchingObject matchingObject = matchingObjectEvent
                    .getMatchingObject();
                progressBar.worked(1);
                progressBar.subTask(matchingObject.getName());
                matchingObjects.add(matchingObject);
                Matcher matcher = pattern.matcher(matchingObject.getName()
                    .toLowerCase());
                if (matcher.matches()) {
                    tableViewer.add(matchingObject);
                    setSelection();
                }
            }
        }
    }

    /**
     * Cancel the job running the element selection service.
     */
    public void cancel() {
        if (job != null) {
            elementSelectionService.cancelJob(job);
            job = null;
            progressBar.done();
            progressBar.setVisible(false);
        }
    }

    /**
     * Convert the UNIX style pattern entered by the user to a Java regex
     * pattern (? = any character, * = any string).
     * 
     * @param string
     *            the UNIX style pattern.
     * @return a Java regex pattern.
     */
    private String validatePattern(String string) {
        if (string.equals(StringStatics.BLANK)) {
            return string;
        }
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            char c = Character.toLowerCase(string.charAt(i));
            if (c == '?') {
                result.append('.');
            } else if (c == '*') {
                result.append(".*"); //$NON-NLS-1$
            } else {
                result.append(c);
            }
        }
        result.append(".*"); //$NON-NLS-1$
        return result.toString();
    }

    /**
     * If there is no selection in the composite, set the selection to the
     * provided MatchingObject.
     * 
     * @param matchingObject
     *            the MatchingObject to select.
     */
    protected void setSelection() {
        StructuredSelection selection = (StructuredSelection) tableViewer
            .getSelection();
        if (selection.isEmpty()) {
            tableViewer.getTable().setSelection(0);
            handleSelectionChange();
        }
    }

    
    /**
     * @return the job
     */
    public ElementSelectionServiceJob getSelectionServiceJob() {
        return job;
    }
}
