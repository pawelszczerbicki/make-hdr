jQuery(function($){

	Dropzone.options.dropzoneMany = {
		paramName: "file", // The name that will be used to transfer the file
		maxFilesize: 10, // MB
		acceptedFiles: 'image/*',
		uploadMultiple: false,
        enqueueForUpload: false,
		dictDefaultMessage: 'Drop several files to upload (or click)'
	};

});