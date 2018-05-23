# Desing patterns
### Introduction
One of the goals of this task was to practice in implementing patterns. This is a list of the patterns used in client-Java.

All file paths below are relative to `./src/D592Client/`.


### Creational patterns

* Abstract factory. `class RepresentationFactory` in `GameObjects/RepresentationFactory.java`.

* Singleton. `class NetWorker` in `NetUtils/NetWorker.java`; `class GameThread` in `GameThread.java`.

* Builder. `class Packet` in `NetUtils/Packet.java` (builds `java.net.DatagramPacket` objects).


### Structural patterns

* Flyweight. `interface Representation`, `class RepresentationFactory` in `GameObjects/Representation.java` and `GameObjects/RepresentationFactory.java`.

* Facade. `class GameThread` in `GameThread.java`.


### Behavioural patterns

* Command. `interface UICommand` in `UserInterface/UICommand/UICommand.java`.

* Observer. `class GameThread` in `GameThread.java` (Observable), `interface IndicatorMessage`, `interface IndicatorField`, `interface IndicatorPlayer` in `UserInterface/IndicatorMessage.java`, `UserInterface/IndicatorField.java`, `UserInterface/IndicatorPlayer.java` (Observer).
