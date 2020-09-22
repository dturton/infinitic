package io.infinitic.workflowManager.common.data.commands

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.infinitic.workflowManager.common.data.methodRuns.MethodPosition
import io.infinitic.workflowManager.common.data.workflows.WorkflowChangeCheckMode

data class PastCommand(
    @JsonProperty("position")
    val methodPosition: MethodPosition,
    val commandType: CommandType,
    val commandId: CommandId,
    val commandHash: CommandHash,
    val commandSimpleName: CommandSimpleName,
    var commandStatus: CommandStatus
) {

    @JsonIgnore
    fun isTerminated() = this.commandStatus is CommandStatusCompleted || this.commandStatus is CommandStatusCanceled

    fun isSimilarTo(newCommand: NewCommand, mode: WorkflowChangeCheckMode): Boolean =
        newCommand.commandMethodPosition == methodPosition &&
            when (mode) {
                WorkflowChangeCheckMode.NONE ->
                    true
                WorkflowChangeCheckMode.SIMPLE_NAME_ONLY ->
                    newCommand.commandType == commandType && newCommand.commandSimpleName == commandSimpleName
                WorkflowChangeCheckMode.ALL ->
                    newCommand.commandHash == commandHash
            }
}
