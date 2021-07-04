# DW-Projekt Backend

## Übersicht

Bei dem Backend handelt es sich um ein Spring-Boot-Projekt. Als Build-Tool wurde maven gewählt, der maven-wrapper
unterstützt dabei, dass möglichst wenig Abhängigkeiten notwendig sind für den Bau. Es wird mindestens Java 11 
benötigt.

## Abhängigkeiten und Konfigurationen

Benötigt wird:

* Java 11
* Eine lokal laufende MariaDB Datenbank

Es muss eine leere Datenbank erzeugt werden. Die application.properties.sample wird nach application.properties kopiert,
und die Login-Daten für diese Datenbank werden dann in die application.properties eingetragen. Die Tabellen und Beispieldaten
werden mit dem ersten Start automatisch angelegt.

Demo-Zugangsdaten: 

| Username | Passwort      | Rolle    |
|----------|---------------|----------|
| admin    | regenbogen    | Admin    |
| storm    | seitenwechsel | Lehrende |
| demjin   | kruge         | Lernende |

Eine Liste mit allen zu Demo-Zwecken vorhandenen Zugangsdaten kann in Anhang 1 der Hausarbeit gefunden werden.

## Authentifizierung

Es kann sich entweder via http basic auth gegen die API authentifiziert werden, oder via `/login` eine form-login Website
abgerufen werden die bei erfolgreichem login ein session-cookie setzt.

## Ausliefern statischer Dateien

Im Ausführungspfad muss neben der Anwendung der Ordner `static` existieren,
Diese Dateien aus diesem Ordner werden statisch ausgeliefert, so dass das gebaute
Frontend dort liegen sollte (die index.html also unter `static/index.html`)

## Anwendung Starten ("quickstart")

*Linux oder OS X*
```bash
./mvnw spring-boot:run
```

*Windows*
```cmd
./mvnw.cmd spring-boot:run
```

Der Backend-Server läuft danach auf localhost port 8080

## Portable Anwendung bauen

```
./mvnw package
```

## API Dokumentation
Eine Online-Api-Dokumentation kann unter `https://localhost:8080/swagger-ui.html` abgerufen werden.


