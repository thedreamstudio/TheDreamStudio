package org.vn.socket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Message {

	public byte command;
	private ByteArrayOutputStream baos = null;
	private DataOutputStream dos = null;
	private ByteArrayInputStream bais = null;
	private DataInputStream dis = null;
	/** whether encrypt data or not */

	private Message() {
	}

	/**
	 * Khởi tạo một message mới để gửi lên server. Dữ liệu kèm theo = null.
	 * 
	 * @param command
	 *            : id của command.
	 */
	public Message(Byte command) {
		this.command = command;
		try {
			writer().writeByte(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	public Message(int command, byte[] data) {
//		this.command = command;
//		bais = new ByteArrayInputStream(data);
//		dis = new DataInputStream(bais);
//	}

	/**
	 * Khởi tạo một Message với data sẵn có bao gồm command trong đó.
	 * 
	 * @param data
	 *            : data[0] là command id còn lại là dữ liệu của command đó.
	 */
	Message(byte[] data) {
		dis = new DataInputStream(new ByteArrayInputStream(data));
		try {
			this.command= dis.readByte();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] getData() {
		if (baos != null)
			return baos.toByteArray();
		return null;
	}

	public DataInputStream reader() {
		return dis;
	}

	public DataOutputStream writer() {
		if (baos == null) {
			baos = new ByteArrayOutputStream();
			dos = new DataOutputStream(baos);
		}
		return dos;
	}

	public void cleanup() {
		try {
			if (dis != null)
				dis.close();
			if (dos != null)
				dos.close();
		} catch (IOException e) {
		}
	}

}
