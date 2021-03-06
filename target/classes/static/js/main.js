submitOrderForms = function(){
    document.getElementById("deliveryDetails").submit();
    document.getElementById("products").submit();
    document.getElementById("orderBody").submit();

}

$(document).ready(function(){
  $("#modalSignIn").click(function(){
    $("#modalSignIn").modal({backdrop: "static"});
  });
});

(function() {
	  'use strict';
	  window.addEventListener('load', function() {
	    // Get the forms we want to add validation styles to
	    var forms = document.getElementsByClassName('needs-validation');
	    // Loop over them and prevent submission
	    var validation = Array.prototype.filter.call(forms, function(form) {
	      form.addEventListener('submit', function(event) {
	        if (form.checkValidity() === false) {
	          event.preventDefault();
	          event.stopPropagation();
	        }
	        form.classList.add('was-validated');
	      }, false);
	    });
	  }, false);
	})();

