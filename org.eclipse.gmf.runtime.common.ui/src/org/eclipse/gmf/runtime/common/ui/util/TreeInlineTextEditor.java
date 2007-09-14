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

package org.eclipse.gmf.runtime.common.ui.util;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.contentassist.ContentAssistHandler;

import org.eclipse.gmf.runtime.common.ui.contentassist.ContentAssistantHelper;
import org.eclipse.gmf.runtime.common.ui.internal.l10n.CommonUIMessages;

/**
 * A class that enables inline-text editing for tree nodes
 * 
 * @author Yasser Lulu
 *  
 */

public class TreeInlineTextEditor {

	/**
	 * a tree editor used to aid in editing the nodes
	 */
	private TreeEditor treeEditor;

	/**
	 * the tree whose nodes are being edited
	 */
	private Tree tree;

	/**
	 * a text widget displayed to enable user string input
	 */
	private Text textEditor;

	/**
	 * a composite parent for the text widget
	 */
	private Composite textEditorParent;

	/**
	 * a zero size rectangle to force the text widget to disappear from screen
	 * when ending editing
	 */
	private static final Rectangle nullRectangle = new Rectangle(0, 0, 0, 0);

	/**
	 * the final text entered and commited by the user
	 */
	private String finalText;

	/**
	 * the initial string displayed when editing started
	 */
	private String initialText;

	/**
	 * the edit string source and sink
	 */
	private IEditStringProvider editStringProvider;

	/**
	 * the tree item currently being edited
	 */
	private TreeItem treeItem;

	/**
	 * the viewer encapsulating the tree to edit
	 */
	private TreeViewer viewer;

	/**
	 * the text action hanndler
	 */
	private IInlineTextActionHandler textActionHandler;

	/**
	 * flag for disabling F2
	 */
	private boolean isF2disabled;

	/**
	 * A semaphore that prevents re-entrance into the endEdit() behavior.
	 */
	private volatile boolean inEndEdit;

	/**
	 * content assistant handler
	 */
	private ContentAssistHandler contentAssistHandler = null;

	/**
	 * content assist background color
	 */
	private Color proposalPopupBackgroundColor;

	/**
	 * content assist foreground color
	 */
	private Color proposalPopupForegroundColor;

	/**
	 * Returns the isF2disabled.
	 * 
	 * @return boolean
	 */
	private boolean isF2disabled() {
		return isF2disabled;
	}

	/**
	 * Sets the isF2disabled.
	 * 
	 * @param isF2disabled
	 *            The isF2disabled to set
	 */
	private void setIsF2disabled(boolean isF2disabled) {
		this.isF2disabled = isF2disabled;
	}

	/**
	 * returns the tree viewer
	 * 
	 * @return TreeViewer
	 */
	private TreeViewer getTreeViewer() {
		return viewer;
	}

	/**
	 * sets the tree viewer
	 * 
	 * @param viewer
	 *            the TreeViewer to set
	 */
	private void setTreeViewer(TreeViewer viewer) {
		this.viewer = viewer;
	}

	/**
	 * return the currently edited tree item, null if none
	 * 
	 * @return TreeItem the tree-item currently being edited or null if none
	 */
	private TreeItem getTreeItem() {
		return treeItem;
	}

	/**
	 * returns the edit string provider
	 * 
	 * @return IEditStringProvider theedit string provider
	 */
	private IEditStringProvider getEditStringProvider() {
		return editStringProvider;
	}

	/**
	 * return the tree-editor
	 * 
	 * @return TreeEditor the tree-editor
	 */
	private TreeEditor getTreeEditor() {
		return treeEditor;
	}

	/**
	 * sets the tree-editor
	 * 
	 * @param treeEditor
	 *            The TreeEditor
	 */
	private void setTreeEditor(TreeEditor treeEditor) {
		this.treeEditor = treeEditor;
	}

	/**
	 * Constructor for TreeInlineTextEditor.
	 */
	public TreeInlineTextEditor(TreeViewer treeViewer, IActionBars actionBars,
			List disableActionsIds, IEditStringProvider editStringProvider) {
		this(treeViewer, editStringProvider, false);
		initTextActionHandler(actionBars, disableActionsIds);
	}

	/**
	 * Constructor for TreeInlineTextEditor.
	 * 
	 * @param treeViewer the tree viewer
	 * @param editStringProvider
	 * @param isF2disabled boolean flag indicating whether F2 is disabled
	 */
	public TreeInlineTextEditor(TreeViewer treeViewer,
			IEditStringProvider editStringProvider, boolean isF2disabled) {
		setTreeViewer(treeViewer);
		setTree(treeViewer.getTree());
		setIsF2disabled(isF2disabled);
		setEditStringProvider(editStringProvider);
		createControl();
		init();
	}

