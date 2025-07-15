<?php
// config.php
/** 
 * Database connection configuration and JWT secret key
 *
 * IMPORTANT:
 * - Database connection parameters
 * - Secret key for signing JWT tokens (replace with your own, do not commit to repo)
 */

/** Database host */
define('DB_HOST', 'localhost');

/** Database name */
define('DB_NAME', 'diablo_db');

/** Database charset */
define('DB_CHARSET', 'utf8mb4');

/** Database username */
define('DB_USER', 'root');

/** Database password */
define('DB_PASS', '');

/** JWT secret key (used for signing tokens) */
define('JWT_KEY', 'CHANGE_ME_SUPER_SECRET');