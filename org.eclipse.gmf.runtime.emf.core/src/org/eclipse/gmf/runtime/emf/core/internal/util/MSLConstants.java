/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.internal.util;

/**
 * Various internal constants.
 * 
 * @author rafikj
 */
public class MSLConstants {

	public final static String INVALID_EXTENSION = "@@@@@InvalidExtension@@@@@"; //$NON-NLS-1$

	public final static String INVALID_PATH = "@@@@@InvalidPath@@@@@." //$NON-NLS-1$
		+ INVALID_EXTENSION;

	public final static String EPHEMERAL_INVALID_EXTENSION = "@@@@@EphemeralInvalidExtension@@@@@"; //$NON-NLS-1$

	public final static String EPHEMERAL_INVALID_PATH = "@@@@@EphemeralInvalidPath@@@@@." //$NON-NLS-1$
		+ EPHEMERAL_INVALID_EXTENSION;

	public final static String EMPTY_STRING = ""; //$NON-NLS-1$

	public final static String CC_TOKEN = "xtools2_universal_type_manager"; //$NON-NLS-1$

	public final static char META_CLASS_BEGIN = '<';

	public final static char META_CLASS_END = '>';

	public final static char ID_SEPARATOR = '.';

	public final static char PATH_SEPARATOR = '/';

	public final static char REF_SEPARATOR = ',';

	public final static char FRAGMENT_SEPARATOR = '?';

	public final static char SCHEME_SEPARATOR = ':';

	public final static String QUALIFIED_NAME_SEPARATOR = "::"; //$NON-NLS-1$

	public final static String XMI_ENCODING = "UTF-8"; //$NON-NLS-1$

	public final static String PATH_MAP_SCHEME = "pathmap"; //$NON-NLS-1$

	public final static String PLATFORM_SCHEME = "platform"; //$NON-NLS-1$

	public final static String RESOURCE = "resource"; //$NON-NLS-1$

	public final static String PLUGIN = "plugin"; //$NON-NLS-1$

	public final static String FILE_SCHEME = "file"; //$NON-NLS-1$

	public final static int MAX_STACK_DEPTH = 2000;

	public final static int INTERVAL_FLUSH_COUNT = 1000;

	public final static Integer OUTPUT_BUFFER_SIZE = new Integer(256 * 1024);

	// The viz proxy hack tries to fix undo/redo viz scenarios.
	// If issues are uncovered, a better less hacky solution should be
	// developed. Setting the flag to false, will disable the hack.
	public static final boolean PROXY_HACK = true;

	// annotations
	public static final String SHORTCUT_ANNOTATION = "eObjectShortcut"; //$NON-NLS-1$
}