/******************************************************************************
*
* Copyright: Intellectual Property of Four Elements Capital Pte Ltd, Singapore.
* All rights reserved.
*
******************************************************************************/

package com.fourelementscapital.alarm;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Socket Server to listen & process client's message after receive it 
 */
public class SocketServer {
	
	private static final Logger log = LogManager.getLogger(SocketServer.class.getName());
	
	/**
	 * Listening message and process it. Use SocketProcess as dependency injection.
	 * @param socketProcess SocketProcess as dependency injection to process message
	 * @throws Exception
	 */	
	public static void listen(SocketProcess socketProcess) throws Exception {
	    AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open();
	    String host = "0.0.0.0"; // all IPs available on this host 
	    int port = Integer.parseInt(Config.getConfigValue_socket("socket_server_port_number"));
	    InetSocketAddress sAddr = new InetSocketAddress(host, port);
	    server.bind(sAddr);
	    System.out.print(String.format("Server is listening at %s%n", sAddr)); // sysout, not log
	    AttachmentServer attach = new AttachmentServer();
	    attach.server = server;
	    attach.socketProcess = socketProcess;
	    server.accept(attach, new ConnectionHandlerServer());
	    Thread.currentThread().join();
	}	
}

/**
 * Class as value container to be passed among methods
 */
class AttachmentServer {
	AsynchronousServerSocketChannel server;
	AsynchronousSocketChannel client;
	ByteBuffer buffer;
	SocketAddress clientAddr;
	SocketProcess socketProcess;
}


/**
 * Server connection handler
 */
class ConnectionHandlerServer implements CompletionHandler<AsynchronousSocketChannel, AttachmentServer> {
	
	private static final Logger log = LogManager.getLogger(ConnectionHandlerServer.class.getName());
	
	private static final int BUFFER_ALLOCATED_IN_BYTE = 10000;  // how many chars allowed in 1 socket message;
	
	/**
	 * An override method to be processed after operation has completed
	 * @param client AsynchronousSocketChannel object
	 * @param attach AttachmentServer object
	 */		
	@Override
	public void completed(AsynchronousSocketChannel client, AttachmentServer attach) {
		try {
			SocketAddress clientAddr = client.getRemoteAddress();
			System.out.print(String.format("Accepted connection from %s%n", clientAddr));  // sysout, not log
			attach.server.accept(attach, this);
			ReadWriteHandlerServer rwHandler = new ReadWriteHandlerServer();
			AttachmentServer newAttach = new AttachmentServer();
			newAttach.server = attach.server;
			newAttach.client = client;
			//newAttach.buffer = ByteBuffer.allocate(2048);
			newAttach.buffer = ByteBuffer.allocate(BUFFER_ALLOCATED_IN_BYTE);
			newAttach.clientAddr = clientAddr;
			newAttach.socketProcess = attach.socketProcess;
			client.read(newAttach.buffer, newAttach, rwHandler);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * An override method to be processed when operation fails
	 * @param e Throwable object
	 * @param attach AttachmentServer object
	 */		
	@Override
	public void failed(Throwable e, AttachmentServer attach) {
		log.error("Failed to accept a connection.");
		e.printStackTrace();
	}
}


/**
 * Class to read / write process
 */
class ReadWriteHandlerServer implements CompletionHandler<Integer, AttachmentServer> {
	
	private static final Logger log = LogManager.getLogger(ReadWriteHandlerServer.class.getName());
	
	/**
	 * An override method to be processed after operation has completed
	 * @param result Result
	 * @param attach AttachmentServer object
	 */		
	@Override
	public void completed(Integer result, AttachmentServer attach) {
		
    	attach.buffer.flip();
    	int limits = attach.buffer.limit();
    	byte bytes[] = new byte[limits];
    	attach.buffer.get(bytes, 0, limits);		
    	Charset cs = Charset.forName("UTF-8");
    	String msg = new String(bytes, cs);
    	log.debug("SocketProcess.run() message : " + msg);
		attach.socketProcess.run(msg); // run socket process
		
		attach.buffer.rewind();
	}

	/**
	 * An override method to be processed when operation fails
	 * @param e Throwable object
	 * @param attach AttachmentServer object
	 */			
	@Override
	public void failed(Throwable e, AttachmentServer attach) {
		e.printStackTrace();
	}
}


 