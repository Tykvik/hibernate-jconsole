/*
 * Copyright (c) 2009
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

package net.sf.hibernate.jconsole.util;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Is a utility class for modifying the system classpath.
 *
 * @author Juergen_Kellerer, 2009-11-18
 * @version 1.0
 */
public final class ClasspathUtil {

	private static Method addURL;

	static {
		try {
			addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			addURL.setAccessible(true);
		} catch (Throwable t) {
			throw new RuntimeException("Error, could not add URL to system classloader", t);
		}
	}

	public static void addURLs(List<URL> urls) {
		ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
		if (systemClassLoader instanceof URLClassLoader) {
			URLClassLoader sysloader = (URLClassLoader) systemClassLoader;
			try {
				for (URL url : urls) {
					addURL.invoke(sysloader, url);

					// Reconstruct classpath property
					if ("file".equals(url.getProtocol())) {
						String cp = System.getProperty("java.class.path", "");
						System.setProperty("java.class.path",
								("".equals(cp) ? cp : cp + File.pathSeparatorChar) +
										new File(url.toURI()).getCanonicalPath());
					}
				}
			} catch (Throwable t) {
				throw new RuntimeException("Error, could not add URL to system classloader", t);
			}
		}
	}

	public static boolean addJars(File directory, boolean recursive) {
		return addJars(directory, recursive, null);
	}

	public static boolean addJars(File directory, boolean recursive, FilenameFilter filter) {
		File[] files = directory.isFile() ? new File[]{directory} : directory.listFiles(filter);
		return addJars(files, recursive);
	}

	public static boolean addJars(File[] file, boolean recursive) {
		return addJars(file, recursive, null);
	}

	public static boolean addJars(File[] file, boolean recursive, FilenameFilter filter) {
		if (file == null || file.length == 0)
			return false;
		List<URL> jars = new ArrayList<URL>(file.length);
		for (File f : file) {
			if (f.isDirectory()) {
				if (recursive)
					addJars(f.listFiles(filter), recursive, filter);
				continue;
			}

			if (filter != null && !filter.accept(f.getParentFile(), f.getName()))
				continue;

			if (f.getName().toLowerCase().endsWith(".jar"))
				try {
					jars.add(f.toURI().toURL());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
		}

		addURLs(jars);

		return !jars.isEmpty();
	}

	private ClasspathUtil() {
	}
}
