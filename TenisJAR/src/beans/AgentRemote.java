package beans;

import javax.ejb.Remote;

import model.ACLMessage;

@Remote
public interface AgentRemote {

	public void handleMessage(ACLMessage message);
}
