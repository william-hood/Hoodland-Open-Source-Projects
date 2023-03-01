package hoodland.opensource.koarsegrind

/**
 * An Outfitter is used to provide a setup and teardown for a whole category of tests. Leave the categoryPath field
 * as the default (empty string) value to assign it to the top-level category. IMPORTANT: Your outfitter must be a
 * CLASS, not an OBJECT in Kotlin. Also it must use a default constructor with no parameters.
 */
public abstract class Outfitter(categoryPath: String? = null): ManufacturedTest(checkNull(categoryPath), categoryPath = categoryPath) {
    final override fun performTest() {
        // DELIBERATE NO-OP
    }

    override val overallStatus: TestStatus
        get() {
            if (setupContext.overallStatus == TestStatus.SUBJECTIVE) return TestStatus.SUBJECTIVE
            if (! setupContext.overallStatus.isPassing()) return TestStatus.INCONCLUSIVE
            return TestStatus.PASS
        }

}

internal fun checkNull(candidate: String?): String {
    if (candidate == null) return TOP_LEVEL
    return candidate
}

class DuplicateOutfitterException(msg: String): Exception(msg) { }
class StrayOutfitterException(msg: String): Exception(msg) { }