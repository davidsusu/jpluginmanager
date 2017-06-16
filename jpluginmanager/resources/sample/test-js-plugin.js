(function(){
	var active = false;
	
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

	    start: function () {
	    	active = true;
	    	return true;
	    },

	    stop: function () {
	    	active = false;
	    },

	    isActive: function () {
	    	return active;
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