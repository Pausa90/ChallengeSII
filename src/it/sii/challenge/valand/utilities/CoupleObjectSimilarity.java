package it.sii.challenge.valand.utilities;

/**
 * Classe di supporto che memorizza la coppia (Object, similarità).
 * Utile per ordinare liste di utenti o di business 
 * @author andrea e valerio
 *
 */
public class CoupleObjectSimilarity<T> {
        private double similarity;
        private T object;
        
        public CoupleObjectSimilarity(T object, double similarity){
                this.similarity = similarity;
                this.object = object;
        }
        
        public double getSimilarity() {
                return similarity;
        }
        
        public void setSimilarity(double similarity) {
                this.similarity = similarity;
        }
        
        public T getObject() {
                return object;
        }
        
        public void setObject(T object) {
                this.object = object;
        }
        
        public String toString(){
                return "("+this.object.toString()+","+this.similarity+")";
        }
        
}