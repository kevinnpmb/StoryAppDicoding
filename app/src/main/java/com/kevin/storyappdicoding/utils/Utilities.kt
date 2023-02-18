package com.kevin.storyappdicoding.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import com.google.android.material.textfield.TextInputLayout
import com.kevin.storyappdicoding.R
import com.squareup.picasso.Picasso
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


object Utilities {
    private const val FILENAME_FORMAT = "dd-MMM-yyyy"

    fun TextInputLayout.validate(
        context: Context,
        title: String,
    ): Boolean {
        return when {
            editText?.text.isNullOrEmpty() -> {
                error = context.getString(R.string.is_empty, title)
                false
            }
            !error.isNullOrBlank() -> {
                false
            }
            else -> {
                error = null
                true
            }
        }
    }

    fun TextInputLayout.registerValidateIfEmpty(context: Context, title: String) {
        val clearTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                error = if (p0.toString().isBlank()) context.getString(
                    R.string.is_empty,
                    title
                ) else null
            }

            override fun afterTextChanged(p0: Editable?) {}
        }
        editText?.addTextChangedListener(clearTextChangedListener)
    }

    fun ImageView.setImageResource(url: String?) {
        if (!url.isNullOrBlank()) {
            Picasso.get().load(url).error(R.drawable.ic_no_images).into(this)
        } else {
            setImageResource(R.drawable.ic_no_images)
        }
    }

    fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    private val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())

    fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }
}