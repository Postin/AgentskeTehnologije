package rest;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import dao.AgentCenterDAO;
import model.AgentCenter;
import responseModel.ResponseClass;

@Stateless
@LocalBean
@Remote(NodeCommunication.class)
@Path("")
public class NodeCommunicationBean implements NodeCommunication {

	@POST
	@Path("/register")
	@Override
	public ResponseClass contactMaster(AgentCenter ac) {
		System.out.println("=========== REGISTRATION OF NEW AGENT CENTER ===========");
		if (!AgentCenterDAO.getInstance().getAgentCenters().contains(ac))
			AgentCenterDAO.getInstance().getAgentCenters().add(ac);
		
		ResponseClass rc = new ResponseClass();
		rc.setText("Agent center " + ac.getAlias() + " (" + ac.getAddress() +") succefully registred");
		return rc;
	}

	
}
