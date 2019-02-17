<html>



	<head>
	


		<!-- Web page title -->
    	<title>Top Trumps</title>
    	
    	<!-- Import JQuery, as it provides functions you will probably find useful (see https://jquery.com/) -->
    	<script src="https://code.jquery.com/jquery-2.1.1.js"></script>
    	<script src="https://code.jquery.com/ui/1.11.1/jquery-ui.js"></script>
    	<link rel="stylesheet" href="https://code.jquery.com/ui/1.11.1/themes/flick/jquery-ui.css">

		<!-- Optional Styling of the Website, for the demo I used Bootstrap (see https://getbootstrap.com/docs/4.0/getting-started/introduction/) -->
		<link rel="stylesheet" href="http://dcs.gla.ac.uk/~richardm/TREC_IS/bootstrap.min.css">
    	<script src="http://dcs.gla.ac.uk/~richardm/vex.combined.min.js"></script>
    	<script>vex.defaultOptions.className = 'vex-theme-os';</script>
    	<link rel="stylesheet" href="http://dcs.gla.ac.uk/~richardm/assets/stylesheets/vex.css"/>
    	<link rel="stylesheet" href="http://dcs.gla.ac.uk/~richardm/assets/stylesheets/vex-theme-os.css"/>
    	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">

	</head>

    <body onload="initalize()"> <!-- Call the initalize method when the page loads -->
    	
    	<div class="container">

			<!-- Add your HTML Here -->
		
<div class="Headline">
<div class="p-3 mb-2 bg-info text-white">
<div style="text-align: center;">Top Trumps Game</div>
</div>		

<div class="GameInstructions">
<div class="p-3 mb-2 bg-secondary text-white">
<div style="text-align: center;">Welcome to the game</div>
</div>	


<div class="row mb-2">
	
       <div class="col-lg-4 col-m-t-3">

    <!-- Top Left Panel -->
    <div class="card">
   Top Left
	</div>
	</div>
	
	
          <div class="col-lg-4 col-md-6">

    <!-- Top Center Panel -->
    <div class="card">
     Top Center
    </div>
	</div>
	
	<div class="col-lg-4 col-md-6">

    <!-- Top Right Panel -->
    <div class="card">
      Top Right
	</div>
	</div>

	</div>
	</div>
	
	<!-- End of Row -->
	
	<div class="row mb-2">
	
       <div class="col-lg-4 col-md-6">

    <!--Bottom Left Panel-->
    <div class="card">
      Bottom Left
    </div>
	</div>
	
	
          <div class="col-lg-4 col-md-6">

    <!--Bottom Center Panel-->
    <div class="card">
     Bottom Center
    </div>
	</div>
	
	<div class="col-lg-4 col-md-6">

    <!--Bottom Right Panel-->
    <div class="card">
      Bottom Right
    </div>
	</div>
	
    </div>
	</div>
	
	 <!--End of Row-->
		
		</div>
		
		<script type="text/javascript">
		
			// Method that is called on page load
			function initalize() {
			
				// --------------------------------------------------------------------------
				// You can call other methods you want to run when the page first loads here
				// --------------------------------------------------------------------------
				
				// For example, lets call our sample methods
				//helloJSONList();
				//helloWord("Student");
				deckFile();
				numAIPlayers();
				
				
			}
			
			// -----------------------------------------
			// Add your other Javascript methods Here
			// -----------------------------------------
		
		
		function deckFile() {
			
			var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/deckfile"); // Request type and URL
			
			if (!xhr) {
  					alert("CORS not supported");
				}
			xhr.onload = function(e) {
 					var responseText = xhr.response; //
					alert(responseText); // 
				};
				xhr.send();		
			}
		
		
		function numAIPlayers() {
			
			var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/aiplayers"); // Request type and URL
			
			if (!xhr) {
  					alert("CORS not supported");
				}
			xhr.onload = function(e) {
 					var responseText = xhr.response; //
					alert(responseText); // 
				};
				xhr.send();		
			}
			
			
		
		
			// This is a reusable method for creating a CORS request. Do not edit this.
			function createCORSRequest(method, url) {
  				var xhr = new XMLHttpRequest();
  				if ("withCredentials" in xhr) {

    				// Check if the XMLHttpRequest object has a "withCredentials" property.
    				// "withCredentials" only exists on XMLHTTPRequest2 objects.
    				xhr.open(method, url, true);

  				} else if (typeof XDomainRequest != "undefined") {

    				// Otherwise, check if XDomainRequest.
    				// XDomainRequest only exists in IE, and is IE's way of making CORS requests.
    				xhr = new XDomainRequest();
    				xhr.open(method, url);

 				 } else {

    				// Otherwise, CORS is not supported by the browser.
    				xhr = null;

  				 }
  				 return xhr;
			}
		
		</script>
		
		<!-- Here are examples of how to call REST API Methods -->
		<script type="text/javascript">
		
			// This calls the helloJSONList REST method from TopTrumpsRESTAPI
			function helloJSONList() {
			
				// First create a CORS request, this is the message we are going to send (a get request in this case)
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/helloJSONList"); // Request type and URL
				
				// Message is not sent yet, but we can check that the browser supports CORS
				if (!xhr) {
  					alert("CORS not supported");
				}

				// CORS requests are Asynchronous, i.e. we do not wait for a response, instead we define an action
				// to do when the response arrives 
				xhr.onload = function(e) {
 					var responseText = xhr.response; // the text of the response
					alert(responseText); // lets produce an alert
				};
				
				// We have done everything we need to prepare the CORS request, so send it
				xhr.send();		
			}
			
			// This calls the helloJSONList REST method from TopTrumpsRESTAPI
			function helloWord(word) {
			
				// First create a CORS request, this is the message we are going to send (a get request in this case)
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/helloWord?Word="+word); // Request type and URL+parameters
				
				// Message is not sent yet, but we can check that the browser supports CORS
				if (!xhr) {
  					alert("CORS not supported");
				}

				// CORS requests are Asynchronous, i.e. we do not wait for a response, instead we define an action
				// to do when the response arrives 
				xhr.onload = function(e) {
 					var responseText = xhr.response; // the text of the response
					alert(responseText); // lets produce an alert
				};
				
				// We have done everything we need to prepare the CORS request, so send it
				xhr.send();		
			}
		

		</script>
		
		</body>
</html>