<?php
//model.php
    // базовый класс + база + работа с PDO
    namespace App;
    use PDO;
    
    /**
     * Model class handles database connection and operations using PDO.
     */
    class Model {

        /**
         * @var PDO PDO instance for DB connection.
         */
        private PDO $pdo;

        /**
         * JWT secret key used for token signing.
         */
        const JWT_SECRET = JWT_KEY;
    
        /**
         * Initializes PDO connection with database credentials.
         *
         * @throws PDOException if connection fails.
         */
        public function __construct() {
            $this->pdo = new PDO("mysql:host=" . DB_HOST . ";dbname=" . DB_NAME . ";charset=" . DB_CHARSET, DB_USER, DB_PASS, [
                PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION
            ]);
        }

        /**
         * Retrieves user info by username.
         *
         * @param string $name Username.
         * @return array|null Associative array with keys 'id' and 'pass_hash', or null if not found.
         */
        public function getUser(string $name): ?array {
            $q = $this->pdo->prepare("SELECT id, pass_hash FROM users WHERE username=?");
            $q->execute([$name]);
            return $q->fetch(PDO::FETCH_ASSOC) ?: null;
        }
        
        /**
         * Adds new user with hashed password and returns new user ID.
         *
         * @param string $name Username.
         * @param string $pass Plain password.
         * @return int Inserted user ID.
         */
        public function addUserReturnId(string $name, string $pass): int {
            $s = $this->pdo->prepare("INSERT INTO users (username, pass_hash) VALUES (?,?)");
            $s->execute([$name, password_hash($pass, PASSWORD_BCRYPT)]);
            return (int)$this->pdo->lastInsertId();
        }

        /**
         * Adds new user with hashed password.
         *
         * @param string $name Username.
         * @param string $pass Plain password.
         * @return void
         */
        public function addUser(string $name, string $pass): void {
            $stmt = $this->pdo->prepare("INSERT INTO users (username, pass_hash) VALUES (?, ?)");
            $stmt->execute([$name, password_hash($pass, PASSWORD_BCRYPT)]);
        }

        /**
         * Checks if username/password are valid and returns user ID if success.
         *
         * @param string $name Username.
         * @param string $pass Plain password.
         * @return int|null User ID if authenticated, null otherwise.
         */
        public function checkUser(string $name, string $pass): ?int {
            $stmt = $this->pdo->prepare("SELECT id, pass_hash FROM users WHERE username = ?");
            $stmt->execute([$name]);
            $u = $stmt->fetch(PDO::FETCH_ASSOC);
            if ($u && password_verify($pass, $u['pass_hash'])) return $u['id'];
            return null;
        }
    
        /**
         * Saves or updates game save data for user.
         *
         * @param int $uid User ID.
         * @param int $level Game map level.
         * @param string $ascii ASCII map string.
         * @param array $char Character JSON-decoded array.
         * @param int $positionTileX Hero X position tile.
         * @param int $positionTileY Hero Y position tile.
         * @return void
         */
        public function saveData(int $uid, int $level, string $ascii, array $char, int $positionTileX, int $positionTileY): void {
            $stmt = $this->pdo->prepare("REPLACE INTO saves (user_id, map_level, ascii_map, char_json, positionTileX, positionTileY) VALUES (?,?,?,?,?,?)");
            $stmt->execute([$uid, $level, $ascii, json_encode($char), $positionTileX, $positionTileY]);
        }

        /**
         * Loads latest saved game data for a user.
         *
         * @param int $uid User ID.
         * @return array|null Associative array with map_level, ascii_map, and char data or null if none found.
         */
        public function loadData(int $uid): ?array {
            $stmt = $this->pdo->prepare("SELECT map_level, ascii_map, char_json FROM saves WHERE user_id = ?");
            $stmt->execute([$uid]);
            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            if (!$row) return null;
            return [
                'map_level' => $row['map_level'],
                'ascii_map' => $row['ascii_map'],
                'char' => json_decode($row['char_json'], true)
            ];
        }

        /**
         * Loads list of saved game entries for a user, limited to 4 most recent.
         *
         * @param int $uid User ID.
         * @return array|null Array of saves with keys: id, map_level, updated, or null if none.
         */
        public function loadList(int $uid): ?array {
            $stmt = $this->pdo->prepare("SELECT id, map_level, updated FROM saves WHERE user_id = ? ORDER BY updated DESC LIMIT 4");
            $stmt->execute([$uid]);
            return $stmt->fetchAll(PDO::FETCH_ASSOC);
        }
        
        /**
         * Loads saved game data by specific save ID for a user.
         *
         * @param int $uid User ID.
         * @param int $id Save ID.
         * @return array|null Associative array with save details or null if not found.
         */
        public function loadDataById(int $uid, int $id): ?array {
            $stmt = $this->pdo->prepare("SELECT id, map_level, ascii_map, char_json, updated, positionTileX, positionTileY FROM saves WHERE user_id = ? AND id = ?");
            $stmt->execute([$uid, $id]);
            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            if (!$row) return null;
            return [
                'id' => $row['id'],
                'map_level' => $row['map_level'],
                'ascii_map' => $row['ascii_map'],
                'char' => json_decode($row['char_json'], true),
                'updated' => $row['updated'],
                'positionTileX' => $row['positionTileX'],
                'positionTileY' => $row['positionTileY']
            ];
        }
    }