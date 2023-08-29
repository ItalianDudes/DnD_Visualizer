-- TABLES DECLARATION
CREATE TABLE IF NOT EXISTS key_parameters (
     param_key VARCHAR(32) NOT NULL PRIMARY KEY,
     param_value TEXT
);

CREATE TABLE IF NOT EXISTS personality_traits (
    id INTEGER NOT NULL PRIMARY KEY,
    value TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS ideals (
    id INTEGER NOT NULL PRIMARY KEY,
    value TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS bonds (
    id INTEGER NOT NULL PRIMARY KEY,
    value TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS flaws (
    id INTEGER NOT NULL PRIMARY KEY,
    value TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS features_and_traits (
    id INTEGER NOT NULL PRIMARY KEY,
    ft_name VARCHAR(32) NOT NULL,
    ft_description TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS allies_and_organizations (
    ao_name VARCHAR(32) NOT NULL PRIMARY KEY,
    ao_description TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS notes (
    id INTEGER NOT NULL PRIMARY KEY,
    title VARCHAR(32) NOT NULL UNIQUE,
    content TEXT NOT NULL,
    creation_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_edit DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS items (
    id INTEGER NOT NULL PRIMARY KEY,
    base64image TEXT,
    image_extension TEXT CHECK (image_extension = 'png' OR image_extension = 'jpg' OR image_extension = 'jpeg'),
    name TEXT NOT NULL UNIQUE,
    cost_copper INTEGER NOT NULL DEFAULT 0,
    description TEXT,
    rarity INTEGER NOT NULL DEFAULT 0,
    weight REAL NOT NULL DEFAULT 0,
    category INTEGER NOT NULL DEFAULT 0,
    quantity INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS spells (
    id INTEGER NOT NULL PRIMARY KEY,
    item_id INTEGER NOT NULL UNIQUE REFERENCES items(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    level INTEGER DEFAULT 0,
    type TEXT,
    cast_time TEXT,
    spell_range TEXT,
    components TEXT,
    duration TEXT
);

CREATE TABLE IF NOT EXISTS equipments (
    id INTEGER NOT NULL PRIMARY KEY,
    item_id INTEGER NOT NULL REFERENCES items(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    type INTEGER NOT NULL,
    life_effect INTEGER NOT NULL DEFAULT 0,
    life_percentage_effect REAL NOT NULL DEFAULT 0,
    ca_effect INTEGER NOT NULL DEFAULT 0,
    load_effect INTEGER NOT NULL DEFAULT 0,
    load_percentage_effect REAL NOT NULL DEFAULT 0,
    other_effects TEXT
);

CREATE TABLE IF NOT EXISTS armors (
    id INTEGER NOT NULL PRIMARY KEY,
    equipment_id INTEGER NOT NULL UNIQUE REFERENCES equipments(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    slot INTEGER NOT NULL
    );

CREATE TABLE IF NOT EXISTS weapons (
    id INTEGER NOT NULL PRIMARY KEY,
    equipment_id INTEGER NOT NULL UNIQUE REFERENCES equipments(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    category TEXT,
    properties TEXT
);