package application;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class ServiceCommercialAgent extends GuiAgent {

	public String agentName = new String();
	
	private List<ACLMessage> FileAttente = new ArrayList<>();

	@Override
	protected void setup() {

		Object[] args = getArguments();
		if (args.length == 1) {
			
			addBehaviour(new TickerBehaviour(this, 10) {
				@Override
				protected void onTick() {
					if (!FileAttente.isEmpty()) {
						Stock stock = Stock.getInsatance();
						ACLMessage aclMessage = FileAttente.get(0);
						String aString = (aclMessage.getReplyWith());

						JSONObject commande;

						JSONObject msgToClientAgent = new JSONObject();
						List<String> insufficientProducts = new ArrayList<>();
						try {
							commande = new JSONObject(aclMessage.getContent());
							Iterator<?> keys = commande.keys();
							while (keys.hasNext()) {
								String product = (String) keys.next();
								if (!stock.isSufficientProd(product, Integer.parseInt(commande.get(product).toString()))) {
									insufficientProducts.add(product);
	
								}
							}
							if ((insufficientProducts.size() != 0)&&aclMessage.getReplyWith().equals("waiting")) {
								aclMessage.setReplyWith("notWaiting");
								System.out.println("le stock est insuffisant");
								System.out.println("envoi de demande à  l'atelier");
								ACLMessage message = new ACLMessage(ACLMessage.INFORM);
								JSONObject msgToAtelierAgent = new JSONObject();
								msgToAtelierAgent.put("produitInsuffisant", new JSONArray(insufficientProducts));
								message.setContent(msgToAtelierAgent.toString());
								message.addReceiver(new AID("atelierAgent1", AID.ISLOCALNAME));
								message.addReceiver(new AID("atelierAgent2", AID.ISLOCALNAME));
								message.addReceiver(new AID("atelierAgent3", AID.ISLOCALNAME));
								send(message);
							} else if (insufficientProducts.size() == 0) {
								System.out.println("le stock est suffisant");
								System.out.println("envoi de reponse à  " + aclMessage.getSender().getLocalName());
								Iterator<?> keys2 = commande.keys();
								while (keys2.hasNext()) {
									String key = (String) keys2.next();
									stock.getProduct(key, Integer.parseInt(commande.get(key).toString()));
								}
								System.out.println(stock.stockDeProduitsActuel);

								ACLMessage message = new ACLMessage(ACLMessage.CONFIRM);
								message.setContent("accepté");
								message.addReceiver(new AID(aclMessage.getSender().getLocalName(), AID.ISLOCALNAME));
								send(message);
								FileAttente.remove(0);

							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			});
			
			
			addBehaviour(new CyclicBehaviour() {

				private AID requester;
				private String produit_quantite;

				@Override
				public void action() {

					MessageTemplate template = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
							MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

					ACLMessage aclMessage = receive(template);

					if (aclMessage != null) {
						switch (aclMessage.getPerformative()) {
						case ACLMessage.REQUEST:

							System.out.println("Demande d'achat :" + aclMessage.getSender().getLocalName() + " : "
									+ aclMessage.getContent());
							aclMessage.setReplyWith("waiting");
							FileAttente.add(aclMessage);
							break;
						case ACLMessage.CONFIRM:
							break;
						}
					} else {
						block();
					}

				}

			});
		
		}

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
		System.out.println("L'agent est en train de mourir .....");
	}

	@Override
	protected void onGuiEvent(GuiEvent guiEvent) {
	}

}
