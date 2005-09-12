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

import org.eclipse.swt.widgets.Text;

/**
 * A class that is used to handle all keyboard generated text actions 
 * associated with the displayed text editor 
 * 
 * @author Yasser Lulu
 * 
 */
public abstract class BaseInlineTextActionHandler
    implements IInlineTextActionHandler {
    /**
    * an empty string constant
    */
    protected static final String EMPTY_STRING = ""; //$NON-NLS-1$
    /**
     * a boolean indicating if text actions are hooked
     */
    private boolean hooked;
    /**
     * the text box widget used for editing
     */
    private Text text;

    /**
     * Creates a runtime instance of <code>BaseInlineTextActionHandler</code>.
     * 
     * @param text <code>Text</code> widget used for editing
     */
    BaseInlineTextActionHandler(Text text) {
        setText(text);
    }

    /**
     * sets the text widget
     * @param text The Text widget to set
     */
    private void setText(Text text) {
        this.text = text;
    }
    /**
     * returns the text widget 
     * @return Text The text widget
     */
    protected Text getText() {
        return text;
    }

    /**
     * @see org.eclipse.gmf.runtime.common.ui.util.IInlineTextActionHandler
     */
    public void dispose() {
        if (isHooked()) {
            unHookHandlers();
        }
        setText(null);
    }

    /**
     * Returns the hooked.
     * @return boolean
     */
    protected boolean isHooked() {
        return hooked;
    }

    /**
     * Sets the hooked.
     * @param hooked The hooked to set
     */
    protected void setHooked(boolean hooked) {
        this.hooked = hooked;
    }

    /**
     * handles cut action
     */
    protected void handleCut() {
        getText().cut();
    }

    /**
     * handles copy action
     */
    protected void handleCopy() {
        getText().copy();
    }

    /**
     * handles paste action
     */
    protected void handlePaste() {
        getText().paste();
    }

    /**
     * handles select-all action
     */
    protected void handleSelectAll() {
        getText().selectAll();
    }

    /**
     * handles delete action
     */
    protected void handleDelete() {
        if (getText().getSelectionCount() > 0) {
            getText().insert(EMPTY_STRING);
        } else {
            // remove the next character
            int pos = getText().getCaretPosition();
            if (pos < getText().getCharCount()) {
                getText().setSelection(pos, pos + 1);
                getText().insert(EMPTY_STRING);
            }
        }
    }
}

/*
 * Please do NOT delete this commented code
 */
//     /**
//     * @see org.eclipse.gmf.runtime.common.ui.util.IInlineTextActionHandler
//     */
//    public void hookHandlers() {
//        getText().addKeyListener(getKeyAdapter());
//        getText().setFocus();
//        setHooked(true);
//    }
//    /**
//     * @see org.eclipse.gmf.runtime.common.ui.util.IInlineTextActionHandler
//     */
//    public void unHookHandlers() {
//        getText().removeKeyListener(getKeyAdapter());
//        setHooked(false);
//    }
//
//    /**
//     * Returns the keyAdapter.
//     * @return KeyAdapter
//     * 
//     * NOTE: Eclipse does not dispatch to us the ctrl-x, ctrl-v, ctrl-c
//     *  keyboard edit actions, but rather it installs by default and
//     * uses the old Windows text editing short-cuts: 
//     * shift-insert, shift-delete, ctrl-insert.
//     * in other words this key adapter is useless since we'll never be called
//     * appropriately 
//     *                    CTRL+V or SHIFT+INS
//     *                    CTRL+C or CTRL+INSERT
//     *                    CTRL+X or SHIFT+DELETE
//     */
//    private KeyAdapter getKeyAdapter() {        
//        if (keyAdapter == null) {            
//            keyAdapter = new KeyAdapter() {
//                public void keyReleased(KeyEvent event) {
//                    if (event.keyCode == SWT.DEL) {
//                        handleDelete();
//                    } else if (event.stateMask == SWT.CTRL) {
//                        switch (Character.toLowerCase(event.character)) {
//                            case 'a' :
//                                {
//                                    handleSelectAll();
//                                    break;
//                                }
//                            case 'c' :
//                                {
//                                    handleCopy();
//                                    break;
//                                }
//                            case 'v' :
//                                {
//                                    handlePaste();
//                                    break;
//                                }
//                            case 'x' :
//                                {
//                                    handleCut();
//                                    break;
//                                }
//                        }
//                    }
//                }
//            };
//        }
//
//        return keyAdapter;
//    }
