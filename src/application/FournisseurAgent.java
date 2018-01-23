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

public class FournisseurAgent extends GuiAgent {
	private Container c;
	public String agentName = new String();

	@Override
	protected void setup() {

		Object[] args = getArguments();
		if (args.length == 1) {

			addBehaviour(new CyclicBehaviour() {
				@Override
				public void action() {
					MessageTemplate template = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
							MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
					ACLMessage aclMessage = receive(template);

					if (aclMessage != null) {
						switch (aclMessage.getPerformative()) {
						case ACLMessage.REQUEST:
							ACLMessage message = new ACLMessage(ACLMessage.CONFIRM);
							Integer prixBois = ThreadLocalRandom.current().nextInt(5, 10);
							System.out.println("prix proposé pour : " + aclMessage.getContent() + " : " + prixBois);
							message.setContent(prixBois.toString());
							message.addReceiver(new AID(aclMessage.getSender().getLocalName(), AID.ISLOCALNAME));
							send(message);
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
		System.out.println("l'agent est en train de mourir .....");
	}

	@Override
	protected void onGuiEvent(GuiEvent guiEvent) {
	}

}
