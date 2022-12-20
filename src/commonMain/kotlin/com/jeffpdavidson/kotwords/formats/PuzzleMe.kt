package com.jeffpdavidson.kotwords.formats

import com.jeffpdavidson.kotwords.formats.json.JsonSerializer
import com.jeffpdavidson.kotwords.formats.json.PuzzleMeJson
import com.jeffpdavidson.kotwords.model.MarchingBands
import com.jeffpdavidson.kotwords.model.Puzzle
import com.jeffpdavidson.kotwords.model.RowsGarden
import okio.ByteString.Companion.decodeBase64
import okio.ByteString.Companion.toByteString
import kotlin.math.min
import kotlin.math.roundToInt

private val PUZZLE_DATA_REGEX = """\bwindow\.rawc\s*=\s*'([^']+)'""".toRegex()
private val KEY_REGEX = """var [a-zA-Z]+\s*=\s*"([a-z\d]+)"""".toRegex()

private val ROWS_REGEX = """Row \d+: """.toRegex(RegexOption.IGNORE_CASE)
private val BANDS_REGEX = """[^:]*band: """.toRegex(RegexOption.IGNORE_CASE)

/** Container for a puzzle in the PuzzleMe (Amuse Labs) format. */
class PuzzleMe(val json: String) : Puzzleable {

