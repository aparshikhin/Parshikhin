package com.example.devlife.model.models

class LoadData<T>(val status: DataStatus, val data: T?, val error: Throwable?) {

    companion object {
        fun <T> loading(): LoadData<T> {
            return LoadData(
                status = DataStatus.Loading,
                data = null,
                error = null
            )
        }

        fun <T> data(data: T): LoadData<T> {
            return LoadData(
                status = DataStatus.Successful,
                data = data,
                error = null
            )
        }

        fun <T> error(error: Throwable): LoadData<T> {
            return LoadData(
                status = DataStatus.Error,
                data = null,
                error = error
            )
        }
    }
}