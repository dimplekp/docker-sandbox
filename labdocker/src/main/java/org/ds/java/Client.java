package org.ds.java;

import java.net.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.*;
import java.time.Duration;
import java.time.Instant;

class Client implements Runnable {

	static boolean isServerStopped = false;

	static DataOutputStream dout;

	public Client(DataOutputStream dout) {
		Client.dout = dout;
	}

	@Override
	public void run() {
		sendLineThread();
	}

	public static void main(String args[]) throws Exception {
		// /Users/dimple/GoogleDrive/dpatel/Personal/School/ds-projects/ucsd-projects/docker-lab/string.txt
		String file = args[0];
		int portNum = Integer.valueOf(args[1]);
		List<String> lines = new ArrayList<String>();
		try (Stream<String> streams = Files.lines(Paths.get(file))) {
			lines = streams.collect(Collectors.toList());
		}
		for (int i = 0; i < lines.size(); i++) {
			lines.set(i, lines.get(i).toUpperCase());
		}

		String serverIP = "172.17.0.2";
		Socket s = new Socket(serverIP, portNum);
		DataInputStream din = new DataInputStream(s.getInputStream());
		DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());
		String lineFromServer = "";

		// Start thread to send LINE to server
		Runnable r = new Client(dataOut);
		new Thread(r).start();

		// Read line from server
		while (!isServerStopped) {
			try {
				lineFromServer = din.readUTF();
				if (lines.contains(lineFromServer)) {
					System.out.println("OK");
				} else {
					System.out.println("MISSING");
				}
			} catch (Exception e) {
				e.getMessage();
			}
		}
		System.out.println("Exiting. Connection closed with server!");
		din.close();
		dataOut.close();
		dout.close();
		s.close();
		System.exit(0);
	}

	public static void sendLineThread() {
		Timer timer = new Timer();
		final Instant t1 = Instant.now();

		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					long ms = Duration.between(t1, Instant.now()).toMillis();
					if (ms < 30000 && isServerStopped != true) {
						dout.writeUTF("LINE\n");
					} else {
						isServerStopped = true;
						dout.writeUTF("stop");
						return;
					}
				} catch (IOException e) {
					e.getMessage();
					e.printStackTrace();
				}
			}
		}, 0, 3000);
		return;
	}
}