	/**
	 * answers whether this inline-editor has been disposed
	 * 
	 * @return boolean indicating its dispose status
	 */
	public boolean isDisposed() {
		return ((getTextEditorParent() == null) || (getTextEditorParent()
			.isDisposed()));
	}

	/**
	 * answers if we can start editing
	 * 
	 * @return boolean indicating if we can start editing
	 */
	public boolean canEdit() {
		return ((isDisposed() == false) && (getTree().isDisposed() == false)
			&& (getTree().getEnabled()) && (getTree().getVisible()) && (isSelectedItemEditable()));
	}

	/**
	 * Answers whether the currently selected tree-item meets all the editablity
	 * criteria
	 * 
	 * @return boolean true if editable, false otherwise
	 */
	private boolean isSelectedItemEditable() {
		return ((getTree().getSelection().length == 1)
			&& (getTree().getSelection()[0].isDisposed() == false)
			&& (getTree().getSelection()[0].getData() != null) && getEditStringProvider()
			.canEdit(getTree().getSelection()[0].getData()));
	}

	/**
	 * starts the editing process
	 */
	public void startEdit() {
		while (Display.getCurrent().readAndDispatch()) {
			// process handler.setEnabled(false) queued in
			// hide() before re-entering content assist mode
		}

		if (canEdit()) {
			cancelEdit();
			setTreeItem(getTree().getSelection()[0]);
			setInitialText(getEditStringProvider().getEditString(
				getTreeItem().getData()));
			getTextEditor().setText(getInitialText());
			getTreeEditor().setItem(getTreeItem());

			show();
		}
	}

	/**
	 * cancels the editing process
	 */
	public void cancelEdit() {
		if (canProceed()) {
			hide();
		}
	}

	/**
	 * ends the editing process
	 */
	public void endEdit() {
		if (inEndEdit) {
			// prevent re-entrance into this method when we are already
			//   ending the edit. This prevents, in particular, the focus
			//   listener from ending the edit while we are ending it
			return;
		}

		inEndEdit = true;
		try {
			if (canProceed()) {
				setFinalText(getCurrentText());
				if (getFinalText().equals(getInitialText()) == false) {
					final Object obj = getTreeItem().getData();
					getEditStringProvider().setEditString(
						obj, getFinalText());
					// Hide first so that the focus change when the dialog opens
					// doesn't attempt to re-enter "endEdit()".
					
					//RATLC00529737
					//ask the label-provider to the update tree-node's label in order to
					//display the newly entered name  
					getTreeItem().getDisplay().asyncExec(new Runnable() {

						public void run() {
							if (!isDisposed()) {
								getTreeViewer().update(obj, null);
							}
						}
					});
					hide();
				} else {
					hide();
				}
			}
		} finally {
			inEndEdit = false;
		}
	}

	/**
	 * Opens an error dialog for the specified status object.
	 * 
	 * @param status
	 *            The status object for which to open an error dialog.
	 *  
	 */
	protected void openErrorDialog(IStatus status) {
		ErrorDialog.openError(getShell(), CommonUIMessages.TreeInlineTextEditor_errorDialogTitle, null, status);
	}

	private Shell getShell() {
		return getTree().getShell();
	}

	/**
	 * answers if it is ok to continue operation
	 * 
	 * @return boolean indicating if it is ok to continue operation
	 */
	private boolean canProceed() {
		return isDisposed() == false && getTreeItem() != null
				&& !getTreeItem().isDisposed();
	}

	/**
	 * displays the text editing widget
	 */
	private void show() {
		uninstallContentAssist(false);

		if (getTreeItem() != null) {
			IContentAssistProcessor processor = getEditStringProvider()
				.getCompletionProcessor(getTreeItem().getData());
			if (processor != null) {
				// install content assist
				contentAssistHandler = ContentAssistantHelper
					.createTextContentAssistant(getTextEditor(),
						proposalPopupForegroundColor,
						proposalPopupBackgroundColor, processor);
			}
		}
        getTextEditorParent().setEnabled(true);
		getTextEditorParent().setVisible(true);
        getTextEditor().setEnabled(true);
        getTextEditor().setVisible(true);
		adjustTextEditorBounds();
		getTextEditorParent().redraw();
		getTextEditor().selectAll();
		getTextEditor().setFocus();

		if (getTextActionHandler() != null) {
			getTextActionHandler().hookHandlers();
		}
	}

