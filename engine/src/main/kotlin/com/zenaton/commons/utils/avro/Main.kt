package com.zenaton.commons.utils.avro

import com.zenaton.commons.utils.json.Json
import com.zenaton.taskManager.engine.messages.AvroTaskEngineMessage
import com.zenaton.taskManager.metrics.messages.AvroMonitoringPerNameMessage
import com.zenaton.taskManager.workers.AvroTaskWorkerMessage
import java.io.File
import kotlin.reflect.KClass
import org.apache.avro.specific.SpecificRecordBase
import org.apache.pulsar.client.impl.schema.AvroSchema

/**
 * This class creates files in /build/schemas, used to upload schemas to topics
 */
fun main() {
    class Schema(klass: KClass<out SpecificRecordBase>) {
        val type = "AVRO"
        val properties = mapOf(
            "__alwaysAllowNull" to "true",
            "__jsr310ConversionEnabled" to "false"
        )
        val schema = AvroSchema.of(klass.java).avroSchema.toString()
    }

    listOf(
        AvroTaskEngineMessage::class,
        AvroTaskWorkerMessage::class,
        AvroMonitoringPerNameMessage::class
    ).map { klass ->
        File(System.getProperty("user.dir") + "/build/schemas/${klass.simpleName}.schema")
            .also { it.parentFile.mkdirs() }
            .writeText(Json.stringify(Schema(klass)))
    }
}
