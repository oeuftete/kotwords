package com.jeffpdavidson.kotwords.web

import com.jeffpdavidson.kotwords.KotwordsInternal
import com.jeffpdavidson.kotwords.model.Puzzle
import com.jeffpdavidson.kotwords.model.Spiral
import com.jeffpdavidson.kotwords.web.html.FormFields
import com.jeffpdavidson.kotwords.web.html.Html
import kotlin.js.Promise

@JsExport
@KotwordsInternal
class SpiralForm {
    private val jpzForm = PuzzleFileForm("spiral", ::createPuzzle)
    private val title: FormFields.InputField = FormFields.InputField("title")
    private val creator: FormFields.InputField = FormFields.InputField("creator")
    private val copyright: FormFields.InputField = FormFields.InputField("copyright")
    private val description: FormFields.TextBoxField = FormFields.TextBoxField("description")
    private val inwardAnswers: FormFields.TextBoxField = FormFields.TextBoxField("inward-answers")
    private val inwardClues: FormFields.TextBoxField = FormFields.TextBoxField("inward-clues")
    private val outwardAnswers: FormFields.TextBoxField = FormFields.TextBoxField("outward-answers")
    private val outwardClues: FormFields.TextBoxField = FormFields.TextBoxField("outward-clues")
    private val inwardCells: FormFields.TextBoxField = FormFields.TextBoxField("inward-cells")

    init {
        Html.renderPage {
            jpzForm.render(this, bodyBlock = {
                this@SpiralForm.title.render(this, "Title")
                creator.render(this, "Creator (optional)")
                copyright.render(this, "Copyright (optional)")
                description.render(this, "Description (optional)") {
                    rows = "5"
                }
                inwardAnswers.render(this, "Inward answers") {
                    placeholder = "In sequential order, separated by whitespace. " +
                            "Non-alphabetical characters are ignored."
                    rows = "5"
                }
                inwardClues.render(this, "Inward clues") {
                    placeholder = "One clue per row. Omit clue numbers."
                    rows = "10"
                }
                outwardAnswers.render(this, "Outward answers") {
                    placeholder = "In sequential order, separated by whitespace. " +
                            "Non-alphabetical characters are ignored."
                    rows = "5"
                }
                outwardClues.render(this, "Outward clues") {
                    placeholder = "One clue per row. Omit clue numbers."
                    rows = "10"
                }
                inwardCells.render(this, "Inward cells (optional)") {
                    placeholder = "Each cell of the spiral, in sequential order, separated by whitespace. " +
                            "Non-alphabetical characters are ignored. " +
                            "Defaults to each letter of the inward answers. " +
                            "May be used to place more than one letter in some or all cells, " +
                            "e.g. for a Crushword Spiral."
                    rows = "5"
                }
            })
        }
    }

    private fun createPuzzle(): Promise<Puzzle> {
        val spiral = Spiral(
            title = title.getValue(),
            creator = creator.getValue(),
            copyright = copyright.getValue(),
            description = description.getValue(),
            inwardAnswers = inwardAnswers.getValue().split("\\s+".toRegex()),
            inwardClues = inwardClues.getValue().split("\n").map { it.trim() },
            outwardAnswers = outwardAnswers.getValue().split("\\s+".toRegex()),
            outwardClues = outwardClues.getValue().split("\n").map { it.trim() },
            inwardCellsInput = inwardCells.getValue().split("\\s+".toRegex()),
        )
        return Promise.resolve(spiral.asPuzzle())
    }
}