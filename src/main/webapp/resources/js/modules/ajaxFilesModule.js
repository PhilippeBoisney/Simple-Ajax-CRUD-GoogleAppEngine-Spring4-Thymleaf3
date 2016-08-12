APP.setModule("ajax-files");
$ajaxFileModule = APP[APP.getNamespace()][APP.getModule()];

(function () {
    "use strict";
    $ajaxFileModule.core = $ajaxFileModule.core || {};
    
    var files = [];
    var maxFiles = 5;
    var numberUploadedFiles = 0; 
    var urlHandler = '/url-handler';
    var filesContainer = '#files';
    var fileUploadContainer = "#uploaded-file-";
    var urlFileUploadContainer = "#uploaded-file-url-";
    var blobKeyFileUploadContainer = "#uploaded-file-blob-key-";
    var fileUploadBtnContainer = '#file-upload-btn';
    var progressBarContainer = '#progress .progress-bar';
    
    $ajaxFileModule.core.api = {
		getFiles: function() {
			return files;
		},
		getMaxFiles: function() {
			return maxFiles;
		},
		setMaxFiles: function(max) {
			maxFiles = max;
		},
		_init: function() {
			$(fileUploadBtnContainer).fileupload({
		        url: urlHandler,
		        sequentialUploads: true
		    });
		    
		    $(fileUploadBtnContainer).fileupload({
		        submit: function (e, data) {
		        	if(numberUploadedFiles < maxFiles) {
		        		var $this = $(this);
			            $.getJSON(urlHandler, function (result) {
			                data.url = result;
			                $this.fileupload('send', data);
			            });
		        	} else {
		        		self._createNotification(i18next.t('maxFiles'), 'warn');
		        	}
		            return false;
		        },
		        progressall: function (e, data) {
		            var progress = parseInt(data.loaded / data.total * 100, 10);
		            $(progressBarContainer).css('width', progress + '%');
		        },
		        done: function(e, data) {
		        	var json = jQuery.parseJSON(data.result);
		        	console.log(json);
		        	files.push(json);
		        	self._updateView(json);
		        	$.notify(i18next.t('imagesDownloaded'), 'success'); 
		        }
		    });
		},
		_updateView: function(json) {
			var html = '<div id="'+ fileUploadContainer + numberUploadedFiles +'" class="col-md-4">';
			html += '<input type="hidden" id="'+ urlFileUploadContainer + numberUploadedFiles +'" value="'+ json.url +'" >';
			html += '<input type="hidden" id="'+ blobKeyFileUploadContainer + numberUploadedFiles +'" value="'+ json.blobKey +'" >';
        	html += '<img src="'+ json.url +'" alt="'+ json.title +'" />';
        	html += '<p>'+ json.description +'</p>';
        	html += '</div>';
            $(filesContainer).append(html);
            numberUploadedFiles++;
		},
    	_createNotification: function(message, styleClass) {
    		$.notify(message, styleClass);
    	},
    	_resetFilesUpload: function() {
    		files = [];
			numberUploadedFiles = 0;
			$(filesContainer).html("");
    	}
    };
    
    var self = $ajaxFileModule.core.api;
})();

$ajaxFileModule.core.api._init();
