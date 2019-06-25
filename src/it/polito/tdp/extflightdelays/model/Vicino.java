package it.polito.tdp.extflightdelays.model;

public class Vicino implements Comparable<Vicino>{
	
	private Airport aereoporto;
	private Double durataMedia;
	public Vicino(Airport aereoporto, Double durataMedia) {
		super();
		this.aereoporto = aereoporto;
		this.durataMedia = durataMedia;
	}
	public Airport getAereoporto() {
		return aereoporto;
	}
	public Double getDistanzaMedia() {
		return durataMedia;
	}
	
	
	
	@Override
	public String toString() {
		return String.format("Aereoporto=%s, DurataMedia=%s", aereoporto, durataMedia);
	}
	@Override
	public int compareTo(Vicino o) {
		return this.durataMedia.compareTo(o.durataMedia);
	}
	

}
