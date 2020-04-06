package com.rwawrzyniak.getby.core

import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
// https://stackoverflow.com/questions/41200876/how-to-set-onitemclicklistener-for-recyclerview

open class RecycleOnTouchListener(
	context: Context,
	recyclerView: RecyclerView,
	private val clickListener: ClickListener
) :
	OnItemTouchListener {
	private val gestureDetector: GestureDetector

	interface ClickListener {
		fun onClick(view: View?, position: Int)
		fun onLongClick(view: View?, position: Int)
	}


	override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
	override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

	override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
		val child: View? = rv.findChildViewUnder(e.x, e.y)
		if (child != null && gestureDetector.onTouchEvent(e)) {
			clickListener.onClick(child, rv.getChildAdapterPosition(child))
		}
		return false
	}

	init {
		gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
			override fun onSingleTapUp(e: MotionEvent): Boolean {
				return true
			}

			override fun onLongPress(e: MotionEvent) {
				val child: View? = recyclerView.findChildViewUnder(e.x, e.y)
				if (child != null) {
					clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child))
				}
			}
		})
	}
}
