<html>

	<head>

	<style type="text/css" media="screen">
	   
	   body{
	  background: url('https://images.unsplash.com/photo-1482489603187-f0ae98f407a3?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1400&q=80') no-repeat center center fixed;
  -webkit-background-size: cover;
  -moz-background-size: cover;
  background-size: cover;
  -o-background-size: cover;
  }

    </style>
	
	 <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	
	  <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css" integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">
	
	<html lang="en"><script type="text/javascript" charset="utf-8" id="zm-extension" src="chrome-extension://fdcgdnkidjaadafnichfpabhfomcebme/scripts/webrtc-patch.js" async=""></script><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="Mark Otto, Jacob Thornton, and Bootstrap contributors">
    <meta name="generator" content="Jekyll v3.8.5">
	
		<!-- Web page title -->
    	<title>Top Trumps</title>
	

	</head>

    <body onload="initalize()"> <!-- Call the initalize method when the page loads -->
    	
    	<div class="container">

			<!-- Add your HTML Here -->
		
		<div class="pricing-header px-3 py-3 pt-md-5 pb-md-4 mx-auto text-center">
  <h3 class="display-4"><font color="black"><b>WarCraft - Top Trumps</b></h3>
  <p class="lead"><h2>Enter the World of Warcraft and Descend Into a World of Myth, Magic, and Legendary Adventure</h2></p>
</div>

<div class="container">
  <div class="card-deck mb-3 text-center">
    <div class="card mb-4 shadow-sm">
      <div class="card-header">
        <h2 class="my-0 font-weight-normal"> Fight </h2>
      </div>
      <div class="card-body">
        <ul class="list-unstyled mt-3 mb-4">
		        <h4><li>Test Your Skills Against</li>
				<li>Azeroth's Mightiest Warriors...</li></h4>
        </ul>
        <a href="toptrumps/game" class="btn btn-lg btn-secondary">Start New Game</a>
      </div>
    </div>
    <div class="card mb-4 shadow-sm">
      <div class="card-header">
        <h2 class="my-0 font-weight-normal">Game Statistics</h2>
      </div>
      <div class="card-body">
        <ul class="list-unstyled mt-3 mb-4">
		<h4><li>Check Your Battlefield</li>
		<li>Performance So Far...</li></h4>
        </ul>
		<a href="toptrumps/stats" class="btn btn-lg btn-secondary">View Statistics</a>
      </div>
    </div>
      </div>
    </div>
  </div>

  <footer class="pt-4 my-md-5 pt-md-5 border-top">
    <div class="row">
      <div class="col-12 col-md">
		
        <small class="d-block mb-3 text-muted"><font color="black"><b>Developed by Alaska Løvald, Ivaylo Dimanov, Denislav Nedelov, Cheng Jiawen & Adam Cunliffe - 2019</b></small>
      </div>
        </ul>
      </div>
      </div>
      </div>
    </div>
  </footer>
  
		</div>
		
		<script type="text/javascript">
		
			// Method that is called on page load
			function initalize() {
			
				// --------------------------------------------------------------------------
				// You can call other methods you want to run when the page first loads here
				// --------------------------------------------------------------------------
				
				// For example, lets call our sample methods
				// helloJSONList();
				// helloWord("Student");
				
				
			}
			
			// -----------------------------------------
			// Add your other Javascript methods Here
			// -----------------------------------------
		
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
		
				 <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="./Statistics_files/jquery-3.2.1.slim.min.js.download" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script>window.jQuery || document.write('<script src="../../assets/js/vendor/jquery-slim.min.js"><\/script>')</script>
    <script src="./Statistics_files/popper.min.js.download"></script>
    <script src="./Statistics_files/bootstrap.min.js.download"></script>
	
		</body>
</html>