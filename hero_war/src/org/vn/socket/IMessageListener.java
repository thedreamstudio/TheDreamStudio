package org.vn.socket;

public interface IMessageListener {

	public void onMessage(Message message);

	public abstract void onConnectionFail();

	public abstract void onDisconnected();

	public abstract void onConnectOK();
}
