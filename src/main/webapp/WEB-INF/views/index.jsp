<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="bg"></div>

<div class="container">
	<div class="row jumbotron main">
		<div class="col-md-6">
			<h2>How it works?</h2>
			<p>We have to add some description there to be a way better than it is now</p>  	
			<br>
			<p>Choose <b>one file</b> to generate HDR </p>
			<br>
			<p>Choose <b>many file</b> to generate HDR </p>		
		</div>
		<div class="col-md-6">
			<h2>Make HDR image</h2>
			<div class="column">
				<fieldset class="text-center">
				</fieldset>

                <form action="/upload" class="dropzone" id="dropzone-many" enctype="multipart/form-data">
                </form>
                <a id="btn-makehdr" href="javascript:void(0);" class="btn btn-block btn-primary">Make HDR</a>
                <div id="result-hdr"></div>
			</div> 
		</div>
	</div>
</div>

