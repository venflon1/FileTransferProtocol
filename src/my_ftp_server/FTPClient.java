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
import java.util.List;
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

	public void handle() throws IOException, ClassNotFoundException {
		Scanner stdin = new Scanner(System.in);
		boolean exit = false;

		do {
			System.out.print("ftp>");
			String lineInput = stdin.nextLine();
			String[] strReceiveSplit = lineInput.split(" ");
			String command = strReceiveSplit[0].trim();

			switch (command.toLowerCase()) {
				case "ls": {
					ObjectOutputStream os = new ObjectOutputStream(this.socketFTPClient.getOutputStream());
					os.writeObject(command);
					Log.info("send command: " + command +"\n");
					
					ObjectInputStream is = new ObjectInputStream(this.socketFTPClient.getInputStream());
					List<String> listFile = (List<String>) is.readObject();
					if(listFile.size() == 0)
						logger.info("no such files into FTP_Server_directory\n");
					else
						logger.info("files:\n" + listFile + "\n");
					os.flush();
					break;
				}
				case "get": {
					logger.info("get this file from server: " + strReceiveSplit[1]);
					ObjectOutputStream os = new ObjectOutputStream(this.socketFTPClient.getOutputStream());
					os.writeObject(lineInput);
					os.flush();
					
					DataInputStream dis = new DataInputStream(this.socketFTPClient.getInputStream());
					logger.info("after read bytes\n");
					byte[] readBytes = dis.readAllBytes();
					logger.info("read bytes\n");
	
					saveFile(new File("C:\\Users\\titano\\Desktop\\copy"), readBytes);
					logger.info("file saved....written " + readBytes.length + " Bytes");
	
					dis.close();
					os.close();
					break;
				}
				case "quit": {
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
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
