/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.providers.internal.parsers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserEditStatus;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserEditStatus;
import org.eclipse.gmf.runtime.diagram.core.commands.SetPropertyCommand;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.notation.DescriptionStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Description parser
 * 
 * @author melaasar
 */
public class DescriptionParser implements IParser {
	/** instance of parser */
	static protected IParser instance = null;

	static final String DESC_DEFAULT_STR = StringStatics.BLANK;

	/**
	 * Constructor for DescriptionParser.
	 */
	protected DescriptionParser() {
		super();
	}

	/**
	 * Gets the instance of the parser
	 * @return IParser the single instance of the parser
	 */
	static public IParser getInstance() {
		if (instance == null) {
			instance = new DescriptionParser();
		}
		return instance;
	}

	/**
	 * @see com.ibm.xtools.common.services.parser.IParser#getEditString(IAdaptable, int)
	 */
	public String getEditString(IAdaptable adapter, int flags) {
		return getPrintString(adapter, flags);
	}

	/**
	 * @see com.ibm.xtools.common.services.parser.IParser#getParseCommand(IAdaptable, String, int)
	 */
	public ICommand getParseCommand(
		IAdaptable adapter,
		String newString,
		int flags) {
		return new SetPropertyCommand(adapter, Properties.ID_DESCRIPTION, Properties.TEXT, newString);
	}

	/**
	 * @see com.ibm.xtools.common.services.parser.IParser#getPrintString(IAdaptable, int)
	 */
	public String getPrintString(IAdaptable adapter, int flags) {
		final View view = (View) adapter.getAdapter(View.class);
		DescriptionStyle style = (DescriptionStyle) view.getStyle(NotationPackage.eINSTANCE.getDescriptionStyle());
		if (style != null) {
			String descString = style.getDescription();
			if (descString == null || descString.length() == 0)
				descString = DESC_DEFAULT_STR;
			return descString;
		}
		return StringStatics.BLANK;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.services.parser.IParser#isAffectingEvent(Object, int)
	 */
	public boolean isAffectingEvent(Object event, int flags) {
		if (event instanceof Notification) {
			Object feature = ((Notification) event).getFeature();
			if (feature == NotationPackage.eINSTANCE.getDescriptionStyle_Description()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.services.parser.IParser#isValidEditString(org.eclipse.core.runtime.IAdaptable, java.lang.String)
	 */
	public IParserEditStatus isValidEditString(IAdaptable element, String editString) {
		return ParserEditStatus.EDITABLE_STATUS;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.services.parser.IParser#getCompletionProcessor(org.eclipse.core.runtime.IAdaptable)
	 */
	public IContentAssistProcessor getCompletionProcessor(IAdaptable element) {
		return null;
	}
}
