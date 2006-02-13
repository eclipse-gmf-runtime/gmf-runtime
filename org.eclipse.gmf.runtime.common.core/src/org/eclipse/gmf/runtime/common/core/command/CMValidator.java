/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.common.core.command;

import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.gmf.runtime.common.core.internal.command.BaseModificationValidator;
import org.eclipse.gmf.runtime.common.core.internal.command.FileModificationApprover;


/**
 * A validator that verifies if the files being modified by a particular command
 * are editable. Calls validateedit() to bring up source control dialogs as
 * applicable.
 * 
 * @author dlander
 * @author ldamus
 * 
 * @deprecated Each GMF operation provides a list of {@link IFile}s that are expected to
 *             be modified when the operation is executed, undone or redone. An
 *             {@link FileModificationApprover} is registered with the
 *             {@link OperationHistoryFactory#getOperationHistory()} to validate
 *             the modification to these resources.
 * 
 * @canBeSeenBy %partners
 */
public class CMValidator {

	/**
	 * Validator responsible for doing actual validation on files
	 */
	private static IModificationValidator validator = new BaseModificationValidator();
	
	/**
	 * Checks whether a particular command can proceed based on status of units
	 * being modified.
	 * 
	 * @param command
	 *            The command that the user wants to execute.
	 * @return <code>true</code> if the <code>command</code>'s affected
	 *         objects can be modified, <code>false</code> otherwise.
	 */
    public boolean okToEdit(ICommand command) {
    	
    	HashSet collAffected;
    	
    	if (command.getAffectedObjects() == null) {
    		collAffected = new HashSet(); 
    	}
    	else {
    		collAffected = new HashSet(command.getAffectedObjects());
    	}
    	
    	/* We have a problem in which some people are using the affected objects
    	 * for stuff which is completely not what was intended.  (eg as their own private
    	 * storage spaced to store lists of views and whatever else they may want).
    	 * The get affected objects is used to determine if resources being modified are editable.
    	 * I check for that here and put something in the log.  
    	 */
    	Iterator it = collAffected.iterator();
    	
    	while (it.hasNext()) {
    		if (!(it.next() instanceof IFile)) {
    			it.remove();

    			// Log a stack trace for this.
        		//Thread.dumpStack();
    			// The strategy for notifying developers of non-IFile
    			// objects should be revisited for the next release. 
    			// We shouldn't be dumping a stack for the customer to see.

        		// DJL would like to use Log.info() but cannot change now due to CCB.
        		//Log.info(CommonCorePlugin.getDefault(), CommonCoreStatusCodes.OK, 
        		//	"Invalid Use of getAffectedObjects" + "Command Label" + this.getLabel()); //$NON-NLS-1$ //$NON-NLS-2$
    		}
    	}

    	IFile[] files = (IFile[]) collAffected.toArray(new IFile[0]);
    	
    	// perform validate edit on the files 
    	return okToEdit(files, command); 
    }
     
    
    /**
     * Validates that the given files can be modified using the Team 
     * validateEdit support.
     * @param files files that are to be modified; these files must all 
     * exist in the workspace.
     * @param command the command that the user wants to execute.
     * @return true if it is OK to edit the files.
     * @see org.eclipse.core.resources.IFileModificationValidator#validateEdit
     */
    private boolean okToEdit(IFile[] files, ICommand command) {
        boolean bRet = true;
    	IStatus status =
    		validator.validateEdit(files);
    	
    	if (!status.isOK() || command.involvesReadOnlyNonWorkSpaceFiles())
    		bRet = false;
    	       
        return bRet;
    }
    
    /**
     * Set the IModificationValidator
     * 
     * @param newValidator new IModificationValidator to use
     */
    public static void setValidator(IModificationValidator newValidator) {
    	validator = newValidator;
    }
}
