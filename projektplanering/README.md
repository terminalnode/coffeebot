# Coffee Bot-planering
(This document serves to document the planning and development process of this bot. The planning, but not the bot, will be removed from the repository once the project is finished.)

Planeringen går ut på att utöka funktionaliteten stegvis med flera delmål tills de ultimata projektmålen (listade nedan) är implementerade.

De ultimata målen för projektarbetet är:
1. Att ha en bot som kan utföra vissa funktioner när den triggas antingen av en viss händelse eller innehåll i ett meddelande eller med ett kommando.
2. Att vissa inställningar för boten ska sparas lokalt i JSON-format.
3. Att vissa av inställningarna för boten ska vara serverspecifika.
4. Att skapa en funktion där användare kan logga sina kaffedrickarvanor i en databas (rimligtvis en serverless SQL-databas som sqlite).

Utöver detta finns två bonusmål, som jag hoppas uppnå men som kanske inte är rimligt inom projektets tidsramar:
1. Att exportera boten till ett .jar-format så att den kan startas utanför IntelliJ och till exempel lämnas ensam på en server.
2. Att databasen över kaffedrickarvanorna ska hostas på Amazon Web Services.

# Delmål
Mål 1 och 2 är skrivna i retrospekt då de redan var avklarade när planeringen påbörjades, de syftar till att beskriva mitt tillvägagångssätt när jag påbörjade projektet. Listan är i nedåtgående ordning med de äldsta målen längst ner.

## Delmål 5: Botens svar på tilltal ska kunna modifieras
En användare med admin-rättigheter ska kunna ställa in vad boten svarar med när den tilltalas genom ett @mention i servern där inställningen sker (funktionen från delmål 3).

Detta mål är uppnått när:
* Boten lämnar olika svar på tilltal i olika servrar.
* Användare med adminbehörigheter kan ändra svaret under runtime.
* Svaren ändras inte vid omstart. Ändringar som görs sparas som variabler i boten men ändras också omedelbart på disk.

**Status:** Ej implementerat

## Delmål 4: Inställningar ska kunna göras genom Discord och sparas i JSON
Användare ska kunna göra inställningar som sparas i JSON-format på disk och laddas när boten startar. Administratörer ska ha rättighet att göra vissa ändringar som vanliga användare inte kan.

**Status:** Ej implementerat

## Delmål 3: Boten ska kunna svara på tilltal
Kommandon är praktiskt, men passiva funktioner som triggas vid specifika tillfällen är också bra. En funktion i Discord är att man kan highlighta personer genom att skriva @PersonensNick, ett rimligt mål.

Detta mål är uppnått när boten kan svara på meddelanden där den omnämnts med ett @mention. När det är klart är ultimat mål 1 avklarat.

**Status:** Ej implementerat

## Delmål 2: Boten ska kunna ta ett enkelt kommando
Det finns botar som bara ligger och läser alla meddelanden som kommer flödande samt eventuellt reagerar på dessa meddelanden, men den typen av botar är svårare att hitta användningsområden för än de som exekverar funktioner på kommando. Boten bör kunna göra båda delar men kommandon är viktigast, därför bör något ramverk för att kunna upptäcka och reagera på kommandon implementeras.

Detta mål är uppnått när boten kan svara på ett enkelt kommando. Kommandot i fråga kan vara helt eller delvis baserat på en existerande mall.

**Status:** Klart 2019-09-19
Det fanns ett utökningsbibliotek till JDA som heter JDA Utilities, i detta finns möjlighet att implementera kommandon på ett enkelt sätt. Boten har kan nu svara på kommandot `?ping`.

## Delmål 1: Boten ska kunna gå online
Hitta och implementera ett javabibliotek för att skapa Discord-botar. Målet uppnått när botens status inte står som offline.

**Status:** Klart 2019-09-19
Biblioteket som använts heter JDA, vilket står för Java Discord API. Det är en inofficiell wrapper kring Discords egen API.
