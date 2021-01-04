$(document).ready(function(){
    $('#bank').on('submit', function(e){
        e.preventDefault();
        let bankName = $("#bankName").val();
        let baseURL = $("#baseURL").val();
        let transactionID = $("#transactionID").val();
        let pan = $("#pan").val();
        let secCode = $("#secCode").val();
        let cardholder = $("#cardholder").val();
        let expiryDate = $("#expiryDate").val();

        let completePaymentURL = baseURL + "/api/complete-payment/" + transactionID + "?bankName=" + bankName +
            "&pan=" + pan + "&secCode=" + secCode + "&cardholder=" + cardholder + "&expiryDate=" + expiryDate;

        $.ajax({
            url: completePaymentURL,
            type : 'GET',
            success: function (response) {
                console.log(response);
            },
            error: function (xhr) {
                alert(xhr.responseText);
            }
        });
    });
});
