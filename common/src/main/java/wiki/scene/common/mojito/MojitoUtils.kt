package wiki.scene.common.mojito

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import net.mikaelzero.mojito.Mojito
import net.mikaelzero.mojito.ext.mojito
import net.mikaelzero.mojito.impl.DefaultPercentProgress
import net.mikaelzero.mojito.impl.NumIndicator
import net.mikaelzero.mojito.interfaces.IProgress
import net.mikaelzero.mojito.loader.InstanceLoader
import net.mikaelzero.mojito.view.sketch.SketchImageLoadFactory
import wiki.scene.common.mojito.coil.CoilImageLoader
import wiki.scene.common.mojito.listener.CustomMojitoListener

object MojitoUtils {

    fun init(context: Context, imageLoader: ImageLoader) {
        Mojito.initialize(
            CoilImageLoader.with(context, imageLoader),
            SketchImageLoadFactory()
        )
    }

    private val progressLoader = object : InstanceLoader<IProgress> {
        override fun providerInstance(): IProgress {
            return DefaultPercentProgress()
        }
    }

    fun previewImage(mContext: Context, imageUrl: String) {
        previewImage(mContext, listOf(imageUrl))
    }

    fun previewImage(mContext: Context, imageUrls: List<String>, position: Int = 0) {
        if (imageUrls.isEmpty()) {
            return
        }
        Mojito.start(mContext) {
            urls(imageUrls)
            position(position)
            setProgressLoader(progressLoader)
            if (imageUrls.size > 1) {
                setIndicator(NumIndicator())
            }
            setOnMojitoListener(CustomMojitoListener(imageUrls))
        }
    }

    fun previewImage(
        recyclerview: RecyclerView,
        @IdRes targetId: Int,
        position: Int,
        imageUrl: String,
        headerSize: Int = 0,
        footerSize: Int = 0
    ) {
        previewImage(recyclerview, targetId, position, listOf(imageUrl), headerSize, footerSize)
    }

    fun previewImage(
        recyclerview: RecyclerView,
        @IdRes targetId: Int,
        position: Int,
        imageUrls: List<String>,
        headerSize: Int = 0,
        footerSize: Int = 0
    ) {
        recyclerview.mojito(targetId) {
            urls(imageUrls)
            position(position, headerSize, footerSize)
            setProgressLoader(progressLoader)
            if (imageUrls.size > 1) {
                setIndicator(NumIndicator())
            }
            setOnMojitoListener(CustomMojitoListener(imageUrls))
        }
    }

    fun previewImage(view: View, imageUrl: String) {
        previewImage(view, listOf(imageUrl))
    }

    fun previewImage(view: View, imageUrls: List<String>, position: Int = 0) {
        if (imageUrls.isEmpty()) {
            return
        }
        view.mojito(imageUrls[position]) {
            urls(imageUrls)
            position(position)
            setProgressLoader(progressLoader)
            if (imageUrls.size > 1) {
                setIndicator(NumIndicator())
            }
            setOnMojitoListener(CustomMojitoListener(imageUrls))
        }
    }

}