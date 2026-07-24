package com.example.nailnail.ui.mypage

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nailnail.R

class NoticeAdapter(
    private val items: List<NoticeItem>,
    private val onItemClick: (NoticeItem) -> Unit
) : RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoticeViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_notice,
                    parent,
                    false
                )

        return NoticeViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: NoticeViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int =
        items.size

    inner class NoticeViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val noticeRoot: View =
            itemView.findViewById(
                R.id.layout_notice_item
            )

        private val noticeTitle: TextView =
            itemView.findViewById(
                R.id.tv_notice_title
            )

        private val noticeDate: TextView =
            itemView.findViewById(
                R.id.tv_notice_date
            )

        private val noticeArrow: ImageView =
            itemView.findViewById(
                R.id.iv_notice_open
            )

        fun bind(item: NoticeItem) {
            noticeTitle.text =
                item.title

            noticeDate.text =
                item.date

            val backgroundColor =
                if (item.isHighlighted) {
                    COLOR_HIGHLIGHTED_NOTICE
                } else {
                    COLOR_DEFAULT_NOTICE
                }

            noticeRoot.setBackgroundColor(
                Color.parseColor(backgroundColor)
            )

            noticeArrow.setColorFilter(
                Color.parseColor(COLOR_ARROW)
            )

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    companion object {
        private const val COLOR_HIGHLIGHTED_NOTICE =
            "#FCF4F6"

        private const val COLOR_DEFAULT_NOTICE =
            "#FFFEFD"

        private const val COLOR_ARROW =
            "#686360"
    }
}