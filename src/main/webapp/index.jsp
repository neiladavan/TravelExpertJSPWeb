<%@ include file="header.jsp" %>
<script>
    const mainScript = document.createElement("script");
    mainScript.src = "main.js?v=" + Date.now();
    document.head.appendChild(mainScript);
</script>
    <section class="mb-4">
        <label for="fldPackageSelect" class="form-label">Select Package:</label>
        <select id="fldPackageSelect" class="form-select">
            <option value="">Select package to view details</option>
        </select>
    </section>

    <form id="packageForm" class="border p-4 rounded shadow-sm bg-light">
        <div class="row mb-3">
            <div class="col-md-6">
                <label for="fldPackageId" class="form-label">ID:</label>
                <input id="fldPackageId" type="number" class="form-control" disabled />
            </div>
            <div class="col-md-6">
                <label for="fldPkgName" class="form-label">Name:</label>
                <input id="fldPkgName" type="text" class="form-control" required/>
            </div>
        </div>

        <div class="row mb-3">
            <div class="col-md-6">
                <label for="fldPkgStartDate" class="form-label">Start Date:</label>
                <input id="fldPkgStartDate" type="date" class="form-control" required/>
            </div>
            <div class="col-md-6">
                <label for="fldPkgEndDate" class="form-label">End Date:</label>
                <input id="fldPkgEndDate" type="date" class="form-control" required/>
            </div>
        </div>

        <div class="mb-3">
            <label for="fldPkgDesc" class="form-label">Description:</label>
            <textarea id="fldPkgDesc" class="form-control" rows="3" required></textarea>
        </div>

        <div class="row mb-3">
            <div class="col-md-6">
                <label for="fldPkgBasePrice" class="form-label">Base Price:</label>
                <input id="fldPkgBasePrice" type="number" step="0.01" class="form-control" required/>
            </div>
            <div class="col-md-6">
                <label for="fldPkgAgencyCommission" class="form-label">Commission:</label>
                <input id="fldPkgAgencyCommission" type="number" step="0.01" class="form-control" required/>
            </div>
        </div>

        <div class="d-flex justify-content-end">
            <button type="submit" id="addOrUpdatePackageButton" class="btn btn-primary me-2">Save</button>
            <button type="button" id="deletePackageButton" class="btn btn-danger">Delete Package</button>
        </div>
    </form>

    <%--<section class="gas-mileage-conversion">
        <h2>Gas Mileage Conversion</h2>
        <form action="hello-servlet" method="post">
            <div class="mb-3">
                <label for="litresPer100km" class="form-label">Enter fuel consumption (L/100km):</label>
                <input type="number" class="form-control" id="litresPer100km" name="litresPer100km" required step="any">
            </div>
            <button type="submit" class="btn btn-primary">Convert</button>
        </form>
    </section>

    <c:if test="${not empty litresPer100km}">
        <section class="conversion-results">
            <h2>Conversion Results</h2>
            <table class="table table-bordered">
                <tbody>
                <tr>
                    <th>Fuel Consumption (L/100km)</th>
                    <td>${litresPer100km}</td>
                </tr>
                <tr>
                    <th>Kilometers per Litre</th>
                    <td>${kilometersPerLitre}</td>
                </tr>
                <tr>
                    <th>MPG (Canadian Gallons)</th>
                    <td>${mpgCanadian}</td>
                </tr>
                <tr>
                    <th>MPG (US Gallons)</th>
                    <td>${mpgUS}</td>
                </tr>
                </tbody>
            </table>
        </section>
    </c:if>

    <br>--%>

    <div class="links">
        <c:if test="${isAuthenticated ne true}">
            <a href="login" class="btn btn-outline-secondary">Login</a>
        </c:if>
        <a href="register.jsp" class="btn btn-primary">Register</a>
        <a href="products" class="btn btn-primary">Products</a>
    </div>
</div>
</body>
</html>
