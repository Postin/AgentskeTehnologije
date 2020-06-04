package dao;

import java.util.ArrayList;
import java.util.List;

import model.ACLMessage;

public class MessageDAO {
	private List<ACLMessage> allMessages = new ArrayList<ACLMessage>();
	
	private static MessageDAO instance;
	
	public MessageDAO() {
		
	}

	public List<ACLMessage> getAllMessages() {
		return allMessages;
	}

	public void setAllMessages(List<ACLMessage> allMessages) {
		this.allMessages = allMessages;
	}
	
	public static MessageDAO getInstance() {
		if(instance == null) {
			instance = new MessageDAO();
		}
		
		return instance;
	}
}
