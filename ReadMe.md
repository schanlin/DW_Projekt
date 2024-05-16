# DW-Projekt
## Bauen der Anwendung
Die Anwendung besteht aus zwei Teilen (Frontend und Backend), welche unabhängig voneinander gebaut werden. Da das gebaute Frontend in die Backendanwendung eingebettet wird, muss dieses zuerst gebaut werden.
Die Anleitung dafür findet sich in der ReadMe.md im Frontend-Ordner.

Danach müssen die entstandenen Dateien aus dem Ordner unter `./Frontend/dist/Frontend/` in den Ordner `./Backend/target/static/` kopiert werden.
Ist dies geschehen, kann das Backend entsprechend der ReadMe.md im Backend-Ordner gebaut werden. Als nächstes muss die `applications.properties.example` aus dem Backend-Ordner in den Ordner `./Backend/target/`kopiert und in `applications.properties` umbenannt werden.
In dieser Datei können die Logindaten für die Datenbank geändert werden.

Anschließend kann die entstandene Jar-Datei unter `./Backend/target/` kann dann gestartet werden. Die Anwendung ist dann unter `http://localhost:8080` zu erreichen.
