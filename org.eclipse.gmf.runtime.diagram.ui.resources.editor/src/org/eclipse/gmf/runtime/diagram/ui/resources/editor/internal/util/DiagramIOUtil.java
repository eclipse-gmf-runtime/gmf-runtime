/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.util;

import java.io.InputStream;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.ClassNotFoundException;
import org.eclipse.emf.ecore.xmi.FeatureNotFoundException;
import org.eclipse.emf.ecore.xmi.PackageNotFoundException;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;

import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorPlugin;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.l10n.EditorResourceManager;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.edit.MResourceOption;
import org.eclipse.gmf.runtime.emf.core.exceptions.MSLRuntimeException;
import org.eclipse.gmf.runtime.notation.Diagram;

public class DiagramIOUtil {
	
	//	localized labels
	private static String TITLE_OPEN = EditorResourceManager
		.getI18NString("compatibility.title_open"); //$NON-NLS-1$

	private static String MESSAGE1_OPEN = EditorResourceManager
		.getI18NString("compatibility.message1_open"); //$NON-NLS-1$

	private static String MESSAGE2_OPEN = EditorResourceManager
		.getI18NString("compatibility.message2_open"); //$NON-NLS-1$

	private static String MESSAGE3_OPEN = EditorResourceManager
		.getI18NString("compatibility.message3_open"); //$NON-NLS-1$
	
	private static String UNABLE_TO_LOAD_DIAGRAM = EditorResourceManager.getI18NString("Diagram.UNABLE_TO_LOAD_RESOURCE"); //$NON-NLS-1$
	private static String NO_DIAGRAM_IN_RESOURCE = EditorResourceManager.getI18NString("Diagram.NO_DIAGRAM_IN_RESOURCE"); //$NON-NLS-1$

	private static String TITLE_SAVE = EditorResourceManager
	.getI18NString("compatibility.title_save"); //$NON-NLS-1$

	private static String MESSAGE1_SAVE = EditorResourceManager
	.getI18NString("compatibility.message1_save"); //$NON-NLS-1$

	private static String MESSAGE2_SAVE = EditorResourceManager
	.getI18NString("compatibility.message2_save"); //$NON-NLS-1$

	private static interface ILoader {
		public Resource load(MEditingDomain domain, int loadOptions, IProgressMonitor monitor) throws CoreException;
	}
	
	private static class FileLoader implements ILoader {
		private IFile fFile;
		public FileLoader(IFile file) {
			assert file != null;
			fFile = file;
		}
		
		public Resource load(MEditingDomain domain, int loadOptions, IProgressMonitor monitor) throws CoreException {
			fFile.refreshLocal(IResource.DEPTH_ZERO, monitor);
			String fileName = fFile.getLocation().toOSString();
			
			Resource resource = domain.loadResource(fileName);
			return resource;
		}
	}
	
	private static class StorageLoader implements ILoader {
		private IStorage fStorage;
		public StorageLoader(IStorage storage) {
			assert storage != null;
			fStorage = storage;
		}
		
		public Resource load(MEditingDomain domain, int loadOptions, IProgressMonitor monitor) throws CoreException {
			InputStream contents = fStorage.getContents();
			String storagePath = fStorage.getFullPath().toString();
			
			Resource resource = domain.loadResource(storagePath, loadOptions, contents);
			return resource;
		}
	}
	
	static public Diagram load(final MEditingDomain domain, final IFile file, boolean bTryCompatible, IProgressMonitor monitor) throws CoreException {
		FileLoader loader = new FileLoader(file);
		return load(domain, loader, bTryCompatible, monitor);
	}
	
	static public Diagram load(final MEditingDomain domain, final IStorage storage, boolean bTryCompatible, IProgressMonitor monitor) throws CoreException {
		ILoader loader = null;
		if(storage instanceof IFile) {
			loader = new FileLoader((IFile)storage);
		} else {
			loader = new StorageLoader(storage);
		}
		return load(domain, loader, bTryCompatible, monitor);
	}
	
