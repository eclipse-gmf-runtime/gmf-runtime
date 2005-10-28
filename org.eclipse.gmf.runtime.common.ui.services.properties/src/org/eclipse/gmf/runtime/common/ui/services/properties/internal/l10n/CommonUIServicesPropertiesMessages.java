/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gmf.runtime.common.ui.services.properties.internal.l10n;

import org.eclipse.osgi.util.NLS;

public final class CommonUIServicesPropertiesMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.runtime.common.ui.services.properties.internal.l10n.CommonUIServicesPropertiesMessages";//$NON-NLS-1$

	private CommonUIServicesPropertiesMessages() {
		// Do not instantiate
	}

	public static String ExtendedColorPropertyLabelProvider_RGBValue;
	public static String ExtendedBooleanPropertyDescriptor_True;
	public static String ExtendedBooleanPropertyDescriptor_False;
	public static String ExtendedPropertyDescriptor_blank;
	public static String ExtendedTextPropertyDescriptor_PropertiesViewErrorDialog_Title;
	public static String PropertySource__ERROR__descriptorError;
	public static String CellValidatorFactory_InvalidIntegerFormat;
	public static String CellValidatorFactory_InvalidPositiveZeroInclusiveIntegerFormat;
	public static String CellValidatorFactory_InvalidPositiveZeroExclusiveIntegerFormat;
	public static String CellValidatorFactory_InvalidNegativeZeroInclusiveIntegerFormat;
	public static String CellValidatorFactory_InvalidNegativeZeroExclusiveIntegerFormat;
	public static String CellValidatorFactory_InvalidRealFormat;
	public static String CellValidatorFactory_InvalidByteFormat;
	public static String CellValidatorFactory_InvalidFloatFormat;
	public static String CellValidatorFactory_InvalidLongFormat;
	public static String CellValidatorFactory_InvalidShortFormat;
	public static String CellValidatorFactory_InvalidCharFormat;
	public static String PropertyPageCellEditor_PropertiesDialog_title;

	static {
		NLS.initializeMessages(BUNDLE_NAME, CommonUIServicesPropertiesMessages.class);
	}
}
