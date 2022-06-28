package it.polito.tdp.genes.model;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {
	private GenesDao dao;
	private Graph<Integer, DefaultWeightedEdge> grafo;
	private List<Integer> vertici;
	private double pesoMinimo=100000;
	private double pesoMassimo=0;
	private List<DefaultWeightedEdge> archiMaggiori;
	private List<Integer> best;
	private double lunghezza;
	private double soglia;
	
	public double getLunghezza() {
		return this.lunghezza;
	}
	
	public Model() {
		this.dao= new GenesDao();
	}
	
	public void creaGrafo() {
		//Inizializzo
		this.grafo= new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		this.vertici= new ArrayList<>();
		
		//Creo i vertici
		for(int i: this.dao.getCromosomi()) {
			this.vertici.add(i);
		}
		Graphs.addAllVertices(this.grafo, vertici);
		
		
		//Creo gli archi
		
		/*for(int c1: this.vertici) {
			for(int c2: this.vertici) {
				if(!this.grafo.containsEdge(c2, c1) && c1!=c2) {
					double peso= this.dao.getPesoArco(c1, c2);
					if(peso!=0.0) {
					Graphs.addEdgeWithVertices(this.grafo, c1, c2, peso);
					System.out.println("Arco da "+c1+" a "+ c2+" peso: "+peso);
					if(peso>pesoMassimo) {
						pesoMassimo=peso;
					}
					
					if(peso<pesoMinimo) {
						this.pesoMinimo=peso;
					}
					
					}
				}
				
			}
		}*/
		/*for(int c1: this.vertici) {
			for(int c2: this.vertici) {
				if(c1<c2) {
					double peso= this.dao.getPesoArco(c1, c2);
					if(peso!=0) {
						Graphs.addEdgeWithVertices(this.grafo, c1, c2, peso);
						System.out.println("Arco da "+c1+" a "+ c2+" peso: "+peso);
						if(peso>pesoMassimo) {
							pesoMassimo=peso;
						}
						
						if(peso<pesoMinimo) {
							this.pesoMinimo=peso;
						}
					}
				}else if(c1>c2 && !this.grafo.containsEdge(c2, c1)) {
					double peso= this.dao.getPesoArco(c1, c2);
					Graphs.addEdgeWithVertices(this.grafo, c1, c2, peso);
					System.out.println("Arco da "+c1+" a "+ c2+" peso: "+peso);
					if(peso>pesoMassimo) {
						pesoMassimo=peso;
					}
					
					if(peso<pesoMinimo) {
						this.pesoMinimo=peso;
					}
				}
			}
		}*/
		
		for(int c1: this.vertici) {
			for(int c2: this.vertici) {
				if(c1!=c2) {
					
					
					double peso= 0.0;
							//this.dao.getPesoArco(c1, c2);
					for(double d: this.dao.getPesoArco(c1, c2)) {
						peso=peso+d;
					}
					if(this.dao.getPesoArco(c1, c2).size()!=0) {
						Graphs.addEdgeWithVertices(this.grafo, c1, c2, peso);
						System.out.println("Arco da "+c1+" a "+ c2+" peso: "+peso);
						if(peso>pesoMassimo) {
							pesoMassimo=peso;
						}
						
						if(peso<pesoMinimo) {
							this.pesoMinimo=peso;
						}
					}
				}
			}
		}
		
	}
	
	public int getNVertici(){
		return this.grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}

	public double getPesoMinimo() {
		return pesoMinimo;
	}

	public double getPesoMassimo() {
		return pesoMassimo;
	}
	
	/**
	 * 
	 * PUNTO 1D
	 */
	
	public String getMaggioriMinori(double soglia) {
		List<DefaultWeightedEdge> maggiori=new ArrayList<>();
		List<DefaultWeightedEdge> minori= new ArrayList<>();
		this.archiMaggiori= new ArrayList<>();
		
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			double peso= this.grafo.getEdgeWeight(e);
			if(peso<soglia) {
				minori.add(e);
			}else if(peso>soglia) {
				maggiori.add(e);
				this.archiMaggiori.add(e);
			}
		}
		String ret="Maggiori: "+maggiori.size()+"   Minori: "+minori.size();
		return ret;
	}

	
	/**
	 * RICORSIONE: CAMMINO PIU LUNGO COMPOSTO SOLO DA VERTICI DI PESO>SOGLIA
	 */
	
	public List<Integer> calcolaPercorso(double soglia){
		this.best= new ArrayList<>();
		this.lunghezza=0;
		this.soglia= soglia;
		
		List<Integer> parziale= new ArrayList<>();
		
		cerca(parziale, 0);
		return best;
	}

	private void cerca(List<Integer> parziale, double pesoTot) {
		//condizioni di ricorsione: inserisco solo se c1-->c2 maggiore di soglia
		for(Integer i: this.vertici) {
			if(parziale.size()==0) {
				parziale.add(i);
				cerca(parziale,0);
				parziale.remove(0);
			}else {
				int precedente= parziale.get(parziale.size()-1);
				if(!parziale.contains(i)) {
					double peso= this.grafo.getEdgeWeight(this.grafo.getEdge(precedente, i));
					if(peso>this.soglia) {
						parziale.add(i);
						pesoTot=pesoTot+peso;
						cerca(parziale, pesoTot);
						parziale.remove(parziale.size()-1);
					}
				}
				
				
			}
		}
		
		//Condizioni per il best
		if(pesoTot>this.lunghezza) {
			this.best= new ArrayList<>(parziale);
			this.lunghezza= pesoTot;
		}
		
	}
	
	
}