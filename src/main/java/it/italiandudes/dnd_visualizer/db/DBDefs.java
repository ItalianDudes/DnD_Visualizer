package it.italiandudes.dnd_visualizer.db;

@SuppressWarnings("unused")
public final class DBDefs {

    //DB Table Names
    public static final String DB_TABLE_AMMUNITIONS = "ammunitions";
    public static final String DB_TABLE_ARMORS = "armors";
    public static final String DB_TABLE_CLASSES = "classes";
    public static final String DB_TABLE_ITEMS = "items";
    public static final String DB_TABLE_LANGUAGES = "languages";
    public static final String DB_TABLE_MELEE_WEAPONS = "melee_weapons";
    public static final String DB_TABLE_RANGED_WEAPONS = "ranged_weapons";
    public static final String DB_TABLE_SPECIES = "species";
    public static final String DB_TABLE_SPECIES_AND_LANGUAGES_BRIDGE = "species_and_languages";
    public static final String DB_TABLE_SPELLS = "spells";

    public static final String[] DB_TABLE_LIST = {
            DB_TABLE_ITEMS,
            DB_TABLE_LANGUAGES,
            DB_TABLE_SPECIES,
            DB_TABLE_CLASSES,
            DB_TABLE_ARMORS,
            DB_TABLE_AMMUNITIONS,
            DB_TABLE_MELEE_WEAPONS,
            DB_TABLE_RANGED_WEAPONS,
            DB_TABLE_SPELLS
    };

}
