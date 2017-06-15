(function(){
	return {

	    getName: function () {
	    	return "hu.webarticum.jpluginmanager.testjsplugin.TestJsPlugin";
	    },

	    getVersion: function () {
	    	return "0.1.1";
	    },

	    getLabel: function () {
	    	return "Hello JS";
	    },

	    validate: function () {
	    	return true;
	    },
	    
		getExtensions: function (type) {
			if (type.getSimpleName().equals("HelloExtensionInterface")) {
				return [{
					
					hello: function () {
						println("Hello, JavaScript!");
					}
					
				}];
			} else {
				return [];
			}
		}
		
	};
})();