package com.kevin.storyappdicoding.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target


class PicassoMarker(private val mMarker: Marker) : Target {
    override fun hashCode(): Int {
        return mMarker.hashCode()
    }

    override fun equals(o: Any?): Boolean {
        return if (o is PicassoMarker) {
            val marker: Marker = o.mMarker
            mMarker == marker
        } else {
            false
        }
    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap!!))
    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
        mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(drawableToBitmap(errorDrawable!!)!!))
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(drawableToBitmap(placeHolderDrawable!!)!!))
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            if (drawable.bitmap != null) {
                return drawable.bitmap
            }
        }
        val bitmap: Bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(
                1,
                1,
                Bitmap.Config.ARGB_8888
            ) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}