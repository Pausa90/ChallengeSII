package it.sii.challenge.valand.utilities;

import java.util.LinkedList;
import java.util.List;

/**
 * Classe utilizzata per creare una lista di user/business.
 * Si comporta come una semplice List<T> dall'esterno, ma permette l'accesso
 * alla variabile commonsValue in O(1) invece che in O(n)
 * @author andrea
 * @param <T>
 */

public class PredictionList <T>{
	private List<CoupleObjectSimilarity<T>> list;
	private int commonsValue; //Determina l'affidabilità dell'insieme 
	
	public PredictionList(List<CoupleObjectSimilarity<T>> list, int value){
		this.list = list;
		this.commonsValue = value;
	}
	
	public PredictionList(){
		this.list = new LinkedList<CoupleObjectSimilarity<T>>();
		this.commonsValue = 0;
	}
	
	/**
	 * CommonsValue determina l'affidabilità della lista
	 * @return
	 */
	public int getCommonsValue() {
		return this.commonsValue;
	}
	
	public List<CoupleObjectSimilarity<T>> getList() {
		return this.list;
	}
	
	public void setCommonsValue(int commonsValue) {
		this.commonsValue = commonsValue;
	}
	
	public void add(CoupleObjectSimilarity<T> elem, int value){
		this.list.add(elem);
		this.commonsValue += value;
	}
	
	public void remove(CoupleObjectSimilarity<T> elem, int value){
		this.list.remove(elem);
		this.commonsValue -= value;
	}

	public int size(){
		return this.list.size();
	}

	public void setList(List<CoupleObjectSimilarity<T>> list) {
		this.list = list;		
	}
	
}
