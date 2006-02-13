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


package org.eclipse.gmf.runtime.emf.ui.internal.validation;

import java.text.MessageFormat;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.ui.util.ConsoleUtil;
import org.eclipse.gmf.runtime.emf.ui.internal.MslUIPlugin;
import org.eclipse.gmf.runtime.emf.ui.internal.l10n.EMFUIMessages;
import org.eclipse.gmf.runtime.emf.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.emf.ui.preferences.ValidationLiveProblemsDestination;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IValidationListener;
import org.eclipse.emf.validation.service.ValidationEvent;


/**
 * The MSL UI's live validation listener is responsible for showing the
 * error/warning dialog or console output, according to the user's preference
 * settings.
 *
 * @author Christian W. Damus (cdamus)
 */
public class LiveValidationListener
	implements IValidationListener {

    /**
     * Helper object for creating message to output view.
     */
    private final OutputUtility outputUtility;

	/**
	 * Initializes me.
	 */
	public LiveValidationListener() {
		this.outputUtility = new OutputUtility();
	}

	/* (non-Javadoc)
	 * Implements the interface method.
	 */
	public void validationOccurred(ValidationEvent event) {
		
	    if ((event.getEvaluationMode() == EvaluationMode.LIVE) &&
	    		(event.getSeverity() >= IStatus.WARNING)) {
	        showProblemMessages(event);
	    }
	}

    /**
     * Dispays any problem messages from live validation on the output view.
     * If there are any messages, the view is brought forth (if it is open).
     * 
     * @param event the live validation occurred event
     */
    private void showProblemMessages(ValidationEvent event) {
        final ValidationLiveProblemsDestination destination =
        	ValidationLiveProblemsDestination.getPreferenceSetting();
        final boolean warningsInDialog =
        	MslUIPlugin.getDefault().getPluginPreferences().getBoolean(
        		IPreferenceConstants.VALIDATION_LIVE_WARNINGS_IN_DIALOG);
        final String messages = getProblemMessages(event);
        
    	if (destination == ValidationLiveProblemsDestination.CONSOLE
    			|| (!getOutputUtility().hasErrors() && !warningsInDialog)) {
            if (messages.length() > 0) {
            	
	            ConsoleUtil.println(EMFUIMessages.Validation_outputProviderCategory, EMFUIMessages.Validation_problems);
	            ConsoleUtil.println(EMFUIMessages.Validation_outputProviderCategory, messages);
        	}

            final boolean showConsole =
            	MslUIPlugin.getDefault().getPluginPreferences().getBoolean(
            		IPreferenceConstants.VALIDATION_LIVE_SHOW_CONSOLE);
            
            if (getOutputUtility().hasProblems()) {
            	if (showConsole) {            		
            		ConsoleUtil.showConsole( EMFUIMessages.Validation_outputProviderCategory);
            	}
            }
        } else if (destination == ValidationLiveProblemsDestination.DIALOG) {
    		showLiveValidationDialog(event);
    	}
    }
    
    /**
     * Shows the specified <code>status</code>'s children in the "Details"
     * area of an information dialog.
     * 
     * @param event the live validation occurred event
     */
    private void showLiveValidationDialog(final ValidationEvent event) {
    	Display.getCurrent().syncExec(new Runnable() {
            public void run() {
                IWorkbenchWindow workbenchWindow =
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                if (workbenchWindow != null) {
                	IStatus[] details = toStatusArray(event);
                	
                	String message = event.getSeverity() >= IStatus.ERROR
						? EMFUIMessages.Validation_liveError
						: EMFUIMessages.Validation_liveWarning_part1
			    		+ "\n\n"	+ EMFUIMessages.Validation_liveWarning_part2; //$NON-NLS-1$
                	
                	// the dialog should show INFO severity for errors because
                	//   the corrective action has already been taken by the
                	//   system; the user is just being informed that everything
                	//   is still OK.  Warnings, however, require corrective
                	//   action by the user.
                	final int dialogSeverity = event.matches(IStatus.WARNING)
						? IStatus.WARNING
						: IStatus.INFO;
                	
                	// get the first of the most severe error messages and use
                	//   it in the summary message presented to the user
                	IStatus primary = getFirstStatus(details, event.getSeverity());
                	
                	IStatus toDisplay;
					
					if (details.length > 1) {
						toDisplay = new MultiStatus(
		                		MslUIPlugin.getPluginId(),
								0,
								details,
								primary.getMessage(),
								null) {
							
							/**
							 * Redefines the inherited method to always return
							 * the more appropriate severity for the dialog.
							 */
							public int getSeverity() {
								return dialogSeverity;
							}};
					} else {
						toDisplay = new Status(
							dialogSeverity,
							primary.getPlugin(),
							primary.getCode(),
							primary.getMessage(),
							primary.getException());
					}
                	
					new LiveValidationDialog(
                		Display.getCurrent().getActiveShell(),
                		EMFUIMessages.Validation_liveDialogTitle,
						message,
						toDisplay).open();
                }
            }});
    }

    /**
     * Composes the string message to display based on the count of the various
     * types of problems in the <code>status</code>.
     * 
     * @param event the live validation occurred event
     * @return	A formulated message string.
     */
    private String getProblemMessages(ValidationEvent event) {
        StringBuffer buffer = new StringBuffer();

        getOutputUtility().appendProblems(event, buffer);

        return buffer.toString();
    }
    
    /**
     * Obtains the first in an array of <code>statuses</code> that has the
     * specified <code>severity</code>.
     * 
     * @param statuses an array of statuses.  Must not be <code>null</code>
     *     or a zero-length array
     * @param severity the severity to look for
     * @return the first status found with this <code>severity</code>, or the
     *     first in the array if none is found
     */
    static IStatus getFirstStatus(IStatus[] statuses, int severity) {
    	for (int i = 0; i < statuses.length; i++) {
			if (statuses[i].matches(severity)) {
				return statuses[i];
			}
		}
    	
    	return statuses[0];
    }
    
    /**
     * Converts a validation event to an array of statuses.
     * 
     * @param event the validation event
     * @return its validation results, as a status array
     */
    private static IStatus[] toStatusArray(ValidationEvent event) {
    	List results = event.getValidationResults();
    	
    	return (IStatus[]) results.toArray(
    		new IStatus[results.size()]);
    }

    /**
     * Accessor for my helpful output utility.
     * 
     * @return my output utility
     */
    private OutputUtility getOutputUtility() {
        return outputUtility;
    }

    /**
     * Inner class that helps in the production of an output view message when
     * live validation encounters problems.
     *
     * @author Christian W. Damus (cdamus)
     */
    private static class OutputUtility {
        /** Whether the last status that I processed had errors. */
        private boolean hasErrors = false;

        /** Whether the last status that I processed had problems. */
        private boolean hasProblems = false;

        /**
         * Appends the problems contained within the specified
         * <code>status</code> collection to the specified <code>output</code>
         * buffer.
         * 
         * @param event the live validation occurred event
         * @param output the output
         */
        void appendProblems(
            ValidationEvent event,
            StringBuffer output) {
            hasProblems = false;
            hasErrors = false;

            appendProblemsRecursive(
                toStatusArray(event),
                output);

            if (hasErrors()) {
                output.append(EMFUIMessages.Validation_rollback);
                output.append(StringStatics.PLATFORM_NEWLINE);
            }
        }

        /**
         * Queries whether any errors were found in the last processing of
         * validation status.
         * 
         * @return whether any errors were found
         */
        boolean hasErrors() {
            return hasErrors;
        }

        /**
         * Queries whether any problems were found in the last processing of
         * validation status.
         * 
         * @return whether any problems (errors or warnings) were found
         */
        boolean hasProblems() {
            return hasProblems;
        }

        // private helper to appendProblems() that can be called recursively
        private void appendProblemsRecursive(
            IStatus[] statuses,
            StringBuffer output) {
            for (int i = 0; i < statuses.length; i++) {
                IStatus next = statuses[i];

                if (!next.isOK()) {
                    final String messagePattern;

                    switch (next.getSeverity()) {
                        case IStatus.ERROR :
                            hasProblems = true;
                            hasErrors = true;
                            messagePattern = EMFUIMessages.Validation_error;
                            break;
                        case IStatus.WARNING :
                            hasProblems = true;
                            messagePattern = EMFUIMessages.Validation_warn;
                            break;
                        default :
                            messagePattern = EMFUIMessages.Validation_note;
                            break;
                    }

                    output.append(
                        MessageFormat.format(
                            messagePattern,
                            new Object[] { next.getMessage()}));
                    output.append(StringStatics.PLATFORM_NEWLINE);
                }

                if (next.isMultiStatus()) {
                    appendProblemsRecursive(
                        next.getChildren(),
                        output);
                }
            }
        }
    }

    /**
	 * A specialized error dialog that includes, when warnings or errors are
	 * displayed, a check box to suppress the dialog in the future.
	 *
	 * @author Christian W. Damus (cdamus)
	 */
	private static class LiveValidationDialog
		extends ErrorDialog {

		private final IStatus status;

		/**
		 * Initializes me.
		 * 
		 * @param parentShell my parent window
		 * @param dialogTitle my title
		 * @param message my error message
		 * @param status the detailed status
		 */
		public LiveValidationDialog(Shell parentShell, String dialogTitle,
				String message, IStatus status) {
			super(parentShell, dialogTitle, message, status, IStatus.CANCEL
				| IStatus.ERROR | IStatus.WARNING | IStatus.INFO);
			this.status = status;
		}

		/**
		 * Add a check-box below the message to allow the user to prevent
		 * recurrence of this dialog, if it is being shown for warnings only.
		 * 
		 * @param composite The composite to parent from.
		 * @return <code>composite</code>
		 */
		protected Control createMessageArea(Composite composite) {
			super.createMessageArea(composite);

			if (status.getSeverity() >= IStatus.WARNING) {
				// showing warnings?  Let the user opt not to show again

				// a spacer to offset the "Don't show this dialog again"
				//   checkbox from the warning message
				Control spacer = new Label(composite, SWT.NONE);

				final Button checkbox = new Button(composite, SWT.CHECK);
				checkbox.setText(EMFUIMessages.Validation_dontShowCheck);

				GridData data = new GridData(GridData.GRAB_HORIZONTAL
					| GridData.HORIZONTAL_ALIGN_FILL
					| GridData.VERTICAL_ALIGN_BEGINNING);
				data.horizontalSpan = 2; // span icon and label

				checkbox.setLayoutData(data);

				data = new GridData(GridData.GRAB_HORIZONTAL
					| GridData.HORIZONTAL_ALIGN_FILL
					| GridData.VERTICAL_ALIGN_BEGINNING);
				data.horizontalSpan = 2; // span icon and label
				spacer.setLayoutData(data);

				checkbox.addSelectionListener(new SelectionAdapter() {

					public void widgetSelected(SelectionEvent e) {
						// toggle the preference setting for display of live
						//   warnings according to user's selection
						MslUIPlugin
							.getDefault()
							.getPluginPreferences()
							.setValue(
								IPreferenceConstants.VALIDATION_LIVE_WARNINGS_IN_DIALOG,
								!checkbox.getSelection());
					}
				});
			}

			return composite;
		}
	}
}
