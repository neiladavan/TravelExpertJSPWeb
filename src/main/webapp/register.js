$(document).ready(function() {
    $('#country').change(function() {
        const country = $(this).val();

        if (!country) {
            $('#provinceState').html('<option value="">Select a province/state</option>');
            return;
        }

        // AJAX request to get provinces/states based on selected country
        $.ajax({
            url: 'register',
            type: 'GET',
            data: { country: country },
            success: function(response) {
                $('#provinceState').html(response);
            },
            error: function(error) {
                console.log(error);
                alert(error.responseText || "An unexpected error occurred. Please try again.");
            }
        });
    });
});