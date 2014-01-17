package it.sii.challenge.valand.utilities;

import java.util.LinkedList;
import java.util.List;

public class PredictionList <T>{
	private List<CoupleObjectSimilarity<T>> list;
	private int commonsValue;
	
	public PredictionList(List<CoupleObjectSimilarity<T>> list, int value){
		this.list = list;
		this.commonsValue = value;
	}
	
	public PredictionList(){
		this.list = new LinkedList<CoupleObjectSimilarity<T>>();
		this.commonsValue = 0;
	}
	
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
	
}
