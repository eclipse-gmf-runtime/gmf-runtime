/******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.common.ui.services.action.contributionitem;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.ContributionItemService;
import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IKeyBindingService;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.activities.IWorkbenchActivitySupport;
import org.eclipse.ui.services.IServiceLocator;

/**
 * Tests for the Contribution Item Service.
 * 
 * @author cmahoney, mmostafa
 */
public class ContributionItemServiceTests
	extends TestCase {

	private static final String MY_ACTIVITY_ID = "MyActivityID"; //$NON-NLS-1$
    private static final String MY_ACTIVITY_ID_OF_SPECIFIC_ENTRIES = "MyActivityIDOfSpecificEntries"; //$NON-NLS-1$

	class MyActionBars
		implements IActionBars {

		IToolBarManager myToolBarManager = new ToolBarManager();

		IMenuManager myMenuManager = new MenuManager();

		public void clearGlobalActionHandlers() {
			// do nothing
		}

		public IAction getGlobalActionHandler(String actionId) {
			return null;
		}

		public IMenuManager getMenuManager() {
			return myMenuManager;
		}

		public IStatusLineManager getStatusLineManager() {
			return null;
		}

		public IToolBarManager getToolBarManager() {
			return myToolBarManager;
		}

		public void setGlobalActionHandler(String actionId, IAction handler) {
			// do nothing
		}

		public void updateActionBars() {
			// do nothing
		}
        
        public IServiceLocator getServiceLocator() {
            return null;
        }

	}
    
    class MySelection
        implements ISelection, IStructuredSelection {

        public boolean isEmpty() {
            return true;
        }

        public Object getFirstElement() {
            return null;
        }

        public Iterator iterator() {
            return Collections.EMPTY_LIST.iterator();
        }

        public int size() {
            return 0;
        }

        public Object[] toArray() {
            return null;
        }

        public List toList() {
            return null;
        }

    }

    class MySelectionProvider
        implements ISelectionProvider {

        public void addSelectionChangedListener(
                ISelectionChangedListener listener) {
            // nothing to do here
        }

        public ISelection getSelection() {
            return new MySelection();
        }

        public void removeSelectionChangedListener(
                ISelectionChangedListener listener) {
            // nothing to do here
        }

        public void setSelection(ISelection selection) {
            // nothing to do here
        }

    }

    class MySite
        implements IWorkbenchPartSite {

        public String getId() {
            return null;
        }

        public IKeyBindingService getKeyBindingService() {
            return null;
        }

        public IWorkbenchPart getPart() {
            return null;
        }

        public String getPluginId() {
            return null;
        }

        public String getRegisteredName() {
            return null;
        }

        public void registerContextMenu(MenuManager menuManager,
                ISelectionProvider selectionProvider) {
            // nothing to do here
        }

        public void registerContextMenu(String menuId, MenuManager menuManager,
                ISelectionProvider selectionProvider) {
            // nothing to do here
        }

        public IWorkbenchPage getPage() {
            return null;
        }

        public ISelectionProvider getSelectionProvider() {
            return new MySelectionProvider();
        }

        public Shell getShell() {
            return null;
        }

        public IWorkbenchWindow getWorkbenchWindow() {
            return null;
        }

        public void setSelectionProvider(ISelectionProvider provider) {
            // nothing to do here
        }

        public Object getAdapter(Class adapter) {
            return null;
        }

        public Object getService(Class api) {
            return null;
        }

        public boolean hasService(Class api) {
            return false;
        }

    }

    class MyWorkBenchPart
        implements IWorkbenchPart {

        public void addPropertyListener(IPropertyListener listener) {
            // nothing to do here

        }

        public void createPartControl(Composite parent) {
            // nothing to do here

        }

        public void dispose() {
            // nothing to do here

        }

        public IWorkbenchPartSite getSite() {
            return new MySite();
        }

        public String getTitle() {
            return null;
        }

        public Image getTitleImage() {
            return null;
        }

        public String getTitleToolTip() {
            return null;
        }

        public void removePropertyListener(IPropertyListener listener) {
            // nothing to do here

        }

        public void setFocus() {
            // nothing to do here

        }

        public Object getAdapter(Class adapter) {
            return null;
        }

    }

	class MyWorkbenchPartDescriptor
		implements IWorkbenchPartDescriptor {

		String partId;

		public MyWorkbenchPartDescriptor(String id) {
			partId = id;
		}

		public String getPartId() {
			return partId;
		}

		public Class getPartClass() {
			return MyWorkbenchPart.class;
		}

		public IWorkbenchPage getPartPage() {
			return null;
		}
	}

	public ContributionItemServiceTests(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(ContributionItemServiceTests.class);
	}

    protected void setUp() throws Exception {
        // start with both activities enabled
        toggleActivity(MY_ACTIVITY_ID, true);
        toggleActivity(MY_ACTIVITY_ID_OF_SPECIFIC_ENTRIES, true);    }
    
	/**
     * Tests the filtering of contribution item providers given a disabled
     * capability/activity.
     * 
     * @throws Exception
     */
	public void testCapabilityFiltering()
        throws Exception {

        String EDITOR1 = "editor1"; //$NON-NLS-1$
        String EDITOR2 = "editor2"; //$NON-NLS-1$

        IWorkbenchPartDescriptor editor1Descriptor = new MyWorkbenchPartDescriptor(
            EDITOR1);
        IWorkbenchPartDescriptor editor2Descriptor = new MyWorkbenchPartDescriptor(
            EDITOR2);

        MyActionBars editor1ActionBars = new MyActionBars();
        MyActionBars editor2ActionBars = new MyActionBars();

        toggleActivity(MY_ACTIVITY_ID, false);

        ContributionItemService.getInstance().contributeToActionBars(
            editor1ActionBars, editor1Descriptor);

        validateActionBars(editor1ActionBars, false, false);

        ContributionItemService.getInstance().contributeToActionBars(
            editor2ActionBars, editor2Descriptor);

        validateActionBars(editor2ActionBars, false, false);

        // now enable provider2 and update editor 1 only
        toggleActivity(MY_ACTIVITY_ID, true);
        ContributionItemService.getInstance().updateActionBars(
            editor1ActionBars, editor1Descriptor);

        validateActionBars(editor1ActionBars, true, true);
        validateActionBars(editor2ActionBars, false, true);

        // now update editor 2
        ContributionItemService.getInstance().updateActionBars(
            editor2ActionBars, editor2Descriptor);

        validateActionBars(editor1ActionBars, true, true);
        validateActionBars(editor2ActionBars, true, true);

        // try a double update to make sure nothing changes
        int expectedLength = editor2ActionBars.getToolBarManager().getItems().length;
        IContributionItem expectedItem0 = editor2ActionBars.getToolBarManager().getItems()[0];
        IContributionItem expectedItem2 = editor2ActionBars.getToolBarManager().getItems()[2];
        
        ContributionItemService.getInstance().updateActionBars(
            editor2ActionBars, editor2Descriptor);
        
        assertEquals(expectedLength, editor2ActionBars.getToolBarManager()
            .getItems().length);
        assertEquals(expectedItem0, editor2ActionBars.getToolBarManager()
            .getItems()[0]);
        assertEquals(expectedItem2, editor2ActionBars.getToolBarManager()
            .getItems()[2]);

        validateActionBars(editor1ActionBars, true, true);
        validateActionBars(editor2ActionBars, true, true);

        // now disable provider2
        toggleActivity(MY_ACTIVITY_ID, false);

        validateActionBars(editor1ActionBars, true, false);
        validateActionBars(editor2ActionBars, true, false);

        // now enable provider2
        toggleActivity(MY_ACTIVITY_ID, true);

        validateActionBars(editor1ActionBars, true, true);
        validateActionBars(editor2ActionBars, true, true);

    }
    

    /**
     * Tests the filtering of specific contribution items given a disabled
     * capability/activity.
     * 
     * @throws Exception
     */
    public void testCapabilityFilteringOfSpecificEntries()
        throws Exception {

        String EDITOR1 = "editor1"; //$NON-NLS-1$
        IWorkbenchPartDescriptor editor1Descriptor = new MyWorkbenchPartDescriptor(
            EDITOR1);
        MyActionBars editor1ActionBars = new MyActionBars();

        // disable the activity associated with specific entries
        toggleActivity(MY_ACTIVITY_ID_OF_SPECIFIC_ENTRIES, false);

        ContributionItemService.getInstance().contributeToActionBars(
            editor1ActionBars, editor1Descriptor);
        validateActionBars(editor1ActionBars, true, false);

        // enable it again
        toggleActivity(MY_ACTIVITY_ID_OF_SPECIFIC_ENTRIES, true);
        ContributionItemService.getInstance().updateActionBars(
            editor1ActionBars, editor1Descriptor);
        validateActionBars(editor1ActionBars, true, true);
    }    
    
    /**
     * Tests the loading of the class provided by the Policy_Class attribute
     * 
     * @throws Exception
     */
    public void testPolicy_ClassAttribute()
        throws Exception {
        toggleActivity(MY_ACTIVITY_ID, true);
        ContributionItemService.getInstance().contributeToPopupMenu(
            new MenuManager(), new MyWorkBenchPart());
        assertTrue(
            "The policy should be called at least once", ContributionPolicy.getCallCount() >= 1); //$NON-NLS-1$

    }

	/**
	 * Toggles the enablement of the activity <code>MY_ACTIVITY_ID</code>
	 * which is defined in the plugin.xml.
	 * 
     * @param activityID the activity id
	 * @param enabled true to enable; false to disable
	 */
	private void toggleActivity(String activityID, boolean enabled) {
		IWorkbenchActivitySupport workbenchActivitySupport = PlatformUI
			.getWorkbench().getActivitySupport();

		Set enabledActivityIds = new HashSet(workbenchActivitySupport
			.getActivityManager().getEnabledActivityIds());

		boolean changeMade = enabled ? enabledActivityIds.add(activityID)
			: enabledActivityIds.remove(activityID);

		if (changeMade) {
			workbenchActivitySupport.setEnabledActivityIds(enabledActivityIds);
		}
	}

	/**
	 * Validates the presence and visibility state of some of the entries in the
	 * action bars.
	 * 
	 * @param actionBars
	 * @param provider2Contributed
	 *            Should items from provider2 be contributed?
	 * @param provider2Visible
	 *            Should items from provider2 be visible?
	 */
	private void validateActionBars(IActionBars actionBars,
			boolean provider2Contributed, boolean provider2Visible) {

		// a few tests to make sure things were contributed that should have
		// been
		assertNotNull(actionBars.getToolBarManager().find(
			ContributionItemProvider1.ACTION1));

		if (provider2Contributed) {
			assertNotNull(actionBars.getToolBarManager().find(
				ContributionItemProvider2.ACTION2));
			assertNotNull(((IMenuManager) actionBars.getToolBarManager().find(
				ContributionItemProvider1.MENU1))
				.find(ContributionItemProvider2.MENU2));
		} else {
			assertNull(actionBars.getToolBarManager().find(
				ContributionItemProvider2.ACTION2));
			assertNull(((IMenuManager) actionBars.getToolBarManager().find(
				ContributionItemProvider1.MENU1))
				.find(ContributionItemProvider2.MENU2));
		}

		if (provider2Contributed) {
			validateVisibility(actionBars.getMenuManager().getItems(),
				provider2Visible);
			validateVisibility(actionBars.getToolBarManager().getItems(),
				provider2Visible);
		}
	}

	/**
	 * Validates the visibility state of any <code>ACTION2</code> contribution items.
	 * 
	 * @param items
	 *            an array of contribution items
	 * @param visibility
	 *            Should the items be visible?
	 */
	private void validateVisibility(IContributionItem[] items,
			boolean visibility) {
		for (int i = 0; i < items.length; i++) {
			IContributionItem item = items[i];
			// if the item was contributed by provider2, check the visibility
			if (item.getId().equals(ContributionItemProvider2.ACTION2)) { 
				assertEquals(visibility, item.isVisible());
			}
			if (item instanceof IMenuManager) {
				validateVisibility(((IMenuManager) item).getItems(), visibility);
			}
		}
	}

	/**
	 * Prints out the contribution items in the actionbars to the console. Used
	 * for debugging.
	 */
//	private void printActionBars(IActionBars actionBars) {
//		System.out.println("    ******************************"); //$NON-NLS-1$
//		System.out.println("TOOLBAR: "); //$NON-NLS-1$
//		printContributionItems(actionBars.getToolBarManager().getItems(), "  "); //$NON-NLS-1$
//		System.out.println("MENU: "); //$NON-NLS-1$
//		printContributionItems(actionBars.getMenuManager().getItems(), "  "); //$NON-NLS-1$
//		System.out.println("    ******************************"); //$NON-NLS-1$
//	}
//
//	private void printContributionItems(IContributionItem[] items, String prefix) {
//		for (int i = 0; i < items.length; i++) {
//			IContributionItem item = items[i];
//			System.out.println(prefix + item.getId());
//			if (item instanceof IMenuManager) {
//				printContributionItems(((IMenuManager) item).getItems(), prefix
//					+ "  "); //$NON-NLS-1$
//			}
//		}
//	}


}
