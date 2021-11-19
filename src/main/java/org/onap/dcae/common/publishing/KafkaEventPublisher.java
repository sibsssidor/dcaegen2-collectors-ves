/*-
 * ============LICENSE_START=======================================================
 * org.onap.dcaegen2.collectors.ves
 * ================================================================================
 * Copyright (C) 2017,2020 AT&T Intellectual Property. All rights reserved.
 * Copyright (C) 2018-2021 Nokia. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.dcae.common.publishing;

import io.vavr.collection.Map;
import org.onap.dcae.common.model.VesEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;

/**
 *
 */
@Component
public class KafkaEventPublisher implements EventPublisher {
    private Map<String, PublisherConfig> dMaaPConfig;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private ListenableFuture<SendResult<String, String>> send(String topic, String payload) {
        log.info("sending payload='{}' to topic='{}'", payload, topic);
        return kafkaTemplate.send(topic, payload);
    }

    @Override
    public HttpStatus sendEvent(List<VesEvent> vesEvents, String dmaapId) {
        clearVesUniqueIdFromEvent(vesEvents);
        io.vavr.collection.List<String> eventStringList = mapListOfEventsToVavrList(vesEvents);
        eventStringList.toStream().forEach(event -> this.send(dmaapId, event));
        return HttpStatus.ACCEPTED;
    }

}
