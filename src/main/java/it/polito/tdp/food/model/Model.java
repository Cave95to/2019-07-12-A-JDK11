package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private FoodDao dao;
	private Map<Integer, Food> idMap;
	private Graph<Food, DefaultWeightedEdge> grafo;
	private List<Food> vertici;
	
	
	public Model() {
		this.dao = new FoodDao();
		this.idMap = new HashMap<>();
		this.dao.listAllFoods(idMap);
	}

	public void creaGrafo(int numPorzioni) {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.vertici = new ArrayList<>();
		
		this.vertici = this.dao.getFoodByPortions(numPorzioni, this.idMap);
		
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		List<Adiacenza> adiacenze = new ArrayList<>();
		
		adiacenze = this.dao.getAdiacenze(idMap);
		
		for(Adiacenza a : adiacenze) {
			
			Food f1 = a.getF1();
			Food f2 = a.getF2();
			
			if(this.grafo.containsVertex(f1) && this.grafo.containsVertex(f2))
				Graphs.addEdge(this.grafo, f1, f2, a.getPeso());
		}
	}

	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}

	public List<Food> getVertici() {
		Collections.sort(this.vertici);
		return this.vertici;
	}
	
	public List<Result> calcolaAdiacentiMax(Food f) {
		
		List<Food> adiacenti = Graphs.neighborListOf(this.grafo, f);
		
		List<Result> risultato = new ArrayList<>();
		
		for(Food fo : adiacenti) {
			Double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(fo, f));
			risultato.add(new Result(fo, peso));	
		}
		
		Collections.sort(risultato);
		return risultato;
	}

	public String simula(Food f, int numStazioni) {
		
		Simulator sim = new Simulator(this.grafo, this);
		sim.setNumStazioni(numStazioni);
		sim.init(f);
		sim.run();
		
		String messaggio = String.format("Preparati %d cibi in %f minuti\n", 
				sim.getNumCibiPreparati() ,sim.getTempoTotPreparazione());
		
		return messaggio;
		
	}
}
