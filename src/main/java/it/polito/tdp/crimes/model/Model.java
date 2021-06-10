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
		Double pesoMin;
		
		
		
		public Model() {
			dao= new EventsDao();
		}
		
		public void creaGrafo(String categoria, Integer anno) {
			grafo= new SimpleWeightedGraph <String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
			Graphs.addAllVertices(this.grafo, dao.listAllVertici(categoria, anno));
			for(Adiacenza a :dao.listAllAdiacenze(categoria, anno)) {
				if(grafo.containsVertex(a.getTipo1())&&grafo.containsVertex(a.getTipo2())) {
					Graphs.addEdge(this.grafo, a.getTipo1(), a.getTipo2(), a.getPeso());
				}
			}
		
		}
		
		public List<Adiacenza> getMassime(String categoria, Integer anno){
			List <Adiacenza> massime= new ArrayList<>();
			List <Adiacenza> tutte= dao.listAllAdiacenze(categoria, anno);
			massime.add(tutte.get(0));
			for(Adiacenza a : tutte ) {
				if(a.getPeso()==tutte.get(0).getPeso() && !a.equals(tutte.get(0))) {
					massime.add(a);
				}
			}
			return massime;
		}
		
		
		public List <String> trovaPercorso(DefaultWeightedEdge e ){
			this.migliore= new ArrayList<>();
			String partenza = grafo.getEdgeSource(e);
			
			this.pesoMin=0.0;
			String arrivo= grafo.getEdgeTarget(e);
			List <String> parziale= new ArrayList <>();
			parziale.add(partenza);
			cerca(arrivo, parziale, 0);
			return migliore;
			
		}
		
		
		private void cerca(String arrivo, List<String> parziale, double peso) {
			//caso terminale --> tocco tutti i vertici
			if(parziale.size()==grafo.vertexSet().size()) {
				if(parziale.get(parziale.size()-1).equals(arrivo)) {
					if(peso<this.pesoMin) {
						migliore= new ArrayList <>(parziale);
						this.pesoMin=peso;
						return;
					}
					return;
				}
				return;
			}
			
			String ultimo=parziale.get(parziale.size()-1);
			for(String s: Graphs.neighborListOf(grafo, ultimo)) {
				if(!parziale.contains(s)) {
					DefaultWeightedEdge e= grafo.getEdge(ultimo, s);
					double pesoProva= grafo.getEdgeWeight(e);
					parziale.add(s);
					cerca( arrivo, parziale, peso+pesoProva);
					parziale.remove(parziale.get(parziale.size()-1));
					peso=peso-pesoProva;
					
				}
			}
			
		}

		public EventsDao getDao() {
			return dao;
		}

		

		public SimpleWeightedGraph<String, DefaultWeightedEdge> getGrafo() {
			return grafo;
		}

		

		public int getNVertici() {
			return this.grafo.vertexSet().size();
		}
		
		public int getNArchi() {
			return this.grafo.edgeSet().size();
		}

		public List<String> listAllCategorie(){
			return dao.listAllCategorie();
		}
		
		
		public List<Integer> listAllAnni(){
			return dao.listAllAnni();
		}
	
}
