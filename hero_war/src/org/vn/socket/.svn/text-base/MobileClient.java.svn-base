package org.vn.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Vector;

import org.vn.constant.CommandClientToServer;
import org.vn.gl.DebugLog;

import android.util.Log;

/**
 * 
 * Update by @author Tran Vu Tat Binh (tvtbinh.bino@gmail.com) Custom for better
 * performance on Android platform
 */
public class MobileClient implements IMobileClient {

	/**
	 * co de biet hien tai co su dung encrypt hay khong.
	 */
	public static boolean ENCRYPT_STATUS = true;
	private static final byte HEADER_SIZE = 2;
	private DataOutputStream dos;
	private DataInputStream dis;
	public IMessageListener mMessageHandler;
	private Socket sc;
	/** Cho biet hien tai da duoc ket noi hay chua */
	public boolean isConnected, connecting;
	private Sender sender;
	private Reader reader;
	private NetworkInit mInitNetwork;
	public long sendByteCount;
	public long recvByteCount;
	// private TEA tea;
	// private boolean isEncrypt = true;
	private long lastTimeMessageSent = -1;
	private int lastCommandId = -1;

	protected static MobileClient instance;

	private MobileClient() {
		// Do nothing
	}

	public static MobileClient getInstance() {
		if (instance == null) {
			instance = new MobileClient();
		}
		return instance;
	}

	public boolean isConnected() {
		return (isConnected && (sc != null ? sc.isConnected() : false));
	}

	public void setHandler(IMessageListener messageHandler) {
		mMessageHandler = messageHandler;
	}

	public static final int CONNECT_STATUS_CONNECTED = 1;
	public static final int CONNECT_STATUS_CONNECTING = 2;
	public static final int CONNECT_STATUS_DISCONNECTED = 0;

	/**
	 * Try to connect to the host with the port provided Return the connected
	 * status
	 */
	public int connect(String host, int port) {
		if (!isConnected && !connecting) {
			cleanNetwork();
			mInitNetwork = new NetworkInit(host, port);
			if (mInitNetwork.tryConnecting()) {
				return CONNECT_STATUS_CONNECTED;
			} else {
				return CONNECT_STATUS_DISCONNECTED;
			}
		} else {
			if (isConnected) {
				return CONNECT_STATUS_CONNECTED;
			} else {
				return CONNECT_STATUS_CONNECTING;
			}
		}
	}

	class NetworkInit {

		private final String host;
		private final int port;

		NetworkInit(String host, int port) {
			this.host = host;
			this.port = port;
		}

		/**
		 * Try connecting to server
		 * 
		 * @return true if connect succesfully, false otherewise
		 */
		public boolean tryConnecting() {
			connecting = true;
			try {
				doConnect(host, port);
			} catch (Exception ex) {
				if (mMessageHandler != null) {
					close();
					mMessageHandler.onConnectionFail();
				}
				return false;
			}
			return true;
		}

		public void doConnect(String IP, int P) throws IOException {
			sc = new Socket(IP, port);
			// sc = new Socket("192.168.1.45", 12345);
			// sc = new Socket("113.172.89.32", 12345);

			dos = new DataOutputStream(sc.getOutputStream());
			dis = new DataInputStream(sc.getInputStream());
			dos.flush();
			isConnected = true;
			sender = new Sender();
			reader = new Reader();

			Thread tempReader = new Thread(reader);
			tempReader.setPriority(Thread.MAX_PRIORITY);
			tempReader.start();
			new Thread(sender).start();
			connecting = false;
		}
	}

	public void sendMessage(Message message) {
		sender.addMessage(message);
	}

	private synchronized void doSendMessage(Message message) {
		byte[] data = message.getData();
		try {
			if (data == null) {
				data = new byte[] { (byte) message.command };
			}
			dos.writeInt(data.length);// 0x09
			dos.write(data);
			sendByteCount += data.length;
			sendByteCount += HEADER_SIZE;
			dos.flush();
			lastCommandId = message.command;
			lastTimeMessageSent = System.currentTimeMillis();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class Sender implements Runnable {
		private final Vector<Message> sendingMessage = new Vector<Message>();

		public void addMessage(Message message) {
			if (lastCommandId == message.command
					&& System.currentTimeMillis() - lastTimeMessageSent < 1000) {
				System.out.println(message.command + " bị từ chối.");
				return;
			}
			sendingMessage.addElement(message);
			synchronized (sendingMessage) {
				sendingMessage.notifyAll();
			}
		}

		public void run() {
			while (isConnected) {
				while (sendingMessage.size() > 0) {
					Message m = (Message) sendingMessage.elementAt(0);
					sendingMessage.removeElementAt(0);
					doSendMessage(m);
					DebugLog.d("MobileClient", "Request:" + m.command + ">>>>>");
					m = null;
				}
				synchronized (sendingMessage) {
					try {
						sendingMessage.wait();
					} catch (InterruptedException ex) {
					}
				}
			}
		}

		public void stop() {
			synchronized (sendingMessage) {
				sendingMessage.removeAllElements();
				sendingMessage.notifyAll();
			}
		}
	}

	private class Reader implements Runnable {

		public void run() {
			Message message;
			while (isConnected) {
				try {
					message = readMessage();
					if (message != null) {
						try {
							DebugLog.d("MobileClient",
									"<<<<<<<<<<<<<<<<<<<Response:"
											+ message.command);
							mMessageHandler.onMessage(message);
							message = null;
						} catch (Exception e) {
							System.out.printf("MobileClient", e.getMessage()
									+ "");
						}
					} else {
						break;
					}
				} catch (SocketTimeoutException e) {
					System.out.printf("MobileClient", "SocketTimeoutException");
					lostConnection();
					e.printStackTrace();
				} catch (EOFException e) {
					System.out.printf("MobileClient", "EOFException");
					lostConnection();
					e.printStackTrace();
				} catch (Exception e) {
					System.out.printf("MobileClient", "Exception");
					lostConnection();
					e.printStackTrace();
				}
			}
			lostConnection();
		}

		private void lostConnection() {
			if (isConnected) {
				if (mMessageHandler != null) {
					Log.e("MobileClient", "LOST_CONNECTION");
					Message message = new Message(
							CommandClientToServer.LOST_CONNECTION);
					mMessageHandler.onMessage(message);
				}
				close();
			}
		}

		private Message readMessage() throws Exception {
			int size = 0;
			size = dis.readInt();
			byte data[] = new byte[size];
			int byteRead = dis.read(data, 0, size);
			Message msg = null;
			if (size > 0) {
				msg = new Message(data);
			}
			recvByteCount += (HEADER_SIZE + byteRead);
			return msg;
		}
	}

	public void close() {
		cleanNetwork();
		if (sender != null) {
			sender.stop();
		}
		sendByteCount = 0;
		recvByteCount = 0;
	}

	private void cleanNetwork() {
		try {
			isConnected = false;
			connecting = false;
			// isEncrypt = false;
			if (sc != null) {

				// Start by ChinhQT, 05/17/2011
				sc.shutdownInput();
				sc.shutdownOutput();
				// End
				sc.close();
				sc = null;
			}
			if (dos != null) {
				dos.close();
				dos = null;
			}
			if (dis != null) {
				dis.close();
				dis = null;
			}
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
