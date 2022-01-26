package com.mortex.launchertest.ui.app_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mortex.launchertest.databinding.AppItemBinding
import com.mortex.launchertest.local.AppInfo
import com.mortex.launchertest.local.AppInfoWithIcon

class AppInfoLinksAdapter(private val appListener: AppListenerLinks) :
    RecyclerView.Adapter<AppInfoLinksViewHolder>() {
    private val items = ArrayList<AppInfoWithIcon>()

    fun setItems(items: List<AppInfoWithIcon>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppInfoLinksViewHolder {
        val binding: AppItemBinding =
            AppItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppInfoLinksViewHolder(binding, appListener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: AppInfoLinksViewHolder, position: Int) =
        holder.bind(items[position])

}

class AppInfoLinksViewHolder(
    private val itemBinding: AppItemBinding,
    private val appListener: AppListenerLinks
) : RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var appInfo: AppInfoWithIcon

    fun bind(item: AppInfoWithIcon) {
        this.appInfo = item
        itemBinding.listAppName.text = item.label
        itemBinding.appPackage.text = item.packageName
        itemBinding.appIcon.setImageDrawable(item.icon)


        itemBinding.root.setOnClickListener {
            appListener.appTappedForLinks(item)
        }

    }


}

