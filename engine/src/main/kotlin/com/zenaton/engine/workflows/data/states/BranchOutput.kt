package com.zenaton.engine.workflows.data.states

import com.zenaton.engine.interfaces.data.DataInterface

data class BranchOutput(override val data: ByteArray) : DataInterface {
    final override fun equals(other: Any?) = equalsData(other)
    final override fun hashCode() = hashCodeData()
}