/*
 * Copyright 2017 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.q.memory

import com.netflix.spinnaker.q.DeadMessageCallback
import com.netflix.spinnaker.q.QueueTest
import com.netflix.spinnaker.q.metrics.EventPublisher
import com.netflix.spinnaker.q.metrics.MonitorableQueueTest
import com.netflix.spinnaker.q.metrics.QueueEvent
import org.funktionale.partials.invoke
import java.time.Clock

object InMemoryQueueTest : QueueTest<InMemoryQueue>(createQueue(p3 = null))

object InMemoryMonitorableQueueTest : MonitorableQueueTest<InMemoryQueue>(
  createQueue,
  InMemoryQueue::retry
)

private val createQueue = { clock: Clock,
                            deadLetterCallback: DeadMessageCallback,
                            publisher: EventPublisher? ->
  InMemoryQueue(
    clock = clock,
    deadMessageHandlers = listOf(deadLetterCallback),
    publisher = publisher ?: (object : EventPublisher {
      override fun publishEvent(event: QueueEvent) {}
    })
  )
}
