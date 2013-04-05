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

public class Connection {

	private static final int MANAGE_SIEVE_DEFAULT_PORT = 2000;

	private Socket socket;

	public Connection(String host) throws UnknownHostException {
		this(host, MANAGE_SIEVE_DEFAULT_PORT);
	}

	public Connection(String host, int port) throws UnknownHostException {
		try {
			InetAddress address = InetAddress.getByName(host);
			socket = new Socket(address, port);
		} catch (UnknownHostException e) {
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isConnected() {
		return !socket.isClosed() && socket.isConnected();
	}

	public String[] getResponse() {
		List<String> responseBuffer = new LinkedList<String>();

		try {
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (inputReader.ready()) {
				String line = inputReader.readLine();
				responseBuffer.add(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return responseBuffer.toArray(new String[0]);
	}

}