	/**
	 * load an existing diagram file.
	 * 
	 * @param file
	 * @return
	 * @throws CoreException
	 */
	static private Diagram load(final MEditingDomain domain, final ILoader loader, boolean bTryCompatible, IProgressMonitor monitor) throws CoreException  {
		Resource notationModel = null;
		try {
			try {	
				// File exists with contents..
				notationModel = loader.load(domain, 0, monitor);
		     
			} catch (MSLRuntimeException e) {
				if (bTryCompatible) {
					Throwable t = e.getCause();
					
					Throwable causeError = t.getCause();
					if (causeError == null) {
						causeError = t;
					}
					String errMsg = causeError.getLocalizedMessage();
					if (causeError instanceof Resource.IOWrappedException) {
						Exception exc = ((Resource.IOWrappedException) causeError)
							.getWrappedException();
						if (exc != null) {
							causeError = exc;
						}
					}
					if ((causeError instanceof PackageNotFoundException 
							|| causeError instanceof ClassNotFoundException
							|| causeError instanceof FeatureNotFoundException)) {
						if (shouldLoadInCompatibilityMode(errMsg)) {
							notationModel = loader.load(domain, MResourceOption.COMPATIBILITY_MODE, monitor);
						} else {
							// user does not want to load in compatibility mode.
							return null; 
						}
					} else {
						throw new CoreException(new Status(IStatus.ERROR, EditorPlugin.getPluginId(), EditorStatusCodes.ERROR, UNABLE_TO_LOAD_DIAGRAM, causeError));
					}
				} else {
					throw e;
				}
			}
			if(notationModel == null)
				throw new RuntimeException(UNABLE_TO_LOAD_DIAGRAM);

			Iterator rootContents = notationModel.getContents().iterator();
			while(rootContents.hasNext()) {
				EObject rootElement = (EObject)rootContents.next();
				if(rootElement instanceof Diagram)
					return (Diagram)rootElement;
			}
			
			throw new RuntimeException(NO_DIAGRAM_IN_RESOURCE);
		} catch(Exception e) {
			Trace.catching(EditorPlugin.getInstance(), EditorDebugOptions.EXCEPTIONS_CATCHING, DiagramIOUtil.class, "load(IFile, boolean)", e); //$NON-NLS-1$
			CoreException thrownExcp = null;
			if(e instanceof CoreException) {
				thrownExcp = (CoreException)e;
			} else
				thrownExcp = new CoreException(new Status(IStatus.ERROR, EditorPlugin.getPluginId(), EditorStatusCodes.ERROR, e.getMessage(), e));
			Trace.throwing(EditorPlugin.getInstance(), EditorDebugOptions.EXCEPTIONS_THROWING, DiagramIOUtil.class, "load(IFile, boolean)", thrownExcp); //$NON-NLS-1$
			throw thrownExcp;
		}
	}

	static public void save(MEditingDomain domain, IFile file, Diagram diagram, boolean bOverwrite, boolean bKeepUnrecognizedData, IProgressMonitor progressMonitor) throws CoreException {
		int nOptions = 0;
		if(bOverwrite)
			nOptions = MResourceOption.OVERWRITE_READONLY;
		if(bKeepUnrecognizedData)
			nOptions |= MResourceOption.COMPATIBILITY_MODE;
        save(domain, file, diagram, progressMonitor, nOptions);
	}
	
	static public void save(MEditingDomain domain, IFile file, Diagram diagram, IProgressMonitor progressMonitor, int nOptions) throws CoreException {
        Resource notationModel = ((EObject)diagram).eResource();
        String fileName = file.getLocation().toOSString();
        
        domain.saveResourceAs(notationModel, fileName, nOptions);

		if (progressMonitor != null)		
			progressMonitor.done();
	}
	
