package it.sii.challenge.valand.logic.algorithm;

import it.sii.challenge.valand.logic.similarity.SimilarityCalculator;

import java.util.List;


public abstract class ClassificationAlgorithm {
        public final int initializedPrediction = -1;
        public final int positivePrediction = 4;
        public final int negativePrediction = 0;
        public SimilarityCalculator calculator;

        protected final double emotionPredictionFactor = 2.01;
        
        public ClassificationAlgorithm(SimilarityCalculator method){
                this.calculator = method;
        }
        

        public int getInitializedPrediction() {
                return initializedPrediction;
        }

        public int getPositivePrediction() {
                return positivePrediction;
        }

        public int getNegativePrediction() {
                return negativePrediction;
        }
        
        public abstract int RatingPrediction(List<CoupleObjectDistance> coupleObjectDistances, Object newObject);
        
}