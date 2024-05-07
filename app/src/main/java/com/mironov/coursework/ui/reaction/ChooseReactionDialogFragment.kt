package com.mironov.coursework.ui.reaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mironov.coursework.data.reactionList
import com.mironov.coursework.databinding.FragmentChooseReactionBinding
import com.mironov.coursework.ui.view.EmojiView

class ChooseReactionDialogFragment : BottomSheetDialogFragment() {

    var onEmojiClickedCallback: ((Long, String) -> Unit)? = null

    companion object {

        fun newInstance(messageId: Long) = ChooseReactionDialogFragment().apply {
            arguments = Bundle().apply {
                putLong(MESSAGE_ID, messageId)
            }
        }

        private const val MESSAGE_ID = "message id"
        private const val UNDEFINED_ID = -1L

        const val TAG = "ChooseReactionDialogFragment"
    }

    private var messageId = UNDEFINED_ID

    private var _binding: FragmentChooseReactionBinding? = null
    private val binding: FragmentChooseReactionBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseReactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (dialog as? BottomSheetDialog)?.behavior?.apply {
            state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        setupScreen()
        setClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArguments() {
        val args = requireArguments()
        messageId = args.getLong(MESSAGE_ID)
    }

    private fun setClickListener() {
        binding.flexBox.setOnReactionsClickListeners {
            onEmojiClickedCallback?.invoke(messageId, it)
            dismiss()
        }
    }

    private fun setupScreen() {
        binding.flexBox.iconAdd.visibility = View.INVISIBLE
        reactionList.forEach { (key, value) ->
            val emojiView = EmojiView(requireContext()).apply {
                emoji = key
                reactionsCount = 0
                isSelected = false
                emojiTextSize = 40f
                name = value
            }
            binding.flexBox.addView(emojiView)
        }
    }
}