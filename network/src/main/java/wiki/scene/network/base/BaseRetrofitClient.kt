package wiki.scene.network.base

import com.hjq.gson.factory.GsonFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import wiki.scene.network.BuildConfig
import java.util.concurrent.TimeUnit

abstract class BaseRetrofitClient {

    companion object CLIENT {
        private const val TIME_OUT = 30

        private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
            val logging = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                logging.level = HttpLoggingInterceptor.Level.BODY
            } else {
                logging.level = HttpLoggingInterceptor.Level.BODY
            }
            return logging
        }

        fun getOkHttpClientBuilder(): OkHttpClient.Builder {
            return OkHttpClient.Builder()
                .addInterceptor(getHttpLoggingInterceptor())
                .connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
        }
    }

    private val client: OkHttpClient by lazy {
        val builder = getOkHttpClientBuilder()
        handleBuilder(builder)
        builder.build()
    }

    abstract fun handleBuilder(builder: OkHttpClient.Builder)

    open fun <Service> getService(serviceClass: Class<Service>, baseUrl: String): Service {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonFactory.getSingletonGson()))
            .baseUrl(baseUrl)
            .build()
            .create(serviceClass)
    }


}
