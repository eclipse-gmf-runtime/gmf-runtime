/******************************************************************************
 * Copyright (c) 2007, 2022 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.label;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.TextCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.SharedImages;
import org.eclipse.gmf.runtime.diagram.ui.label.ILabelDelegate;
import org.eclipse.gmf.runtime.draw2d.ui.figures.LabelEx;
import org.eclipse.gmf.tests.runtime.diagram.ui.util.TestsPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class TextLabelPropertySection
    extends AbstractPropertySection {

    private static final String ICON = "Icon"; //$NON-NLS-1$

    private static final String LABEL_ALIGNMENT = "Label Alignment"; //$NON-NLS-1$

    private static final String ICON_ALIGNMENT = "Icon Alignment"; //$NON-NLS-1$

    private static final String TEXT_ALIGNMENT = "Text Alignment"; //$NON-NLS-1$

    private static final String TEXT_PLACEMENT = "Text Placement"; //$NON-NLS-1$

    private static final String TEXT_JUSTIFICATION = "Text Justification"; //$NON-NLS-1$

    private static final String TEXT_UNDERLINE = "Text Underline"; //$NON-NLS-1$

    private static final String TEXT_STRIKETHROUGH = "Text Strikethrough"; //$NON-NLS-1$

    private static final String TEXT_WRAP = "Text Wrapping"; //$NON-NLS-1$

    private static final String BIG_IMAGE_PATH = "images/test4.gif"; //$NON-NLS-1$

    private static Image bigIcon;

    // radio button widgets cache with a button as a value and abstract
    // label string as a key
    protected Map buttons = new HashMap();

    private class ButtonKey {

        String label;

        Object value;

        public ButtonKey(String label, Object value) {
            super();
            this.label = label;
            this.value = value;
        }

        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((label == null) ? 0
                : label.hashCode());
            result = prime * result + ((value == null) ? 0
                : value.hashCode());
            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final ButtonKey other = (ButtonKey) obj;
            if (label == null) {
                if (other.label != null)
                    return false;
            } else if (!label.equals(other.label))
                return false;
            if (value == null) {
                if (other.value != null)
                    return false;
            } else if (!value.equals(other.value))
                return false;
            return true;
        }

    }

    public void createControls(Composite parent,
            TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);
        initializeControls(parent);
    }

    /**
     * 
     * Sets up controls with proper layouts and groups
     * 
     * @param parent
     */
    private void initializeControls(Composite parent) {
        parent.setLayout(new GridLayout(1, false));

        // Create the groups for this section
        createTextJustificationGroup(parent);
        createTextPlacementGroup(parent);
        createLabelAlignmentGroup(parent);
        createIconAlignmentGroup(parent);
        createTextAlignmentGroup(parent);
        createIconGroup(parent);
        createTextUnderlineGroup(parent);
        createTextStrikethroughGroup(parent);
        createTextWrappingGroup(parent);
    }

    public void refresh() {
        super.refresh();

        // Deselect all the radio buttons;
        // the appropriate radio buttons will be properly
        // selected below
        for (Iterator i = buttons.keySet().iterator(); i.hasNext();) {
            Button radioButton = (Button) buttons.get(i.next());
            radioButton.setSelection(false);
        }

        // Update display from first textcompartment figure
        IGraphicalEditPart ep = (IGraphicalEditPart) getPrimarySelection();

        if (ep == null) {
            return;
        }

        enableLabelAlignmentButtons();

        checkButton(new ButtonKey(LABEL_ALIGNMENT, Integer.valueOf(
            getLabelAlignment(ep))));
        checkButton(new ButtonKey(ICON_ALIGNMENT, Integer.valueOf(
            getIconAlignment(ep))));
        checkButton(new ButtonKey(TEXT_ALIGNMENT, Integer.valueOf(
            getTextAlignment(ep))));
        checkButton(new ButtonKey(TEXT_PLACEMENT, Integer.valueOf(
            getTextPlacement(ep))));
        checkButton(new ButtonKey(TEXT_JUSTIFICATION, Integer.valueOf(
            getTextJustification(ep))));
        checkButton(new ButtonKey(TEXT_UNDERLINE, Boolean.valueOf(
            getTextUnderline(ep))));
        checkButton(new ButtonKey(TEXT_STRIKETHROUGH, Boolean.valueOf(
            getTextStrikethrough(ep))));
        checkButton(new ButtonKey(TEXT_WRAP, Boolean.valueOf(getWrappingOn(ep))));

    }

    private void enableLabelAlignmentButtons() {
        boolean enableCorners = true;
        for (Iterator iterator = getSelectedLabelEditParts().iterator(); iterator
            .hasNext();) {
            IGraphicalEditPart ep = (IGraphicalEditPart) iterator.next();
            if (ep.getFigure() instanceof OriginalWrapLabel
                || ep.getFigure() instanceof LabelEx) {
                enableCorners = false;
                break;
            }
        }

        Integer[] CORNERS = new Integer[] {
            Integer.valueOf(PositionConstants.TOP | PositionConstants.LEFT),
            Integer.valueOf(PositionConstants.TOP | PositionConstants.RIGHT),
            Integer.valueOf(PositionConstants.BOTTOM | PositionConstants.LEFT),
            Integer.valueOf(PositionConstants.BOTTOM | PositionConstants.RIGHT)};

        for (int i = 0; i < CORNERS.length; i++) {
            Button button = (Button) buttons.get(new ButtonKey(LABEL_ALIGNMENT,
                CORNERS[i]));
            if (button != null && !button.isDisposed()) {
                button.setEnabled(enableCorners);
            }
        }
    }

    private void checkButton(ButtonKey buttonKey) {
        Button button = (Button) buttons.get(buttonKey);
        if (button != null && !button.isDisposed()) {
            button.setSelection(true);
        }
    }

    private Group createGroup(Composite parent, String label, int size) {
        Group group = getWidgetFactory().createGroup(parent, label);
        group.setLayout(new GridLayout(size, true));
        GridData data = new GridData(GridData.FILL_BOTH);
        group.setLayoutData(data);
        return group;
    }

    private void createTextJustificationGroup(Composite parent) {
        String labels[] = new String[] {"LEFT", "RIGHT", "CENTER"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        int values[] = new int[] {PositionConstants.LEFT,
            PositionConstants.RIGHT, PositionConstants.CENTER};

        Group group = createGroup(parent, TEXT_JUSTIFICATION, labels.length);

        for (int i = 0; i < labels.length; i++) {
            String label = labels[i];
            final int value = values[i];

            Button radioButton = getWidgetFactory().createButton(group, label,
                SWT.RADIO);
            buttons.put(new ButtonKey(TEXT_JUSTIFICATION, Integer.valueOf(value)),
                radioButton);
            radioButton.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent event) {
                    setTextJustification(value);
                }
            });
        }
    }

    private void setTextJustification(int alignment) {
        for (Iterator iterator = getSelectedLabelEditParts().iterator(); iterator
            .hasNext();) {
            IGraphicalEditPart ep = (IGraphicalEditPart) iterator.next();

            ILabelDelegate label = (ILabelDelegate) ep
                .getAdapter(ILabelDelegate.class);
            if (label != null) {
                label.setTextJustification(alignment);
            }
        }
    }

    private int getTextJustification(IGraphicalEditPart ep) {
        ILabelDelegate label = (ILabelDelegate) ep
            .getAdapter(ILabelDelegate.class);
        if (label != null) {
            return label.getTextJustification();
        }
        return -1;
    }

    private void createTextAlignmentGroup(Composite parent) {
        String labels[] = new String[] {"LEFT", "RIGHT", "CENTER", "TOP", //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
            "BOTTOM"}; //$NON-NLS-1$
        int values[] = new int[] {PositionConstants.LEFT,
            PositionConstants.RIGHT, PositionConstants.CENTER,
            PositionConstants.TOP, PositionConstants.BOTTOM};

        Group group = createGroup(parent, TEXT_ALIGNMENT, labels.length);

        for (int i = 0; i < labels.length; i++) {
            String label = labels[i];
            final int value = values[i];

            Button radioButton = getWidgetFactory().createButton(group, label,
                SWT.RADIO);
            buttons.put(new ButtonKey(TEXT_ALIGNMENT, Integer.valueOf(value)),
                radioButton);
            radioButton.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent event) {
                    setTextAlignment(value);
                }
            });
        }
    }

    private void setTextAlignment(int alignment) {
        for (Iterator iterator = getSelectedLabelEditParts().iterator(); iterator
            .hasNext();) {
            IGraphicalEditPart ep = (IGraphicalEditPart) iterator.next();
            ILabelDelegate label = (ILabelDelegate) ep
                .getAdapter(ILabelDelegate.class);
            if (label != null) {
                label.setTextAlignment(alignment);
            }
        }
    }

    private int getTextAlignment(IGraphicalEditPart ep) {
        ILabelDelegate label = (ILabelDelegate) ep
            .getAdapter(ILabelDelegate.class);
        if (label != null) {
            return label.getTextAlignment();
        }
        return -1;
    }

    private void createIconAlignmentGroup(Composite parent) {
        String labels[] = new String[] {"LEFT", "RIGHT", "CENTER", "TOP",//$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
            "BOTTOM"};//$NON-NLS-1$ 
        int values[] = new int[] {PositionConstants.LEFT,
            PositionConstants.RIGHT, PositionConstants.CENTER,
            PositionConstants.TOP, PositionConstants.BOTTOM};

        Group group = createGroup(parent, ICON_ALIGNMENT, labels.length);

        for (int i = 0; i < labels.length; i++) {
            String label = labels[i];
            final int value = values[i];

            Button radioButton = getWidgetFactory().createButton(group, label,
                SWT.RADIO);
            buttons.put(new ButtonKey(ICON_ALIGNMENT, Integer.valueOf(value)),
                radioButton);
            radioButton.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent event) {
                    setIconAlignment(value);
                }
            });
        }
    }

    private void setIconAlignment(int alignment) {
        for (Iterator iterator = getSelectedLabelEditParts().iterator(); iterator
            .hasNext();) {
            IGraphicalEditPart ep = (IGraphicalEditPart) iterator.next();
            ILabelDelegate label = (ILabelDelegate) ep
                .getAdapter(ILabelDelegate.class);
            if (label != null) {
                label.setIconAlignment(alignment);
            }
        }
    }

    private int getIconAlignment(IGraphicalEditPart ep) {
        ILabelDelegate label = (ILabelDelegate) ep
            .getAdapter(ILabelDelegate.class);
        if (label != null) {
            return label.getIconAlignment();
        }
        return -1;
    }

    private void createLabelAlignmentGroup(Composite parent) {
        String labels[] = new String[] {"TOP | LEFT", "TOP", "TOP | RIGHT",//$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
            "RIGHT", "BOTTOM | RIGHT", "BOTTOM", "BOTTOM | LEFT", "LEFT",//$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
            "CENTER"};//$NON-NLS-1$ 
        int values[] = new int[] {
            PositionConstants.TOP | PositionConstants.LEFT,
            PositionConstants.TOP,
            PositionConstants.TOP | PositionConstants.RIGHT,
            PositionConstants.RIGHT,
            PositionConstants.BOTTOM | PositionConstants.RIGHT,
            PositionConstants.BOTTOM,
            PositionConstants.BOTTOM | PositionConstants.LEFT,
            PositionConstants.LEFT, PositionConstants.CENTER};

        Group group = createGroup(parent, LABEL_ALIGNMENT, labels.length);

        for (int i = 0; i < labels.length; i++) {
            String label = labels[i];
            final int value = values[i];

            Button radioButton = getWidgetFactory().createButton(group, label,
                SWT.RADIO);
            buttons.put(new ButtonKey(LABEL_ALIGNMENT, Integer.valueOf(value)),
                radioButton);
            radioButton.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent event) {
                    setLabelAlignment(value);
                }
            });
        }
    }

    private void setLabelAlignment(int alignment) {
        for (Iterator iterator = getSelectedLabelEditParts().iterator(); iterator
            .hasNext();) {
            IGraphicalEditPart ep = (IGraphicalEditPart) iterator.next();
            ILabelDelegate label = (ILabelDelegate) ep
                .getAdapter(ILabelDelegate.class);
            if (label != null) {
                label.setAlignment(alignment);
            }

        }
    }

    private int getLabelAlignment(IGraphicalEditPart ep) {
        ILabelDelegate label = (ILabelDelegate) ep
            .getAdapter(ILabelDelegate.class);
        if (label != null) {
            return label.getAlignment();
        }
        return -1;
    }

    private void createIconGroup(Composite parent) {
        String labels[] = new String[] {"none", "big", "small", "two", "three"};//$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$ 

        Group group = createGroup(parent, ICON, labels.length);

        for (int i = 0; i < labels.length; i++) {
            final String label = labels[i];

            Button radioButton = getWidgetFactory().createButton(group, label,
                SWT.RADIO);
            buttons.put(new ButtonKey(ICON, label), radioButton);
            radioButton.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent event) {
                    setIcon(label);
                }
            });
        }
    }

    private void setIcon(String iconDescription) {
        Image[] icons = new Image[3];
        if (iconDescription.equals("big")) {//$NON-NLS-1$ 
            if (bigIcon == null) {
                URL url = FileLocator.find(
                    TestsPlugin.getDefault().getBundle(), new Path(
                        BIG_IMAGE_PATH), null);
                bigIcon = ImageDescriptor.createFromURL(url).createImage();
            }
            icons[0] = bigIcon;
        } else if (iconDescription.equals("small")) {//$NON-NLS-1$
            icons[0] = SharedImages.get(SharedImages.IMG_NOTE);
        } else if (iconDescription.equals("two")) {//$NON-NLS-1$
            icons[0] = SharedImages.get(SharedImages.IMG_NOTE);
            icons[1] = SharedImages.get(SharedImages.IMG_NOTE);
        } else if (iconDescription.equals("three")) {//$NON-NLS-1$
            icons[0] = SharedImages.get(SharedImages.IMG_NOTE);
            icons[1] = SharedImages.get(SharedImages.IMG_NOTE);
            icons[2] = SharedImages.get(SharedImages.IMG_NOTE);
        }

        for (Iterator iterator = getSelectedLabelEditParts().iterator(); iterator
            .hasNext();) {
            IGraphicalEditPart ep = (IGraphicalEditPart) iterator.next();
            ILabelDelegate label = (ILabelDelegate) ep
                .getAdapter(ILabelDelegate.class);
            if (label != null) {
                for (int i = 0; i < icons.length; i++) {
                    label.setIcon(icons[i], i);
                }
            }
        }
    }

    private void createTextPlacementGroup(Composite parent) {
        String labels[] = new String[] {"EAST", "WEST", "NORTH", "SOUTH"};//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
        int values[] = new int[] {PositionConstants.EAST,
            PositionConstants.WEST, PositionConstants.NORTH,
            PositionConstants.SOUTH};

        Group group = createGroup(parent, TEXT_PLACEMENT, labels.length);

        for (int i = 0; i < labels.length; i++) {
            String label = labels[i];
            final int value = values[i];

            Button radioButton = getWidgetFactory().createButton(group, label,
                SWT.RADIO);
            buttons.put(new ButtonKey(TEXT_PLACEMENT, Integer.valueOf(value)),
                radioButton);
            radioButton.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent event) {
                    setTextPlacement(value);
                }
            });
        }
    }

    private void setTextPlacement(int placement) {
        for (Iterator iterator = getSelectedLabelEditParts().iterator(); iterator
            .hasNext();) {
            IGraphicalEditPart ep = (IGraphicalEditPart) iterator.next();
            ILabelDelegate label = (ILabelDelegate) ep
                .getAdapter(ILabelDelegate.class);
            if (label != null) {
                label.setTextPlacement(placement);
            }
        }
    }

    private int getTextPlacement(IGraphicalEditPart ep) {
        ILabelDelegate label = (ILabelDelegate) ep
            .getAdapter(ILabelDelegate.class);
        if (label != null) {
            return label.getTextPlacement();
        }
        return -1;
    }

    private void createTextUnderlineGroup(Composite parent) {
        String labels[] = new String[] {"on", //$NON-NLS-1$
            "off"}; //$NON-NLS-1$
        boolean values[] = new boolean[] {true, false};

        Group group = createGroup(parent, TEXT_UNDERLINE, labels.length);

        for (int i = 0; i < labels.length; i++) {
            String label = labels[i];
            final boolean value = values[i];

            Button radioButton = getWidgetFactory().createButton(group, label,
                SWT.RADIO);
            buttons.put(new ButtonKey(TEXT_UNDERLINE, Boolean.valueOf(value)),
                radioButton);
            radioButton.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent event) {
                    setTextUnderline(value);
                }
            });
        }
    }

    private void setTextUnderline(boolean underline) {
        for (Iterator iterator = getSelectedLabelEditParts().iterator(); iterator
            .hasNext();) {
            IGraphicalEditPart ep = (IGraphicalEditPart) iterator.next();
            ILabelDelegate label = (ILabelDelegate) ep
                .getAdapter(ILabelDelegate.class);
            if (label != null) {
                label.setTextUnderline(underline);
            }
        }
    }

    private boolean getTextUnderline(IGraphicalEditPart ep) {
        ILabelDelegate label = (ILabelDelegate) ep
            .getAdapter(ILabelDelegate.class);
        if (label != null) {
            return label.isTextUnderlined();
        }
        return false;
    }

    private void createTextStrikethroughGroup(Composite parent) {
        String labels[] = new String[] {"on", //$NON-NLS-1$
            "off"}; //$NON-NLS-1$
        boolean values[] = new boolean[] {true, false};

        Group group = createGroup(parent, TEXT_STRIKETHROUGH, labels.length);

        for (int i = 0; i < labels.length; i++) {
            String label = labels[i];
            final boolean value = values[i];

            Button radioButton = getWidgetFactory().createButton(group, label,
                SWT.RADIO);
            buttons.put(new ButtonKey(TEXT_STRIKETHROUGH, Boolean.valueOf(value)),
                radioButton);
            radioButton.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent event) {
                    setTextStrikethrough(value);
                }
            });
        }
    }

    private void setTextStrikethrough(boolean strikethrough) {
        for (Iterator iterator = getSelectedLabelEditParts().iterator(); iterator
            .hasNext();) {
            IGraphicalEditPart ep = (IGraphicalEditPart) iterator.next();
            ILabelDelegate label = (ILabelDelegate) ep
                .getAdapter(ILabelDelegate.class);
            if (label != null) {
                label.setTextStrikeThrough(strikethrough);
            }
        }
    }

    private boolean getTextStrikethrough(IGraphicalEditPart ep) {
        ILabelDelegate label = (ILabelDelegate) ep
            .getAdapter(ILabelDelegate.class);
        if (label != null) {
            return label.isTextStrikedThrough();
        }
        return false;
    }

    private void createTextWrappingGroup(Composite parent) {
        String labels[] = new String[] {"on", //$NON-NLS-1$
            "off"}; //$NON-NLS-1$
        boolean values[] = new boolean[] {true, false};

        Group group = createGroup(parent, TEXT_WRAP, labels.length);

        for (int i = 0; i < labels.length; i++) {
            String label = labels[i];
            final boolean value = values[i];

            Button radioButton = getWidgetFactory().createButton(group, label,
                SWT.RADIO);
            buttons.put(new ButtonKey(TEXT_WRAP, Boolean.valueOf(value)),
                radioButton);
            radioButton.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent event) {
                    setWrappingOn(value);
                }
            });
        }
    }

    private void setWrappingOn(boolean wrappingOn) {
        for (Iterator iterator = getSelectedLabelEditParts().iterator(); iterator
            .hasNext();) {
            IGraphicalEditPart ep = (IGraphicalEditPart) iterator.next();
            ILabelDelegate label = (ILabelDelegate) ep
                .getAdapter(ILabelDelegate.class);
            if (label != null) {
                label.setTextWrapOn(wrappingOn);
            }
        }
    }

    private boolean getWrappingOn(IGraphicalEditPart ep) {
        ILabelDelegate label = (ILabelDelegate) ep
            .getAdapter(ILabelDelegate.class);
        if (label != null) {
            return label.isTextWrapOn();
        }
        return false;
    }

    protected Object getPrimarySelection() {
        return (getSelectedLabelEditParts() != null
            && !getSelectedLabelEditParts().isEmpty() ? getSelectedLabelEditParts()
            .get(0)
            : null);
    }

    private List getSelectedLabelEditParts() {
        List textCompartmentEPs = new ArrayList();
        if (getSelection() != null && !getSelection().isEmpty()) {
            for (Iterator iterator = ((IStructuredSelection) getSelection())
                .iterator(); iterator.hasNext();) {
                IGraphicalEditPart shapeEP = (IGraphicalEditPart) iterator
                    .next();
                textCompartmentEPs
                    .addAll(getNestedTextCompartmentEditParts(shapeEP));
            }
        }
        return textCompartmentEPs;
    }

    private Collection getNestedTextCompartmentEditParts(
            IGraphicalEditPart containerEP) {
        HashSet textCompartmentEPs = new HashSet();
        for (Iterator iterator = containerEP.getChildren().iterator(); iterator
            .hasNext();) {
            IGraphicalEditPart childEP = (IGraphicalEditPart) iterator.next();
            if (childEP instanceof TextCompartmentEditPart) {
                textCompartmentEPs.add(childEP);
            } else {
                textCompartmentEPs
                    .addAll(getNestedTextCompartmentEditParts(childEP));
            }
        }
        return textCompartmentEPs;
    }
}
