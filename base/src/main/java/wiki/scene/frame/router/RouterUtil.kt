package wiki.scene.frame.router

import com.alibaba.android.arouter.launcher.ARouter
import wiki.scene.common.router.ARouterConfig

object RouterUtil {

    fun launchWeb(webUrl: String?) {
        ARouter.getInstance()
            .build(ARouterConfig.Base.BASE_WEB)
            .withString("webUrl", webUrl)
            .navigation()
    }

}