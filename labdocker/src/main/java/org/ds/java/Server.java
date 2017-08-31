package org.ds.java;

import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.*;

class Server {
	public static void main(String args[]) throws Exception {
		String file = args[0];
		int portNum = Integer.valueOf(args[1]);
		ServerSocket ss = new ServerSocket(portNum);
		printIP();
		List<String> lines = new ArrayList<String>();
		try (Stream<String> streams = Files.lines(Paths.get(file))) {
			lines = streams.collect(Collectors.toList());
		}
		System.out.println("SERVER STARTED SUCCESSFULLY");
		Socket s = ss.accept();
		DataInputStream din = new DataInputStream(s.getInputStream());
		DataOutputStream dout = new DataOutputStream(s.getOutputStream());

		String lineFromClient = "";
		int indx = -1;
		while (!lineFromClient.equals("stop")) {
			lineFromClient = din.readUTF();
			if (lineFromClient.equals("LINE\n")) {
				if (indx == lines.size() - 1) {
					indx = -1;
				}
				System.out.println("Received LINE");
				String sendToClient = lines.get(++indx).toUpperCase();
				dout.writeUTF(sendToClient);
				dout.flush();
			}
		}

		din.close();
		dout.close();
		s.close();
		ss.close();
		System.out.println("SERVER SHUT DOWN NORMALLY");
		System.exit(0);
	}

	private static void printIP() throws SocketException, UnknownHostException {
		System.out.println("Server IP : ");
		System.out.println();
		Enumeration<?> e = NetworkInterface.getNetworkInterfaces();
		while (e.hasMoreElements()) {
			NetworkInterface n = (NetworkInterface) e.nextElement();
			Enumeration<?> ee = n.getInetAddresses();
			while (ee.hasMoreElements()) {
				InetAddress i = (InetAddress) ee.nextElement();
				System.out.println(i.getHostAddress());
			}
		}
		System.out.println();
	}
}
