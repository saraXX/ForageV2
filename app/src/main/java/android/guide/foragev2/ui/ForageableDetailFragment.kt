package android.guide.foragev2.ui

import android.guide.foragev2.BaseApplication
import android.guide.foragev2.R
import android.guide.foragev2.databinding.FragmentForageableDetailBinding
import android.guide.foragev2.databinding.FragmentForageableListBinding
import android.guide.foragev2.model.Forageable
import android.guide.foragev2.ui.viewmodel.ForageableViewModel
import android.guide.foragev2.ui.viewmodel.ForageableViewModelFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

class ForageableDetailFragment : Fragment() {
    val TAG = "ForageableDetailFragment"
    // get parameter from previous fragment
    private val navigationArgs: ForageableDetailFragmentArgs by navArgs()

    private val viewModel: ForageableViewModel by activityViewModels{
        ForageableViewModelFactory(
            (activity?.application as BaseApplication).database.forageableDao()
        )
    }

    private var _binding: FragmentForageableDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var forageable: Forageable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForageableDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.id
//        Observe a forageable that is retrieved by id, set the forageable variable,
        //  and call the bind forageable method
//        forage -> return from viewmodel
//        forageable -> the one created here
//        bindForageable -> is inside observe block , cuz it will updated when ever the observer change.
        viewModel.getForageableById(id).observe(this.viewLifecycleOwner){forage ->
            forageable = forage
            bindForageable()

        }
    }

    private fun bindForageable() {
        binding.apply {
            name.text = forageable.name
            location.text = forageable.address
            notes.text = forageable.notes
            if (forageable.inSeason) {
                season.text = getString(R.string.in_season)
            } else {
                season.text = getString(R.string.out_of_season)
            }
            editForageableFab.setOnClickListener {
                val action = ForageableDetailFragmentDirections
                    .actionForageableDetailFragmentToAddForageableFragment(forageable.id)
                findNavController().navigate(action)
            }
        }
    }

}