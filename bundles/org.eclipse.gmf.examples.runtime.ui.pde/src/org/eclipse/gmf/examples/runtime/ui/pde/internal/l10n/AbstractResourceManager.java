/******************************************************************************
 * Copyright (c) 2002, 2010 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.ui.pde.internal.l10n;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.gmf.examples.runtime.ui.pde.internal.GmfExamplesDebugOptions;
import org.eclipse.gmf.examples.runtime.ui.pde.internal.GmfExamplesPlugin;
import org.eclipse.gmf.examples.runtime.ui.pde.internal.GmfExamplesStatusCodes;
import org.eclipse.gmf.examples.runtime.ui.pde.util.Log;
import org.eclipse.gmf.examples.runtime.ui.pde.util.StringStatics;
import org.eclipse.gmf.examples.runtime.ui.pde.util.Trace;

/**
 * <p>
 * AbstractResourceManager is an abstract class which is designed to encapsulate a behaviour 
 * of an object that provides a single point of access to the I18N and resource management
 * per logical cluster of Java packages. 
 * </p>
 * <p>
 * The logical cluster, though can be equivalent, but not limited to one per plug-in. 
 * For instance, a logical cluster in the plug-in devoted to UML diagrams can be
 * split into as many logical clusters, as there are types of diagrams in the plug-in.
 * That is - a plugin that is dedicated to collaboration and sequence diagrams, can be
 * split into two logical clusters, each requiring separate resource management -
 * one for collaboration diagram, and another for sequence diagram.
 * </p>
 * <p>
 * Each cluster should allocate a dedicated package for I18N and resource management. The
 * preferred convention is use i18n package suffix to designate such package. For example, 
 * com.ibm.diagrams.collaboration.l10n and com.ibm.diagrams.sequence.l10n are
 * designated packages for resource and I18N management.
 * </p>
 * <p>
 * Each cluster-oriented resource management package will have a single 
 * <code>org.eclipse.gmf.examples.ui.pde.internal.l10n.AbstractResourceManager</code> subclass. The subclass should
 * have a singleton instance and override, if necessary, the resource initialization method
 * <code>
 * 	initializeMessageResources()
 * </code>
 * to add the assignments of values to any value holding static variables declared in the 
 * subclass. The subclass can be designated to hold 
 * any resource and I18N related static variables if necessary, e.g. message resource bundle 
 * keys. 
 * 
 * The <code>initilaizeResources()</code> method should be overridden by subclasses in order to 
 * add/remove initialization of any additional/redundant resource types
 * </p>
 * <p>
 * The subclass should override if necessary the default names of the message resource bundle 
 * file (default MessageBundle) using the method:
 * <code>
 * 		getMessageBundleName()
 * </code>
 * </p>
 * <p>
 * On the file system side the convention is to store all resources associated with the given
 * cluster in the designated i18n package. That includes messages.properties.
 * For example, com.ibm.diagram.collaboration cluster will store
 * its resources here:
 * com/
 * 	rational/
 * 		diagram/
 * 			collaboration/
 * 				l10n/
 * 					 messages.properties
 * </p>
 * <p>
 * Synchronization aspects
 * 
 * The instances of this class are immutable, once created and initialized.
 * 
 * @see java.util.ResourceBundle
 * @author Natalia Balaba
 * @canBeSeenBy %partners
 */

public abstract class AbstractResourceManager {
	// --------------------------------------------------------------------//
	// ------------  STATIC VARIABLES BEGIN -------------------------------//
	// --------------------------------------------------------------------//

	// strings that used to compose default resource names

	private static final String MESSAGES = ".messages"; //$NON-NLS-1$

	private static final String MISSING_RESOURCE_MESSAGE = "Attempt to access missing resource ({0})."; //$NON-NLS-1$

	//
	// Resource bundle keys for localizable components of a list of items.
	//   Note that these strings are localized in the Common Core plug-in's
	//   message resource bundle, not in my subclass's plug-in's bundle!
	//
	/** Key for list separator. */
	static final String KEY_LIST_SEPARATOR = "list.separator"; //$NON-NLS-1$
	
	/** Ksy for list separator only. */
	static final String KEY_LIST_SEPARATOR_ONLY = "list.separator.only"; //$NON-NLS-1$
	
	/** Key for first list separator. */
	static final String KEY_LIST_SEPARATOR_FIRST = "list.separator.first"; //$NON-NLS-1$
	
	/** Key for last list separator. */
	static final String KEY_LIST_SEPARATOR_LAST = "list.separator.last"; //$NON-NLS-1$
	
	/** Key for list prefix. */
	static final String KEY_LIST_PREFIX = "list.prefix"; //$NON-NLS-1$
	
	/** Key for list suffix. */
	static final String KEY_LIST_SUFFIX = "list.suffix"; //$NON-NLS-1$
	
	/** Key for default list separator. */
	static final String DEFAULT_LIST_SEPARATOR = ", "; //$NON-NLS-1$
	
	/** Key for default list prefix. */
	static final String DEFAULT_LIST_PREFIX = ""; //$NON-NLS-1$
	
