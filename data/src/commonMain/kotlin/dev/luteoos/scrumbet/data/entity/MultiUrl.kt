package dev.luteoos.scrumbet.data.entity

data class MultiUrl(
    private val base: String
) {
    val httpSchema: String = "http://$base"
    val appSchema: String = "app://$base"

    init {
//        "(\\b(https?|ftp|file)://)?[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]".toRegex()
//            .matches(base).let {
//                if(!it)
//                    throw Exception("Invalid URL in MultiUrl.init()")
//            }
    }
}
