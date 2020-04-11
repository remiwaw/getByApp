package com.rwawrzyniak.getby.habits.details

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.maltaisn.recurpicker.Recurrence
import com.maltaisn.recurpicker.RecurrencePickerSettings
import com.maltaisn.recurpicker.list.RecurrenceListCallback
import com.maltaisn.recurpicker.list.RecurrenceListDialog
import com.maltaisn.recurpicker.picker.RecurrencePickerCallback
import com.maltaisn.recurpicker.picker.RecurrencePickerDialog
import com.maltaisn.recurpicker.picker.RecurrencePickerFragment
import com.rwawrzyniak.getby.R
import com.rwawrzyniak.getby.core.DateTimeProvider
import com.rwawrzyniak.getby.core.SchedulerProvider
import com.rwawrzyniak.getby.core.ext.markRequired
import com.rwawrzyniak.getby.dagger.fragmentScopedViewModel
import com.rwawrzyniak.getby.dagger.injector
import com.rwawrzyniak.getby.databinding.FragmentHabitDetailsDialogBinding
import com.rwawrzyniak.getby.habits.Frequency
import com.rwawrzyniak.getby.habits.Habit
import io.reactivex.Completable
import io.reactivex.rxkotlin.subscribeBy
import io.sellmair.disposer.disposeBy
import io.sellmair.disposer.onStop
import kotlinx.android.synthetic.main.fragment_habit_details_dialog.*
import timber.log.Timber

class HabitDetailsDialog : DialogFragment(),
	RecurrenceListCallback, RecurrencePickerCallback {
    private lateinit var binding: FragmentHabitDetailsDialogBinding
    private val viewModel by fragmentScopedViewModel { injector.habitsDetailsViewModel }
	private val schedulerProvider: SchedulerProvider by lazy { injector.provideSchedulerProvider() }
	private var isUserInput = true // TODO make it better, change to avoid executing listener on text changed.


	private var selectedRecurrence = Recurrence(Recurrence.Period.NONE)
		set(value) {
			field = value
			Toast.makeText(requireContext(), "Recurrence selected: " +
				settings.formatter.format(requireContext(), value, startDate), Toast.LENGTH_SHORT).show()
		}

	private val dateTimeProvider = DateTimeProvider()
	private val settings = RecurrencePickerSettings()
	private val startDate = dateTimeProvider.getCurrentMiliseconds()

	private val listDialog by lazy { RecurrenceListDialog.newInstance(settings) }
	private val pickerFragment by lazy { RecurrencePickerFragment.newInstance(settings) }
	private val pickerDialog by lazy { RecurrencePickerDialog.newInstance(settings) }

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
        binding = FragmentHabitDetailsDialogBinding.inflate(inflater, container, false)

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
			subscribeTo(viewModel.onAction(HabitDetailsViewAction.LoadHabit(it)))
		}
		initializeFullScreen()
	}

	override fun onRecurrenceCustomClicked() {
		pickerFragment.selectedRecurrence = selectedRecurrence
		pickerFragment.startDate = startDate

		childFragmentManager.beginTransaction()
			.add(R.id.picker_fragment_container, pickerFragment, "recurrence-picker-fragment")
			.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
			.addToBackStack(null)
			.commit()
	}

	override fun onRecurrencePresetSelected(recurrence: Recurrence) {
		selectedRecurrence = recurrence
	}

	override fun onRecurrenceCreated(recurrence: Recurrence) {
		// tu ladujemy po wlasciwym wyborze
		selectedRecurrence = recurrence
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

	private fun renderState(state: HabitDetailsViewState) {
		if(state.isUpdateMode && state.backingHabit != null){
			binding.habitName.setText(state.backingHabit.name)
			binding.habitDescription.setText(state.backingHabit.description)
			// binding.frequencyPicker.setSelection(getFrequencyIndex(state.backingHabit.frequency))
			// binding.reminder.text = state.backingHabit.reminder.toString()
		}
	}

	private fun executeEffect(effect: HabitDetailsViewEffect) {
		when (effect) {
			is HabitDetailsViewEffect.ConfigureFields -> showFieldsError(effect)
			is HabitDetailsViewEffect.DismissPopup -> dismiss()
		}
	}

	private fun showFieldsError(effect: HabitDetailsViewEffect.ConfigureFields) {
			binding.habitNameLayout.isErrorEnabled = effect.habitNameInput.isError
			binding.habitDescriptionLayout.isErrorEnabled = effect.habitDescriptionInput.isError
			binding.habitNameLayout.error = effect.habitNameInput.errorMessage
			binding.habitDescriptionLayout.error = effect.habitDescriptionInput.errorMessage
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
							HabitDetailsViewAction.OnInputFieldStateChanged(
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
							HabitDetailsViewAction.OnInputFieldStateChanged(
								isNameFieldEmpty = binding.habitName.text.isNullOrEmpty(),
								isDescriptionFieldEmpty = habitDescription.isBlank()
							)
						)
					)
				}
			}
		})
	}

    private fun setupFrequencyPicker() {
		binding.frequencySummary.setOnClickListener{
			listDialog.selectedRecurrence = selectedRecurrence
			listDialog.startDate = startDate
			listDialog.show(childFragmentManager, "recurrence-list-dialog")
		}
	}

	private fun setupReminder() {
		// binding.reminderSummary.setOnClickListener {
		// 	recurentChoser.build().show(requireFragmentManager(), REMINDER_PICKER_DIALOG)
		// }
	}


	private fun setupToolbar() {
        with(toolbar) {
            setNavigationOnClickListener { dismiss() }
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
				HabitDetailsViewAction.OnSaveHabitClicked(
					Habit(
						name = binding.habitName.text.toString(),
						description = binding.habitDescription.text.toString(),
						frequency = Frequency(1,1),
						reminder = null
					)
				)
			)
		)
	}

    private fun initializeFullScreen() {
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)
    }

	private fun subscribeTo(completable: Completable) {
		completable.onErrorComplete()
			.subscribeOn(schedulerProvider.io())
			.subscribeBy(onError = Timber::e)
			.disposeBy(lifecycle.onStop)
	}

    companion object {
        const val ADD_NEW_HABIT_DIALOG_TAG = "AddNewHabitDialog"
        const val TIME_PICKER_DIALOG_TAG = "TimePickerDialog"
        const val REMINDER_PICKER_DIALOG = "ReminderPickerDialog"
        const val ARG_HABIT_ID = "HabitIdArg"
        fun show(habitId: String = "", fragmentManager: FragmentManager): HabitDetailsDialog =
            HabitDetailsDialog().apply {
				arguments = Bundle().apply {
					putString(ARG_HABIT_ID, habitId)
				}
				show(fragmentManager,
					ADD_NEW_HABIT_DIALOG_TAG
				)
			}
    }
}
