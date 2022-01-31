package com.mortex.launchertest.ui.app_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.mortex.launchertest.R
import com.mortex.launchertest.databinding.AppItemBinding
import com.mortex.launchertest.databinding.LinkItemBinding
import com.mortex.launchertest.local.AppInfo
import com.mortex.launchertest.local.AppInfoWithIcon
import com.mortex.launchertest.local.ULink

class LinkAdapter(private val linkListener: LinkListener) :
    RecyclerView.Adapter<LinkViewHolder>() {
    private val items = ArrayList<ULink>()

    fun setItems(items: List<ULink>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        val binding: LinkItemBinding =
            LinkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LinkViewHolder(binding, linkListener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) =
        holder.bind(items[position])

}

class LinkViewHolder(
    private val itemBinding: LinkItemBinding,
    private val linkListener: LinkListener
) : RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var link: ULink

    fun bind(item: ULink) {
        this.link = item
        itemBinding.btnLinkName.text = item.name

            when {
                link.url.contains("ebuy.mvmco.ir") ->   itemBinding.linkImage.setImageDrawable(
                    AppCompatResources.getDrawable(
                        itemBinding.btnLinkName.context,
                        R.drawable.ic_ebuy
                    )
                )

                link.url.contains("fownix.ir") ->   itemBinding.linkImage.setImageDrawable(
                    AppCompatResources.getDrawable(
                        itemBinding.btnLinkName.context,
                        R.drawable.ic_fownix
                    )
                )

                link.url.contains("mvmco.ir") ->   itemBinding.linkImage.setImageDrawable(
                    AppCompatResources.getDrawable(
                        itemBinding.btnLinkName.context,
                        R.drawable.ic_mvm
                    )
                )

                else -> itemBinding.linkImage.visibility = View.GONE
            }


        itemBinding.root.setOnClickListener {
            linkListener.linkTapped(item)
        }

    }


}

