

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.DBUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Servlet implementation class AddCourseCSV
 */
@WebServlet("/AddCourseCSV")
@MultipartConfig
public class AddCourseCSV extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddCourseCSV() {
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
	  Connection con = null;
      PreparedStatement pst = null;
      String sql = "INSERT INTO courses (course_code, course_name, description, credits) VALUES (?, ?, ?, ?)";

      try {
          con = DBUtil.getConnection();
          pst = con.prepareStatement(sql);

          Part filePart = request.getPart("csv_file");
          InputStream inputStream = filePart.getInputStream();
          BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
          String line;

          while ((line = reader.readLine()) != null) {
              String[] parts = line.split(",");
              if (parts.length == 4) {
                  String courseCode = parts[0].trim();
                  String courseName = parts[1].trim();
                  String description = parts[2].trim();
                  int credits = Integer.parseInt(parts[3].trim());

                  pst.setString(1, courseCode);
                  pst.setString(2, courseName);
                  pst.setString(3, description);
                  pst.setInt(4, credits);
                  pst.executeUpdate();
              }
          }

          reader.close();
          pst.close();
          con.close();

      } catch (Exception e) {
          e.printStackTrace();
      }

      response.sendRedirect("Course");
	}

}
