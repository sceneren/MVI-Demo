package wiki.scene.frame.widget.picture_selector

import android.app.Activity
import android.graphics.Color
import androidx.fragment.app.Fragment
import com.luck.picture.lib.PictureSelectionModel
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.manager.PictureCacheManager
import com.luck.picture.lib.style.PictureSelectorUIStyle
import com.luck.picture.lib.style.PictureWindowAnimationStyle
import wiki.scene.frame.R
import wiki.scene.frame.base.BaseApp
import wiki.scene.frame.widget.picture_selector.engine.CoilImageEngine
import wiki.scene.frame.widget.picture_selector.listener.PictureSelectorListener

/**
 *
 * @Description:    图片选择器封装
 * @Author:         scene
 * @CreateDate:     2021/12/6 10:28
 * @UpdateUser:
 * @UpdateDate:     2021/12/6 10:28
 * @UpdateRemark:
 * @Version:        1.0.0
 */
object PictureSelectorUtils {
    private val windowAnimationStyle by lazy {
        PictureWindowAnimationStyle().apply {
            ofAllAnimation(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out)
        }
    }

    private val mPictureSelectorUIStyle by lazy {
        PictureSelectorUIStyle.ofDefaultStyle().apply {
            picture_top_leftBack = R.drawable.base_ic_back
            picture_container_backgroundColor = Color.parseColor("#F5F5F5")
            picture_switchSelectNumberStyle = true
            picture_statusBarBackgroundColor = Color.WHITE
            picture_statusBarChangeTextColor = true
            picture_top_titleBarBackgroundColor = Color.WHITE
            picture_navBarColor = Color.WHITE
            picture_top_titleTextColor = Color.parseColor("#333333")
            picture_top_titleRightTextColor =
                intArrayOf(Color.parseColor("#333333"), Color.parseColor("#333333"))
            picture_bottom_barBackgroundColor = Color.WHITE
            picture_check_style = R.drawable.base_picture_checkbox_num_selector
            picture_top_titleArrowUpDrawable = R.drawable.picture_icon_orange_arrow_up
            picture_top_titleArrowDownDrawable = R.drawable.picture_icon_orange_arrow_down
        }
    }

    fun selectPicture(activity: Activity, count: Int, listener: PictureSelectorListener) {
        val pictureSelectionModel = PictureSelector.create(activity)
            .openGallery(PictureMimeType.ofImage())
        selectPicture(pictureSelectionModel, count, listener)
    }

    fun selectPicture(fragment: Fragment, count: Int, listener: PictureSelectorListener) {
        val pictureSelectionModel = PictureSelector.create(fragment)
            .openGallery(PictureMimeType.ofImage())
        selectPicture(pictureSelectionModel, count, listener)
    }

    private fun selectPicture(
        pictureSelectionMode: PictureSelectionModel,
        count: Int,
        listener: PictureSelectorListener
    ) {
        if (count == 1) {
            pictureSelectionMode.selectionMode(PictureConfig.SINGLE)
        } else {
            pictureSelectionMode.selectionMode(PictureConfig.MULTIPLE)
                .maxSelectNum(count)
        }
        pictureSelectionMode.imageEngine(CoilImageEngine.instance)
            .isCompress(true)
            .imageSpanCount(4)
            .minimumCompressSize(100)// 小于100kb的图片不压缩
            .compressQuality(60)// 压缩质量 默认90 int
            .setPictureWindowAnimationStyle(windowAnimationStyle)
            .isPreviewImage(false)
            .isGif(true)
            .setPictureUIStyle(mPictureSelectorUIStyle)
            .forResult(listener)
    }

    fun clearCache() {
        PictureCacheManager.deleteAllCacheDirRefreshFile(BaseApp.instance)
    }

}