/**
 * ms4j - https://github.com/ithempel/ms4j
 *
 * Copyright (C) 2013 Sebastian Hempel
 *
 * ms4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * ms4j is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ms4j. If not, see <http://www.gnu.org/licenses/>.
 */
package de.ithempel.ms4j;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

/**
 * This class contains tests to open the connection to a ManageSieve server.
 * 
 * @author Sebastian Hempel <shempel@it-hempel.de>
 */
public class ServerConnectTest {

	private static final String TEST_SERVER = "zeus.olymp.local";

	@Test
	public void noConnection_openConnection_hasConnection() throws UnknownHostException {
		Connection connection = new Connection(TEST_SERVER);

		assertThat(connection.isConnected(), is(Boolean.TRUE));

		connection.close();
	}

	@Test
	public void hasConnection_closeConnection_hasNoConnection() throws UnknownHostException {
		Connection connection = new Connection(TEST_SERVER);

		connection.close();

		assertThat(connection.isConnected(), is(Boolean.FALSE));
	}

	@Test(expected=UnknownHostException.class)
	public void noConnection_unkownHost_unkownHostException() throws UnknownHostException {
		new Connection("abc");
	}

	@Test
	public void noConnection_openConnection_getInitalResponse() throws UnknownHostException, TimeoutException {
		Connection connection = new Connection(TEST_SERVER);

		String[] response = connection.getResponse();
		connection.close();

		assertThat(response.length, not(equalTo(0)));
	}

	@Test
	public void openConnection_getInitialResponse_ResultOK() throws UnknownHostException, TimeoutException {
		Connection connection = new Connection(TEST_SERVER);
		String[] response = connection.getResponse();
		connection.close();

		String lastLine = response[response.length - 1];

		assertThat(lastLine, startsWith("OK"));
	}

	@Test(expected=TimeoutException.class)
	public void openConnection_getSomeThingAfterInitialResponse_TimeoutException() throws UnknownHostException, TimeoutException {
		Connection connection = new Connection(TEST_SERVER);
		connection.getResponse();

		connection.getResponse();

		fail("got something after initial response");
	}

}
