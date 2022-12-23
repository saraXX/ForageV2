package android.guide.foragev2.ui

import android.guide.foragev2.BaseApplication
import android.guide.foragev2.R
import android.guide.foragev2.databinding.FragmentAddForageableBinding
import android.guide.foragev2.model.Forageable
import android.guide.foragev2.ui.viewmodel.ForageableViewModel
import android.guide.foragev2.ui.viewmodel.ForageableViewModelFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs


class AddForageableFragment : Fragment() {
    val TAG = "AddForageableFragment"
    val navigationArgs: AddForageableFragmentArgs by navArgs()
    private val viewModel: ForageableViewModel by activityViewModels {
        ForageableViewModelFactory(
            (activity?.application as BaseApplication).database.forageableDao()
        )
    }

    private var _binding: FragmentAddForageableBinding? = null
    private val binding get() = _binding!!

    private lateinit var forageable: Forageable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddForageableBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.id
        if (id > 0) {

            // TODO: Observe a Forageable that is retrieved by id, set the forageable variable,
            //  and call the bindForageable method
            viewModel.getForageableById(id).observe(this.viewLifecycleOwner){ forage ->
                forageable = forage
                bindForageable(forageable)
            }

            binding.deleteBtn.visibility = View.VISIBLE
            binding.deleteBtn.setOnClickListener {
                deleteForageable(forageable)
            }
        } else {
            binding.saveBtn.setOnClickListener {
                addForageable()
            }
        }
    }

    private fun deleteForageable(forageable: Forageable) {
        viewModel.deleteForageable(forageable)
        findNavController().navigate(
            R.id.action_addForageableFragment_to_forageableListFragment
        )
    }

    private fun addForageable() {
        if (isValidEntry()) {
            viewModel.addForageable(
                binding.nameInput.text.toString(),
                binding.locationAddressInput.text.toString(),
                binding.inSeasonCheckbox.isChecked,
                binding.notesInput.text.toString()
            )
            findNavController().navigate(
                R.id.action_addForageableFragment_to_forageableListFragment
            )
        }
    }

    private fun updateForageable() {
        if (isValidEntry()) {
            viewModel.updateForageable(
                id = navigationArgs.id,
                name = binding.nameInput.text.toString(),
                address = binding.locationAddressInput.text.toString(),
                inSeason = binding.inSeasonCheckbox.isChecked,
                note = binding.notesInput.text.toString()
            )
            findNavController().navigate(
                R.id.action_addForageableFragment_to_forageableListFragment
            )
        }
    }

    private fun bindForageable(forageable: Forageable) {
        binding.apply {
            nameInput.setText(forageable.name, TextView.BufferType.SPANNABLE)
            locationAddressInput.setText(forageable.address, TextView.BufferType.SPANNABLE)
            inSeasonCheckbox.isChecked = forageable.inSeason
            notesInput.setText(forageable.notes, TextView.BufferType.SPANNABLE)
            saveBtn.setOnClickListener {
                updateForageable()
            }
        }
    }

    private fun isValidEntry() = viewModel.isValidEntry(
        binding.nameInput.text.toString(),
        binding.locationAddressInput.text.toString()
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}