-- TABLE: Registered Users
CREATE TABLE IF NOT EXISTS registered_users (
    id INTEGER NOT NULL PRIMARY KEY,
    register_date BIGINT NOT NULL UNIQUE,
    player_name TEXT NOT NULL UNIQUE,
    sha512_password TEXT NOT NULL
);

-- TABLE: Maps
CREATE TABLE IF NOT EXISTS maps (
    id INTEGER NOT NULL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    creation_date BIGINT NOT NULL UNIQUE,
    base64map TEXT NOT NULL,
    map_extension VARCHAR(5) CHECK (map_extension = 'png' OR map_extension = 'jpg' OR map_extension = 'jpeg')
);

-- TABLE: Entities
CREATE TABLE IF NOT EXISTS entities (
    id INTEGER NOT NULL PRIMARY KEY,
    map_id INTEGER NOT NULL REFERENCES maps(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    creation_date BIGINT NOT NULL UNIQUE,
    name TEXT NOT NULL,
    race TEXT NOT NULL,
    class TEXT,
    level INTEGER NOT NULL CHECK (level > 0),
    type INTEGER NOT NULL DEFAULT 0,
    center_x REAL NOT NULL,
    center_y REAL NOT NULL,
    base64image TEXT,
    image_extension TEXT CHECK (image_extension = 'png' OR image_extension = 'jpg' OR image_extension = 'jpeg'),
    ca INTEGER NOT NULL CHECK (ca > 0),
    hp INTEGER NOT NULL DEFAULT 0,
    max_hp INTEGER NOT NULL DEFAULT 0,
    temp_hp INTEGER NOT NULL DEFAULT 0,
    description TEXT,
    player_owner_id INTEGER REFERENCES registered_users(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    player_visibility INTEGER NOT NULL DEFAULT 0
);

-- TABLE: Player Entities
CREATE TABLE IF NOT EXISTS player_entities (
    id INTEGER NOT NULL PRIMARY KEY,
    map_id INTEGER NOT NULL REFERENCES maps(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    creation_date BIGINT NOT NULL UNIQUE,
    name TEXT NOT NULL UNIQUE,
    race TEXT NOT NULL,
    class TEXT NOT NULL,
    level INTEGER NOT NULL CHECK (level > 0),
    center_x REAL NOT NULL,
    center_y REAL NOT NULL,
    ca INTEGER NOT NULL CHECK (ca > 0),
    hp INTEGER NOT NULL DEFAULT 0,
    player_owner_id INTEGER REFERENCES registered_users(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

-- TABLE: Elements
CREATE TABLE IF NOT EXISTS elements (
    id INTEGER NOT NULL PRIMARY KEY,
    map_id INTEGER NOT NULL REFERENCES maps(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    creation_date BIGINT NOT NULL UNIQUE,
    name TEXT NOT NULL,
    center_x REAL NOT NULL,
    center_y REAL NOT NULL,
    type INTEGER NOT NULL DEFAULT 0,
    item_id INTEGER NOT NULL REFERENCES items(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    player_visibility INTEGER NOT NULL DEFAULT 0,
    UNIQUE(map_id, name)
);

-- TABLE: Waypoints
CREATE TABLE IF NOT EXISTS waypoints (
    id INTEGER NOT NULL PRIMARY KEY,
    map_id INTEGER NOT NULL REFERENCES maps(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    creation_date BIGINT NOT NULL UNIQUE,
    name TEXT NOT NULL,
    center_x REAL NOT NULL,
    center_y REAL NOT NULL,
    type INTEGER NOT NULL DEFAULT 0,
    player_visibility INTEGER NOT NULL DEFAULT 0,
    UNIQUE(map_id, name, type)
);

-- TABLE: Coin Deposits
CREATE TABLE IF NOT EXISTS coin_deposits (
    id INTEGER NOT NULL PRIMARY KEY,
    map_id INTEGER NOT NULL REFERENCES maps(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    creation_date BIGINT NOT NULL UNIQUE,
    center_x REAL NOT NULL,
    center_y REAL NOT NULL,
    player_visibility INTEGER NOT NULL DEFAULT 0,
    mr INTEGER NOT NULL DEFAULT 0,
    ma INTEGER NOT NULL DEFAULT 0,
    me INTEGER NOT NULL DEFAULT 0,
    mo INTEGER NOT NULL DEFAULT 0,
    mp INTEGER NOT NULL DEFAULT 0
);