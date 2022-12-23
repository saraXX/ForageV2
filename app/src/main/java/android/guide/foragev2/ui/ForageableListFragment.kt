package android.guide.foragev2.ui

import android.guide.foragev2.BaseApplication
import android.guide.foragev2.R
import android.guide.foragev2.databinding.FragmentForageableListBinding
import android.guide.foragev2.model.Forageable
import android.guide.foragev2.ui.adapter.ForageableListAdapter
import android.guide.foragev2.ui.viewmodel.ForageableViewModel
import android.guide.foragev2.ui.viewmodel.ForageableViewModelFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kotlin.math.log


class ForageableListFragment : Fragment() {

    val TAG = "ForageableListFragment"

    private val viewModel: ForageableViewModel by activityViewModels{
        ForageableViewModelFactory(
            (activity?.application as BaseApplication).database.forageableDao()
        )
    }


    private var _binding: FragmentForageableListBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentForageableListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ForageableListAdapter{ forageable ->
            val action = ForageableListFragmentDirections
                .actionForageableListFragmentToForageableDetailFragment(forageable.id)
            findNavController().navigate(action)
        }

//         any update in forgeableList will be automatically shown in adapter
        /**
         * forage = viewModel.foragableList
         * it = forage
         * which all of these are List<Forageable>
         * */
        viewModel.foragableList.observe(this.viewLifecycleOwner){ forage ->
            forage.let {
                adapter.submitList(it)
            }
        }
        binding.apply {
            recyclerView.adapter = adapter
            addForageableFab.setOnClickListener {
                findNavController().navigate(R.id.action_forageableListFragment_to_addForageableFragment)
            }
        }
    }
}