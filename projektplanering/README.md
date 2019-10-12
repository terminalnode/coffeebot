# Coffee Bot-planering
(This document serves to document the planning and development process of this bot. The planning, but not the bot, will be removed from the repository once the project is finished.)

Planeringen går ut på att utöka funktionaliteten stegvis med flera delmål tills de ultimata projektmålen (listade nedan) är implementerade.

De ultimata målen för projektarbetet är:
1. ~~Att ha en bot som kan utföra vissa funktioner när den triggas antingen av en viss händelse eller innehåll i ett meddelande eller med ett kommando.~~
2. ~~Att vissa inställningar för boten ska sparas lokalt i JSON-format.~~
3. ~~Att vissa av inställningarna för boten ska vara serverspecifika.~~
4. ~~Att skapa en funktion där användare kan logga sina kaffedrickarvanor i en databas (rimligtvis en serverless SQL-databas som sqlite).~~

Utöver detta finns två bonusmål, som jag hoppas uppnå men som kanske inte är rimligt inom projektets tidsramar:
1. Att exportera boten till ett .jar-format så att den kan startas utanför IntelliJ och till exempel lämnas ensam på en server.
2. ~~Att databasen över kaffedrickarvanorna ska hostas på Amazon Web Services.~~

# Uppnådda mål utanför planeringen
Vissa av kraven på projektarbetet upptäcktes rätt sent och har därför inte varit med i planeringen från början.
1. Projektet ska implementera arv. Från och med 2019-10-10 så ärver SettingsFile det mesta från SettingsAbstract, vilket tillåter alternativa sätt att spara data i SettingsDB som också ärver från SettingsAbstract.
2. Projektet ska implementera en meny. Från och med 2019-10-10 så finns det en AltMain som implementerar en liten meny vid uppstart.

# Delmål
Mål 1 och 2 är skrivna i retrospekt då de redan var avklarade när planeringen påbörjades, de syftar till att beskriva mitt tillvägagångssätt när jag påbörjade projektet. Listan är i nedåtgående ordning med de äldsta målen längst ner.

## Delmål 12: De inställningar som boten sparat i databasen ska även kunna läsas av boten
Delmål 11 specificerar endast att inställningarna ska kunna sparas i databasen, men att kunna spara saker i databasen är föga användbart om vi inte kommer åt dem.

Detta mål är uppnått när:
* Inställningar kan både sparas och läsas från databasen.

**Status:** Ej implementerat

## Delmål 11: Botens inställningar ska också kunna sparas i databasen
SettingsDB fungerar inte än, den måste kunna spara inställningarna i databasen genom writeJSON() funktionen.

Detta mål är uppnått när:
* Nya inställningar registreras korrekt i databasen.

**Status:** Ej implementerat

## ~~Delmål 10: Användare ska kunna kolla hur många kaffekoppar de har druckit~~
DBHandler kan just nu bara utföra uppgifter som inte ger någon returdata, den har inget stöd för queries. För att implementera och enkelt testa detta utökas funktionaliteten i `?coffeelog` med ett subcommando för att se antalet koppar kaffe man registrerat.

När detta mål är uppnått är mål #4 för projektet uppnått.

Detta mål är uppnått när:
* `?coffeelog check` ger användare information om hur många koppar kaffe de druckit hittills.

**Status:** Klart 2019-10-12

`?coffeelog check` fungerar nu som beskrivet ovan!

## ~~Delmål 8 och 9: Registrera antalet druckna kopppar kaffe i en databas~~
Delmål 8 och 9 flöt ihop eftersom databasstrukturen började arbetas på före `?coffeelog`, respektive delmåls mission statements återfinns nedan.

(Delmål 8) När en användare använder detta kommando ska de kunna registera en drucken kaffe. I första steget behöver inte denna data sparas någonstans, bara noteras och parseas.

(Delmål 9) I bästa fall kör vi direkt på AWS för databasen, men SQLite är acceptabelt i det här stadiet. Användaren behöver inte kunna utläsa informationen som sparas eller registrera den.

Detta mål är uppnått när:
* (Delmål 8) `?coffeelog` eller liknande kan användas för att registrera en kopp kaffe.
* (Delmål 8) Användaren ska optionally kunna specificera ett antal detaljer om sin kopp kaffe.
* (Delmål 9) Outputen från ?coffeelog sparats i en databas.

**Status:** Klart 2019-10-12

