package org.onap.dcae.common.publishing;

import io.vavr.collection.Map;
import org.onap.dcae.common.model.VesEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.List;

/*
 * ============LICENSE_START=======================================================
 * VES Collector
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * Copyright (C) 2018 - 2021 Nokia. All rights reserved.
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

public interface EventPublisher {

    Logger log = LoggerFactory.getLogger(EventPublisher.class);

    HttpStatus sendEvent(List<VesEvent> vesEvents, String dmaapId);

    default io.vavr.collection.List<String> mapListOfEventsToVavrList(List<VesEvent> vesEvents) {
        return io.vavr.collection.List.ofAll(vesEvents)
                .map(event -> event.asJsonObject().toString());
    }

    default void clearVesUniqueIdFromEvent(List<VesEvent> events) {
        events.stream()
                .filter(event -> event.hasType(VesEvent.VES_UNIQUE_ID))
                .forEach(event -> {
                    EventPublisher.log.debug("Removing VESuniqueid object from event");
                    event.removeElement(VesEvent.VES_UNIQUE_ID);
                });
    }
}
