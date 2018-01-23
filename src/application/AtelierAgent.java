package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
import jade.lang.acl.ACLMessage;

public class AtelierAgent extends GuiAgent {
	
	private Container c;

	public String agentName = new String();
	
	List<String> produits = new ArrayList<>();
	
	String typeBois = new String();
	
	Integer nbFournisseur = new Integer(0);

	@Override
	protected void setup() {

		Object[] args = getArguments();
		agentName = this.getAID().getLocalName();
		
		if (args.length == 2) {

			c = (Container) getArguments()[0];
			JSONObject atelierAgentInfo;

			try {
				atelierAgentInfo = new JSONObject((String) getArguments()[1]);
				typeBois = atelierAgentInfo.get("typeBois").toString();
				nbFournisseur = Integer.parseInt(atelierAgentInfo.get("nbFournisseur").toString());

				JSONArray jsonArrayProduits = (JSONArray) atelierAgentInfo.get("produits");
				if (jsonArrayProduits != null) {
					int len = jsonArrayProduits.length();
					for (int i = 0; i < len; i++) {
						produits.add(jsonArrayProduits.get(i).toString());
					}
				}
				

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			System.out.println("passage des arguments invalide");
			doDelete();
		}

		addBehaviour(new TickerBehaviour(this, 5000) {
			@Override
			protected void onTick() {

			}
		});

		addBehaviour(new CyclicBehaviour() {
			@Override
			public void action() {
				ACLMessage aclMessage = receive();
				if (aclMessage != null) {
					try {
						JSONObject demande = new JSONObject(aclMessage.getContent());
						JSONArray jsonArrayProduits = (JSONArray) demande.get("produitInsuffisant");
						if (jsonArrayProduits != null) {
							int len = jsonArrayProduits.length();
							for (int i = 0; i < len; i++) {
								String product = jsonArrayProduits.get(i).toString();
								if (produits.contains(product)) {
									System.out.println(product + " : " + agentName);
									make(product);
								}
							}
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					block();
				}
			}
		});

	}

	public void make(String product) {
		Stock stock = Stock.getInsatance();
		Integer initialValue = stock.stockDeProduitsInitial.get(product);
		Integer numPlanche = stock.numPlancheParProduit.get(product) * initialValue;
		boolean isSuffient = stock.isSufficientBois(this.typeBois, numPlanche);
		System.out.println(numPlanche);
		if (isSuffient) {
			stock.getBois(this.typeBois, numPlanche);
			stock.defaultProductValue(product);
			System.out.println("le stock du produit " + product + " est réinitialisé par " + agentName);
			System.out.println(stock.stockDeProduitsActuel);
			System.out.println(stock.stockDeBoisActuel);
		} else {
			System.out.println("le stock de bois est  insuffisant pour : "+ this.typeBois);
			
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
