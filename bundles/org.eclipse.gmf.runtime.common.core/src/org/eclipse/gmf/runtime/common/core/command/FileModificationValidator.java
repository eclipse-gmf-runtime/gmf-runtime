/******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.core.command;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.gmf.runtime.common.core.internal.command.BaseModificationValidator;

/**
 * Static utility that approves the modification of files using an
 * {@link IModificationValidator}.
 * <P>
 * The modification validator can be set exactly once using
 * {@link #setModificationValidator(IModificationValidator)} before the first
 * request to validate files.
 * 
 * @author ldamus
 */
public class FileModificationValidator {

	private static IModificationValidator validator;

	private FileModificationValidator() {
		// not to be instantiated
	}

	/**
	 * Set the modification validator to be used to check that files can be
	 * modified.
	 * <P>
	 * This method may only be called once, and must be called before any
	 * request to validate files. Attempts to set the validator will be ignored
	 * after it has been already set, or after a default one has been created.
	 * 
	 * @param validator
	 *            the modification validator
	 */
	public static void setModificationValidator(IModificationValidator v) {
		if (validator == null) {
			validator = v;
		}
	}

	/**
	 * Gets the validator.
	 * 
	 * @return the validator
	 */
	private static IModificationValidator getValidator() {
		if (validator == null) {
			validator = new BaseModificationValidator();
		}
		return validator;
	}

	/**
	 * Checks that the <code>files</code> may be modified.
	 * 
	 * @return the approval status
	 */
	public static IStatus approveFileModification(IFile[] files) {
		return getValidator().validateEdit(files);
	}
	
	/**
	 * This interface works in conjuction with the {@link SyncExecHelper}
	 * to bridge entities knowing of UI and those with knowledge of editing domains.
	 * 
	 * @author James Bruck (jbruck@ca.ibm.com)
	 *
	 */
	public interface ISyncExecHelper {
		
		/**
		 * Will wrap the input runnable with one that is thread safe.
		 * 
		 * @param runnable
		 */
		public Runnable safeRunnable(Runnable runnable);
	}

	/**
	 * 
	 * Utility class that is used to bridge those entities that 
	 * have knowledge of UI and those that have knowledge of editing domains.
	 * Uses {@link ISyncExecHelper}.
	 * It is always initialized so we don't have to worry about null checking.
	 * 
	 * @author James Bruck (jbruck@ca.ibm.com)
	 */
	public static class SyncExecHelper implements ISyncExecHelper {

		private static ISyncExecHelper INSTANCE;
		static {
			SyncExecHelper.setInstance(new SyncExecHelper());
		}

		/**
		 * The result of this method is guaranteed to be 
		 * non-null since we initialize it with a default implementation.
		 * 
		 * @return the ISyncExecHelper instance
		 */
		public static synchronized ISyncExecHelper getInstance() {
			return INSTANCE;
		}
		
		private SyncExecHelper(){
			// not intended to be called externally
		}

		public static synchronized void setInstance(ISyncExecHelper instance) {
			SyncExecHelper.INSTANCE = instance;
		}

		/**
		 * Provides a default implementation. 
		 */
		public Runnable safeRunnable(Runnable runnable) {
			return runnable;
		}
	}

}
