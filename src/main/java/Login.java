

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
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

      String email = request.getParameter("email");
      String password = request.getParameter("password");
      
      RequestDispatcher dispatcher = null;
      Connection con = null;
      
      HttpSession session = request.getSession();
      
      try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_student_management", "dbadmin", "Abc@13579");
        
        PreparedStatement pst = con.prepareStatement("select * from user where email=? and password=? ");
        
        
        pst.setString(1,email);
        pst.setString(2,password);
        
        ResultSet rs = pst.executeQuery();
        
        if(rs.next()) {
          session.setAttribute("email", rs.getString("email"));
          session.setAttribute("full_name", rs.getString("full_name"));
//          dispatcher = request.getRequestDispatcher("dashboard.jsp");
          response.sendRedirect("Dashboard");
        } else {
          request.setAttribute("status","failed");
          dispatcher = request.getRequestDispatcher("login.jsp");
          dispatcher.forward(request, response);
        }
        
//        dispatcher.forward(request,response);
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
