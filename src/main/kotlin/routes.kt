external fun require(module:String):dynamic

val crypto = require("crypto")
val jsHelpers = require("../js/jshelpers")

fun main(args: Array<String>) {
    println("Hello JavaScript!")

    val express = require("express")
    val app = express()
    val expressHandlebars = require("express-handlebars")
    val hbs = expressHandlebars.create()
    val request = require("request")
    app.engine("handlebars", hbs.engine)
    app.set("view engine', 'handlebars")

    app.get("/", { req, res ->
        res.type("text/plain")
        res.render("test.handlebars")
    })

    app.get("/playlist/master", { req, res ->
        res.setHeader("Content-Type", "application/x-mpegURL")
        res.render("master.m3u.handlebars")
    })

    app.get("/playlist", { req, res ->
        res.setHeader("Content-Type", "application/x-mpegURL")
        res.render("m3u.handlebars", Factory.audiobook)
    })

    app.get("/playlist/chapters/:chapterID/stream.mp3", { req, res ->
        val chapter = Factory.audiobook.chapters[req.params.chapterID]
        val cipher = crypto.createCipheriv("aes128", chapter.key, chapter.iv);
        request
                .get(chapter.url)
                .pipe(cipher)
                .pipe(res)
    })

    app.get("/playlist/chapters/:chapterId/key", {req, res ->
        val chapter = Factory.audiobook.chapters[req.params.chapterId]
        res.setHeader("Content-Type", "binary/octet-stream")
        res.send(chapter.key)
    })

    app.listen(3000, {
        println("Listening on port 3000")
    })
}

object Factory {
    val audiobook : Audiobook by lazy {

        val chap1 = Chapter(1, 290,
                generateCrypto(), generateCrypto(),
                "https://ia800504.us.archive.org/24/items/Item548HowToGoProWithAnEntryLevelDSLR/OMHT548.mp3")
        val chap2 = Chapter(2, 215,
                generateCrypto(), generateCrypto(),
                "https://ia800500.us.archive.org/32/items/Item335HowToMeditate/OMHT335.mp3")
        return@lazy Audiobook(290 + 215, arrayOf(chap1, chap2))
    }

    private fun generateCrypto() : ByteArray = crypto.randomBytes(16)
}

data class Audiobook(val duration: Long, val chapters: Array<Chapter>)

data class Chapter(val index: Int, val duration: Long, val key: ByteArray, val iv: ByteArray, val url: String) {
    fun ivPresentation() : String = jsHelpers.toHexString(iv)
}
