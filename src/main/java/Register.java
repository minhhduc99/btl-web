

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);
	  
	  String full_name = request.getParameter("full_name");
	  String email = request.getParameter("email");
	  String password = request.getParameter("password");
	  
	  RequestDispatcher dispatcher = null;
	  Connection con = null;
	  
	  try {
	    Class.forName("com.mysql.cj.jdbc.Driver");
	    
	    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_student_management", "dbadmin", "Abc@13579");
	    
	    PreparedStatement pst = con.prepareStatement("insert into user(full_name,email,password) " 
	        + "values(?,?,?)");
	    
	    pst.setString(1,full_name);
	    pst.setString(2,email);
	    pst.setString(3,password);
	    
	    int rowCount = pst.executeUpdate();
	    
	    dispatcher = request.getRequestDispatcher("register.jsp");
	    
	    if(rowCount > 0) {
	      request.setAttribute("status","success");
	      response.sendRedirect("login.jsp");
	    } else {
	      request.setAttribute("status","failed");
	      dispatcher = request.getRequestDispatcher("register.jsp");
	      dispatcher.forward(request, response);
	    }
	    
//	    dispatcher.forward(request,response);
	  }
	  catch(Exception e) {
	    e.printStackTrace();
	  }
	  finally {
	    try {
	      con.close();
	    }
	    catch(SQLException e) {
	      e.printStackTrace();
	    }
	  }
	}

}
