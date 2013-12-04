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

				<div id="visible-for-one">
					<form action="/" class="dropzone" id="dropzone-one"> 							
						<button type="submit" class="btn btn-block btn-primary">Make HDR</button>
					</form>							
				</div>
				<div id="visible-for-many" style="display: none;">
					<form action="/" class="dropzone" id="dropzone-many">
						<button type="submit" class="btn btn-block btn-primary">Make HDR</button>
					</form>	
				</div>
			</div> 
		</div>
	</div>
</div>


<!-- <form:form method="POST" commandName="file" action="/upload"
           enctype="multipart/form-data">

    <form:errors path="*" cssClass="errorblock" element="div" />

    Please select image 1 to upload : <input type="file" name="im1" />    <br/>
    Please select image 2 to upload : <input type="file" name="im2" />    <br/>
    Please select image 3 to upload : <input type="file" name="im3" />         <br/>
    <input type="submit" value="upload" />
		<span><form:errors path="file" cssClass="error" />
		</span>

</form:form> -->