package com.jeffpdavidson.kotwords.formats

import com.jeffpdavidson.kotwords.formats.json.JsonSerializer
import com.jeffpdavidson.kotwords.formats.json.PuzzleMeJson
import com.jeffpdavidson.kotwords.model.BLACK_SQUARE
import com.jeffpdavidson.kotwords.model.Crossword
import com.jeffpdavidson.kotwords.model.Square
import org.jsoup.Jsoup
import java.nio.charset.StandardCharsets
import java.util.Base64

private val PUZZLE_DATA_REGEX = """\bwindow\.rawc\s*=\s*'([^']+)'""".toRegex()

/** Container for a puzzle in the PuzzleMe (Amuse Labs) format. */
class PuzzleMe(private val html: String) : Crosswordable {

    override fun asCrossword(): Crossword {
        return toCrossword(extractPuzzleJson(html))
    }

    companion object {
        internal fun extractPuzzleJson(html: String): String {
            // Look for "window.rawc = '[data]'" inside <script> tags; this is JSON puzzle data
            // encoded as Base64.
            Jsoup.parse(html).getElementsByTag("script").forEach {
                val matchResult = PUZZLE_DATA_REGEX.find(it.data())
                if (matchResult != null) {
                    return String(
                            Base64.getDecoder().decode(matchResult.groupValues[1]),
                            StandardCharsets.UTF_8)
                }
            }
            throw InvalidFormatException("Could not find puzzle data in PuzzleMe HTML")
        }

        internal fun toCrossword(json: String): Crossword {
            val data = JsonSerializer.fromJson(PuzzleMeJson.Data::class.java, json)
            val grid: MutableList<MutableList<Square>> = mutableListOf()
            val circledCells = data.cellInfos.filter { it.isCircled }.map { it -> it.x to it.y }
            for (y in 0 until data.box[0].size) {
                val row: MutableList<Square> = mutableListOf()
                for (x in 0 until data.box.size) {
                    if (data.box[x][y] == "\u0000") {
                        row.add(BLACK_SQUARE)
                    } else {
                        val solutionRebus = if (data.box[x][y].length > 1) data.box[x][y] else ""
                        val isCircled = data.backgroundShapeBoxes.contains(listOf(x, y))
                                || circledCells.contains(x to y)
                        row.add(Square(
                                solution = data.box[x][y][0],
                                solutionRebus = solutionRebus,
                                isCircled = isCircled))
                    }
                }
                grid.add(row)
            }
            return Crossword(
                    title = data.title,
                    author = data.author,
                    copyright = data.copyright,
                    notes = data.description,
                    grid = grid,
                    acrossClues = buildClueMap(data.placedWords.filter { it.acrossNotDown }),
                    downClues = buildClueMap(data.placedWords.filter { !it.acrossNotDown })
            )
        }

        private val clueReplacements = mapOf(
                "</?i>" to "\"",
                "ł" to "l",
                "ă" to "a",
                "[Αα]" to "[Alpha]",
                "[Ββ]" to "[Beta]",
                "[Γγ]" to "[Gamma]",
                "[Δδ]" to "[Delta]",
                "[Εε]" to "[Epsilon]",
                "[Ζζ]" to "[Zeta]",
                "[Ηη]" to "[Eta]",
                "[Θθ]" to "[Theta]",
                "[Ιι]" to "[Iota]",
                "[Κκ]" to "[Kappa]",
                "[Λλ]" to "[Lambda]",
                "[Μμ]" to "[Mu]",
                "[Νν]" to "[Nu]",
                "[Ξξ]" to "[Xi]",
                "[Οο]" to "[Omicron]",
                "[Ππ]" to "[Pi]",
                "[Ρρ]" to "[Rho]",
                "[Σσς]" to "[Sigma]",
                "[Ττ]" to "[Tau]",
                "[Υυ]" to "[Upsilon]",
                "[Φφ]" to "[Phi]",
                "[Χχ]" to "[Chi]",
                "[Ψψ]" to "[Psi]",
                "[Ωω]" to "[Omega]")
                .map { (key, value) -> key.toRegex() to value }.toMap()

        private fun buildClueMap(clueList: List<PuzzleMeJson.PlacedWord>): Map<Int, String> {
            return clueList
                    .map {
                        // TODO(#2): Generalize and centralize accented character replacement.
                        it.clueNum to
                                clueReplacements.entries.fold(it.clue.clue) { clue, (from, to) ->
                                    clue.replace(from, to)
                                }
                    }
                    .toMap()
        }
    }
}
