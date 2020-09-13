package model;

public class PingAgent extends Agent implements AgentInterface {

	@Override
	public void handleMessage(ACLMessage message) {
		// TODO Auto-generated method stub
		
		if(message.getPerformative().getPerformative().equals(Performative.INFORM)) {
			System.out.println("Answer from Pong");
		}
		
	}

}
