<%@ include file="header.jsp" %>
<script src="register.js"></script>
<a href="index.jsp" class="btn btn-outline-secondary">Home</a>
<div class="row justify-content-center">
  <div class="col-md-6">
    <div class="card shadow-sm">
      <div class="card-header text-center">
        <h4>Register</h4>
      </div>
      <div class="card-body">
        <form id="registerForm">
          <div class="mb-3">
            <label for="name" class="form-label">Full Name</label>
            <input type="text" class="form-control" id="name" name="name" placeholder="Enter your full name" required>
          </div>

          <div class="mb-3">
            <label for="email" class="form-label">Email Address</label>
            <input type="email" class="form-control" id="email" name="email" placeholder="Enter your email" required>
          </div>

          <div class="mb-3">
            <label for="password" class="form-label">Password</label>
            <input type="password" class="form-control" id="password" name="password" placeholder="Create a password" required>
          </div>

          <div class="mb-3">
            <label for="country" class="form-label">Country</label>
            <select class="form-select" id="country" name="country" required>
              <option value="">Select Country</option>
              <option value="CA">Canada</option>
              <option value="US">United States</option>
            </select>
          </div>

          <div class="mb-3">
            <label for="provinceState" class="form-label">State/Province</label>
            <select class="form-select" id="provinceState" name="provinceState" required>
              <option value="">Select State/Province</option>
              <!-- Options populated dynamically based on selected country -->
            </select>
          </div>

          <button type="submit" class="btn btn-primary w-100">Register</button>
        </form>
      </div>
    </div>
  </div>
</div>