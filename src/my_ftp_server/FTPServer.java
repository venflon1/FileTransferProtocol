package my_ftp_server;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


public class FTPServer {
	
	private Logger logger = Logger.getLogger(FTPServer.class.getName());
	
	private static InetAddress hostName;
	private static int port ;
	private static File absoluteDir;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	
	public FTPServer(InetAddress address, int port) throws IOException {
		super();
		this.hostName = address;
		this.port = port;		
		File directoryFTP = new File("C:\\Users\\titano\\Desktop\\rootFTPDirectory");
		if(!directoryFTP.exists()) {
			logger.severe("ERRORE NELLA CARTELLA FTP_SERVER!\n");
			// TO-DO
		}
		else {
			this.absoluteDir = directoryFTP;
			this.serverSocket = new ServerSocket(FTPServer.port);
		}
	}
	
	public void handle() throws IOException, ClassNotFoundException {
		
		logger.info("FTPServer is up.....\n");
		while(true) {
			
			this.clientSocket = this.serverSocket.accept();
			logger.info("connection established with client: " + this.clientSocket.getInetAddress() + "\n");
			boolean exit = false;

			do{	
				String receiveString = (String) new ObjectInputStream(this.clientSocket.getInputStream()).readObject();
				String[] rcvStringSplit = receiveString.split(" ");
				String command = rcvStringSplit[0];
				logger.info("command received: " + command);

				switch(command) {
					case "ls":{
								List<String> listFile = Arrays.asList(this.absoluteDir.list());
								
								if(listFile == null)
									listFile = new ArrayList<String>();
								
								ObjectOutputStream out = new ObjectOutputStream(this.clientSocket.getOutputStream());
								out.writeObject(listFile);
								logger.info("send listFile to client " + this.clientSocket.getInetAddress() + "\n");
								break;
							  }
					case "get":{
									List<String> listFile = Arrays.asList(this.absoluteDir.list());
									if(listFile.contains(rcvStringSplit[1])) {
										BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(this.absoluteDir+"\\"+rcvStringSplit[1])));
										byte[] bytes = bis.readAllBytes();
										logger.info("reading file: " + rcvStringSplit[1] + " from disk\n");
										DataOutputStream out = new DataOutputStream(this.clientSocket.getOutputStream());
										logger.info("send file: " + rcvStringSplit[1] + " to client: " + this.clientSocket.getInetAddress() + "\n");
										out.write(bytes);									
										logger.info(Arrays.toString(bytes));
										bis.close();
										out.close();
									}
									else logger.info("file " + rcvStringSplit[1] + "not in disk");
									break;
								}
					case "quit":{
									ObjectOutputStream out = new ObjectOutputStream(this.clientSocket.getOutputStream());
									out.writeObject("quit");
									logger.info("ServerFtp closing...");
									exit = true;
									logger.info("exited\n");
//									if(this.clientSocket != null) {
//										if( this.clientSocket.getInputStream() != null)
//											this.clientSocket.getInputStream().close();
//										if(this.clientSocket.getOutputStream() != null)
//											this.clientSocket.getOutputStream().close();
//										this.clientSocket.close();
										logger.info("close connection with client: " + this.clientSocket.getInetAddress() + "\n");
//									}
									break;
								}
				} 
			}while(!exit);
		} // end while
	} // end method handle	
	
	public static void main(String[] args) {
		
		InetAddress address;
		try {
			address = InetAddress.getLocalHost();
			int port = 9988;
			
			FTPServer myFTPServer = new FTPServer(address, port);
			myFTPServer.handle();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}