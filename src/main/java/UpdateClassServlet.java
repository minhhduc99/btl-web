

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Servlet implementation class UpdateClassServlet
 */
@WebServlet("/UpdateClass")
public class UpdateClassServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateClassServlet() {
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
	    int id = Integer.parseInt(request.getParameter("id"));
	    String className = request.getParameter("class_name");
	    String classPeriod = request.getParameter("class_period");
	    String teacherName = request.getParameter("teacher_name");

	    try {
	      Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_student_management", "dbadmin", "Abc@13579");
	      PreparedStatement ps = con.prepareStatement("UPDATE classes SET class_name=?, class_period=?, teacher_name=? WHERE id=?");
	      ps.setString(1, className);
	      ps.setString(2, classPeriod);
	      ps.setString(3, teacherName);
	      ps.setInt(4, id);
	      ps.executeUpdate();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    response.sendRedirect("Classes");
	}

}
