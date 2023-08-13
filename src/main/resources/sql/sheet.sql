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
    ft_name VARCHAR(32) NOT NULL PRIMARY KEY,
    ft_description TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS inventory (
    item_name VARCHAR(64) NOT NULL PRIMARY KEY,
    item_type VARCHAR(16) NOT NULL,
    item_value TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS allies_and_organizations (
    ao_name VARCHAR(32) NOT NULL PRIMARY KEY,
    ao_description TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS spells (
    spell_name VARCHAR(32) NOT NULL PRIMARY KEY,
    spell_value TEXT NOT NULL
);