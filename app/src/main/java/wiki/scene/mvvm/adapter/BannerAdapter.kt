package wiki.scene.mvvm.adapter

import coil.load
import coil.transform.RoundedCornersTransformation
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder
import wiki.scene.common.utils.toPx
import wiki.scene.entity.wanandroid.BannerInfo
import wiki.scene.mvvm.R
import wiki.scene.mvvm.databinding.FragmentTab2HeaderItemBinding

class BannerAdapter : BaseBannerAdapter<BannerInfo>() {
    override fun bindData(
        holder: BaseViewHolder<BannerInfo>,
        data: BannerInfo,
        position: Int,
        pageSize: Int
    ) {
        val mBinding = FragmentTab2HeaderItemBinding.bind(holder.itemView)
        mBinding.ivImage
            .load(data.imagePath) {
                transformations(RoundedCornersTransformation(8.toPx().toFloat()))
            }
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.fragment_tab_2_header_item
    }
}