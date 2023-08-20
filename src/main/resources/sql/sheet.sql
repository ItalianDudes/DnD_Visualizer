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
    title VARCHAR(32) NOT NULL,
    content TEXT NOT NULL
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
    category INTEGER NOT NULL DEFAULT 0
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
    duration TEXT,
    damage TEXT
);

CREATE TABLE IF NOT EXISTS weapons (
    id INTEGER NOT NULL PRIMARY KEY,
    item_id INTEGER NOT NULL UNIQUE REFERENCES items(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    category TEXT,
    damage TEXT,
    strength_required INTEGER NOT NULL DEFAULT 0,
    properties TEXT
);

CREATE TABLE IF NOT EXISTS armors (
    id INTEGER NOT NULL PRIMARY KEY,
    item_id INTEGER NOT NULL UNIQUE REFERENCES items(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    category TEXT,
    ac INTEGER NOT NULL DEFAULT 0,
    strength_required INTEGER NOT NULL DEFAULT 0,
    stealth INTEGER NOT NULL CHECK (stealth >= -1 AND stealth <= 1) DEFAULT 0
);

-- VIEWS DECLARATION
CREATE VIEW IF NOT EXISTS v_items AS
    SELECT
        i.id AS ItemID,
        i.name AS Name,
        i.base64image AS Base64Image,
        i.cost_copper AS CostCopper,
        i.description AS Description,
        i.rarity AS Rarity,
        i.weight AS Weight
    FROM items AS i
;

CREATE VIEW IF NOT EXISTS v_armors AS
    SELECT
        i.id AS ItemID,
        a.id AS ArmorID,
        i.name AS Name,
        i.base64image AS Base64Image,
        i.cost_copper AS CostCopper,
        i.description AS Description,
        i.rarity AS Rarity,
        i.weight AS Weight,
        a.category AS Category,
        a.ac AS AC,
        a.strength_required AS StrengthRequired,
        a.stealth AS Stealth
    FROM
        items AS i
        JOIN armors AS a
        ON i.id = a.item_id
;

CREATE VIEW IF NOT EXISTS v_weapons AS
    SELECT
        i.id AS ItemID,
        w.id AS WeaponID,
        i.name AS Name,
        i.base64image AS Base64Image,
        i.cost_copper AS CostCopper,
        i.description AS Description,
        i.rarity AS Rarity,
        i.weight AS Weight,
        w.category AS Category,
        w.damage AS Damage,
        w.properties AS Properties
    FROM
        items AS i
        JOIN weapons AS w
        ON i.id = w.item_id
;

CREATE VIEW IF NOT EXISTS v_spells AS
    SELECT
        i.id AS ItemID,
        s.id AS SpellID,
        i.name AS Name,
        i.base64image AS Base64Image,
        i.cost_copper AS CostCopper,
        i.description AS Description,
        i.rarity AS Rarity,
        i.weight AS Weight,
        s.level AS Level,
        s.type AS Type,
        s.cast_time AS CastTime,
        s.spell_range AS Range,
        s.components AS Components,
        s.duration AS Duration,
        s.damage AS Damage
    FROM
        items AS i
        JOIN spells AS s
        ON i.id = s.item_id
;