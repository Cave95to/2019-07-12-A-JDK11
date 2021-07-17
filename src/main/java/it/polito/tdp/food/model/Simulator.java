package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.food.model.Event.EventType;
import it.polito.tdp.food.model.Food.StatoPreparazione;

public class Simulator {
	
	// modello del mondo
	private List<Stazione> stazioni;
	private List<Food> cibi;
	
	private Graph<Food, DefaultWeightedEdge> grafo;
	private Model model;	// MI SERVE PER POTERNE CHIAMARE I METODI, in particolare quello che restituisce
							// gli adiacenti ordinati
	
	// parametri di simulazione
	private int numStazioni = 5;
	
	// risultati calcolati
	private double tempoTotPreparazione;
	private int numCibiPreparati;
	
	// coda degli eventi
	private PriorityQueue<Event> queue;
	
	
	public void init(Food partenza) {
		
		this.cibi = new ArrayList<>(this.grafo.vertexSet());
		for(Food cibo : cibi) 
			cibo.setPreparazione(StatoPreparazione.DA_PREPARARE);
		
		this.stazioni = new ArrayList<>();
		
		for(int i=0; i < this.numStazioni; i++) {
			this.stazioni.add(new Stazione(true, null));
		}
		
		this.tempoTotPreparazione = 0.0;
		this.numCibiPreparati = 0;
		
		this.queue = new PriorityQueue<>();
		
		List<Result> vicini = this.model.calcolaAdiacentiMax(partenza);
		
		for(int i=0; i<this.numStazioni && i<vicini.size(); i++) {
			
			this.stazioni.get(i).setLibera(false);
			this.stazioni.get(i).setFood(vicini.get(i).getF());
			vicini.get(i).getF().setPreparazione(StatoPreparazione.IN_CORSO);
			
			Event e = new Event(vicini.get(i).getPeso(), 
					EventType.FINE_PREPARAZIONE,
					this.stazioni.get(i),
					vicini.get(i).getF()
					);
			
			this.queue.add(e);
					
		}
	}
	
	public void run() {
		
		while(!queue.isEmpty()) {
			Event e = queue.poll();
			processEvent(e);
		}

	}
	
	private void processEvent(Event e) {
		
		switch(e.getType()) {
		
		case INIZIO_PREPARAZIONE:
			
			// il mio e.getFood contiene il cibo che ho finito -> cerco il max da preparare ora
			
			List<Result> vicini = this.model.calcolaAdiacentiMax(e.getFood());
			
			for(Result vicino : vicini) {
				
				if(vicino.getF().getPreparazione()== StatoPreparazione.DA_PREPARARE) {
					
					vicino.getF().setPreparazione(StatoPreparazione.IN_CORSO);
					e.getStazione().setLibera(false);
					e.getStazione().setFood(vicino.getF());
					
					Event e2 = new Event (e.getTime()+ vicino.getPeso(),
							EventType.FINE_PREPARAZIONE,
							e.getStazione(),
							vicino.getF());  // cambio il cibo
					
					this.queue.add(e2);
					break;
				}
			}
		
			
			break;
		
		case FINE_PREPARAZIONE:
			
			this.numCibiPreparati++;
			this.tempoTotPreparazione = e.getTime();
			
			e.getStazione().setLibera(true);
			
			//e.getFood().setPreparato(true);   NON VA BENE PERCHE CI SERVE ANCHE SAPERE SE PER CASO SIA
			//									IN PREPARAZIONE -> ENUM DENTRO FOOD
			e.getFood().setPreparazione(StatoPreparazione.PREPARATO );
			
			Event e2 = new Event(e.getTime(), 
					EventType.INIZIO_PREPARAZIONE, 
					e.getStazione(), 
					e.getFood()); // mantengo il cibo vecchio perche mi serve per calcolare il nuovo
			
			this.queue.add(e2);
			break;
		
		}
		
	}

	public Simulator(Graph<Food, DefaultWeightedEdge> grafo, Model model) {
		this.grafo = grafo;
		this.model = model;
	}


	public int getNumStazioni() {
		return numStazioni;
	}


	public void setNumStazioni(int numStazioni) {
		this.numStazioni = numStazioni;
	}


	public double getTempoTotPreparazione() {
		return tempoTotPreparazione;
	}

	public int getNumCibiPreparati() {
		return numCibiPreparati;
	}

}
