package io.infinitic.workflowManager.engine.storages

import io.infinitic.workflowManager.common.data.workflows.WorkflowId
import io.infinitic.workflowManager.common.states.WorkflowState

open class InMemoryWorkflowStateStorage : WorkflowStateStorage {
    private var workflowStateStore: MutableMap<String, WorkflowState> = mutableMapOf()

    override fun createState(workflowId: WorkflowId, state: WorkflowState) {
        workflowStateStore["$workflowId"] = state
    }

    override fun getState(workflowId: WorkflowId) = workflowStateStore["$workflowId"]

    override fun updateState(workflowId: WorkflowId, state: WorkflowState) {
        workflowStateStore["$workflowId"] = state
    }

    override fun deleteState(workflowId: WorkflowId) {
        workflowStateStore.remove("$workflowId")
    }

    fun reset() {
        workflowStateStore.clear()
    }
}