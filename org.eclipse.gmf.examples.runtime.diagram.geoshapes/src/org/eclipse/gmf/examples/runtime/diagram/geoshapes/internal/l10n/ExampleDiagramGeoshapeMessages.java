/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal.l10n;

import org.eclipse.osgi.util.NLS;

/**
 * An accessor class for externalized strings.
 *
 * @author cmahoney
 */
public final class ExampleDiagramGeoshapeMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal.l10n.ExampleDiagramGeoshapeMessages";//$NON-NLS-1$

	private ExampleDiagramGeoshapeMessages() {
		// Do not instantiate
	}

	public static String GEOVisualizer_DefaultGeoshapeDiagramFileName;
	public static String CreationWizard_New_Geoshape_Diagram;
	public static String GeoshapeWizardPage_Title;
	public static String GeoshapeWizardPage_Description;
	public static String GeoShapeGeneralDetails_GeoShapeDescriptionLabel_text;
	public static String GeoShapeGeneralDetails_GeoShapeDescriptionChangeCommand_text;

	static {
		NLS.initializeMessages(BUNDLE_NAME, ExampleDiagramGeoshapeMessages.class);
	}
}