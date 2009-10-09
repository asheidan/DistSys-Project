package gcom.interfaces;

import java.io.IOException;
import java.io.Serializable;

public interface MessageSender {
	public void sendMessage(String groupName, Serializable message) throws IOException;
}
