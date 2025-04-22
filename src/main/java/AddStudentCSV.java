

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Servlet implementation class AddStudentCSV
 */
@WebServlet("/AddStudentCSV")
@MultipartConfig
public class AddStudentCSV extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddStudentCSV() {
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
	  Connection con = null;
	  PreparedStatement pst = null;
	  String sql = "INSERT INTO students (student_id, full_name, email) VALUES (?, ?, ?)";

	  try {
	      Class.forName("com.mysql.cj.jdbc.Driver");
	      con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_student_management", "dbadmin", "Abc@13579");
	      pst = con.prepareStatement(sql);

	      Part filePart = request.getPart("csv_file");
	      InputStream inputStream = filePart.getInputStream();
	      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	      String line;

	      while ((line = reader.readLine()) != null) {
	          String[] parts = line.split(",");
	          if (parts.length == 3) {
	              String studentID = parts[0].trim();
	              String fullName = parts[1].trim();
	              String email = parts[2].trim();

	              pst.setString(1, studentID);
	              pst.setString(2, fullName);
	              pst.setString(3, email);
	              pst.executeUpdate();
	          }
	      }

	      reader.close();
	      pst.close();
	      con.close();

	  } catch (Exception e) {
	      e.printStackTrace();
	  }

	  response.sendRedirect("Student");
	}

}
