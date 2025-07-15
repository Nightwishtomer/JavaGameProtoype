<?php
    //Controller.php
    use App\Model;

    /**
     * Base controller class that provides common methods for JSON responses,
     * input parsing, token creation and verification.
     */
    class Controller {
        /**
         * @var Model Database model instance for DB operations.
         */
        protected Model $db;

        /**
         * @var int|null User ID extracted from JWT token (if authorized).
         */
        public ?int $uid = null;

        /**
         * Constructor initializes the Model instance.
         */
        public function __construct() {
            $this->db = new Model();
        }

        /**
         * Outputs JSON response and terminates the script.
         *
         * @param mixed $data Data to be encoded as JSON.
         * @param int $code HTTP status code (default 200).
         * @return void
         */
        protected function json($data, int $code = 200): void {
            http_response_code($code);
            echo json_encode($data, JSON_UNESCAPED_UNICODE);
            exit;
        }

        /**
         * Parses JSON input from the request body.
         *
         * @return array Parsed input data as associative array, or empty array if invalid.
         */
        protected function input(): array {
            return json_decode(file_get_contents('php://input'), true) ?? [];
        }

        /**
         * Creates a JWT token with user ID as subject and expiration.
         *
         * @param int $uid User ID.
         * @return string JWT token string.
         */
        protected function makeToken(int $uid): string {
            $payload = ['sub'=>$uid,'iat'=>time(),'exp'=>time()+86400];
            $data = json_encode($payload);
            return base64_encode($data) . '.' . hash_hmac('sha256', $data, Model::JWT_SECRET);
        }

         /**
         * Verifies JWT token signature and expiration.
         *
         * @param string $token JWT token string.
         * @return int|null Returns user ID (sub) if valid, null otherwise.
         */
        public function verifyToken(string $token): ?int {
            [$payloadB64, $sig] = explode('.', $token);
            $data = base64_decode($payloadB64);
            $expected = hash_hmac('sha256', $data, Model::JWT_SECRET);
            if (!hash_equals($expected, $sig)) return null;

            $payload = json_decode($data, true);
            if (!$payload || $payload['exp'] < time()) return null;

            return $payload['sub'];
        }
    }

    /**
     * UserController handles user-related actions such as authentication,
     * registration, saving/loading game data.
     */
    class UserController extends Controller {

        /**
         * Authenticates or registers user:
         * - If user exists, verifies password.
         * - If user does not exist, registers new user.
         * Returns JWT token on success.
         *
         * @return void
         */
        public function auth(): void {
            $d = $this->input();
            $user = $d['username'] ?? '';
            $pass = $d['password'] ?? '';

            if (strlen($user) < 3 || strlen($pass) < 3) {
                $this->json(['error'=>'invalid input'], 400);
            }

            $row = $this->db->getUser($user);
            if ($row) {
                if (!password_verify($pass, $row['pass_hash'])) {
                    $this->json(['error'=>'bad password'], 401);
                }
                $uid = (int)$row['id'];
            } else {
                $uid = $this->db->addUserReturnId($user, $pass);
            }
            $this->json(['token' => $this->makeToken($uid)]);
        }

        /**
         * Logs in user by checking credentials.
         * Returns JWT token on success.
         *
         * @return void
         */
        public function login(): void {
            $d = $this->input();
            $uid = $this->db->checkUser($d['username'], $d['password']);
            if (!$uid) $this->json(['error'=>'auth failed'], 401);
            $this->json(['token' => $this->makeToken($uid)]);
        }

        /**
         * Saves game data (map, level, hero position) for authenticated user.
         *
         * @return void
         */
        public function save(): void {
            $d = $this->input();
            $this->db->saveData($this->uid, $d['level'], $d['asciiMap'], array(), $d["heroPosition"]["positionTileX"], $d["heroPosition"]["positionTileY"]); //  $d['char']
            $this->json(['ok' => true]);
        }

        /**
         * Loads saved game data for authenticated user.
         *
         * @return void
         */
        public function load(): void {
            $data = $this->db->loadData($this->uid);
            if (!$data) $this->json(['error'=>'no data'],404);
            $this->json($data);
        }

        /**
         * Loads list of saved games or data entries for authenticated user.
         *
         * @return void
         */
        public function loadList(): void {
            $data = $this->db->loadList($this->uid);
            if (!$data) $this->json(['error'=>'no data'],404);
            $this->json($data);
        }

        /**
         * Loads saved game data by ID for authenticated user.
         * ID is taken from query parameter 'id'.
         *
         * @return void
         */
        public function loadDataById(): void {
            $id = isset($_GET['id']) ? (int)$_GET['id'] : 0;
            if ($id <= 0) $this->json(['error'=>'bad id'],400);
            $data = $this->db->loadDataById($this->uid, $id);
            if (!$data) $this->json(['error'=>'no data'],404);
            $this->json($data);
        }
    }
