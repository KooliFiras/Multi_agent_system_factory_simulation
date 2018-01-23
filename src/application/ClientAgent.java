package application;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.ACLMessage;
import java.util.concurrent.ThreadLocalRandom;

public class ClientAgent extends GuiAgent {
	private Container c;
	public String agentName = new String();
	boolean purchaseSent = false;

	@Override
	protected void setup() {
		Object[] args = getArguments();
		 agentName = this.getAID().getLocalName();
		if (args.length == 1) {
			c = (Container) getArguments()[0];
			addBehaviour(new CyclicBehaviour() {

				private AID commercialAgent;
				private String reponse;

				@Override
				public void action() {

					ACLMessage aclMessage = receive();

					if (aclMessage != null) {
						System.out.println("ma demande est : " + aclMessage.getContent()+" : "+ agentName);
						purchaseSent = false;
					} else {
						block();
					}
				}
			});

			addBehaviour(new TickerBehaviour(this, 6000) {
				@Override
				protected void onTick() {
					if (!purchaseSent) {
						int randomNum = ThreadLocalRandom.current().nextInt(0, 10 + 1);

						if (randomNum % 2 == 0) {
							ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
							message.setContent(getAchatContent());
							message.addReceiver(new AID("serviceCommercialAgent", AID.ISLOCALNAME));
							send(message);
							purchaseSent = true;
							System.out.println("\n");
							System.out.println("demande envoyée : " + agentName);
						}
					}

				}
			});
		}

	}

	public String getAchatContent() {

		JSONObject commande = new JSONObject();
		Stock stock = Stock.getInsatance();
		List<String> listP = new ArrayList<>(stock.stockDeProduitsActuel.keySet());
		int quantieMax = 10;
		double produitNumber = ThreadLocalRandom.current().nextInt(1, 5);
		for (int i = 0; i < produitNumber; i++) {
			double quantite = ThreadLocalRandom.current().nextInt(1, quantieMax);
			String produit = listP.get(ThreadLocalRandom.current().nextInt(0, listP.size()));
			try {
				commande.put(produit, quantite);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Iterator<String> j = listP.iterator();
			while (j.hasNext()) {
				if (j.next().equals(produit))
					j.remove();
			}

		}

		return commande.toString();
	}

	@Override
	protected void beforeMove() {
		System.out.println("Avant de migrer vers une nouvelle location .....");
	}

	@Override
	protected void afterMove() {
		System.out.println("Je viens d'arriver à une nouvelle location .....");
	}

	@Override
	protected void takeDown() {
		System.out.println("L'agent est en train de mourir...");
	}

	@Override
	protected void onGuiEvent(GuiEvent guiEvent) {
	}

}