	/**
	 * @param errMsg
	 * @return
	 */
	private static boolean shouldLoadInCompatibilityMode(String errMsg) {

		boolean bLoadAgain = false;

		// Check prefs to see how we handle compatibility issues.
		IPreferenceStore prefs = CommonUIPlugin.getDefault()
			.getPreferenceStore();
		// The pref is available on globalPreferancesPage.java
		prefs
			.setDefault(
				org.eclipse.gmf.runtime.common.ui.preferences.IPreferenceConstants.OPEN_UNRECOGNIZED_VERSIONS,
				MessageDialogWithToggle.PROMPT);
		String szOption = prefs
			.getString(org.eclipse.gmf.runtime.common.ui.preferences.IPreferenceConstants.OPEN_UNRECOGNIZED_VERSIONS);

		// Are we prompting the user?
		if (szOption == MessageDialogWithToggle.PROMPT) {

			// Prepare the message
			StringBuffer displayErrors = new StringBuffer();
			displayErrors.append(MESSAGE1_OPEN);
			displayErrors.append(StringStatics.PLATFORM_NEWLINE);

			if (errMsg != null && errMsg.trim().length() > 0) {
				displayErrors.append(StringStatics.PLATFORM_NEWLINE);
				displayErrors.append(errMsg);
			}

			displayErrors.append(StringStatics.PLATFORM_NEWLINE);
			displayErrors.append(StringStatics.PLATFORM_NEWLINE);
			displayErrors.append(MESSAGE2_OPEN);
			displayErrors.append(StringStatics.PLATFORM_NEWLINE);
			displayErrors.append(StringStatics.PLATFORM_NEWLINE);
			displayErrors.append(MESSAGE3_OPEN);

			// Show the message
			MessageDialogWithToggle dlg = MessageDialogWithToggle
				.openYesNoQuestion(Display.getDefault().getActiveShell(),
					TITLE_OPEN, displayErrors.toString(), null, false, null,
					null);

			// Respond to the user's decisions
			bLoadAgain = (dlg.getReturnCode() == IDialogConstants.YES_ID);

			//	More Responding to the user's decisions
			if (true == dlg.getToggleState()) {
				String state = MessageDialogWithToggle.ALWAYS;
				if (false == bLoadAgain) {
					state = MessageDialogWithToggle.NEVER;
				}
				prefs
					.setValue(
						org.eclipse.gmf.runtime.common.ui.preferences.IPreferenceConstants.OPEN_UNRECOGNIZED_VERSIONS,
						state);
			}
		} else {
			bLoadAgain = (szOption == MessageDialogWithToggle.ALWAYS);
		}

		return bLoadAgain;
	}
	
	public static void unload(MEditingDomain domain, Diagram diagram) {
		Resource resource = diagram.eResource();
		domain.unloadResource(resource);
	}

	public static boolean hasUnrecognizedData(Resource resource) {
		boolean bKeepUnrecognizedData = false;
		// Do we have any tags?
		if ((resource instanceof XMLResource)
			&& (false == ((XMLResource) resource).getEObjectToExtensionMap()
				.isEmpty())) {

			// Check prefs to see how we handle compatibility issues.
			// The pref is available on globalPreferancesPage.java which is in
			// presentation
			IPreferenceStore prefs = CommonUIPlugin.getDefault()
				.getPreferenceStore();
			prefs
				.setDefault(
					org.eclipse.gmf.runtime.common.ui.preferences.IPreferenceConstants.SAVE_UNRECOGNIZED_VERSIONS,
					MessageDialogWithToggle.PROMPT);
			String szOption = prefs
				.getString(org.eclipse.gmf.runtime.common.ui.preferences.IPreferenceConstants.SAVE_UNRECOGNIZED_VERSIONS);

			// Are we prompting the user?
			if (szOption == MessageDialogWithToggle.PROMPT) {

				// Prepare the message
				String display = MESSAGE1_SAVE + StringStatics.PLATFORM_NEWLINE
					+ StringStatics.PLATFORM_NEWLINE + MESSAGE2_SAVE;

				// Show the message
				MessageDialogWithToggle dlg = MessageDialogWithToggle
					.openYesNoQuestion(Display.getDefault().getActiveShell(),
						TITLE_SAVE, display, null, false, null, null);

				// Respond to the user's decisions
				bKeepUnrecognizedData = (dlg.getReturnCode() == IDialogConstants.YES_ID);

				//	More Responding to the user's decisions
				if (true == dlg.getToggleState()) {

					//Warnings say this is not used
					//String state = MessageDialogWithToggle.ALWAYS;
					//if (false == bKeepUnrecognizedData) {
					//	state = MessageDialogWithToggle.NEVER;
					//}
					prefs
						.setValue(
							org.eclipse.gmf.runtime.common.ui.preferences.IPreferenceConstants.SAVE_UNRECOGNIZED_VERSIONS,
							szOption);
				}
			} else if (szOption == MessageDialogWithToggle.ALWAYS) {
				bKeepUnrecognizedData = true;
			}
		}

		return bKeepUnrecognizedData;
	}
}

