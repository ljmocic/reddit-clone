package beans;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Message implements Serializable{

	private String senderId;
	private String receiverId;
	private String content;
	private boolean seen;
	
	public Message(String senderId, String receiverId, String content) {
		super();
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.content = content;
		this.seen = false;
	}
	
	public String getSenderId() {
		return senderId;
	}
	
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	
	public String getReceiverId() {
		return receiverId;
	}
	
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String text) {
		this.content = text;
	}
	
	public boolean isSeen() {
		return seen;
	}
	
	public void setSeen(boolean seen) {
		this.seen = seen;
	}

	

}