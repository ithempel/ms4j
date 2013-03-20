# ms4j - Manage Sieve for Java

## Purpose

Implementation of the ManageSieve protocol ([RFC 5804](http://www.ietf.org/rfc/rfc5804.txt))
as a java library (JAR). The protocol is used to manage sieve scripts - create, update, delete,
(de-)activate - on servers using the Sieve script language to filter mails.

The goal of this project is to implement the client part of the protocol in java. The library
can be used to access an IMAP server for managing the sieve scripts of a user.

## Work in Progress

Further information about the library and its usage will be published at this place. For
now only the first steps of the implementation are known.

The first steps will be to implement the following functions:

 * Connect to a server with plain password authentication
 * Get the list of scripts
 * Activate and Deactivate a script
 * Upload a new script to the server

## License

ms4j is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

ms4j is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with ms4j.  If not, see <http://www.gnu.org/licenses/>.
