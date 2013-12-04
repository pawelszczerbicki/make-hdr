jQuery(function($){
	$('.icheck').iCheck({
		checkboxClass: 'icheckbox_flat-blue',
		radioClass: 'iradio_flat-blue'
	});
	$('.icheck').on('ifChecked', function(e){
		if($(this).val() === 'one') {
			$('#visible-for-many').hide();
			$('#visible-for-one').show();
		}
		else if ($(this).val() === 'many') {
			$('#visible-for-one').hide();
			$('#visible-for-many').show();
		}
	});
	Dropzone.options.dropzoneOne = {
		paramName: "file", // The name that will be used to transfer the file
		maxFilesize: 10, // MB
		acceptedFiles: 'image/*',
		uploadMultiple: false,
		maxFiles: 1,
		dictDefaultMessage: 'Drop file to upload (or click)'
	};
	Dropzone.options.dropzoneMany = {
		paramName: "file", // The name that will be used to transfer the file
		maxFilesize: 10, // MB
		acceptedFiles: 'image/*',
		uploadMultiple: true,
		dictDefaultMessage: 'Drop several files to upload (or click)'
	};
});