	/** Key for default list suffix. */
	static final String DEFAULT_LIST_SUFFIX = ""; //$NON-NLS-1$

	// --------------------------------------------------------------------//
	// ------------  STATIC VARIABLES END ---------------------------------//
	// --------------------------------------------------------------------//

	// --------------------------------------------------------------------//
	// ------------  INSTANCE VARIABLES BEGIN -----------------------------//
	// --------------------------------------------------------------------//

	/**
	 *  a resource bundle that stores I18N message resources
	 */
	private ResourceBundle messagesBundle = null;

	/*
	 * the strings that point to the names and locations of the resources.
	 * subclasses must override if name of the messages bundle is different
	 * from the default
	 */

	/**
	 * The name of the messages bundle. The default is "messages"
	 */
	private String messagesBundleName = null;

	// --------------------------------------------------------------------//
	// ------------  INSTANCE VARIABLES END  ------------------------------//
	// --------------------------------------------------------------------//

	// --------------------------------------------------------------------//
	// ------------  CONSTRUCTORS BEGIN   ---------------------------------//
	// --------------------------------------------------------------------//

	/**
	 * Create a resource manager instance and initialize resources it will manage.
	 * Subclasses should be declared final and have a singleton instance. If the
	 * name of the messages bundle is different from the default the subclasses
	 * should override getMessagesBundleDefaultName()
	 */
	protected AbstractResourceManager() {
		super();
		messagesBundleName = getMessagesBundleDefaultName();

		initializeResources();
	}

	// --------------------------------------------------------------------//
	// ------------  CONSTRUCTORS BEGIN   ---------------------------------//
	// --------------------------------------------------------------------//

	// --------------------------------------------------------------------//
	// ------------  INSTANCE METHODS BEGIN  ------------------------------//
	// --------------------------------------------------------------------//

	/**
	 * Returns the messageBundle.
	 * Resource bundles contain locale-specific objects - text, numbers, etc.
	 * @return the message bundle
	 */
	protected ResourceBundle getMessagesBundle() {
		return messagesBundle;
	}

	/**
	 * Returns a package name of the class of this object
	 * @return - the full name if the client resource package
	 */
	protected String getPackageName() {
		return getClass().getPackage().getName();
	}

	/**
	 * Returns the name of the messages bundle, including the package path.
	 * E.g. for MessageBundle.properties file located at 
	 * com.ibm.diagrams.collaboration.l10n
	 * the name returned will be com.ibm.diagrams.collaboration.l10n.MessageBundle
	 * @return - messages bundle name
	 */
	protected String getMessagesBundleName() {
		return messagesBundleName;
	}

	/**
	 * Returns the plugin that hosts the resource manager
	 * @return Plugin the plugin that hosts the resource manager
	 */
	protected abstract Plugin getPlugin();

	/**
	 * Load various resources. Do nothing by default.
	 * Subclasses should override this method to include initialization of the 
	 * particular resource types. 
	 */
	protected abstract void initializeResources();

	/**
	 * Populate messageBundle with text related resources from the MessageBundle 
	 * properties file. 
	 * This method provides  single assignment point to the private variable messagesBundle.
	 * To override default initialization subclasses should override createMessagesBundle()
	 * @see #createMessagesBundle()
	 */
	protected void initializeMessageResources() {
		messagesBundle = createMessagesBundle();
	}

	/**
	* Load messages resource bundle.
	* 
	* If resource bundle is missing creates an instance of EmptyResourceBundle and
	* returns that as a default value
	* @return - messages resource bundle
	*/

