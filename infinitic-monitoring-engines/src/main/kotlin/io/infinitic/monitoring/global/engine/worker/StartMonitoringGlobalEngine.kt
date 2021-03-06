/**
 * "Commons Clause" License Condition v1.0
 *
 * The Software is provided to you by the Licensor under the License, as defined
 * below, subject to the following condition.
 *
 * Without limiting other conditions in the License, the grant of rights under the
 * License will not include, and the License does not grant to you, the right to
 * Sell the Software.
 *
 * For purposes of the foregoing, “Sell” means practicing any or all of the rights
 * granted to you under the License to provide to third parties, for a fee or
 * other consideration (including without limitation fees for hosting or
 * consulting/ support services related to the Software), a product or service
 * whose value derives, entirely or substantially, from the functionality of the
 * Software. Any license notice or attribution required by the License must also
 * include this Commons Clause License Condition notice.
 *
 * Software: Infinitic
 *
 * License: MIT License (https://opensource.org/licenses/MIT)
 *
 * Licensor: infinitic.io
 */

package io.infinitic.monitoring.global.engine.worker

import io.infinitic.monitoring.global.engine.MonitoringGlobalEngine
import io.infinitic.monitoring.global.engine.storage.MonitoringGlobalStateStorage
import io.infinitic.monitoring.global.engine.transport.MonitoringGlobalInputChannels
import io.infinitic.monitoring.global.engine.transport.MonitoringGlobalMessageToProcess
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val logger: Logger
    get() = LoggerFactory.getLogger(MonitoringGlobalEngine::class.java)

private fun logError(messageToProcess: MonitoringGlobalMessageToProcess, e: Exception) = logger.error(
    "exception on message {}:${System.getProperty("line.separator")}{}",
    messageToProcess.message,
    e
)

fun <T : MonitoringGlobalMessageToProcess> CoroutineScope.startMonitoringGlobalEngine(
    coroutineName: String,
    monitoringGlobalStateStorage: MonitoringGlobalStateStorage,
    monitoringGlobalInputChannels: MonitoringGlobalInputChannels<T>
) = launch(CoroutineName(coroutineName)) {

    val monitoringGlobalEngine = MonitoringGlobalEngine(
        monitoringGlobalStateStorage
    )

    val out = monitoringGlobalInputChannels.monitoringGlobalResultsChannel

    for (message in monitoringGlobalInputChannels.monitoringGlobalChannel) {
        try {
            message.output = monitoringGlobalEngine.handle(message.message)
        } catch (e: Exception) {
            message.exception = e
            logError(message, e)
        } finally {
            out.send(message)
        }
    }
}
