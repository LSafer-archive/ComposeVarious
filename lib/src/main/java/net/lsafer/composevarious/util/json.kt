/*
 *	Copyright 2021 LSafer
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package net.lsafer.composevarious.util

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import org.cufy.http.body.JSONBody
import org.cufy.http.body.JSONBody.json
import org.cufy.http.request.Request
import org.cufy.http.response.Response

// [ ENCODE / DECODE ]

val jsonFormat = Json { ignoreUnknownKeys = true }

/**
 * ### (String) -> JsonElement
 *
 * Transform the data in the given [json] string into a json-element with type [T].
 *
 * @param json the json source.
 * @param <T> the type of the desired json element.
 * @since 0.0.1 ~2021.08.17
 */
inline fun <reified T : JsonElement> element(json: String): T =
    when (T::class) {
        JsonObject::class -> jsonFormat.parseToJsonElement(json).jsonObject as T
        JsonArray::class -> jsonFormat.parseToJsonElement(json).jsonArray as T
        JsonNull::class -> jsonFormat.parseToJsonElement(json).jsonNull as T
        JsonPrimitive::class -> jsonFormat.parseToJsonElement(json).jsonPrimitive as T
        else -> throw IllegalArgumentException("Unsupported JsonElement type: ${T::class}")
    }

/**
 * ### (Serializable) -> JsonElement
 *
 * Transform the data of the given [serializable] into a json-element with type [U].
 *
 * @param serializable the serializable to transform.
 * @param <T> the type of the [serializable]
 * @param <U> the type of the desired json element.
 * @since 0.0.1 ~2021.08.17
 */
inline fun <reified T, reified U : JsonElement> element(serializable: T): U =
    when (U::class) {
        JsonObject::class -> jsonFormat.encodeToJsonElement(serializable).jsonObject as U
        JsonArray::class -> jsonFormat.encodeToJsonElement(serializable).jsonArray as U
        JsonNull::class -> jsonFormat.encodeToJsonElement(serializable).jsonNull as U
        JsonPrimitive::class -> jsonFormat.encodeToJsonElement(serializable).jsonPrimitive as U
        else -> throw IllegalArgumentException("Unsupported JsonElement type: ${U::class}")
    }

/**
 * ### (JsonElement) -> String
 *
 * Generate a json string from encoding the given [element].
 *
 * @param element the element to be converted.
 * @return a json string from the given [element].
 * @since 0.0.1 ~2021.08.17
 */
fun encode(element: JsonElement): String =
    jsonFormat.encodeToString(element)

/**
 * ### (Serializable) -> String
 *
 * Generate a json string from encoding the given [serializable].
 *
 * @param serializable the object to be converted. (must be annotated with [Serializable])
 * @param T the type of the [serializable].
 * @return a json string from the given [serializable].
 * @since 0.0.1 ~2021.08.17
 */
inline fun <reified T> encode(serializable: T): String =
    jsonFormat.encodeToString(serializable)

/** ### (String) -> Serializable
 *
 * Decode the given [json] string in a new object of type [T]
 *
 * @param json the json source.
 * @param T the type of the returned serializable.
 * @return a new object of type [T] with the data from decoding the given [json] string.
 * @since 0.0.1 ~2021.08.17
 */
inline fun <reified T> decode(json: String): T =
    jsonFormat.decodeFromString(json)

/** ### (JsonElement) -> Serializable
 *
 * Decode the given json [element] in a new object of type [T]
 *
 * @param element the json element.
 * @param T the type of the returned serializable.
 * @return a new object of type [T] with the data from decoding the given [element].
 * @since 0.0.1 ~2021.08.17
 */
inline fun <reified T> decode(element: JsonElement): T =
    jsonFormat.decodeFromJsonElement(element)

/**
 * ### (Serializable) -> Serializable
 *
 * Clone the given [serializable] by encoding into a json string then decoding it again.
 *
 * @param serializable the object to be cloned.
 * @param T the type of the [serializable].
 * @since 0.0.1 ~2021.08.17
 */
inline fun <reified T> clone(serializable: T): T =
    jsonFormat.decodeFromString(jsonFormat.encodeToString(serializable))

/**
 * ### (Serializable) -> Serializable
 *
 * Transform the given [serializable] by encoding into a json string then decoding it again as [U].
 *
 * @param serializable the object to be cloned.
 * @param T the type of the [serializable].
 * @param U the type of the desired serializable output.
 * @since 0.0.1 ~2021.08.17
 */
inline fun <reified T, reified U> transform(serializable: T): U =
    jsonFormat.decodeFromString(jsonFormat.encodeToString(serializable))

// [ REQUEST / RESPONSE ]

var Request<*>.element: JsonObject
    get() {
        return element(body.toString())
    }
    set(value) {
        this.setBody(json(encode(value)))
    }

var Response<*>.element: JsonObject
    get() {
        return element(body.toString())
    }
    set(value) {
        this.setBody(json(encode(value)))
    }

var Request<*>.json: JSONBody
    get() {
        return this.body as JSONBody
    }
    set(value) {
        this.setBody(value)
    }

var Response<*>.json: JSONBody
    get() {
        return this.body as JSONBody
    }
    set(value) {
        this.setBody(value)
    }

// [ Vararg ]

fun json(vararg pairs: Pair<String, Any?>): JSONBody =
    json(mutableMapOf(pairs = pairs))

fun element(vararg pairs: Pair<String, Any?>): JsonObject =
    element(mutableMapOf(pairs = pairs))
