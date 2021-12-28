package wiki.scene.common.router

object ARouterConfig {
    private const val BASE = "/base"
    private const val APP = "/app"

    object Base {
        const val BASE_WEB = "$BASE/WebAc"
    }

    object App {
        const val FRAG_TAB_1 = "$APP/Tab1Fragment"
        const val FRAG_TAB_2 = "$APP/Tab2Fragment"
        const val FRAG_TAB_3 = "$APP/Tab3Fragment"
        const val FRAG_TAB_4 = "$APP/Tab4Fragment"
        const val FRAG_TYPE = "$APP/TypeFragment"
        const val ACT_API = "$APP/ApiFragment"
    }

}