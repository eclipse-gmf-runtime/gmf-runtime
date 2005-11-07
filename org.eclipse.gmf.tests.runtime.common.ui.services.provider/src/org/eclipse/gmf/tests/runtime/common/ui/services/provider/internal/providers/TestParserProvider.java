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
package org.eclipse.gmf.tests.runtime.common.ui.services.provider.internal.providers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProviderChangeListener;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserEditStatus;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserProvider;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;

/**
 * Base test parser provider
 * 
 * @author wdiu, Wayne Diu
 */
public class TestParserProvider implements IParserProvider {

	public IParser getParser(IAdaptable hint) {
		return new IParser() {

			public String getEditString(IAdaptable element, int flags) {
				return StringStatics.BLANK;
			}

			public IParserEditStatus isValidEditString(IAdaptable element, String editString) {
				return null;
			}

			public ICommand getParseCommand(IAdaptable element, String newString, int flags) {
				return null;
			}

			public String getPrintString(IAdaptable element, int flags) {
				return StringStatics.BLANK;
			}

			public boolean isAffectingEvent(Object event, int flags) {
				return false;
			}

			public IContentAssistProcessor getCompletionProcessor(IAdaptable element) {
				return null;
			}
			
		};
	}

	public void addProviderChangeListener(IProviderChangeListener listener) {
		//does nothing
	}

	public boolean provides(IOperation operation) {
		return false;
	}

	public void removeProviderChangeListener(IProviderChangeListener listener) {
		//does nothing
	}

}
