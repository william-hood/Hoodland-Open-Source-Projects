package hoodland.changescan.core

import hoodland.opensource.toolbox.UNSET_STRING
import java.io.File


class FilesystemExclusion(ChosenCategory: Categories, Description: String) {
    private var category = Categories.UNSET
    private var specifics: String = UNSET_STRING
    fun Excludes(Candidate: String): Boolean {
        when (category) {
            Categories.Directory -> if (File(Candidate).isDirectory) {
                if (Candidate === specifics) {
                    return true
                }
            }
            Categories.File -> if (File(Candidate).isFile) {
                if (Candidate === specifics) {
                    return true
                }
            }
            else -> return Candidate.contains(specifics)
        }
        return false
    }

    override fun toString(): String {
        return specifics
    }

    init {
        category = ChosenCategory
        specifics = Description
    }
}
