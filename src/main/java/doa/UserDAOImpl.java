package doa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import models.Admin;
import models.Employee;
import models.User;
import util.ConnectionUtil;
import doa.AccountDAO;
import doa.AccountDAOImpl;




public class UserDAOImpl implements UserDAO {

	private static final AccountDAO accountDAO = new AccountDAOImpl();

	// ==================================================
	// ///////////////// REGISTER USER /////////////////
	// ==================================================

	@Override
	public boolean insert(User user) {

		String role = user.getRole();
		switch (role) {
		case "standard":
			try(Connection conn = ConnectionUtil.getConnection()){
				int index=0;
				String sql = "INSERT INTO customers (first_name, last_name, user_name, user_password, email, phone) " + "VALUES(?,?,?,?,?,?)";
				System.out.println(sql);
				PreparedStatement statement = conn.prepareStatement(sql);
				statement.setString(++index, user.getFirstName());
				statement.setString(++index, user.getLastName());
				statement.setString(++index, user.getUserName());
				statement.setString(++index, user.getPassword());
				statement.setString(++index, user.getEmail());
				statement.setString(++index, user.getPhoneNumber());
				System.out.println("Adding user to database");

				statement.execute();
				return true;


			}catch (SQLException e) {
				System.out.println(e);
			}
			return false;


		default:
			try(Connection conn = ConnectionUtil.getConnection()){
				int index=0;
				String sql = "INSERT INTO employees (first_name, last_name, role, user_name, user_password, email, phone) " + "VALUES(?,?,?,?,?,?,?)";
				System.out.println(sql);
				PreparedStatement statement = conn.prepareStatement(sql);
				statement.setString(++index, user.getFirstName());
				statement.setString(++index, user.getLastName());
				statement.setString(++index, user.getRole());
				statement.setString(++index, user.getUserName());
				statement.setString(++index, user.getPassword());
				statement.setString(++index, user.getEmail());
				statement.setString(++index, user.getPhoneNumber());
				System.out.println("Adding employee to database");

				statement.execute();
				return true;


			}catch (SQLException e) {
				System.out.println(e);
			}
			return false;

		}


	}



	// =======================================================
	// ///////////////// FIND ALL CUSTOMERS /////////////////
	// =======================================================

