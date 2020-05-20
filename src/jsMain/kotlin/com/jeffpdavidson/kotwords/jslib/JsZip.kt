package com.jeffpdavidson.kotwords.jslib

import org.khronos.webgl.Uint8Array
import kotlin.js.Promise
import kotlin.js.RegExp

@JsModule("jszip")
@JsNonModule
external class JSZip {
    fun file(regex: RegExp): Array<ZipObject>
    fun file(name: String, data: String): JSZip
    fun loadAsync(data: Uint8Array): Promise<JSZip>
    fun generateAsync(options: GenerateAsyncOptions): Promise<Any>
}

external class ZipObject {
    fun async(type: String): Promise<Any?>
}

enum class ZipOutputType(val jsValue: String) {
    BASE64("base64")
}

enum class ZipOutputCompression(val jsValue: String) {
    DEFLATE("deflate")
}

external interface GenerateAsyncOptions {
    var type: String
    var compression: String
}

fun newGenerateAsyncOptions(
        type: ZipOutputType = ZipOutputType.BASE64,
        compression: ZipOutputCompression = ZipOutputCompression.DEFLATE): GenerateAsyncOptions {
    val options = js("{}").unsafeCast<GenerateAsyncOptions>()
    options.type = type.jsValue
    options.compression = compression.jsValue
    return options
}