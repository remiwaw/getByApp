package com.rwawrzyniak.getby.habits.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rwawrzyniak.getby.core.SchedulerProvider
import com.rwawrzyniak.getby.dagger.fragmentScopedViewModel
import com.rwawrzyniak.getby.dagger.injector
import com.rwawrzyniak.getby.databinding.FragmentHabitDetailsBinding
import io.reactivex.Completable
import io.reactivex.rxkotlin.subscribeBy
import io.sellmair.disposer.disposeBy
import io.sellmair.disposer.onStop
import timber.log.Timber

class HabitDetailsFragment : Fragment() {
    private lateinit var binding: FragmentHabitDetailsBinding
    private val viewModel by fragmentScopedViewModel { injector.habitDetailsViewModel }
	private val schedulerProvider: SchedulerProvider by lazy { injector.provideSchedulerProvider() }
	private var isUserInput = true // TODO make it better, change to avoid executing listener on text changed.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentHabitDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
    }

	override fun onStart() {
		super.onStart()
		startObservers()
		arguments?.getString(ARG_HABIT_ID)?.let {
			subscribeTo(viewModel.onAction(HabitDetailsViewAction.LoadHabit(it)))
		}
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

	private fun executeEffect(effect: HabitDetailsViewEffect) {
	}

	private fun renderState(state: HabitDetailsViewState) {

	}

	private fun subscribeTo(completable: Completable) {
		completable.onErrorComplete()
			.subscribeOn(schedulerProvider.io())
			.subscribeBy(onError = Timber::e)
			.disposeBy(lifecycle.onStop)
	}

    companion object {
        const val ARG_HABIT_ID = "HabitIdArg"

        private const val CUSTOM_FREQUENCY_USED =  -1
        fun create(habitId: String = ""): HabitDetailsFragment =
            HabitDetailsFragment().apply {
				arguments = Bundle().apply {
					putString(ARG_HABIT_ID, habitId)
				}
			}
    }
}
