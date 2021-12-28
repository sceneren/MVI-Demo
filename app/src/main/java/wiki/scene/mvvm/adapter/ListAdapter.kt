package wiki.scene.mvvm.adapter

import com.chad.library.adapter.base.module.LoadMoreModule
import wiki.scene.common.adapter.BaseBindingQuickAdapter
import wiki.scene.entity.wanandroid.ArticleInfo
import wiki.scene.mvvm.R
import wiki.scene.mvvm.databinding.FragmentTab1ItemBinding

class ListAdapter :
    BaseBindingQuickAdapter<ArticleInfo, FragmentTab1ItemBinding>(FragmentTab1ItemBinding::inflate),
    LoadMoreModule {
    init {
        addChildClickViewIds(R.id.left_menu, R.id.right_menu, R.id.tv_name)
    }

    override fun convert(holder: BaseBindingHolder, item: ArticleInfo) {
        holder.getViewBinding<FragmentTab1ItemBinding>().run {
            tvName.text = item.title
        }
    }
}