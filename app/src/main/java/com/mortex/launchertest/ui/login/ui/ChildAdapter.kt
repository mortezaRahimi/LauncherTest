package com.mortex.launchertest.ui.login.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mortex.launchertest.R
import com.mortex.launchertest.databinding.ChildItemBinding
import com.mortex.launchertest.local.Child

class ChildAdapter(private val childListener: ChildListener) :
    RecyclerView.Adapter<ChildViewHolder>() {
    private val items = ArrayList<Child>()

    fun setItems(items: List<Child>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val binding: ChildItemBinding =
            ChildItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChildViewHolder(binding, childListener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) =
        holder.bind(items[position])

}

class ChildViewHolder(
    private val itemBinding: ChildItemBinding,
    private val childListener: ChildListener
) : RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var child: Child

    fun bind(item: Child) {
        this.child = item
        itemBinding.childName.text = child.name

        itemBinding.root.setOnClickListener{
            childListener.childTapped()
        }
    }



}

