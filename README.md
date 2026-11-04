# Tangentopoli

**Gioco da tavolo in stile Monopoly per desktop, in Java e JavaFX**, ambientato nella Milano di Tangentopoli: si comprano piazze e corsi del centro, si paga la *Tangente* invece delle tasse e si costruiscono cantieri abusivi.

Progetto universitario di gruppo, Università degli Studi dell'Aquila (2024).

## Funzionalità

- **Da 2 a 6 giocatori**, umani o controllati dal computer, su tre tabelloni di dimensione diversa scelti in base al numero di partecipanti
- **Proprietà, stazioni e società** da comprare, con affitti, costruzioni, demolizioni e ipoteche
- **Aste**: una casella libera che il giocatore non compra va all'asta tra tutti i partecipanti
- **Carte Imprevisto e Probabilità**, prigione, parcheggio e casella "Vai in prigione"
- **Giocatori CPU** con decisioni automatiche: acquisti, uscita di prigione, ipoteche e demolizioni quando restano senza soldi
- **Salvataggio e caricamento** delle partite tramite serializzazione Java
- Animazioni, interfaccia grafica e colonna sonora a tema

## Architettura

Il codice separa modello, logica e interfaccia:

| Package | Contenuto |
|---|---|
| `model` | Le entità del gioco: tabellone, caselle, giocatori, carte, dadi, asta |
| `service` | Le regole: turni, movimenti, acquisti, aste, carte, comportamento dei giocatori CPU |
| `controller` | I controller JavaFX delle schermate (menu, tabellone, asta, partite salvate) |
| `engine` | L'avvio di una nuova partita e il caricamento del tabellone adatto |
| `serialization` | Salvataggio e ripristino delle partite |
| `exception` | Eccezioni di dominio (proprietà già acquistata, soldi insufficienti, ...) |

Le caselle del tabellone sono create da una **Abstract Factory** (`BoxFactory` / `ConcreteBoxFactory`); le schermate sono descritte in **FXML** e stilizzate con CSS.

## Avvio

Servono il **JDK 21** e una connessione per scaricare le dipendenze; Maven è incluso nel wrapper.

```bash
./mvnw clean javafx:run
```

Su Windows si usa `mvnw.cmd` al posto di `./mvnw`. Le partite salvate finiscono nella cartella `saves/`.

## Tecnologie

Java 21 · JavaFX 21 (controls, FXML, media) · Maven · Jackson

## Autori

Progetto sviluppato in gruppo da [@MatteoCardarelli](https://github.com/MatteoCardarelli), [@GianFor](https://github.com/GianFor) e [@ringoliRob](https://github.com/ringoliRob).

> Nota: i nomi di luoghi, aziende e il tema storico sono usati a scopo satirico.