    override suspend fun asPuzzle(): Puzzle {
        val data = JsonSerializer.fromJson<PuzzleMeJson.Data>(json)
        val grid: MutableList<MutableList<Puzzle.Cell>> = mutableListOf()

        val cellInfoMap = data.cellInfos.associateBy { it.x to it.y }

        // PuzzleMe supports circled cells and cells with special background shapes. We pick one
        // mechanism to map to circles (preferring "isCircled" which is a direct match) and the
        // other if present.
        val circledCells =
            when {
                data.cellInfos.find { it.isCircled } != null -> {
                    cellInfoMap.filterValues { it.isCircled }.keys
                }
                else -> {
                    data.backgroundShapeBoxes.filter { it.size == 2 }.map { it[0] to it[1] }
                }
            }

        val images = data.imagesInGrid.flatMap { image ->
            val format = ParsedImageFormat.fromExtension(image.imageFormat)
            val parsedImage = ParsedImage.parse(format, image.image.decodeBase64()!!.toByteArray())
            val widthBoxes = image.endX - image.startX + 1
            val heightBoxes = image.endY - image.startY + 1
            val imageWidth = (parsedImage.width / widthBoxes.toDouble())
            val imageHeight = (parsedImage.height / heightBoxes.toDouble())
            (image.startX..image.endX).flatMap { x ->
                (image.startY..image.endY).flatMap { y ->
                    val croppedImage = parsedImage.crop(
                        width = imageWidth.roundToInt(),
                        height = imageHeight.roundToInt(),
                        x = ((x - image.startX) * imageWidth).roundToInt(),
                        y = ((y - image.startY) * imageHeight).roundToInt(),
                    )
                    if (croppedImage.containsVisiblePixels()) {
                        listOf((x to y) to croppedImage.toPngBytes())
                    } else {
                        listOf()
                    }
                }
            }
        }.toMap()

        for (y in 0 until data.box[0].size) {
            val row: MutableList<Puzzle.Cell> = mutableListOf()
            for (x in 0 until data.box.size) {
                // Treat black squares, void squares, and squares with no intersecting words that aren't pre-filled
                // (which likely means they're meant to be revealed after solving) as black squares.
                val box = data.box[x][y]
                val cellInfo = cellInfoMap[x to y]
                val isBlack = box == null || box == "\u0000"
                val isVoid = cellInfo?.isVoid == true
                // If bgColor == fgColor, assume the square is meant to be hidden/black and revealed after solving.
                val isInvisible = cellInfo?.bgColor?.isNotEmpty() == true && cellInfo.bgColor == cellInfo.fgColor
                // If the square has no intersecting words that aren't pre-filled, assume the square is likely meant to
                // be revealed after solving.
                val hasNoIntersectingWords =
                    data.boxToPlacedWordsIdxs.isNotEmpty() &&
                            (!data.boxToPlacedWordsIdxs.indices.contains(x) ||
                                    !data.boxToPlacedWordsIdxs[x].indices.contains(y) ||
                                    data.boxToPlacedWordsIdxs[x][y] == null)
                val isPrefilled = data.preRevealIdxs.isNotEmpty() && data.preRevealIdxs[x][y]
                val backgroundImage =
                    if (images.containsKey(x to y)) {
                        Puzzle.Image.Data(
                            format = Puzzle.ImageFormat.PNG,
                            bytes = images[x to y]!!.toByteString()
                        )
                    } else {
                        Puzzle.Image.None
                    }
                val borderDirections =
                    setOfNotNull(
                        if (cellInfo?.topWall == true) Puzzle.BorderDirection.TOP else null,
                        if (cellInfo?.bottomWall == true) Puzzle.BorderDirection.BOTTOM else null,
                        if (cellInfo?.leftWall == true) Puzzle.BorderDirection.LEFT else null,
                        if (cellInfo?.rightWall == true) Puzzle.BorderDirection.RIGHT else null,
                    )

                if (isBlack || isVoid || isInvisible || (hasNoIntersectingWords && !isPrefilled)) {
                    // Black square, though it may have a custom background and/or borders.
                    val backgroundColor =
                        if (isBlack) {
                            cellInfoMap[x to y]?.bgColor ?: ""
                        } else {
                            ""
                        }
                    row.add(
                        Puzzle.Cell(
                            cellType = if (isVoid) Puzzle.CellType.VOID else Puzzle.CellType.BLOCK,
                            backgroundColor = backgroundColor,
                            backgroundImage = backgroundImage,
                            borderDirections = borderDirections,
                        )
                    )
                } else {
                    val backgroundShape =
                        if (circledCells.contains(x to y)) {
                            Puzzle.BackgroundShape.CIRCLE
                        } else {
                            Puzzle.BackgroundShape.NONE
                        }
                    val number =
                        if (data.clueNums.isNotEmpty() && (data.clueNums[x][y] ?: 0) != 0) {
                            data.clueNums[x][y].toString()
                        } else {
                            ""
                        }
                    row.add(
                        Puzzle.Cell(
                            solution = if (isPrefilled && box!! == "*") { "" } else { box!! },
                            cellType = if (isPrefilled) Puzzle.CellType.CLUE else Puzzle.CellType.REGULAR,
                            backgroundShape = backgroundShape,
                            number = number,
                            foregroundColor = cellInfo?.fgColor ?: "",
                            backgroundColor = cellInfo?.bgColor ?: "",
                            borderDirections = borderDirections,
                            backgroundImage = backgroundImage,
                        )
                    )
                }
            }
            if (grid.size > 0 && grid[0].size != row.size) {
                throw InvalidFormatException("Grid is not rectangular")
            }
            grid.add(row)
        }

        // Post-solve revealed squares can lead to entirely black rows/columns on the outer edges. Delete these.
        val anyNonBlackSquare = { row: List<Puzzle.Cell> -> row.any { !it.cellType.isBlack() } }
        val topRowsToDelete = grid.indexOfFirst(anyNonBlackSquare)
        val bottomRowsToDelete = grid.size - grid.indexOfLast(anyNonBlackSquare) - 1
        val leftRowsToDelete =
            grid.filter(anyNonBlackSquare).minOf { row -> row.indexOfFirst { !it.cellType.isBlack() } }
        val rightRowsToDelete = grid[0].size -
                grid.filter(anyNonBlackSquare).maxOf { row -> row.indexOfLast { !it.cellType.isBlack() } } - 1
        val filteredGrid = grid.drop(topRowsToDelete).dropLast(bottomRowsToDelete)
            .map { row -> row.drop(leftRowsToDelete).dropLast(rightRowsToDelete) }

        // Ignore words that don't have a proper clue associated.
        val words = data.placedWords.filterNot { it.clueNum == 0 }
        val acrossWords = words
            .filter { it.acrossNotDown }
            .filter { it.y >= topRowsToDelete && it.y <= grid.size - bottomRowsToDelete }
            .map { it.copy(x = it.x - leftRowsToDelete, y = it.y - topRowsToDelete) }
        val downWords = words
            .filterNot { it.acrossNotDown }
            .filter { it.x >= leftRowsToDelete && it.x <= grid[0].size - rightRowsToDelete }
            .map { it.copy(x = it.x - leftRowsToDelete, y = it.y - topRowsToDelete) }
        val processedData = getProcessedPuzzleData(filteredGrid, acrossWords, downWords)

        return Puzzle(
            title = toHtml(data.title.trim()),
            creator = toHtml(data.author.trim()),
            copyright = toHtml(data.copyright.trim()),
            description = toHtml(data.description.ifBlank { data.help?.ifBlank { "" } ?: "" }.trim()),
            grid = processedData.grid,
            clues = processedData.clues,
            hasHtmlClues = true,
            words = processedData.words,
        )
    }

