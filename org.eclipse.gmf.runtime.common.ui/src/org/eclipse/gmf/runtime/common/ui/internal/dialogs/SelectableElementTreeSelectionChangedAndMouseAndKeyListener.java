/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.internal.dialogs;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

import org.eclipse.gmf.runtime.common.ui.dialogs.SelectableElement;
import org.eclipse.gmf.runtime.common.ui.dialogs.SelectedType;

/**
 * This is a selection changed listener for SelectableElement. You must
 * implement abstract protected void switchCheckType(SelectableElement element)
 * since there may be a different number of check types you want to support. The
 * show related elements only supports two, LEAVE and SELECTED. The show hide
 * relationships supports three, LEAVE, UNSELECTED, and SELECTED.
 * 
 * Also, add this as a SelectionChangedListener on the TreeViewer control, and
 * add a MouseListener and a KeyListener on the Tree control. If you don't add
 * the MouseListener and KeyListener, you'll have to take out the check for
 * mouseOrKeyPressed. I had to add mouse and key listeners to filter out tabbing
 * or pressing the up and down arrows.
 * 
 * @author Wayne Diu, wdiu
 */
public abstract class SelectableElementTreeSelectionChangedAndMouseAndKeyListener
	implements ISelectionChangedListener, MouseListener, KeyListener {

	/**
	 * The TreeViewer that will have its selection changed listener set to this
	 * selection changed listener.
	 */
	private TreeViewer viewer;

	/**
	 * The ASCII code for the spacebar on the keyboard.
	 */
	private static final char KEYBOARD_SPACE_ASCII_CODE = 32;

	/**
	 * The mouse or keyboard was pressed
	 */
	protected boolean mouseOrKeyPressed = false;

	/**
	 * The selection from the selection changed event.
	 */
	protected SelectionChangedEvent selectionChangedEvent = null;

	/**
	 * Temporary check to see if this is Linux. Should not be needed after the
	 * double selectionChanged event bug in Linux is resolved.
	 */
	protected boolean isLinux = false;

	/**
	 * Constructor that takes the TreeViewer that will have its selection
	 * changed listener set to this selection changed listener.
	 * 
	 * @param aViewer
	 *            the TreeViewer
	 */
	public SelectableElementTreeSelectionChangedAndMouseAndKeyListener(
			TreeViewer aViewer) {
		this.viewer = aViewer;

		if (System.getProperty("os.name").toUpperCase().startsWith("LIN")) { //$NON-NLS-1$ //$NON-NLS-2$
			isLinux = true;
		}
	}

	/**
	 * Pass in an element, and this will update its parents' check states
	 * 
	 * @param element
	 *            the SelectableElement which will have its parents' check state
	 *            updated
	 */
	private void setSelectedTypeForParent(SelectableElement element) {
		SelectableElement parent = element.getParent();
		if (parent != null) {

			if (SelectableElement.doAllChildrenHaveSelectedType(parent,
				SelectedType.UNSELECTED)) {
				parent.setSelectedType(SelectedType.UNSELECTED);
			} else if (SelectableElement.doAllChildrenHaveSelectedType(parent,
				SelectedType.SELECTED)) {
				parent.setSelectedType(SelectedType.SELECTED);
			} else {
				parent.setSelectedType(SelectedType.LEAVE);
			}

			setSelectedTypeForParent(parent);
		}
	}

	/**
	 * Pass in an element, and this will update its children's check states
	 * 
	 * @param parent
	 *            the SelectableElement which will have its children's check
	 *            state updated
	 */
	private void setSelectedTypeForChildren(SelectableElement parent) {
		for (int i = 0; i < parent.getNumberOfChildren(); i++) {
			parent.getChild(i).setSelectedType(parent.getSelectedType());
			setSelectedTypeForChildren(parent.getChild(i));
		}
	}

	/**
	 * Switch the check type of an element
	 * 
	 * @param element
	 *            the SelectableElement that will have its checktype changed
	 */
	abstract protected void switchCheckType(SelectableElement element);

	/**
	 * Handle the selection changed event. This won't do anything unless both a
	 * selection occured and the mouse or key was pressed.
	 */
	public void handleSelectionChanged() {
		if (selectionChangedEvent != null && mouseOrKeyPressed) {
			StructuredSelection s = (StructuredSelection) selectionChangedEvent
				.getSelection();
			s.getFirstElement();

			assert (s.getFirstElement() instanceof SelectableElement);
			SelectableElement element = (SelectableElement) s.getFirstElement();

			switchCheckType(element);

			setSelectedTypeForChildren(element);
			setSelectedTypeForParent(element);

			viewer.refresh();

			//reset since processed
			selectionChangedEvent = null;
			mouseOrKeyPressed = false;
		}
	}

	//Mouse listeners

	/**
	 * The double click mouse event is ignored.
	 * 
	 * @param arg
	 *            ignored
	 */
	public void mouseDoubleClick(MouseEvent arg) {
		//do nothing
	}

	/**
	 * The mouse down event sets the mouse or keyboard flag.
	 * 
	 * @param arg
	 *            ignored
	 */
	public void mouseDown(MouseEvent arg) {
		mouseOrKeyPressed = true;
		handleSelectionChanged();
	}

	/**
	 * The mouse up event is ignored
	 * 
	 * @param arg
	 *            ignored
	 */
	public void mouseUp(MouseEvent arg) {
		//do nothing
	}

	//Key listeners

	/**
	 * The key pressed event filters out everything except the spacebar before
	 * setting the mouse or keyboard flag.
	 * 
	 * @param arg
	 *            ignored
	 */
	public void keyPressed(KeyEvent arg) {
		//only respond to space
		if (arg.character == KEYBOARD_SPACE_ASCII_CODE) {
			mouseOrKeyPressed = true;
			handleSelectionChanged();
		}
	}

	/**
	 * The key released event is ignored.
	 * 
	 * @param arg
	 *            ignored
	 */
	public void keyReleased(KeyEvent arg) {
		//do nothing
	}

	//Selection changed

	/**
	 * Event handler. The check state has changed.
	 * 
	 * @param event
	 *            the SelectionChangedEvent that made this selection changed
	 *            event
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		//Temporary check to see if this is Linux. Should not be needed after
		//the double selectionChanged event bug in Linux is resolved.
		//This will not work if they change the order of the selectionChanged
		//and mouse events, so I have restricted the possible problem to Linux
		if (isLinux && !mouseOrKeyPressed)
			return;

		//I must check ((StructuredSelection)event.getSelection()).
		//getFirstElement() instanceof SelectableElement for Linux
		//because I get a selection event when expanding or
		//collapsing a tree
		if (event.getSelection() instanceof StructuredSelection
			&& ((StructuredSelection) event.getSelection()).getFirstElement() instanceof SelectableElement) {
			selectionChangedEvent = event;
			handleSelectionChanged();
		} else {
			selectionChangedEvent = null;
		}
	}
}