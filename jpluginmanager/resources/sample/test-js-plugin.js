(function(){
	var active = false;
	
	return {
		
	    getName: function () {
	    	return "hu.webarticum.jpluginmanager.testjsplugin.TestJsPlugin";
	    },

	    getVersion: function () {
	    	return "0.1.1";
	    },

	    getDependency: function () {
	    	return "test-jar-plugin@0.1.0";
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
			if (type.getName().equals("hu.webarticum.jpluginmanager.Main$HelloExtensionInterface")) {
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