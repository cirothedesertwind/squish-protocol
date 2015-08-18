# Squish Protocol Specification

Version 0.0.1

## Objective

The squish protocol is designed to be an easy to implement, small, bi-directional message protocol for messages consisting of binary data. Multiple modes are possible between clients and servers so communication can switch between exchanges of a single message to exchanges of multiple message familes.

## Definitions

* **Message:** Any binary blob that gets sent across the wire as a single group. Specified by a Message Specifcation file.
* **Message family:** A group of messages that describe a certain set of actions that are related.
* **Message specification file:** A document that describes what primitive elements are composed to make a certain message.

## Primitive Types

* **Option**
    * A metatype that allows for the inclusion of a None type or a Some type which contains the variant of primitive type specified in the message specification. 
* **Boolean**
    * True (0xFF) and False (0x00)
* **Integer**
    * byte, short, int, long, ubyte, ushort, uint, ulong, varint
    * Integers are encoded using Big-Endian notation (like Java)
    * varint represents a variable-sized positive integer up to 2^31-1 (Java's Integer.MAX_VALUE)
        * [0-127] can be represented as the number itself
        * [128-256] is preceded by 0xB1
        * [257-65536] is preceded by 0xB2
        * [65537-2^31-1] is preceded by 0xB4
* **Floating Point**
    * float, double
    * Uses the IEEE 754-2008 standard.
* **String**
    * A string is a varint specifying the length of the string in bytes followed by the UTF-8 encoded string.
* **Array**
    * All arrays are of a single type.
    * Arrays support byte, short, int, long, ubyte, ushort, uint, ulong, varint, float, double, String primitives.
    * For an array of boolean values, use a bit array for a more compressed representation.
* **Bit Array**
    * An array of bits.
* **Map**
    * A map ordered by insertion order.
    * Keys are byte, short, int, long, ubyte, ushort, uint, ulong, float, double, String primitives.
    * Objects are byte, short, int, long, ubyte, ushort, uint, ulong, varint, float, double, String, any array, BitSet, and Binary data.
* **Dates and Times**
* **Binary**

## Modes

* **Multi Family**
* **Single Family**
* **Cross Family Multi Message**
* **Single Message**
