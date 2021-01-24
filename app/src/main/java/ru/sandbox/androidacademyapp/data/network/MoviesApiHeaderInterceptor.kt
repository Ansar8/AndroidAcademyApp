package ru.sandbox.androidacademyapp.data.network

import okhttp3.Interceptor
import okhttp3.Response
import ru.sandbox.androidacademyapp.BuildConfig

class MoviesApiHeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("api_key", BuildConfig.API_KEY)
            .build()

        val request = originalRequest.newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}