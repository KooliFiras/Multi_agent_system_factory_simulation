package application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class MainContainer {

	public static void main(String[] args) {
		
		try {
			Runtime runtime = Runtime.instance();
			Properties properties = new ExtendedProperties();
			properties.setProperty(Profile.GUI, "true");
			ProfileImpl profileImpl = new ProfileImpl(properties);

			AgentContainer agentContainer = runtime.createMainContainer(profileImpl);
			agentContainer.start();

			/** start serviceApprovisionnementAgent */
			AgentController ServiceApprovisionnementAgent=null;
			ServiceApprovisionnementAgent = agentContainer.createNewAgent("ServiceApprovisionnementAgent",
					"application.ServiceApprovisionnementAgent", new Object[] { ServiceApprovisionnementAgent });
			ServiceApprovisionnementAgent.start();
			
			/** start serviceApprovisionnementAgent */
			AgentController FournisseurAgent=null;
			FournisseurAgent = agentContainer.createNewAgent("FournisseurAgent",
					"application.FournisseurAgent", new Object[] { FournisseurAgent });
			FournisseurAgent.start();
			
			
				/** start atelierAgents */
				AgentController atelierAgent1 = null;
				atelierAgent1 = agentContainer.createNewAgent("atelierAgent1", "application.AtelierAgent",
						new Object[] {atelierAgent1 ,getAtelierAgentInfo(Arrays.asList("Table", "Chaise", "Buffet"), "Chêne", 4) });
				atelierAgent1.start();

				AgentController atelierAgent2= null;
				atelierAgent2 = agentContainer.createNewAgent("atelierAgent2", "application.AtelierAgent", new Object[] {
						atelierAgent2,getAtelierAgentInfo(Arrays.asList("Lit", "Chevet", "Armoire"), "Merisier", 2) });
				atelierAgent2.start();

				AgentController atelierAgent3=null;
				atelierAgent3 = agentContainer.createNewAgent("atelierAgent3", "application.AtelierAgent", new Object[] {
						atelierAgent3,getAtelierAgentInfo(Arrays.asList("Banquette", "Fauteuil", "Etagère"), "Noyer", 3) });
				atelierAgent3.start();

				/** start clientAgents */
				AgentController clientAgent1=null;
				clientAgent1 = agentContainer.createNewAgent("clientAgent1", "application.ClientAgent",
						new Object[] { clientAgent1 });
				clientAgent1.start();

				AgentController clientAgent2=null;
				clientAgent2 = agentContainer.createNewAgent("clientAgent2", "application.ClientAgent",
						new Object[] { clientAgent2 });
				clientAgent2.start();

				AgentController clientAgent3=null;
				clientAgent3 = agentContainer.createNewAgent("clientAgent3", "application.ClientAgent",
						new Object[] { clientAgent3 });
				clientAgent3.start();

				/** start serviceCommercialAgent */
				AgentController serviceCommercialAgent=null;
				serviceCommercialAgent = agentContainer.createNewAgent("serviceCommercialAgent",
						"application.ServiceCommercialAgent", new Object[] { serviceCommercialAgent });
				serviceCommercialAgent.start();

				

		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

private static String getAtelierAgentInfo(List<String> produits, String typeBois, double nbFournisseur) {
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

