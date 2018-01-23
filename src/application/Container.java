package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;


public class Container  {


	public static void main(String[] args) {
		
	}

	public void startcontainer() {
		try {
			Runtime runtime = Runtime.instance();
			ProfileImpl profileImpl = new ProfileImpl(false);
			profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
			AgentContainer agentContainer = runtime.createAgentContainer(profileImpl);

			/** start atelierAgents */
			AgentController atelierAgent1;
			atelierAgent1 = agentContainer.createNewAgent("atelierAgent1", "application.AtelierAgent",
					new Object[] { this, getAtelierAgentInfo(Arrays.asList("Table", "Chaise", "Buffet"), "Chêne", 4) });
			atelierAgent1.start();

			AgentController atelierAgent2;
			atelierAgent2 = agentContainer.createNewAgent("atelierAgent2", "application.AtelierAgent", new Object[] {
					this, getAtelierAgentInfo(Arrays.asList("Lit", "Chevet", "Armoire"), "Merisier", 2) });
			atelierAgent2.start();

			AgentController atelierAgent3;
			atelierAgent3 = agentContainer.createNewAgent("atelierAgent3", "application.AtelierAgent", new Object[] {
					this, getAtelierAgentInfo(Arrays.asList("Banquette", "Fauteuil", "Etagère"), "Noyer", 3) });
			atelierAgent3.start();

			/** start clientAgents */
			AgentController clientAgent1;
			clientAgent1 = agentContainer.createNewAgent("clientAgent1", "application.ClientAgent",
					new Object[] { this });
			clientAgent1.start();

			AgentController clientAgent2;
			clientAgent2 = agentContainer.createNewAgent("clientAgent2", "application.ClientAgent",
					new Object[] { this });
			clientAgent2.start();

			AgentController clientAgent3;
			clientAgent3 = agentContainer.createNewAgent("clientAgent3", "application.ClientAgent",
					new Object[] { this });
			clientAgent3.start();

			/** start serviceCommercialAgent */
			AgentController serviceCommercialAgent;
			serviceCommercialAgent = agentContainer.createNewAgent("serviceCommercialAgent",
					"application.ServiceCommercialAgent", new Object[] { this });
			serviceCommercialAgent.start();

		} catch (StaleProxyException e1) {
			// TODO Auto-generated catch block
			System.out.println("excep.....");
			e1.printStackTrace();
		}
	}

	private String getAtelierAgentInfo(List<String> produits, String typeBois, double nbFournisseur) {
		JSONObject atelierAgent1Info = new JSONObject();
		JSONArray jArray = new JSONArray(produits);
		try {
			
			atelierAgent1Info.put("produits", jArray);
			atelierAgent1Info.put("typeBois", typeBois);
			atelierAgent1Info.put("nbFournisseur", nbFournisseur);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return atelierAgent1Info.toString();
	}
	
}
