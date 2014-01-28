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
            <div class="text-center">
                <label for="radio-one" class="checkbox">
                    <input id="radio-one" class="icheck" type="radio" name="choose" value="one" checked/>
                    Algorithm 1
                </label>
                <label for="radio-two" class="checkbox">
                    <input id="radio-two" class="icheck" type="radio" name="choose" value="two"/>
                    Algorithm 2
                </label>
                <label for="radio-three" class="checkbox">
                    <input id="radio-three" class="icheck" type="radio" name="choose" value="three"/>
                    Algorithm 3
                </label>
            </div>
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
    <div id="modal-result" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">Results</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-6">
                            <h3>Input images</h3>
                            <div id="input-images">

                            </div>
                        </div>
                        <div class="col-md-6">
                            <h3>Output image</h3>
                            <div id="output-images">

                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>

