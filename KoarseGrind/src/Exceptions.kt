package hoodland.opensource.koarsegrind


/**
 * NameCollisionException: This Exception will be thrown if any test shares a name with another test. This is considered a "preclusion" in that Koarse Grind will decline to run the suite if it is thrown.
 *
 * @constructor
 *
 * @param collidedName The name that was used more than once.
 */
class NameCollisionException(collidedName: String) : Exception("It is prohibited for two tests to have the same: $collidedName")


/**
 * IdentifierCollisionException: This Exception will be thrown if any test shares an identifier with another test. This is considered a "preclusion" in that Koarse Grind will decline to run the suite if it is thrown.
 *
 * @constructor
 *
 * @param collidedIdentifier The identifier that was used more than once.
 */
class IdentifierCollisionException(collidedIdentifier: String) : Exception("It is prohibited for two tests to have the same identifier (other than null): $collidedIdentifier")




class DuplicateOutfitterException(msg: String): Exception(msg) { }
class StrayOutfitterException(msg: String): Exception(msg) { }

