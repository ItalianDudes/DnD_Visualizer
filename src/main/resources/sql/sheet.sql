-- TABLE: Privileges and Traits
CREATE TABLE IF NOT EXISTS privileges_and_traits (
    id INTEGER NOT NULL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    type INTEGER NOT NULL,
    content TEXT
);

-- TABLE: Effects
CREATE TABLE IF NOT EXISTS effects (
    id INTEGER NOT NULL PRIMARY KEY,
    name VARCHAR(32) NOT NULL UNIQUE,
    duration TEXT,
    intensity INTEGER NOT NULL DEFAULT 0,
    is_treatable INTEGER NOT NULL DEFAULT -1,
    is_curable INTEGER NOT NULL DEFAULT -1,
    is_lethal INTEGER NOT NULL DEFAULT -1,
    life_effect INTEGER NOT NULL DEFAULT 0,
    life_percentage_effect REAL NOT NULL DEFAULT 0,
    ca_effect INTEGER NOT NULL DEFAULT 0,
    load_effect INTEGER NOT NULL DEFAULT 0,
    load_percentage_effect REAL NOT NULL DEFAULT 0,
    other_effects TEXT,
    description TEXT,
    is_active INTEGER NOT NULL DEFAULT 0
);