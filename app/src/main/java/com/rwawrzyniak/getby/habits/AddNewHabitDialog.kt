package com.rwawrzyniak.getby.habits
import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

class ExampleDialog : DialogFragment() {
    private var toolbar: Toolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.fragment_add_new_habit_popup, container, false)
        toolbar = view.findViewById(R.id.toolbar)
        return view
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setNavigationOnClickListener { v: View? -> dismiss() }
        toolbar.title = "Some Title"
        toolbar.inflateMenu(R.menu.example_dialog)
        toolbar.setOnMenuItemClickListener { item: MenuItem? ->
            dismiss()
            true
        }
    }

    companion object {
        const val TAG = "example_dialog"
        fun display(fragmentManager: FragmentManager?): ExampleDialog {
            val exampleDialog = ExampleDialog()
            exampleDialog.show(fragmentManager, TAG)
            return exampleDialog
        }
    }
}