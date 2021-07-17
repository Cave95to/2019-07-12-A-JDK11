package it.polito.tdp.food.model;

public class Result implements Comparable<Result> {
	
	private Food f;
	private Double peso;
	
	public Result(Food f, Double peso) {
		super();
		this.f = f;
		this.peso = peso;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((f == null) ? 0 : f.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Result other = (Result) obj;
		if (f == null) {
			if (other.f != null)
				return false;
		} else if (!f.equals(other.f))
			return false;
		return true;
	}
	public Food getF() {
		return f;
	}
	public void setF(Food f) {
		this.f = f;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return f.getDisplay_name() + " " + peso + "\n";
	}
	@Override
	public int compareTo(Result o) {
		// TODO Auto-generated method stub
		return o.peso.compareTo(this.peso);
	}
	
	

}
