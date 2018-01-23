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

public class ServiceApprovisionnementAgent extends GuiAgent {
	
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
						// Stock stock = Stock.getInsatance();
						ACLMessage aclMessage = FileAttente.get(0);
						String typeBois = aclMessage.getContent();
						if (aclMessage.getReplyWith().equals("waiting")) {
							aclMessage.setReplyWith("notWaiting");
							System.out.println("envoi de demande aux  fourniseurs");
							ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
							message.setContent(typeBois);
							message.addReceiver(new AID("FournisseurAgent" + typeBois, AID.ISLOCALNAME));
							send(message);
						}
					}
				}
			});

			addBehaviour(new CyclicBehaviour() {

				@Override
				public void action() {

					MessageTemplate template = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
							MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

					ACLMessage aclMessage = receive(template);

					if (aclMessage != null) {
						switch (aclMessage.getPerformative()) {
						case ACLMessage.REQUEST:
							
							System.out.println("demande de bois :" + aclMessage.getSender().getLocalName() + " : "
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
