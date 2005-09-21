/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.emf.type.ui.internal.providers;

import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.Platform;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.ui.internal.providers.ElementTypeIconProvider;
import org.eclipse.gmf.tests.runtime.emf.type.core.MetamodelTypeDescriptorTest;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

public class ElementTypeIconProviderTest
	extends TestCase {

	private ElementTypeIconProvider fixture;

	public ElementTypeIconProviderTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(MetamodelTypeDescriptorTest.class);
	}

	protected ElementTypeIconProvider getFixture() {
		return fixture;
	}

	protected void setFixture(ElementTypeIconProvider fixture) {
		this.fixture = fixture;
	}

	public void test_metamodelTypeConstructor() {

		Bundle bundle = Platform
			.getBundle("org.eclipse.gmf.tests.runtime.emf.type.ui"); //$NON-NLS-1$
		URL iconURL = bundle.getEntry("icons/icon.gif"); //$NON-NLS-1$
		ImageDescriptor iconImageDescriptor = ImageDescriptor
			.createFromURL(iconURL);

		setFixture(new ElementTypeIconProvider());

		IElementType iconType = ElementTypeRegistry.getInstance().getType(
			"org.eclipse.gmf.tests.runtime.emf.type.ui.iconType"); //$NON-NLS-1$
		Image icon = getFixture().getIcon(iconType, 0);

		assertEquals(iconImageDescriptor.getImageData(), icon.getImageData());
	}
}
