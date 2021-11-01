package com.mortex.launchertest.ui.app_list

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mortex.launchertest.AppInfo
import com.mortex.launchertest.databinding.AppItemBinding

class AppInfoAdapter() :
    RecyclerView.Adapter<InterestsViewHolder>() {
    private val items = ArrayList<AppInfo>()

    fun setItems(items: List<AppInfo>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestsViewHolder {
        val binding: AppItemBinding =
            AppItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InterestsViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: InterestsViewHolder, position: Int) =
        holder.bind(items[position])

}

class InterestsViewHolder(
    private val itemBinding: AppItemBinding
) : RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var appInfo: AppInfo

    fun bind(item: AppInfo) {
        this.appInfo = item
        itemBinding.listAppName.text = item.label
        itemBinding.appPackage.text = item.packageName
        itemBinding.appIcon.setImageDrawable(item.icon)

        itemBinding.listAppName.setOnClickListener {
            val launchIntent: Intent = itemBinding.listAppName.context.packageManager
                .getLaunchIntentForPackage(item.packageName.toString())!!
            itemBinding.listAppName.context.startActivity(launchIntent)
        }
    }


}

