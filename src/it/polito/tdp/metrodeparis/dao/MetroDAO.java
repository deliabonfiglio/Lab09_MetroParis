package it.polito.tdp.metrodeparis.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.metrodeparis.model.Fermata;
import it.polito.tdp.metrodeparis.model.FermataConLinea;
import it.polito.tdp.metrodeparis.model.FermataIdMap;
import it.polito.tdp.metrodeparis.model.FermatePair;
import it.polito.tdp.metrodeparis.model.Linea;

public class MetroDAO {

	public List<Fermata> getAllFermate(FermataIdMap map) {

		final String sql = "SELECT id_fermata, nome, coordx, coordy FROM fermata ORDER BY nome ASC";
		List<Fermata> fermate = new ArrayList<Fermata>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Fermata f = new Fermata(rs.getInt("id_Fermata"), rs.getString("nome"), new LatLng(rs.getDouble("coordx"), rs.getDouble("coordy")));
				fermate.add(f);
				map.put(f);
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return fermate;
	}
	public List<FermatePair> getFermateCollegate(){
		List<FermatePair> stops = new ArrayList<FermatePair>();
		
		final String sql = "SELECT * FROM connessione ";
		
		try{	
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

				while (rs.next()) {
					FermatePair fp = new FermatePair(rs.getInt("id_linea"), rs.getInt("id_stazP"), rs.getInt("id_stazA"));
					stops.add(fp);

				}
		
				st.close();
				conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		
		return stops;
	}
	public Map<Integer, Linea> getMappaLinea() {
		Map<Integer, Linea> mapL= new TreeMap<Integer, Linea> ();
		
		final String sql = "SELECT l.id_linea, l.velocita , l.nome, l.intervallo, l.colore "+
				"FROM linea l ";
		
		try{	
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

				while (rs.next()) {
					Linea l = new Linea(rs.getInt("id_Linea"), rs.getInt("velocita"), rs.getString("nome"), rs.getDouble("intervallo"), rs.getString("colore"));
					mapL.put(l.getId(), l);

				}
		
				st.close();
				conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return mapL;
	}
	public List<FermataConLinea> getFermateConLinea(FermataIdMap map) {
		final String sql = "SELECT DISTINCT f.id_fermata, c.id_linea "+
				"FROM fermata as f, connessione as c "+
				"WHERE f.id_fermata=c.id_stazP";
		List<FermataConLinea> fermate = new ArrayList<FermataConLinea>();
		
		try {
		Connection conn = DBConnect.getInstance().getConnection();
		PreparedStatement st = conn.prepareStatement(sql);
		ResultSet rs = st.executeQuery();
		
		while (rs.next()) {
			Fermata f = map.get(rs.getInt("id_Fermata"));
		
			FermataConLinea fl = new FermataConLinea(f, rs.getInt("id_linea"));
			fermate.add(fl);
			
			//per aggiungere rif della figlia nel padre 
			//creo un nuovo oggetto fermata per trovarlo nella lista delle fermate che ho già
			//con il get usato su una lista prendo l'oggetto in pos restituita da index of che tutavia resituisce il primo incontrato quindi quello già esistente
			f.addChild(fl);
		}
		
		st.close();
		conn.close();
		
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}
		return fermate;
	}
		
}