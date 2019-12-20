package my_ftp_server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Logger;

import com.oracle.tools.packager.Log;

public class FTPClient {

	private Logger logger = Logger.getLogger(FTPServer.class.getName());

	private InetAddress addressServer;
	private int serverPort;
	private Socket socketFTPClient;

	public FTPClient(InetAddress serverAddress, int serverPort) throws IOException {
		this.addressServer = serverAddress;
		this.serverPort = serverPort;
		this.socketFTPClient = new Socket(this.addressServer, serverPort);
		if (this.socketFTPClient != null)
			logger.info("connection with server ftp established....\n");
		else
			logger.severe("connection refused by server ftp!");
	}

	public void handle() throws IOException {
		ObjectInputStream is = new ObjectInputStream(this.socketFTPClient.getInputStream());
		ObjectOutputStream os = new ObjectOutputStream(this.socketFTPClient.getOutputStream());
		Scanner stdin = new Scanner(System.in);
		boolean exit = false;

		do {
			logger.info("mm");
			System.out.print("ftp>");
			String lineInput = stdin.nextLine();
			String[] strReceiveSplit = lineInput.split(" ");
			String command = strReceiveSplit[0].trim();

			switch (command.toLowerCase()) {
				case "ls": {
					os.writeObject(command);
					Log.info("send command: " + command +"\n");
					break;
				}
				case "get": {
					os.writeObject(strReceiveSplit[1]);
	
					DataInputStream dis = new DataInputStream(this.socketFTPClient.getInputStream());
					logger.info("after read bytes\n");
					byte[] readBytes = is.readAllBytes();
					logger.info("read bytes\n");
	
					saveFile(new File("C:\\Users\\titano\\Desktop\\copy"), readBytes);
					logger.info("file saved....written " + readBytes.length + " Bytes");
	
					dis.close();
					break;
				}
				case "quit": {
					if(is != null)
						is.close();
					if(os != null)
						os.close();
					logger.info("ClientFTP closing...\n");
					exit = true;
					break;
				}
			}
		} while(!exit);
	} // end method

	private void saveFile(File file, byte[] bytes) throws IOException {
		BufferedOutputStream bis = new BufferedOutputStream(new FileOutputStream(file));
		bis.write(bytes);
		bis.close();
	}

	public static void main(String[] args) {
		
		try {
			InetAddress addressServer = InetAddress.getLocalHost();
			int port = 9988;
			
			FTPClient myClientFTP = new FTPClient(addressServer, port);
			myClientFTP.handle();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}