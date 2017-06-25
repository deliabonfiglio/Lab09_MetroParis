package it.polito.tdp.metrodeparis.model;

import java.util.*;

public class FermataIdMap {

	private Map<Integer, Fermata> map;
	
	public FermataIdMap(){
		map = new HashMap<Integer, Fermata>();
	}
	
	
	public Fermata get(Integer id){
		return map.get(id);
	}
	
	public Fermata put(Fermata value){
		Fermata old = map.get(value);
		if(old == null){
			map.put(value.getIdFermata(), value);
			return value;
		} else 
			return old;
	}
}
