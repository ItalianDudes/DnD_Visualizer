package it.italiandudes.dnd_visualizer.data.bags;

import it.italiandudes.dnd_visualizer.data.enums.Category;
import it.italiandudes.dnd_visualizer.data.enums.Rarity;
import it.italiandudes.dnd_visualizer.data.item.Item;
import org.jetbrains.annotations.NotNull;

public final class DnDBags {

    // Bag List
    public  static final DnDBag DUNGEONEER_BAG = new DnDBag("Dotazione da Avventuriero", 0, true,
            configureBagItem("Piede di Porco", 200, 2.5, 1),
            configureBagItem("Martello", 100, 1.5, 1),
            configureBagItem("Chiodo da Rocciatore", 5, 0.125, 10),
            configureBagItem("Torcia",1, 0.5, 10),
            configureBagItem("Acciarino e Pietra Focaia", 50, 0.5, 1),
            configureBagItem("Razione Giornaliera", 50, 1, 10),
            configureBagItem("Otre", 20, 0.5, 1),
            configureBagItem("Corda di Canapa (15m)", 100, 5, 1)
    );
    public static final DnDBag DIPLOMAT_BAG = new DnDBag("Dotazione da Diplomatico", 0, true,
            configureBagItem("Custodia per Mappe o Pergamene", 100, 0.5, 2),
            configureBagItem("Abito Pregiato", 1500, 3, 1),
            configureBagItem("Inchiostro (boccetta da 30 grammi)", 1000, 0, 1),
            configureBagItem("Pennino", 2, 0, 1),
            configureBagItem("Lampada", 50, 0.5, 1),
            configureBagItem("Olio (Ampolla)", 10, 0.5, 2),
            configureBagItem("Foglio di Carta", 20, 0, 5),
            configureBagItem("Fiala di Profumo", 500, 0, 1),
            configureBagItem("Cera per Sigillo", 50, 0, 1),
            configureBagItem("Sapone", 2, 0, 1)
    );
    public static final DnDBag EXPLORER_BAG = new DnDBag("Dotazione da Esploratore", 0, true,
            configureBagItem("Giaciglio", 100, 3.5, 1),
            configureBagItem("Gavetta", 20, 0.5, 1),
            configureBagItem("Acciarino e Pietra Focaia", 50, 0.5, 1),
            configureBagItem("Torcia",1, 0.5, 10),
            configureBagItem("Razione Giornaliera", 50, 1, 10),
            configureBagItem("Otre", 20, 0.5, 1),
            configureBagItem("Corda di Canapa (15m)", 100, 5, 1)
    );
    public static final DnDBag ENTERTAINER_BAG = new DnDBag("Dotazione da Intrattenitore", 0, true,
            configureBagItem("Giaciglio", 100, 3.5, 1),
            configureBagItem("Costume", 500, 2, 2),
            configureBagItem("Candela", 1, 0, 5),
            configureBagItem("Razione Giornaliera", 50, 1, 5),
            configureBagItem("Otre", 20, 0.5, 1),
            configureBagItem("Trucchi per il Camuffamento", 2500, 1.5, 1)
    );
    public static final DnDBag PRIEST_BAG = new DnDBag("Dotazione da Sacerdote", 0, true,
            configureBagItem("Coperta", 50, 1.5, 1),
            configureBagItem("Candela", 1, 0, 10),
            configureBagItem("Acciarino e Pietra Focaia", 50, 0.5, 1),
            configureBagItem("Cassetta per le Offerte", 5, 0.25, 1),
            configureBagItem("Cubetto di Incenso", 20, 0.1, 2),
            configureBagItem("Incensiere", 50, 0.4, 1),
            configureBagItem("Veste", 100, 2, 1),
            configureBagItem("Razione Giornaliera", 50, 1, 2),
            configureBagItem("Otre", 20, 0.5, 1)
    );
    public static final DnDBag BURGLAR_BAG = new DnDBag("Dotazione da Scassinatore", 0, true,
            configureBagItem("Sfere Metalliche (sacchetto da 1000)", 100, 1, 1),
            configureBagItem("Spago (3m)", 10, 0.1, 1),
            configureBagItem("Campanella", 100, 0, 1),
            configureBagItem("Candela", 1, 0, 5),
            configureBagItem("Piede di Porco", 200, 2.5, 1),
            configureBagItem("Martello", 100, 1.5, 1),
            configureBagItem("Chiodo da Rocciatore", 5, 0.125, 10),
            configureBagItem("Lanterna Schermabile", 500, 1, 1),
            configureBagItem("Olio (Ampolla)", 10, 0.5, 2),
            configureBagItem("Razione Giornaliera", 50, 1, 5),
            configureBagItem("Acciarino e Pietra Focaia", 50, 0.5, 1),
            configureBagItem("Otre", 20, 0.5, 1),
            configureBagItem("Corda di Canapa (15m)", 100, 5, 1)
    );
    public static final DnDBag SCHOLAR_BAG = new DnDBag("Dotazione da Studioso", 0, true,
            configureBagItem("Libro", 2500, 2.5, 1),
            configureBagItem("Inchiostro (boccetta da 30 grammi)", 1000, 0, 1),
            configureBagItem("Pennino", 2, 0, 1),
            configureBagItem("Foglio di Pergamena", 10, 0, 10),
            configureBagItem("Sacchetto di Sabbia", 5, 0.25, 1),
            configureBagItem("Coltellino", 25, 0.6, 1)
    );

    // Methods
    private static Item configureBagItem(@NotNull final String name, final int mr, final double weight, final int quantity) {
        return new Item(null, null, null, name, mr, null, Rarity.COMMON, weight, Category.ITEM, quantity);
    }
}
