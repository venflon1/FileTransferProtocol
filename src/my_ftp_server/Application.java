package my_ftp_server;

import java.io.IOException;
import java.net.InetAddress;

public class Application {
	public static void main(String[] args) {
		try {
			InetAddress address = InetAddress.getLocalHost();
			int port = 9876;
			
			FTPServer myFTPServer = new FTPServer(address, port);
			myFTPServer.handle();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
