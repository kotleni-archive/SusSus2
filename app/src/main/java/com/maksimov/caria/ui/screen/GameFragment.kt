package com.maksimov.caria.ui.screen

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.maksimov.caria.R
import com.maksimov.caria.databinding.FragmentGameBinding
import com.maksimov.caria.ui.contracts.nav
import com.maksimov.caria.ui.utils.SpinningWheel
import com.maksimov.caria.ui.viewmodels.GameViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DecimalFormat
import kotlin.random.Random

class GameFragment : Fragment() {

    private val decimalFormat = DecimalFormat("###,###")
    private var spinEndCount = 0
    private var isSpinning = false

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw NullPointerException("FragmentGameBinding is null")

    private val items by lazy {
        ITEMS.mapIndexed { index, it ->
            MyItem(index, AppCompatResources.getDrawable(requireContext(), it)!!)
        }
    }

    private val viewModel: GameViewModel by viewModel()

    private fun randomItems() = (items + items).shuffled()
    private fun randomCount() = -Random.nextInt(160, 640)
    private fun randomDuration() = CONST_TIME.toLong() //+ Random.nextLong(0, (0.3f * CONST_TIME).toLong())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        getCoinsGame()
        return binding.root
    }

    private fun getCoinsGame() {
        binding.btnHome.setOnClickListener {
            activity?.let {
                spinEnd()
                nav()?.goToStartScreen()
            }
        }

        binding.btnInfo.setOnClickListener {
            AlertDialog.Builder(it.context)
                .setMessage(getString(R.string.rules))
                .create()
                .show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.betLiveData.observe(viewLifecycleOwner) {
            binding.tvBetCount.text = it.toString()
        }
        viewModel.balanceLiveData.observe(viewLifecycleOwner) {
            if (it >= 10000) {
                nav()?.goToResultScreen(true)
            } else {
                if (it < 0) {
                    nav()?.goToResultScreen(false)
                } else {
                    binding.tvScorePoints.text = decimalFormat.format(it)
                }
            }
        }
        binding.btnBetPlus.setOnClickListener { if (!isSpinning) viewModel.bet++ }
        binding.btnBetMinus.setOnClickListener { if (!isSpinning) viewModel.bet-- }
        binding.btnSpin.setOnClickListener {
            if (isSpinning) return@setOnClickListener
            binding.btnSpin.isEnabled = false
            isSpinning = true

            viewModel.balance -= viewModel.bet

            spinEndCount = 0
            binding.col1.spin(randomCount(), randomDuration()) { spinEnd() }
            binding.col2.spin(randomCount(), randomDuration()) { spinEnd() }
            binding.col3.spin(randomCount(), randomDuration()) { spinEnd() }
            binding.col4.spin(randomCount(), randomDuration()) { spinEnd() }
            binding.col5.spin(randomCount(), randomDuration()) { spinEnd() }
        }

        binding.col1.setState(randomItems(), 0)
        binding.col2.setState(randomItems(), 0)
        binding.col3.setState(randomItems(), 0)
        binding.col4.setState(randomItems(), 0)
        binding.col5.setState(randomItems(), 0)
    }

    private fun spinEnd() {
        if (++spinEndCount != 5) return

        val lineUp = listOf(
            (binding.col1.getUp() as MyItem).id,
            (binding.col2.getUp() as MyItem).id,
            (binding.col3.getUp() as MyItem).id,
            (binding.col4.getUp() as MyItem).id,
            (binding.col5.getUp() as MyItem).id,
        )

        val lineMaster = listOf(
            (binding.col1.getMaster() as MyItem).id,
            (binding.col2.getMaster() as MyItem).id,
            (binding.col3.getMaster() as MyItem).id,
            (binding.col4.getMaster() as MyItem).id,
            (binding.col5.getMaster() as MyItem).id
        )

        val lineDown = listOf(
            (binding.col1.getDown() as MyItem).id,
            (binding.col2.getDown() as MyItem).id,
            (binding.col3.getDown() as MyItem).id,
            (binding.col4.getDown() as MyItem).id,
            (binding.col5.getDown() as MyItem).id
        )

        viewModel.calculate(lineUp, lineMaster, lineDown)

        isSpinning = false
        binding.btnSpin.isEnabled = true
    }

    companion object {
        private const val CONST_TIME = 10_000
        private val ITEMS = listOf(
            R.drawable.item_1,
            R.drawable.item_2,
            R.drawable.item_3,
            R.drawable.item_4,
            R.drawable.item_5
        )

        @JvmStatic
        fun newInstance() = GameFragment()
    }

    data class MyItem(val id: Int, override val drawable: Drawable) : SpinningWheel.Item
}