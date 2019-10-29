package com.inspetor

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

class InspetorProvider: ContentProvider() {
    override fun onCreate(): Boolean {
        Inspetor.sharedInstance().setContextWithoutConfig(
            this.context?.applicationContext
        )

        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }

}