/******************************************************************************
*
* Copyright: Intellectual Property of Four Elements Capital Pte Ltd, Singapore.
* All rights reserved.
*
******************************************************************************/

package com.fourelementscapital.alarm;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Socket Client to send message to Socket Server
 */
public class SocketClient {
	
	private static final Logger log = LogManager.getLogger(SocketClient.class.getName());
	
	/**
	 * Send message to listening socket server
	 * @param message Message being sent
	 * @throws Exception
	 */		
	public static void sendMessage(String message) throws Exception {
		
	    AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
	    String serverAddress = Config.getConfigValue_socket("socket_server_address");
	    int port = Integer.parseInt(Config.getConfigValue_socket("socket_server_port_number"));
	    SocketAddress serverAddr = new InetSocketAddress(serverAddress, port);
	    Future<Void> result = channel.connect(serverAddr);
	    result.get();
	    log.debug("Connected");
	    AttachmentClient attach = new AttachmentClient();
	    attach.channel = channel;
	    attach.buffer = ByteBuffer.allocate(2048);

	    Charset cs = Charset.forName("UTF-8");
	    byte[] data = message.getBytes(cs);
	    attach.buffer.put(data);
	    attach.buffer.flip();

	    ReadWriteHandlerClient readWriteHandler = new ReadWriteHandlerClient();
	    channel.write(attach.buffer, attach, readWriteHandler);
	}	
}

/**
 * Class as value container to be passed among methods
 */
class AttachmentClient {
	AsynchronousSocketChannel channel;
	ByteBuffer buffer;
}


/**
 * Class to read / write process
 */
class ReadWriteHandlerClient implements CompletionHandler<Integer, AttachmentClient> {
	
	private static final Logger log = LogManager.getLogger(ReadWriteHandlerClient.class.getName());
	
	/**
	 * An override method to be processed after operation has completed
	 * @param result Result
	 * @param attach AttachmentClient object
	 */			
	@Override
	public void completed(Integer result, AttachmentClient attach) {
		// do process here
	}
	
	/**
	 * An override method to be processed when operation fails
	 * @param e Throwable object
	 * @param attach AttachmentClient object
	 */			
	@Override
	public void failed(Throwable e, AttachmentClient attach) {
		e.printStackTrace();
	}
	
}

 
