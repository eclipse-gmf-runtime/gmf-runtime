/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.action.internal.actions.global;

import java.text.MessageFormat;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.help.WorkbenchHelp;

import org.eclipse.gmf.runtime.common.ui.action.global.GlobalAction;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalActionId;
import org.eclipse.gmf.runtime.common.ui.action.internal.IHelpContextIds;
import org.eclipse.gmf.runtime.common.ui.action.internal.l10n.ResourceManager;

/**
 * Global Redo Action
 * This class simply delegates all the calls to the <code>RedoActionHandler</code>
 * 
 * @author vramaswa
 */
public final class GlobalRedoAction extends GlobalAction {
    
	/**
	 * The stand-alone Redo label. 
	 */
	private static final String REDO_LABEL = ResourceManager
		.getI18NString("GlobalRedoAction.label"); //$NON-NLS-1$

	/**
	 * The Redo label to be used with a command label.
	 */
	private static final String REDO_LABEL_FORMATTED = ResourceManager
		.getI18NString("GlobalRedoAction.formattedLabel"); //$NON-NLS-1$

    /**
     * Action definition id of the redo action.
     */
    private static final String REDO = "org.eclipse.gmf.runtime.common.ui.actions.global.redo"; //$NON-NLS-1$

    /**
     * Imagedescriptor for the redo action
     */
    private static final ImageDescriptor REDO_IMAGE = ResourceManager.getInstance().getImageDescriptor("full/etool16/redo_edit.gif"); //$NON-NLS-1$

    /**
     * Imagedescriptor for the redo action
     */
    private static final ImageDescriptor DISABLED_REDO_IMAGE = ResourceManager.getInstance().getImageDescriptor("full/dtool16/redo_edit.gif"); //$NON-NLS-1$

    /**
     * Imagedescriptor for the redo action
     */
    private static final ImageDescriptor HOVER_REDO_IMAGE = ResourceManager.getInstance().getImageDescriptor("full/ctool16/redo_edit.gif"); //$NON-NLS-1$

	/**
	 * @param workbenchPage
	 */
	public GlobalRedoAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

    /**
     * @param workbenchPart
     */
    public GlobalRedoAction(IWorkbenchPart workbenchPart) {
        super(workbenchPart);
    }

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.IDisposableAction#init()
	 */
	public void init() {
        /* Set the id */
        setId(
            getWorkbenchActionConstant() != null
                ? getWorkbenchActionConstant()
                : REDO);

        /* Set the label */
        setText(REDO_LABEL);

        /* Set the image */
        setImageDescriptor(REDO_IMAGE);
        setHoverImageDescriptor(HOVER_REDO_IMAGE);
        setDisabledImageDescriptor(DISABLED_REDO_IMAGE);

        /* Set the context sensitive help */
        WorkbenchHelp.setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);
		super.init();
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.action.internal.global.GlobalAction#getActionId()
     */
    public String getActionId() {
        return GlobalActionId.REDO;
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#doRun(org.eclipse.core.runtime.IProgressMonitor)
     */
    protected void doRun(IProgressMonitor progressMonitor) {
    	try {
    		// RATLC00138974 - prevents the user trying to redo while redo is in progress.
    		setEnabled(false);
    		getCommandManager().redo();
    	} finally {
    		refresh();
    	}
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.action.IRepeatableAction#refresh()
     */
    public void refresh() {
		setEnabled(getCommandManager().canRedo());
		String label = REDO_LABEL;
		String commandLabel = getCommandManager().getRedoLabel();
		if (commandLabel != null) {
			label = MessageFormat.format(REDO_LABEL_FORMATTED,
				new Object[] {removeMnemonics(commandLabel)});
		}
		setText(label);
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.action.IRepeatableAction#isRepeatable()
     */
    public boolean isRepeatable() {
        return false;
    }

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isCommandStackListener()
	 */
	protected boolean isCommandStackListener() {
		return true;
	}
}
