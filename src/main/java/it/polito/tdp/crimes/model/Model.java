package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	List <String> migliore;
	double pesoMin;
	EventsDao dao;
	SimpleWeightedGraph <String, DefaultWeightedEdge> grafo;
	
	public Model() {
		dao = new EventsDao();
	}
	
	public List <String> getCategorie(){
		return dao.getCategorie();
	}
	
	public void creaGrafo(int anno, String categoria) {
		grafo= new SimpleWeightedGraph <String, DefaultWeightedEdge> (DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, dao.getVertici(anno, categoria));
		for(Adiacenza a: dao.getAdiacenza(anno, categoria)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getTipo1(), a.getTipo2(),a .getPeso());
		}
	
	}
	
	public List <Integer> getAnni(){
		return dao.getAnni();
	}
	public int getNvertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List <Adiacenza> getMassimi(int anno, String categoria){
		List <Adiacenza> massimi= new ArrayList<>();
		massimi.add(dao.getAdiacenza(anno, categoria).get(0));
		for(Adiacenza a: this.dao.getAdiacenza(anno, categoria)) {
			if(!a.equals(dao.getAdiacenza(anno, categoria).get(0))&& a.getPeso()==dao.getAdiacenza(anno, categoria).get(0).getPeso())
				massimi.add(a);
		}
		return massimi;
	}

	public EventsDao getDao() {
		return dao;
	}

	public void setDao(EventsDao dao) {
		this.dao = dao;
	}

	public SimpleWeightedGraph<String, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public void setGrafo(SimpleWeightedGraph<String, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
	}
	
	public List <String> trovaPercorso(Adiacenza a){
		this.migliore= new ArrayList<String>();
		this.pesoMin=Integer.MAX_VALUE;
		List <String> parziale= new ArrayList <String>();
		parziale.add(a.getTipo1());
		cerca(a.getTipo2(), parziale,0);
		return migliore;
	}

	private void cerca(String tipo2, List<String> parziale,double peso) {
		if(parziale.size()==grafo.vertexSet().size()) {
		if(parziale.get(parziale.size()-1).equals(tipo2)) {
			if(this.migliore.size()==0) {
				this.pesoMin=peso;
				this.migliore= new ArrayList <>(parziale);
				return;
			}
			if(this.pesoMin>peso) {
				this.pesoMin=peso;
				this.migliore= new ArrayList <>(parziale);
				return;
			}
			return;
		}
		return;
		}
		String tipoUltimo= parziale.get(parziale.size()-1);
		for(DefaultWeightedEdge e: this.grafo.edgesOf(tipoUltimo)) {
			String prova= Graphs.getOppositeVertex(grafo, e, tipoUltimo);
			double pesoProva= grafo.getEdgeWeight(e);
			if(!parziale.contains(prova)) {
				parziale.add(prova);
				peso=peso+pesoProva;
				cerca(prova,parziale, peso);
				parziale.remove(prova);
				peso=peso-pesoProva;
				
			}
		}
		
		
	}

	

	
}
