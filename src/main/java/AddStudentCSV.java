

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
import java.sql.ResultSet;

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
      PreparedStatement insertPst = null;
      PreparedStatement checkPst = null;
      String insertSQL = "INSERT INTO students (student_id, full_name, email) VALUES (?, ?, ?)";
      String checkSQL = "SELECT * FROM students WHERE student_id = ? OR email = ?";

      try {
          Class.forName("com.mysql.cj.jdbc.Driver");
          con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_student_management", "dbadmin", "Abc@13579");

          insertPst = con.prepareStatement(insertSQL);
          checkPst = con.prepareStatement(checkSQL);

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

                  // Check whether student_id or email already exists
                  checkPst.setString(1, studentID);
                  checkPst.setString(2, email);
                  ResultSet rs = checkPst.executeQuery();

                  if (!rs.next()) { 
                    // If not duplicate, insert this student
                      insertPst.setString(1, studentID);
                      insertPst.setString(2, fullName);
                      insertPst.setString(3, email);
                      insertPst.executeUpdate();
                  }

                  rs.close();
              }
          }

          reader.close();
          insertPst.close();
          checkPst.close();
          con.close();

      } catch (Exception e) {
          e.printStackTrace();
      }

      response.sendRedirect("Student");
	}

}
