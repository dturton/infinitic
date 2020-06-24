package com.zenaton.workflowManager.data.branches

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.zenaton.common.data.SerializedData

data class BranchInput
@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
constructor(@get:JsonValue val input: List<SerializedData>)
