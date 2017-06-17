(function(){
	var active = false;
	
	return {
		
	    getName: function () {
	    	return "test-strict-js-plugin";
	    },

	    getVersion: function () {
	    	return "0.1.1";
	    },

	    getDependency: function () {
	    	return {
	    		"sample-inner-plugin": "0.1.*"
	    	}
	    },

	    getLabel: function () {
	    	return "Test Strict JS Plugin";
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