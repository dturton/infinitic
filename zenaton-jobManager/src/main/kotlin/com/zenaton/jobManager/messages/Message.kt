package com.zenaton.jobManager.messages

import com.zenaton.jobManager.data.JobAttemptError
import com.zenaton.jobManager.data.JobAttemptId
import com.zenaton.jobManager.data.JobAttemptIndex
import com.zenaton.jobManager.data.JobAttemptRetry
import com.zenaton.jobManager.data.JobId
import com.zenaton.jobManager.data.JobInput
import com.zenaton.jobManager.data.JobName
import com.zenaton.jobManager.data.JobOutput
import com.zenaton.jobManager.data.JobStatus
import com.zenaton.jobManager.data.WorkflowId
import com.zenaton.jobManager.messages.envelopes.ForJobEngineMessage
import com.zenaton.jobManager.messages.envelopes.ForMonitoringGlobalMessage
import com.zenaton.jobManager.messages.envelopes.ForMonitoringPerNameMessage
import com.zenaton.jobManager.messages.envelopes.ForWorkerMessage
import com.zenaton.jobManager.messages.envelopes.ForWorkflowEngineMessage
import com.zenaton.jobManager.messages.interfaces.FailingJobAttemptMessage
import com.zenaton.jobManager.messages.interfaces.JobAttemptMessage

sealed class Message

data class CancelJob(
    override val jobId: JobId
) : Message(), ForJobEngineMessage

data class DispatchJob(
    override val jobId: JobId,
    val jobName: JobName,
    val jobInput: JobInput,
    val workflowId: WorkflowId? = null
) : Message(), ForJobEngineMessage

data class JobAttemptCompleted(
    override val jobId: JobId,
    override val jobAttemptId: JobAttemptId,
    override val jobAttemptRetry: JobAttemptRetry,
    override val jobAttemptIndex: JobAttemptIndex,
    val jobOutput: JobOutput?
) : Message(), JobAttemptMessage, ForJobEngineMessage

data class JobAttemptDispatched(
    override val jobId: JobId,
    override val jobAttemptId: JobAttemptId,
    override val jobAttemptRetry: JobAttemptRetry,
    override val jobAttemptIndex: JobAttemptIndex
) : Message(), JobAttemptMessage, ForJobEngineMessage

data class JobAttemptFailed(
    override val jobId: JobId,
    override val jobAttemptId: JobAttemptId,
    override val jobAttemptRetry: JobAttemptRetry,
    override val jobAttemptIndex: JobAttemptIndex,
    override val jobAttemptDelayBeforeRetry: Float?,
    val jobAttemptError: JobAttemptError
) : Message(), FailingJobAttemptMessage, ForJobEngineMessage

data class JobAttemptStarted(
    override val jobId: JobId,
    override val jobAttemptId: JobAttemptId,
    override val jobAttemptRetry: JobAttemptRetry,
    override val jobAttemptIndex: JobAttemptIndex
) : Message(), JobAttemptMessage, ForJobEngineMessage

data class JobCanceled(
    override val jobId: JobId
) : Message(), ForJobEngineMessage

data class JobCompleted(
    override val jobId: JobId,
    val jobOutput: JobOutput?
) : Message(), ForJobEngineMessage

data class JobCreated(
    val jobName: JobName
) : Message(), ForMonitoringGlobalMessage

data class JobStatusUpdated constructor(
    override val jobName: JobName,
    val jobId: JobId,
    val oldStatus: JobStatus?,
    val newStatus: JobStatus
) : Message(), ForMonitoringPerNameMessage

data class RetryJob(
    override val jobId: JobId
) : Message(), ForJobEngineMessage

data class RetryJobAttempt(
    override val jobId: JobId,
    override val jobAttemptId: JobAttemptId,
    override val jobAttemptRetry: JobAttemptRetry,
    override val jobAttemptIndex: JobAttemptIndex
) : Message(), JobAttemptMessage, ForJobEngineMessage

data class RunJob(
    override val jobName: JobName,
    override val jobId: JobId,
    override val jobAttemptId: JobAttemptId,
    override val jobAttemptRetry: JobAttemptRetry,
    override val jobAttemptIndex: JobAttemptIndex,
    val jobInput: JobInput
) : Message(), JobAttemptMessage, ForWorkerMessage

data class TaskCompleted(
    override val workflowId: WorkflowId,
    val taskId: JobId,
    val taskOutput: JobOutput?
) : Message(), ForWorkflowEngineMessage
