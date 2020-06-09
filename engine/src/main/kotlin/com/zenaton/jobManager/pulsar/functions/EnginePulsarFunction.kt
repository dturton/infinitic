package com.zenaton.jobManager.pulsar.functions

import com.zenaton.jobManager.functions.EngineFunction
import com.zenaton.jobManager.messages.envelopes.AvroForEngineMessage
import com.zenaton.jobManager.pulsar.dispatcher.PulsarAvroDispatcher
import com.zenaton.jobManager.pulsar.storage.PulsarAvroStorage
import com.zenaton.workflowengine.pulsar.topics.workflows.dispatcher.WorkflowDispatcher
import org.apache.pulsar.functions.api.Context
import org.apache.pulsar.functions.api.Function

class EnginePulsarFunction : Function<AvroForEngineMessage, Void> {

    var engine = EngineFunction()

    override fun process(input: AvroForEngineMessage, context: Context?): Void? {
        val ctx = context ?: throw NullPointerException("Null Context received")

        try {
            engine.logger = ctx.logger
            engine.avroStorage = PulsarAvroStorage(ctx)
            engine.avroDispatcher = PulsarAvroDispatcher(ctx)
            engine.workflowDispatcher = WorkflowDispatcher(ctx)

            engine.handle(input)
        } catch (e: Exception) {
            ctx.logger.error("Error:%s for message:%s", e, input)
            throw e
        }

        return null
    }
}