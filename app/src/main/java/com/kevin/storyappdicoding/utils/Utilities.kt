package com.kevin.storyappdicoding.utils

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import com.google.android.material.textfield.TextInputLayout
import com.kevin.storyappdicoding.R
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
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

    val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())

    fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    fun createFile(application: Application): File {
        val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
            File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        val outputDirectory = if (
            mediaDir != null && mediaDir.exists()
        ) mediaDir else application.filesDir

        return File(outputDirectory, "$timeStamp.jpg")
    }

}