# 1.0.0 - 04/10/2015
- NUOVA FUNZIONE: aggiunto nuovo parametro eseguendo BYAManager tramite linea di comando. Comunque si tratta una funzione da utenti esperti e che serve più a me che ad altri ;)
- RISOLTO BUG MINORE: ultimo bug conosciuto da tempo, risolto. Cioè, forzando l'aggiornamento del db o del programma, l'interfaccia grafica non si congela più per qualche secondo. Ora non ne conosco di rilevanti.

# 0.9.0 - 10/04/2015
- NUOVA FUNZIONE: ora se Apple rilascia un nuovo dispositivo, es Apple Watch, non dovrò far uscire un aggiornamento del programma (es la versione 0.9.1), ma mi basterà modificare i dati sul mio server, così magicamente, nei menu a tendina del programma, appariranno le scritte coi nomi commerciali di questi prodotti Apple (è comunque richiesto un riavvio, dopo la mia modifica, ovvio ;))

# 0.8.0 - 06/04/2015
- PREMESSA: da diversi mesi Apple rilascia le versioni di iTunes anche con link https. Inoltre, ha fatto si che tutte le versioni di iTunes siano scaricabili con https. Infatti, ora si può scaricare tutte le versioni di iTunes sia tramite collegamento http, sia https. Quindi, è evidente che vada verso https.
BYAManager non è stato pensato per supportare connessioni https, così ho dovuto estendere il programma e potenziare notevolmente il sistema di download.
- LA MIA SOLUZIONE: In questa versione ho aggiunto proprio la possibilità di scaricare file via https (per ora senza accelerazione dei download). Questa funzione è già abilitata, ma per una questione di sicurezza, cercherò di usare solo link http, fino a che Apple non rilascerà solo versioni con https. Fino a questo giorno, continuerò a migliorare la parte https, in preparazione di quel momento.
Poiché non so quando avverrà quel momento, ho deciso di aggiungere il supporto e abilitarlo direttamente nella versione 0.8.0. Se apple rilascerà il nuovo iOS (magari per l'Apple Watch) o iTunes SOLO via https, non servirà aggiornare il programma, poiché l'ho già preparato per quel momento grazie alla versione 0.8.0.

# 0.7.0 - 03/04/2015
- Nuovissimo sistema di logging, per creare il file BYAManager.log e portato anche su BYAUpdater.
- Aggiornamento librerie per il download dei file.
- Riabilitato il proxy HTTP nelle impostazioni (attenzione assicurasi di trovare un proxy a prestazioni elevate).
- Corretto bug che lanciava messaggio d'errore durante la reinizializzazione del programma. In realtà la funzionalità era ok, ma c'era un messaggio d'errore inutile.


# 0.6.0 - 02/04/2015
- Definitivamente corretto problema nel sistema di auto-aggiornamento per Mac OSX e Linux e anche Windows, presente da anni. Ho cambiato proprio il metodo per farlo, usando le nuove funzioni di java.
- Miglioramento generale delle performance nel processo di download.
- Completamente rifatto il sistema di connessione al server.
- Completamente rivisto il sistema per il download dei firmware e itunes. Ora è più facile da estendere per riabilitare i proxy, nella 0.7.0.


# 0.5.6 - 03/30/2015
- Corretto problema nel sistema di auto-aggiornamento per Mac OSX e Linux, presente da anni.
- NOTA BENE per gli utenti Windows: assicuratevi di eseguire BYAManager.jar da una cartella in cui nel percorso non appaiono spazi. E' un bug che ho corretto per MAC OSX e LINUX, mentre per Windows ci vuole ancora un po' di pazienza. Devo trovare un attimo libero e lo correggo facilmente.


# 0.5.5 - 03/29/2015
- Cambiamento del server che gestisce gli aggiornamenti. Non avendo più il precedente, tutte le versioni di questo programma (prima della 0.5.5) non sono più supportate, sia nel funzionamento, sia negli aggiornamenti. 
- Aggiornate dipendenze varie di librerie usate internamente.


# 0.5.4 - 10/21/2014
- Aggiornato ai nuovi dispositivi


# 0.5.3 - 09/29/2014
- Versione uscita dopo più di 2 anni di attesa per rendere di nuovo funzionante il programma.
- Aggiunti nuovi dispositivi e database
- Richiede Java 8 o superiore
- Aggiornate tutte le librerie interne
- Attenzione: l'auto-aggiornamento di itunes non va perchè semplicemente apple cambia metodo ogni 2 secondi ed è impossibile fare un sistema davvero efficace. Invece, quello dei firmware non ha mai avuto problemi in anni e anni XD


# 0.5.2 - 04/06/2012
NOVITA' GRAFICHE
- La colonna della tabella chiamata "Versione" non ha piu' nessun logo a fianco. Il logo del file ipsw e' stato spostato nella prima colonna, nel caso in download sia di un firmware.
- Aggiunti i pulsanti per eseguire le azioni di spostamento ed esecuzione
- Aggiunta la voce nel popup (prima era chiamata COMING SOON) per disabilitare il controllo della sha1

NOVITA’ IN FUNZIONALITA'
- Comando update-software finalmente completato e funzionante (in realta' andava anche nella 0.5.1 :) anche se con alcuni possibili errori)
- Cliccando col tasto destro su una riga della tabella (con un download in corso o in pausa) si puo' scegliere se disabilitare la verifica della SHA1 al termine del download. Questa procedura (verifica dell'hash tramite algoritmo sha1, identificata dall'icona fucsia col punto esclamativo) assicura la correttezza del file scaricato, ma potrebbe richiedere del tempo ( a volta anche un minuto) e un notevole spreco di risorse. Vista la crescente affidabilita' del programma, ho deciso di dare all'utente la possibilita' di disabilitare la procedura per ottenere il file desiderato piu' rapidamente.
- Aggiunta tutta la logica per gestire le azioni di esecuzione e spostamento dei download termimati
- Aggiunto GOD BETA1 (Guardian of download) per tenere sotto controllo i download, la loro velocita' e il fatto che alcuni processi non si blocchino senza motivo


# 0.5.1 - 03/28/2012
NOVITA' GRAFICHE
- Rimosso il limite di ridimensionamento dello sfondo della finestra del programma per risoluzioni superiori a 1920x***. - Infatti, fino ad ora, la finestra principale del programma mostrava lo sfondo con una dimensione massima di 1920x***, con quelle superiori appariva lo sfondo grigio. Ora non accade più. Adesso lo sfondo viene disegnato in modo molto piu’ efficiente.
- Rifatte le icone download, validation ecc… nella tabella
- Aggiunti i bordi nei popupmenu (come il menu file, help ecc…in alto oppure quello che appare cliccando col tasto destro su un download nella tabella) e migliorati i “separator” tra gli elementi di questi menu.
- Aggiunta voce nel popupmenu della tabella chiamata “coming soon”, che in futuro sara’ sostituita dalla reale funzione 
- Ora la velocità globale non appare più come “0″, ma se è 0 perchè i download sono fermi o non ce ne sono non appare nulla (vuoto)
- La finestra preferenze appare a centro schermo e da ora se dovessi modificare la dimensione predefinita di una finestra il posizionamento al centro avviene in automatico (cosi’ evito i problemi col pannello delle preferenze).
- Migliorata SplashScreen per adattarla meglio alla grafica del programma
- Cambiata dimensione predefinita del programma (+ lungo) e ricentrata.
- Aggiunto autoridimensionamento delle colonne della tabella (ancora da perfezionare)
- Nella colonna della velocita’ ora non appare nulla se il download e’ in pausa (velocita’ ==0)
- Colonna “ora fine” rimossa e migliorata quella del tempo rimanente che ora dice h, m e s in modo preciso.

NOVITA’ GENERICHE
- Praticamente riscritta la gestione della GUI per essere piu’ adattabile.
- Aggiornamento delle librerie di terze parti
- Aggiunto comando update-software, ma ancora in beta e non provato in caso di aggiornamento reale
- La fase di verifica nuove versioni software non richiede piu’ la scrittura di dati sul disco (maggiori performance)
- La fase di verifica corretteza file JAR durante un agg software non richiedono piu’ la scrittura di dati sul disco (maggiori performance)
- Pulizia ed ottimizzazione del codice.
- Aggiunta suddivisione tra stati merging e validation. Il primo avviene sempre alla fine di ogni download (identificato dal simbolo +) e consiste nell’unire i vari file part che compongono il download per fornire il file desiderato. Il secondo può avvenire dopo al merging (per ora è sempre obbligatorio, nella 0.6.0 non lo sarà più) e consiste nel verificare tramite l’algoritmo SHA1 la correttezza del file scaricato. Questo metodo è lo stesso che consiglia Apple e che anche iTunes, probabilmente, esegue.

CORREZIONE BUG (BUGFIX)
- Rimossa una lettura+scrittura di un file di oltre 600KB di testo ad ogni verifica degli aggiornamenti con Apple. Ora l’intera operazione è svolta in modo super-efficiente e in un tempo praticamente impercettibile. Questo permette di rendere molto piu’ veloce la verifica degli aggiornamenti dei firmware con Apple (un grazie allo sviluppatore della libreria che uso per averla aggiornata in modo da risolvere il mio problema , dopo averlo contattato via email, da solo non ci sarei mai riuscito!!!).
- Corretto GRAVISSIMO bug durante l’avvio e di conseguenza non avviene più nessuna scrittura superflua sul disco. 
- CORRETTO GRAVE BUG CHE IMPEDIVA L’AUTO-AGGIORNAMENTO DEL PROGRAMMA
- Corretto bug nella gestione del pulsante per verificare gli aggiornamenti software
- Corretto bug su abilitazione/disabilitazione delle voci nel menu help: agg software e agg database
- Corretto problema nella generazione dei path sul disco per i pc con la partizione di sistema diversa da “C” (windows)


# 0.5.0.0 - 03/19/2012
Changelog 0.5.0.0 FINALE (news rispetto la RC2): 
- Testato per bene il sistema di autoaggiornamento database e del programma
- Migliorato il menu del programma con una divisione delle voci più intelligente
- Messaggi nella splashscreen tradotti in inglese e italiano
- Aggiunta traduzione inglese sui nuovi componenti della gui




Changelog 0.5.0 RC2:

- Corretto grave bug nell'aggiornamento delle versioni di iTunes perche' non caricava il db aggiornato all'avvio, ma quello precedente. In realta' mancava un sacco di codice per questa parte :) errore mio XD.
- Corretto grave bug nell'aggiornamento delle preferenze, sembrava risolto ma e' tornato ancora :) Ora spero di averlo rimosso.



