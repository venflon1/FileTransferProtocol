package my_ftp_server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Logger;import javax.swing.event.ListSelectionEvent;


public class ClientFTP3 {
	
	private static Logger logger = Logger.getLogger(ClientFTP3.class.getName());
	
	public static void main(String[] args) {
		try {
			Socket socketClient = new Socket(InetAddress.getLocalHost(), 9988);
			logger.info("ClientFTP is up....\n");
			
			String command = new String("quit");
			ObjectOutputStream os = new ObjectOutputStream(socketClient.getOutputStream());
			os.writeObject(command);
			
			ObjectInputStream is = new ObjectInputStream(socketClient.getInputStream());
			String message = (String) is.readObject();

			if(message.equalsIgnoreCase("quit"))
				logger.info("ClientFTP quit...");
			
			os.close();
			is.close();
			
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
