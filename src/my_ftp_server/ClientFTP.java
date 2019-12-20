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
import java.util.logging.Logger;


public class ClientFTP {
	
	private static Logger logger = Logger.getLogger(ClientFTP.class.getName());
	
	public static void main(String[] args) {
		try {
			Socket socketClient = new Socket(InetAddress.getLocalHost(), 9988);
			logger.info("ClientFTP is up....\n");
			
			String command = new String("get colors_image.jpg");
			ObjectOutputStream os = new ObjectOutputStream(socketClient.getOutputStream());
			os.writeObject(command);
			
			DataInputStream is = new DataInputStream(socketClient.getInputStream());

			logger.info("after read bytes" );
			byte[] readBytes = is.readAllBytes();
			logger.info("read bytes" );

			saveFile(new File("C:\\Users\\titano\\Desktop\\copy"), readBytes);
			logger.info("file saved....written " + readBytes.length + " Bytesis.readAllBytes();"  );
			
			os.close();
			is.close();
			
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private static void saveFile(File file, byte[] bytes) throws IOException {
		BufferedOutputStream bis = new BufferedOutputStream(new FileOutputStream(file));
		bis.write(bytes);
		bis.close();
	}
}
