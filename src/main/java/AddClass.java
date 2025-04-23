

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DBUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Servlet implementation class AddClass
 */
@WebServlet("/AddClass")
public class AddClass extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddClass() {
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
	    String className = request.getParameter("class_name");
	    String classPeriod = request.getParameter("class_period");
	    String teacherName = request.getParameter("teacher_name");
	    
	    String sql = "INSERT INTO classes (class_name, class_period, teacher_name) VALUES (?, ?, ?)";
        String checkSQL = "SELECT * FROM classes WHERE class_name=?";
        
        Connection conn = null;
        PreparedStatement pst = null;
        PreparedStatement checkPst = null;
        ResultSet rs = null;

	    try {
	      conn = DBUtil.getConnection();
	      checkPst = conn.prepareStatement(checkSQL);
          checkPst.setString(1, className);
          rs = checkPst.executeQuery();

          if (rs.next()) {
              // If exists, raise error
              response.sendRedirect("Classes?add_error=class_name_already_exists");
          } else {
              // If not exist, add student
              pst = conn.prepareStatement(sql);
              pst.setString(1, className);
              pst.setString(2, classPeriod);
              pst.setString(3, teacherName);
              pst.executeUpdate();
              response.sendRedirect("Classes");
          }
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	      try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
          try { if (checkPst != null) checkPst.close(); } catch (SQLException e) { e.printStackTrace(); }
          try { if (pst != null) pst.close(); } catch (SQLException e) { e.printStackTrace(); }
          try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
	    }
	}

}
