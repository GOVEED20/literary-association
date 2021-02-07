$(document).ready(function(){
    $('#bank').on('submit', function(e){
        e.preventDefault();
        let bankName = $("#bankName").val();
        let baseURL = $("#baseURL").val();
        let transactionID = $("#transactionID").val();
        let pan = $("#pan12").val() + "-" + $("#pan4").val();
        let secCode = $("#secCode").val();
        let cardholder = $("#cardholder").val();
        let expiryDate = $("#expiryDate").val();

        let completePaymentURL = baseURL + "/api/complete-payment/" + transactionID + "?bankName=" + bankName;

        $.ajax({
            url: completePaymentURL,
            type : 'GET',
            headers: {
                "pan": pan,
                "secCode": secCode,
                "cardholder": cardholder,
                "expiryDate": expiryDate
            },
            success: function (response) {
                toastr["success"](response);
            },
            error: function (response) {
                toastr["error"](response.responseText);
            }
        });
    });

    toastr.options = {
        "closeButton": false,
        "debug": false,
        "newestOnTop": false,
        "progressBar": false,
        "positionClass": "toast-bottom-center",
        "preventDuplicates": false,
        "onclick": null,
        "showDuration": "300",
        "hideDuration": "1000",
        "timeOut": "5000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    }
});
