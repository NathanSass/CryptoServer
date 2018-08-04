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

    app.listen(3000, {
        println("Listening on port 3000")
    })
}