Changelog 0.5.0 RC1:

- Aggiunto download versioni di iTunes e preparato gia' per i tool per il Jailbreak
- Aggiunto sistema di autoaggiornamento delle versioni di iTunes direttamente dal server Apple.
- Il database usato dal programma e' ora compresso per dimezzarne la dimensione. Questo porta alla novita' seguente...
- Ridotto il consumo di banda (internet) durante l'avvio del programma (AD OGNI AVVIO).
- Corretto grave bug nel sistema di aggiornamento delle preferenze (presente dalla 0.4.0 alla 0.5.0beta4 [non rilasciata al pubblico]).
- Funzionante "update-db" da linea di comando. Grazie ad esso e' possibile risolvere problemi sul caricamento del database al momento dell'avvio in tutta semplicità. Questo comando rimuove i database esistenti e scarica le versioni piu' recenti dal server, dopodiche' cerca anche di aggiornarli contattando Apple. Terminato il processo, il software viene chiuso e al riavvio (normale con doppio clic) avrai i database aggiornati.
- Corretto il bug: "se 4 i temp file vengono rinominati manualmente o a causa di un problema in BYAM e poi avvio il prog, lui giustamente non li carica, ma non li rimuove nemmeno, sia nel caso in cui il download sia incompleto sia nel caso fosse terminato. Ovviamente, se il firmware non puo' essere trovato, i sui part non servono e devono essere rimossi."
- Corretto il bug: "Se per errore il file .sha si trova in temp al prossimo riavvio veniva cancellato, ora invece viene spostato tra i download ed appare una richiesta all'utente su che fare"
- Rivisto il pannello delle impostazioni con la possibilita' di personalizzare le notifiche e inserire i valori per il Proxy
- Separati i file delle traduzioni tra normale, preferenze e messaggi di notifica.
- GUI separata dalla logica del programma per il 50%, il processo, lento e complicato richiedera' del tempo per essere perfezionato
- Cambiato ordine dei firmware, dal piu' recente al piu' vecchio
- Cambiato ordine dispositivi in modo piu' intelligente e aggiunti nuovi nomi commerciali AppleTV3 e iPad3
- Creato messaggio con spunta che avvisa alla prima riduzione ad icona
- Aggiunto il supporto ad ArchLinux 2011 (ultmi update con kernel 3.1.x.x) sia con KDE sia con Gnome
- Ora si puo' rinizializzare il programma anche durante la fase di aggiornamento del database
- Aggiunte le icone di itunes nella tabella
- Cambiate alcune scritte nella splashscreen 
- Corretti bug minori
- Ottimizzazione di alcune procedure e migliorato il codice per renderlo + facile da aggiornare in futuro



