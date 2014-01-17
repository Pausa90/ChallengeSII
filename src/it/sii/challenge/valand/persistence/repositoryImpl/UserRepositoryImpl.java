package it.sii.challenge.valand.persistence.repositoryImpl;

import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.User;
import it.sii.challenge.valand.persistence.DataSource;
import it.sii.challenge.valand.persistence.PersistenceException;
import it.sii.challenge.valand.persistence.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository{
	private DataSource d= new DataSource();
	private Connection c=null;
	
	@Override
	public List<User> findAll() {
		Connection c=null;
		List<User> listUsers = new LinkedList<User>();
		
		try {
			 c=d.getConnection();
		} catch (PersistenceException e) {
			e.printStackTrace();
			return null;
		}
		String find="select * from User";
		try {
			PreparedStatement ps=c.prepareStatement(find);
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				listUsers.add(new User(rs.getString("user_id"), rs.getInt("review_count"), rs.getDouble("average_stars")));
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
		return listUsers;
	}

	@Override
	public User findById(String id) {
		Connection c=null;
		try {
			 c=d.getConnection();
			
		} catch (PersistenceException e) {
			e.printStackTrace();
			return null;
		}
		String find="select * from User where user_id=?";
		try {
			PreparedStatement ps=c.prepareStatement(find);
			ps.setString(1, id);
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				String id2=rs.getString("user_id");
				int reviewCount=rs.getInt("review_count");
				double averageStars=rs.getDouble("average_stars");
				User u=new User(id2, reviewCount, averageStars);
				return u;
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
	public boolean insert(User u) {
		Connection c = null;
		try {

			c=d.getConnection();
			PreparedStatement statement = null;
			String insert = "insert into User(user_id, review_count, average_stars) values (?,?,?)";
			statement = c.prepareStatement(insert);
			statement.setString(1, u.getId());
			statement.setInt(2, u.getReviewCount());
			statement.setDouble(3, u.getAverageStars());
			statement.executeUpdate();
			return true;
			
			
		}  catch (SQLException e) {
			try {
				throw new PersistenceException(e.getMessage());
			} catch (PersistenceException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
			
		} catch (PersistenceException e) {
			e.printStackTrace();
			return false;
		}
		
		finally {
			try {
				if (c!= null)
				c.close();
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
	public boolean insertList(List<User> listUsers){
		Connection c = null;
		try {

			c=d.getConnection();
			PreparedStatement statement = null;
			
			String insert = "insert into User values ";
			
			int i=1;
			for(User u : listUsers){
				if(listUsers.indexOf(u)!=listUsers.size()-1)
					insert += "('"+u.getId()+"', '"+u.getReviewCount()+"', '"+u.getAverageStars()+"'), ";
				else
					insert += "('"+u.getId()+"', '"+u.getReviewCount()+"', '"+u.getAverageStars()+"');";
				if(i%1000==0)
					System.out.println("preparato lo User numero: " +i+ " !!!!!" );
				i++;
			}
			statement = c.prepareStatement(insert);
			System.out.println("FACCIO L'UPDATE DI USER!!!");
			statement.executeUpdate();
			System.out.println("FATTO!!!");
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
