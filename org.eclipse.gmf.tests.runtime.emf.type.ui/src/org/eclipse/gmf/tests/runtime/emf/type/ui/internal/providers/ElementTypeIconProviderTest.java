/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.tests.runtime.emf.type.ui.internal.providers;

import java.net.URL;
import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.Platform;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.ui.internal.providers.ElementTypeIconProvider;
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
		return new TestSuite(ElementTypeIconProviderTest.class);
	}

	protected ElementTypeIconProvider getFixture() {
		return fixture;
	}

	protected void setFixture(ElementTypeIconProvider fixture) {
		this.fixture = fixture;
	}

	public void test_getIcon() {

		Bundle bundle = Platform
			.getBundle("org.eclipse.gmf.tests.runtime.emf.type.ui"); //$NON-NLS-1$
		URL iconURL = bundle.getEntry("icons/icon.gif"); //$NON-NLS-1$
		ImageDescriptor iconImageDescriptor = ImageDescriptor
			.createFromURL(iconURL);
        Image icon = iconImageDescriptor.createImage();
        byte[] iconData = icon.getImageData().data;

		setFixture(new ElementTypeIconProvider());

		IElementType iconType = ElementTypeRegistry.getInstance().getType(
			"org.eclipse.gmf.tests.runtime.emf.type.ui.iconType"); //$NON-NLS-1$
		byte[] providerIconData = getFixture().getIcon(iconType, 0).getImageData().data;
 
		assertTrue(Arrays.equals(iconData, providerIconData));
        
        icon.dispose();
	}
	
	/**
	 * Tests that a platform URI can be specified as the location of an element
	 * type icon.
	 */
	public void test_getIconFromPlatformURI_144906() {

		Bundle bundle = Platform
			.getBundle("org.eclipse.gmf.runtime.diagram.ui.geoshapes"); //$NON-NLS-1$
		URL iconURL = bundle.getEntry("icons/IconCircle.gif"); //$NON-NLS-1$
		ImageDescriptor iconImageDescriptor = ImageDescriptor
			.createFromURL(iconURL);
        Image expectedIcon = iconImageDescriptor.createImage();
        byte[] iconData = expectedIcon.getImageData().data;

		setFixture(new ElementTypeIconProvider());

		IElementType iconType = ElementTypeRegistry.getInstance().getType(
			"org.eclipse.gmf.tests.runtime.emf.type.ui.platformIconType"); //$NON-NLS-1$
		Image actualIcon = getFixture().getIcon(iconType, 0);
		
		assertNotNull(actualIcon);
		
		byte[] providerIconData = actualIcon.getImageData().data;
 
		assertTrue(Arrays.equals(iconData, providerIconData));
        
		expectedIcon.dispose();
	}
}
