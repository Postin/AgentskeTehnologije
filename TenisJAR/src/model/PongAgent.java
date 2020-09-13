package model;

import java.util.ArrayList;
import java.util.List;

import dao.PingDAO;
import dto.PerformativeDTO;

public class PongAgent extends Agent implements AgentInterface {

	@Override
	public void handleMessage(ACLMessage message) {
		// TODO Auto-generated method stub
		if(message.getPerformative().getPerformative().equals(Performative.REQUEST)) {

				System.out.println("Pong got a request from Ping");
				ACLMessage msg = new ACLMessage();
				msg.setContent("Reply from Pong");
				PerformativeDTO p = new PerformativeDTO();
				p.setPerformative(Performative.INFORM);
				msg.setPerformative(p);
				List<AID> receivers = new ArrayList<AID>();
				receivers.add(message.getSender());
				AID[] a = new AID[receivers.size()];
				for(int i = 0; i < receivers.size(); i++) {
					a[i] = receivers.get(i);
				}
				msg.setReceivers(a);
				for(int i = 0; i < PingDAO.getInstance().getStartedPingAgents().size();i++) {
					if(message.getSender().getName().equals((PingDAO.getInstance().getStartedPingAgents().get(i).getId().getName()))){
						PingDAO.getInstance().getStartedPingAgents().get(i).handleMessage(msg);
					}
				}
			
			
			
			
			
		}
	}

}
