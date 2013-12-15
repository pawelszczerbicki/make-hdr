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
					
					<label for="radio-one" class="checkbox">
						<input id="radio-one" class="icheck" type="radio" name="choose" value="one" checked/>
						One image
					</label>
					<label for="radio-many" class="checkbox">
						<input id="radio-many" class="icheck" type="radio" name="choose" value="many"/>
						Many images
					</label>
				</fieldset>
                <a href="/clear" class="btn btn-block btn-primary">Clear</a>
				<div id="visible-for-one">
					<form action="/upload" class="dropzone" id="dropzone-one" enctype="multipart/form-data">
                        <button type="submit">aa</button>

					</form>
                    <a href="/make-hdr" class="btn btn-block btn-primary">Make HDR</a>
                </div>
				<div id="visible-for-many" style="display: none;">
					<form action="/upload" class="dropzone" id="dropzone-many" enctype="multipart/form-data">
                        <button type="submit">aa</button>
					</form>
                    <a href="/make-hdr" class="btn btn-block btn-primary">Make HDR</a>
				</div>
			</div> 
		</div>
	</div>
</div>

