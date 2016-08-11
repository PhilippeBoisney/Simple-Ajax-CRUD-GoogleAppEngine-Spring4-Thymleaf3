var APP = function () {
    var namespace = null;
    var module = [];
	var lng = region;
	var localeTranslation = null;
	var failbackLocaleTranslation = null;

	var self = {
		_retrieveLocales: function() {
			$.getJSON("/resources/json/locales/"+ lng +".json", function(json) {
				localeTranslation = json;
				$.getJSON("/resources/json/locales/en_US.json", function(json) {
					failbackLocaleTranslation = json;
					self._i18NextSetup();
				});
			});

		},
		_i18NextSetup: function() {
			var options = {
				"debug": false,
				"lng": lng,
				"fallbackLng": "en_US",
				resources: {
					"fr_FR": {
						translation: localeTranslation
					},
					"en_US": {
						translation: failbackLocaleTranslation
					}
				}
			};

			i18next.init(options, (err, t) => {
				// body
			});
		}
	}
    return {
    	_init: function() {
    		self._retrieveLocales();
    	},
        getNamespace: function () {
            return namespace;
        },
        setNamespace: function (ns) {
            APP[ns] = {};
            namespace = ns;
        },
        getModule: function () {
            return module;
        },
        setModule: function (m) {
            APP[this.getNamespace()][m] = {};
            module = m;
        }
    };

}();
APP._init();
APP.setNamespace("springleafengine");
var $namespace = APP[APP.getNamespace()];