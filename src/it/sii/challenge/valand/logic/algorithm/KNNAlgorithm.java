package it.sii.challenge.valand.logic.algorithm;

import it.sii.challenge.valand.logic.similarity.SimilarityCalculator;

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
        private List<CoupleObjectDistance> neighborsList;
        private List<String> allKeys; //Lista di chiavi delle mappe di entrambi i object
        
        public KNNAlgorithm(int k, SimilarityCalculator method){
                super(method);
                this.k = k;
                this.neighborsList = new LinkedList<CoupleObjectDistance>();
                this.allKeys = new LinkedList<String>();
        }

        @Override
        public int RatingPrediction(List<CoupleObjectDistance> couplesObjectDistances, Object newObject) {
                                
                for (CoupleObjectDistance c : couplesObjectDistances){
                        
                        this.setAllKeys(c, newObject);
                        double distance = this.calculateDistance(newObject,c);
                        this.neighborsList.add(new CoupleObjectDistance(c, distance));
                        this.allKeys.clear();
                }
                

                int prediction = this.predict(newObject);
                this.neighborsList.clear();
                
                return prediction;
        }

        private int predict(Object object) {
//                Collections.sort(this.neighborsList, new MinDistance());
//                
//                double positive = 0;
//                double negative = 0;
//                Object tmpObject;
//                
//                for (int i=0; i<this.k; i++){
//                        tmpObject = this.neighborsList.get(i).getObject();
//                        if (tmpObject.isPositive())
//                                positive++;
//                        else
//                                negative++;
//                }
//                
//                //Eventuali emotion positive e/o negative influiscono il giudizio
//                int emotions = object.getPositiveEmotionNumber() - object.getNegativeEmotionNumber();
//                if (emotions>0) //ho più emotion positive che negative
//                        positive += emotions * (this.emotionPredictionFactor);
//                else if (emotions<0) //ho più emotion negative che positive
//                        negative += Math.abs(emotions) * (this.emotionPredictionFactor);
//                
//                
//                if (positive<negative)
//                        return negativePrediction;
//                else 
//                        return positivePrediction;
        	
        	//TODO da fare metodo
        	return 0;
        }

        //TODO da fare!
        private double calculateDistance(Object object1, Object object2) {
                return 0;
                		//this.calculator.calculateSimilarity(object1, object2);
        }
        //TODO da fare!
        private void setAllKeys(Object t, Object newObject) {
//                for (String key : t.getObjectMap().keySet())
//                        this.allKeys.add(key);
//                
//                for (String key : newObject.getObjectMap().keySet())
//                        if (!this.allKeys.contains(key))
//                                this.allKeys.add(key);          
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
class CoupleObjectDistance {
        private double distance;
        private Object object;
        
        public CoupleObjectDistance(Object object, double distance){
                this.distance = distance;
                this.object = object;
        }
        
        public double getDistance() {
                return distance;
        }
        
        public void setDistance(double distance) {
                this.distance = distance;
        }
        
        public Object getObject() {
                return object;
        }
        
        public void setObject(Object object) {
                this.object = object;
        }
        
        public String toString(){
                return "("+this.object+","+this.distance+")";
        }
        
}

/**
 * Metodo di comparazione basato sulla distanza tra due object
 * @author andrea
 *
 */
class MinDistance implements Comparator<CoupleObjectDistance>{

        public int compare(CoupleObjectDistance c1, CoupleObjectDistance c2) {
                return Double.compare(c1.getDistance(),c2.getDistance());
        }
        
}