package it.sii.challenge.valand.persistence.repositoryImpl;

import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.Review;
import it.sii.challenge.valand.persistence.DataSource;
import it.sii.challenge.valand.persistence.repository.MatrixRepository;
import it.sii.challenge.valand.persistence.repository.ReviewRepository;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatrixRepositoryImpl implements MatrixRepository{
	private DataSource d= new DataSource();
	private Connection c=null;

	@Override
	public boolean insertUserRatings(Map<String, Integer> userRatings) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertBusinessRatings(Map<String, Integer> userRatings) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, Integer> findBusinessRatings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Integer> findUserRatings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Business findARating(String u_id, String b_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Map<String, Integer>> getMatrix() {
		Map<String, Map<String, Integer>> matrix = new HashMap<String, Map<String,Integer>>();
		ReviewRepository repo = new ReviewRepositoryImpl();
		
		List<Review> listReview = repo.findAll();
		
		for(Review r : listReview){
			if(matrix.keySet().contains(r.getUserId())){
				Map<String, Integer> tempMap = matrix.get(r.getUserId());
				tempMap.put(r.getBusinessId(), r.getStars());
			}
			else{
				Map<String, Integer> tempMap = new HashMap<String, Integer>();
				tempMap.put(r.getBusinessId(), r.getStars());
				matrix.put(r.getUserId(), tempMap);
			}
		}
		
		return matrix;
	}
	
}
