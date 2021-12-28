package wiki.scene.common.mojito.listener

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import coil.imageLoader
import coil.request.ImageRequest
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.LogUtils
import com.kongzue.dialogx.dialogs.BottomMenu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.mikaelzero.mojito.MojitoView
import net.mikaelzero.mojito.interfaces.OnMojitoListener
import wiki.scene.common.R
import wiki.scene.common.resources.toXmlString
import wiki.scene.common.toast.showToast

class CustomMojitoListener(private val imageUrls: List<String>) : OnMojitoListener {
    override fun onClick(view: View, x: Float, y: Float, position: Int) {
    }

    override fun onDrag(view: MojitoView, moveX: Float, moveY: Float) {
    }

    override fun onLongClick(
        fragmentActivity: FragmentActivity?,
        view: View,
        x: Float,
        y: Float,
        position: Int
    ) {
        BottomMenu.show(listOf(R.string.common_save_to_mobile.toXmlString()))
            .setCancelButton(R.string.common_cancel.toXmlString())
            .setOnMenuItemClickListener { _, _, _ ->
                fragmentActivity?.let { mActivity ->
                    mActivity.lifecycleScope.launch {
                        flow {
                            val request = ImageRequest.Builder(mActivity)
                                .data(imageUrls[position])
                                .build()
                            val drawable = mActivity.imageLoader.execute(request).drawable
                            emit(drawable)
                        }.mapNotNull {
                            val file = ImageUtils.save2Album(
                                (it as BitmapDrawable).bitmap,
                                Bitmap.CompressFormat.PNG
                            )
                            file
                        }
                            .flowOn(Dispatchers.IO)
                            .catch {
                                showToast(R.string.common_save_failed)
                            }
                            .collect { file ->
                                if (file.exists()) {
                                    LogUtils.e(file.absolutePath)
                                    showToast(R.string.common_save_successful)
                                } else {
                                    showToast(R.string.common_save_failed)
                                }
                            }
                    }
                }

                false
            }
    }

    override fun onLongImageMove(ratio: Float) {
    }

    override fun onMojitoViewFinish(pagePosition: Int) {
    }

    override fun onShowFinish(mojitoView: MojitoView, showImmediately: Boolean) {
    }

    override fun onStartAnim(position: Int) {
    }

    override fun onViewPageSelected(position: Int) {
    }


}