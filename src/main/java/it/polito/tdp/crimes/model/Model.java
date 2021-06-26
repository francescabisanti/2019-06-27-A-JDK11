package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
		EventsDao dao;
		SimpleWeightedGraph <String, DefaultWeightedEdge> grafo;
		List <String> migliore;
		
		public Model() {
			dao= new EventsDao ();
		}
		
		
		public void creaGrafo(String categoria, Integer anno) {
			grafo= new SimpleWeightedGraph <String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
			Graphs.addAllVertices(grafo, dao.getAllVertici(categoria, anno));
			for(Adiacenza a: dao.getAllAdiacenze(categoria, anno)) {
				if(grafo.containsVertex(a.getTipo1())&& grafo.containsVertex(a.getTipo2())) {
					Graphs.addEdge(this.grafo, a.getTipo1(), a.getTipo2(), a.getPeso());
				}
			}
		
		}
		
		public List <DefaultWeightedEdge> trovaMassimi(){
			Double pesoMax=0.0;
			for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
				if(grafo.getEdgeWeight(e)>pesoMax)
					pesoMax= grafo.getEdgeWeight(e);
			}
			List<DefaultWeightedEdge> result= new ArrayList <DefaultWeightedEdge>();
			for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
				if(grafo.getEdgeWeight(e)==pesoMax)
					result.add(e);
			}
			return result;
		}
		
		public List <String> trovaPercorso(DefaultWeightedEdge e){
			this.migliore= new ArrayList <String>();
			List <String> parziale= new ArrayList<>();
			parziale.add(grafo.getEdgeSource(e));
			cerca(parziale, grafo.getEdgeTarget(e));
			return migliore;
			
		}
		private void cerca(List<String> parziale, String edgeTarget) {
			Double peso= calcolaPeso(parziale);
			if(parziale.size()==grafo.vertexSet().size()) {
				if(parziale.get(parziale.size()-1).equals(edgeTarget)) {
					if(migliore.size()==0) {
						migliore=new ArrayList<>(parziale);
						return;
					}
				if(peso<calcolaPeso(migliore)) {
					migliore=new ArrayList <>(parziale);
					return;
				}
				return;
				}
				return;
			}
			String ultimo=parziale.get(parziale.size()-1);
			for(String p: Graphs.neighborListOf(grafo, ultimo)) {
				if(!parziale.contains(p)) {
					parziale.add(p);
					cerca(parziale, edgeTarget);
					parziale.remove(p);
				}
			}
			
		}


		public Double calcolaPeso(List<String> parziale) {
			Double peso=0.0;
			for(int i=1; i<parziale.size(); i++) {
				String a= parziale.get(i-1);
				String b=parziale.get(i);
				peso=peso+grafo.getEdgeWeight(grafo.getEdge(a, b));
			}
			return peso;
		}


		public EventsDao getDao() {
			return dao;
		}


		public SimpleWeightedGraph<String, DefaultWeightedEdge> getGrafo() {
			return grafo;
		}


		public int getNVertici() {
			return grafo.vertexSet().size();
		}
		
		public int getNArchi() {
			return grafo.edgeSet().size();
		}
		public List <String> getAllCategories(){
			return dao.getAllCategories();
		}
		public List <Integer> getAllAnni(){
			return dao.getAllAnni();
		}
}
