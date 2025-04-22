<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Register</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    body {
      background: linear-gradient(135deg, #f8f9fa, #e0e0e0);
      min-height: 100vh;
      display: flex;
      justify-content: center;
      align-items: center;
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    }
    .register-form {
      background-color: white;
      padding: 40px;
      border-radius: 16px;
      box-shadow: 0 4px 20px rgba(0,0,0,0.1);
      width: 100%;
      max-width: 500px;
    }
    .form-title {
      text-align: center;
      margin-bottom: 30px;
    }
  </style>
</head>
<body>
  <div class="register-form">
    <h2 class="form-title">Create your account</h2>

    <%
      String status = (String) request.getAttribute("status");
      if ("success".equals(status)) {
    %>
      <div class="alert alert-success text-center">Registration successful! You can now <a href="login.jsp">Login</a>.</div>
    <% } else if ("failed".equals(status)) { %>
      <div class="alert alert-danger text-center">Registration failed! Please try again.</div>
    <% } %>

    <form method="post" action="Register">
      <div class="mb-3">
        <label for="full_name" class="form-label">Full Name</label>
        <input type="text" class="form-control" name="full_name" id="full_name" required>
      </div>
      <div class="mb-3">
        <label for="email" class="form-label">Email address</label>
        <input type="email" class="form-control" name="email" id="email" required>
      </div>
      <div class="mb-4">
        <label for="password" class="form-label">Password</label>
        <input type="password" class="form-control" name="password" id="password" required>
      </div>
      <div class="d-grid">
        <button type="submit" class="btn btn-primary btn-lg">Register</button>
      </div>
      <div class="text-center mt-3">
        Already have an account? <a href="login.jsp">Login here</a>
      </div>
    </form>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
