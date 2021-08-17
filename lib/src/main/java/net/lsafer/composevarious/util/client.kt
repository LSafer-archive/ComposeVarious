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

import org.cufy.http.body.Body
import org.cufy.http.connect.Action
import org.cufy.http.connect.Callback
import org.cufy.http.connect.Client
import org.cufyx.http.connect.XClient

//infix actions

infix fun <T> Action<in T>.or(
    action: Action<in T>
): Action<T> =
    object : Action<T> {
        override fun test(trigger: String, parameter: Any?): Boolean {
            return this@or.test(trigger, parameter) ||
                    action.test(trigger, parameter)
        }

        override fun triggers(): MutableSet<String> {
            val set = HashSet(this@or.triggers())
            set.addAll(action.triggers())
            return set
        }
    }

//infix callbacks

@Suppress("UNCHECKED_CAST", "NULLABLE_TYPE_PARAMETER_AGAINST_NOT_NULL_TYPE_PARAMETER")
infix fun <T, U> Callback<in T, in U>.then(
    callback: Callback<in T, in U>?
): Callback<T, U> =
    Callback { t, u ->
        this@then.call(t, u)
        callback?.call(t, u)
    }

//xClient redirects

@Suppress("UNCHECKED_CAST")
fun <B : Body, T> XClient<B>.on(
    action: Action<T>,
    callback: Callback<in XClient<B>, in T>
): XClient<B> =
    (this as Client<B>).on(action, callback as Callback<Client<B>, T>) as XClient<B>

@Suppress("UNCHECKED_CAST")
fun <B : Body, T> XClient<B>.ont(
    action: Action<T>,
    callback: Callback<in XClient<B>, in T>
): XClient<B> =
    (this as Client<B>).ont(action, callback as Callback<Client<B>, T>) as XClient<B>

@Suppress("UNCHECKED_CAST")
fun <B : Body, T> XClient<B>.onh(
    action: Action<T>,
    callback: Callback<in XClient<B>, in T>
): XClient<B> =
    this.onh(action, callback as Callback<XClient<B>, T>)
