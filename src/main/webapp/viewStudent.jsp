<%@ page import="java.util.*, model.Classes, model.User, dao.StudentDAO, model.Course, model.Score" %>
<%
  String email = (String) session.getAttribute("email");
  if (email == null) {
    session.setAttribute("status", "You must be logged in to access this page");
    response.sendRedirect("login.jsp?status=failed");
    return;
  }

  User student = (User) request.getAttribute("student");
  Classes classOfStudent = (Classes) request.getAttribute("classOfStudent");
  List<Course> courseList = (List<Course>) request.getAttribute("courseList");
  List<Score> scoreList = (List<Score>) request.getAttribute("scoreList");
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Student Info</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
  
  <style>
    body {
      background: #f7f9fc;
    }
    .navbar-custom {
      background: linear-gradient(90deg, #4e54c8, #8f94fb);
    }
    .navbar-custom .navbar-brand,
    .navbar-custom .nav-link,
    .navbar-custom .navbar-text {
      color: white;
    }
    .welcome-box {
      margin-top: 60px;
      padding: 40px;
      background-color: white;
      box-shadow: 0 0 15px rgba(0,0,0,0.1);
      border-radius: 15px;
      text-align: center;
    }
    .welcome-box h1 {
      color: #4e54c8;
      font-weight: bold;
    }
  </style>
</head>
<body>

  <!-- NAVBAR -->
  <nav class="navbar navbar-expand-lg navbar-custom shadow-sm">
    <div class="container">
      <a class="navbar-brand" href="<%= request.getContextPath() %>/">Student Management</a>
      <div class="collapse navbar-collapse justify-content-end">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <a class="nav-link" href="<%= request.getContextPath() %>/Student" aria-expanded="false">Student</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="<%= request.getContextPath() %>/Classes" aria-expanded="false">Class</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="<%= request.getContextPath() %>/Course" aria-expanded="false">Course</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="<%= request.getContextPath() %>/Schedule" aria-expanded="false">Schedule</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="<%= request.getContextPath() %>/Score/Input" aria-expanded="false">Score</a>
          </li>
        </ul>
        <ul class="navbar-nav mb-2 mb-lg-0">
          <li class="nav-item me-3">
            <span class="navbar-text">
              <i class="bi bi-person-circle me-1"></i> Welcome, <%= session.getAttribute("full_name") != null ? session.getAttribute("full_name") : "User" %>
            </span>
          </li>
          <li class="nav-item">
            <a class="btn btn-light btn-sm" href="<%= request.getContextPath() %>/Logout"><i class="bi bi-box-arrow-right"></i> Logout</a>
          </li>
        </ul>
      </div>
    </div>
  </nav>

  <!-- CONTENT -->
  <!-- CONTENT -->
	<div class="container mt-4">
	  <div class="card shadow-sm">
	    <div class="card-header bg-primary text-white">
	      <h4 class="mb-0"><i class="bi bi-person-lines-fill me-2"></i>Student Information</h4>
	    </div>
	    <div class="card-body">
	      <div class="row mb-3">
	        <div class="col-md-4">
	          <strong><i class="bi bi-hash me-2"></i>Student ID:</strong>
	          <p class="mb-0"><%= student.getStudentID() %></p>
	        </div>
	        <div class="col-md-4">
	          <strong><i class="bi bi-person-fill me-2"></i>Name:</strong>
	          <p class="mb-0"><%= student.getFullName() %></p>
	        </div>
	        <div class="col-md-4">
	          <strong><i class="bi bi-envelope-fill me-2"></i>Email:</strong>
	          <p class="mb-0"><%= student.getEmail() %></p>
	        </div>
	      </div>
	
	      <hr>
	
	      <h5 class="mt-4"><i class="bi bi-building me-2"></i>Class</h5>
	      <% if (classOfStudent != null) { %>
	        <p><strong>Class Name:</strong> <%= classOfStudent.getClassName() %></p>
	      <% } else { %>
	        <p class="text-muted">No class information found.</p>
	      <% } %>
	
	      <hr>
	
	      <h5 class="mt-4"><i class="bi bi-journal-text me-2"></i>Courses</h5>
	      <% if (courseList != null && !courseList.isEmpty()) { %>
	        <div class="table-responsive">
	          <table class="table table-striped table-bordered">
	            <thead class="table-light">
	              <tr>
	                <th>Course Name</th>
	                <th>Score</th>
	              </tr>
	            </thead>
	            <tbody>
	              <%
	                for (Course course : courseList) {
	                  String scoreValue = "N/A";
	                  for (Score score : scoreList) {
	                    if (score.getCourseId() == course.getID()) {
	                      scoreValue = String.valueOf(score.getScore());
	                      break;
	                    }
	                  }
	              %>
	                <tr>
	                  <td><%= course.getCourseName() %></td>
	                  <td><%= scoreValue %></td>
	                </tr>
	              <% } %>
	            </tbody>
	          </table>
	        </div>
	      <% } else { %>
	        <p class="text-muted">No course information available.</p>
	      <% } %>
	    </div>
	  </div>
	</div>

</body>
</html>