`?coffeelog add` fungerar nu och användarna kan om de vill specificera hur många koppar de vill lägga till.

## ~~Delmål 7: Inställningar ska kunna nollas genom Discord~~
Detta mål är uppnått när:
* ?unset, ?unsetguild, och ?unsetowner är implementerade och återställer standardvärdet för en inställning.
* Ett återställt värde hålls återställt vid omstart av boten.

**Status:** Klart 2019-09-30 eller 2019-09-29 typ.

Kommandona `?unset`, `?unsetguild` och `?unsetbot` följt av ett inställningsnamn kan användas för att nolla inställningar.

## ~~Delmål 6: Tillgängliga inställningar ska kunna sökas efter genom Discord~~
Det finns redan en funktion för att filtrera alla inställningar beroende på om de innehåller en given substräng. Gör ett kommando av det här.

**Status:** Klart 2019-09-27

Kommandot `?setlist` följt av en eller flera söktermer listar alla tillgängliga inställningar som innehåller söksträngen.

## ~~Delmål 5: Inställningar ska kunna göras genom Discord och sparas i JSON~~
Användare ska kunna göra inställningar som sparas i JSON-format på disk och laddas när boten startar.

Detta mål är uppnått när:
* Diverse ändringar i inställningar kan göras under runtime.
* Dessa ändringar består vid omstart och skrivs till disk i JSON-format så fort de utförs.

**Status:** Klart 2019-09-26

En del omorganisering i Settings.java och CustomSettings.java var nödvändigt för att fungera i ett JSON-format. Googles bibliotek GSON används för att serialisera och deserialisera inställningar.

## ~~Delmål 4: Botens svar på tilltal ska kunna modifieras~~
En användare med admin-rättigheter ska kunna ställa in vad boten svarar med när den tilltalas genom ett @mention i servern där inställningen sker (funktionen från delmål 3).

Detta mål är uppnått när:
* Boten lämnar olika svar på tilltal i olika servrar och till olika användare.
* Vanliga användare kan ändra beteende för sig själva.
* Admins kan ändra beteende för hela servern.
* Ägare kan ändra beteende för boten.

**Status:** Klart 2019-09-25

Kommandona `?set`, `?setguild` respektive `?setowner` kan ändra inställningar för sig själva/servern/boten under förutsättning att de har rätt privilegier. Funktioner som fått settingsobjektet kan själva registrera inställningar + standardvärden för dessa vid uppstart. Texter kan fyllas i med diverse wildcards så som `{user}` för användaren, `{botname}` för boten eller `{content}` för meddelandets innehåll. Fler av dessa wildcards kan enkelt läggas till i framtiden.

## ~~Delmål 3: Boten ska kunna svara på tilltal~~
Kommandon är praktiskt, men passiva funktioner som triggas vid specifika tillfällen är också bra. En funktion i Discord är att man kan highlighta personer genom att skriva @PersonensNick, ett rimligt mål.

Detta mål är uppnått när boten kan svara på meddelanden där den omnämnts med ett @mention. När det är klart är ultimat mål 1 avklarat.

**Status:** Klart 2019-09-22

listeners/HelloListener.java kollar igenom alla meddelanden den får och svarar på de som innehåller "hi" eller "hello" samt ett mention av boten.

## ~~Delmål 2: Boten ska kunna ta ett enkelt kommando~~
Det finns botar som bara ligger och läser alla meddelanden som kommer flödande samt eventuellt reagerar på dessa meddelanden, men den typen av botar är svårare att hitta användningsområden för än de som exekverar funktioner på kommando. Boten bör kunna göra båda delar men kommandon är viktigast, därför bör något ramverk för att kunna upptäcka och reagera på kommandon implementeras.

Detta mål är uppnått när boten kan svara på ett enkelt kommando. Kommandot i fråga kan vara helt eller delvis baserat på en existerande mall.

**Status:** Klart 2019-09-19

Det fanns ett utökningsbibliotek till JDA som heter JDA Utilities, i detta finns möjlighet att implementera kommandon på ett enkelt sätt. Boten har kan nu svara på kommandot `?ping`.

## ~~Delmål 1: Boten ska kunna gå online~~
Hitta och implementera ett javabibliotek för att skapa Discord-botar. Målet uppnått när botens status inte står som offline.

**Status:** Klart 2019-09-19

Biblioteket som använts heter JDA, vilket står för Java Discord API. Det är en inofficiell wrapper kring Discords egen API.
