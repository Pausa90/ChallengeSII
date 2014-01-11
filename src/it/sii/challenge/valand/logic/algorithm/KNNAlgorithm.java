package it.sii.challenge.valand.logic.algorithm;

import it.sii.challenge.valand.logic.similarity.SimilarityCalculator;
import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.Review;
import it.sii.challenge.valand.model.User;
import it.sii.challenge.valand.model.UserBusinessMatrix;
import it.sii.challenge.valand.utilities.MapsListsUtilities;
import it.sii.challenge.valand.utilities.MapsListsUtilitiesTest;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class KNNAlgorithm extends ClassificationAlgorithm {
        
        /**
         * I conti di KNN vengono realizzati tramite una matrice sparsa.
         * Il suo output è una lista di oggetti, che vengono ordinati sulla base della distanza dal
         * Rating i-esimo.
         * Si considerano infine i primi n elementi, riportando il valore scelto.
         */
		private int k;
	
		public KNNAlgorithm(int k) {
			this.k = k;
		}
		
		@Override
		public List<User> getNeighborHood(UserBusinessMatrix matrix, User user) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<Business> getNeighborHood(UserBusinessMatrix matrix, Business business) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public int itemBasedPrediction(List<Business> neighborhood, Review review) {
			// TODO Auto-generated method stub
			return 0;
		}


		@Override
		public int userBasedPrediction(List<User> neighborhood, Review review) {
			// TODO Auto-generated method stub
			return 0;
		}
        
        public int getK() {
                return k;
        }
        

        public void setK(int k) {
                this.k = k;
        }

}

/**
 * Classe di supporto che memorizza la coppia (Object, distanza).
 * @author andrea
 *
 */
class CoupleObjectDistance<T> {
        private double distance;
        private T object;
        
        public CoupleObjectDistance(T object, double distance){
                this.distance = distance;
                this.object = object;
        }
        
        public double getDistance() {
                return distance;
        }
        
        public void setDistance(double distance) {
                this.distance = distance;
        }
        
        public T getObject() {
                return object;
        }
        
        public void setObject(T object) {
                this.object = object;
        }
        
        public String toString(){
                return "("+this.object.toString()+","+this.distance+")";
        }
        
}

/**
 * Metodo di comparazione basato sulla distanza tra due object
 * @author andrea
 * @param <T>
 *
 */
class MinDistance<T> implements Comparator<CoupleObjectDistance<T>>{

        public int compare(CoupleObjectDistance<T> c1, CoupleObjectDistance<T> c2) {
                return Double.compare(c1.getDistance(),c2.getDistance());
        }
        
}