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
package net.lsafer.composevarious.composable

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.NoOpUpdate
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import net.lsafer.composevarious.R
import net.lsafer.composevarious.databinding.GmapsBinding

@Composable
fun GoogleMapComposable(
    modifier: Modifier = Modifier,
    onMapInit: ((GoogleMap) -> Unit)? = {},
    onMapLoaded: ((GoogleMap) -> Unit)? = null,
    onUpdate: (View) -> Unit = NoOpUpdate
) {
    val activity = LocalContext.current as AppCompatActivity

    val fragment = remember {
        GmapsBinding.inflate(activity.layoutInflater)

        val fragment = activity.supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        if (onMapInit != null)
            fragment.getMapAsync(onMapInit)

        fragment
    }

    if (onMapLoaded != null)
        fragment.getMapAsync(onMapLoaded)

    AndroidView(
        modifier = modifier,
        factory = { fragment.requireView() },
        update = onUpdate
    )
}
