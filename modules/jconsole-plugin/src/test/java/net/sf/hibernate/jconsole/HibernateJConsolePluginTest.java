/*
 * Copyright (c) 2011
 *
 * This file is part of HibernateJConsole.
 *
 *     HibernateJConsole is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     HibernateJConsole is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with HibernateJConsole.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.sf.hibernate.jconsole;

import com.sun.tools.jconsole.JConsoleContext;
import net.sf.hibernate.jconsole.ui.MainTab;
import org.junit.Before;
import org.junit.Test;

import javax.management.MBeanServerConnection;
import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Smoke tests the console initialization and refresh operation.
 *
 * @author juergen, 28.03.11
 */
public class HibernateJConsolePluginTest {

	JConsoleContext context = new JConsoleContext() {
		@Override
		public MBeanServerConnection getMBeanServerConnection() {
			return ManagementFactory.getPlatformMBeanServer();
		}

		@Override
		public ConnectionState getConnectionState() {
			return JConsoleContext.ConnectionState.CONNECTED;
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {

		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {

		}
	};

	HibernateJConsolePlugin plugin = new HibernateJConsolePlugin();

	@Before
	public void setUp() throws Exception {
		plugin.setContext(context);
	}

	@Test
	public void testGetTabs() throws Exception {
		assertNotNull(plugin.getTabs());
	}

	@Test
	public void testNewSwingWorkerCanReturnWorkerForRefresh() throws Exception {
		final AtomicBoolean called = new AtomicBoolean(false);
		plugin.contexts.put("test-receiver", new StatisticsTestContext());
		plugin.tabs.put("test-receiver", new MainTab() {
			@Override
			public void refresh(AbstractStatisticsContext context) {
				called.set(true);
			}
		});

		final SwingWorker<?, ?> swingWorker = plugin.newSwingWorker();
		assertNotNull(swingWorker);

		swingWorker.execute();
		swingWorker.get();

		final Method done = swingWorker.getClass().getDeclaredMethod("done");
		done.setAccessible(true);
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				try {
					done.invoke(swingWorker);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		assertTrue(called.get());
	}
}
