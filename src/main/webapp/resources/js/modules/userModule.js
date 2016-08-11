APP.setModule("user");
$userModule = APP[APP.getNamespace()][APP.getModule()];

(function () {
    "use strict";
    $userModule.core = $userModule.core || {};

    // Containers
    var userModalContainer = "#user-modal";
    var deleteModalContainer = "#delete-modal";
    var deleteMessageModalContainer = "#delete-modal #message";
    var counterContainer = "#counter";
    var userModalTitleContainer = "#user-modal .modal-title";
    var userSearchModalContainer = "#user-search-result-modal";
    var tableSearchInputContainer = "#table-search";
    var tableSearchBtnContainer = "#search-btn-ms";
    var formErrorsContainer = ".modal-form-errors";
    var userModalAddBtn = "#user-modal-add-btn";
    var userModalUpdateBtn = "#user-modal-update-btn";
    var titleModalSearch = ".search-request-name";
    
    // Form containers
    var modalFormElements = [];
    var userIdInput = "#userId";
    var nameInput = "#name";
    var lastnameInput = "#lastname";
    var typeInput = "#type";
    var adminCheckbox = "#isAdmin";
    
    // Table containers
    var TRContainer = "#el-";
    var userIdTRContainer = "#el-userid-";
    var nameTRContainer = "#el-name-";
    var lastnameTRContainer = "#el-lastname-";
    var typeTRContainer = "#el-type-";
    var adminTRContainer = "#el-admin-";
    
    // URL
    var baseUserUrlApi = baseUrl + "/users";
    var searchUrl = baseUserUrlApi + "/api-search";
    var addUrl = baseUserUrlApi + "/api-add";
    var updateUrl = baseUserUrlApi + "/api-update";
    var deleteUrl = baseUserUrlApi + "/api-delete";
    
    $userModule.core.api = {
    	_init: function() {
    		// Event for search action
    		$(tableSearchBtnContainer).on('click', function (e) {
    			e.preventDefault();
    			self._search();
			});
    		
    		// Event for show user-modal
			$(userModalContainer).on('shown.bs.modal', function () {
				$(nameInput).focus();
			});
			
			// Event for hide user-modal
			$(userModalContainer).on('hidden.bs.modal', function () {
				self._cleanForm();
			})
			
			// Event for add button in user-modal
			$(userModalAddBtn).on('click', function (e) {
				e.preventDefault();
				self._add();
			});
			
			// Event for update button in user-modal
			$(userModalUpdateBtn).on('click', function (e) {
				e.preventDefault();
				self._update();
			});
			 
			//Event for remove button in delete-modal
			$(deleteModalContainer + ' .btn-primary').on('click', function (e) {
				e.preventDefault();
				self._delete();
			});
			
			// Events on rows buttons
			self._initEventsOnRowsButtons('.box-body .table');
			
			// Retrieve jQuery object in modalFormElements array
			self._retrieveModalFormElements();
    	},
    	_initEventsOnRowsButtons: function(currentId) {
    		$(currentId + ' .user-update-btn').on('click', function (e) {
				e.preventDefault();
				self._prepareForm($(this).data('userid'));
			});
			$(currentId + ' .user-delete-btn').on('click', function (e) {
				e.preventDefault();
				$(userIdInput).val($(this).data('userid'));
				$(deleteMessageModalContainer).text(i18next.t('deleteUserModalMessage'));
			});
    	},
    	_retrieveModalFormElements: function() {
    		modalFormElements.push($(userIdInput));
    		modalFormElements.push($(nameInput));
    		modalFormElements.push($(lastnameInput));
    		modalFormElements.push($(typeInput));
    		modalFormElements.push($(adminCheckbox));
    	},
    	_prepareForm: function(id) {
    		$(userModalTitleContainer).text(i18next.t('updateUserModalTitle'));
    		$(userModalAddBtn).hide();
    		$(userModalUpdateBtn).show();
    		
    		$(userIdInput).val(id);
    		$(nameInput).val($(nameTRContainer + id).text());
    		$(lastnameInput).val($(lastnameTRContainer + id).text());
    		$(typeInput).val($(typeTRContainer + id).text());
    		
    		if($(adminTRContainer + id).text() === 'true') {
    			$(adminCheckbox).attr('checked', true);
    			$(adminCheckbox).prop('checked', true);
    		}
    	},
    	_retrieveFormData: function() {
    		var admin = false;
    		if($(adminCheckbox).is(':checked')) {
    			admin = true;
    		}
			return {
				id: $(userIdInput).val(),
				name: $(nameInput).val(),
				lastname: $(lastnameInput).val(),
				type: $(typeInput).val(),
				admin: admin
			};
    	},
    	_updateCounter: function(add) {
    		var counter = $(counterContainer).text();
    		if(add) {
    			counter++;
    			$(counterContainer).text(counter);
    		} else {
    			counter--;
    			if(counter >= 0) {
    				$(counterContainer).text(counter);
    			}
    		}
    	},
    	_updateTable: function(add, user) {
    		if(add) {    		    
    			var row = '<tr id="'+ TRContainer.slice(1) + user.id +'" class="bg-success">';
    			row += '<td id="'+ nameTRContainer.slice(1) + user.id +'">'+ user.name +'</td>';
    			row += '<td id="'+ lastnameTRContainer.slice(1) + user.id +'">'+ user.lastname +'</td>';
    			row += '<td id="'+ typeTRContainer.slice(1) + user.id +'">'+ user.type +'</td>';
    			row += '<td id="'+ adminTRContainer.slice(1) + user.id +'">'+ user.admin +'</td>';
    			row += self._createButtonsActions(user.id);
    			row += '</tr>';
    			$(row).insertBefore('table > tbody > tr:first');
    			
    			self._initEventsOnRowsButtons(TRContainer + user.id);
    		} else {
    			$(TRContainer+user.id).attr('class', 'bg-success');
    			$(nameTRContainer+user.id).text(user.name);
    			$(lastnameTRContainer+user.id).text(user.lastname);
    			$(typeTRContainer+user.id).text(user.type);
    			$(adminTRContainer+user.id).text(user.admin);
    		}
    	},
    	_createButtonsActions: function(id) {
    		var td = '<td><a href="#" class="user-update-btn" title="'+ i18next.t('updateUserModalTitleUpdateBtn') +'" data-toggle="modal" data-target="#user-modal" data-userid="'+ id +'">';
				td += '<i class="fa fa-2x fa-pencil-square-o" aria-hidden="true"></i></a>';
				td += ' | <a class="user-delete-btn" href="#" title="'+ i18next.t('updateUserModalTitleDeleteBtn') +'" data-toggle="modal" data-target="#delete-modal" data-userid="'+ id +'">';
				td += '<i class="fa fa-2x fa-trash-o" aria-hidden="true"></i>';
				td += '</td>';
			return td;
    	},
    	_cleanForm: function() {
    		_.forEach(modalFormElements, function(el, key) {
			  if(el.attr('id') === 'isAdmin') {
				  el.attr('checked', false);
				  el.prop('checked', false);
			  } else {
				  el.val('');
			  }
			});
    		$(formErrorsContainer).html('');
    		$(formErrorsContainer).hide();
    		$(userModalTitleContainer).text(i18next.t('addUserModalTitle'));
    		$(userModalUpdateBtn).hide();
    		$(userModalAddBtn).show();
    	},
    	_closeUserModal: function() {
    		$(userModalContainer).modal('hide');
    	},
    	_closeDeleteModal: function() {
    		$(deleteModalContainer).modal('hide');
    	},
    	_displayFormErrors: function(msg) {
    		$(formErrorsContainer).html(msg);
    		$(formErrorsContainer).show();
    	},
    	_createNotification: function(message, styleClass) {
    		$.notify(message, styleClass);
    	},
    	_prepareSearchModal: function(users) {
    		$(titleModalSearch).html(users[0].lastname);
    		var title = '<p>'+ i18next.t('titleSeverarlUsers') +'</p>';
    		if(users.length > 1) {
    			title = '<p>'+ i18next.t('titleOneUser') +'</p>';
    		}
    		var html = title;
    		
    		_.forEach(users, function(user, key) {    			
    			var admin = i18next.t('searchModalAdminNo');
	    		if(user.admin) {
	    			admin = i18next.t('searchModalAdminYes');
	    		}
	    		html += '<div class="user-name">';
	    		html += '<span class="name-label">'+ i18next.t('searchName') +' : </span><span class="name-value">'+ user.name +'</span>';
	    		html += '</div>';
	    		html += '<div class="user-lastname">';
	    		html += '<span class="lastname-label">'+ i18next.t('searchLastname') +' : </span><span class="lastname-value">'+ user.lastname +'</span>';
	    		html += '</div>';
	    		html += '<div class="user-type">';
	    		html += '<span class="type-label">'+ i18next.t('searchType') +' : </span><span class="type-value">'+ user.type +'</span>';
	    		html += '</div>';
	    		html += '<div class="user-admin">';
	    		html += '<span class="admin-label">'+ i18next.t('searchIsAdmin') +' : </span><span class="admin-value">'+ admin +'</span>';
	    		html += '</div><hr class="hr-60" /';
  			});
    		
    		$(userSearchModalContainer + ' .modal-body').html(html);
    	},
    	_search: function() {
    		$.ajax({
				type: "POST",
				dataType: "json",
				url: searchUrl,
				data: { lastname: $(tableSearchInputContainer).val() },
				success: function(json) { 
					console.log(json); 
					
					if(json.status === 200) {
						self._prepareSearchModal(json.listObjs);
						$(tableSearchInputContainer).val('');
						$(userSearchModalContainer).modal('show');
						self._createNotification(json.message, json.styleClass);
					} else {
						self._createNotification(json.message, json.styleClass);
					}
				}, 
				error: function(xhr, ajaxOptions, thrownError) { 
					console.log(xhr);
					console.log(ajaxOptions);
					console.log(thrownError);
				}
			});
    	},
		_add: function() {
			$.ajax({
				type: "POST",
				dataType: "json",
				url: addUrl,
				data: self._retrieveFormData(),
				success: function(json) { 
					console.log(json); 
					
					if(json.status === 200) {
						self._updateCounter(true);
						self._updateTable(true, json.obj);
						self._cleanForm();
						self._closeUserModal();
						self._createNotification(json.message, json.styleClass);
					} else {
						self._displayFormErrors(json.message);
					}
				}, 
				error: function(xhr, ajaxOptions, thrownError) { 
					console.log(xhr);
					console.log(ajaxOptions);
					console.log(thrownError);
				}
			});
		},
		_update: function() {
			$.ajax({
				type: "POST",
				dataType: "json",
				url: updateUrl,
				data: self._retrieveFormData(),
				success: function(json) { 
					console.log(json); 
					
					if(json.status === 200) {
						self._updateTable(false, json.obj);
						self._cleanForm();
						self._closeUserModal();
						self._createNotification(json.message, json.styleClass);
					} else {
						self._displayFormErrors(json.message);
					}
				}, 
				error: function(xhr, ajaxOptions, thrownError) { 
					console.log(xhr);
					console.log(ajaxOptions);
					console.log(thrownError);
				}
			});
		},
		_delete: function() {
			var data = self._retrieveFormData();
			$.ajax({
				type: "POST",
				dataType: "json",
				url: deleteUrl,
				data: {id: data.id},
				success: function(json) { 
					console.log(json); 
					
					if(json.status === 200) {
						self._updateCounter(false);
						$(TRContainer + json.oldId).hide('pulsate', function() { $(TRContainer + json.oldId).remove(); });
						self._cleanForm();
						self._closeDeleteModal();
						self._createNotification(json.message, json.styleClass);
					} else {
						self._displayFormErrors(json.message);
					}
				}, 
				error: function(xhr, ajaxOptions, thrownError) { 
					console.log(xhr);
					console.log(ajaxOptions);
					console.log(thrownError);
				}
			});
		},
    };
    
    var self = $userModule.core.api;
})();