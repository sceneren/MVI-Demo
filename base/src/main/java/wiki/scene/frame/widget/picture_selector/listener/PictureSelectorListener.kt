package wiki.scene.frame.widget.picture_selector.listener

import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener

/**
 *
 * @Description:    选择图片的回掉
 * @Author:         scene
 * @CreateDate:     2021/12/6 10:08
 * @UpdateUser:
 * @UpdateDate:     2021/12/6 10:08
 * @UpdateRemark:
 * @Version:        1.0.0
 */
open class PictureSelectorListener : OnResultCallbackListener<LocalMedia> {
    override fun onResult(result: MutableList<LocalMedia>?) {

    }
    override fun onCancel() {

    }
}