package com.rwawrzyniak.getby.habits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.rwawrzyniak.getby.R
import com.rwawrzyniak.getby.core.ext.markRequired
import com.rwawrzyniak.getby.dagger.fragmentScopedViewModel
import com.rwawrzyniak.getby.dagger.injector
import com.rwawrzyniak.getby.databinding.FragmentAddNewHabitPopupBinding
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.fragment_add_new_habit_popup.*

class AddNewHabitDialog : DialogFragment(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentAddNewHabitPopupBinding
    private val viewModel by fragmentScopedViewModel { injector.addNewHabitViewModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentAddNewHabitPopupBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupPickers()
        setupTextViews()
    }

    private fun setupPickers() {
        setupFrequencyPicker()
        setupReminder()
    }

    private fun setupTextViews() {
        binding.habitNameLayout.markRequired()
        binding.habitDescriptionLayout.markRequired()
        binding.habitName.doAfterTextChanged{
            binding.habitNameLayout.isErrorEnabled = false
        }
        binding.habitDescription.doAfterTextChanged{
            binding.habitDescriptionLayout.isErrorEnabled = false
        }
    }

    private fun setupReminder() {
        binding.reminder.setOnClickListener {
                TimePickerDialog.newInstance(
                    { view, hourOfDay, minute, _ ->
                        view?.dismiss()
                        viewModel.habitBuilder.reminder(Reminder(HourMinute(hourOfDay, minute)))
						binding.reminder.text = "$hourOfDay:$minute"
                    },
                    false
                ).show(requireFragmentManager(), TIME_PICKER_DIALOG_TAG)
        }
    }

    private fun setupFrequencyPicker() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.habits_frequency,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            binding.frequencyPicker.adapter = adapter
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.frequencyPicker.setSelection(0, false)
        binding.frequencyPicker.onItemSelectedListener = this
    }

    private fun setupToolbar() {
        with(toolbar) {
            setNavigationOnClickListener { dismiss() }
            title = "Some Title"
            inflateMenu(R.menu.menu_add_habit_popup)
            setOnMenuItemClickListener { item: MenuItem? ->
                if(saveHabit()){
                    dismiss()
                }
                true
            }
        }
    }

    private fun saveHabit(): Boolean {
        val isValid = validateHabit()

        if(isValid){
            viewModel.habitBuilder.name = binding.habitName.text.toString()
            viewModel.habitBuilder.description = binding.habitDescription.text.toString()
			addFrequencyToHabit(binding.frequencyPicker.selectedItemPosition)

            viewModel.saveHabit()
        }

        return isValid
    }

    private fun validateHabit(): Boolean {
        var isValid = true
        if (binding.habitName.text.toString().isNullOrBlank()) {
            binding.habitNameLayout.error = getString(R.string.empty_field_error)
            isValid = false
        }
        if (binding.habitDescription.text.toString().isNullOrBlank()) {
            binding.habitDescription.error = getString(R.string.empty_field_error)
            isValid = false
        }

        return isValid
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        // <item>Every day</item>
        // <item>Once a week</item>
        // <item>2 times per week</item>
        // <item>5 times per week</item>
        // <item>Custom</item>
        when (parent.id) {
            R.id.frequencyPicker -> {
				addFrequencyToHabit(pos)
            }
        }
    }

	private fun addFrequencyToHabit(
		pos: Int
	) {
		when (pos) {
			0 -> viewModel.habitBuilder.frequency(Frequency(7, 7))
			1 -> viewModel.habitBuilder.frequency(Frequency(1, 7))
			2 -> viewModel.habitBuilder.frequency(Frequency(2, 7))
			3 -> viewModel.habitBuilder.frequency(Frequency(5, 7))
			4 -> TODO("Custom not yet made")
		}
	}

	private fun buildHabitWithDefaultFrequency() {
		viewModel.habitBuilder.frequency(Frequency(7, 7))
	}

	override fun onNothingSelected(parent: AdapterView<*>) {
    }

    override fun onStart() {
        super.onStart()
        initializeFullScreen()
    }

    private fun initializeFullScreen() {
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)
    }

    companion object {
        const val ADD_NEW_HABIT_DIALOG_TAG = "AddNewHabitDialog"
        const val TIME_PICKER_DIALOG_TAG = "TimePickerDialog"
        fun show(fragmentManager: FragmentManager): AddNewHabitDialog =
            AddNewHabitDialog().apply { show(fragmentManager, ADD_NEW_HABIT_DIALOG_TAG) }
    }
}
