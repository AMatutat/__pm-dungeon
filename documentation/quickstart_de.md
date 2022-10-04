# Quickstart


Dieses Dokument liefert einen Einstieg in das PM-Dungeon. Es erläutert die Installation des Frameworks und die ersten Schritte, um eigene Inhalte zum Dungeon hinzuzufügen. Es dient als Grundlage für alle weiteren Praktika. Lesen Sie das Dokument daher aufmerksam durch und versuchen Sie sich zusätzlich selbst mit dem Aufbau vertraut zu machen.
Das Framework befindet sich im [`PM-Dungeon`](https://github.com/Programmiermethoden/PM-Dungeon)-Repo.

Sie benötigen nur dieses Framework für die Aufgaben, die zusätzlichen Abhängigkeiten werden automatisch über Gradle eingebunden.

*Hinweis: Achten Sie darauf, Daten nur dann in öffentliche Git-Repos zu laden, wenn Sie die nötigen Rechte an diesen Daten haben. Dies gilt insbesondere auch für Artefakte wie Bilder, Bitmaps, Musik oder Soundeffekte.*

## Installation

Sie werden das Java SE Development Kit 17.x.x oder höher benötigen.

Für eine Anleitung, wie Sie das Projekt in Ihrer IDE laden können, schauen Sie bitte in das [PM-Dungeon-Wiki: "Import Project"](https://github.com/Programmiermethoden/PM-Dungeon/wiki/Import-Project).

*Anmerkung: Sollten bei der Installation Probleme auftreten, ist vielleicht ein Blick in die [FAQ](https://github.com/Programmiermethoden/PM-Dungeon/wiki/FAQ) bzw. die [Kompatibilitätsliste](https://github.com/Programmiermethoden/PM-Dungeon/wiki/JDK-Kompatibilit%C3%A4t) hilfreich. Sollten Sie Ihr Problem dennoch nicht lösen können, melden Sie sich bitte frühzeitig bei uns.*

## Arbeiten mit dem Framework

Zu Beginn einige grundlegende Prinzipien, die Sie verstanden haben sollten, bevor Sie mit dem Dungeon arbeiten.

Das PM-Dungeon benutzt das Cross-Plattform-Java-Framework [`libGDX`](https://libgdx.com) als Backend.
Dieses ist im `PM-Dungeon`-Projekt bereits als Abhängigkeit in die Gradle-Konfiguration integriert, Sie müssen dieses nicht extra installieren. Die Ihnen zur Verfügung gestellten Vorgaben sind so umgesetzt, dass Sie kein tieferes Verständnis für das Framework oder `libGDX` benötigen, um die Aufgaben zu lösen. Sollten Sie allerdings einmal auf Probleme stoßen, kann es unter Umständen helfen, einen Blick in die [Dokumentation von `libGDX`](https://libgdx.com/wiki/) zu werfen.

Das Framework fungiert, ganz vereinfacht gesagt, als eine Facade zwischen `libGDX` und Ihrer eigenen Implementierung. Es liefert die Schnittstellen, mit denen Sie arbeiten sollen.
Sie selbst schreiben die Logik des Spiels und implementieren die Helden/Monster/Gegenstände/usw.

Bis auf einige seltene (dokumentierte) Ausnahmen werden Sie nicht gezwungen sein, an den Vorgaben Änderungen durchzuführen.

Sie werden im Laufe der Praktika verschiedene Assets benötigen. Diese liegen per Default im `assets`-Verzeichnis. Sie können das Standardverzeichnis in der `build.gradle` anpassen.

- Standardpfad für Texturen: `assets/`
- Standardpfad für Charaktere: `assets/character/`
- Standardpfad für Level-Texturen: `assets/textures/dungeon/`

## Strukturen

Bevor wir mit der eigentlichen Implementierung des Spiels anfangen, eine kurze Erklärung über den Aufbau des Frameworks.

- Das Framework verwendet sogenannte `Controller` um die einzelnen Aspekte des Spiels zu managen und Ihnen das Leben einfacher zu machen.
    - `EntityController`: Dieser verwaltet alle "aktiven" Elemente wie Helden, Monster, Items etc.
    - `LevelAPI`: Kümmert sich darum, dass neue Level erzeugt und geladen werden.
    - `ScreenController`: Verwaltet alle Bildschirm-Anzeigen, die Sie implementieren.
    - `Game` Verwaltet die anderen `Controller` und beinhaltet die Game-Loop. Ihre Implementierung wird Teil des `MyGame`.
- Game-Loop: Die Game-Loop ist die wichtigste Komponente des Spieles. Sie ist eine Endlosschleife, welche einmal pro [Frame](https://de.wikipedia.org/wiki/Bildfrequenz) aufgerufen wird. Das Spiel läuft in 30 FPS (also 30 *frames per seconds*), die Game-Loop wird also 30-mal in der Sekunde aufgerufen. Alle Aktionen, die wiederholt ausgeführt werden müssen, wie zum Beispiel das Bewegen und Zeichnen von Figuren, müssen innerhalb der Game-Loop stattfinden. Das Framework ermöglicht es Ihnen, eigene Aktionen in die Game-Loop zu integrieren. Wie genau das geht, erfahren Sie im Laufe dieser Anleitung.
- Zusätzlich existieren noch eine Vielzahl an weiteren Hilfsklassen, mit denen Sie mal mehr oder mal weniger Kontakt haben werden.
- `Painter`: Kümmert sich darum, dass die Inhalte grafisch dargestellt werden.
- `DungeonCamera`: Ihr Auge in das Dungeon.

*Hinweis: Die Game-Loop wird automatisch ausgeführt, Sie müssen sie nicht aktiv aufrufen.*

## Erster Start

Die Vorgaben sind bereits lauffähig und können direkt ausgeführt werden. Dafür können Sie die Vorgaben entweder als Projekt in Ihrer IDE laden (siehe auch [PM-Dungeon-Wiki: "Import Project"](https://github.com/Programmiermethoden/PM-Dungeon/wiki/Import-Project)) und die Anwendung über die Run-Funktion Ihrer IDE starten oder Sie starten die Anwendung über die Kommandozeile per `./gradlew run`. Öffnen Sie dafür die Konsole, gehen Sie in das `PM-Dungeon/code/`-Verzeichnis und geben Sie folgenden Befehl ein:

- Unter Windows: `.\gradlew run`
- Unter Linux: `./gradlew run`

*Anmerkung: Sollten beim Starten Probleme auftreten, ist vielleicht ein Blick in die [FAQ](https://github.com/Programmiermethoden/PM-Dungeon/wiki/FAQ) bzw. die [Kompatibilitätsliste](https://github.com/Programmiermethoden/PM-Dungeon/wiki/JDK-Kompatibilit%C3%A4t) hilfreich. Sollten Sie Ihr Problem dennoch nicht lösen können, melden Sie sich bitte frühzeitig bei uns.*

*Anmerkung: Wir verwenden in unserem Beispiel zufällig generierte Level, daher werden Sie vermutlich nicht das exakt gleiche Level sehen wie auf unseren Abbildungen.*

## Blick in den Code

Betrachten wir nun den `code/core/src/starter/MyGame.java`. Diese Klasse ist Ihr Einstiegspunkt in den Dungeon. Hier werden Sie später Ihre Inhalte erzeugen und in den Dungeon hinzufügen.

`MyGame` erbt von `Game`. `Game` kann als Hauptcontroller Verstanden werden und ist die Haupt-Steuerung des Spiels. Es bereitet alles für den Start des Spieles vor, verwaltet die anderen Controller und enthält die Game-Loop. Sie nutzen `MyGame`, um selbst in die Game-Loop einzugreifen und unsere eigenen Objekte wie Helden und Monster zu verwalten. `Game` ist der Punkt, an dem alle Fäden des Dungeons zusammenlaufen.

`MyGame` implementiert bereits einige Methoden:

- `setup` wird zu Beginn der Anwendung aufgerufen. In dieser Methode werden später die Objekte initialisiert und konfiguriert, welche bereits vor dem Spielstart existieren müssen. In der Vorgabe wird hier bereits das erste Level geladen.
- `update` Aktualisiert alle Elemente, die beim Controller registriert sind, entfernt löschbare Elemente und ruft die Update- und Draw-Methode für jedes registrierte Element auf.
- `onLevelLoad` wird immer dann aufgerufen, wenn ein Level geladen wird. Hier werden später Monster und Items erstellt, die initial im Level verteilt werden.
- `main` startet das Spiel.

## Eigener Held

@amatutat

### Intermezzo: Der Assets-Ordner

@amatutat

### Der bewegte (animierte) Held

@amatutat

### WASD oder die Steuerung des Helden über die Tastatur

@amatutat

### Nächstes Level laden

@amatutat

## Abschlussworte

Sie haben nun die ersten Schritte im Dungeon gemacht. Von nun an müssen Sie selbst entscheiden, wie Sie die Aufgaben im Praktikum umsetzen möchten. Ihnen ist mit Sicherheit aufgefallen, dass einige Interface-Methoden in diesem Dokument noch nicht erläutert wurden. Machen Sie sich daher mit der Javadoc des [Frameworks](https://github.com/PM-Dungeon/core) vertraut.

## Zusätzliche Funktionen

Hier finden Sie weitere Funktionen, welche Sie im Verlauf des Praktikums gebrauchen können.

### Head-up-Display (HUD)

- Was das HUD alles so kann

@aheinisch

### Pathfinding

- Funktionen der Level Interfaces (viele getter)
- Wie funktioniert das Pathfinding, wie benutz ich das

@Lena241
