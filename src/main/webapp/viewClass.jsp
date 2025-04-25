<%@ page import="java.util.*, model.Classes, model.User, dao.StudentDAO" %>
<%
  String email = (String) session.getAttribute("email");
  if (email == null) {
    session.setAttribute("status", "You must be logged in to access this page");
    response.sendRedirect("login.jsp?status=failed");
    return;
  }

  // List<Classes> classList = ClassDAO.getAllClasss();
  List<User> allStudentList = StudentDAO.getAllStudents();
  Classes cls = (Classes) request.getAttribute("class");
  List<User> studentInClassList = StudentDAO.getStudentsByClassId(cls.getID());
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Class Info</title>
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
          <a class="nav-link" href="<%= request.getContextPath() %>/Student" aria-expanded="false">
            Student
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="<%= request.getContextPath() %>/Classes" aria-expanded="false">
            Class
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="<%= request.getContextPath() %>/Course" aria-expanded="false">
            Course
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="<%= request.getContextPath() %>/Schedule" aria-expanded="false">
          	Schedule
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="<%= request.getContextPath() %>/Score/Input" aria-expanded="false">
          	Score
          </a>
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
  <div class="container">
    <div class="d-flex justify-content-between align-items-center mb-4">
	  <h2 class="mb-0">Student in this class:</h2>
	  <div>
	  	<button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addStudentModal">
		    <i class="bi bi-person-plus"></i> Add students
		  </button>
	  	<button id="deleteSelectedBtn" class="btn btn-danger me-2" disabled onclick="deleteSelectedStudents()">
	      <i class="bi bi-trash"></i> Delete Student
	    </button>
	  </div>
	</div>

	<% if (studentInClassList != null && !studentInClassList.isEmpty()) { %>
		<form id="deleteForm" action="RemoveStudentFromClass" method="post">
			<input type="hidden" name="classId" value="<%= cls.getID() %>">
		    <table class="table table-bordered">
			  <thead>
			    <tr>
		    	  <th><input type="checkbox" id="selectAllCheckbox"></th>
			      <th>NO</th>
			      <th>Student ID</th>
			      <th>Full Name</th>
			      <th>Email</th>
			    </tr>
			  </thead>
			  <tbody>
			  	<%
			      int index = 1;
			      for (User u : studentInClassList) {
			    %>
			      <tr>
			        <td><input type="checkbox" class="studentCheckbox" name="studentIds" value="<%= u.getID() %>"></td>
			        <td><%= index++ %></td>
			        <td><%= u.getStudentID() %></td>
			        <td><%= u.getFullName() %></td>
			        <td><%= u.getEmail() %></td>
			      </tr>
			    <% } %>
			  </tbody>
			</table>
		</form>
	<% } else { %>
	  <div class="alert alert-warning">No students found.</div>
	<% } %>
  </div>
  
  <!-- ADD STUDENT MODAL -->
	<div class="modal fade" id="addStudentModal" tabindex="-1" aria-labelledby="addStudentModalLabel" aria-hidden="true">
	  <div class="modal-dialog modal-lg">
	    <form action="<%= request.getContextPath() %>/AssignStudentToClass" method="post">
	      <div class="modal-content">
	        <div class="modal-header">
	          <h5 class="modal-title" id="addStudentModalLabel">Add Students to Class</h5>
	          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
	        </div>
	
	        <div class="modal-body">
	          <!-- Filter -->
	          <div class="mb-3">
	            <input type="text" class="form-control" id="filterInput" placeholder="Search by Student ID">
	          </div>
	
	          <!-- Student List Table -->
	          <table class="table table-bordered table-striped">
	            <thead class="table-light">
	              <tr>
	                <th><input type="checkbox" id="selectAllCheckboxAssignStudent"></th>
	                <th>Student ID</th>
	                <th>Full Name</th>
	                <th>Email</th>
	              </tr>
	            </thead>
	            <tbody id="studentTableBody">
	              <% for (User u : allStudentList) { %>
	                <tr>
	                  <td><input type="checkbox" class="student-checkbox" name="selectedStudents" value="<%= u.getID() %>"></td>
	                  <td class="student-id"><%= u.getStudentID() %></td>
	                  <td><%= u.getFullName() %></td>
	                  <td><%= u.getEmail() %></td>
	                </tr>
	              <% } %>
	            </tbody>
	          </table>
	        </div>
	
	        <div class="modal-footer">
	          <input type="hidden" name="classId" value="<%= cls.getID() %>">
	          <button type="submit" class="btn btn-primary">Add Selected</button>
	          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
	        </div>
	      </div>
	    </form>
	  </div>
	</div>
	 
	<script>
	  // Filter student by ID
	  document.getElementById('filterInput').addEventListener('keyup', function () {
	    const filterValue = this.value.toLowerCase();
	    const rows = document.querySelectorAll('#studentTableBody tr');
	    rows.forEach(row => {
	      const studentID = row.querySelector('.student-id').textContent.toLowerCase();
	      row.style.display = studentID.includes(filterValue) ? '' : 'none';
	    });
	  });
	
	  const selectAllCheckboxInModal = document.getElementById('selectAllCheckboxAssignStudent');
	  selectAllCheckboxInModal.addEventListener('change', function () {
	    const isChecked = this.checked;
	    const rows = document.querySelectorAll('#studentTableBody tr');
	    rows.forEach(row => {
	      if (row.style.display !== 'none') {
	        const checkbox = row.querySelector('input.student-checkbox');
	        checkbox.checked = isChecked;
	      }
	    });
	  });

	  const studentCheckboxesInModal = document.querySelectorAll('#studentTableBody input.student-checkbox');
	  studentCheckboxesInModal.forEach(cb => {
	    cb.addEventListener('change', function() {
	      const visibleCheckboxes = Array.from(document.querySelectorAll('#studentTableBody tr'))
	        .filter(row => row.style.display !== 'none')
	        .map(row => row.querySelector('input.student-checkbox'));
	      
	      const allChecked = visibleCheckboxes.every(cb => cb.checked);
	      selectAllCheckboxInModal.checked = allChecked;
	    });
	  });
	</script>
	
	<script>
	  const addModal = document.getElementById('addStudentModal');
	  addModal.addEventListener('show.bs.modal', function (event) {
	    const button = event.relatedTarget;
	    const classId = button.getAttribute('data-class-id');
	    const inputHidden = assignModal.querySelector('input[name="classId"]');
	    inputHidden.value = classId;
	  });
	</script>
	
	<script>
	  const selectAllCheckbox = document.getElementById('selectAllCheckbox');
	  const studentCheckboxes = document.getElementsByClassName('studentCheckbox');
	  const deleteSelectedBtn = document.getElementById('deleteSelectedBtn');
	
	  // Get "Select All" event
	  selectAllCheckbox.addEventListener('change', function() {
	    for (let checkbox of studentCheckboxes) {
	      checkbox.checked = selectAllCheckbox.checked;
	    }
	    toggleDeleteButton();
	  });
	
	  // Get each checkbox event
	  for (let checkbox of studentCheckboxes) {
		  checkbox.addEventListener('change', function() {
		    if (!this.checked) {
		      selectAllCheckbox.checked = false;
		    } else {
		      const allChecked = Array.from(studentCheckboxes).every(cb => cb.checked);
		      selectAllCheckbox.checked = allChecked;
		    }
		    toggleDeleteButton();
		  });
		}

	
	  function toggleDeleteButton() {
	    const anyChecked = Array.from(studentCheckboxes).some(cb => cb.checked);
	    deleteSelectedBtn.disabled = !anyChecked;
	  }
	
	  // Delete Selected
	  function deleteSelectedStudents() {
	    if (confirm('Are you sure you want to delete the selected students?')) {
	      document.getElementById('deleteForm').submit();
	    }
	  }
	</script>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
