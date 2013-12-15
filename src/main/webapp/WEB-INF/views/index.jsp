<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
${message}

<div class="bg"></div>

<div class="container">
	<div class="row jumbotron main">
		<div class="col-md-6">
			<h2>Make HDR images </h2> 	
			<p>We have to add some description there to be a way better than it is now</p>  	
			<br>
			<p>Choose <b>one file</b> to generate HDR </p>
			<br>
			<p>Choose <b>many file</b> to generate HDR </p>		
		</div>
		<div class="col-md-6">
			<h2>Choose option </h2>                  
			<div class="column">
				<fieldset class="text-center">
				</fieldset>
                <a href="/clear" class="btn btn-block btn-primary">Clear</a>

                <form action="/upload" class="dropzone" id="dropzone-many" enctype="multipart/form-data">
                </form>
                <a href="/make-hdr" class="btn btn-block btn-primary">Make HDR</a>

			</div> 
		</div>
	</div>
</div>

