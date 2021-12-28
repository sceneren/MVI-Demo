package wiki.scene.frame.widget.picture_selector.engine

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.widget.ImageView
import androidx.core.view.isVisible
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import coil.size.ViewSizeResolver
import coil.transform.RoundedCornersTransformation
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.listener.OnImageCompleteCallback
import com.luck.picture.lib.tools.MediaUtils
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView
import wiki.scene.frame.widget.picture_selector.utils.ImageEngineUtils
import java.io.File

class CoilImageEngine private constructor() : ImageEngine {

    //    PictureMimeType.isContent(path)
    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { CoilImageEngine() }
    }

    override fun loadImage(context: Context, url: String, imageView: ImageView) {
        if (!ImageEngineUtils.assertValidRequest(context)) {
            return
        }
        imageView.load(url.formatUriPath())
    }

    override fun loadImage(
        context: Context,
        url: String,
        imageView: ImageView,
        longImageView: SubsamplingScaleImageView?,
        callback: OnImageCompleteCallback?
    ) {
        if (!ImageEngineUtils.assertValidRequest(context)) {
            return
        }

        val request = ImageRequest.Builder(context)
            .data(url.formatUriPath())
            .target(
                onStart = {
                    callback?.onShowLoading()
                },
                onSuccess = { result ->
                    val bitmap = (result as BitmapDrawable).bitmap
                    val eqLongImage: Boolean = MediaUtils.isLongImg(
                        bitmap.width,
                        bitmap.height
                    )
                    longImageView?.isVisible = eqLongImage
                    imageView.isVisible = !eqLongImage
                },
                onError = {
                    callback?.onHideLoading()
                }
            )
            .build()
        context.imageLoader.enqueue(request)
    }

    override fun loadFolderImage(context: Context, url: String, imageView: ImageView) {
        if (!ImageEngineUtils.assertValidRequest(context)) {
            return
        }
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.load(url.formatUriPath()) {
            transformations(RoundedCornersTransformation(8F))
            size(ViewSizeResolver(imageView))
        }
    }

    override fun loadGridImage(context: Context, url: String, imageView: ImageView) {
        if (!ImageEngineUtils.assertValidRequest(context)) {
            return
        }
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.load(url.formatUriPath()) {
            size(ViewSizeResolver(imageView))
        }
    }

    private fun String.formatUriPath(): Uri {
        return if (PictureMimeType.isContent(this)) {
            Uri.parse(this)
        } else {
            Uri.fromFile(File(this))
        }
    }
}