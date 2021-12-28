package wiki.scene.frame.base

import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.decode.VideoFrameDecoder
import coil.imageLoader
import coil.util.CoilUtils
import coil.util.DebugLogger
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.kingja.loadsir.core.LoadSir
import com.kongzue.dialogx.DialogX
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import wiki.scene.common.mojito.MojitoUtils
import wiki.scene.common.mojito.coil.ProgressSupport
import wiki.scene.frame.BuildConfig
import wiki.scene.frame.R
import wiki.scene.frame.loadsir.ErrorCallback
import wiki.scene.frame.loadsir.LoadingCallback
import wiki.scene.network.base.BaseRetrofitClient
import com.kongzue.dialogx.style.IOSStyle
import wiki.scene.common.toast.ToastUtil


/**
 *
 * @Description:    基类的Application
 * @Author:         scene
 * @CreateDate:     2021/11/10 09:45
 * @UpdateUser:
 * @UpdateDate:     2021/11/10 09:45
 * @UpdateRemark:
 * @Version:        1.0.0
 */
open class BaseApp : MultiDexApplication(), ImageLoaderFactory {
    init {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            MaterialHeader(context)
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        ProcessLifecycleOwner.get().lifecycle.addObserver(ApplicationLifecycleObserver())
        initToast()
        initDialogX()
        initARouter()
        initLoadSir()
        initMojito()
    }

    private fun initToast() {
        ToastUtil.init(this)
    }

    /**
     * 配置DialogX
     */
    private fun initDialogX() {
        DialogX.init(this)
        DialogX.globalStyle = IOSStyle()
        DialogX.globalTheme = DialogX.THEME.LIGHT
        DialogX.dialogMaxWidth = ScreenUtils.getAppScreenWidth()
        DialogX.autoShowInputKeyboard = false
    }

    /**
     * 初始化ARouter
     */
    private fun initARouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }

    /**
     * 初始化多状态布局
     */
    private fun initLoadSir() {
        LoadSir.beginBuilder()
            .addCallback(LoadingCallback())
            .addCallback(ErrorCallback())
            .commit()
    }

    companion object {
        lateinit var instance: BaseApp
            private set
    }


    private inner class ApplicationLifecycleObserver : DefaultLifecycleObserver {

        override fun onStart(owner: LifecycleOwner) {
            super.onStart(owner)
            LogUtils.e("AppLifecycleObserver", "onStart")
        }

        override fun onStop(owner: LifecycleOwner) {
            super.onStop(owner)
            LogUtils.e("AppLifecycleObserver", "onStop")
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(applicationContext)
            .crossfade(true)
            .okHttpClient {
                BaseRetrofitClient.getOkHttpClientBuilder()
                    .cache(CoilUtils.createDefaultCache(this))
                    .addInterceptor { chain ->
                        val request = chain.request()
                        val response = chain.proceed(request)
                        response.newBuilder()
                            .body(
                                ProgressSupport.OkHttpProgressResponseBody(
                                    request.url, response.body,
                                    ProgressSupport.DispatchingProgressListener()
                                )
                            )
                            .build()
                    }
                    .build()
            }
            .componentRegistry {
                // GIFs
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder(applicationContext))
                } else {
                    add(GifDecoder())
                }
                // SVGs
                add(SvgDecoder(applicationContext))
                // Video frames
                add(VideoFrameDecoder(applicationContext))
            }
            .placeholder(R.drawable.picture_image_placeholder)
            .error(R.drawable.picture_image_placeholder)
            .apply {
                // Enable logging to the standard Android log if this is a debug build.
                if (BuildConfig.DEBUG) {
                    logger(DebugLogger(Log.VERBOSE))
                }
            }
            .build()
    }

    /**
     * 查看大图
     */
    private fun initMojito() {
        MojitoUtils.init(applicationContext, applicationContext.imageLoader)
    }
}