	protected ResourceBundle createMessagesBundle() {
		try {
			return ResourceBundle.getBundle(
				getMessagesBundleName(),
				Locale.getDefault(),
				getClass().getClassLoader());
		} catch (MissingResourceException mre) {
			Trace.catching(GmfExamplesPlugin.getDefault(), GmfExamplesDebugOptions.EXCEPTIONS_CATCHING, getClass(), "createMessagesBundle", mre); //$NON-NLS-1$
			Log.error(GmfExamplesPlugin.getDefault(), GmfExamplesStatusCodes.L10N_FAILURE, "createMessagesBundle", mre); //$NON-NLS-1$
			return new EmptyResourceBundle(getMessagesBundleName());
		}

	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 * @return - value for the given key or the key if value
	 * @param key java.lang.String the key to retrieve the value
	 */
	public String getString(String key) {
		return getString(key, key);
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or  defaultValue if not found.
	 * @return - value for the given key or the suuplied 
	 * 							   default if value was not found
	 * @param key java.lang.String the key to retrieve the value
	 * @param defaultValue java.lang.String the default value to return
	 * 											if no value by the given key was
	 * 											found
	 */
	public String getString(String key, String defaultValue) {
		try {
			return getMessagesBundle().getString(key);
		} catch (MissingResourceException mre) {
			Trace.catching(GmfExamplesPlugin.getDefault(), GmfExamplesDebugOptions.EXCEPTIONS_CATCHING, getClass(), "getString", mre); //$NON-NLS-1$
			Log.warning(
				GmfExamplesPlugin.getDefault(),
				GmfExamplesStatusCodes.L10N_FAILURE,
				MessageFormat.format(
					MISSING_RESOURCE_MESSAGE,
					new Object[] { key }),
				mre);

			return defaultValue;
		}
	}

	/**
	 * Creates a localized, parameterized message from the specified pattern
	 * in the resource bundle.
	 * 
	 * @param patternKey resource bundle key of the message pattern
	 * @param args objects to substitute into the <tt>{0}</tt>, <tt>{1}</tt>,
	 *     etc. parameters in the message pattern
	 * @return the formatted message
	 * 
	 * @see MessageFormat
	 */
	public String formatMessage(String patternKey, Object[] args) {
		final String pattern = getString(patternKey);
		
		try {
			return MessageFormat.format(pattern, args);
		} catch (Exception e) {
			// formats may throw IllegalArgumentExceptions and others
			Trace.catching(
					getPlugin(),
					GmfExamplesDebugOptions.EXCEPTIONS_CATCHING,
					ResourceManager.class,
					"messageFormat", //$NON-NLS-1$
					e);
			
			return pattern;  // better than nothing?
		}
	}

	/**
	 * Formats an array of strings according to the conventions of the locale.
	 * For example, in English locales, the result is a comma-separated list
	 * with "and" preceding the last item (no commas if there are only two
	 * items).  The entry in a singleton array is returned as is.
	 * 
	 * @param strings an array of strings to format into a list
	 * @return the list, <code>strings[0]</code> if there is only one element,
	 *    or <code>""</code> if the array has no elements
	 */
	public String formatList(String[] strings) {
		return formatList(java.util.Arrays.asList(strings));
	}

	/**
	 * <p>
	 * Formats a collection of objects according to the conventions of the
	 * locale.
	 * For example, in English locales, the result is a comma-separated list
	 * with "and" preceding the last item (no commas if there are only two
	 * items).
	 * </p>
	 * <p>
	 * The individual elements of the collection are converted to strings using
	 * the {@link String#valueOf(java.lang.Object)} method.
	 * </p>
	 * 
	 * @param items an array of objects to format into a list
	 * @return the list, <code>strings[0]</code> if there is only one element,
	 *    or <code>""</code> if the array has no elements
	 */
	public String formatList(Collection items) {
		switch (items.size()) {
			case 0 :
				return StringStatics.BLANK;
			case 1 :
				return String.valueOf(items.iterator().next());
			case 2 :
				return formatPair(ResourceManager.getInstance(), items);
			default :
				return formatList(ResourceManager.getInstance(), items);
		}
	}

	/**
	 * Helper method to format a two-item list (which in some locales looks
	 * different from a list of more than two items).
	 * 
	 * @param mgr the common core plug-in's resource manager, which is used to
	 *     retrieve the localized components of a list
	 * @param items the pair of items (must be exactly two)
	 * @return the pair as a string
	 * 
	 * @see #formatList(Collection)
	 */
	private String formatPair(AbstractResourceManager mgr, Collection items) {
		Iterator iter = items.iterator();

		StringBuffer result = new StringBuffer(32);

		result.append(iter.next());

		result.append(mgr.getString(
						KEY_LIST_SEPARATOR_ONLY,
						mgr.getString(
								KEY_LIST_SEPARATOR,
								DEFAULT_LIST_SEPARATOR)));

		result.append(iter.next());

		return result.toString();
	}

	/**
	 * Helper method to format a list of more than two items.
	 * 
	 * @param mgr the common core plug-in's resource manager, which is used to
	 *     retrieve the localized components of a list
	 * @param items the list of items (must be more than two)
	 * @return the list as a string
	 * 
	 * @see #formatList(Collection)
	 */
	private String formatList(AbstractResourceManager mgr, Collection items) {
		Iterator iter = items.iterator();
		int max = items.size() - 1;

		final String sep = mgr.getString(
				KEY_LIST_SEPARATOR,
				DEFAULT_LIST_SEPARATOR);

		StringBuffer result = new StringBuffer(32);

		result.append(mgr.getString(KEY_LIST_PREFIX, DEFAULT_LIST_PREFIX));

		for (int i = 0; i <= max; i++) {
			if (i == 1) {
				result.append(mgr.getString(KEY_LIST_SEPARATOR_FIRST, sep));
			} else if (i == max) {
				result.append(mgr.getString(KEY_LIST_SEPARATOR_LAST, sep));
			} else if (i > 1) {
				result.append(sep);
			}

			result.append(iter.next());
		}

		result.append(mgr.getString(KEY_LIST_SUFFIX, DEFAULT_LIST_SUFFIX));

		return result.toString();
	}

	/**
	 * Returns default name for the messages bundle. Subclasses should override 
	 * if the messages bundle name differs from the default
	 * @return - default name for the messages bundle
	 */
	protected String getMessagesBundleDefaultName() {
		return getPackageName() + MESSAGES;
	}



	// --------------------------------------------------------------------//
	// ------------  INSTANCE METHODS END  --------------------------------//
	// --------------------------------------------------------------------//

}
