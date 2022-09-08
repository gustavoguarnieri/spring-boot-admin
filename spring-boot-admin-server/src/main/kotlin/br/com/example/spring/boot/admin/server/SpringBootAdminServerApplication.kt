package br.com.example.spring.boot.admin.server

import de.codecentric.boot.admin.server.config.EnableAdminServer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableAdminServer
@SpringBootApplication
class SpringBootAdminServerApplication

fun main(args: Array<String>) {
	runApplication<SpringBootAdminServerApplication>(*args)
}