	/**
	 * hides the text editing widget
	 */
	private void hide() {
		setTreeItem(null);
		getTreeEditor().setItem(null);
		
        getTextEditor().setVisible(false);
        getTextEditor().setEnabled(false);
		
        getTextEditorParent().setBounds(getNullRectangle());
        getTextEditorParent().setVisible(false);
        getTextEditorParent().setEnabled(false);
        
		if (getTextActionHandler() != null) {
			getTextActionHandler().unHookHandlers();
		}

		uninstallContentAssist(true);
	}

	/**
	 * Uninstalls content assist on the text widget, if installed
	 * 
	 * @param fork
	 *            whether to queue the uninstall or not
	 */
	private void uninstallContentAssist(boolean fork) {
		if (contentAssistHandler != null) {
			// uninstall content assist
			final ContentAssistHandler localHandler = contentAssistHandler;
			contentAssistHandler = null;
			if (fork) {
				Display.getCurrent().asyncExec(new Runnable() {

					public void run() {
						// Content Assist hack - queue disablement, otherwise
						// cleanup on focus lost won't happen
						localHandler.setEnabled(false);

					}
				});
			} else {
				localHandler.setEnabled(false);
			}
		}
	}

	/**
	 * Disposes the text widget and reset the editorText field. 
	 * It becomes unreusable afterwards
	 */
	public void dispose() {
		if (getTextEditorParent() != null) {
			if (getTextActionHandler() != null) {
				getTextActionHandler().dispose();
			}
			setTextActionHandler(null);
			setTextEditorParent(null);
			setTextEditor(null);
			getTreeEditor().setEditor(null, null);
			setTreeEditor(null);
			setTree(null);

			proposalPopupBackgroundColor.dispose();
			proposalPopupForegroundColor.dispose();
		}
	}

	/**
	 * creates the text widget and its parent composite
	 */
	private void createControl() {
		setTextEditorParent(new Composite(getTree(), SWT.NONE));
		setTreeEditor(new TreeEditor(getTree()));
		getTreeEditor().horizontalAlignment = SWT.LEFT;
		getTreeEditor().grabHorizontal = true;
		getTreeEditor().setEditor(getTextEditorParent(), null);
		getTextEditorParent().setVisible(false);
		setTextEditor(new Text(getTextEditorParent(), SWT.NONE));
		getTextEditorParent().setBackground(getTextEditor().getBackground());

		proposalPopupBackgroundColor = new Color(getShell().getDisplay(),
			new RGB(254, 241, 233));
		proposalPopupForegroundColor = new Color(getShell().getDisplay(),
			new RGB(0, 0, 0));

	}

