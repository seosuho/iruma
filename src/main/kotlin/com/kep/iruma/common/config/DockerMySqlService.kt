package com.kep.iruma.common.config

import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.DockerClient
import com.spotify.docker.client.exceptions.ContainerNotFoundException
import com.spotify.docker.client.messages.ContainerConfig
import com.spotify.docker.client.messages.HostConfig
import com.spotify.docker.client.messages.PortBinding
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.Lifecycle
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Configuration
class MysqlServiceConfig {

    @Bean("mysqlService")
    @Profile("local", "test")
    fun dockerMysqlService(): MysqlService = DockerMysqlService()

    @Bean("mysqlService")
    @ConditionalOnMissingBean(MysqlService::class)
    fun dumbMysqlService(): MysqlService = object : MysqlService {}
}

interface MysqlService

class DockerMysqlService(
    private val client: DockerClient = DefaultDockerClient.fromEnv().build()
) : MysqlService, Lifecycle {
    private var isRunning = false
    private var isAlive = false
    private val containerName = "iruma-db"
    private val image = "mysql:5.7"
    private val hostPort = "3308"
    private val mysqlPort = "3306"

    override fun isRunning(): Boolean {
        return isRunning && isAlive
    }

    @PostConstruct
    override fun start() {
        try {
            client.inspectContainer(containerName).apply { isRunning = state().running() }
        } catch (e: ContainerNotFoundException) {
            client.pull(image)
            val hostConfig = HostConfig.builder()
                .portBindings(mapOf(mysqlPort to listOf(PortBinding.of("0.0.0.0", hostPort))))
                .build()

            val containerConfig = ContainerConfig.builder()
                .hostConfig(hostConfig)
                .image(image)
                .exposedPorts(mysqlPort)
                .env("MYSQL_DATABASE=iruma", "MYSQL_ROOT_PASSWORD=root")
                .cmd("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci")
                .build()
            client.createContainer(containerConfig, containerName)
        }

        if (!isRunning) {
            client.startContainer(containerName)
            isRunning = true
        }

        var count = 0

        while (!isAlive) {
            isAlive = pingDb()
            Thread.sleep(3000)
            if (count > 5) {
                throw RuntimeException("Cannot start the mysqld container")
            }
            count += 1
        }
    }

    @PreDestroy
    override fun stop() {
        // client.stopContainer(containerName, 1)
    }

    private fun pingDb(): Boolean {
        return client.execCreate(containerName, arrayOf("mysqladmin", "ping", "-h", "localhost", "-proot"),
            DockerClient.ExecCreateParam.attachStdout(),
            DockerClient.ExecCreateParam.attachStderr()
        ).let {
            client.execStart(it.id()).readFully().contains("mysqld is alive")
        }
    }
}

