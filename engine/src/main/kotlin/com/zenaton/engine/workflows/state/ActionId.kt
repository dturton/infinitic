package com.zenaton.engine.workflows.state

import java.util.UUID

data class ActionId(val uuid: String = UUID.randomUUID().toString())
