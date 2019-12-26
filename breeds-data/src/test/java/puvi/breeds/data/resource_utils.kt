package puvi.breeds.data

import java.io.File

object ClassLoaderHelper

fun getJson(path: String): String {
    val uri = ClassLoaderHelper::class.java.classLoader!!.getResource(path)
    val file = File(uri.path)
    return String(file.readBytes())
}
