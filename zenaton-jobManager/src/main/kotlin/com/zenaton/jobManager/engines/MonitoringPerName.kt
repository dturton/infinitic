package com.zenaton.jobManager.engines

import com.zenaton.jobManager.data.JobStatus
import com.zenaton.jobManager.dispatcher.Dispatcher
import com.zenaton.jobManager.messages.ForMonitoringPerNameMessage
import com.zenaton.jobManager.messages.JobCreated
import com.zenaton.jobManager.messages.JobStatusUpdated
import com.zenaton.jobManager.states.MonitoringPerNameState
import com.zenaton.jobManager.storages.MonitoringPerNameStorage
import org.slf4j.Logger

class MonitoringPerName {
    lateinit var logger: Logger
    lateinit var storage: MonitoringPerNameStorage
    lateinit var dispatcher: Dispatcher

    fun handle(message: ForMonitoringPerNameMessage) {

        // get associated state
        val oldState = storage.getState(message.jobName)
        val newState = oldState?.deepCopy() ?: MonitoringPerNameState(message.jobName)

        when (message) {
            is JobStatusUpdated -> handleTaskStatusUpdated(message, newState)
        }

        // Update stored state if needed and existing
        if (newState != oldState) {
            storage.updateState(message.jobName, newState, oldState)
        }

        // It's a new task type
        if (oldState == null) {
            val tsc = JobCreated(jobName = message.jobName)

            dispatcher.toMonitoringGlobal(tsc)
        }
    }

    private fun handleTaskStatusUpdated(message: JobStatusUpdated, state: MonitoringPerNameState) {
        when (message.oldStatus) {
            JobStatus.RUNNING_OK -> state.runningOkCount--
            JobStatus.RUNNING_WARNING -> state.runningWarningCount--
            JobStatus.RUNNING_ERROR -> state.runningErrorCount--
            JobStatus.TERMINATED_COMPLETED -> state.terminatedCompletedCount--
            JobStatus.TERMINATED_CANCELED -> state.terminatedCanceledCount--
            else -> Unit
        }

        when (message.newStatus) {
            JobStatus.RUNNING_OK -> state.runningOkCount++
            JobStatus.RUNNING_WARNING -> state.runningWarningCount++
            JobStatus.RUNNING_ERROR -> state.runningErrorCount++
            JobStatus.TERMINATED_COMPLETED -> state.terminatedCompletedCount++
            JobStatus.TERMINATED_CANCELED -> state.terminatedCanceledCount++
        }
    }
}
