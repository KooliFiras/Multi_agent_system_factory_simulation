package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Stock {

	public HashMap<String, Integer> stockDeProduitsInitial = new HashMap<String, Integer>();
	public HashMap<String, Integer> stockDeProduitsActuel = new HashMap<String, Integer>();
	public HashMap<String, Integer> numPlancheParProduit = new HashMap<String, Integer>();

	public HashMap<String, Integer> stockDeBoisActuel = new HashMap<String, Integer>();

	private static final Stock STOCK;

	static {
		STOCK = new Stock();
	}

	public static Stock getInsatance() {
		return STOCK;
	}

	private Stock() {
		// Stock intial
		stockDeProduitsInitial.put("Table", 20);
		stockDeProduitsInitial.put("Chaise", 120);
		stockDeProduitsInitial.put("Buffet", 20);

		stockDeProduitsInitial.put("Lit", 10);
		stockDeProduitsInitial.put("Chevet", 20);
		stockDeProduitsInitial.put("Armoire", 10);

		stockDeProduitsInitial.put("Banquette", 30);
		stockDeProduitsInitial.put("Fauteuil", 60);
		stockDeProduitsInitial.put("Etagère", 30);

		// quantité actuel
		stockDeProduitsActuel.put("Table", 20);
		stockDeProduitsActuel.put("Chaise", 120);
		stockDeProduitsActuel.put("Buffet", 20);

		stockDeProduitsActuel.put("Lit", 10);
		stockDeProduitsActuel.put("Chevet", 20);
		stockDeProduitsActuel.put("Armoire", 10);

		stockDeProduitsActuel.put("Banquette", 30);
		stockDeProduitsActuel.put("Fauteuil", 60);
		stockDeProduitsActuel.put("Etagère", 30);

		// nombre de planche par produits
		numPlancheParProduit.put("Table", 5);
		numPlancheParProduit.put("Chaise", 2);
		numPlancheParProduit.put("Buffet", 6);

		numPlancheParProduit.put("Lit", 7);
		numPlancheParProduit.put("Chevet", 1);
		numPlancheParProduit.put("Armoire", 8);

		numPlancheParProduit.put("Banquette", 6);
		numPlancheParProduit.put("Fauteuil", 3);
		numPlancheParProduit.put("Etagère", 8);

		// stock en nombre de planche pour chaque type de bois
		stockDeBoisActuel.put("Chêne", 1000);
		stockDeBoisActuel.put("Merisier", 1000);
		stockDeBoisActuel.put("Noyer", 1000);
	}

	public synchronized void getProduct(String prodcut, Integer quantity) {
		Integer stockQuantiy = stockDeProduitsActuel.get(prodcut);
		stockDeProduitsActuel.put(prodcut, (stockQuantiy - quantity));
	}

	public synchronized boolean isSufficientProd(String prodcut, Integer quantity) {
		int stockQuantiy = stockDeProduitsActuel.get(prodcut);
		if (stockQuantiy < quantity) {
			return false;
		} else {
			return true;
		}
	}

	public synchronized boolean isSufficientBois(String typeBois, Integer quantity) {
		Integer stockQuantiy = stockDeBoisActuel.get(typeBois);
		if (stockQuantiy < quantity) {
			return false;
		}
		return true;

	}

	public synchronized void getBois(String typeBois, Integer quantity) {
		Integer stockQuantiy = stockDeBoisActuel.get(typeBois);
		System.out.println(typeBois + " a été mise à jour  "+ (stockQuantiy - quantity));
		stockDeBoisActuel.put(typeBois, (stockQuantiy - quantity));
	}

	public synchronized void defaultProductValue(String product) {
		Integer initialValue = stockDeProduitsInitial.get(product);
		stockDeProduitsActuel.put(product, (initialValue));
	}

}
