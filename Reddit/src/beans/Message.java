package beans;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Message implements Serializable{

	private String senderId;
	private String receiverId;
	private String content;
	private boolean seen;
	
	private boolean hasReport;
	private Report report;
	
	public Message() {
		
	}
	
	public Message(String senderId, String receiverId, String content) {
		super();
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.content = content;
		this.seen = false;
		this.hasReport = false;
		this.report = null;
	}
	
	public Message(String senderId, String receiverId, String content, boolean hasReport, Report report) {
		super();
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.content = content;
		this.seen = false;
		this.hasReport = hasReport;
		this.report = report;
	}
	
	
	public Message(Message message) {
		super();
		this.senderId = message.getSenderId();
		this.receiverId = message.getSenderId();
		this.content = message.getContent();
		this.seen = message.isSeen();
		this.hasReport = message.isHasReport();
		this.report = message.getReport();
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

	public boolean isHasReport() {
		return hasReport;
	}

	public void setHasReport(boolean hasReport) {
		this.hasReport = hasReport;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

}