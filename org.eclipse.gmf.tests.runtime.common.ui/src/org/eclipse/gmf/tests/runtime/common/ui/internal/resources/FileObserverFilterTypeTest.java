/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.tests.runtime.common.ui.internal.resources;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gmf.runtime.common.ui.internal.resources.FileObserverFilterType;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;


/**
 * 
 * 
 * @author Anthony Hunter 
 * <a href="mailto:ahunter@rational.com">ahunter@rational.com</a>
 */
public class FileObserverFilterTypeTest extends TestCase {
	
	protected static class Fixture extends FileObserverFilterType {

		private static final long serialVersionUID = 1;
		
		protected Fixture() {
			super("Fixture", 0); //$NON-NLS-1$
		}

		protected List getValues() {
			return super.getValues();
		}

	}

	private Fixture fixture = null;

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(FileObserverFilterTypeTest.class);
	}

	public FileObserverFilterTypeTest(String name) {
		super(name);
	}

	protected Fixture getFixture() {
		return fixture;
	}

	private void setFixture(Fixture fixture) {
		this.fixture = fixture;
	}

	protected void setUp() {
		setFixture(new Fixture());
	}

	public void test_readResolve() {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		ObjectOutput output = null;
		ObjectInput input = null;
		try {
			output = new ObjectOutputStream(stream);
			for (Iterator i = getFixture().getValues().iterator();
				i.hasNext();
				) {
				output.writeObject(i.next());
			}
			output.flush();

			input =
				new ObjectInputStream(
					new ByteArrayInputStream(stream.toByteArray()));
			for (Iterator i = getFixture().getValues().iterator();
				i.hasNext();
				) {
				assertSame(i.next(), input.readObject());
			}
		} catch (Exception e) {
			fail();
		} finally {
			try {
				output.close();
				input.close();
			} catch (Exception e) {
				/*Empty block*/
			}
		}
	}
}
