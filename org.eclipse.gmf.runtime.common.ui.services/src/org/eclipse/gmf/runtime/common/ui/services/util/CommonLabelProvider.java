/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.util;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;

import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.ui.services.icon.IconOptions;
import org.eclipse.gmf.runtime.common.ui.services.icon.IconService;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserOptions;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserService;

/**
 * Label provider that delegates to the Icon Service
 * and the Parser Service for the images and text it provides.
 * 
 * @author ldamus
 *
 */
public class CommonLabelProvider
	extends DecoratingLabelProvider {
	
	/**
	 * A label provider which uses the icon and parser service to
	 * get the labels.
	 */
	private static class MyDelegatingLabelProvider extends LabelProvider {
		
		private int parserOptions = ParserOptions.NONE.intValue();
		private int iconOptions = IconOptions.NONE.intValue();

		public Image getImage(Object element) {

			if ((element instanceof IStructuredSelection)) {
				IStructuredSelection ss = (IStructuredSelection) element;
				if (ss.size() == 1) {
					element = ss.getFirstElement();
				}
			}
			
			if (!(element instanceof IAdaptable)) {
				return null;
			}
			
			return IconService.getInstance().getIcon((IAdaptable) element, iconOptions);
		}

		public String getText(Object element) {

			if ((element instanceof IStructuredSelection)) {
				IStructuredSelection ss = (IStructuredSelection) element;
				if (ss.size() == 1) {
					element = ss.getFirstElement();
				}
			}
			
			if (!(element instanceof IAdaptable)) {
				return StringStatics.BLANK;
			}
			
			return ParserService.getInstance().getPrintString(
				(IAdaptable) element, parserOptions);
		}
		
		/**
		 * Sets parser options.
		 * 
		 * @param options parser option
		 */
		public void setParserOptions(int options) {
			this.parserOptions = options;
		}
		
		/**
		 * Sets icon options.
		 * 
		 * @param options icon options
		 */
		public void setIconOptions(int options) {
			this.iconOptions = options;
		}
	};
	
	/**
	 * Constructors a new label provider instance.
	 */
	public CommonLabelProvider() {
		super(
			new MyDelegatingLabelProvider(),
			PlatformUI
				.getWorkbench()
				.getDecoratorManager()
				.getLabelDecorator());
	}
	
	/**
	 * Sets the parser options.
	 * @param options parser options
	 */
	public void setParserOptions(int options) {
		((MyDelegatingLabelProvider) getLabelProvider()).setParserOptions(options);
	}
	
	/**
	 * Sets the icon options.
	 * @param options icon options
	 */
	public void setIconOptions(int options) {
		((MyDelegatingLabelProvider) getLabelProvider()).setIconOptions(options);
	}
	
}
