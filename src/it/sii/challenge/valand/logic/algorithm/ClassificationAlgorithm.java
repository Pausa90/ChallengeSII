package it.sii.challenge.valand.logic.algorithm;

import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.Review;
import it.sii.challenge.valand.model.User;
import it.sii.challenge.valand.model.UserBusinessMatrix;

import java.util.List;


public abstract class ClassificationAlgorithm {
        public final int initializedPrediction = -1;

        public abstract int itemBasedPrediction(List<Business> neighborhood, Review review);
        public abstract int userBasedPrediction(List<User> neighborhood, Review review);
        public abstract List<User> getNeighborHood(UserBusinessMatrix matrix, User user);
        public abstract List<Business> getNeighborHood(UserBusinessMatrix matrix, Business business);
        
}