# 0.4.0 - 01/06/2012
CHANGELOG 
- Ora il programma si chiamerà BYAManager 0.4.0 e non più beta4, visto che questa versione non merita il nome beta, dopotutto è stabile.
- Aggiunta la spashScreen prima dell'avvio dell'interfaccia grafica del programma
- RISOLTO IMPORTANTISSIMO BUG NEL SISTEMA DI AUTOAGGIORNAMENTO DEL DATABASE DEI FIRMWARE E DIVERSE NOVITA' PER RENDERLO ANCORA PIU' AUTOMATIZZATO 
- CON LA  0.4.0, DOPO AVERLO SCARICATO, IL NUMERO DI RIAVVI PER INSTALLARE IL NUOVO DATABASE SI E' RIDOTTO DRASTICAMENTE, ORA FA TUTTO LUI ECCETTO UN CASO PARTICOLARE CHE NON SI PUO' EVITARE (POCHI MINUTI/ORE DOPO L'USCITA DI NUOVI FIRMWARE/DISPOSITIVI).
- RIABILITATI GLI AUTOAGGIORNAMETI (IN BETA 3 HO DISATTIVATO TUTTO) PER LE VERSIONI SUPERIORI ALLA 0.4.0 (SISTEMA 1000 VOLTE PIU' SICURO RISPETTO QUELLO USATO PRIMA DELLA BETA3) 
- Interrompendo download al 99% durante unione delle 4 parti, al riavvio il processo continua correttamente, a differenza della beta3
- Interrompendo download al 99% durante la validation, al riavvio il processo viene verificato chiedendo all'utente cosa fare
- Cambiati i nomi commerciali aggiungendo gli spazi e la scritta "touch" con la G su consiglio di Filippo Rossi di BYAStaff
- Il pannello preferenze non manda + i download in pausa nemmeno modificando le impostazioni, perchè esse saranno applicate al riavvio
- Ora si puo' cambiare il percorso dei download e poi continuare a scaricare nella cartella prima del cambiamento senza problemi per poter far si che le modifiche abbiamo effetto bisogna per forza riavviare il programma.
- Pannello delle preferenze completamente rivisto e gestito in modo differente e migliore.
- Si puo' cambiare il percorso di download scrivendo direttamente nel campo di testo nel pannello preferenze
- Su OSX ora appare il nome di BYAManager nella barra in alto e non + logica.main
- Corretto bug grafico nella toolbar che causava la rimozione dell'effetto arrotondato
- Ora i testi presenti nei pulsanti verdi e personalizzati appaiono ben centrati
- Modificato e ottimizzato il sistema di gestione dei pulsanti della toolbar (risolto un bug su windows7 che causava un comportamento anomalo nella versione prebeta3 rilasciata solo allo staff di BYA. Per nasconderlo temporaneamente avevo adottato una tecnica folle che richiedeva troppe risorse)
- Rimosso i pulsanti STOP e anche la riga nel popup sulla tabella. Ora un pulsante ferma e rimuove automaticamente, tutto in uno
- Rimosse diverse immagini presenti nel programma che gestivano lo stato STOP, riducendo la dimensione del file jar
- Aggiunta la traduzione in ceco inviatami da un utente
- Durante la fase di validation non si può più mettere in pausa o cancellare un download (come giusto che sia)
- Cambiamenti alle colonne della tabella (IMPORTANTE per i prossimi aggiornamenti), tra cui: la versione di iOS e la build sono integrate nella stessa colonna "versione" e dispositivo e' sostituito da "tipo".
- Aggiunta voce per ridurre ad icona il programma nel menu file
- Corretto bug nella gestione del Logger
- Corretti altri bug grafici minori e poco visibili
- Corretti altri bug funzionali minori
 

# Beta 3 - 12/28/2011
- AGGIUNTA l'interfaccia grafica in stile BYA e riposizionati molti elementi (cambiata la textfield nelle impostazioni con un menu a tendina e personalizzato ogni elemento grafico).
- AGGIUNTA la velocità globale di download (somma delle velocità di ogni singolo download ed aggiornata ogni secondo).
- AGGIUNTA la compatibilità con JAVA 7
- AGGIUNTA la possibilità di inviare comandi via terminale per avviare il programma secondo diverse modalità, proprio come in redSn0w (utile per risolvere problemi di avvio e per reinizializzare/aggiornare il programma)
- OTTIMIZZAZIONE dell'intero programma e di alcune procedure di ricerca
- RIMOSSA (momentaneamente) la splash screen in attesa di una versione migliore
- AGGIUNTA la doppia lingua in base a quella dell'OS (italiano, inglese)
- MIGLIORATO il sistema di autoaggiornamento
- CORRETTO bug gravissimo che causava consumi spropositati di CPU...ora si attesta tra l'8 e il 20% con tantissimi download ;) direi che è perfetto.
- INTERAMENTE riscritto il sistema di Download per supportare connessioni contemporanee thread-safe aumentando di un centinaio di volte le prestazioni e riducendo moltissimi tempi di attesa non giustificati.
- INTERAMENTE rivisto il codice di tutto il programma (usando ove possibile i Design Pattern), ottimizzazione spinta tanto da ridurre di ben 30MB il consumo della RAM, pulendo/semplificando anche il codice e permettendomi di aggiornarlo e modificarlo più velocemente.
- Rivisto il progetto del programma per aumentare la riusabilita', preparandomi per il riciclo del codice nel caso sviluppassi un nuovo progetto per BYA.
- INTERAMENTE riscritto il sistema per calcolare velocità, tempo rimanente e data fine che ora avviene nello stesso istante (ogni secondo) per tutti i download in lista permettendo di stimare facilmente la velocità globale senza commettere errori eccessivi. Questo novità porta con sè un'enorme ottimizzazione della RAM usata, infatti ora vengono risparmiati più di 10 MB semplicemente riscrivendo questa piccola, ma importantissima, funzione. C'è ancora lavoro da fare per ridurre gli sprechi, ma per ora è più che accettabile XD.
- Corretto il bug presente nelle versioni alpha8, alpha9, beta1 e beta2 che causava un "lampeggio" della tabella quando si cancellava un download completato e sopra di esso ve ne era un altro completato (senza altri in stati differenti). C'è voluto un sacco ma finalmente ho trovato il difetto, bastava aggiungere una sola riga di codice per correggerlo ;)
- Diversi bug (ALCUNI RILEVANTI) corretti in tutto il programma
- PULIZIA del codice e miglioramenti vari
- Possibilità di scegliere come visualizzare la dimensione dei file nella tabella tra Byte, Multipli del Byte (tiene conto del fattore 1000) [PREDEFINITO per comodità] o Multipli del Binary Byte (tiene conto del fattore 1024). Quindi, aggiunto menu' a tendina del pannello delle preferenze.



# Beta 2 - 10/24/2011
- Migliorata la grafica aggiungendo icone nella tabella, riducendo la dimensione della toolbar (impedendone anche lo spostamento), riprogrettando il pannello impostazioni con l'uso di schede.
- Dimensione del download nella tabella non viene mostrata in byte ma in KB, MB o GB. E' comunque possibile ripristinare la visione precedente tramite il pannello "impostazioni" e riavviando il programma.
- Su mac OSX ora l'app nella dock appare con l'icona corretta e col nome giusto
- Aggiunta la data di ultimo aggiornamento database
- Completamente rivisito e corretto il sistema di aggiornamento database automatico ogni 24ore o manuale cliccando sul pulsante apposito...ora funziona e non presenta più i bug riscontrati nella versione beta1 con l'uscita di iOS5
- Il sistema di auto-aggiornamento ora verificherà anche la correttezza dell'updater e della nuova versione del programma tramite SHA, per evitare di eseguire applicazioni pericolose.
- Moltissimi bugfix in tutto il programma e in tutte le funzionalità
- Migliorate le prestazioni
- Inserito sistema totalmente anonimo per scopi statistici sull'uso del programma
- e altre modifiche che ora non mi vengono in mente ma che ci sono di certo... XD
