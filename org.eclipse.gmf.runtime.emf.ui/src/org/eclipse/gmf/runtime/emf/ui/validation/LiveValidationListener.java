/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.emf.ui.validation;

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
import org.eclipse.gmf.runtime.emf.ui.internal.l10n.ResourceManager;
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

    /** Message indicating that problems were found in applying model changes. */
    private static final String VALIDATION_PROBLEMS = ResourceManager.getI18NString("Validation.problems"); //$NON-NLS-1$

    /** Message indicating that model changes were rolled back. */
    private static final String VALIDATION_ROLLED_BACK = ResourceManager.getI18NString("Validation.rollback"); //$NON-NLS-1$
    
    /**
     * Message reporting an error.  The arguments are:
     * <ul>
     *   <li><tt>{0}</tt> - the message from the validation service</li>
     * </ul>
     */
    private static final String VALIDATION_ERROR = ResourceManager.getI18NString("Validation.error"); //$NON-NLS-1$

    /**
     * Message reporting a warning.  The arguments are:
     * <ul>
     *   <li><tt>{0}</tt> - the message from the validation service</li>
     * </ul>
     */
    private static final String VALIDATION_WARNING = ResourceManager.getI18NString("Validation.warn"); //$NON-NLS-1$

    /**
     * Message reporting a note.  The arguments are:
     * <ul>
     *   <li><tt>{0}</tt> - the message from the validation service</li>
     * </ul>
     */
    private static final String VALIDATION_NOTE = ResourceManager.getI18NString("Validation.note"); //$NON-NLS-1$

    /** Shown in a dialog window when live validation rolls back a model update. */
    private static final String VALIDATION_LIVE_ERROR = ResourceManager.getI18NString("Validation.liveError"); //$NON-NLS-1$
    
    /** Shown in a dialog window when live validation generates warnings. */
    private static final String VALIDATION_LIVE_WARNING = ResourceManager.getI18NString("Validation.liveWarning.part1") //$NON-NLS-1$
    		+ "\n\n"	+ ResourceManager.getI18NString("Validation.liveWarning.part2"); //$NON-NLS-1$ //$NON-NLS-2$
    
    
    /** Title of dialog window for live validation. */
    private static final String VALIDATION_LIVE_DIALOG_TITLE = ResourceManager.getI18NString("Validation.liveDialogTitle"); //$NON-NLS-1$

    /**
     * The category of output view to use for our messages.
     */
    private static final String PXDE_OUTPUT_CATEGORY = ResourceManager.getInstance().getString("Validation.outputProviderCategory"); //$NON-NLS-1$
    
    
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
            	
	            ConsoleUtil.println(PXDE_OUTPUT_CATEGORY, VALIDATION_PROBLEMS);
	            ConsoleUtil.println(PXDE_OUTPUT_CATEGORY, messages);
        	}

            final boolean showConsole =
            	MslUIPlugin.getDefault().getPluginPreferences().getBoolean(
            		IPreferenceConstants.VALIDATION_LIVE_SHOW_CONSOLE);
            
            if (getOutputUtility().hasProblems()) {
            	if (showConsole) {            		
            		ConsoleUtil.showConsole( PXDE_OUTPUT_CATEGORY);
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
						? VALIDATION_LIVE_ERROR
						: VALIDATION_LIVE_WARNING;
                	
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
						VALIDATION_LIVE_DIALOG_TITLE,
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
                output.append(VALIDATION_ROLLED_BACK);
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
                            messagePattern = VALIDATION_ERROR;
                            break;
                        case IStatus.WARNING :
                            hasProblems = true;
                            messagePattern = VALIDATION_WARNING;
                            break;
                        default :
                            messagePattern = VALIDATION_NOTE;
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
				checkbox.setText(ResourceManager
					.getI18NString("Validation.dontShowCheck")); //$NON-NLS-1$

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
