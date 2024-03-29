package it.sii.challenge.valand.logic.algorithm;

import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.Review;
import it.sii.challenge.valand.model.User;
import it.sii.challenge.valand.model.UserBusinessMatrix;
import it.sii.challenge.valand.utilities.PredictionList;


public abstract class ClassificationAlgorithm {
        public final int initializedPrediction = -1;
        public final double SIMILARITY_TRESHOLD = 0.8; //Soglia di similarità minima
        
        public abstract int itemBasedPrediction(PredictionList<Business> neighborhood, Review review, User user, UserBusinessMatrix matrix);
        public abstract int userBasedPrediction(PredictionList<User> neighborhood, Review review, User user, Business business, UserBusinessMatrix matrix);
        public abstract PredictionList<User> getNeighborHood(UserBusinessMatrix matrix, User user, Business business);
        public abstract PredictionList<Business> getNeighborHood(UserBusinessMatrix matrix, Business business, User user);
        
}