    companion object {
        fun fromHtml(html: String): PuzzleMe = PuzzleMe(extractPuzzleJson(html))

        fun fromRawc(rawc: String, onReadyFn: String = ""): PuzzleMe =
            PuzzleMe(decodeRawcFromOnReadyFn(rawc, onReadyFn))

        internal fun extractPuzzleJson(html: String): String {
            return decodeRawc(extractRawc(html))
        }

        internal fun extractRawc(html: String): String {
            // Look for "window.rawc = '[data]'" inside <script> tags; this is JSON puzzle data
            // encoded as Base64.
            Xml.parse(html, format = DocumentFormat.HTML).select("script").forEach {
                val matchResult = PUZZLE_DATA_REGEX.find(it.data)
                if (matchResult != null) {
                    return matchResult.groupValues[1]
                }
            }
            throw InvalidFormatException("Could not find puzzle data in PuzzleMe HTML")
        }

        private fun deobfuscateRawc(rawc: String): String {
            val rawcParts = rawc.split(".")
            return deobfuscateRawc(rawcParts[0], rawcParts[1].reversed())
        }

        private fun deobfuscateRawc(rawc: String, keyStr: String): String {
            val buffer = rawc.toCharArray()
            val key = keyStr.map { it.digitToInt(16) + 2 }
            var i = 0
            var segmentCount = 0
            while (i < buffer.size - 1) {
                // Reverse sections of the buffer, using the key digits as lengths of each section.
                val segmentLength = min(key[segmentCount++ % key.size], buffer.size - i)
                (0 until segmentLength / 2).forEach { j ->
                    val temp = buffer[i + j]
                    buffer[i + j] = buffer[i + segmentLength - j - 1]
                    buffer[i + segmentLength - j - 1] = temp
                }
                i += segmentLength
            }
            return buffer.joinToString("")
        }

        internal fun decodeRawcFromOnReadyFn(rawc: String, onReadyFn: String): String {
            if (!rawc.contains('.') && onReadyFn.isNotEmpty()) {
                // Try to find the key variable in the onReady function.
                KEY_REGEX.findAll(onReadyFn).forEach { matchResult ->
                    val decodedRawc = try {
                        decodeRawc(rawc, matchResult.groupValues[1])
                    } catch (e: InvalidFormatException) {
                        // Assume this is an invalid key; try the next match.
                        return@forEach
                    }
                    return decodedRawc
                }
            }
            return decodeRawc(rawc)
        }

        private fun decodeRawc(rawc: String, key: String = ""): String {
            val deobfuscatedRawc = if (rawc.contains(".")) {
                deobfuscateRawc(rawc)
            } else if (key.isNotEmpty()) {
                deobfuscateRawc(rawc, key)
            } else {
                rawc
            }
            return deobfuscatedRawc.decodeBase64()?.utf8() ?: throw InvalidFormatException("Rawc is invalid base64")
        }

        private fun buildClueMap(isAcross: Boolean, clueList: List<PuzzleMeJson.PlacedWord>): List<Puzzle.Clue> =
            clueList.map {
                Puzzle.Clue(
                    wordId = getWordId(isAcross, it.clueNum),
                    number = it.clueNum.toString(),
                    text = toHtml(it.clue.clue)
                )
            }

        private fun buildWordList(
            grid: List<List<Puzzle.Cell>>,
            words: List<PuzzleMeJson.PlacedWord>
        ): List<Puzzle.Word> {
            return words.map { word ->
                var x = word.x
                var y = word.y
                val cells = mutableListOf<Puzzle.Coordinate>()
                repeat(word.nBoxes) {
                    cells.add(Puzzle.Coordinate(x = x, y = y))
                    if (word.acrossNotDown) {
                        x++
                    } else {
                        y++
                    }
                }
                Puzzle.Word(
                    id = getWordId(isAcross = word.acrossNotDown, clueNum = word.clueNum),
                    // Filter out any squares that fall outside the grid (e.g. due to void squares) or which cannot
                    // have letters entered in them.
                    cells = cells.filter { (x, y) ->
                        y >= 0 && y < grid.size && x >= 0 && x < grid[y].size
                                && grid[y][x].cellType == Puzzle.CellType.REGULAR
                    }
                )
            }
        }

        private fun getWordId(isAcross: Boolean, clueNum: Int): Int = (if (isAcross) 0 else 1000) + clueNum

        /**
         * Convert a PuzzleMe JSON string to HTML.
         *
         * PuzzleMe mixes unescaped special XML characters (&, <) with HTML tags. This method escapes the special
         * characters while leaving supported HTML tags untouched. <br> tags are replaced with newlines. Attributes
         * are stripped out.
         */
        internal fun toHtml(clue: String): String {
            return clue
                .replace("&", "&amp;")
                .replace("\\s*<br/?>\\s*".toRegex(RegexOption.IGNORE_CASE), "\n")
                // Strip other unsupported tags.
                .replace("</?(?:div|img)(?: [^>]*)?/?>".toRegex(RegexOption.IGNORE_CASE), "")
                .replace("<", "&lt;")
                // Workaround for New Yorker titles. These use a span with a custom style to add a margin between the
                // date and the title; since we don't support styles, add a space to separate them.
                .replace("&lt;span[^>]*class=\"[^\"]*subtitle[^\"]*\">".toRegex(RegexOption.IGNORE_CASE), " <span>")
                .replace("&lt;(/?(?:b|i|sup|sub|span))(?: [^>]*)?>".toRegex(RegexOption.IGNORE_CASE), "<$1>")
        }

        data class PuzzleData(
            val grid: List<List<Puzzle.Cell>>,
            val clues: List<Puzzle.ClueList>,
            val words: List<Puzzle.Word>
        )

        private suspend fun getProcessedPuzzleData(
            filteredGrid: List<List<Puzzle.Cell>>,
            acrossWords: List<PuzzleMeJson.PlacedWord>,
            downWords: List<PuzzleMeJson.PlacedWord>
        ): PuzzleData {
            return if (acrossWords.all { it.clue.clue.isEmpty() || it.clue.clue.contains(ROWS_REGEX) } &&
                downWords.all { it.clue.clue.isEmpty() || it.clue.clue.contains(BANDS_REGEX) }) {
                // Assume this is a Marching Bands puzzle. There's no other indication apparent in the data itself; the
                // only proper way to know is to look at the full HTML to see if the extra "Sparkling bands" Javascript
                // code which post-processes the raw puzzle data is included.

                // Regenerate words since the provided ones are invalid.
                val rowWords = acrossWords
                    .filterNot { it.clue.clue.isEmpty() }.mapIndexed { i, word ->
                        word.copy(
                            clueNum = i + 1,
                            // Clear the "Row [n]: " prefix
                            clue = word.clue.copy(clue = word.clue.clue.replace(ROWS_REGEX, "")),
                            x = 0,
                            y = word.y,
                            nBoxes = filteredGrid[0].size
                        )
                    }
                val rowWordList = buildWordList(filteredGrid, rowWords)
                val bandWords = downWords.filterNot { it.clue.clue.isEmpty() }.map { word ->
                    // Clear the "[Color] band: " prefix
                    word.copy(clue = word.clue.copy(clue = word.clue.clue.replace(BANDS_REGEX, "")))
                }
                val bandWordList = List(bandWords.size) { i ->
                    Puzzle.Word(
                        id = getWordId(isAcross = false, clueNum = i + 1),
                        cells = MarchingBands.getBandCells(
                            width = filteredGrid[0].size,
                            height = filteredGrid.size,
                            bandIndex = i,
                        )
                    )
                }
                val bandClues = bandWords.mapIndexed { i, word ->
                    Puzzle.Clue(
                        wordId = getWordId(isAcross = false, clueNum = i + 1),
                        number = ('A' + i).toString(),
                        text = toHtml(word.clue.clue)
                    )
                }

                // Clear extraneous numbers and add band letters.
                val grid = filteredGrid.mapIndexed { y, row ->
                    row.mapIndexed { x, cell ->
                        val topRightNumber =
                            if (x == y && x < bandClues.size && cell.cellType == Puzzle.CellType.REGULAR) {
                                ('A' + y).toString()
                            } else {
                                ""
                            }
                        cell.copy(
                            number = if (x == 0) "${y + 1}" else "",
                            topRightNumber = topRightNumber,
                        )
                    }
                }

                PuzzleData(
                    grid = grid,
                    clues = listOf(
                        Puzzle.ClueList("<b>Bands</b>", bandClues),
                        Puzzle.ClueList("<b>Rows</b>", buildClueMap(isAcross = true, clueList = rowWords)),
                    ),
                    words = bandWordList + rowWordList
                )
            } else if (acrossWords.all { it.clueSection == "Rows" } && downWords.all { it.clueSection == "Blooms" }) {
                // Assume this is a Rows Garden puzzle.
                // TODO(#15): Ideally, we'd rework the interface here to return the RowsGarden object directly. This
                // would retain more of the original information for better PDF conversion, possible RGZ output, etc.
                val rowsGarden = RowsGarden(
                    title = "",
                    creator = "",
                    copyright = "",
                    description = "",
                    rows = acrossWords.map { word ->
                        val answers = word.originalTerm.split(" / ")
                        val clues = toHtml(word.clue.clue).split(" / ")
                        if (answers.size != clues.size) {
                            throw InvalidFormatException("Row clue has mismatched clue and answer counts")
                        }
                        clues.zip(answers).map { (clue, answer) ->
                            RowsGarden.Entry(clue = clue, answer = answer)
                        }
                    },
                    light = downWords.filter { it.clueNum == acrossWords.size + 1 }.map { word ->
                        RowsGarden.Entry(clue = toHtml(word.clue.clue), answer = word.originalTerm)
                    },
                    medium = downWords.filter { it.clueNum == acrossWords.size + 3 }.map { word ->
                        RowsGarden.Entry(clue = toHtml(word.clue.clue), answer = word.originalTerm)
                    },
                    dark = downWords.filter { it.clueNum == acrossWords.size + 2 }.map { word ->
                        RowsGarden.Entry(clue = toHtml(word.clue.clue), answer = word.originalTerm)
                    },
                    addWordCount = false,
                    addHyphenated = false,
                ).asPuzzle()
                PuzzleData(
                    grid = rowsGarden.grid,
                    clues = rowsGarden.clues.map { it.copy(title = "<b>${it.title}</b>") },
                    words = rowsGarden.words,
                )
            } else {
                PuzzleData(
                    grid = filteredGrid,
                    clues = listOf(
                        Puzzle.ClueList("<b>Across</b>", buildClueMap(isAcross = true, clueList = acrossWords)),
                        Puzzle.ClueList("<b>Down</b>", buildClueMap(isAcross = false, clueList = downWords))
                    ),
                    words = buildWordList(filteredGrid, acrossWords + downWords)
                )
            }
        }
    }
}
