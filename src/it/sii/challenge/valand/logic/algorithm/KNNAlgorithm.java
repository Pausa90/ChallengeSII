package it.sii.challenge.valand.logic.algorithm;

import it.sii.challenge.valand.logic.SimilarityCalculator;
import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.Review;
import it.sii.challenge.valand.model.User;
import it.sii.challenge.valand.model.UserBusinessMatrix;
import it.sii.challenge.valand.persistence.repository.BusinessRepository;
import it.sii.challenge.valand.persistence.repository.UserRepository;
import it.sii.challenge.valand.persistence.repositoryImpl.BusinessRepositoryImpl;
import it.sii.challenge.valand.persistence.repositoryImpl.UserRepositoryImpl;
import it.sii.challenge.valand.utilities.CoupleObjectSimilarity;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class KNNAlgorithm extends ClassificationAlgorithm {
        
        /**
         * I conti di KNN vengono realizzati tramite una matrice sparsa.
         * Il suo output è una lista di oggetti, che vengono ordinati sulla base della distanza dal
         * Rating i-esimo.
         * Si considerano infine i primi n elementi, riportando il valore scelto.
         */
		private int k;
	
		public KNNAlgorithm(SimilarityCalculator calculator, int k) {
			super(calculator);
			this.k = k;
		}
		
		@Override
		public List<CoupleObjectSimilarity<User>> getNeighborHood(UserBusinessMatrix matrix, User user, Business business) {			
			Map<String, Integer> column = matrix.getItemRatingsByAllUsers(business.getId());
			Map<String, Integer> rowUser = matrix.getUserValutatedItems(user.getId());	
			
			List<CoupleObjectSimilarity<User>> neighborhood = new LinkedList<CoupleObjectSimilarity<User>>();
			Map<String, Integer> rowU;
			double similarity;
			for (String u : column.keySet()){	
				if (!user.equals(u)){
					rowU = matrix.getUserValutatedItems(u);	
					similarity = this.getSimilarity(user, matrix.getUser(u), rowUser, rowU);
					if (similarity > SIMILARITY_TRESHOLD)
						neighborhood.add(new CoupleObjectSimilarity<User>(matrix.getUser(u), similarity));
				}
			}
			return neighborhood;
		}


		private double getSimilarity(User user, User u, Map<String, Integer> rowUser, Map<String, Integer> rowU) {
			return this.similarityCalculator.doPearsonSimilarity(rowUser, rowU,	user.getAverageStars(), u.getAverageStars());
		}

		@Override
		public List<CoupleObjectSimilarity<Business>> getNeighborHood(UserBusinessMatrix matrix, Business business, User user) {
			Map<String, Integer> row = matrix.getUserValutatedItems(user.getId());
			Map<String, Integer> columnBusiness = matrix.getItemRatingsByAllUsers(business.getId());	
			
			List<CoupleObjectSimilarity<Business>> neighborhood = new LinkedList<CoupleObjectSimilarity<Business>>();
			Map<String, Integer> columnB;
			double similarity;
			for (String id_business : row.keySet()){	
				if (!business.equals(id_business)){
					columnB = matrix.getItemRatingsByAllUsers(id_business);
					similarity = this.getSimilarity(business, matrix.getBusiness(id_business), columnBusiness, columnB);
					if (similarity > SIMILARITY_TRESHOLD)
						neighborhood.add(new CoupleObjectSimilarity<Business>(matrix.getBusiness(id_business), similarity));
				}
			}
			return neighborhood;
		}
		
		private double getSimilarity(Business business, Business b, Map<String, Integer> columnBusiness, Map<String, Integer> columnB) {
			return this.similarityCalculator.doAdjustedCosineSimilarity(columnBusiness, columnB);
				
		}
		
		@Override
		public int itemBasedPrediction(List<CoupleObjectSimilarity<Business>> neighborhood, Review review, User user, UserBusinessMatrix matrix) {
			Collections.sort(neighborhood, new MaxSimilarity<Business>());
			
			double predictNumerator = 0;
			double predictDenominator = 0;
			
			int i = 0;
			
			for (CoupleObjectSimilarity<Business> b : neighborhood){
				if (i == this.k)	break;
				predictNumerator += b.getSimilarity() * (matrix.getRatingByUserItem(user, b.getObject()));
				predictDenominator += b.getSimilarity();
				i++;
			}
			
			this.setStars(review, predictNumerator/predictDenominator);
			return review.getStars();
		}


		@Override
		public int userBasedPrediction(List<CoupleObjectSimilarity<User>> neighborhood, Review review, User user, Business business, UserBusinessMatrix matrix) {
			Collections.sort(neighborhood, new MaxSimilarity<User>());
			
			double predictNumerator = 0;
			double predictDenominator = 0;
			
			int i = 0;
			
			for (CoupleObjectSimilarity<User> u : neighborhood){
				if (i == this.k)	break;
				predictNumerator += u.getSimilarity() * (matrix.getRatingByUserItem(u.getObject(), business) - u.getObject().getAverageStars());
				predictDenominator += u.getSimilarity();
				i++;
			}
			
			this.setStars(review, user.getAverageStars() + predictNumerator/predictDenominator);
			return review.getStars();
		}
        
        private void setStars(Review review, double predicted) {
        	int casted = (int) predicted;
			if (predicted-casted > 0.5)
				review.setStars(casted+1);
			else
				review.setStars(casted);
		}

		public int getK() {
                return k;
        }
        

        public void setK(int k) {
                this.k = k;
        }

}

/**
 * Metodo di comparazione basato sulla distanza tra due object
 * @author andrea
 * @param <T>
 *
 */
class MaxSimilarity<T> implements Comparator<CoupleObjectSimilarity<T>>{

        public int compare(CoupleObjectSimilarity<T> c1, CoupleObjectSimilarity<T> c2) {
                return Double.compare(c2.getSimilarity(),c1.getSimilarity());
        }
}