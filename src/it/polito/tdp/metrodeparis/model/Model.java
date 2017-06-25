package it.polito.tdp.metrodeparis.model;

import java.util.*;
import org.jgrapht.Graphs;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;
import it.polito.tdp.metrodeparis.dao.MetroDAO;

public class Model {

	private List<Fermata> fermate;
	private DirectedWeightedMultigraph<FermataConLinea,DefaultWeightedEdge> graph;
	private FermataIdMap mapFermate;
	private Map<Integer, Linea> mapLinee;
	private DijkstraShortestPath<FermataConLinea, DefaultWeightedEdge> dj;
	private List<DefaultWeightedEdge> bestPath;
	private double bestTime;
	
	
	public Model(){
		this.mapFermate= new FermataIdMap();
	}
		
	public List<Fermata> getFermate(){
		if(fermate==null){
			MetroDAO dao = new MetroDAO();
			fermate= dao.getAllFermate(mapFermate);
		}
		return fermate;
	}
	/*
	public void creaGrafo(){
		if(graph==null)
			graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
		
		MetroDAO dao = new MetroDAO();
		this.mapLinee = dao.getMappaLinea();
		
		Graphs.addAllVertices(graph, this.getFermate());
		
		for(FermatePair fp : dao.getFermateCollegate()){
			Fermata f1 = mapFermate.get(fp.getId_partenza());
			Fermata f2 = mapFermate.get(fp.getId_arrivo());
			Linea l = mapLinee.get(fp.getId_linea());
			
			double distanza = LatLngTool.distance( f1.getCoords(), f2.getCoords(), LengthUnit. KILOMETER);
			double peso = distanza/l.getVelocita();
			
			DefaultWeightedEdge e = graph.addEdge(f1, f2);
			graph.setEdgeWeight(e, peso);
		}
		System.out.println(graph.toString());
		
	}

	public double pesoCamminoMinimo(Fermata partenza, Fermata arrivo) {
		this.partenza=partenza;
		this.arrivo=arrivo;

		dj=new DijkstraShortestPath<Fermata,DefaultWeightedEdge>(graph, partenza, arrivo);
		
		return dj.getPathLength()*3600+(dj.getPathEdgeList().size())*30;
	
	}

	public List<Fermata> camminoMinimo() {
		List<Fermata> cammino = new ArrayList<Fermata>();
		dj=new DijkstraShortestPath<Fermata,DefaultWeightedEdge>(graph, partenza, arrivo);
		
		for(DefaultWeightedEdge dwe: dj.getPathEdgeList()){
			Fermata fs = graph.getEdgeSource(dwe);
			Fermata fa = graph.getEdgeTarget(dwe);
			
			if(!cammino.contains(fs)){
				cammino.add(fs);
			}
			else
				cammino.add(fa);
		}
		
		
		return cammino;
	}
	*/
	public void creaGrafo(){
		if(graph==null)
			graph = new DirectedWeightedMultigraph<>(DefaultWeightedEdge.class);
		
		MetroDAO dao = new MetroDAO();
		this.mapLinee = dao.getMappaLinea();
		
		List<FermataConLinea> tutteFermate= dao.getFermateConLinea(mapFermate);
		
		Graphs.addAllVertices(graph, tutteFermate);
		

		for(FermatePair fp : dao.getFermateCollegate()){
			Fermata f1 = mapFermate.get(fp.getId_partenza());
			Fermata f2 = mapFermate.get(fp.getId_arrivo());
			Linea l = mapLinee.get(fp.getId_linea());
			
			double distanza = LatLngTool.distance( f1.getCoords(), f2.getCoords(), LengthUnit. KILOMETER);
			double peso = distanza/l.getVelocita();
			
			FermataConLinea fl1= new FermataConLinea(f1, l.getId());
			FermataConLinea fl2 = new FermataConLinea(f2, l.getId());
			
			DefaultWeightedEdge e = graph.addEdge(fl1, fl2);
			graph.setEdgeWeight(e, peso);
			
			DefaultWeightedEdge e2 = graph.addEdge(fl2, fl1);
			graph.setEdgeWeight(e2, peso);
		}
		
		for(Fermata f: fermate){
			for(FermataConLinea fclP: f.getChildren()){
				for(FermataConLinea fclA: f.getChildren()){
					if(!fclP.equals(fclA)){
						DefaultWeightedEdge a = graph.addEdge(fclP,fclA);
						graph.setEdgeWeight(a, (mapLinee.get(fclA.getId_linea()).getIntervallo())/60);
					}
				}	
			}
		}	
			System.out.println(graph.toString());
	}

	public void camminoMinimo(Fermata partenza, Fermata arrivo) {
		List<DefaultWeightedEdge> cammino = null;
		double tempo=Double.MAX_VALUE;
		
		for(FermataConLinea flp: partenza.getChildren()){
			for(FermataConLinea fla: arrivo.getChildren()){
				dj = new DijkstraShortestPath<FermataConLinea, DefaultWeightedEdge>(graph, flp, fla);
				
				if(dj.getPathLength()<tempo){
					tempo = dj.getPathLength();
					cammino=dj.getPathEdgeList();
				}
			}
		}
		bestPath= cammino;
		bestTime= tempo;
		
		if (bestPath == null)
			throw new RuntimeException("Non è stato creato un percorso.");

		// Nel calcolo del tempo non tengo conto della prima e dell'ultima
		if (bestPath.size() - 1 > 0) {
			bestTime += (bestPath.size() - 1) * 30;
		}
	}

	public String getShortestPath(){
		if (bestPath == null)
			throw new RuntimeException("Non è stato creato un percorso.");

		Linea lineaTemp;
		StringBuilder risultato = new StringBuilder();
		risultato.append("Percorso:\n\n[");
		
		for(DefaultWeightedEdge dwe : bestPath){
			FermataConLinea f1 = graph.getEdgeSource(dwe);
			FermataConLinea f2 = graph.getEdgeTarget(dwe);
			
			//risultato.append("Prendo Linea: " + lineaTemp.getNome() + "\n[");
			
			risultato.append(f1.getFermata().toString());
			
			if(f1.getId_linea()!=f2.getId_linea()){
				risultato.append("]\n\nCambio su Linea: " + f2.getId_linea() + "\n[");
				lineaTemp = mapLinee.get(f2.getId_linea());
			} else {
				risultato.append(", ");
			}
			
		}
		risultato.setLength(risultato.length() - 2);
		risultato.append("]");

		return risultato.toString();
	}
	public double getTime(){
		return bestTime;
	}

}
