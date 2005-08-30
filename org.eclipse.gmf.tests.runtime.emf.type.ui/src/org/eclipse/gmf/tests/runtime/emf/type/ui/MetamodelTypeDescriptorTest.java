package org.eclipse.gmf.tests.runtime.emf.type.ui;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.gmf.runtime.emf.type.core.IMetamodelType;
import org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.MetamodelTypeDescriptor;
import org.eclipse.gmf.tests.runtime.emf.type.ui.internal.EmployeeType;


public class MetamodelTypeDescriptorTest extends TestCase {

	private MetamodelTypeDescriptor fixture;

	public MetamodelTypeDescriptorTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(MetamodelTypeDescriptorTest.class);
	}

	protected MetamodelTypeDescriptor getFixture() {
		return fixture;
	}

	protected void setFixture(MetamodelTypeDescriptor fixture) {
		this.fixture = fixture;
	}

	public void test_metamodelTypeConstructor() {

		setFixture(new MetamodelTypeDescriptor((IMetamodelType) EmployeeType.STUDENT));
		
		assertEquals(EmployeeType.STUDENT.getDisplayName(), getFixture().getName());
		assertEquals(EmployeeType.STUDENT.getId(), getFixture().getId());
		assertEquals(EmployeeType.STUDENT.getIconURL(), getFixture().getIconURL());
		assertEquals(EmployeeType.STUDENT.getEClass(), getFixture().getEClass());
		assertEquals(EmployeeType.STUDENT.getEditHelper(), getFixture().getEditHelper());
		assertEquals(EmployeeType.STUDENT, getFixture().getElementType());
	}
}
