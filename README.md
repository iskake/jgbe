# JGBE Kotlin branch

Experimental branch for migrating most (if not all) of the codebase
over to Kotlin from Java.

The main reasons for doing this (in order of importance):
- Unsigned integer types
  - This would mean not having to cast every byte and short `toUnsignedInt`
  and back when doing arithmetic or bitwise operations.
  - (Though, unsigned arrays are currently in beta)
- Syntactic sugar such as _operator overloading_
- More modern and functional than Java, less verbose.
- ?