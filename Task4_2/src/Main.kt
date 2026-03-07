import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.io.File
import java.security.MessageDigest

fun findJsonFiles(path: String): List<File> {
    val root = File(path)

    if (!root.exists() || !root.isDirectory) {
        return emptyList()
    }

    return root.walkTopDown()
        .filter { it.isFile && it.extension.equals("json", ignoreCase = true) }
        .toList()
}

suspend fun calculateSHA256(file: File): String = withContext(Dispatchers.IO) {
    val digest = MessageDigest.getInstance("SHA-256")
    val bytes = file.readBytes()
    val hash = digest.digest(bytes)
    hash.joinToString("") { "%02x".format(it) }
}

fun main() {
    val path = "src/json"
    val n = 10L

    val result = runBlocking {
        withTimeoutOrNull(n * 1000) {
            val files = findJsonFiles(path)

            val jobs = files.map { file ->
                async {
                    file to calculateSHA256(file)
                }
            }

            jobs.awaitAll()
        }
    }

    if (result == null) {
        println("Поиск прерван по таймауту")
        return
    }

    val duplicates = result
        .groupBy({ it.second }, { it.first })
        .filterValues { it.size > 1 }

    if (duplicates.isEmpty()) {
        println("Дубликаты не найдены")
    } else {
        println("Группы дубликатов:")
        duplicates.forEach { (hash, files) ->
            println("\nHash: $hash")
            files.forEach { println(it.absolutePath) }
        }
    }
}