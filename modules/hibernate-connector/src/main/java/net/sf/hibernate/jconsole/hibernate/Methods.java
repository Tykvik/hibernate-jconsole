/*
 * Copyright (c) 2010
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

package net.sf.hibernate.jconsole.hibernate;

import net.sf.hibernate.jconsole.stats.Names;

import javax.management.*;
import java.io.IOException;

import static net.sf.hibernate.jconsole.util.HibernateJmxBinding.HIBERNATE_STATISTICS;

/**
 * Enumerates used methods.
 *
 * @author Juergen_Kellerer, 2009-11-19
 * @version 1.0
 */
public enum Methods {

	getEntityStatistics(HIBERNATE_STATISTICS, Names.getEntityStatistics, String.class.getName()),
	getCollectionStatistics(HIBERNATE_STATISTICS, Names.getCollectionStatistics, String.class.getName()),
	getSecondLevelCacheStatistics(HIBERNATE_STATISTICS, Names.getSecondLevelCacheStatistics, String.class.getName()),
	getQueryStatistics(HIBERNATE_STATISTICS, Names.getQueryStatistics, String.class.getName()),
	clear(HIBERNATE_STATISTICS, Names.clear),;

	private ObjectName mBeanName;
	private Names name;
	private String[] signature;

	private Methods(ObjectName mBeanName, Names name, String... signature) {
		this.mBeanName = mBeanName;
		this.name = name;
		this.signature = signature;
	}

	public String[] getSignature() {
		return signature.clone();
	}

	/**
	 * Invokes the method on the given connection and args.
	 *
	 * @param connection The connection to execute the method on.
	 * @param args	   The argument list to use for the invokation.
	 *                   (Note: The arguments must match the method signature in type and amount)
	 * @param <R>        The return type of the method.
	 *                   (Note: If the return type is incorrectly specified, a class cast exception will be thrown)
	 * @return The value returned by the method invokation.
	 * @throws InstanceNotFoundException The MBean specified is not registered in the MBean server.
	 * @throws MBeanException			Wraps an exception thrown by the MBean's invoked method.
	 * @throws ReflectionException	   Wraps a <CODE>java.lang.Exception</CODE> thrown while trying
	 *                                   to invoke the method.
	 * @throws IOException			   A communication problem occurred when talking to the MBean server.
	 */
	@SuppressWarnings("unchecked")
	public <R> R invoke(MBeanServerConnection connection, Object... args)
			throws InstanceNotFoundException, IOException, ReflectionException, MBeanException {
		return (R) connection.invoke(mBeanName, name.name(), args, signature);
	}
}
