package it.polito.tdp.extflightdelays.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private ExtFlightDelaysDAO dao;
	private Graph<Airport, DefaultWeightedEdge> grafo;
	private Map<Integer, Airport> aIdMap;
	private List<Airport> aeroporti;
	
	public Model() {
		this.dao= new ExtFlightDelaysDAO();
		this.aIdMap = new HashMap<>();
		this.aeroporti = new LinkedList<>();
	}
	public void creaGrafo(Integer voliMin) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		dao.getAirports(voliMin, aIdMap);//popolo la mappa
		Graphs.addAllVertices(this.grafo, aIdMap.values());
		
		for(Rotta rotta: this.dao.getRotte(aIdMap)) {
			DefaultWeightedEdge e = this.grafo.getEdge(rotta.getPartenza(), rotta.getDestinazione());
			if(e==null) { //creo l'arco perchè non è ancora presente
				Graphs.addEdge(this.grafo, rotta.getPartenza(), rotta.getDestinazione(), rotta.getDistanzaMedia());
			}else {
				double peso = this.grafo.getEdgeWeight(e);
				double pesoNuovo = (peso + rotta.getDistanzaMedia())/2;
				grafo.setEdgeWeight(e, pesoNuovo);
			}
			/*
			System.out.println("Partenza: "+ rotta.getPartenza().toString() +
							" Arrivo: "+ rotta.getDestinazione().toString() +
							" --> peso= "+ rotta.getDistanzaMedia() + "\n");
			*/
		}
		System.out.println("Vertici: "+ grafo.vertexSet().size()+ " Archi: "+grafo.edgeSet().size());
		for(DefaultWeightedEdge e : grafo.edgeSet()) {
			System.out.println(e.toString() + " - "+ grafo.getEdgeWeight(e));
		}
	}
	
	public Integer nVertici() {
		return this.grafo.vertexSet().size();
	}
	public Integer nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Airport> getAeroporti(Integer voliMin){
		this.aeroporti = this.dao.getAirports(voliMin, aIdMap);
		Collections.sort(aeroporti);
		return aeroporti;
	}
	
	public List<Vicino> getAdiacenti(Airport aeroporto){
		List<Vicino> vicini = new LinkedList<>();
		List<Airport> listA= Graphs.neighborListOf(this.grafo, aeroporto);
		
		for(Airport a: listA) {
			Double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(aeroporto, a));
			Vicino v = new Vicino(a, peso);
			vicini.add(v);
		}
		Collections.sort(vicini);
		return vicini;
	}
}
