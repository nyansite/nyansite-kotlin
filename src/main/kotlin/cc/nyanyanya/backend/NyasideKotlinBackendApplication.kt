package cc.nyanyanya.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.*


@SpringBootApplication
class NyasideKotlinBackendApplication

fun main(args: Array<String>) {
	TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

	runApplication<NyasideKotlinBackendApplication>(*args)
}
