/**
 * @PluginName test-simplified-js-plugin
 * @PluginVersion 0.1.0
 * @PluginLabel Test Simplified JS Plugin
 * @ExtensionInterface hu.webarticum.jpluginmanager.Main$HelloExtensionInterface
 */
(function(){
	return {
		getExtensions: function(type) {
			return [{
				
				hello: function () {
					println("Hello, simpler JavaScript!");
				}
				
			}];
		}
	}
})();