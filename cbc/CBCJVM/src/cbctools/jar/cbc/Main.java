package cbctools.jar.cbc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
	ServerSocket server = null;
	Socket client = null;
	InputStream inStream;
	OutputStream outStream;
	DataInputStream inDataStream;
	DataOutputStream outDataStream;
	public Main() throws IOException {
		server = new ServerSocket();
	}
	public static void main(String[] args) {
		System.out.println("Awaiting connections...");
	}
}
