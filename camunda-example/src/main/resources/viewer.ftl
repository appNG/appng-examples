<!DOCTYPE html>
<html>
	<head>
		<link rel="stylesheet" href="/template_camunda-example/bpmn-js/assets/diagram-js.css" />
		<link rel="stylesheet" href="/template_camunda-example/bpmn-js/assets/bpmn-font/css/bpmn-embedded.css" />
	</head>
<body>

	<script src="/template_camunda-example/bpmn-js/bpmn-viewer.js"></script>
	<div id="canvas" style="width:1200px;height:800px"></div>
	<script>
		var BpmnViewer = window.BpmnJS;
		var viewer = new BpmnViewer({ container: '#canvas' });
		var xhr = new XMLHttpRequest();
		xhr.onreadystatechange = function() {
			if (xhr.readyState === 4) {
				viewer.importXML(xhr.response, function(err) {
					if (!err) {
						console.log('success!');
						viewer.get('canvas').zoom('fit-viewport');
					} else {
						console.log('something went wrong:', err);
					}
				});
			}
		};
		xhr.open('GET', '/service/${site}/camunda-example/webservice/processes/${resource}', true);
		xhr.send(null);
	</script>
	
</body>
</html>
