package it.sii.challenge.valand.persistence.repositoryImpl;

import it.sii.challenge.valand.model.Business;
import it.sii.challenge.valand.model.Review;
import it.sii.challenge.valand.model.User;
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
		Connection c = null;
		try {

			c=d.getConnection();
			PreparedStatement statement = null;
			
			String insert = "insert into Review values ";
			
			int i=1;
			for(Review r : listReview){
				if(listReview.indexOf(r)!=listReview.size()-1)
					insert += "('"+r.getBusinessId()+"', '"+r.getUserId()+"', '"+r.getStars()+"'), ";
				else
					insert += "('"+r.getBusinessId()+"', '"+r.getUserId()+"', '"+r.getStars()+"');";
				if(i%1000==0)
					System.out.println("preparata la Review numero: " +i+ " !!!!!" );
				i++;
			}
			statement = c.prepareStatement(insert);
			System.out.println("FACCIO L'UPDATE DI REVIEW!!!");
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

	public boolean delete(List<Review> reviews) {
		PreparedStatement statement = null; 
		try{
			c=d.getConnection();
			String insert = "delete from Review where business_id in (";
			String insert2 = ") and user_id in (";

			for(Review r : reviews){
				if(reviews.indexOf(r) != reviews.size()-1){
					insert += (r.getBusinessId()+", ");
					insert2 += (r.getUserId()+", ");
				}
				else{
					insert += r.getBusinessId()+")";
					insert2 += r.getUserId()+")";
				}
			}
			statement = null;
			statement = c.prepareStatement(insert+insert2);
			statement.executeUpdate();

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

	public List<User> getNeighborhood(User user, Business business, int treshold){
		Connection c = null;
		try {
			List<User> users = new LinkedList<User>();
			c=d.getConnection();
			PreparedStatement statement = null;
			ResultSet rs;

			//			String check = "select COUNT(*) as reviewInTheMatrix from Review where user_id=?";
			//			statement = c.prepareStatement(check);
			//			statement.setString(1, user.getId());
			//			ResultSet rs=statement.executeQuery();
			//			rs.next();
			//			int reviewInTheMatrix = rs.getInt("reviewInTheMatrix");
			//			
			//			if (reviewInTheMatrix < treshold)
			//				return users;

			String createView1 = "create or replace view businessVotati as"
					+ "			select User.user_id, Review.business_id from User, Review where User.user_id = Review.user_id and"
					+ "			       User.user_id = ?;";

			String createView2 = "create or replace view allTogether as"
					+ "			select Review.business_id, Review.user_id, stars from businessVotati, Review"
					+ "			where businessVotati.business_id = Review.business_id;";

			String createView3 = "create or replace view usersWhoVotedIt as"
					+ "			select Review.user_id from Business, Review"
					+ "			where Review.business_id=Business.business_id"
					+ "			and Business.business_id = ?;";

			String createView4 = "create or replace view total as"
					+ "			select allTogether.user_id, COUNT(allTogether.business_id) as countSameBusiness, avg(stars) as averageStars"
					+ "			from allTogether, usersWhoVotedIt where allTogether.user_id=usersWhoVotedIt.user_id"
					+ "			group by user_id order by countSameBusiness desc;";

			String getNeighborhood = "select User.user_id, User.average_stars, User.review_count, countSameBusiness from total, "
					+ "User where User.user_id = total.user_id LIMIT 0, 10;";

			statement = c.prepareStatement(createView1);
			statement.setString(1, user.getId());
			statement.executeUpdate();		
			statement = c.prepareStatement(createView2);
			statement.executeUpdate();		
			statement = c.prepareStatement(createView3);
			statement.setString(1, business.getId());
			statement.executeUpdate();		
			statement = c.prepareStatement(createView4);
			statement.executeUpdate();		
			statement = c.prepareStatement(getNeighborhood);
			rs = statement.executeQuery();

			while(rs.next()){
				//users.add(new User(rs.getString("user_id"), rs.getInt("review_count"), rs.getDouble("average_stars")));
				users.add(new User(rs.getString("user_id"), rs.getInt("review_count"), rs.getDouble("average_stars"), rs.getInt("countSameBusiness")));
			}
			//System.out.println(users.size());

			return users;

		}  catch (SQLException e) {
			try {
				throw new PersistenceException(e.getMessage());
			} catch (PersistenceException e1) {
				e1.printStackTrace();
			}
			return null;
		} catch (PersistenceException e) {
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
	}

	public List<Business> getNeighborhood(Business business, User user, int treshold){
		
		Connection c = null;
		try {
			List<Business> businessList = new LinkedList<Business>();
			c=d.getConnection();
			PreparedStatement statement = null;
			ResultSet rs;
			
		String createView1 = "create or replace view usersWhoVotedItems as "
				+ "select Business.business_id, Review.user_id from Business, Review where Business.business_id = Review.business_id and "
				+ "Business.business_id = ?;";

		String createView2 = "create or replace view allTogetherB as "
				+ "select Review.business_id, Review.user_id, stars from usersWhoVotedItems, Review "
				+ "where usersWhoVotedItems.user_id = Review.user_id;";

		String createView3 = "create or replace view itemsThatAreVotedByHim as "
				+ "select Review.business_id from Business, Review "
				+ "where Review.business_id=Business.business_id "
				+ "and Review.user_id = ?;";

		String createView4 = "create or replace view totalB as "
				+ "select allTogetherB.business_id, COUNT(allTogetherB.user_id) as countSameUsers, avg(stars) as averageStarsUsers "
				+ "from allTogetherB, itemsThatAreVotedByHim where allTogetherB.business_id = itemsThatAreVotedByHim.business_id "
				+ "group by allTogetherB.business_id order by countSameUsers desc;";
	
		String getNeighborhood = "select Business.business_id, Business.stars, Business.review_count, countSameUsers from totalB, Business where Business.business_id = totalB.business_id LIMIT 0, 100;";
		
		
		statement = c.prepareStatement(createView1);
		statement.setString(1, business.getId());
		statement.executeUpdate();		
		statement = c.prepareStatement(createView2);
		statement.executeUpdate();		
		statement = c.prepareStatement(createView3);
		statement.setString(1, user.getId());
		statement.executeUpdate();		
		statement = c.prepareStatement(createView4);
		statement.executeUpdate();		
		statement = c.prepareStatement(getNeighborhood);
		rs = statement.executeQuery();

		while(rs.next()){
			businessList.add(new Business(rs.getString("business_id"), "", rs.getDouble("stars"), rs.getInt("review_count"), rs.getInt("countSameUsers")));
		}
		//System.out.println(businessList.size());

		return businessList;

	}  catch (SQLException e) {
		try {
			throw new PersistenceException(e.getMessage());
		} catch (PersistenceException e1) {
			e1.printStackTrace();
		}
		return null;
	} catch (PersistenceException e) {
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

}



public boolean isValoriUguali(User user){
	Connection c = null;
	try {

		c=d.getConnection();
		PreparedStatement statement = null;

		String getNeighborhood = "select stars, COUNT(stars) as starCount from Review where user_id=? group by stars;";

		statement = c.prepareStatement(getNeighborhood);
		statement.setString(1, user.getId());
		ResultSet rs=statement.executeQuery();
		rs.next();
		if (rs.next())
			return false;
		return true;

	}  catch (SQLException e) {
		try {
			throw new PersistenceException(e.getMessage());
		} catch (PersistenceException e1) {
			e1.printStackTrace();
		}
		return true;
	} catch (PersistenceException e) {
		e.printStackTrace();
		return true;
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

}
