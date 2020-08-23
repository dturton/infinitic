package io.infinitic.taskManager.engine.pulsar.functions

import io.infinitic.taskManager.engine.avroClasses.AvroMonitoringGlobal
import io.infinitic.taskManager.messages.envelopes.AvroEnvelopeForMonitoringGlobal
import io.infinitic.taskManager.engine.pulsar.storage.PulsarAvroStorage
import org.apache.pulsar.functions.api.Context
import org.apache.pulsar.functions.api.Function

class MonitoringGlobalPulsarFunction : Function<AvroEnvelopeForMonitoringGlobal, Void> {

    var monitoring = AvroMonitoringGlobal()

    override fun process(input: AvroEnvelopeForMonitoringGlobal, context: Context?): Void? {
        val ctx = context ?: throw NullPointerException("Null Context received")

        try {
            monitoring.logger = ctx.logger
            monitoring.avroStorage = PulsarAvroStorage(ctx)

            monitoring.handle(input)
        } catch (e: Exception) {
            ctx.logger.error("Error:%s for message:%s", e, input)
            throw e
        }

        return null
    }
}