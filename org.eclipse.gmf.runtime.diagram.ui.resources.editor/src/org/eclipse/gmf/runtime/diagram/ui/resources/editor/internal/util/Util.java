/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.util;


import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.ui.util.DisplayUtils;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorPlugin;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

/**
 * @author qili
 * 
 */
public class Util {

    /**
     * The relative path of the elements and relationships icons.
     * It's relative to the icon subdirectory.
     *
     * I use PATH_SEPARATOR since we are not getting an os path.
     */
    protected static final String ELEMENTS_AND_RELATIONSHIPS_PATH = "elementsandrelationships" + StringStatics.PATH_SEPARATOR; //$NON-NLS-1$

    final static String PLUGIN_ID = "org.eclipse.gmf.runtime.diagram.ui.resources.editor"; //$NON-NLS-1$

    static public IProject getProject(IFile file) {
        return file.getProject();
    }

    static public IProject getProject(IEditorPart editor) {
        if (editor == null)
            return null;
        IEditorInput input = editor.getEditorInput();
        if (input == null)
            return null;
        IResource resource =
            (IResource) ((IAdaptable)input).getAdapter(IResource.class);
        if (resource == null)
            return null;
        IProject project = resource.getProject();
        return project;
    }

    static public void logWarning(String s) {
        Log.warning(EditorPlugin.getInstance(), IStatus.OK, s);
    }
    static public void logError(String s) {
        Log.error(EditorPlugin.getInstance(), IStatus.OK, s);
    }
    static public void logInfo(String s) {
        Log.info(EditorPlugin.getInstance(), IStatus.OK, s);
    }

    public static void reportException(CoreException e) {
        final IStatus status = e.getStatus();
        logError(status.getMessage());
        Display display = getStandardDisplay();
        display.asyncExec(new Runnable() {
            public void run() {
                ErrorDialog.openError(null, null, null, status);
            }
        });
    }

    /**
	 * Returns the standard display to be used. The method first checks, if
	 * the thread calling this method has an associated disaply. If so, this
	 * display is returned. Otherwise the method returns the default display.
	 */
	public static Display getStandardDisplay() {
		return DisplayUtils.getDisplay();
	}


    public static void reportException(
        Throwable e,
        final String title,
        String message,
        String pluginId) {
        if (e instanceof InvocationTargetException) {
            e = ((InvocationTargetException)e).getTargetException();
        }
        IStatus status = null;
        if (e instanceof CoreException) {
            reportException((CoreException)e);
            return;
        } else {
            if (message == null)
                message = e.getMessage();
            if (message == null)
                message = e.toString();
            status =
                new org.eclipse.core.runtime.Status(
                    IStatus.ERROR,
                    pluginId,
                    IStatus.OK,
                    message,
                    e);
        }
        logError(message);
        final IStatus fstatus = status;
        Display display = getStandardDisplay();
        display.asyncExec(new Runnable() {
            public void run() {
                ErrorDialog.openError(null, title, null, fstatus);
            }
        });
    }
    
    public static void reportException(
            Throwable e,
            final String title,
            String message) {
    	reportException(e, title, message, PLUGIN_ID);
    }
    
}

