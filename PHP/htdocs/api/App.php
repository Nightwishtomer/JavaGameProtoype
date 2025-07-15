<?php
    //App.php
    namespace App;

    /**
     * Core application class responsible for handling HTTP requests.
     * Manages route registration and dispatching to controller methods.
     */
    class App {
    
        /**
         * @var array<string, array{0: array, 1: bool}> 
         * Associative array of routes with keys as "METHOD /path"
         * Each value is an array: [handler, authRequired]
         */
        private array $routes = [];

        /**
         * Registers a new route.
         *
         * @param string $method HTTP method (GET, POST, etc.)
         * @param string $path URI path, e.g. "/api/login"
         * @param array $handler Array with two elements: [Controller class name, method name]
         * @param bool $auth Whether JWT authorization is required (default false)
         * 
         * @return void
         */
        public function addRoute(string $method, string $path, array $handler, bool $auth = false): void {
            $this->routes["$method $path"] = [$handler, $auth];
        }

        /**
         * Processes the current HTTP request:
         * - Sends JSON and CORS headers
         * - Handles OPTIONS preflight requests
         * - Matches request to registered routes
         * - Checks JWT token if authorization is required
         * - Instantiates controller and calls specified method
         *
         * @return void
         */
        public function run(): void {
            
            // Send headers for JSON response and CORS
            header('Content-Type: application/json');
            header('Access-Control-Allow-Origin: *');
            header('Access-Control-Allow-Headers: Content-Type, Authorization');

            // Handle OPTIONS preflight request
            if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') exit;

            $method = $_SERVER['REQUEST_METHOD'];
            $path = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);
            $key = "$method $path";

            // If route not found, respond 404
            if (!isset($this->routes[$key])) {
                http_response_code(404);
                echo json_encode(['error' => 'Not found']);
                return;
            }

            [$handler, $authRequired] = $this->routes[$key];
            [$class, $func] = $handler;

            $controller = new $class();

            if ($authRequired) {
                $token = null;

                // Retrieve token from Authorization headers
                if (isset($_SERVER['HTTP_AUTHORIZATION'])) {
                    $token = $_SERVER['HTTP_AUTHORIZATION'];
                } elseif (isset($_SERVER['REDIRECT_HTTP_AUTHORIZATION'])) {
                    $token = $_SERVER['REDIRECT_HTTP_AUTHORIZATION'];
                }
                
                 // Check Bearer token format
                if (!preg_match('/Bearer\s+(\S+)/', $token, $m)) {
                    http_response_code(401);
                    echo json_encode(['error' => 'No token']);
                    return;
                }

                 // Verify token using controller method
                $uid = $controller->verifyToken($m[1]);
                if (!$uid) {
                    http_response_code(401);
                    echo json_encode(['error' => 'Invalid token']);
                    return;
                }

                // Assign user ID to controller instance
                $controller->uid = $uid;
            }

            // Call controller method to handle request
            $controller->$func();
        }
    }
