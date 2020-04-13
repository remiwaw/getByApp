package com.rwawrzyniak.getby.habits.createupdate

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.rwawrzyniak.getby.R
import com.rwawrzyniak.getby.core.SchedulerProvider
import com.rwawrzyniak.getby.core.ext.date.convertWeekDaysToMaterial
import com.rwawrzyniak.getby.core.ext.date.convertWeekDaysToStandard
import com.rwawrzyniak.getby.core.ext.markRequired
import com.rwawrzyniak.getby.dagger.fragmentScopedViewModel
import com.rwawrzyniak.getby.dagger.injector
import com.rwawrzyniak.getby.databinding.FragmentHabitCreateUpdateDialogBinding
import com.rwawrzyniak.getby.habits.Frequency
import com.rwawrzyniak.getby.habits.Habit
import com.rwawrzyniak.getby.habits.HourMinute
import com.rwawrzyniak.getby.habits.Reminder
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import io.reactivex.Completable
import io.reactivex.rxkotlin.subscribeBy
import io.sellmair.disposer.disposeBy
import io.sellmair.disposer.onStop
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_habit_create_update_dialog.*
import kotlinx.android.synthetic.main.fragment_tour.*
import timber.log.Timber

class HabitCreateUpdateDialog : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentHabitCreateUpdateDialogBinding
    private val viewModel by fragmentScopedViewModel { injector.habitsCreateUpdateViewModel }
	private val schedulerProvider: SchedulerProvider by lazy { injector.provideSchedulerProvider() }
	private var isUserInput = true // TODO make it better, change to avoid executing listener on text changed.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentHabitCreateUpdateDialogBinding.inflate(inflater, container, false)

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

	override fun onStart() {
		super.onStart()
		startObservers()
		arguments?.getString(ARG_HABIT_ID)?.let {
			subscribeTo(viewModel.onAction(HabitCreateUpdateViewAction.LoadHabit(it)))
		}
		initializeFullScreen()
	}

	private fun startObservers() {
		wireViewModel()
	}

	private fun wireViewModel() {
		viewModel.observeState()
			.observeOn(schedulerProvider.main())
			.subscribeBy(onNext = this::renderState)
			.disposeBy(lifecycle.onStop)

		viewModel.observeEffects()
			.observeOn(schedulerProvider.main())
			.subscribeBy(onNext = this::executeEffect)
			.disposeBy(lifecycle.onStop)
	}

	private fun renderState(state: HabitCreateUpdateViewState) {
		binding.rowDayOfWeekPicker.selectAllDays()
		if(state.isUpdateMode && state.backingHabit != null){
			renderTextFields(state.backingHabit)
			renderFrequency(state.backingHabit)
			renderReminder(state.backingHabit)
		}
	}

	private fun renderTextFields(backingHabit: Habit) {
		binding.habitName.setText(backingHabit.name)
		binding.habitDescription.setText(backingHabit.description)
	}

	private fun renderFrequency(backingHabit: Habit) {
		if (shouldDisplayCustomFrequency(backingHabit.frequency)) {
			binding.customFrequencyView.setTimes(backingHabit.frequency.times)
			binding.customFrequencyView.setDays(backingHabit.frequency.days)
			showCustomFrequency()
		} else {
			binding.frequencyPicker.setSelection(getFrequencySpinnerIndex(backingHabit.frequency))
			hideCustomFrequency()
		}
	}

	private fun renderReminder(
		backingHabit: Habit
	) {
		backingHabit.reminder?.let {
			binding.rowDayOfWeekPicker.setSelectedDays(convertWeekDaysToMaterial(it.days))
		}

		showWeekDayRow(resources.getString(R.string.reminderDefaultValue) == binding.reminder.text)
		binding.reminder.text = resources.getString(R.string.reminderAt, backingHabit.reminder?.time)
	}

	private fun executeEffect(effect: HabitDetailsViewEffect) {
		when (effect) {
			is HabitDetailsViewEffect.ConfigureFields -> showFieldsError(effect)
			is HabitDetailsViewEffect.DismissPopup -> dismiss()
		}
	}

	private fun shouldDisplayCustomFrequency(frequency: Frequency) =
		getFrequencySpinnerIndex(frequency) == CUSTOM_FREQUENCY_USED

	private fun showFieldsError(effect: HabitDetailsViewEffect.ConfigureFields) {
			binding.habitNameLayout.isErrorEnabled = effect.habitNameInput.isError
			binding.habitDescriptionLayout.isErrorEnabled = effect.habitDescriptionInput.isError
			binding.habitNameLayout.error = effect.habitNameInput.errorMessage
			binding.habitDescriptionLayout.error = effect.habitDescriptionInput.errorMessage
			if(binding.customFrequencyView.isVisible && effect.frequencyInput.isError){
				binding.customFrequencyView.setError(effect.frequencyInput.errorMessage)
			}
	}

	private fun setupPickers() {
        setupFrequencyPicker()
        setupReminder()
    }

    private fun setupTextViews() {
        binding.habitNameLayout.markRequired()
        binding.habitDescriptionLayout.markRequired()
		setupInputFields()
	}

	private fun setupInputFields() {
		binding.habitName.addTextChangedListener(object : TextWatcher {
			override fun afterTextChanged(s: Editable) {}

			override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

			override fun onTextChanged(
				habitName: CharSequence,
				start: Int,
				before: Int,
				count: Int
			) {
				if (isUserInput) {
					subscribeTo(
						viewModel.onAction(
							HabitCreateUpdateViewAction.OnInputFieldStateChanged(
								isNameFieldEmpty = habitName.isBlank(),
								isDescriptionFieldEmpty = binding.habitDescription.text.isNullOrBlank()
							)
						)
					)
				}
			}
		})

		binding.habitName.addTextChangedListener(object : TextWatcher {
			override fun afterTextChanged(s: Editable) {}

			override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

			override fun onTextChanged(
				habitDescription: CharSequence,
				start: Int,
				before: Int,
				count: Int
			) {
				if (isUserInput) {
					subscribeTo(
						viewModel.onAction(
							HabitCreateUpdateViewAction.OnInputFieldStateChanged(
								isNameFieldEmpty = binding.habitName.text.isNullOrEmpty(),
								isDescriptionFieldEmpty = habitDescription.isBlank()
							)
						)
					)
				}
			}
		})
	}

	private fun setupReminder() {
		binding.reminder.setOnClickListener {
                val newInstance = TimePickerDialog.newInstance(
					{ view, hourOfDay, minute, _ ->
						view?.dismiss()
						binding.reminder.text = "$hourOfDay:$minute"
						showWeekDayRow(true)
					},
					false
				)
			newInstance.setOnDismissListener {
				showWeekDayRow(false)
				binding.reminder.text = resources.getString(R.string.reminderDefaultValue)
			}
			newInstance.show(requireFragmentManager(), TIME_PICKER_DIALOG_TAG)
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
            setNavigationOnClickListener { nav_host.goToNextScreenButton() }
            title = "Some Title"
            inflateMenu(R.menu.menu_add_habit_popup)
            setOnMenuItemClickListener { item: MenuItem? ->
				onSaveHabitClick()
                true
            }
        }
    }

	private fun onSaveHabitClick() {
		subscribeTo(
			viewModel.onAction(
				HabitCreateUpdateViewAction.OnSaveHabitClicked(
					Habit(
						name = binding.habitName.text.toString(),
						description = binding.habitDescription.text.toString(),
						frequency = getFrequencyValue(),
						reminder = getReminder()
					)
				)
			)
		)
	}

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long){
        when (parent.id) {
            R.id.frequencyPicker -> if (binding.frequencyPicker.selectedItemPosition == 4) showCustomFrequency()
		}
	}

	override fun onNothingSelected(parent: AdapterView<*>) {}

	private fun getReminder(): Reminder? {
		if(binding.reminder.text == resources.getString(R.string.reminderDefaultValue)){
			return null
		}
		val daysOfWeek = convertWeekDaysToStandard(rowDayOfWeekPicker.selectedDays)
		val reminderText = binding.reminder.text
		val hourOfDay = reminderText.split(":")[0].toInt()
		val minuteOfDay  = reminderText.split(":")[1].toInt()
		return Reminder(HourMinute(hourOfDay, minuteOfDay), daysOfWeek)
	}

	private fun getFrequencyValue(): Frequency = when (binding.frequencyPicker.selectedItemPosition) {
		0 -> Frequency(7, 7)
		1 -> Frequency(1, 7)
		2 -> Frequency(2, 7)
		3 -> Frequency(5, 7)
		else -> Frequency(binding.customFrequencyView.getTimes(), binding.customFrequencyView.getDays())
	}

	private fun getFrequencySpinnerIndex(frequency: Frequency): Int = when (frequency) {
		Frequency(7, 7) -> 0
		Frequency(1, 7) -> 1
		Frequency(2, 7) -> 2
		Frequency(5, 7) -> 3
		else -> CUSTOM_FREQUENCY_USED
	}

	private fun hideCustomFrequency() {
		binding.customFrequencyView.visibility = View.GONE
		binding.frequencyPicker.visibility = View.VISIBLE
	}

	private fun showCustomFrequency() {
		binding.customFrequencyView.visibility = View.VISIBLE
		binding.frequencyPicker.visibility = View.GONE
	}

	private fun showWeekDayRow(isShow: Boolean){
		if(isShow){
			binding.rowDayOfWeekPicker.visibility = View.VISIBLE
		} else {
			binding.rowDayOfWeekPicker.visibility = View.GONE
		}
	}

    private fun initializeFullScreen() {
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
    }

	private fun subscribeTo(completable: Completable) {
		completable.onErrorComplete()
			.subscribeOn(schedulerProvider.io())
			.subscribeBy(onError = Timber::e)
			.disposeBy(lifecycle.onStop)
	}

    companion object {
        const val TIME_PICKER_DIALOG_TAG = "TimePickerDialog"
        const val ARG_HABIT_ID = "HabitIdArg"

        private const val CUSTOM_FREQUENCY_USED =  -1
        fun create(habitId: String = ""): HabitCreateUpdateDialog =
            HabitCreateUpdateDialog().apply {
				arguments = Bundle().apply {
					putString(ARG_HABIT_ID, habitId)
				}
			}
    }
}
