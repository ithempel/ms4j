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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to handle the connection to the manage sieve server.
 * <p>
 * The class will handle the tcp/ip connection to the server. There are methods
 * to communicate (send / receive) with the manage sieve server.
 * 
 * @author Sebastian Hempel <shempel@it-hempel.de>
 */
public class Connection {

	private static final int MANAGE_SIEVE_DEFAULT_PORT = 2000;

	private static final int WAIT_INTERVALL_IN_MS = 30;
	private static final int INTERVALLS_TO_WAIT = 10;

	private static final Logger logger = LoggerFactory.getLogger(Connection.class);

	private Socket socket;

	private BufferedReader inputReader;

	/**
	 * Create a new connection to the manage sieve server with the given
	 * (domain) name. The default port 2000 for a manage sieve connection is used.
	 * 
	 * @param host name of the server to connect to
	 * 
	 * @throws UnknownHostException The given server name is not known.
	 */
	public Connection(String host) throws UnknownHostException {
		this(host, MANAGE_SIEVE_DEFAULT_PORT);
	}

	/**
	 * Create a new connection to the manage sieve server with the given (domain)
	 * name and the given port.
	 * 
	 * @param host name of the server to connect to
	 * @param port port to use
	 * 
	 * @throws UnknownHostException The given server name is not known
	 */
	public Connection(String host, int port) throws UnknownHostException {
		try {
			InetAddress address = InetAddress.getByName(host);
			socket = new Socket(address, port);
			inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			logger.info("Successfully connect to server {} on port {}", host, port);
		} catch (UnknownHostException e) {
			logger.error("UnkownHostException connecting to server {} on port {}", host,
					port, e);
			throw e;
		} catch (IOException e) {
			logger.error("IOException connecting to server {} on port {}", host, port, e);
		}
	}

	/**
	 * Close the connection to the manage sieve server.
	 */
	public void close() {
		try {
			socket.shutdownInput();
			socket.close();

			logger.info("Close connection to server");
		} catch (IOException e) {
			logger.error("IOException closing connection to server", e);
		}
	}

	/**
	 * Check for an established connection to the manage sieve server.
	 * 
	 * @return true, if the connection is active
	 */
	public boolean isConnected() {
		return !socket.isClosed() && socket.isConnected();
	}

	/**
	 * Get the response lines the server transmitted to the client.
	 * <p>
	 * The Method will return a TImeoutException, if the server does not
	 * transmit at least one line in time.
	 * 
	 * @return Lines transmitted as the result from the server
	 * 
	 * @throws TimeoutException Server did not transmit at least on line in time
	 */
	public String[] getResponse() throws TimeoutException {
		List<String> responseBuffer = new LinkedList<String>();

		try {
			waitForServer();
			while (inputReader.ready()) {
				String line = inputReader.readLine();
				responseBuffer.add(line);
			}
		} catch (IOException e) {
			logger.error("IOException while reading lines from server connection", e);
		}

		return responseBuffer.toArray(new String[0]);
	}

	/**
	 * Wait for the server to transmit at least one complete line.
	 * <p>
	 * The method will wait up to WAIT_INTERVALL_IN_MS * INTERVALLS_TO_WAIT milliseconds
	 * for the server to transmit at least one complete line.
	 * 
	 * @throws IOException Exception while checking for result of manage siever server
	 * @throws TimeoutException no response from server
	 */
	private void waitForServer() throws IOException, TimeoutException {
		int retries = INTERVALLS_TO_WAIT;
		while (!inputReader.ready() && retries > 0) {
			try {
				Thread.sleep(WAIT_INTERVALL_IN_MS);
			} catch (InterruptedException e) {
				logger.warn("Waiting for resonse of server was interrupted.");
			}

			retries--;
		}

		if (retries == 0) {
			throw new TimeoutException("no response from server");
		}
	}

}
