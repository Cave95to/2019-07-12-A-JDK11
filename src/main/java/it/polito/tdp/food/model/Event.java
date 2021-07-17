package it.polito.tdp.food.model;

public class Event implements Comparable<Event>{
	
	public enum EventType {
		
		INIZIO_PREPARAZIONE,   // viene assegnato un cibo ad una stazione
		FINE_PREPARAZIONE,     // la stazione ha completato il cibo
	}
	
	private Double time;  // tempo in minuti
	private EventType type;
	private Stazione stazione;
	private Food food;
	
	public Event(Double time, EventType type, Stazione stazione, Food food) {
		super();
		this.time = time;
		this.stazione = stazione;
		this.food = food;
		this.type = type;
	}

	public Double getTime() {
		return time;
	}
	
	public Stazione getStazione() {
		return stazione;
	}

	public Food getFood() {
		return food;
	}

	@Override
	public int compareTo(Event o) {
		return this.time.compareTo(o.time);
	}

	public EventType getType() {
		return type;
	}

	
}

/*   supponiamo di avere 3 stazioni
  
  	0[F1], 1[F2], 2[F3]
  	
  	FINE_PREPARAZ ( F2, stazione 1)
  	
  	INIZIO_PREPARAZ ( F2 cibo concluso, stazione 1) -> ci serve tenere traccia di quale cibo è stato fatto
  														per poter calclare il tempo che deve passare, NON
  														del cibo nuovo, che calcolo dopo
 	scelgo il prossimo cibo
 	calcolo la durata
 	programmo un nuovo evento FINE(cibo appena scelto)
 	
 	FINE_PREPARAZ ( F4, stazione 1)   che scatta a T + DELTA (peso di f2--f4)
 	
 	posso programmare un nuovo evento INIZIO allo stesso istante, SALVANDO IL CIBO CONCLUSO
 	 
 */
     