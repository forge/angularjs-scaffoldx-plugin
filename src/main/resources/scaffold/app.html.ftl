<!doctype html>
<html lang="en" ng-app="${projectId}">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>${projectTitle}</title>
    <link href="styles/bootstrap.css" rel="stylesheet" media="screen">
    <link href="styles/main.css" rel="stylesheet" media="screen">
    <link href="styles/bootstrap-responsive.css" rel="stylesheet" media="screen">
</head>
<body>
    <div id="wrap">
    	
    	<div class="navbar navbar-inverse navbar-fixed-top">
    		<div class="navbar-inner">
    			<div class="container">
    				<a class="brand" href="#">${projectTitle}</a>
    			</div>
    		</div>
    	</div>
        
        <div class="container-fluid">
            <div class="row-fluid">
                <div class="span3 well">
                    <img class="hidden-phone" src="img/forge-logo.png" alt="JBoss Forge"></img>
                    <nav class="sidebar-nav" ng-controller="NavController">
                        <ul id="sidebar-entries" class="nav nav-list">
                            <#list entityNames as entityName>
                        	<li ng-class="{active: matchesRoute('${entityName}s')}"><a href="#/${entityName}s">${entityName}s</a></li>
                        	</#list>
                        </ul>
                    </nav>
                </div>
                <div class="span9">
                    <div id="main" class="well" ng-view>
                    </div>
                </div>
            </div>
        </div>
        
        <div id="push"></div>
    </div>
    
    <div id="footer">
        <div class="container">
            <p><a href="http://glyphicons.com">Glyphicons Free</a> licensed under <a href="http://creativecommons.org/licenses/by/3.0/">CC BY 3.0</a>.</p>
        </div>
    </div>
    
    <script src="scripts/vendor/jquery-1.9.1.js"></script>
    <script src="scripts/vendor/angular.js"></script>
    <script src="scripts/vendor/angular-resource.js"></script>
    <script src="scripts/app.js"></script>
    <script src="scripts/filters/startFromFilter.js"></script>
    <script src="scripts/filters/genericSearchFilter.js"></script>
    <script src="scripts/services/locationParser.js"></script>
    <#list entityNames as entityName>
    <script src="scripts/services/${entityName}Factory.js"></script>
    <script src="scripts/controllers/new${entityName}Controller.js"></script>
    <script src="scripts/controllers/search${entityName}Controller.js"></script>
    <script src="scripts/controllers/edit${entityName}Controller.js"></script>
    </#list>
</body>
</html>