/*
 * Copyright (c) 2022, Valaphee.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.valaphee.service.minecraftservices.gatherings

import com.fasterxml.jackson.annotation.JsonProperty
import com.valaphee.service.minecraftservices.discovery.DiscoveryResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import java.util.UUID

/**
 * @author Kevin Ludwig
 */
data class VenueResponse(
    @JsonProperty("result") val result: Result
) {
    data class Result(
        @JsonProperty("venue") val venue: Venue
    ) {
        data class Venue(
            @JsonProperty("serverIpAddress") val serverIpAddress: String,
            @JsonProperty("serverPort") val serverPort: Int
        )
    }
}

suspend fun HttpClient.venue(uri: String = "https://gatherings.franchise.minecraft-services.net", authorization: String, id: UUID) = get("$uri/api/v1.0/venue/$id") { header(HttpHeaders.Authorization, authorization) }.body<DiscoveryResponse>()