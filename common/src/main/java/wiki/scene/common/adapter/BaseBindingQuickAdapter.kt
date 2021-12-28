package wiki.scene.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

abstract class BaseBindingQuickAdapter<T, VB : ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
    layoutResId: Int = -1
) :
    BaseQuickAdapter<T, BaseBindingQuickAdapter.BaseBindingHolder>(layoutResId) {

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int) =
        BaseBindingHolder(inflate(LayoutInflater.from(parent.context), parent, false))

    fun addChildClickViews(vararg views: View) {
        for (view in views) {
            addChildClickViewIds(view.id)
        }
    }


    open class BaseBindingHolder(private val binding: ViewBinding) : BaseViewHolder(binding.root) {

        @Suppress("UNCHECKED_CAST")
        fun <VB : ViewBinding> getViewBinding() = binding as VB

    }


}