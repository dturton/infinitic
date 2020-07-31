package com.zenaton.jobManager.common.data

import com.zenaton.jobManager.common.utils.TestFactory
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.nio.ByteBuffer

internal class JobMetaBuilderTests : StringSpec({
    "JobMetaBuiler should build correct JobMeta" {
        // given
        val str = TestFactory.random(String::class)
        val bytes = TestFactory.random(ByteArray::class)
        val buffer = TestFactory.random(ByteBuffer::class)
        // when
        val out = JobMeta.builder()
            .add("key1", str)
            .add("key2", bytes)
            .add("key3", buffer)
            .build()
        // then
        out.meta.size shouldBe 3
        out.meta["key1"]?.deserialize<String>() shouldBe str
        out.meta["key2"]?.deserialize<ByteArray>() shouldBe bytes
        ByteBuffer.wrap(out.meta["key3"]?.deserialize<ByteArray>()) shouldBe buffer
    }
})
