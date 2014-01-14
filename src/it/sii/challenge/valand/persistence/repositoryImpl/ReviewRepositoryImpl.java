package it.sii.challenge.valand.persistence.repositoryImpl;

import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.Review;
import it.sii.challenge.valand.persistence.DataSource;
import it.sii.challenge.valand.persistence.PersistenceException;
import it.sii.challenge.valand.persistence.repository.ReviewRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ReviewRepositoryImpl implements ReviewRepository{
	private DataSource d= new DataSource();
	private Connection c=null;
	
	@Override
	public List<Review> findAll() {
		Connection c=null;
		List<Review> listReview = new LinkedList<Review>();
		
		try {
			 c=d.getConnection();
		} catch (PersistenceException e) {
			e.printStackTrace();
			return null;
		}
		String find="select * from Review";
		try {
			PreparedStatement ps=c.prepareStatement(find);
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				listReview.add(new Review(rs.getString("business_id"), rs.getString("user_id"), rs.getInt("stars")));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		finally {
			try {
				if (c!= null) c.close();
			} catch (SQLException e) {
				try {
					throw new PersistenceException(e.getMessage());
				} catch (PersistenceException e1) {
					e1.printStackTrace();
				}
			}
		}
		return listReview;
	}

	@Override
	public Review findById(String b_id, String u_id) {
		Connection c=null;
		try {
			 c=d.getConnection();
			
		} catch (PersistenceException e) {
			e.printStackTrace();
			return null;
		}
		String find="select * from Review where business_id=? and user_id=?";
		try {
			PreparedStatement ps=c.prepareStatement(find);
			ps.setString(1, b_id);
			ps.setString(2, u_id);
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				Review r=new Review(rs.getString("business_id"), rs.getString("user_id"), rs.getInt("stars"));
				return r;
			}
			
		} catch (SQLException e) {
		
			e.printStackTrace();
			return null;
		}
		finally {
			try {
				if (c!= null) c.close();
			} catch (SQLException e) {
				try {
					throw new PersistenceException(e.getMessage());
				} catch (PersistenceException e1) {
					e1.printStackTrace();
				}
			}
		}
		return null;
	}

	
	@Override
	public boolean insert(Review b) {
		Connection c = null;
		try {

			c=d.getConnection();
			PreparedStatement statement = null;
			
			String insert = "insert into Review(business_id, user_id, stars) values (?,?,?)";
			statement = c.prepareStatement(insert);
			statement.setString(1, b.getBusinessId());
			statement.setString(2, b.getUserId());
			statement.setInt(3, b.getStars());
			statement.executeUpdate();
			
			return true;
			
		}  catch (SQLException e) {
			try {
				throw new PersistenceException(e.getMessage());
			} catch (PersistenceException e1) {
				e1.printStackTrace();
			}
			return false;
		} catch (PersistenceException e) {
			e.printStackTrace();
			return false;
		}
		finally {
			try {
				if (c!= null) c.close();
			} catch (SQLException e) {
				try {
					throw new PersistenceException(e.getMessage());
				} catch (PersistenceException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	
	@Override
	public boolean insertList(List<Review> listReview){
		PreparedStatement statement = null; 
		try{
			c=d.getConnection();

			int i=1;
			for(Review r : listReview){
				statement = null;
				
				String insert = "insert into Review(business_id, user_id, stars) values (?,?,?)";
				statement = c.prepareStatement(insert);
				statement.setString(1, r.getBusinessId());
				statement.setString(2, r.getUserId());
				statement.setInt(3, r.getStars());
				statement.executeUpdate();
				
				if(i%1000==0)
					System.out.println("inserita la Review numero: " +i+ " !!!!!" );
				i++;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally {
			try {
				if (c!= null) c.close();
			} catch (SQLException e) {
				try {
					throw new PersistenceException(e.getMessage());
				} catch (PersistenceException e1) {
					e1.printStackTrace();
				}
			}
		}
		return false;
	}

	
	
}
