<%@ page import="java.util.*, model.User" %>
<%
  String email = (String) session.getAttribute("email");
  if (email == null) {
    session.setAttribute("status", "You must be logged in to access this page");
    response.sendRedirect("login.jsp?status=failed");
    return;
  }

  List<User> studentList = (List<User>) request.getAttribute("studentList");
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Student Management</title>
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
	  <h2 class="mb-0">Student List</h2>
	  <div>
	  	<button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addStudentModal">
	      <i class="bi bi-person-plus"></i> Add Student
	    </button>
	    <button id="deleteSelectedBtn" class="btn btn-danger me-2" disabled onclick="deleteSelectedStudents()">
	      <i class="bi bi-trash"></i> Delete Student
	    </button>
	  </div>
	</div>

	<% String errorMessage = request.getParameter("error"); %>
	<% if (errorMessage != null) { 
	  	if (errorMessage.equals("info_already_exists")) {
	%>
	    <div style="color: red;">Student ID or email already exists.</div>
	<% } } %>

	<%
	  List<String> undeletableStudents = (List<String>) session.getAttribute("undeletableStudents");
	  if (undeletableStudents != null && !undeletableStudents.isEmpty()) {
	%>
	  <div class="alert alert-danger alert-dismissible fade show" role="alert">
	    <strong>Warning!</strong> Cannot delete the following students because they belong to a class:
	    <ul>
	      <% for (String studentName : undeletableStudents) { %>
	        <li><%= studentName %></li>
	      <% } %>
	    </ul>
	    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
	  </div>
	<%
	    session.removeAttribute("undeletableStudents");
	  }
	%>
		

    <% if (studentList != null && !studentList.isEmpty()) { %>
	  <form id="deleteForm" action="DeleteStudent" method="post">
	    <table class="table table-bordered table-striped">
	      <thead class="table-dark">
	        <tr>
	          <th><input type="checkbox" id="selectAllCheckbox"></th>
	          <th>NO</th>
	          <th>Student ID</th>
	          <th>Full Name</th>
	          <th>Email</th>
	          <th>Actions</th>
	        </tr>
	      </thead>
	      <tbody>
	        <% int index = 1;
	           for(User u : studentList) { %>
	          <tr>
	            <td><input type="checkbox" class="studentCheckbox" name="studentIds" value="<%= u.getID() %>"></td>
	            <td><%= index++ %></td>
	            <td><%= u.getStudentID() %></td>
	            <td><%= u.getFullName() %></td>
	            <td><%= u.getEmail() %></td>
	            <td>
		          <button type="button" class="btn btn-warning btn-sm" onclick="openEditModal('<%= u.getID() %>', '<%= u.getStudentID() %>', '<%= u.getFullName() %>', '<%= u.getEmail() %>')">
		            <i class="bi bi-pencil"></i>
		          </button>
		        </td>
	          </tr>
	        <% } %>
	      </tbody>
	    </table>
	  </form>
	<% } else { %>
	  <div class="alert alert-warning">No students found.</div>
	<% } %>
  </div>
  
  <!-- Add Student Modal -->
	<div class="modal fade" id="addStudentModal" tabindex="-1" aria-labelledby="addStudentModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <form action="<%= request.getContextPath() %>/AddStudent" method="post">
	      <div class="modal-content">
	        <div class="modal-header">
	          <h5 class="modal-title" id="addStudentModalLabel">Add New Student</h5>
	          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	        </div>
	        <div class="modal-body">
	          <div class="mb-3">
	            <label for="student_id" class="form-label">Student ID</label>
	            <input type="text" class="form-control" id="student_id" name="student_id" required>
	          </div>
	          <div class="mb-3">
	            <label for="full_name" class="form-label">Full Name</label>
	            <input type="text" class="form-control" id="full_name" name="full_name" required>
	          </div>
	          <div class="mb-3">
	            <label for="email" class="form-label">Email</label>
	            <input type="email" class="form-control" id="email" name="email" required>
	          </div>
	        </div>
	        <div class="modal-footer">
	          <button type="button" class="btn btn-primary" onclick="switchToImportModal()">
				<i class="bi bi-upload"></i> Add Excel/CSV
			  </button>
	          <button type="submit" class="btn btn-success">Add Student</button>
	          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
	        </div>
	      </div>
	    </form>
	  </div>
	</div>
	
	<div class="modal fade" id="importCSVModal" tabindex="-1" aria-labelledby="importCSVModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <form action="<%= request.getContextPath() %>/AddStudentCSV" method="post" enctype="multipart/form-data">
	      <div class="modal-content">
	        <div class="modal-header">
	          <h5 class="modal-title" id="importCSVModalLabel">Import Students from CSV</h5>
	          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
	        </div>
	        <div class="modal-body">
	          <div class="mb-3">
	            <label for="csv_file" class="form-label">CSV File</label>
	            <input type="file" class="form-control" name="csv_file" id="csv_file" accept=".csv" required>
	            <small class="form-text text-muted">Format: student_id,full_name,email</small>
	          </div>
	        </div>
	        <div class="modal-footer">
	          <button type="submit" class="btn btn-primary">Import</button>
	          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
	        </div>
	      </div>
	    </form>
	  </div>
	</div>
	
	<!-- Edit Student Modal -->
	<div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <form action="EditStudent" method="post">
	      <div class="modal-content">
	        <div class="modal-header">
	          <h5 class="modal-title" id="editModalLabel">Edit Student</h5>
	          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
	        </div>
	        <div class="modal-body">
	          <input type="hidden" id="edit-id" name="id">
	          <div class="mb-3">
	            <label for="edit-studentID" class="form-label">Student ID</label>
	            <input type="text" class="form-control" id="edit-studentID" name="student_id" required>
	          </div>
	          <div class="mb-3">
	            <label for="edit-fullName" class="form-label">Full Name</label>
	            <input type="text" class="form-control" id="edit-fullName" name="full_name" required>
	          </div>
	          <div class="mb-3">
	            <label for="edit-email" class="form-label">Email</label>
	            <input type="email" class="form-control" id="edit-email" name="email" required>
	          </div>
	        </div>
	        <div class="modal-footer">
	          <button type="submit" class="btn btn-primary">Save changes</button>
	        </div>
	      </div>
	    </form>
	  </div>
	</div>
	
	<script>
	  function switchToImportModal() {
	    const addModalEl = document.getElementById('addStudentModal');
	    const importModalEl = document.getElementById('importCSVModal');
	
	    const addModalInstance = bootstrap.Modal.getInstance(addModalEl) || new bootstrap.Modal(addModalEl);
	    const importModalInstance = new bootstrap.Modal(importModalEl);
	
	    const onHidden = function () {
	      addModalEl.removeEventListener('hidden.bs.modal', onHidden);
	
	      document.body.classList.remove('modal-open');
	      const backdrop = document.querySelector('.modal-backdrop');
	      if (backdrop) backdrop.remove();
	
	      importModalInstance.show();
	    };
	
	    addModalEl.addEventListener('hidden.bs.modal', onHidden);
	
	    addModalInstance.hide();
	  }
	</script>
	
		
	
	<script>
	  function openEditModal(id, studentID, fullName, email) {
	    document.getElementById('edit-id').value = id;
	    document.getElementById('edit-studentID').value = studentID;
	    document.getElementById('edit-fullName').value = fullName;
	    document.getElementById('edit-email').value = email;
	    var editModal = new bootstrap.Modal(document.getElementById('editModal'));
	    editModal.show();
	  }
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
