external fun require(module:String):dynamic

fun main(args: Array<String>) {
    println("Hello JavaScript!")

    val express = require("express")
    val app = express()
    val expressHandlebars = require("express-handlebars")
    val hbs = expressHandlebars.create()
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
        res.render("m3u.handlebars", Factory.audiobook())
    })

    app.listen(3000, {
        println("Listening on port 3000")
    })
}

object Factory {
    fun audiobook() : Audiobook{
        val chap1 = Chapter(1, 290, "https://ia800504.us.archive.org/24/items/Item548HowToGoProWithAnEntryLevelDSLR/OMHT548.mp3" )
        val chap2 = Chapter(2, 215, "https://ia800500.us.archive.org/32/items/Item335HowToMeditate/OMHT335.mp3")
        return Audiobook(290 + 215, arrayOf(chap1, chap2))
    }
}

data class Audiobook(val duration: Long, val chapters: Array<Chapter>)

data class Chapter(val index: Int, val duration: Long, val url: String)
