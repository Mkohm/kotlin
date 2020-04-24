// Dangerfile.df.kts

/*
 * Use external dependencies using the following annotations:
 */
@file:Repository("https://repo.maven.apache.org")
@file:DependsOn("org.apache.commons:commons-text:1.6")

import org.apache.commons.text.WordUtils
import systems.danger.kotlin.*

// register plugin MyDangerPlugin

val danger = Danger(args)

val allSourceFiles = danger.git.modifiedFiles + danger.git.createdFiles

val changelogChanged = allSourceFiles.contains("CHANGELOG.md")
val sourceChanges = allSourceFiles.firstOrNull { it.contains("src") }
val isTrivial = danger.github.pullRequest.title.contains("#trivial")

warn("This is a test")

if (!isTrivial && !changelogChanged && sourceChanges != null) {
    warn(WordUtils.capitalize("any changes to library code should be reflected in the Changelog.\n\nPlease consider adding a note there and adhere to the [Changelog Guidelines](https://github.com/Moya/contributors/blob/master/Changelog%20Guidelines.md)."))
}

if ((danger.github.pullRequest.additions ?: 0) - (danger.github.pullRequest.deletions ?: 0) > 300) {
    warn("Big PR, try to keep changes smaller if you can")
}

if (danger.github.pullRequest.title.contains("WIP", false)) {
    warn("PR is classed as Work in Progress")
}

danger.git.createdFiles.filter {
    it.endsWith(".java")
}.forEach {
    // Using apache commons-text dependency to be sure the dependency resolution always works
    warn(WordUtils.capitalize("please consider to create new files in Kotlin"), it, 1)
}
