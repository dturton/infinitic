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

package io.infinitic.client.proxies

import io.infinitic.client.transport.ClientOutput
import io.infinitic.common.data.methods.MethodInput
import io.infinitic.common.data.methods.MethodName
import io.infinitic.common.data.methods.MethodParameterTypes
import io.infinitic.common.proxies.MethodProxyHandler
import io.infinitic.common.tasks.data.TaskId
import io.infinitic.common.tasks.data.TaskMeta
import io.infinitic.common.tasks.data.TaskName
import io.infinitic.common.tasks.data.TaskOptions
import io.infinitic.common.tasks.engine.messages.DispatchTask
import io.infinitic.common.tasks.exceptions.NoMethodCall
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future

internal class TaskProxyHandler<T : Any>(
    private val klass: Class<T>,
    private val taskOptions: TaskOptions,
    private val taskMeta: TaskMeta,
    private val clientOutput: ClientOutput
) : MethodProxyHandler<T>(klass) {

    /*
     * Start a task
     */
    fun startTaskAsync(): String {
        // throw error if no method called
        if (method == null) throw NoMethodCall(klass.name, "async")

        val msg = DispatchTask(
            taskId = TaskId(),
            taskName = TaskName.from(method!!),
            methodName = MethodName.from(method!!),
            methodParameterTypes = MethodParameterTypes.from(method!!),
            methodInput = MethodInput.from(method!!, args),
            workflowId = null,
            methodRunId = null,
            taskOptions = taskOptions,
            taskMeta = taskMeta
        )
        GlobalScope.future { clientOutput.sendToTaskEngine(msg, 0F) }.join()

        // reset method to allow reuse of the stub
        reset()

        return "${msg.taskId}"
    }
}
