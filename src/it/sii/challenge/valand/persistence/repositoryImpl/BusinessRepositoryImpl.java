package it.sii.challenge.valand.persistence.repositoryImpl;

import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.User;
import it.sii.challenge.valand.persistence.DataSource;
import it.sii.challenge.valand.persistence.PersistenceException;
import it.sii.challenge.valand.persistence.repository.BusinessRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class BusinessRepositoryImpl implements BusinessRepository{
	private DataSource d= new DataSource();
	private Connection c=null;
	
	@Override
	public List<Business> findAll() {
		Connection c=null;
		List<Business> listBusiness = new LinkedList<Business>();
		
		try {
			 c=d.getConnection();
		} catch (PersistenceException e) {
			e.printStackTrace();
			return null;
		}
		String find="select * from Business";
		try {
			PreparedStatement ps=c.prepareStatement(find);
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				listBusiness.add(new Business(rs.getString("business_id"), rs.getString("name"), rs.getDouble("stars"), rs.getInt("review_count")));
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
		return listBusiness;
	}

	@Override
	public Business findById(String id) {
		Connection c=null;
		try {
			 c=d.getConnection();
			
		} catch (PersistenceException e) {
			e.printStackTrace();
			return null;
		}
		String find="select * from Business where business_id=?";
		try {
			PreparedStatement ps=c.prepareStatement(find);
			ps.setString(1, id);
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				Business b=new Business(rs.getString("business_id"), rs.getString("name"), rs.getDouble("stars"), rs.getInt("review_count"));
				return b;
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
	public boolean insert(Business b) {
		Connection c = null;
		try {

			c=d.getConnection();
			PreparedStatement statement = null;
			
			String insert = "insert into Business(business_id, name, stars, review_count) values (?,?,?,?)";
			statement = c.prepareStatement(insert);
			statement.setString(1, b.getId());
			statement.setString(2, b.getName());
			statement.setDouble(3, b.getStars());
			statement.setDouble(4, b.getReviewCount());
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
	public boolean insertList(List<Business> listBusiness){
		try{
			int i=1;
			for(Business b : listBusiness){
				insert(b);
				if(i%1000==0)
					System.out.println("inserito il Business numero: " +i+ " !!!!!" );
				i++;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	
	
}