	@Override
	public Set<User> findAllCustomers() {
		System.out.println("Finding all users");
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM customers ORDER BY id DESC;";

			Statement statement = conn.createStatement();

			Set<User> customerSet = new LinkedHashSet<>();

			ResultSet result = statement.executeQuery(sql);




			while(result.next()) {
				User user = new User();

				user.setId(result.getInt("id"));
				user.setFirstName(result.getString("first_name"));
				user.setLastName(result.getString("last_name"));
				user.setUserName(result.getString("user_name"));
				user.setPassword(result.getString("user_password"));
				user.setEmail(result.getString("email"));
				user.setPhoneNumber(result.getString("phone"));
				user.setAccounts(accountDAO.getAccountsByUserID(result.getInt("id")));
				customerSet.add(user);


			}



			return customerSet;

		}catch(SQLException e) {
			System.out.println(e);
		}
		return null;
	}






	// =======================================================
	// ///////////////// FIND ALL EMPLOYEES /////////////////
	// =======================================================

	@Override
	public Set<User> findAllEmployees() {
		System.out.println("Finding all employees");
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM employees ORDER BY id DESC;";

			Statement statement = conn.createStatement();

			Set<User> set = new LinkedHashSet<>();

			ResultSet result = statement.executeQuery(sql);
			System.out.println(result);

			while(result.next()) {
				set.add(new User(
						result.getInt("id"),
						result.getString("first_name"),
						result.getString("last_name"),
						result.getString("role"),
						result.getString("user_name"),
						result.getString("user_password"),
						result.getString("email"),
						result.getString("phone"))
				);
			}

			return set;

		}catch(SQLException e) {
			System.out.println(e);
		}
		return null;
	}






	// ===================================================
	// ///////////////// FIND ALL ADMIN /////////////////
	// ===================================================

	@Override
	public Set<Admin> findAllAdmins() {

		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM employees WHERE role = 'admin' ORDER BY id DESC;";

			Statement statement = conn.createStatement();

			Set<Admin> set = new LinkedHashSet<>();

			ResultSet result = statement.executeQuery(sql);

			while(result.next()) {
				set.add(new Admin(
						result.getInt("id"),
						result.getString("first_name"),
						result.getString("last_name"),
						result.getString("user_name"),
						result.getString("user_password"),
						result.getString("email"),
						result.getString("phone"))
				);
			}

			return set;

		}catch(SQLException e) {
			System.out.println(e);
		}
		return null;
	}





	// ========================================================
	// ///////////////// FIND EMPLOYEE BY ID /////////////////
	// ========================================================

	@Override
	public Set<Employee> findEmployeeByID(int id) {
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM employees WHERE id = " + id + " ;";

			Statement statement = conn.createStatement();

			Set<Employee> set = new HashSet<>();

			ResultSet result = statement.executeQuery(sql);

			while(result.next()) {
				set.add(new Employee(result.getInt("id"), result.getString("first_name"),
						result.getString("last_name"), result.getString("user_name"),
						result.getString("user_password"), result.getString("email"),
						result.getString("phone"))
				);
			}

			return set;

		}catch(SQLException e) {
			System.out.println(e);
		}
		return null;
	}






	// ========================================================
	// ///////////////// FIND CUSTOMER BY ID /////////////////
	// ========================================================

	@Override
	public User findCustomerByID(int id) {
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM customers WHERE id = " + id + " ;";

			Statement statement = conn.createStatement();

			ResultSet result = statement.executeQuery(sql);

			if (result.next()) {
				User user = new User();
				user.setId(result.getInt("id"));
				user.setFirstName(result.getString("first_name"));
				user.setLastName(result.getString("last_name"));
				user.setUserName(result.getString("user_name"));
				user.setPassword(result.getString("user_password"));
				user.setEmail(result.getString("email"));
				user.setPhoneNumber(result.getString("phone"));
				user.setAccounts(accountDAO.getAccountsByUserID(result.getInt("id")));

				return user;
			}

		}catch(SQLException e) {
			System.out.println(e);
		}
		return null;
	}







	// ===========================================================
	// ///////////////// FIND CUSTOMER BY EMAIL /////////////////
	// ===========================================================

	@Override
	public User findCustomerByEmail(String email) {
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM customers WHERE email = '" + email + "' ;";

			Statement statement = conn.createStatement();

			ResultSet result = statement.executeQuery(sql);

			if (result.next()) {
				User user = new User();
				user.setId(result.getInt("id"));
				user.setFirstName(result.getString("first_name"));
				user.setLastName(result.getString("last_name"));
				user.setUserName(result.getString("user_name"));
				user.setPassword(result.getString("user_password"));
				user.setEmail(result.getString("email"));
				user.setPhoneNumber(result.getString("phone"));
				user.setAccounts(accountDAO.getAccountsByUserID(result.getInt("id")));

				return user;
			}

		}catch(SQLException e) {
			System.out.println(e);
		}
		return null;
	}










	// ==========================================================
	// ///////////////// FIND CUSTOMER BY NAME /////////////////
	// ==========================================================

	@Override
	public Set<User> findCustomerByName(String firstName, String lastName) {
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM customers WHERE first_name = " + firstName + " AND last_name = " + lastName + " ORDER BY id DESC ;";

			Statement statement = conn.createStatement();

			Set<User> set = new LinkedHashSet<>();

			ResultSet result = statement.executeQuery(sql);

			while(result.next()) {
				set.add(new User(result.getInt("id"), result.getString("first_name"),
						result.getString("last_name"), result.getString("user_name"),
						result.getString("user_password"), result.getString("email"),
						result.getString("phone"))
				);
			}

			return set;

		}catch(SQLException e) {
			System.out.println(e);
		}
		return null;
	}






	// ==========================================================
	// ///////////////// UPDATE CUSTOMER ///////////////////////
	// ==========================================================

	@Override
	public boolean update(User user) {
		try(Connection conn = ConnectionUtil.getConnection()){

			String sql = "UPDATE customers SET  first_name=?, last_name=?, user_password=?, email=?, phone=? WHERE id = ?;";

			PreparedStatement statement = conn.prepareStatement(sql);
			System.out.println(statement);
			int index = 0;

			statement.setString(++index, user.getFirstName());
			statement.setString(++index, user.getLastName());
			statement.setString(++index, user.getPassword());
			statement.setString(++index, user.getEmail());
			statement.setString(++index, user.getPhoneNumber());
			statement.setInt(++index, (int) user.getId());

			statement.execute();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}



	@Override
	public User login(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

}
