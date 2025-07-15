<?php
//index.php
/**
 * Entry point of the application, API routes setup and application start.
 */
require_once __DIR__ . '/config.php';
require_once __DIR__ . '/App.php';
require_once __DIR__ . '/Controller.php';
require_once __DIR__ . '/Model.php';

use App\App;

/** 
 * Create application instance
 */
$app = new App();

/**
 * Register API routes
 * 
 * Format: addRoute(HTTP_METHOD, URI, [ControllerClass, Method], authRequired = false)
 */
$app->addRoute('POST', '/api/auth',         ['UserController', 'auth'        ]      );
$app->addRoute('POST', '/api/save',         ['UserController', 'save'        ], true);
$app->addRoute('GET' , '/api/loadList',     ['UserController', 'loadList'    ], true);
$app->addRoute('GET' , '/api/loadDataById', ['UserController', 'loadDataById'], true);

/** Run the application (handle the current request) */
$app->run();


