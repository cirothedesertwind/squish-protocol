# Squish Protocol Specification

Version 0.0.1

## Objective

The squish protocol is designed to be an easy to implement, small, bi-directional message protocol for messages consisting of binary data. Multiple modes are possible between clients and servers so communication can switch between exchanges of a single message to exchanges of multiple message familes.

## Definitions

* **Message:** Any binary blob that gets sent across the wire as a single group. Specified by a Message Specifcation file.
* **Message family:** A group of messages that describe a certain set of actions that are related.
* **Message specification file:** A document that describes what primitive elements are composed to make a certain message.

## Primitive Types

* **Null**
* **Boolean**
* **Integer**
    * byte, short, int, long, ubyte, ushort, uint, ulong
* **Floating Point**
    * float, double
* **String**
* **Dates and Times**
* **Array**
* **Map**
* **Binary**

## Modes

* **Multi Family**
* **Single Family**
* **Cross Family Multi Message**
* **Single Message**
