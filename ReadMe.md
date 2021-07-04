# DW-Projekt
## Bauen der Anwendung
Die Anwendung besteht aus zwei Teilen (Frontend und Backend), welche unabhängig voneinander gebaut werden. Da das gebaute Frontend in die Backendanwendung eingebettet wird muss diese zuerst gebaut werden.
Die Anleitung dafür findet sich in der ReadMe.md im Frontend-Ordner.

Danach müssen die entstandenen Dateien aus dem Ordner unter `./Frontend/dist/` in den Ordner `./Backend/static/` kopiert werden.
Ist dies geschehen kann das Backend entsprechend der ReadMe.md im Backend-Ordner gebaut werden.

Die entstandene Jar-Datei kann dann gestartet werden. Die Anwendung ist dann unter `http://localhost:8080` zu erreichen.
