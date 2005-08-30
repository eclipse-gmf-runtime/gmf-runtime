package org.eclipse.gmf.examples.runtime.ui.pde.internal.wizards;

import java.net.URL;

import org.eclipse.core.runtime.Path;

import org.eclipse.gmf.examples.runtime.ui.pde.internal.GmfExamplesPlugin;
import org.eclipse.gmf.examples.runtime.ui.pde.internal.l10n.ResourceManager;

/**
 * This wizard generates the geoshapes example plug-in.
 */
public class LogicNewWizard
	extends ProjectUnzipperNewWizard {

	/**
	 * Wizard page title
	 */
	private static final String LOGIC_WIZARD_CREATEPROJECTPAGE_TITLE = ResourceManager
		.getI18NString("Logic.wizard.createProjectPage.title"); //$NON-NLS-1$

	/**
	 * Wizard page description
	 */
	private static final String LOGIC_WIZARD_CREATEPROJECTPAGE_DESC = ResourceManager
		.getI18NString("Logic.wizard.createProjectPage.desc"); //$NON-NLS-1$

	/**
	 * Constructor.
	 */
	public LogicNewWizard() {
		super("exampleProjectLocation", //$NON-NLS-1$
			LOGIC_WIZARD_CREATEPROJECTPAGE_TITLE,
			LOGIC_WIZARD_CREATEPROJECTPAGE_DESC, new URL[] {
				GmfExamplesPlugin.getDefault().find(
					new Path("examples/logic.zip")), //$NON-NLS-1$
				GmfExamplesPlugin.getDefault().find(
					new Path("examples/logicSemantic.zip")), //$NON-NLS-1$
				GmfExamplesPlugin.getDefault().find(
					new Path("examples/logicSemanticEdit.zip")) //$NON-NLS-1$
			}, new String[] {"{0}", "{0}.semantic", "{0}.semantic.edit"} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		);
	}
}
