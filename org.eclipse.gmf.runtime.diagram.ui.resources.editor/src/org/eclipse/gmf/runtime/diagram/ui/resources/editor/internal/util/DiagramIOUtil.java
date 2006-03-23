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

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.FeatureNotFoundException;
import org.eclipse.emf.ecore.xmi.PackageNotFoundException;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorPlugin;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.l10n.EditorMessages;
import org.eclipse.gmf.runtime.emf.core.edit.MResourceOption;
import org.eclipse.gmf.runtime.emf.core.resources.GMFResourceFactory;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.util.NotationExtendedMetaData;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;

public class DiagramIOUtil {
	
	// localized labels
	private static String TITLE_OPEN = EditorMessages.compatibility_title_open;

	private static String MESSAGE1_OPEN = EditorMessages.compatibility_message1_open;

	private static String MESSAGE2_OPEN = EditorMessages.compatibility_message2_open;

	private static String MESSAGE3_OPEN = EditorMessages.compatibility_message3_open;

	private static String UNABLE_TO_LOAD_DIAGRAM = EditorMessages.Diagram_UNABLE_TO_LOAD_RESOURCE;

	private static String NO_DIAGRAM_IN_RESOURCE = EditorMessages.Diagram_NO_DIAGRAM_IN_RESOURCE;

	private static String TITLE_SAVE = EditorMessages.compatibility_title_save;

	private static String MESSAGE1_SAVE = EditorMessages.compatibility_message1_save;

	private static String MESSAGE2_SAVE = EditorMessages.compatibility_message2_save;

	private static interface ILoader {
		public Resource load(TransactionalEditingDomain domain, int loadOptions, IProgressMonitor monitor) throws CoreException;
	}
	
	private static class FileLoader implements ILoader {
		private IFile fFile;
		public FileLoader(IFile file) {
			assert file != null;
			fFile = file;
		}
		
		public Resource load(TransactionalEditingDomain domain, int loadOptions, IProgressMonitor monitor) throws CoreException {
			fFile.refreshLocal(IResource.DEPTH_ZERO, monitor);
			URI uri = URI.createPlatformResourceURI(fFile.getFullPath()
                .toString(), true);
			
			Resource resource = domain.getResourceSet().getResource(uri, false);
			
			if (resource == null) {
				resource = domain.getResourceSet().createResource(uri);
			}
			
			if (!resource.isLoaded()) {
				Map loadingOptions = new HashMap(GMFResourceFactory.getDefaultLoadOptions());
				// We will place a special extended metadata in here to ensure that we can load diagrams
				//  from older versions of our metamodel.
				loadingOptions.put(XMLResource.OPTION_EXTENDED_META_DATA, new NotationExtendedMetaData());
				
				try {
					resource.load(loadingOptions);
				} catch (IOException e) {
					// Proceed with an unloaded resource.
				}
			}
			return resource;
		}
	}
	
	private static class StorageLoader implements ILoader {
		private IStorage fStorage;
		public StorageLoader(IStorage storage) {
			assert storage != null;
			fStorage = storage;
		}
		
		public Resource load(TransactionalEditingDomain editingDomain,
				int loadOptions, IProgressMonitor monitor)
			throws CoreException {
            
			String storagePath = fStorage.getFullPath().toString();
 
			Resource resource = editingDomain.getResourceSet().getResource(
				URI.createPlatformResourceURI(storagePath, true), true);
			return resource;
		}
	}
	
	static public Diagram load(final TransactionalEditingDomain domain, final IFile file, boolean bTryCompatible, IProgressMonitor monitor) throws CoreException {
		FileLoader loader = new FileLoader(file);
		return load(domain, loader, bTryCompatible, monitor);
	}
	
	static public Diagram load(final TransactionalEditingDomain domain, final IStorage storage, boolean bTryCompatible, IProgressMonitor monitor) throws CoreException {
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
	static private Diagram load(final TransactionalEditingDomain domain, final ILoader loader, boolean bTryCompatible, IProgressMonitor monitor) throws CoreException  {
		Resource notationModel = null;
		try {
			try {	
			// File exists with contents..
			notationModel = loader.load(domain, 0, monitor);

			} catch (Exception e) {
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

	static public void save(TransactionalEditingDomain domain, IFile file, Diagram diagram, boolean bKeepUnrecognizedData, IProgressMonitor progressMonitor) throws CoreException {
        Map options = new HashMap();
		int nOptions = 0;
		if(bKeepUnrecognizedData)
			nOptions |= MResourceOption.COMPATIBILITY_MODE;
        save(domain, file, diagram, progressMonitor, options);
	}
	
	static public void save(TransactionalEditingDomain domain, IFile file, Diagram diagram, IProgressMonitor progressMonitor, Map options) throws CoreException {
		Resource notationModel = ((EObject) diagram).eResource();
		String fileName = file.getFullPath().toOSString();
		notationModel.setURI(URI.createPlatformResourceURI(fileName, true));
		try {
			notationModel.save(options);
		} catch (IOException e) {
			throw new CoreException(new Status(IStatus.ERROR, EditorPlugin
				.getPluginId(), EditorStatusCodes.RESOURCE_FAILURE, e
				.getLocalizedMessage(), null));
		}

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
	
	public static void unload(TransactionalEditingDomain domain, Diagram diagram) {
		diagram.eResource().unload();
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

