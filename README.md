# ms4j - Manage Sieve for Java

## Purpose

Implementation of the ManageSieve protocol ([RFC 5804](http://www.ietf.org/rfc/rfc5804.txt))
as a java library (JAR). The protocol is used to manage sieve scripts - create, update, delete,
(de-)activate - on servers using the Sieve script language to filter mails.

The goal of this project is to implement the client part of the protocol in java. The library
can be used to access a IMAP server for managing the sieve scripts of a user.