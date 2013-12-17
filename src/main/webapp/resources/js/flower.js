jQuery(function($){
    var algorithm = 1;
    $('.icheck').iCheck({
        checkboxClass: 'icheckbox_flat-blue',
        radioClass: 'iradio_flat-blue'
    });
    $('.icheck').on('ifChecked', function(e){
        if($(this).val() === 'one') {
            $('#visible-for-two').hide();
            $('#visible-for-one').show();
            algorithm = 1;
        }
        else if ($(this).val() === 'two') {
            $('#visible-for-one').hide();
            $('#visible-for-two').show();
            algorithm = 2;
        }
    });


    $('#btn-makehdr').click( function(e){
        $('#result-hdr').html('<img src="/make-hdr?algorithm=' + algorithm + '" class="result-hdr" />')
    });

	Dropzone.options.dropzoneMany = {
		paramName: "file", // The name that will be used to transfer the file
		maxFilesize: 10, // MB
		acceptedFiles: 'image/*',
		uploadMultiple: false,
        enqueueForUpload: false,
		dictDefaultMessage: 'Drop several files to upload (or click)'
	};

});