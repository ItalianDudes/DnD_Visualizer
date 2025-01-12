package it.italiandudes.dnd_visualizer.features;

import it.italiandudes.dnd_visualizer.utils.Randomizer;
import org.jetbrains.annotations.NotNull;

public final class ProTipsManager {

    // Pro Tips
    private static final String[] PRO_TIPS = new String[]{
            "Ricordati di dare un'occhiata alle impostazioni per personalizzare la tua esperienza in gioco.",
            "Qui dovrebbe esserci una pro tip, utile vero?",
            "Ti e' venuta un'idea per questa app? Vai al menu principale clicca sul pulsante \"Segnala un Problema o Proponi un'Idea\" per ottenere il link al GitHub per propormi la tua idea!\nSe e' fattibile e interessante verra' implementata!",
            "Sai che e' disponibile un launcher per questa e tutte altre applicazioni di ItalianDudes?\nSe non lo stai gia' usando, vai al Menu Principale e clicca su \"Verifica Aggiornamenti\", potrai da li scaricare il launcher!",
            "Usare un'armatura completa ti permette di avere un valore di CA fisso, ma e' un blocco unico, mentre invece assemblarsi la propria armatura pezzo pezzo ti permette di personalizzarti completamente la tua armatura!",
            "Se la tua classe aggiunge altera i modificatori caratteristica applicati al calcolo della CA, clicca tasto destro sullo scudo della CA per mettere i modificatori applicati!",
            "Aggiungi un incantesimo dall'inventario per visualizzarlo nella sezione incantesimi!",
            "Sei troppo pesante? Scopri quali oggetti pesano di piu' cliccando sulla colonna \"Peso\" nell'inventario!",
            "Il DM ha detto qualcosa di particolarmente utile? Segnalo nelle note per non dimenticartelo!",
            "Hai paura che l'app si chiuda e che tu perda tutti i progressi? Non ti preoccupare! Il D&D Visualizer salva costantemente tutti i progressi!",
            "Lo sapevi che lo sviluppatore di questa app su Discord si chiama \"hacker6329_hd\"?",
            "Siamo realisti, questa app e' stata sviluppata da una persona sola in anni di duro lavoro, ed e' pure gratuita con codice disponibile su GitHub!\nItalianDudes e' tanta roba!",
            "Dicono che sia in arrivo una modalita' di creazione guidata per le schede, sarebbe interessante vero?",
            "Quanto sarebbe bello poter vedere la mappa di gioco senza dover usare quei sitacci spesso a pagamento non del tutto personalizzabili!",
            "Sai che questa applicazione funziona su tutti i dispositivi in cui Java 8 e' disponibile? E Java 8 e' disponibile per TANTI dispositivi!",
            "L'area di testo \"Privilegi & Tratti\" nella tab \"Tratti & Competenze\" e' li per retrocompatibilita', in realta' sarebbe meglio metterli nella tab \"Privilegi e Tratti\".",
            "INSERT INTO player (happiness) VALUES (OVER9000!!!);",
            "Prima di iniziare la campagna, tira qualche dado e vedi come ti va, se i valori sono bassi, forse sarebbe meglio avvisare il DM che oggi non e' la tua giornata!",
            "Sai che il logo cambia in base al periodo dell'anno?",
            "Eliminando importanti file di sistema....... SCHERZAVO!!!",
            "Ho DECISAMENTE bisogno di un caffe'...",
            "Ho DECISAMENTE bisogno di rimettere a posto i miei orari di sonno...",
            "Dici che e' preoccupante che molto del codice di questa app e' stato scritto di notte tra le 12 e le 6 di mattina?",
            "Non sottovalutare mai l'importanza di una pausa durante la sessione, puo' aiutare il DM e i giocatori a riprendersi dopo magari ore di immersione!"
    };

    // Tips Getter
    @NotNull
    public static String getRandomProTip() {
        return PRO_TIPS[Randomizer.randomBetween(0, PRO_TIPS.length)];
    }
}
