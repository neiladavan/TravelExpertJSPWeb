async function getPackages() {
    try {
        const packageSelect = $("#fldPackageSelect");
        const response = await fetch('http://localhost:8080/TravelExpertsREST_war_exploded/api/packages');
        const packages = await response.json();
        console.log(packages);

        packageSelect.empty();
        packageSelect.append("<option value=''>Select package to view details</option>");

        packages.forEach(pkg => {
            packageSelect.append("<option value='"+pkg.id+"'>" + pkg.pkgName + "</option>");
        });

        const selectedPackageId = $('#fldPackageId').val();
        if (selectedPackageId) {
            packageSelect.val(selectedPackageId);
        }

        return packages;
    } catch (error) {
        console.error('Error:', error);
    }
}

async function getPackage(id) {
    try {
        const response = await fetch('http://localhost:8080/TravelExpertsREST_war_exploded/api/packages/' + id);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const packageData = await response.json();
        console.log(packageData);

        $('#fldPackageId').val(packageData.id);
        $('#fldPkgName').val(packageData.pkgName);
        $('#fldPkgStartDate').val(dayjs(packageData.pkgStartDate, "MMM D, YYYY").format("YYYY-MM-DD"));
        $('#fldPkgEndDate').val(dayjs(packageData.pkgEndDate).format("YYYY-MM-DD"));
        $('#fldPkgDesc').val(packageData.pkgDesc);
        $('#fldPkgBasePrice').val(packageData.pkgBasePrice);
        $('#fldPkgAgencyCommission').val(packageData.pkgAgencyCommission);

        return packageData;
    } catch (error) {
        console.error('Error:', error);
    }
}

async function addOrUpdate() {
    const fldPackageId = $('#fldPackageId');
    const id = fldPackageId.val();
    const pkgStartDate = $('#fldPkgStartDate').val();
    const pkgEndDate = $('#fldPkgEndDate').val();

    // Parse dates for comparison
    const startDate = new Date(pkgStartDate);
    const endDate = new Date(pkgEndDate);

    // Validate that endDate is greater than startDate
    if (endDate <= startDate) {
        alert("End Date should be greater than Start Date.");
        return; // Stop execution if the dates are not valid
    }

    const packageData = {
        pkgName: $('#fldPkgName').val(),
        pkgStartDate,
        pkgEndDate,
        pkgDesc: $('#fldPkgDesc').val(),
        pkgBasePrice: $('#fldPkgBasePrice').val(),
        pkgAgencyCommission: $('#fldPkgAgencyCommission').val()
    };

    try {
        if (id) {
            packageData.id = id;
            const response = await fetch(`http://localhost:8080/TravelExpertsREST_war_exploded/api/packages/`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(packageData)
            });

            if (!response.ok) throw new Error(`Failed to update package: ${response.statusText}`);
            await getPackages();
            alert("Package updated successfully!");
        } else {
            const response = await fetch(`http://localhost:8080/TravelExpertsREST_war_exploded/api/packages/`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(packageData)
            });

            if (!response.ok) throw new Error(`Failed to add package: ` + response.statusText);
            const data = await response.json();

            fldPackageId.val(data.id);

            await getPackages();  // Update package list after addition
            alert("Package added successfully!");
        }
    } catch (error) {
        alert(error.message);
    }
}

function resetForm() {
    $('#fldPackageId').val('');
    $('#fldPkgName').val('');
    $('#fldPkgStartDate').val('');
    $('#fldPkgEndDate').val('');
    $('#fldPkgDesc').val('');
    $('#fldPkgBasePrice').val('');
    $('#fldPkgAgencyCommission').val('');
}

$(document).ready(function() {
    const fldPackageId = $('#fldPackageId');
    getPackages();

    $('#fldPackageSelect').on('change', async function() {
        const selectedId = $(this).val();
        if (selectedId) {
            await getPackage(selectedId);
        } else {
            resetForm();
        }
    });

    $('#addOrUpdatePackageButton').on('click', async function() {
        await addOrUpdate();
    });

    $('#deletePackageButton').on('click', async function() {
        const id = fldPackageId.val();  // Assuming the package ID is stored here
        console.log(`http://localhost:8080/TravelExpertsREST_war_exploded/api/packages/` + id);
        if (!id) {
            alert('Please select a package to delete.');
            return;
        }

        if (!confirm('Are you sure you want to delete this package?')) {
            return; // User canceled the deletion
        }

        try {
            // DELETE request to remove package
            const response = await fetch(`http://localhost:8080/TravelExpertsREST_war_exploded/api/packages/` + id, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) throw new Error(`Failed to delete package: ` + response.statusText);

            alert("Package deleted successfully!");

            // After successful deletion, clear the form and refresh the package list
            resetForm();

            await getPackages();  // Refresh the package list

        } catch (error) {
            alert(error.message);
            // Handle error (e.g., log error, display error message on UI)
        }
    });

});