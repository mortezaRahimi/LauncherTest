package com.mortex.launchertest.ui.app_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mortex.launchertest.databinding.AppItemBinding
import com.mortex.launchertest.local.AppInfo

class AppInfoAdapter(private val appListener: AppListener) :
    RecyclerView.Adapter<AppInfoViewHolder>() {
    private val items = ArrayList<AppInfo>()

    fun setItems(items: List<AppInfo>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppInfoViewHolder {
        val binding: AppItemBinding =
            AppItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppInfoViewHolder(binding, appListener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: AppInfoViewHolder, position: Int) =
        holder.bind(items[position])

}

class AppInfoViewHolder(
    private val itemBinding: AppItemBinding,
    private val appListener: AppListener
) : RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var appInfo: AppInfo

    fun bind(item: AppInfo) {
        this.appInfo = item
        itemBinding.listAppName.text = item.label
        itemBinding.appPackage.text = item.packageName
//        itemBinding.appIcon.setImageDrawable()


        itemBinding.root.setOnClickListener {
            appListener.appTapped(item)
        }

    }


}

