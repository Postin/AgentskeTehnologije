package model;


import javax.ejb.Stateful;

import beans.AgentLocal;
import beans.AgentRemote;

@Stateful
public class Agent implements AgentRemote, AgentLocal {

	private AID id;

	public AID getId() {
		return id;
	}

	public void setId(AID id) {
		this.id = id;
	}

	@Override
	public void handleMessage(ACLMessage message) {
		// TODO Auto-generated method stub
		
	}
	
}