	/**
	 * initializes the controls and listeners needed to manage the editing
	 * process
	 */
	private void init() {
		getTextEditorParent().addListener(SWT.Paint, new Listener() {

			public void handleEvent(Event e) {
				Point textSize = getTextEditor().getSize();
				Point parentSize = getTextEditorParent().getSize();
				e.gc.drawRectangle(0, 0, Math.min(textSize.x + 4,
					parentSize.x - 1), parentSize.y - 1);
			}
		});

		getTextEditor().addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event e) {
				adjustTextEditorBounds();
				getTextEditorParent().redraw();
			}
		});

		getTextEditor().addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent event) {
				if (event.character == SWT.CR) {
					endEdit();
				} else if (event.character == SWT.ESC) {
					cancelEdit();
				}
			}
		});

		getTextEditor().addFocusListener(new FocusAdapter() {

			public void focusGained(FocusEvent e) {
				return;
			}

			public void focusLost(FocusEvent fe) {
				Shell activeShell = fe.display.getActiveShell();
				if (activeShell != null
					&& getTextEditor().getShell().equals(
						activeShell.getParent())) {
					/*
					 * CONTENT ASSIST: focus is lost to the content assist pop
					 * up - stay in focus
					 */
					return;
				}

				if ((getTreeViewer().getSelection().isEmpty() == false)
					&& canProceed()) {
					final Object obj = getTreeItem().getData();
					getTreeItem().getDisplay().asyncExec(new Runnable() {

						public void run() {
							if (!isDisposed()) {
								getTreeViewer().update(obj, null);
							}
						}
					});
				}
				endEdit();
			}
		});

		getTreeViewer().addSelectionChangedListener(
			new ISelectionChangedListener() {

				public void selectionChanged(SelectionChangedEvent event) {
					cancelEdit();
				}
			});

		if (!isF2disabled()) {
			getTree().addKeyListener(new KeyAdapter() {

				public void keyReleased(KeyEvent event) {
					if (event.keyCode == SWT.F2) {
						startEdit();
					}
				}
			});
		}
	}

	/**
	 * initializes the text action handlers
	 * 
	 * @param actionBars
	 *            The IActionBars for the view-site to retarget global
	 *            edit-cut-copy events for the text box
	 * @param disableActionsIds
	 *            a List of global actions ids that are non-Eclipse and which
	 *            we'll have to disable
	 */
	private void initTextActionHandler(IActionBars actionBars,
			List disableActionsIds) {
		if (actionBars == null) {
			return;
		}

		actionBars.getMenuManager().addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				cancelEdit();
			}
		});

		setTextActionHandler(new InlineTextActionHandler(actionBars,
			getTextEditor(), disableActionsIds));

	}

	/**
	 * adjusts the bounds of the text widget
	 */
	private void adjustTextEditorBounds() {
		Point textSize = getTextEditor().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		textSize.x += textSize.y; // increase width a little
		Point parentSize = getTextEditorParent().getSize();
		getTextEditor().setBounds(2, 1, Math.min(textSize.x, parentSize.x - 4),
			parentSize.y - 2);
	}

	/**
	 * Get the Tree being edited.
	 * 
	 * @returnTree
	 */
	private Tree getTree() {
		return tree;
	}

	/**
	 * return the current text
	 * 
	 * @return String the text currently in the text widget, or null if it is
	 *         disposed already
	 */
	public String getCurrentText() {
		return (canProceed()) ? getTextEditor().getText()
			: null;
	}

	/**
	 * returns the initial value when editing started
	 * 
	 * @return String the last initial value used when editing last started
	 */
	public String getInitialText() {
		return initialText;
	}

	/**
	 * returns the comitted string by the user when editing ended (not-canclled)
	 * 
	 * @return String the comitted string by the user
	 */
	public String getFinalText() {
		return finalText;
	}

	/**
	 * returns the text widget
	 * 
	 * @return Text the text widget used for editing
	 */
	private Text getTextEditor() {
		return textEditor;
	}

	/**
	 * returns the text widget parent composite
	 * 
	 * @return Composite text widget parent composite
	 */
	private Composite getTextEditorParent() {
		return textEditorParent;
	}

	/**
	 * Sets the editStringProvider.
	 * 
	 * @param editStringProvider
	 *            The editStringProvider to set
	 */
	private void setEditStringProvider(IEditStringProvider editStringProvider) {
		this.editStringProvider = editStringProvider;
	}

	/**
	 * Sets the textEditor.
	 * 
	 * @param textEditor
	 *            The textEditor to set
	 */
	private void setTextEditor(Text textEditor) {
		this.textEditor = textEditor;
	}

	/**
	 * Sets the textEditorParent.
	 * 
	 * @param textEditorParent
	 *            The textEditorParent to set
	 */
	private void setTextEditorParent(Composite textEditorParent) {
		this.textEditorParent = textEditorParent;
	}

	/**
	 * Sets the tree.
	 * 
	 * @param tree
	 *            The tree to set
	 */
	private void setTree(Tree tree) {
		this.tree = tree;
	}

	/**
	 * Sets the treeItem.
	 * 
	 * @param treeItem
	 *            The treeItem to set
	 */
	private void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
	}

	/**
	 * Returns the nullRectangle.
	 * 
	 * @return Rectangle
	 */
	private Rectangle getNullRectangle() {
		return nullRectangle;
	}

	/**
	 * Sets the finalText.
	 * 
	 * @param finalText
	 *            The finalText to set
	 */
	private void setFinalText(String finalText) {
		this.finalText = finalText;
	}

	/**
	 * Sets the initialText.
	 * 
	 * @param initialText
	 *            The initialText to set
	 */
	private void setInitialText(String initialText) {
		this.initialText = initialText;
	}

	/**
	 * Returns the textActionHandler.
	 * 
	 * @return TextActionHandler
	 */
	private IInlineTextActionHandler getTextActionHandler() {
		return textActionHandler;
	}

	/**
	 * Sets the textActionHandler.
	 * 
	 * @param textActionHandler
	 *            The textActionHandler to set
	 */
	private void setTextActionHandler(IInlineTextActionHandler textActionHandler) {
		this.textActionHandler = textActionHandler;
	}

}