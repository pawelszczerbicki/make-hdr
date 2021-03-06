jQuery(function($){
    var algorithm = 1;
    $('.icheck').iCheck({
        checkboxClass: 'icheckbox_flat-blue',
        radioClass: 'iradio_flat-blue'
    });
    $('.icheck').on('ifChecked', function(e){
        if($(this).val() === 'one') {
            algorithm = 1;
        }
        else if ($(this).val() === 'two') {
            algorithm = 2;
        }
        else if ($(this).val() === 'three') {
            algorithm = 3;
        }
    });


    $('#btn-makehdr').click( function(e){

        $.get('/amount', function(result){
            $('#input-images').empty();
            for(var i = 0; i < parseInt(result); i++){
                $('#input-images').append($('<img>', { src: '/uploaded-photo?photo=' + i}));
            }
            $('#output-images').empty().append($('<img>', { src: '/make-hdr?algorithm=' + algorithm }));
            $('#modal-result').modal();
        });

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