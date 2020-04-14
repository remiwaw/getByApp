package com.rwawrzyniak.getby.habits.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.rwawrzyniak.getby.R
import com.rwawrzyniak.getby.core.BaseFragment
import com.rwawrzyniak.getby.core.ChromeConfiguration
import com.rwawrzyniak.getby.core.SchedulerProvider
import com.rwawrzyniak.getby.dagger.fragmentScopedViewModel
import com.rwawrzyniak.getby.dagger.injector
import com.rwawrzyniak.getby.databinding.FragmentHabitDetailsBinding
import io.reactivex.Completable
import io.reactivex.rxkotlin.subscribeBy
import io.sellmair.disposer.disposeBy
import io.sellmair.disposer.onStop
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class HabitDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentHabitDetailsBinding
    private val viewModel by fragmentScopedViewModel { injector.habitDetailsViewModel }
	private val schedulerProvider: SchedulerProvider by lazy { injector.provideSchedulerProvider() }
	private var isUserInput = true // TODO make it better, change to avoid executing listener on text changed.
	private lateinit var habitId: String

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

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun onStart() {
		super.onStart()
		startObservers()
		habitId = requireNotNull(arguments?.getString(ARG_HABIT_ID))
	}

	private fun startObservers() {
		wireViewModel()
	}

	override fun getChromeConfig(): ChromeConfiguration {
		return ChromeConfiguration(
			showActionBar = true,
			actionBarTitle = getString(R.string.habitsDetailsTitle),
			showActionBarEditButton = true,
			showActionBarSaveButton = false
		)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when(item.itemId){
			R.id.action_edit -> nav_host.findNavController().navigate(
				R.id.action_habitDetailsFragment_to_habitCreateUpdateFragment,
				Bundle().apply {
					putString(ARG_HABIT_ID ,habitId)
				}
			)
		}
		return super.onOptionsItemSelected(item)
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
