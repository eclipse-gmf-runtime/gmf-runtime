/******************************************************************************
 * Copyright (c) 2008, 2022 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.printing.render.dialogs;

import java.util.Locale;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.ColorSupported;
import javax.print.attribute.standard.PrinterName;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.l10n.DiagramUIPrintingMessages;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.model.PrintOptions;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A section of the options print dialog that handles the print color options.
 * 
 * @author James Bruck (jbruck)
 */
public class ColorBlock extends DialogBlock {

    private final DataBindingContext bindings;

    private final PrintOptions options;

    private Button colorRadio;

    private Button monoRadio;

    private Binding colorBinding;

    private Binding monoBinding;

    ColorBlock(IDialogUnitConverter dluConverter, DataBindingContext bindings, PrintOptions options) {
        super(dluConverter);
        this.bindings = bindings;
        this.options = options;
    }

    @Override
    public Control createContents(Composite parent) {
        final Realm realm = bindings.getValidationRealm();

        Composite result = group(parent, DiagramUIPrintingMessages.JPSOptionsDialog_Color);
        layout(result, 2);

        colorRadio = radio(result, DiagramUIPrintingMessages.JPSOptionsDialog_ChromaticityColor);
        layoutSpanHorizontal(colorRadio, 4);

        monoRadio = radio(result, DiagramUIPrintingMessages.JPSOptionsDialog_ChromaticityMonochrome);
        layoutSpanHorizontal(monoRadio, 4);

        colorBinding = bindings.bindValue(WidgetProperties.buttonSelection().observe(colorRadio),
                BeanProperties.value(PrintOptions.class, PrintOptions.PROPERTY_CHROMATICITY_COLOR).observe(realm, options), null, null);

        monoBinding = bindings.bindValue(WidgetProperties.buttonSelection().observe(monoRadio),
                BeanProperties.value(PrintOptions.class, PrintOptions.PROPERTY_CHROMATICITY_MONO).observe(realm, options), null, null);

        initializeControls(options.getDestination().getName());

        return result;
    }

    /**
     * Initialize the enabled state of the controls based on the printer capabilities.
     * 
     * @param printerName
     */
    private void initializeControls(String printerName) {
        AttributeSet attributes = new HashPrintServiceAttributeSet(new PrinterName(printerName, Locale.getDefault()));
        PrintService[] services = PrintServiceLookup.lookupPrintServices(DocFlavor.SERVICE_FORMATTED.PRINTABLE, attributes);
        PrintService printService = services[0];
        PrintServiceAttributeSet printServiceAttributes = printService.getAttributes();
        ColorSupported colorSupported = (ColorSupported) printServiceAttributes.get(ColorSupported.class);

        if (colorSupported == ColorSupported.SUPPORTED) {
            options.setChromaticityColor(true);
            options.setChromaticityMono(false);
            colorRadio.setEnabled(true);
        } else {
            options.setChromaticityColor(false);
            options.setChromaticityMono(true);
            colorRadio.setEnabled(false);
        }
    }

    @Override
    public void dispose() {
        bindings.removeBinding(colorBinding);
        colorBinding.dispose();
        bindings.removeBinding(monoBinding);
        monoBinding.dispose();
    }

}
